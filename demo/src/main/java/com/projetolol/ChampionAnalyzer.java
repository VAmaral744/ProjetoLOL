package com.projetolol;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChampionAnalyzer {

    public static Champion findBestOverallChampion(List<Champion> champions) {
        return champions.stream()
                .max(Comparator.comparingDouble(Champion::getPowerScore))
                .orElse(null);
    }

    public static Champion findBestAttacker(List<Champion> champions) {
        return champions.stream()
                .max(Comparator.comparingDouble(Champion::getAttack))
                .orElse(null);
    }

    public static Champion findBestDefender(List<Champion> champions) {
        return champions.stream()
                .max(Comparator.comparingDouble(Champion::getDefense))
                .orElse(null);
    }

    public static Champion findBestMage(List<Champion> champions) {
        return champions.stream()
                .max(Comparator.comparingDouble(Champion::getMagic))
                .orElse(null);
    }

    public static Champion findBestByRole(List<Champion> champions, String role) {
        return champions.stream()
                .filter(c -> c.getTags().contains(role))
                .max(Comparator.comparingDouble(Champion::getPowerScore))
                .orElse(null);
    }

    public static List<Champion> getTopChampions(List<Champion> champions, int limit) {
        return champions.stream()
                .sorted(Comparator.comparingDouble(Champion::getPowerScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static Map<String, Champion> getBestChampionPerRole(List<Champion> champions, List<String> roles) {
        return roles.stream()
                .collect(Collectors.toMap(
                        role -> role,
                        role -> findBestByRole(champions, role),
                        (v1, v2) -> v1
                ));
    }
}
