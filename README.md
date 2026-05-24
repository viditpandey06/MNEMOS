# MNEMOS - Vector Database Engine

MNEMOS is a Java-based vector database engine built from scratch to demonstrate how semantic search systems work internally. It stores 16-dimensional vectors, indexes them through multiple nearest-neighbor algorithms, exposes a REST API, and visualizes the vector space in the browser.

Live demo: [https://mnemos.viditpandey.in](https://mnemos.viditpandey.in)

## Why This Project Matters

Most AI search projects stop at calling an embedding API and storing vectors in an existing vector database. MNEMOS goes one layer deeper.

This project implements the core retrieval engine itself:

- Brute Force search as the exact correctness baseline.
- KD-Tree search as a classic spatial indexing approach.
- HNSW-style graph search for approximate nearest-neighbor retrieval.
- Runtime-selectable distance metrics.
- Live insertion of new vectors into all indexes.
- A browser visualization that projects 16D vectors into 2D.

The goal is not to wrap Pinecone, Chroma, FAISS, or a managed vector store. The goal is to understand and demonstrate the mechanics behind vector search: indexing, distance calculation, top-k retrieval, approximate search, latency tradeoffs, and visual exploration of high-dimensional data.

## What You Can Try

Open the live demo and search for:

```text
who is Vidit
Cognizant Azure testing
FORGE distributed queue
AXIOMVAULT encryption
Java DPI engine
LeetCode DSA
Playwright Selenium Cucumber
binary search tree
matrix probability
pizza ramen
```

The app converts the query into a 16D vector, searches the active index, and returns nearest neighbors with latency measurements.

## Core Features

### 1. Multiple Search Algorithms

MNEMOS indexes the same dataset into three different retrieval engines:

| Algorithm | Type | Purpose |
|---|---|---|
| Brute Force | Exact | Scans every vector and acts as the ground-truth baseline. |
| KD-Tree | Spatial index | Uses dimensional splitting and pruning for nearest-neighbor search. |
| HNSW | Approximate graph search | Uses layered graph traversal for fast similarity search. |

This makes the project useful for comparing how search strategy affects latency and retrieval behavior.

### 2. Runtime Distance Metrics

The search API supports:

- Cosine distance
- Euclidean distance
- Manhattan distance

Cosine distance is the default because it is commonly used for semantic embeddings, where vector direction often matters more than magnitude.

### 3. Live Vector Ingestion

Users can add custom text through the UI. MNEMOS converts it into a 16D vector and inserts it into:

- Brute Force index
- KD-Tree index
- HNSW graph
- Main in-memory store

The visualization updates immediately.

### 4. 16D Semantic Vector Space

MNEMOS intentionally uses 16D synthetic/demo vectors instead of requiring Ollama or another embedding model.

This was a deliberate design choice:

- The project remains self-contained and easy to run.
- The vector values are understandable during interviews and debugging.
- No GPU, model download, local Ollama setup, or external API key is required.
- The focus stays on vector database internals rather than model integration.
- The same architecture can later be connected to real embeddings by replacing the embedding generator.

In production, the synthetic 16D embedding layer could be replaced with Ollama, OpenAI embeddings, Sentence Transformers, or another model provider. The indexing layer only needs a `double[]` vector, so the search engine is already separated from the embedding source.

### 5. Interview-Aware Profile Search

The seeded database includes a dedicated profile cluster for Vidit Pandey's resume and project work. This allows the live demo to answer queries about:

- Education and contact details
- Cognizant experience
- Azure testing strategy
- Playwright, Selenium, and Cucumber automation
- FORGE distributed task queue engine
- AXIOMVAULT encrypted messaging platform
- Enterprise DPI Engine in Java
- VE-COMPILER package
- Cloud, security, systems, and DSA skills

This turns the project into both a vector database demo and an interactive portfolio search engine.

## Architecture

```text
Browser UI
   |
   |  REST API calls
   v
Javalin Server
   |
   v
VectorStore
   |
   +--> BruteForce index
   +--> KDTree index
   +--> HNSW graph index
   +--> In-memory HashMap store
```

## Search Flow

1. A user types a query in the browser.
2. The frontend converts the text into a 16D query vector.
3. The query is sent to `/api/search`.
4. The backend validates vector dimensions.
5. `VectorStore` selects the requested distance metric.
6. The selected algorithm performs top-k nearest-neighbor search.
7. Results are returned with distance scores and latency in microseconds.
8. The frontend highlights matching vectors on the canvas.

## API Overview

| Endpoint | Method | Description |
|---|---|---|
| `/api/search` | GET | Search nearest vectors using selected algorithm and metric. |
| `/api/insert` | POST | Insert a new vector into all indexes. |
| `/api/delete/{id}` | DELETE | Remove a vector by id. |
| `/api/items` | GET | Return all stored vectors. |
| `/api/benchmark` | GET | Compare Brute Force, KD-Tree, and HNSW on current data. |
| `/api/benchmark-scale` | GET | Generate datasets up to 5000 vectors and compare scaling. |
| `/api/hnsw-info` | GET | Return graph layer and edge information. |
| `/api/stats` | GET | Return vector count and dimensionality. |
| `/api/health` | GET | Health check endpoint. |

Example search:

```text
GET /api/search?v=0.1,0.2,...&k=5&metric=cosine&algo=hnsw
```

## Algorithm Notes

### Brute Force

Brute Force computes the distance from the query vector to every stored vector, sorts the results, and returns the top `k`.

It is simple, exact, and useful as a correctness baseline.

Complexity:

```text
Search: O(N * d)
Insert: O(1)
```

### KD-Tree

KD-Tree recursively partitions the vector space by cycling through dimensions. During search, it explores the closer subtree first and prunes the farther subtree when it cannot improve the current top-k results.

It is useful for demonstrating spatial indexing, though it becomes less effective as dimensions grow.

### HNSW

HNSW builds a layered proximity graph. Search starts from a high-level entry point, greedily moves closer to the query, then performs a broader local search on the bottom layer.

MNEMOS uses:

- `M = 16`
- `M0 = 32`
- `efConstruction = 200`
- `efSearch = 50`

This demonstrates the classic latency-vs-recall tradeoff of approximate nearest-neighbor search.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17 |
| HTTP server | Javalin |
| JSON | Gson |
| Frontend | Vanilla JavaScript, HTML5 Canvas |
| Styling | Tailwind CSS |
| Charts | Chart.js |
| Build | Maven Shade Plugin |
| Deployment | Docker, Render |

## Project Structure

```text
src/main/java/com/mnemos/
  Server.java                 # Javalin server and REST API
  algorithm/
    BruteForce.java           # Exact linear scan search
    KDTree.java               # KD-Tree index
    HNSW.java                 # Approximate graph index
  data/
    DemoVectors.java          # Seeded semantic/profile vectors
  metric/
    DistanceMetrics.java      # Cosine, Euclidean, Manhattan
  model/
    VectorItem.java
    SearchHit.java
    BenchmarkResult.java
  store/
    VectorStore.java          # Coordinates storage and indexes

src/main/resources/public/
  index.html                  # Single-page UI
  app.js                      # Embedding simulation, API calls, visualization
```

## Run Locally

Prerequisites:

- Java 17
- Maven

Build:

```bash
mvn clean package
```

Run:

```bash
java -jar target/mnemos-1.0.jar
```

Open:

```text
http://localhost:8080
```

The server reads the `PORT` environment variable when present. Without `PORT`, it defaults to `8080`.

## Docker

```bash
docker build -t mnemos .
docker run -p 8080:8080 mnemos
```

## Deployment

MNEMOS is deployed on Render using the included multi-stage Dockerfile.

Production URL:

[https://mnemos.viditpandey.in](https://mnemos.viditpandey.in)

The backend binds to `0.0.0.0:$PORT`, which makes it compatible with Render and similar platforms.

## Future Improvements

- Add persistent storage.
- Add real embedding integration with Ollama or another embedding model.
- Add recall@k benchmarking against Brute Force.
- Add metadata filtering.
- Add automated tests for distance metrics and algorithm consistency.
- Add HNSW parameter controls in the UI.
- Add index serialization and reload support.
- Improve delete handling for graph indexes.

## Interview Talking Point

MNEMOS is not just a frontend demo. It is a compact vector database engine that shows how semantic search works below the API layer: vectors, distance metrics, top-k retrieval, exact search, approximate graph search, benchmarking, and visualization.

## Note On Use

This project is built for learning, interview discussion, and portfolio demonstration. You are welcome to explore the code and learn from it, but please do not copy or present it as your own work without proper credit.

