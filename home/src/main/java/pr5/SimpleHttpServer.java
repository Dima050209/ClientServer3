package pr5;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import pr4.Db;
import pr4.Product;
import pr4.ProductFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleHttpServer {

    public final static Db db = new Db("products.db");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8765), 0);

        HttpContext context = server.createContext("/", new EchoHandler());
        //context.setAuthenticator(new Auth());

        server.setExecutor(null);
        server.start();
    }

    static class EchoHandler implements HttpHandler {
        private final List<EndpointHandler> handlers = List.of(
                new EndpointHandler("/api/good/?", "GET",this::processGetAll),
                new EndpointHandler("/api/good/?", "PUT", this::processPut),
                new EndpointHandler("/api/good/(\\d+)", "GET",this::processGetById),
                new EndpointHandler("/api/good/(\\d+)", "DELETE", this::processDeleteById),
                new EndpointHandler("/api/good/(\\d+)", "POST", this::processUpdate),
                new EndpointHandler("/login", "POST", this::processLogin)
        );

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handlers.stream()
                    .filter(endpointHandler -> endpointHandler.isMatch(exchange))
                    .findFirst()
                    .ifPresentOrElse(endpointHandler -> endpointHandler.handle(exchange), processUnknownEndpoint(exchange));
        }

        private Runnable processUnknownEndpoint(HttpExchange exchange) {
            return () -> {
                String result = "Error";
                byte[] data = result.getBytes();
                try {
                    exchange.sendResponseHeaders(404,data.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(data);
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
        }
        private void  process(HttpExchange exchange, Object content, int code){
            try {
                byte[] data = OBJECT_MAPPER.writeValueAsBytes(content);
                exchange.sendResponseHeaders(code, data.length);
                OutputStream os = exchange.getResponseBody();
                os.write(data);
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void processGetAll(HttpExchange exchange){
            List<Product> products = db.getProducts();
            process(exchange, products, 200);
        }
        private void processGetById(HttpExchange exchange){
            String idStr = exchange.getRequestURI().getPath().replace("/api/good/","");
            int id = Integer.parseInt(idStr);
            Product product = db.getProductById(id);
            process(exchange, product, 200);
        }
        private void processPut(HttpExchange exchange){
            InputStream stream = exchange.getRequestBody();
            try {
                sProduct p = OBJECT_MAPPER.readValue(stream, sProduct.class);
                if(!p.name.isEmpty() && p.price > 0 && !p.factoryName.isEmpty()) {
                    Product product = new Product(p.name, p.price, p.factoryName);
                    db.createProduct(product);
                    process(exchange, product, 200);
                }
                else {
                    process(exchange,"Error2", 401);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void processUpdate(HttpExchange exchange){
            String idStr = exchange.getRequestURI().getPath().replace("/api/good/","");
            int id = Integer.parseInt(idStr);
            InputStream stream = exchange.getRequestBody();
            try {
                sProduct p = OBJECT_MAPPER.readValue(stream, sProduct.class);
                if(!p.name.isEmpty() && p.price > 0 && !p.factoryName.isEmpty()) {
                    Product product = new Product(p.name, p.price, p.factoryName);
                    db.update(id, product);
                    process(exchange, product, 200);
                }
                else {
                    process(exchange,"Error2", 401);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void processDeleteById(HttpExchange exchange){
            String idStr = exchange.getRequestURI().getPath().replace("/api/good/","");
            int id = Integer.parseInt(idStr);
            db.delete(id);
            process(exchange, null, 204);
        }
        private void processLogin(HttpExchange exchange) {
            InputStream stream = exchange.getRequestBody();
            try {
                User user = OBJECT_MAPPER.readValue(stream, User.class);
                if (user.getLogin().equals("123") && user.getPassword().equals("456")){
                    String jwt = JWT.createJWT(user.login);
                    process(exchange, Map.of("token", jwt), 200);
                }
                else {
                    process(exchange,"Error", 401);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private static class EndpointHandler {
        private final String pathPattern, httpMethod;
        private final Consumer<HttpExchange> handler;
        public EndpointHandler(String pathPattern, String httpMethod, Consumer<HttpExchange> handler) {
            this.pathPattern = pathPattern;
            this.httpMethod = httpMethod;
            this.handler = handler;
        }

        public boolean isMatch(HttpExchange exchange){
            if(!exchange.getRequestMethod().equals(httpMethod)) return false;
            String path = exchange.getRequestURI().getPath();
            return path.matches(pathPattern);
        }
        public void handle(HttpExchange exchange){
            handler.accept(exchange);
        }
    }
    static class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            String path = httpExchange.getRequestURI().getPath();
            if(path.equals("/login")) return new Success(new HttpPrincipal("c0nst", "realm"));

            String jwt = httpExchange.getRequestHeaders().getFirst("jwt");
            if(jwt == null ) return new Failure(403);
            String login = JWT.extractUserName(jwt);
            if (!login.equals("123"))
                return new Failure(403);
            else
                return new Success(new HttpPrincipal(login, "USER_ROLE"));
        }
    }
    public static class User{
        private String login;
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    public static class sProduct{
        private String name;
        private double price;
        private String factoryName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getFactoryName() {
            return factoryName;
        }

        public void setFactoryName(String factoryName) {
            this.factoryName = factoryName;
        }
    }
}
