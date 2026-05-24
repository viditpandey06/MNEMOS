package com.mnemos.algorithm;

import com.mnemos.model.SearchHit;
import com.mnemos.model.VectorItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class BruteForce {
    private final List<VectorItem> items = new ArrayList<>();

    public void insert(VectorItem item) {
        items.add(item);
    }

    public void remove(int id) {
        items.removeIf(item -> item.getId() == id);
    }

    public List<SearchHit> knn(double[] query, int k, BiFunction<double[], double[], Double> distFn) {
        List<SearchHit> results = new ArrayList<>();
        for (VectorItem item : items) {
            double dist = distFn.apply(query, item.getEmbedding());
            results.add(new SearchHit(item, dist));
        }
        Collections.sort(results);
        if (results.size() > k) {
            return results.subList(0, k);
        }
        return results;
    }
}
