package com.fedakivan;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class QueryHandler {

    public static final String QUERY_ADD_PRODUCT = "INSERT INTO " + DatabaseSource.TABLE_PRODUCTS + "(" + DatabaseSource.COLUMN_PRODUCTS_ID +
            ", " + DatabaseSource.COLUMN_PRODUCTS_NAME +
            ", " + DatabaseSource.COLUMN_PRODUCTS_PRICE + ", " + DatabaseSource.COLUMN_PRODUCTS_STATUS + ", " +
            DatabaseSource.COLUMN_PRODUCTS_CREATED_AT + ")" + " VALUES (?,?,?,?,?)";

    public static final String QUERY_ADD_ORDER = "INSERT INTO " + DatabaseSource.TABLE_ORDERS + "(" + DatabaseSource.COLUMN_ORDERS_ID + ", "
            + DatabaseSource.COLUMN_ORDERS_STATUS + ", " + DatabaseSource.COLUMN_ORDERS_CREATED_AT + ")" + " VALUES (?,?,?)";

    public static final String QUERY_ADD_ORDER_ITEM = "INSERT INTO " + DatabaseSource.TABLE_ORDER_ITEMS + "(" + DatabaseSource.COLUMN_ORDER_ITEMS_ORDER_ID +
            ", " + DatabaseSource.COLUMN_ORDER_ITEMS_PRODUCT_ID + ", " + DatabaseSource.COLUMN_ORDER_ITEMS_QUANTITY + ")" + " VALUES (?,?,?)";

    public static final String QUERY_SHOW_VIEW = "SELECT * FROM ";
    public static final String VIEW_ALL_PRODUCTS = "all_products";
    public static final String QUERY_CREATE_VIEW_ALL_PRODUCTS = "CREATE OR REPLACE VIEW  " + VIEW_ALL_PRODUCTS + " AS SELECT " +
            DatabaseSource.COLUMN_PRODUCTS_NAME + ", " + DatabaseSource.COLUMN_PRODUCTS_PRICE + ", " +
            DatabaseSource.COLUMN_PRODUCTS_STATUS + " FROM " + DatabaseSource.TABLE_PRODUCTS;

    public static final String VIEW_SPECIFIC_VIEW_1 = "specific_view_1";
    public static final String QUERY_CREATE_VIEW_SPECIFIC_VIEW_1 = "CREATE OR REPLACE VIEW " + VIEW_SPECIFIC_VIEW_1 + " AS SELECT " +
            DatabaseSource.TABLE_PRODUCTS + '.' + DatabaseSource.COLUMN_PRODUCTS_NAME + ", " + DatabaseSource.TABLE_ORDER_ITEMS + '.' +
            DatabaseSource.COLUMN_ORDER_ITEMS_QUANTITY + " FROM " + DatabaseSource.TABLE_PRODUCTS + " INNER JOIN " +
            DatabaseSource.TABLE_ORDER_ITEMS + " ON " + DatabaseSource.TABLE_PRODUCTS + '.' + DatabaseSource.COLUMN_PRODUCTS_ID + " = " +
            DatabaseSource.TABLE_ORDER_ITEMS + '.' + DatabaseSource.COLUMN_ORDER_ITEMS_PRODUCT_ID + " ORDER BY " +
            DatabaseSource.TABLE_ORDER_ITEMS + '.' + DatabaseSource.COLUMN_ORDER_ITEMS_QUANTITY + " DESC";

    public static final String VIEW_SPECIFIC_VIEW_2 = "specific_view_2";
    public static final String QUERY_CREATE_VIEW_SPECIFIC_VIEW_2 = "CREATE OR REPLACE VIEW " + VIEW_SPECIFIC_VIEW_2 + " AS SELECT " +
            DatabaseSource.TABLE_ORDERS + '.' + DatabaseSource.COLUMN_ORDERS_ID + ", " + DatabaseSource.TABLE_ORDER_ITEMS + '.' +
            DatabaseSource.COLUMN_ORDER_ITEMS_QUANTITY + "*" + DatabaseSource.TABLE_PRODUCTS + '.' + DatabaseSource.COLUMN_PRODUCTS_PRICE +
            " AS total_price, " + DatabaseSource.TABLE_PRODUCTS + '.' + DatabaseSource.COLUMN_PRODUCTS_NAME + ", " +
            DatabaseSource.TABLE_ORDER_ITEMS + '.' + DatabaseSource.COLUMN_ORDER_ITEMS_QUANTITY + ", " +
            DatabaseSource.TABLE_ORDERS + '.' + DatabaseSource.COLUMN_ORDERS_CREATED_AT + " FROM " + DatabaseSource.TABLE_ORDER_ITEMS +
            " INNER JOIN " + DatabaseSource.TABLE_ORDERS + " ON " + DatabaseSource.TABLE_ORDERS + '.' + DatabaseSource.COLUMN_ORDERS_ID +
            " = " + DatabaseSource.TABLE_ORDER_ITEMS + '.' + DatabaseSource.COLUMN_ORDER_ITEMS_ORDER_ID + " INNER JOIN " +
            DatabaseSource.TABLE_PRODUCTS + " ON " + DatabaseSource.TABLE_PRODUCTS + '.' + DatabaseSource.COLUMN_PRODUCTS_ID +
            " = " + DatabaseSource.TABLE_ORDER_ITEMS + '.' + DatabaseSource.COLUMN_ORDER_ITEMS_PRODUCT_ID + " ORDER BY " +
            DatabaseSource.TABLE_ORDERS + '.' + DatabaseSource.COLUMN_ORDERS_ID;

    public static final String LIST_ALL_ORDERS = "SELECT " + DatabaseSource.COLUMN_ORDERS_ID + ", " +
            DatabaseSource.COLUMN_ORDERS_CREATED_AT + " FROM " + VIEW_SPECIFIC_VIEW_2;

    public static final String QUERY_REMOVE_PRODUCT_BY_ID = "DELETE FROM " + DatabaseSource.TABLE_PRODUCTS + " WHERE "
            + DatabaseSource.COLUMN_PRODUCTS_ID + " = ?";
    public static final String QUERY_REMOVE_ALL_PRODUCTS = "DELETE FROM " + DatabaseSource.TABLE_PRODUCTS;

    public static final String QUERY_CHECK_PRODUCT = "SELECT " + DatabaseSource.COLUMN_PRODUCTS_STATUS + " FROM " +
            DatabaseSource.TABLE_PRODUCTS + " WHERE " + DatabaseSource.COLUMN_PRODUCTS_ID + " = ?";

    public static final String QUERY_UPDATE_QUANTITY = "UPDATE " + DatabaseSource.TABLE_ORDER_ITEMS + " SET " + DatabaseSource.COLUMN_ORDER_ITEMS_QUANTITY +
            " = ? WHERE " + DatabaseSource.COLUMN_ORDER_ITEMS_ORDER_ID + " = ? AND " + DatabaseSource.COLUMN_ORDER_ITEMS_PRODUCT_ID +
            " = ?";

    public static final String QUERY_CHECK_ITEM = "SELECT " + DatabaseSource.COLUMN_ORDER_ITEMS_PRODUCT_ID + ", " +
            DatabaseSource.COLUMN_ORDER_ITEMS_ORDER_ID + " FROM " +
            DatabaseSource.TABLE_ORDER_ITEMS + " WHERE " + DatabaseSource.COLUMN_ORDER_ITEMS_PRODUCT_ID + " = ? AND " +
            DatabaseSource.COLUMN_ORDER_ITEMS_ORDER_ID + " = ?";

    private Connection conn;
    private PreparedStatement insertIntoOrders;
    private PreparedStatement insertIntoOrdersItems;
    private PreparedStatement removeProductById;
    private PreparedStatement checkProduct;
    private PreparedStatement updateQuantity;
    private PreparedStatement checkItem;

    public QueryHandler(Connection conn) {
        this.conn = conn;
    }

    public boolean addProduct(int id, String name, int price, product_status product_status) {
        try {
            conn.setAutoCommit(false);
            PreparedStatement insertIntoProducts = conn.prepareStatement(QUERY_ADD_PRODUCT, Statement.RETURN_GENERATED_KEYS);
            insertIntoProducts.setInt(1, id);
            insertIntoProducts.setString(2, name);
            insertIntoProducts.setInt(3, price);
            insertIntoProducts.setString(4, String.valueOf(product_status));

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());

            insertIntoProducts.setString(5, String.valueOf(dateFormat.format(date)));
            int affectedRows = insertIntoProducts.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("Couldn't insert product");
            }
        } catch (SQLException e) {
            System.out.println("Insert product exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
            return false;
        }
        System.out.println("Product added successfully");
        return true;
    }

    public boolean addOrder(int orderId, String status, List<Integer> productIDs) {
        try {
            conn.setAutoCommit(false);
            insertIntoOrders = conn.prepareStatement(QUERY_ADD_ORDER, Statement.RETURN_GENERATED_KEYS);
            insertIntoOrders.setInt(1, orderId);
            insertIntoOrders.setString(2, status);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());

            insertIntoOrders.setString(3, String.valueOf(dateFormat.format(date)));
            int affectedRows = insertIntoOrders.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
                addOrderItem(orderId, productIDs);
            } else {
                throw new SQLException("Couldn't insert order");
            }

        } catch (SQLException e) {
            System.out.println("Insert orders exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
            return false;
        }
        System.out.println("Order added successfully");
        return true;
    }

    private boolean addOrderItem(int orderId, List<Integer> productIds) {
        for (int productId : productIds) {
            try {
                conn.setAutoCommit(false);
                insertIntoOrdersItems = conn.prepareStatement(QUERY_ADD_ORDER_ITEM, Statement.RETURN_GENERATED_KEYS);
                insertIntoOrdersItems.setInt(1, orderId);
                insertIntoOrdersItems.setInt(2, productId);
                insertIntoOrdersItems.setInt(3, 1);
                int affectedRows = insertIntoOrdersItems.executeUpdate();
                if (affectedRows == 1) {
                    conn.commit();
                } else {
                    throw new SQLException("Couldn't insert order item");
                }
            } catch (SQLException e) {
                System.out.println("Insert order item exception: " + e.getMessage());
                try {
                    System.out.println("Performing rollback");
                    conn.rollback();
                } catch (SQLException e2) {
                    System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                }
                return false;
            }
        }
        return true;
    }

    public boolean checkIfProductIsPresent(int productId) {
        try {
            checkProduct = conn.prepareStatement(QUERY_CHECK_PRODUCT);
            checkProduct.setInt(1, productId);
            ResultSet result = checkProduct.executeQuery();
            if (!result.next()) {
                System.out.println("No such products present");
                return false;
            } else if (result.getString(1).equals(product_status.out_of_stock.toString())) {
                System.out.println("Product is out of stock");
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Product check exception: " + e.getMessage());
            return false;
        }
    }

    public boolean updateQuantity(int orderID, int productID, int newQuantity) {
        if (checkIfItemIsPresent(orderID, productID)) {
            try {
                conn.setAutoCommit(false);
                updateQuantity = conn.prepareStatement(QUERY_UPDATE_QUANTITY, Statement.RETURN_GENERATED_KEYS);
                updateQuantity.setInt(1, newQuantity);
                updateQuantity.setInt(2, productID);
                updateQuantity.setInt(3, orderID);
                System.out.println(updateQuantity);
                int affectedRows = updateQuantity.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                } else {
                    throw new SQLException("Couldn't update quantity");
                }
            } catch (SQLException e) {
                System.out.println("Quantity update exception: " + e.getMessage());
                try {
                    System.out.println("Performing rollback");
                    conn.rollback();
                } catch (SQLException e2) {
                    System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                }
                return false;
            }
            System.out.println("Quantity successfully updated");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfItemIsPresent(int productId, int orderId) {
        try {
            checkItem = conn.prepareStatement(QUERY_CHECK_ITEM);
            checkItem.setInt(1, productId);
            checkItem.setInt(2, orderId);
            ResultSet result = checkItem.executeQuery();
            if (!result.next()) {
                System.out.println("No such item is present");
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Product check exception: " + e.getMessage());
            return false;
        }
    }

    public boolean showView(int view) {
        String sqlQueryCreate;
        String sqlShowView = QUERY_SHOW_VIEW;
        switch (view) {
            case 1:
                sqlQueryCreate = QUERY_CREATE_VIEW_ALL_PRODUCTS;
                sqlShowView += VIEW_ALL_PRODUCTS;
                break;
            case 2:
                sqlQueryCreate = QUERY_CREATE_VIEW_SPECIFIC_VIEW_1;
                sqlShowView += VIEW_SPECIFIC_VIEW_1;
                break;
            case 3:
                sqlQueryCreate = QUERY_CREATE_VIEW_SPECIFIC_VIEW_2;
                sqlShowView += VIEW_SPECIFIC_VIEW_2;
                break;
            case 4:
                sqlQueryCreate = QUERY_CREATE_VIEW_SPECIFIC_VIEW_2;
                sqlShowView = LIST_ALL_ORDERS;
                break;
            default:
                System.out.println("Unexpected variant");
                return false;
        }
        try {
            System.out.println(sqlQueryCreate);
            Statement statement = conn.createStatement();
            statement.execute(sqlQueryCreate);
        } catch (SQLException e) {
            System.out.println("View creation failed: " + e.getMessage());
            return false;
        }
        try {
            System.out.println(sqlShowView);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlShowView);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                System.out.print(resultSetMetaData.getColumnName(i) + "  |   ");
            }
            System.out.print("\n");
            while (resultSet.next()) {
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " ");
                }
                System.out.println("");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Showing of view failed: " + e.getMessage());
            return false;
        }
    }

    public boolean removeProduct(int productID) {
        try {
            conn.setAutoCommit(false);
            removeProductById = conn.prepareStatement(QUERY_REMOVE_PRODUCT_BY_ID);
            removeProductById.setInt(1, productID);
            removeProductById.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Product removing exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
            return false;
        }
        System.out.println("Product #" + productID + " removed from database");
        return true;
    }

    public boolean removeAllProducts(String password) {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("local.properties"))) {
            props.load(in);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
            return false;
        }
        String pass = props.getProperty("password");
        if (!password.equals(pass)){
            System.out.println("Wrong password!");
            return false;
        }
        try {
            Statement statement = conn.createStatement();
            statement.execute(QUERY_REMOVE_ALL_PRODUCTS);
        } catch (SQLException e) {
            System.out.println("Removing failed: " + e);
        }
        System.out.println("All products were removed");
        return true;
    }

}
