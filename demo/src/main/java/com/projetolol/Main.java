package com.projetolol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        // URL do Data Dragon (última versão padrão)
        String version = "13.21.1"; // ou buscar dinamicamente https://ddragon.leagueoflegends.com/api/versions.json
        String url = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/champion.json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        JsonNode champions = root.path("data");

        Iterator<Map.Entry<String, JsonNode>> fields = champions.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode champ = entry.getValue();

            String name = champ.path("name").asText();
            String title = champ.path("title").asText();
            JsonNode stats = champ.path("info");

            int attack = stats.path("attack").asInt();
            int defense = stats.path("defense").asInt();
            int magic = stats.path("magic").asInt();
            int difficulty = stats.path("difficulty").asInt();

            System.out.printf("Nome: %s, Título: %s, Ataque: %d, Defesa: %d, Magia: %d, Dificuldade: %d%n",
                    name, title, attack, defense, magic, difficulty);
        }
    }
}