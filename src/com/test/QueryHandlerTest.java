package com.test;

import com.fedakivan.DatabaseSource;
import com.fedakivan.QueryHandler;
import com.fedakivan.product_status;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryHandlerTest {
    public static final String DATABASE = "test";
    public static DatabaseSource database;
    public static QueryHandler queryHandler;
    public static Connection conn;

    @BeforeClass
    public static void beforeClass() throws Exception {
        database = new DatabaseSource();
        database.open(DATABASE);
        conn = database.getConn();
        queryHandler = new QueryHandler(conn);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        try {
            System.out.println("Clear database");
            Statement statement = conn.createStatement();
            statement.executeUpdate("delete from orders");
            statement.executeUpdate("delete from products");
            statement.executeUpdate("delete from order_items");
            statement.executeUpdate("drop database test");
            database.close();
        } catch (SQLException e) {
            System.out.println("Database clearing failed: " + e);
            e.printStackTrace();
        }
    }

    @Test
    public void addProduct() {
        int rowsCount = 0;
        queryHandler.addProduct(8, "test", 34, product_status.in_stock);
        assertTrue(queryHandler.checkIfProductIsPresent(8));
        queryHandler.removeProduct(8);
    }

    @Test
    public void addOrder() {
        int rowsCount = 0;
        List<Integer> list = new ArrayList<>();
        list.add(6);
        queryHandler.addOrder(8, "test", list);
        try {
            Statement statement = conn.createStatement();
            rowsCount = statement.executeUpdate("delete from orders where id = 8");
        } catch (SQLException e) {
            System.out.println("Test case failed: " + e);
            e.printStackTrace();
        }
        assertEquals(1, rowsCount);

    }

    @Test
    public void checkIfProductIsPresent() {
        queryHandler.addProduct(8, "test", 34, product_status.in_stock);
        assertTrue(queryHandler.checkIfProductIsPresent(8));
        queryHandler.removeProduct(8);
        assertTrue(!queryHandler.checkIfProductIsPresent(8));
    }

    @Test
    public void updateQuantity() {
        queryHandler.addProduct(8, "test", 34, product_status.in_stock);
        List<Integer> list = new ArrayList<>();
        list.add(8);
        int quantity = 0;
        queryHandler.addOrder(8, "test", list);
        queryHandler.updateQuantity(8, 8, 8);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select quantity from order_items where product_id = 8 and order_id = 8");
            while (resultSet.next()){
                quantity = resultSet.getInt("quantity");
            }

        } catch (SQLException e) {
            System.out.println("Test case failed: " + e);
            e.printStackTrace();
        }
        assertEquals(8, quantity);
        queryHandler.removeProduct(8);
        try {
            Statement statement = conn.createStatement();
            statement.execute("delete from orders where id = 8");
        } catch (SQLException e) {
            System.out.println("Order delete failed, please, clear table manually: " + e);
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfItemIsPresent() {
        queryHandler.addProduct(8, "test", 34, product_status.in_stock);
        List<Integer> list = new ArrayList<>();
        list.add(8);
        queryHandler.addOrder(8, "test", list);
        assertTrue(queryHandler.checkIfItemIsPresent(8, 8));
        queryHandler.removeProduct(8);
    }

    @Test
    public void removeProduct() {
        queryHandler.addProduct(8, "test", 34, product_status.in_stock);
        assertTrue(queryHandler.checkIfProductIsPresent(8));
        queryHandler.removeProduct(8);
        assertTrue(!queryHandler.checkIfProductIsPresent(8));
    }
}