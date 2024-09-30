# Advanced Database Caching Benchmark

## Overview

This repository contains a Java-based benchmarking project that tests the performance of different caching mechanisms in the context of database operations. The system demonstrates multilevel caching, including both **L1 in-memory caching** (using `ConcurrentHashMap`) and **L2 disk-backed caching** (using **Guava Cache**), as well as fallback to database access. The project is designed to compare the performance of direct database operations with cache-augmented approaches and aims to optimize retrieval speeds in large datasets.

![Benchmark Visualization](https://github.com/user-attachments/assets/ebfe9d33-9a01-45a5-a0f6-48d9182cd966)

## Features

- **Database Interaction**: Uses **MySQL** to store and retrieve data.
- **Multilevel Caching**:
  - **L1 Cache**: Implemented using a `ConcurrentHashMap` with **LRU (Least Recently Used)** eviction strategy.
  - **L2 Cache**: Uses **Guava Cache** with both size and time-based expiration policies.
- **Benchmarking**: Performance testing for:
  - Database Inserts and Retrieves
  - L1 Cache Inserts and Retrieves
  - L2 Cache Inserts and Retrieves
  - Multilevel Cache (L1 and L2) Retrieval with fallback to database if needed.
- **Threaded Operations**: Utilizes a thread pool to perform concurrent insert and retrieve operations, maximizing throughput by leveraging available processor cores.
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

- **L1 Cache**: Implemented using `ConcurrentHashMap`, designed to hold a limited number of entries and follow the **LRU eviction policy**.
- **L2 Cache**: Implemented using **Guava Cache**. It is a larger cache with both **size-based** and **time-based** eviction policies. This layer stores more data than L1 but is slower.

### 2. **Concurrency and Threading**

- The project utilizes a thread pool to execute database operations and cache interactions concurrently. This improves performance by making full use of available CPU cores.
- The `ExecutorService` manages the thread pool, and tasks are submitted for each operation to benchmark their performance in parallel.

### 3. **LRU Caching (Least Recently Used)**

- The **L1 Cache** uses `ConcurrentHashMap` to efficiently manage cache entries, ensuring that only the most recent entries remain in the cache while discarding the least accessed entries.

### 4. **Database Interactions**

- Database operations include:
  - **Inserts**: Batch insertion of 100,000 elements into the database.
  - **Retrievals**: Selecting elements from the database using `PreparedStatement`.

### 5. **Benchmarking**

The project measures the execution time for various operations, allowing for performance comparisons between direct database access and cache-enhanced retrieval.

## Benchmarked Operations

The following operations are benchmarked:

- **Database Inserts**: Testing the speed of inserting records into the database.
- **Database Retrieves**: Testing the speed of retrieving records from the database.
- **L1 Cache Inserts**: Testing the speed of inserting records into the L1 cache.
- **L1 Cache Retrieves**: Testing the speed of retrieving records from the L1 cache.
- **L2 Cache Inserts**: Testing the speed of inserting records into the L2 cache.
- **L2 Cache Retrieves**: Testing the speed of retrieving records from the L2 cache.
- **Multilevel Cache Retrieves**: Testing the speed of retrieving records using both L1 and L2 caches with a fallback to the database.

## Results and Analysis

The results of the benchmarking are logged, allowing for performance analysis and improvement identification. This includes comparisons between L1 and L2 cache retrieval times versus direct database access times.

![WhatsApp Image 2024-09-30 at 11 41 42_03672bf5](https://github.com/user-attachments/assets/e9bf4704-28d9-439f-a1fc-a3e0a9e19014)


## Logging and Debugging

- All operations are logged to `benchmark_log.txt`, capturing execution times and any encountered errors.
- The logging framework is configured to output both to a file and the console for real-time feedback during execution.
