package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

public class HeapBench {
    public static void main(String[] args) {
        int M = 4095; 
        int insertions = 16000;
        int deletions = 4000;
        boolean floyd = true;

        Random random = new Random();
        Supplier<Integer> supplier = () -> random.nextInt(100000);

        FibonacciHeap<Integer> fh = new FibonacciHeap<Integer>(Comparator.naturalOrder());

        System.out.println("Initial Fibonacci Heap size: " + fh.size());
        fh.give(10);
        fh.give(20);
        fh.give(30);
        System.out.println("After insertions, Fibonacci Heap size: " + fh.size());

        Integer taken = fh.take();
        System.out.println("Taken element: " + taken);
        System.out.println("Heap size after take(): " + fh.size());

        Benchmark_Timer<FibonacciHeap<Integer>> fibonacciHeapBenchmark = new Benchmark_Timer<>(
                "Fibonacci Heap",
                heap -> heap.give(supplier.get()),
                heap -> {
                    for (int i = 0; i < deletions; i++) {
                        if (!heap.isEmpty()) {
                            heap.take();
                        }
                    }
                });

        double fibonacciHeapTime = fibonacciHeapBenchmark.runFromSupplier(
                () -> new FibonacciHeap<Integer>(Comparator.naturalOrder()),
                insertions);

        System.out.println("Fibonacci Heap Time: " + fibonacciHeapTime + " ms");

       
        Benchmark_Timer<PriorityQueue<Integer>> binaryHeapBenchmark = new Benchmark_Timer<>(
                "Binary Heap",
                pq -> pq.give(supplier.get()),
                pq -> {
                    for (int i = 0; i < deletions; i++)
                        pq.take();
                });

       
        Benchmark_Timer<PriorityQueue<Integer>> binaryHeapFloydBenchmark = new Benchmark_Timer<>(
                "Binary Heap with Floyd's Trick",
                pq -> pq.give(supplier.get()),
                pq -> {
                    for (int i = 0; i < deletions; i++)
                        pq.take();
                });

       
        Benchmark_Timer<PriorityQueue<Integer>> fourAryHeapBenchmark = new Benchmark_Timer<>(
                "4-Ary Heap",
                pq -> pq.give(supplier.get()),
                pq -> {
                    for (int i = 0; i < deletions; i++)
                        pq.take();
                });

       
        Benchmark_Timer<PriorityQueue<Integer>> fourAryHeapFloydBenchmark = new Benchmark_Timer<>(
                "4-Ary Heap with Floyd's Trick",
                pq -> pq.give(supplier.get()),
                pq -> {
                    for (int i = 0; i < deletions; i++)
                        pq.take();
                });


        double binaryHeapTime = binaryHeapBenchmark.runFromSupplier(
                () -> new PriorityQueue<Integer>(M, false, Comparator.naturalOrder(), false),
                insertions);

        double binaryHeapFloydTime = binaryHeapFloydBenchmark.runFromSupplier(
                () -> new PriorityQueue<Integer>(M, false, Comparator.naturalOrder(), true),
                insertions);

        double fourAryHeapTime = fourAryHeapBenchmark.runFromSupplier(
                () -> {
                    PriorityQueue<Integer> pq = new PriorityQueue<Integer>(M, false, Comparator.<Integer>naturalOrder(),
                            false);
                    pq.setArity(4);
                    return pq;
                },
                insertions);

        double fourAryHeapFloydTime = fourAryHeapFloydBenchmark.runFromSupplier(
                () -> {
                    PriorityQueue<Integer> pq = new PriorityQueue<Integer>(M, false, Comparator.<Integer>naturalOrder(),
                            true);
                    pq.setArity(4);
                    return pq;
                },
                insertions);

        System.out.println("Binary Heap Time: " + binaryHeapTime + " ms");
        System.out.println("Binary Heap (Floyd's Trick) Time: " + binaryHeapFloydTime + " ms");
        System.out.println("4-Ary Heap Time: " + fourAryHeapTime + " ms");
        System.out.println("4-Ary Heap (Floyd's Trick) Time: " + fourAryHeapFloydTime + " ms");
        System.out.println("Fibonacci Heap Time: " + fibonacciHeapTime + " ms");
    }
}
