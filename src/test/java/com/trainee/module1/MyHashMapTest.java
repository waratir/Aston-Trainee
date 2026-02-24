package com.trainee.module1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {
    private MyMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new MyHashMap<>();
    }

    @Test
    @DisplayName("Adding and Getting common values")
    void shouldPutAndGetValues() {
        map.put("Apple", 10);
        map.put("Banana", 20);

        assertEquals(10, map.get("Apple"));
        assertEquals(20, map.get("Banana"));
        assertEquals(2, map.size());
    }

    @Test
    @DisplayName("Updating a value by an existing key")
    void shouldUpdateValueWhenKeyExists() {
        map.put("Alex", 32);
        map.put("Alex", 33);

        assertEquals(33, map.get("Alex"));
        assertEquals(1, map.size(), "The size should not increase when updating the key.");
    }

    @Test
    @DisplayName("Collision handling")
    void shouldHandleCollisions() {
        String key1 = "Aa";
        String key2 = "BB";

        map.put(key1, 100);
        map.put(key2, 200);

        assertEquals(100, map.get(key1));
        assertEquals(200, map.get(key2));
        assertEquals(2, map.size());
    }

    @Test
    @DisplayName("Working with a null key")
    void shouldHandleNullKey() {
        map.put(null, 999);
        assertEquals(999, map.get(null));
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Removing an element from the head")
    void shouldRemoveHeadElement() {
        map.put("Aa", 1);
        map.put("BB", 2);

        boolean removed = map.remove("BB");

        assertTrue(removed);
        assertNull(map.get("BB"));
        assertEquals(1, map.get("Aa"));
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Removing an element from the middle/end of a chain")
    void shouldRemoveTailElement() {
        map.put("Aa", 1);
        map.put("BB", 2);

        boolean removed = map.remove("Aa");

        assertTrue(removed);
        assertNull(map.get("Aa"));
        assertEquals(2, map.get("BB"));
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Search for a non-existent key")
    void shouldReturnNullForMissingKey() {
        assertNull(map.get("Goal"));
        assertFalse(map.remove("Goal"));
    }

    @Test
    @DisplayName("Checking if a map is empty")
    void shouldCheckIsEmpty() {
        assertTrue(map.isEmpty());
        map.put("Key", 1);
        assertFalse(map.isEmpty());
    }
}
