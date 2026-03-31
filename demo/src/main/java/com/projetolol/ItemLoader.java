package com.projetolol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ItemLoader {
    private static final String VERSION = "13.21.1";
    private static final String API_URL = "https://ddragon.leagueoflegends.com/cdn/" + VERSION + "/data/en_US/item.json";

    public static List<Item> loadItems() throws Exception {
        List<Item> items = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode itemsData = root.path("data");

        Iterator<Map.Entry<String, JsonNode>> fields = itemsData.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode itemData = entry.getValue();

            String name = itemData.path("name").asText();

            // Custo
            JsonNode goldNode = itemData.path("gold");
            int cost = goldNode.path("total").asInt(0);

            // Ignorar itens que são construções (sem custo próprio)
            if (cost == 0) continue;

            // Stats
            ItemStats stats = new ItemStats();
            JsonNode statsNode = itemData.path("stats");
            stats.ad = statsNode.path("FlatPhysicalDamageMod").asDouble(0);
            stats.ap = statsNode.path("FlatMagicDamageMod").asDouble(0);
            stats.armor = statsNode.path("FlatArmorMod").asDouble(0);
            stats.mr = statsNode.path("FlatSpellBlockMod").asDouble(0);
            stats.hp = statsNode.path("FlatHPPoolMod").asDouble(0);
            stats.attackSpeed = statsNode.path("PercentAttackSpeedMod").asDouble(0);
            stats.crit = statsNode.path("FlatCritChanceMod").asDouble(0);
            stats.abilityHaste = statsNode.path("FlatCooldownMod").asDouble(0);

            Item item = new Item(name, cost, stats);
            items.add(item);
        }

        return items;
    }
}
