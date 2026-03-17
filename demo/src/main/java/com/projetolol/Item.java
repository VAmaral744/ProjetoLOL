package com.projetolol;

public class Item {

    private String name;
    private int cost;
    private ItemStats stats;

    public Item(String name, int cost, ItemStats stats) {
        this.name = name;
        this.cost = cost;
        this.stats = stats;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public ItemStats getStats() {
        return stats;
    }
}