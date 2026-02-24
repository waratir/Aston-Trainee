package com.trainee.module1;

public interface MyMap<K, V> {
    void put(K key, V value);

    V get(K key);

    boolean remove(K key);

    int size();

    boolean isEmpty();
}
