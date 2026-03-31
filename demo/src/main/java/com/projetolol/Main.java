package com.projetolol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(80));
        System.out.println("LEAGUE OF LEGENDS - BEST CHAMPION & BUILD ANALYZER");
        System.out.println("=".repeat(80));

        // Carrega campeões
        System.out.println("\n[1/4] Carregando campeões da API...");
        List<Champion> champions = ChampionLoader.loadChampions();
        System.out.println("✓ " + champions.size() + " campeões carregados!");

        // Carrega itens
        System.out.println("\n[2/4] Carregando itens da API...");
        List<Item> items = ItemLoader.loadItems();
        System.out.println("✓ " + items.size() + " itens carregados!");

        // Analisa melhores campeões
        System.out.println("\n[3/4] Analisando melhores campeões...");
        analyzeBestChampions(champions);

        // Recomenda melhores builds
        System.out.println("\n[4/4] Analisando melhores builds...");
        recommendBestBuilds(items);

        System.out.println("\n" + "=".repeat(80));
        System.out.println("Análise concluída!");
        System.out.println("=".repeat(80));
    }

    private static void analyzeBestChampions(List<Champion> champions) {
        System.out.println("\n>>> MELHOR CAMPEÃO GERAL <<<");
        Champion best = ChampionAnalyzer.findBestOverallChampion(champions);
        if (best != null) {
            System.out.printf("Nome: %s%n", best.getName());
            System.out.printf("Roles: %s%n", String.join(", ", best.getTags()));
            System.out.printf("Attack: %.2f | Defense: %.2f | Magic: %.2f | Difficulty: %.2f%n",
                    best.getAttack(), best.getDefense(), best.getMagic(), best.getDifficulty());
            System.out.printf("Power Score: %.2f%n", best.getPowerScore());
        }

        System.out.println("\n>>> TOP 10 MELHORES CAMPEÕES <<<");
        List<Champion> top10 = ChampionAnalyzer.getTopChampions(champions, 10);
        int rank = 1;
        for (Champion champ : top10) {
            System.out.printf("%d. %s (Power Score: %.2f)%n", rank++, champ.getName(), champ.getPowerScore());
        }

        System.out.println("\n>>> SPECIALISTAS <<<");
        System.out.println("Melhor Ataque: " + ChampionAnalyzer.findBestAttacker(champions).getName());
        System.out.println("Melhor Defesa: " + ChampionAnalyzer.findBestDefender(champions).getName());
        System.out.println("Melhor Magia: " + ChampionAnalyzer.findBestMage(champions).getName());

        System.out.println("\n>>> MELHORES POR ROLE <<<");
        List<String> roles = new ArrayList<>();
        roles.add("Marksman");
        roles.add("Mage");
        roles.add("Support");
        roles.add("Tank");
        roles.add("Assassin");

        Map<String, Champion> bestPerRole = ChampionAnalyzer.getBestChampionPerRole(champions, roles);
        for (Map.Entry<String, Champion> entry : bestPerRole.entrySet()) {
            if (entry.getValue() != null) {
                System.out.printf("  %s: %s (Power Score: %.2f)%n",
                        entry.getKey(),
                        entry.getValue().getName(),
                        entry.getValue().getPowerScore());
            }
        }
    }

    private static void recommendBestBuilds(List<Item> items) {
        System.out.println("\n>>> TOP 10 ITENS MAIS EFICIENTES <<<");
        List<Item> bestItems = BuildRecommender.findMostEfficientItems(items, 10);
        int rank = 1;
        for (Item item : bestItems) {
            System.out.printf("%d. %s - Custo: %d | Eficiência: %.2f%%%n",
                    rank++,
                    item.getName(),
                    item.getCost(),
                    GoldEfficiency.calculate(item));
        }

        System.out.println("\n>>> TOP 5 MELHORES ITENS OFENSIVOS <<<");
        List<Item> offensiveItems = BuildRecommender.findBestOffensiveItems(items, 5);
        for (int i = 0; i < offensiveItems.size(); i++) {
            Item item = offensiveItems.get(i);
            System.out.printf("%d. %s - AD: %.2f | AP: %.2f | Custo: %d%n",
                    i + 1,
                    item.getName(),
                    item.getStats().ad,
                    item.getStats().ap,
                    item.getCost());
        }

        System.out.println("\n>>> TOP 5 MELHORES ITENS DEFENSIVOS <<<");
        List<Item> defensiveItems = BuildRecommender.findBestDefensiveItems(items, 5);
        for (int i = 0; i < defensiveItems.size(); i++) {
            Item item = defensiveItems.get(i);
            System.out.printf("%d. %s - Armor: %.2f | MR: %.2f | HP: %.2f | Custo: %d%n",
                    i + 1,
                    item.getName(),
                    item.getStats().armor,
                    item.getStats().mr,
                    item.getStats().hp,
                    item.getCost());
        }

        System.out.println("\n>>> BUILD RECOMENDADA (OTIMização com orçamento) <<<");
        double budget = 5000; // Orçamento em ouro
        List<Item> recommendedBuild = BuildRecommender.findBestBuild(items, budget, 6);
        System.out.printf("Orçamento: %d ouro (TOP 6 itens mais eficientes)%n%n", (int) budget);

        int buildRank = 1;
        double totalCost = 0;
        double totalDamage = 0;
        double totalDefense = 0;

        for (Item item : recommendedBuild) {
            double efficiency = GoldEfficiency.calculate(item);
            System.out.printf("%d. %s%n", buildRank++, item.getName());
            System.out.printf("   Custo: %d | Eficiência: %.2f%%%n", item.getCost(), efficiency);
            System.out.printf("   Dano (AD+AP): %.2f | Defesa (Armor+MR): %.2f%n",
                    item.getStats().ad + item.getStats().ap,
                    item.getStats().armor + item.getStats().mr);
            totalCost += item.getCost();
            totalDamage += item.getStats().ad + item.getStats().ap;
            totalDefense += item.getStats().armor + item.getStats().mr;
        }

        System.out.println("\n--- Resumo da Build ---");
        System.out.printf("Custo Total: %d ouro%n", (int) totalCost);
        System.out.printf("Dano Total: %.2f%n", totalDamage);
        System.out.printf("Defesa Total: %.2f%n", totalDefense);
        System.out.printf("Eficiência Média: %.2f%%%n", BuildRecommender.calculateBuildEfficiency(recommendedBuild));
    }
}
