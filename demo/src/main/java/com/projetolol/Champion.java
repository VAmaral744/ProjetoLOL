package com.projetolol;

import java.util.List;

public class Champion {
    private String id;
    private String name;
    private List<String> tags;
    private double attack;
    private double defense;
    private double magic;
    private double difficulty;

    public Champion(String id, String name, List<String> tags, 
                    double attack, double defense, double magic, double difficulty) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.difficulty = difficulty;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getTags() { return tags; }
    public double getAttack() { return attack; }
    public double getDefense() { return defense; }
    public double getMagic() { return magic; }
    public double getDifficulty() { return difficulty; }

    // Calcula power score do campeão
    public double getPowerScore() {
        return (attack * 0.3) + (defense * 0.25) + (magic * 0.25) + (difficulty * 0.2);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - Power Score: %.2f", name, String.join(", ", tags), getPowerScore());
    }
}
