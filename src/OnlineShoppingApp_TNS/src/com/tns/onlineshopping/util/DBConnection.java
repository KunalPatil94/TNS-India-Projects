package OnlineShoppingApp_TNS.src.com.tns.onlineshopping.util;

//===== com/tns/onlineshopping/util/DBConnection.java =====


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
 private static final String URL = "jdbc:mysql://localhost:3306/OnlineShoppingDB?useSSL=false&serverTimezone=UTC";
 private static final String USER = "root"; // <-- set your DB user
 private static final String PASS = "krp9904"; // <-- set your DB password

 static {
     try {
         Class.forName("com.mysql.cj.jdbc.Driver");
     } catch (ClassNotFoundException e) {
         e.printStackTrace();
     }
 }

 public static Connection getConnection() throws SQLException {
     return DriverManager.getConnection(URL, USER, PASS);
 }
}
