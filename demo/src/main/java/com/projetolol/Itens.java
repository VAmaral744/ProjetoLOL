package com.projetolol;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Itens {

    public static void main(String[] args) throws Exception {

        String version = "13.21.1";
        String url = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/item.json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        JsonNode items = root.path("data");

        Iterator<Map.Entry<String, JsonNode>> fields = items.fields();

        while (fields.hasNext()) {

            Map.Entry<String, JsonNode> entry = fields.next();

            String itemId = entry.getKey();
            JsonNode item = entry.getValue();

            // Nome
            String name = item.path("name").asText();

            // Tags
            JsonNode tagsNode = item.path("tags");
            StringBuilder tagsBuilder = new StringBuilder();

            for (JsonNode tag : tagsNode) {
                if (tagsBuilder.length() > 0) tagsBuilder.append(", ");
                tagsBuilder.append(tag.asText());
            }

            String tags = tagsBuilder.toString();

            // Custos
            JsonNode gold = item.path("gold");

            int totalCost = gold.path("total").asInt();
            int sellValue = gold.path("sell").asInt();

            // Stats
            JsonNode stats = item.path("stats");

            // Construção
            JsonNode from = item.path("from");

            StringBuilder buildFrom = new StringBuilder();
            for (JsonNode comp : from) {
                if (buildFrom.length() > 0) buildFrom.append(", ");
                buildFrom.append(comp.asText());
            }

            // Evoluções
            JsonNode into = item.path("into");

            StringBuilder buildInto = new StringBuilder();
            for (JsonNode comp : into) {
                if (buildInto.length() > 0) buildInto.append(", ");
                buildInto.append(comp.asText());
            }

            // Passivas / Ativas
            String descriptionRaw = item.path("description").asText();
            String description = limparDescricao(descriptionRaw);

            // Texto simplificado da passiva
            String plaintext = item.path("plaintext").asText();

            System.out.println("=================================");
            System.out.println("ID: " + itemId);
            System.out.println("Nome: " + name);
            System.out.println("Tags: " + tags);

            System.out.println("\n--- Economia ---");
            System.out.println("Custo Total: " + totalCost);
            System.out.println("Valor de Venda: " + sellValue);

            System.out.println("\n--- Stats ---");
            System.out.println(stats.toPrettyString());

            System.out.println("\n--- Construção ---");
            System.out.println("Build From: " + buildFrom);
            System.out.println("Build Into: " + buildInto);

            System.out.println("\n--- Passivas / Ativas ---");
            System.out.println(description);

            if (!plaintext.isEmpty()) {
                System.out.println("\nResumo da Passiva:");
                System.out.println(plaintext);
            }

            System.out.println("=================================\n");
        }
    }

    // Função que limpa HTML do Data Dragon
    public static String limparDescricao(String texto) {

        return texto
                .replaceAll("<br>", "\n")
                .replaceAll("<li>", "\n")
                .replaceAll("<.*?>", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}