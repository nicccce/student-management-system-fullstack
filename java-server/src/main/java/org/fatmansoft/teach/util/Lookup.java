package org.fatmansoft.teach.util;

import java.sql.*;

public class Lookup {
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException {
        String dbUrl = "jdbc:sqlite:/teach/java2/java.db";
        String user = "admin";
        String password = "admin";
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection(
                dbUrl, user, password);
        Statement s = c.createStatement();
        ResultSet r =
                s.executeQuery(
                        "SELECT num, name FROM person");
        while(r.next()) {
            System.out.println(
                    r.getString("num") + ", " + r.getString("name"));
        }
        r.close();
        s.close(); // Also closes ResultSet
        c.close();
    }
}


