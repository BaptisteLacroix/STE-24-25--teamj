package fr.unice.polytech.equipe.j;

import java.sql.*;
import java.nio.file.*;
import java.io.*;

public class ExecuteSQLFileWithHibernate {

    public static void main(String[] args) {
        // JDBC URL, username, and password for HSQLDB
        String jdbcUrl = "jdbc:hsqldb:file:${user.dir}/src/main/resources/dataTest/bdd.db";

        // Path to your SQL file
        String sqlFilePath = System.getProperty("user.dir") + "/src/main/resources/queries.sql";

        executeSQLFile(jdbcUrl, sqlFilePath);
    }

    public static void executeSQLFile(String jdbcUrl, String sqlFilePath) {
        Connection connection = null;
        Statement statement = null;

        try {
            // Step 1: Connect to the database
            connection = DriverManager.getConnection(jdbcUrl);
            statement = connection.createStatement();

            // Step 2: Read SQL file content
            String sql = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

            // Step 3: Split the SQL file content into individual statements (assuming they are separated by semicolons)
            String[] sqlStatements = sql.split(";");

            // Step 4: Execute each SQL statement
            for (String stmt : sqlStatements) {
                stmt = stmt.trim();
                if (!stmt.isEmpty()) {
                    statement.executeUpdate(stmt);  // Use executeUpdate for DML (INSERT, UPDATE, DELETE)
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
