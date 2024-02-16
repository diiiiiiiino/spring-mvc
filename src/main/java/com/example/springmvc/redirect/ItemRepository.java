package com.example.springmvc.redirect;

import java.util.HashMap;
import java.util.Map;

public class ItemRepository {
    Map<Long, Item> db = new HashMap<>();

    public Item save(Item item){
        db.put(item.getId(), item);

        return item;
    }
}
