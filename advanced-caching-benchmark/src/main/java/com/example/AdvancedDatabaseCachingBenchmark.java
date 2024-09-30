package com.example;

import java.sql.Connection;
import java.util.Map;
import com.google.common.cache.Cache;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.concurrent.TimeUnit;
import java.util.LinkedHashMap;
import com.google.common.cache.CacheBuilder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdvancedDatabaseCachingBenchmark {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testingdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // Number of elements for benchmark
    private static final int NUM_ELEMENTS = 100000;
    
    // Cache sizes and parameters
    private static final int L1_CACHE_SIZE = 10000;
    private static final int L2_CACHE_SIZE = 100000;
    private static final int L2_CACHE_DURATION_MINUTES = 10;

    // Progress log interval for batch operations
    private static final int PROGRESS_INTERVAL = 10000;

    // Connection to the database
    private static Connection connection;

    // L1 Cache (In-memory) with a fixed size (using LinkedHashMap for LRU mechanism)
    private static Map<Integer, String> l1Cache;

    // L2 Cache (using Guava Cache for larger capacity)
    private static Cache<Integer, String> l2Cache;

    // Logger for recording the benchmark progress and results
    private static final Logger LOGGER = Logger.getLogger(AdvancedDatabaseCachingBenchmark.class.getName());

    public static void main(String[] args) {
        setupLogging();  // Initialize the logging setup

        try {
            setupDatabase();  // Set up the database connection and table
            setupCaches();    // Initialize L1 and L2 caches

            // List to hold benchmark results for each operation
            List<BenchmarkResult> results = new ArrayList<>();

            // Perform benchmark operations and store results
            results.add(new BenchmarkResult("Database Insert", benchmarkDatabaseInsert()));
            results.add(new BenchmarkResult("Database Retrieve", benchmarkDatabaseRetrieve()));
            results.add(new BenchmarkResult("L1 Cache Insert", benchmarkL1CacheInsert()));
            results.add(new BenchmarkResult("L1 Cache Retrieve", benchmarkL1CacheRetrieve()));
            results.add(new BenchmarkResult("L2 Cache Insert", benchmarkL2CacheInsert()));
            results.add(new BenchmarkResult("L2 Cache Retrieve", benchmarkL2CacheRetrieve()));
            results.add(new BenchmarkResult("Multilevel Cache Retrieve", benchmarkMultilevelCacheRetrieve()));

            // Analyze and log performance results
            analyzeAndLogResults(results);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred during benchmark execution", e);
        } finally {
            closeDatabaseConnection();  // Ensure the database connection is closed
        }
    }

    // Set up logging configuration for the benchmark execution
    private static void setupLogging() {
        try {
            FileHandler fileHandler = new FileHandler("benchmark_log.txt");
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting up logging", e);
        }
    }

    // Establish a connection to the database and set up the table for the benchmark
    private static void setupDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, value VARCHAR(255))");
            }
            LOGGER.info("Database setup completed successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error setting up database", e);
        }
    }

    // Initialize the L1 and L2 caches with size limits and expiration policies
    private static void setupCaches() {
        l1Cache = new LinkedHashMap<Integer, String>(L1_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                return size() > L1_CACHE_SIZE;
            }
        };

        l2Cache = CacheBuilder.newBuilder()
                .maximumSize(L2_CACHE_SIZE)
                .expireAfterAccess(L2_CACHE_DURATION_MINUTES, TimeUnit.MINUTES)
                .build();

        LOGGER.info("Caches setup completed successfully.");
    }

    // Benchmark for inserting data into the database in batches for better performance
    private static long benchmarkDatabaseInsert() throws SQLException {
        LOGGER.info("Starting Database Insert benchmark...");
        long startTime = System.nanoTime();

        String sql = "INSERT INTO test_table (id, value) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < NUM_ELEMENTS; i++) {
                statement.setInt(1, i);
                statement.setString(2, "Value" + i);
                statement.addBatch();

                // Execute batch every 100 operations to optimize performance
                if (i % 100 == 0) {
                    statement.executeBatch();
                }
                logProgress("Database Insert", i);
            }
            statement.executeBatch();  // Execute remaining batches
        }

        long endTime = System.nanoTime();
        LOGGER.info("Database Insert benchmark completed.");
        return endTime - startTime;
    }

    // Benchmark for retrieving data from the database using a SELECT query
    private static long benchmarkDatabaseRetrieve() throws SQLException {
        LOGGER.info("Starting Database Retrieve benchmark...");
        long startTime = System.nanoTime();

        String sql = "SELECT value FROM test_table WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < NUM_ELEMENTS; i++) {
                statement.setInt(1, i);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        resultSet.getString("value");  // Simulate data retrieval
                    }
                }
                logProgress("Database Retrieve", i);
            }
        }

        long endTime = System.nanoTime();
        LOGGER.info("Database Retrieve benchmark completed.");
        return endTime - startTime;
    }

    // Benchmark for inserting data into the L1 Cache (using LinkedHashMap)
    private static long benchmarkL1CacheInsert() {
        LOGGER.info("Starting L1 Cache Insert benchmark...");
        long startTime = System.nanoTime();

        for (int i = 0; i < NUM_ELEMENTS; i++) {
            l1Cache.put(i, "Value" + i);  // Insert data into L1 Cache
            logProgress("L1 Cache Insert", i);
        }

        long endTime = System.nanoTime();
        LOGGER.info("L1 Cache Insert benchmark completed.");
        return endTime - startTime;
    }

    // Benchmark for retrieving data from the L1 Cache
    private static long benchmarkL1CacheRetrieve() {
        LOGGER.info("Starting L1 Cache Retrieve benchmark...");
        long startTime = System.nanoTime();

        for (int i = 0; i < NUM_ELEMENTS; i++) {
            l1Cache.get(i);  // Retrieve data from L1 Cache
            logProgress("L1 Cache Retrieve", i);
        }

        long endTime = System.nanoTime();
        LOGGER.info("L1 Cache Retrieve benchmark completed.");
        return endTime - startTime;
    }

    // Benchmark for inserting data into the L2 Cache (using Guava Cache)
    private static long benchmarkL2CacheInsert() {
        LOGGER.info("Starting L2 Cache Insert benchmark...");
        long startTime = System.nanoTime();

        for (int i = 0; i < NUM_ELEMENTS; i++) {
            l2Cache.put(i, "Value" + i);  // Insert data into L2 Cache
            logProgress("L2 Cache Insert", i);
        }

        long endTime = System.nanoTime();
        LOGGER.info("L2 Cache Insert benchmark completed.");
        return endTime - startTime;
    }

    // Benchmark for retrieving data from the L2 Cache
    private static long benchmarkL2CacheRetrieve() {
        LOGGER.info("Starting L2 Cache Retrieve benchmark...");
        long startTime = System.nanoTime();

        for (int i = 0; i < NUM_ELEMENTS; i++) {
            l2Cache.getIfPresent(i);  // Retrieve data from L2 Cache
            logProgress("L2 Cache Retrieve", i);
        }

        long endTime = System.nanoTime();
        LOGGER.info("L2 Cache Retrieve benchmark completed.");
        return endTime - startTime;
    }

    // Benchmark for retrieving data from both L1 and L2 caches, and fallback to database
    private static long benchmarkMultilevelCacheRetrieve() throws SQLException {
        LOGGER.info("Starting Multilevel Cache Retrieve benchmark...");
        long startTime = System.nanoTime();

        for (int i = 0; i < NUM_ELEMENTS; i++) {
            // Attempt to retrieve from L1 cache
            String value = l1Cache.get(i);

            // Fallback to L2 cache if not in L1
            if (value == null) {
                value = l2Cache.getIfPresent(i);

                // Fallback to database if not in L2
                if (value == null) {
                    String sql = "SELECT value FROM test_table WHERE id = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, i);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                value = resultSet.getString("value");
                            }
                        }
                    }
                }
            }
            logProgress("Multilevel Cache Retrieve", i);
        }

        long endTime = System.nanoTime();
        LOGGER.info("Multilevel Cache Retrieve benchmark completed.");
        return endTime - startTime;
    }

    // Log progress at intervals to track the benchmark operation progress
    private static void logProgress(String operation, int i) {
        if (i % PROGRESS_INTERVAL == 0) {
            LOGGER.info(operation + " progress: " + i + "/" + NUM_ELEMENTS);
        }
    }

    // Analyze the benchmark results and log the performance timings
    private static void analyzeAndLogResults(List<BenchmarkResult> results) {
        LOGGER.info("Benchmark Results:");
        for (BenchmarkResult result : results) {
            LOGGER.info(result.getOperation() + ": " + (result.getDuration() / 1_000_000) + " ms");
        }
    }

    // Close the database connection after completing the benchmark
    private static void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection", e);
            }
        }
    }

    // Helper class to store benchmark operation results
    static class BenchmarkResult {
        private String operation;
        private long duration;

        public BenchmarkResult(String operation, long duration) {
            this.operation = operation;
            this.duration = duration;
        }

        public String getOperation() {
            return operation;
        }

        public long getDuration() {
            return duration;
        }
    }
}
