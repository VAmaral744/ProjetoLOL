package com.projetolol;

public class GoldEfficiency {

    private static final double GOLD_PER_AD = 35;
    private static final double GOLD_PER_AP = 21.75;
    private static final double GOLD_PER_ARMOR = 20;
    private static final double GOLD_PER_MR = 18;
    private static final double GOLD_PER_HP = 2.67;
    private static final double GOLD_PER_ATTACK_SPEED = 25;
    private static final double GOLD_PER_CRIT = 40;
    private static final double GOLD_PER_ABILITY_HASTE = 26.67;

    public static double calculate(Item item) {

        ItemStats stats = item.getStats();

        double totalGoldValue = 0;

        totalGoldValue += stats.ad * GOLD_PER_AD;
        totalGoldValue += stats.ap * GOLD_PER_AP;
        totalGoldValue += stats.armor * GOLD_PER_ARMOR;
        totalGoldValue += stats.mr * GOLD_PER_MR;
        totalGoldValue += stats.hp * GOLD_PER_HP;
        totalGoldValue += stats.attackSpeed * GOLD_PER_ATTACK_SPEED;
        totalGoldValue += stats.crit * GOLD_PER_CRIT;
        totalGoldValue += stats.abilityHaste * GOLD_PER_ABILITY_HASTE;

        return (totalGoldValue / item.getCost()) * 100;
    }
}