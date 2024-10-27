package com.example.playwithgenerics.service;


import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenericService<T> {

    private final List<T> items = new ArrayList<>();

    public void addItem(T item) {
        items.add(item);
    }

    public List<T> getItems() {
        return items;
    }

    public String displayItem(T item) {
        return "Item: " + item.toString();
    }
}
