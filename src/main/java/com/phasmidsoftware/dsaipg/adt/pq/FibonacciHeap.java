package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;

/**
 * Fibonacci Heap implementation for priority queue operations.
 */
public class FibonacciHeap<K> {
    private Node<K> min;
    private int size;
    private final Comparator<K> comparator;

    private static class Node<K> {
        K key;
        int degree;
        Node<K> parent, child, next, prev;
        boolean marked;

        Node(K key) {
            this.key = key;
            next = prev = this;
        }
    }

    public FibonacciHeap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public void give(K key) {
        Node<K> node = new Node<>(key);
        min = mergeLists(min, node);
        size++;
    }

    public K take() {
        if (min == null)
            throw new RuntimeException("Heap is empty");
        Node<K> z = min;
        if (z.child != null) {
            Node<K> child = z.child;
            do {
                child.parent = null;
                child = child.next;
            } while (child != z.child);
            min = mergeLists(min, z.child);
        }
        removeNode(z);
        if (z == z.next)
            min = null;
        else {
            min = z.next;
            consolidate();
        }
        size--;
        return z.key;
    }

    private void consolidate() {
        Node<K>[] array = new Node[45];
        Set<Node<K>> roots = new HashSet<>();
        Node<K> current = min;
        do {
            roots.add(current);
            current = current.next;
        } while (current != min);

        for (Node<K> node : roots) {
            int d = node.degree;
            while (array[d] != null) {
                Node<K> other = array[d];
                if (comparator.compare(node.key, other.key) > 0) {
                    Node<K> temp = node;
                    node = other;
                    other = temp;
                }
                link(other, node);
                array[d] = null;
                d++;
            }
            array[d] = node;
        }
        min = null;
        for (Node<K> node : array) {
            if (node != null)
                min = mergeLists(min, node);
        }
    }

    private void link(Node<K> child, Node<K> parent) {
        removeNode(child);
        child.next = child.prev = child;
        parent.child = mergeLists(parent.child, child);
        child.parent = parent;
        child.marked = false;
        parent.degree++;
    }

    private void removeNode(Node<K> node) {
        if (node.next == node)
            return;
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    private Node<K> mergeLists(Node<K> a, Node<K> b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        if (comparator.compare(a.key, b.key) > 0) {
            Node<K> temp = a;
            a = b;
            b = temp;
        }
        Node<K> an = a.next;
        a.next = b.next;
        a.next.prev = a;
        b.next = an;
        b.next.prev = b;
        return a;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}