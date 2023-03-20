package com.ps.crawler;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import org.json.JSONObject;

public class DataStorage {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crawler_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "prathmesh";

    public static void storeData(String seedUrl, String currentUrl, String html, String screenshot) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Connect to the MySQL database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Prepare the SQL statement to insert the data
            stmt = conn.prepareStatement("INSERT INTO page_data (seed_url, current_url, html, screenshot) VALUES (?, ?, ?, ?)");
            // Set the values of the parameters
            stmt.setString(1, seedUrl);
            stmt.setString(2, currentUrl);
            stmt.setString(3, html);
            stmt.setString(4, screenshot);
            // Execute the SQL statement
            stmt.executeUpdate();
        } finally {
            // Close the statement and the database connection
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    public static void writeDataToFile(String seedUrl, String currentUrl, String html, String screenshot) throws Exception {
        // Convert the screenshot to a Base64-encoded string
        //String base64Image = Base64.getEncoder().encodeToString(screenshot);
        // Create a new JSON object with the data
        JSONObject obj = new JSONObject();
        obj.put("seed_url", seedUrl);
        obj.put("current_url", currentUrl);
        obj.put("html", html);
        obj.put("base64_image", screenshot);
        // Write the JSON object to a file
        FileWriter writer = new FileWriter("D:\\Mvn\\New folder\\stormcrawler\\stormcrawler\\src\\op.json");
        writer.write(obj.toString());
        writer.close();
    }
}
