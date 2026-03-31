package com.projetolol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ChampionLoader {
    private static final String VERSION = "13.21.1";
    private static final String API_URL = "https://ddragon.leagueoflegends.com/cdn/" + VERSION + "/data/en_US/champion.json";

    public static List<Champion> loadChampions() throws Exception {
        List<Champion> champions = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode championsData = root.path("data");

        Iterator<Map.Entry<String, JsonNode>> fields = championsData.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode champData = entry.getValue();

            String id = champData.path("id").asText();
            String name = champData.path("name").asText();

            // Tags
            List<String> tags = new ArrayList<>();
            JsonNode tagsNode = champData.path("tags");
            for (JsonNode tag : tagsNode) {
                tags.add(tag.asText());
            }

            // Stats
            JsonNode stats = champData.path("info");
            double attack = stats.path("attack").asDouble(0);
            double defense = stats.path("defense").asDouble(0);
            double magic = stats.path("magic").asDouble(0);
            double difficulty = stats.path("difficulty").asDouble(0);

            Champion champion = new Champion(id, name, tags, attack, defense, magic, difficulty);
            champions.add(champion);
        }

        return champions;
    }
}
