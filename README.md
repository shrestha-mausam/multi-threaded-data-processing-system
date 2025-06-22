# Multi-Threaded Data Processing System

## Overview
This project demonstrates a multi-threaded data processing system implemented in both Go and Java. The system simulates processing a set of tasks using multiple worker threads, showcasing concurrency, synchronization, and inter-thread communication.

Each implementation reads a set of tasks, processes them concurrently using worker threads, and writes the results to a file (`results.txt`).

---

## Project Structure

```
.
├── go/
│   ├── main.go           # Go implementation
│   └── results.txt       # Output from Go run
├── java/
│   ├── RideProcessingSystem.java  # Java implementation
│   ├── results.txt                # Output from Java run
│   └── *.class                    # Compiled Java classes
└── README.md
```

---

## Setup & Installation

### Go Implementation
1. Ensure you have Go installed (https://golang.org/dl/).
2. Navigate to the `go` directory:
   ```sh
   cd go
   ```
3. Run the program:
   ```sh
   go run main.go
   ```
4. The results will be written to `results.txt`.

### Java Implementation
1. Ensure you have Java (JDK 8+) installed (https://adoptopenjdk.net/).
2. Navigate to the `java` directory:
   ```sh
   cd java
   ```
3. Compile the program:
   ```sh
   javac RideProcessingSystem.java
   ```
4. Run the program:
   ```sh
   java RideProcessingSystem
   ```
5. The results will be written to `results.txt`.

---

## Usage Example

Sample output in `results.txt` (order may vary due to concurrency):
```
Worker 0 processed task 2
Worker 2 processed task 0
Worker 1 processed task 1
...
```

---

## System Architecture & Threading Model

### Go
- Uses goroutines for worker threads.
- A buffered channel is used to distribute tasks to workers.
- `sync.WaitGroup` ensures the main thread waits for all workers to finish.
- `sync.Mutex` is used to synchronize access to the shared results slice.

### Java
- Uses `ExecutorService` with a fixed thread pool for worker threads.
- `BlockingQueue` is used for thread-safe task distribution.
- `Collections.synchronizedList` ensures thread-safe access to results.
- Each worker is a `Runnable` that processes tasks from the queue.

---

## Contribution Guidelines

1. Fork the repository and create a new branch for your feature or bugfix.
2. Ensure code is well-documented and follows idiomatic Go/Java style.
3. Add tests or sample runs if applicable.
4. Submit a pull request with a clear description of your changes.

---

## License
This project is for educational purposes.
