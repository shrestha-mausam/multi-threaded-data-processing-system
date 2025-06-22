package main

import (
    "fmt"
    "os"
    "sync"
    "time"
)

type Task struct {
    ID int
}

func worker(id int, tasks <-chan Task, results *[]string, mu *sync.Mutex, wg *sync.WaitGroup) {
    defer wg.Done()
    fmt.Printf("Worker %d started\n", id)
    for task := range tasks {
        time.Sleep(500 * time.Millisecond) // Simulate processing
        result := fmt.Sprintf("Worker %d processed task %d", id, task.ID)
        mu.Lock()
        *results = append(*results, result)
        mu.Unlock()
    }
    fmt.Printf("Worker %d completed\n", id)
}

func main() {
    numTasks, numWorkers := 10, 3
    tasks := make(chan Task, numTasks)
    var results []string
    var mu sync.Mutex
    var wg sync.WaitGroup

    // Start workers
    for i := 0; i < numWorkers; i++ {
        wg.Add(1)
        go worker(i, tasks, &results, &mu, &wg)
    }

    // Add tasks
    for i := 0; i < numTasks; i++ {
        tasks <- Task{ID: i}
    }
    close(tasks)
    wg.Wait()

    // Write results to file
    file, err := os.Create("results.txt")
    if err != nil {
        fmt.Printf("File write error: %v\n", err)
        return
    }
    defer file.Close()
    for _, result := range results {
        _, err := file.WriteString(result + "\n")
        if err != nil {
            fmt.Printf("File write error: %v\n", err)
            return
        }
    }
    fmt.Println("Results written to results.txt")
} 