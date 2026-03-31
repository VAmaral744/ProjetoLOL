package com.projetolol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BuildRecommender {

    public static List<Item> findBestBuild(List<Item> items, double goldBudget, int itemLimit) {
        return items.stream()
                .filter(i -> i.getCost() <= goldBudget)
                .sorted(Comparator.comparingDouble(GoldEfficiency::calculate).reversed())
                .limit(itemLimit)
                .collect(Collectors.toList());
    }

    public static List<Item> findMostEfficientItems(List<Item> items, int limit) {
        return items.stream()
                .sorted(Comparator.comparingDouble(GoldEfficiency::calculate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static List<Item> findBestOffensiveItems(List<Item> items, int limit) {
        return items.stream()
                .filter(item -> isDamageItem(item))
                .sorted((a, b) -> Double.compare(
                        b.getStats().ad + b.getStats().ap,
                        a.getStats().ad + a.getStats().ap))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static List<Item> findBestDefensiveItems(List<Item> items, int limit) {
        return items.stream()
                .filter(item -> isDefensiveItem(item))
                .sorted((a, b) -> Double.compare(
                        b.getStats().armor + b.getStats().mr,
                        a.getStats().armor + a.getStats().mr))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static double calculateBuildEfficiency(List<Item> build) {
        return build.stream()
                .mapToDouble(GoldEfficiency::calculate)
                .average()
                .orElse(0);
    }

    public static double calculateBuildCost(List<Item> build) {
        return build.stream()
                .mapToInt(Item::getCost)
                .sum();
    }

    public static double calculateBuildDamage(List<Item> build) {
        return build.stream()
                .mapToDouble(i -> i.getStats().ad + i.getStats().ap)
                .sum();
    }

    public static double calculateBuildDefense(List<Item> build) {
        return build.stream()
                .mapToDouble(i -> i.getStats().armor + i.getStats().mr + (i.getStats().hp / 10))
                .sum();
    }

    public static boolean isDamageItem(Item item) {
        ItemStats stats = item.getStats();
        return (stats.ad > 0 || stats.ap > 0) && 
               (stats.armor <= 0 && stats.mr <= 0);
    }

    public static boolean isDefensiveItem(Item item) {
        ItemStats stats = item.getStats();
        return (stats.armor > 0 || stats.mr > 0 || stats.hp > 0);
    }
}
