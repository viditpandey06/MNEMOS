package com.mnemos.model;

import java.util.Arrays;

public class VectorItem {
    private int id;
    private String metadata;
    private String category;
    private double[] embedding;

    public VectorItem(int id, String metadata, String category, double[] embedding) {
        this.id = id;
        this.metadata = metadata;
        this.category = category;
        this.embedding = embedding;
    }

    public int getId() { return id; }
    public String getMetadata() { return metadata; }
    public String getCategory() { return category; }
    public double[] getEmbedding() { return embedding; }

    @Override
    public String toString() {
        return "VectorItem{" +
                "id=" + id +
                ", metadata='" + metadata + '\'' +
                ", category='" + category + '\'' +
                ", embedding=" + Arrays.toString(embedding) +
                '}';
    }
}
