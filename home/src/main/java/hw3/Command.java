package hw3;

import java.util.Objects;

public class Command {

    private CommandType commandId;
    private String userId;
    private double commandBody;
    private String productId;

    public Command(CommandType commandId, String userId, double commandBody, String productId) {
        this.commandId = commandId;
        this.userId = userId;
        this.commandBody = commandBody;
        this.productId = productId;
    }
    public Command() {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCommandId(CommandType commandId) {
        this.commandId = commandId;
    }

    public void setCommandBody(double commandBody) {
        this.commandBody = commandBody;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CommandType getCommandId() {
        return commandId;
    }

    public double getCommandBody() {
        return commandBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Double.compare(command.commandBody, commandBody) == 0 && commandId == command.commandId && Objects.equals(userId, command.userId) && Objects.equals(productId, command.productId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(commandId, userId);
        result = (int) (31 * result + commandBody);
        return result;
    }

    @Override
    public String toString() {
        return "hw2.Command{" +
                "commandId=" + commandId +
                ", userId='" + userId + '\'' +
                ", commandBody=" + commandBody +
                ", productId='" + productId + '\'' +
                '}';
    }
}
