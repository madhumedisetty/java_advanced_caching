# Advanced Database Caching Benchmark

## Overview

This repository contains a Java-based benchmarking project that tests the performance of different caching mechanisms in the context of database operations. The system demonstrates multilevel caching, including both **L1 in-memory caching** (using `LinkedHashMap` with LRU policy) and **L2 disk-backed caching** (using **Guava Cache**), as well as fallback to database access. The project is designed to compare the performance of direct database operations with cache-augmented approaches and aims to optimize retrieval speeds in large datasets.
![WhatsApp Image 2024-09-30 at 11 27 11_4875625c](https://github.com/user-attachments/assets/ebfe9d33-9a01-45a5-a0f6-48d9182cd966)

## Features

- **Database Interaction**: Uses **MySQL** to store and retrieve data.
- **Multilevel Caching**:
  - **L1 Cache**: Implemented using a `LinkedHashMap` with **LRU (Least Recently Used)** eviction strategy.
  - **L2 Cache**: Uses **Guava Cache** with both size and time-based expiration policies.
- **Benchmarking**: Performance testing for:
  - Database Inserts and Retrieves
  - L1 Cache Inserts and Retrieves
  - L2 Cache Inserts and Retrieves
  - Multilevel Cache (L1 and L2) Retrieval with fallback to database if needed.
- **Logging**: The benchmark results and progress are logged to a file for later analysis.
- **Progress Tracking**: The project logs the progress of each operation to track large data benchmarks efficiently.
- **Graph Data Preparation**: The framework is set up for easy integration with graphing libraries for visual analysis of benchmark results.

## Table of Contents

1. [Requirements](#requirements)
2. [Installation](#installation)
3. [Running the Project](#running-the-project)
4. [Project Structure](#project-structure)
5. [Concepts Covered](#concepts-covered)
6. [Benchmarked Operations](#benchmarked-operations)
7. [Results and Analysis](#results-and-analysis)
8. [Logging and Debugging](#logging-and-debugging)

## Requirements

- **Java 8 or higher**
- **MySQL Database** (or any SQL-based database)
- **Guava** (Google Core Libraries for Java)
- **Maven** (for dependency management and build)
- **MySQL JDBC Driver** (added in `pom.xml` for Maven)

## Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/username/AdvancedDatabaseCachingBenchmark.git
cd AdvancedDatabaseCachingBenchmark
```

### Step 2: Setup MySQL Database

1. Ensure you have MySQL installed and running.
2. Create a database called `testingdb`:

```sql
CREATE DATABASE testingdb;
```

3. Adjust the database credentials (`DB_USER`, `DB_PASSWORD`, `DB_URL`) in the `AdvancedDatabaseCachingBenchmark.java` file if needed.

### Step 3: Build the Project

Build the project using **Maven**:

```bash
mvn clean install
```

### Step 4: Run the Application

```bash
java -jar target/AdvancedDatabaseCachingBenchmark-1.0-SNAPSHOT.jar
```

## Running the Project

Once the project is set up and built, run the application using the command shown above. The benchmark will execute several performance tests and log the progress and results to the console and a `benchmark_log.txt` file.

## Project Structure

- `src/main/java/com/example/AdvancedDatabaseCachingBenchmark.java`: The main class responsible for managing database operations, caches, and benchmarking.
- `pom.xml`: Maven configuration for dependencies such as Guava and the MySQL JDBC Driver.
- `benchmark_log.txt`: Log file created during execution, capturing progress and results.

## Concepts Covered

This project covers the following key concepts:

### 1. **Multilevel Caching**

- **L1 Cache**: Implemented using `LinkedHashMap`, designed to hold a limited number of entries and follow the **LRU eviction policy**.
- **L2 Cache**: Implemented using **Guava Cache**. It is a larger cache with both **size-based** and **time-based** eviction policies. This layer stores more data than L1 but is slower.
  
### 2. **LRU Caching (Least Recently Used)**

- The **L1 Cache** uses `LinkedHashMap` with the `removeEldestEntry` method to ensure that only the most recent entries remain in the cache, discarding the least accessed entries.

### 3. **Database Interactions**

- Database operations include:
  - **Inserts**: Batch insertion of 100,000 elements into the database.
  - **Retrievals**: Selecting elements from the database using `PreparedStatement`.

### 4. **Benchmarking**

The project measures the execution time of various operations, such as inserting and retrieving data from the database, L1 cache, and L2 cache. The benchmarking is done using `System.nanoTime()` to measure precise timing.

### 5. **Guava Cache**

The **Guava Cache** is used for **L2 caching**. It is highly configurable with settings like maximum size, expiration after access, and more. This provides a more flexible and scalable caching mechanism compared to in-memory-only solutions like `LinkedHashMap`.

### 6. **Logging**

- The project uses **Java's built-in logging system** (`java.util.logging`) to capture progress and results of the benchmark tests. Logs are output to `benchmark_log.txt` for detailed analysis.
  
### 7. **Batch Database Inserts**

To improve performance, the project uses **batch inserts** in the database during benchmark testing. This minimizes the number of round-trips to the database, increasing efficiency.

## Benchmarked Operations

1. **Database Inserts**: Inserting 100,000 elements into the database.
2. **Database Retrievals**: Retrieving those elements using a SQL `SELECT` query.
3. **L1 Cache Inserts**: Storing 100,000 elements in the in-memory L1 cache.
4. **L1 Cache Retrievals**: Fetching elements from the L1 cache.
5. **L2 Cache Inserts**: Storing elements in the Guava-based L2 cache.
6. **L2 Cache Retrievals**: Fetching elements from the L2 cache.
7. **Multilevel Cache Retrieval**: Retrieving data using both L1 and L2 caches with fallback to the database when cache misses occur.

## Results and Analysis

After running the benchmarks, the log file (`benchmark_log.txt`) will contain the timings for each operation in nanoseconds. The `analyzeAndLogResults` method provides a summary of the benchmark results, which can be used for performance analysis.

### Example Output:
```
Benchmark Results:
Database Insert: 12345 ms
Database Retrieve: 6789 ms
L1 Cache Insert: 3456 ms
L1 Cache Retrieve: 789 ms
L2 Cache Insert: 2345 ms
L2 Cache Retrieve: 567 ms
Multilevel Cache Retrieve: 456 ms
```

## Logging and Debugging

The project uses **Java's logging framework** to log important information, such as benchmark results, progress updates, and error messages. Logs are stored in `benchmark_log.txt`. Each benchmark operation logs progress at regular intervals (e.g., every 10,000 operations) to ensure visibility during execution.
