package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    
    // Cấu hình Database
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "quanlythuvien_mermaid"; // Đảm bảo tên DB đúng
    private static final String USER = "root";
    private static final String PASS = ""; 
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Đảm bảo bạn đã thêm thư viện mysql-connector-j-8.x.x.jar vào Libraries
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy Driver MySQL! Vui lòng add thư viện .jar");
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}