package dbTests;

import java.sql.*;

public class SQL {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String user = "postgres";
        String password = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM users");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
