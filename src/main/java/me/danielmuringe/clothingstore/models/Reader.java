package me.danielmuringe.clothingstore.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    public static String getDatum(String query, String column) {
        Connection conn = null;
        String result = "";

        try {
            // db parameters
            String url = "jdbc:sqlite:src/main/resources/data/base.sqlite3";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            // Your code to read data from the database goes here
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            result = resultSet.getString(column);

            return result;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            return result;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static List<Integer> getDataInt(String table, String column) {
        Connection conn = null;
        List<Integer> data = new ArrayList<>();

        try {
            // db parameters
            String url = "jdbc:sqlite:src/main/resources/data/base.sqlite3";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            // Your code to read data from the database goes here
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + column + " FROM " + table);
            while (resultSet.next()) {
                data.add(resultSet.getInt(column));
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return data;
    }

    public static List<String> getDataString(String table, String column) {
        Connection conn = null;
        List<String> data = new ArrayList<>();

        try {
            // db parameters
            String url = "jdbc:sqlite:src/main/resources/data/base.sqlite3";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            // Your code to read data from the database goes here
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + column + " FROM " + table);
            while (resultSet.next()) {
                data.add(resultSet.getString(column));
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return data;
    }

    public static void nullExecute(String query) {
        Connection conn = null;

        try {
            // db parameters
            String url = "jdbc:sqlite:src/main/resources/data/base.sqlite3";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            // Your code to read data from the database goes here
            Statement statement = conn.createStatement();
            statement.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}