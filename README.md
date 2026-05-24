# MNEMOS — Vector Database Engine

A portfolio-grade vector database built from scratch in pure Java. Features a custom implementation of three exact and approximate nearest-neighbor search algorithms, a live 2D PCA visualization frontend, and a Docker-ready deployment architecture.

![MNEMOS Architecture](https://via.placeholder.com/800x400.png?text=MNEMOS+VectorDB+Architecture)

## Features

- **HNSW (Hierarchical Navigable Small World):** O(log N) approximate nearest-neighbor search using multi-layer graph routing.
- **KD-Tree:** O(log N) exact search with binary space partitioning and subtree pruning.
- **Brute Force:** O(N·d) linear scan baseline for algorithm correctness verification.
- **Distance Metrics:** Support for Cosine Similarity, Euclidean Distance, and Manhattan Distance.
- **Live User Embeddings:** Type text on the frontend and MNEMOS instantly embeds and inserts it into the active indices.
- **PCA Visualization:** Real-time 2D projection of 16D semantic vectors using HTML5 Canvas.
- **Scale Benchmarking:** Compare the scaling behavior of HNSW O(log N) against Brute Force O(N) using live generated datasets up to N=5000.

## Tech Stack

- **Backend / Algorithms:** Java 17 (Zero algorithm dependencies, built from scratch)
- **API / Server:** Javalin HTTP Server, Gson
- **Frontend:** Vanilla JS, HTML5 Canvas, Tailwind CSS, Chart.js
- **Deployment:** Docker

## Project Structure

- `src/main/java/com/mnemos/algorithm/` - Implementations of HNSW, KDTree, and BruteForce.
- `src/main/java/com/mnemos/Server.java` - Javalin REST API and static file serving.
- `src/main/resources/public/` - The React-like single-page frontend.

## How to Run Locally

### Prerequisites
- Java 17
- Maven

### Build & Run
```bash
mvn clean package
java -jar target/mnemos-1.0.jar
```
Navigate to `http://localhost:8080`.

## Docker / Render Deployment

This project uses a multi-stage Dockerfile and is ready to be deployed to Render or Railway.

```bash
docker build -t mnemos .
docker run -p 8080:8080 mnemos
```
