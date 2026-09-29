package com.example.playwithgenerics.service;

import com.example.playwithgenerics.model.User;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GenericServiceTest {

    @Test
    void getItemsReturnsReadOnlySnapshot() {
        GenericService<String> service = new GenericService<>();
        service.addItem("a");
        List<String> items = service.getItems();
        assertThrows(UnsupportedOperationException.class, () -> items.add("b"));
        service.addItem("c");
        assertEquals(List.of("a"), items, "earlier snapshot must not change");
    }

    @Test
    void concurrentAddsAreNotLost() throws InterruptedException {
        GenericService<Integer> service = new GenericService<>();
        try (ExecutorService executor = Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 1_000; i++) {
                int value = i;
                executor.submit(() -> service.addItem(value));
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        }
        assertEquals(1_000, service.getItems().size());
    }

    @Test
    void maxAcceptsComparatorOfSupertype() {
        GenericService<String> service = new GenericService<>();
        service.addItem("kiwi");
        service.addItem("banana");
        Comparator<Object> byStringLength = Comparator.comparingInt(o -> o.toString().length());
        assertEquals("banana", service.max(byStringLength).orElseThrow());
    }

    @Test
    void mapProducesListOfAnotherType() {
        GenericService<User> service = new GenericService<>();
        service.addItem(new User("Ayşe", 30));
        service.addItem(new User("Mehmet", 40));
        List<String> names = service.map(User::name);
        assertEquals(List.of("Ayşe", "Mehmet"), names);
    }
}
