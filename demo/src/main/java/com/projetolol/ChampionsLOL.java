package com.projetolol;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChampionsLOL {
    public static void main(String[] args) throws Exception {
        // URL do Data Dragon (última versão padrão)
        String version = "13.21.1";
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

            // Nome
            String name = champ.path("name").asText();

            // Info
            JsonNode info = champ.path("info");

            // Tags (array de strings)
            JsonNode tagsNode = champ.path("tags");
            StringBuilder tagsBuilder = new StringBuilder();
            for (JsonNode tag : tagsNode) {
                if (tagsBuilder.length() > 0) tagsBuilder.append(", ");
                tagsBuilder.append(tag.asText());
            }
            String tags = tagsBuilder.toString();

            // Stats (estatísticas detalhadas)
            JsonNode stats = champ.path("stats");

            System.out.printf(
                    "Nome: %s%nInfo: %s%nTags: [%s]%nStats: %s%n%n",
                    name,
                    info.toString(),
                    tags,
                    stats.toString()
            );
        }
    }
}