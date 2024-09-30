# Advanced Database Caching Benchmark

## Overview

This repository contains a Java-based benchmarking project that tests the performance of different caching mechanisms in the context of database operations. The system demonstrates multilevel caching, including both **L1 in-memory caching** (using `ConcurrentHashMap`) and **L2 caching** (using **Guava Cache**), as well as fallback to database access. The project is designed to compare the performance of direct database operations with cache-augmented approaches and aims to optimize retrieval speeds in large datasets.

![Benchmark Visualization](https://github.com/user-attachments/assets/ebfe9d33-9a01-45a5-a0f6-48d9182cd966)

## Features

- **Database Interaction**: Uses **MySQL** to store and retrieve data.
- **Multilevel Caching**:
  - **L1 Cache**: Implemented using a `ConcurrentHashMap`.
  - **L2 Cache**: Uses **Guava Cache** with size-based and time-based expiration policies.
- **Benchmarking**: Performance testing for:
  - Database Inserts and Retrieves
  - L1 Cache Inserts and Retrieves
  - L2 Cache Inserts and Retrieves
  - Multilevel Cache (L1 and L2) Retrieval with fallback to the database if needed.
- **Logging**: The benchmark results and progress are logged to a file for later analysis.
- **Progress Tracking**: The project logs the progress of each operation to efficiently track large data benchmarks.
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
2. Create a database called `testdb`:

```sql
CREATE DATABASE testdb;
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

Once the project is set up and built, run the application using the command shown above. The benchmark will execute several performance tests and log the progress and results to the console and a `concurrent_benchmark_log.txt` file.

## Project Structure

- `src/main/java/com/example/AdvancedDatabaseCachingBenchmark.java`: The main class responsible for managing database operations, caches, and benchmarking.
- `pom.xml`: Maven configuration for dependencies such as Guava and the MySQL JDBC Driver.
- `concurrent_benchmark_log.txt`: Log file created during execution, capturing progress and results.

## Concepts Covered

This project covers the following key concepts:

### 1. **Multilevel Caching**

- **L1 Cache**: Implemented using `ConcurrentHashMap`, designed to hold a limited number of entries.
- **L2 Cache**: Implemented using **Guava Cache**. It is a larger cache with both **size-based** and **time-based** eviction policies. This layer stores more data than L1 but is slower.

### 2. **Database Interactions**

- Database operations include:
  - **Inserts**: Batch insertion of up to 100,000 elements into the database.
  - **Retrievals**: Selecting elements from the database using `PreparedStatement`.

### 3. **Benchmarking**

The project measures the execution time of various database operations and caching mechanisms, comparing their performance through concurrent execution.

### 4. **Logging and Error Handling**

Utilizes Java's `Logger` for logging operations, errors, and benchmarking results. Provides detailed information on operations and their performance metrics.

## Benchmarked Operations

The following operations are benchmarked in the project:

- Database Insert
- Database Retrieve
- L1 Cache Insert
- L1 Cache Retrieve
- L2 Cache Insert
- L2 Cache Retrieve
- Multilevel Cache Retrieve (fallback to the database)

## Results and Analysis

Benchmark results are logged to a file for later analysis. The project analyzes the performance of each operation, identifying bottlenecks and improvements in retrieval times using L1 and L2 caches compared to direct database access.

![WhatsApp Image 2024-09-30 at 11 41 42_d253bb1a](https://github.com/user-attachments/assets/74e4f22e-5486-4b66-b11d-588a81fd6b92)


## Logging and Debugging

Logging is configured to write to `concurrent_benchmark_log.txt`, capturing all operations, errors, and benchmark results for analysis.
