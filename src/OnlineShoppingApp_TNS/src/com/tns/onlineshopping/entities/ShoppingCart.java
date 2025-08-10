package com.tns.onlineshopping.entities;


//===== com/tns/onlineshopping/entities/ShoppingCart.java =====


import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
 // Map productId -> quantity
 private Map<Integer, Integer> items = new LinkedHashMap<>();

 public void addItem(int productId, int quantity) {
     items.put(productId, items.getOrDefault(productId, 0) + quantity);
 }

 public Map<Integer, Integer> getItems() { return items; }

 public void clear() { items.clear(); }
}

