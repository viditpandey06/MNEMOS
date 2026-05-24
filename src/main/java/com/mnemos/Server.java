package com.mnemos;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mnemos.data.DemoVectors;
import com.mnemos.store.VectorStore;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
    private static final int DIMS = 16;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        int port = getPort();
        VectorStore store = new VectorStore(DIMS);
        DemoVectors.load(store);

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        }).start("0.0.0.0", port);

        System.out.println("=== MNEMOS VectorDB Engine ===");
        System.out.println("http://localhost:" + port);
        System.out.println(store.size() + " vectors | " + DIMS + " dims | HNSW+KD-Tree+BruteForce");

        app.get("/api/search", ctx -> {
            String vParam = ctx.queryParam("v");
            if (vParam == null) {
                ctx.status(400).result("{\"error\":\"missing v parameter\"}");
                return;
            }
            double[] query = parseVector(vParam);
            if (query.length != DIMS) {
                ctx.status(400).result("{\"error\":\"need " + DIMS + "D vector\"}");
                return;
            }

            int k = ctx.queryParamAsClass("k", Integer.class).getOrDefault(5);
            String metric = ctx.queryParamAsClass("metric", String.class).getOrDefault("cosine");
            String algo = ctx.queryParamAsClass("algo", String.class).getOrDefault("hnsw");

            VectorStore.SearchResult result = store.search(query, k, metric, algo);
            ctx.result(gson.toJson(result)).contentType("application/json");
        });

        app.post("/api/insert", ctx -> {
            JsonObject body = gson.fromJson(ctx.body(), JsonObject.class);
            String metadata = body.get("metadata").getAsString();
            String category = body.get("category").getAsString();
            
            JsonArray embArray = body.getAsJsonArray("embedding");
            double[] embedding = new double[embArray.size()];
            for (int i = 0; i < embArray.size(); i++) {
                embedding[i] = embArray.get(i).getAsDouble();
            }

            if (embedding.length != DIMS) {
                ctx.status(400).result("{\"error\":\"need " + DIMS + "D vector\"}");
                return;
            }

            int id = store.insert(metadata, category, embedding);
            ctx.result("{\"status\":\"success\",\"id\":" + id + "}").contentType("application/json");
        });

        app.delete("/api/delete/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean removed = store.remove(id);
            if (removed) {
                ctx.result("{\"status\":\"success\"}").contentType("application/json");
            } else {
                ctx.status(404).result("{\"error\":\"not found\"}").contentType("application/json");
            }
        });

        app.get("/api/items", ctx -> {
            ctx.result(gson.toJson(store.getAll())).contentType("application/json");
        });

        app.get("/api/benchmark", ctx -> {
            String vParam = ctx.queryParam("v");
            if (vParam == null) {
                ctx.status(400).result("{\"error\":\"missing v parameter\"}");
                return;
            }
            double[] query = parseVector(vParam);
            int k = ctx.queryParamAsClass("k", Integer.class).getOrDefault(5);
            String metric = ctx.queryParamAsClass("metric", String.class).getOrDefault("cosine");

            ctx.result(gson.toJson(store.benchmark(query, k, metric))).contentType("application/json");
        });

        app.get("/api/benchmark-scale", ctx -> {
            // Generates dummy data and shows scaling difference
            int[] scales = {100, 500, 1000, 2000, 5000};
            List<JsonObject> results = new ArrayList<>();
            Random rng = new Random(42);
            double[] query = new double[DIMS];
            for (int i = 0; i < DIMS; i++) query[i] = rng.nextDouble();

            for (int n : scales) {
                VectorStore tempStore = new VectorStore(DIMS);
                for (int i = 0; i < n; i++) {
                    double[] v = new double[DIMS];
                    for (int d = 0; d < DIMS; d++) v[d] = rng.nextDouble();
                    tempStore.insert("dummy", "dummy", v);
                }
                
                long t1 = System.nanoTime();
                tempStore.search(query, 5, "cosine", "bruteforce");
                long bfTime = (System.nanoTime() - t1) / 1000;
                
                long t2 = System.nanoTime();
                tempStore.search(query, 5, "cosine", "hnsw");
                long hnswTime = (System.nanoTime() - t2) / 1000;
                
                JsonObject obj = new JsonObject();
                obj.addProperty("n", n);
                obj.addProperty("bruteForceUs", bfTime);
                obj.addProperty("hnswUs", hnswTime);
                results.add(obj);
            }
            
            ctx.result(gson.toJson(results)).contentType("application/json");
        });

        app.get("/api/hnsw-info", ctx -> {
            ctx.result(gson.toJson(store.getHnswInfo())).contentType("application/json");
        });

        app.get("/api/stats", ctx -> {
            JsonObject stats = new JsonObject();
            stats.addProperty("count", store.size());
            stats.addProperty("dims", DIMS);
            ctx.result(gson.toJson(stats)).contentType("application/json");
        });
        
        app.get("/api/health", ctx -> {
            ctx.result("OK");
        });
    }

    private static double[] parseVector(String s) {
        String[] parts = s.split(",");
        double[] v = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            v[i] = Double.parseDouble(parts[i]);
        }
        return v;
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port == null || port.isBlank()) {
            return 8080;
        }
        return Integer.parseInt(port);
    }
}
