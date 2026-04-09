package com.trainee.module1;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> head = buckets[index];

        Node<K, V> current = head;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, head);
        buckets[index] = newNode;
        size++;
    }

    @Override
    public V get(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean remove(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    private int getIndex(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode() % buckets.length);
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
