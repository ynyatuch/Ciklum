package com.fedakivan;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DatabaseSource {

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDERS_ID = "id";
    public static final String COLUMN_ORDERS_USER_ID = "user_id";
    public static final String COLUMN_ORDERS_STATUS = "status";
    public static final String COLUMN_ORDERS_CREATED_AT = "created_at";

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCTS_ID = "id";
    public static final String COLUMN_PRODUCTS_NAME = "name";
    public static final String COLUMN_PRODUCTS_PRICE = "price";
    public static final String COLUMN_PRODUCTS_STATUS = "status";
    public static final String COLUMN_PRODUCTS_CREATED_AT = "created_at";

    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COLUMN_ORDER_ITEMS_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_ITEMS_PRODUCT_ID = "product_id";
    public static final String COLUMN_ORDER_ITEMS_QUANTITY = "quantity";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS ";

    private Connection conn;

    public Connection getConn() {
        return conn;
    }

    private Connection getConnection() throws SQLException, IOException {

        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("local.properties"))){
            props.load(in);
        } catch (Exception e){
            System.out.println("Something went wrong: " + e);
            return null;
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, username, password);
    }

    private boolean createTables(){
        try {
            Statement statement = conn.createStatement();
            statement.execute(CREATE_TABLE + TABLE_PRODUCTS + " (" + COLUMN_PRODUCTS_ID + " INTEGER NOT NULL AUTO_INCREMENT, "+
                   COLUMN_PRODUCTS_NAME + " VARCHAR(20), " + COLUMN_PRODUCTS_PRICE + " INTEGER, " +
                    COLUMN_PRODUCTS_STATUS + " ENUM('out_of_stock', 'in_stock', 'running_low'), " +
                    COLUMN_PRODUCTS_CREATED_AT + " DATETIME, PRIMARY KEY (" + COLUMN_PRODUCTS_ID + "))");
            statement.execute(CREATE_TABLE + TABLE_ORDERS + " (" + COLUMN_ORDERS_ID  + " INTEGER, " +
                    COLUMN_ORDERS_USER_ID + " INTEGER NOT NULL AUTO_INCREMENT, " + COLUMN_ORDERS_STATUS + " VARCHAR(20), " +
                    COLUMN_ORDERS_CREATED_AT + " VARCHAR(20), PRIMARY KEY (" + COLUMN_ORDERS_USER_ID +"))");
            statement.execute(CREATE_TABLE + TABLE_ORDER_ITEMS + " (" + COLUMN_ORDER_ITEMS_ORDER_ID + " INTEGER, " +
                    COLUMN_ORDER_ITEMS_PRODUCT_ID + " INTEGER, " + COLUMN_ORDER_ITEMS_QUANTITY + " INTEGER)");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't create tables: " + e);
            return false;
        }
    }

    private boolean createDatabase(String databaseName){
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(CREATE_DATABASE + databaseName);
            statement.execute("USE " + databaseName);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't create database: " + e);
            return false;
        }
    }

    public boolean open(String databaseName) {
        try {
            conn = getConnection();
            if (!createDatabase(databaseName)){
                return false;
            }
            return createTables();
        } catch (SQLException | IOException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close(){
        try{
            if (conn != null ){
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection");
        }
    }
}
