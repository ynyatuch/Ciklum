package com.test;

import com.fedakivan.DatabaseSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class DatabaseSourceTest {
    public static DatabaseSource database;
    public static final String DATABASE = "test";
    @BeforeClass
    public static void beforeClass() throws Exception {
        database = new DatabaseSource();
    }

    @Test
    public void open() {

        database.open(DATABASE);
        assertTrue(database.getConn() != null);
        database.close();
    }

    @Test(expected = SQLException.class)
    public void close() throws SQLException {
        DatabaseSource database = new DatabaseSource();
        database.open(DATABASE);
        assertTrue(database.getConn() != null);
        database.close();
        Statement statement = database.getConn().createStatement();
        statement.executeQuery("select * from products");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        database.close();
    }
}