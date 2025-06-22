import java.util.concurrent.*;
import java.util.*;
import java.io.*;

class Task {
    private final int id;
    public Task(int id) { this.id = id; }
    public int getId() { return id; }
}

class SharedQueue {
    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
    public void addTask(Task task) { queue.add(task); }
    public Task getTask() throws InterruptedException { return queue.poll(1, TimeUnit.SECONDS); }
}

class Worker implements Runnable {
    private final SharedQueue queue;
    private final List<String> results;
    private final int workerId;

    public Worker(SharedQueue queue, List<String> results, int workerId) {
        this.queue = queue;
        this.results = results;
        this.workerId = workerId;
    }

    @Override
    public void run() {
        try {
            System.out.println("Worker " + workerId + " started.");
            Task task;
            while ((task = queue.getTask()) != null) {
                // Simulate processing
                Thread.sleep(500);
                String result = "Worker " + workerId + " processed task " + task.getId();
                synchronized (results) {
                    results.add(result);
                }
            }
            System.out.println("Worker " + workerId + " completed.");
        } catch (InterruptedException e) {
            System.err.println("Worker " + workerId + " interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Worker " + workerId + " error: " + e.getMessage());
        }
    }
}

public class RideProcessingSystem {
    public static void main(String[] args) {
        SharedQueue queue = new SharedQueue();
        List<String> results = Collections.synchronizedList(new ArrayList<>());
        int numTasks = 10, numWorkers = 3;

        // Add tasks to the queue
        for (int i = 0; i < numTasks; i++) queue.addTask(new Task(i));

        ExecutorService executor = Executors.newFixedThreadPool(numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            executor.submit(new Worker(queue, results, i));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
        }

        // Write results to file
        try (PrintWriter out = new PrintWriter("results.txt")) {
            for (String result : results) out.println(result);
            System.out.println("Results written to results.txt");
        } catch (IOException e) {
            System.err.println("File write error: " + e.getMessage());
        }
    }
} 