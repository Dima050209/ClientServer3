package pr4;

public class SqlBuilder {
    public static String gte(Double value, String column){
        return value == null ? null : column + " >= " + value;
    }
    public static String lte(Double value, String column){
        return value == null ? null : column + " <= " + value;
    }
    public static String startWith(String value, String column){
        return value == null ? null : column + " like '" + value + "%'";
    }
    public static String endWith(String value, String column){
        return value == null ? null : column + " like '%" + value + "'";
    }
}
