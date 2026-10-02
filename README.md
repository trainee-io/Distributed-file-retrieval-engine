# Distributed File Retrieval Engine

A distributed client-server system in Java that lets multiple clients index and search large sets of text files at the same time, without one client blocking another. Built as an extra credit project for CSC 435 (Distributed Systems) at DePaul University.

## Why this project

Most simple file search tools process one request at a time, which doesn't scale once multiple clients need to index or search the same data simultaneously. This project solves that by using a message broker pattern that routes requests across multiple worker threads in parallel, so clients don't wait in line behind each other.

## Tech stack

- **Java**
- **ZeroMQ (via JeroMQ)** — handles all networking between client, server, and worker threads
- **Multithreading** — configurable number of worker threads per server
- **Custom request-response protocol** — simple text-based commands (`REGISTER`, `INDEX`, `SEARCH`) parsed and handled manually, no external RPC framework

## How it works

- The server starts a **ZeroMQ ROUTER-DEALER broker**, which accepts incoming client connections and distributes them across a pool of worker threads.
- Each worker thread runs its own **REP socket** and handles one request at a time: registering a client, indexing a file (building an inverted index of word → document mappings), or answering a search query.
- Searches support multi-term **AND queries** (e.g. `search distortion AND adaptation`), returning the top 10 ranked results across all indexed documents from all clients.

## How to build and run (Java)

```bash
cd app-java
mvn compile
mvn package
```

Start the server (port, number of worker threads):
```bash
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalServer 12345 2
```

Start a client:
```bash
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalClient
> connect 127.0.0.1 12345
> index <path-to-folder>
> search <term>
```

Run the benchmark tool (simulates multiple clients indexing and searching at once):
```bash
java -cp target/app-java-1.0-SNAPSHOT-jar-with-dependencies.jar csc435.app.FileRetrievalBenchmark 127.0.0.1 12345 2 <client1-path> <client2-path>
```

## Benchmark results (2 clients, 1 server, 2 worker threads)

| Metric | Result |
|---|---|
| Data indexed | 134,247,377 bytes (~128 MB) |
| Indexing time | 6.015 seconds |
| Indexing throughput | ~22 MB/sec |
| Single-term search ("the") | 0.4 seconds |
| Single-term search ("child-like") | 2.8 seconds, 15 matches found |
| Multi-term AND search ("distortion AND adaptation") | 3.27 seconds, 4 matches found |

A C++ implementation of the same system is also included in `app-cpp/`, using the native ZeroMQ library.

## Project structure

```
app-java/   → Java implementation (server, client, benchmark tool)
app-cpp/    → C++ implementation (same architecture)
```

---
Built by Mohammed Musaddiq Vavartar — MS Computer Science, DePaul University
