// package com.phasmidsoftware.dsaipg.util;

// import com.phasmidsoftware.dsaipg.adt.pq.PQException;
// import com.phasmidsoftware.dsaipg.adt.pq.PriorityQueue;
// import com.phasmidsoftware.dsaipg.adt.pq.FibonacciHeap;

// import java.io.IOException;
// import java.util.*;
// import java.util.function.Consumer;

// /**
//  * The {@code PQBenchmark} class is designed to benchmark operations performed
//  * with priority queues.
//  * It evaluates different heap configurations and reports execution times.
//  */
// public class PQBenchmark {

//     private static final int INSERTIONS = 16000;
//     private static final int REMOVALS = 4000;
//     private static final int M = 4095; 

//     public PQBenchmark(Config config) {
//         this.config = config;
//     }

//     public static void main(String[] args) throws IOException {
//         System.out.println("DEBUG: main() started");
//         try{
//             Config config = Config.load(PQBenchmark.class);
//             PQBenchmark benchmark = new PQBenchmark(config);
        
//             System.out.println("\n===== Binary Heap Results =====");
//             System.out.println("DEBUG: Calling insertDeleteN for Binary Heap");
//             double binaryHeapTime = benchmark.insertDeleteN(INSERTIONS, REMOVALS, false, 2);
//             System.out.println("Binary Heap Time: " + binaryHeapTime);
        
//             System.out.println("\n===== Binary Heap (Floyd) Results =====");
//             System.out.println("DEBUG: Calling insertDeleteN for Binary Heap (Floyd)");
//             double binaryHeapFloydTime = benchmark.insertDeleteN(INSERTIONS, REMOVALS, true, 2);
//             System.out.println("Binary Heap (Floyd) Time: " + binaryHeapFloydTime);
        
//             System.out.println("\n===== 4-ary Heap Results =====");
//             System.out.println("DEBUG: Calling insertDeleteN for 4-ary Heap");
//             double fourAryHeapTime = benchmark.insertDeleteN(INSERTIONS, REMOVALS, false, 4);
//             System.out.println("4-ary Heap Time: " + fourAryHeapTime);
        
//             System.out.println("\n===== 4-ary Heap (Floyd) Results =====");
//             System.out.println("DEBUG: Calling insertDeleteN for 4-ary Heap (Floyd)");
//             double fourAryHeapFloydTime = benchmark.insertDeleteN(INSERTIONS, REMOVALS, true, 4);
//             System.out.println("4-ary Heap (Floyd) Time: " + fourAryHeapFloydTime);
        
//             System.out.println("\n===== Fibonacci Heap Results =====");
//             System.out.println("DEBUG: Calling fibonacciHeapTest");
//             double fibonacciHeapTime = benchmark.fibonacciHeapTest(INSERTIONS, REMOVALS);
//             System.out.println("Fibonacci Heap Time: " + fibonacciHeapTime);
//         }catch (Exception e) {
//             System.out.println("ERROR: Exception occurred in main()");
//             e.printStackTrace(); 
//         }
       
//     }

//     /**
//      * Inserts and deletes elements from a priority queue while tracking the
//      * highest-priority spilled element.
//      */
//     private double insertDeleteN(final int n, int m, final boolean floyd, int arity) {

//         System.out.println("\n===== Running Priority Queue (arity = " + arity + ", Floyd = " + floyd + ") =====");

//         final Random rand = new Random();
//         int[] randomArray = new int[n];
//         for (int i = 0; i < n; i++) {
//             randomArray[i] = rand.nextInt(n);
//         }

//         System.out.println("DEBUG: Calling insertArray() for arity = " + arity);
//         List<Integer> spilledElements = insertArray(randomArray, floyd, arity);
//         System.out.println("DEBUG: insertArray() finished for arity = " + arity);

//         double averageSpilled = spilledElements.stream()
//         .mapToInt(Integer::intValue)
//         .average()
//         .orElse(0.0);

// System.out.println("Total spilled elements: " + spilledElements.size());
// System.out.println("Highest spilled element: " + (spilledElements.isEmpty() ? "None" : Collections.max(spilledElements)));
// System.out.printf("Average spilled element: %.2f\n", averageSpilled);


// System.out.println("DEBUG: Starting Benchmark_Timer for arity = " + arity);
//         Benchmark<Boolean> bm = new Benchmark_Timer<>(
//                 "Priority Queue (arity = " + arity + ", Floyd = " + floyd + ")",
//                 null,
//                 b -> insertArray(randomArray, floyd, arity),
//                 null);
      
//         // return bm.run(true, m);

//         double time = bm.run(true, m);
//         System.out.println("DEBUG: Benchmark_Timer finished, time = " + time);
    
//         return time;
//     }

//     /**
//      * Inserts elements into the heap and removes them while keeping track of the
//      * highest-priority spilled element.
//      */
//     private List<Integer> insertArray(int[] array, final boolean floyd, int arity) {
//         System.out.println("DEBUG: Running insertArray() for arity = " + arity);

        
//         Comparator<Integer> comparator = Comparator.naturalOrder();
//         PriorityQueue<Integer> pq = new PriorityQueue<>(M, true, comparator, floyd);
//         pq.setArity(arity);

//         System.out.println("DEBUG: PriorityQueue created with arity = " + arity);

//         List<Integer> spilledElements = new ArrayList<>();
//         Integer highestSpilled = null;

//         for (int j : array) {
//             if (j < 0) continue;
    
//             pq.give(j);
    
//             if (pq.size() > M) {
//                 Integer spilled = pq.take();
//                 if (spilled != null) {
//                     spilledElements.add(spilled);
//                     if (highestSpilled == null || spilled > highestSpilled) {
//                         highestSpilled = spilled;
//                     }
//                 }
//             }
//         }
//         System.out.println("DEBUG: insertArray() finished for arity = " + arity);
        
//         return spilledElements; 
//         }
    
      
//         // if (highestSpilled == null) {
//         //     highestSpilled = -1; 
//         // }
//         // System.out.println("Highest spilled element: " + highestSpilled);

//         // int previousSize = pq.size();
//         // int iteration = 0;
//         // while (pq.size() > M && iteration < 100) {
//         //     Integer spilled = pq.take();
//         //     System.out.println("DEBUG: spilled element = " + spilled);
//         //     iteration++;
            
//         //     if (pq.size() >= previousSize) {
//         //         System.out.println("ERROR: Priority queue size is not decreasing, forcibly exiting!");
//         //         break;
//         //     }
//         //     previousSize = pq.size();
//         // }
//         // if (iteration >= 100) {
//         //     System.out.println("ERROR: Loop stuck, forcibly exiting!");
//         // }
  


    
//     /**
//      * Benchmarks Fibonacci Heap performance.
//      */
//     private double fibonacciHeapTest(final int n, int m) {
//         final Random rand = new Random();
//         int[] randomArray = new int[n];
//         for (int i = 0; i < n; i++) {
//             randomArray[i] = rand.nextInt(n);
//         }

//         Benchmark<Boolean> bm = new Benchmark_Timer<>(
//                 "Fibonacci Heap",
//                 null,
//                 b -> {
//                     if (b) {
//                         insertFibonacciHeap(randomArray);
//                     }
//                 },
//                 null);

//         return bm.run(true, m);
//     }

//     /**
//      * Inserts elements into a Fibonacci Heap and removes elements.
//      */
//     private void insertFibonacciHeap(int[] array) {
//         Comparator<Integer> comparator = Comparator.naturalOrder();
//         FibonacciHeap<Integer> fibHeap = new FibonacciHeap<>(comparator);
//         final Random random = new Random();

//         List<Integer> spilledElements = new ArrayList<>();
//         Integer highestSpilled = null;

//         for (int j : array) {
//             fibHeap.give(j);
//             if (fibHeap.size() > M) {
//                 Integer spilled = fibHeap.take();
//                 if (spilled != null) {
//                     spilledElements.add(spilled);
//                     if (highestSpilled == null || spilled > highestSpilled) {
//                         highestSpilled = spilled;
//                     }
//                 }
//             }
//         }

//         double averageSpilled = spilledElements.stream()
//                 .mapToInt(Integer::intValue)
//                 .average()
//                 .orElse(0.0);

//         System.out.println("\n===== Fibonacci Heap Results =====");
//         System.out.println("Total spilled elements: " + spilledElements.size());
//         System.out.println("Highest spilled element: " + highestSpilled);
//         System.out.printf("Average spilled element: %.2f\n", averageSpilled);
//     }

//     private final Config config;
// }