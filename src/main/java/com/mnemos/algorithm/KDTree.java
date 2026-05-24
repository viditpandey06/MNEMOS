package com.mnemos.algorithm;

import com.mnemos.model.SearchHit;
import com.mnemos.model.VectorItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

public class KDTree {
    private KDNode root;
    private final int dims;

    private static class KDNode {
        VectorItem item;
        KDNode left, right;

        KDNode(VectorItem item) {
            this.item = item;
        }
    }

    public KDTree(int dims) {
        this.dims = dims;
    }

    public void insert(VectorItem item) {
        root = insertRec(root, item, 0);
    }

    private KDNode insertRec(KDNode node, VectorItem item, int depth) {
        if (node == null) {
            return new KDNode(item);
        }
        int axis = depth % dims;
        if (item.getEmbedding()[axis] < node.item.getEmbedding()[axis]) {
            node.left = insertRec(node.left, item, depth + 1);
        } else {
            node.right = insertRec(node.right, item, depth + 1);
        }
        return node;
    }

    public void rebuild(List<VectorItem> items) {
        root = null;
        for (VectorItem item : items) {
            insert(item);
        }
    }

    public List<SearchHit> knn(double[] query, int k, BiFunction<double[], double[], Double> distFn) {
        PriorityQueue<SearchHit> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        knnRec(root, query, k, 0, distFn, maxHeap);

        List<SearchHit> results = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            results.add(maxHeap.poll());
        }
        Collections.reverse(results);
        return results;
    }

    private void knnRec(KDNode node, double[] query, int k, int depth,
                        BiFunction<double[], double[], Double> distFn, PriorityQueue<SearchHit> maxHeap) {
        if (node == null) return;

        double dist = distFn.apply(query, node.item.getEmbedding());
        if (maxHeap.size() < k || dist < maxHeap.peek().getDistance()) {
            maxHeap.offer(new SearchHit(node.item, dist));
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        int axis = depth % dims;
        double diff = query[axis] - node.item.getEmbedding()[axis];
        
        KDNode closer = diff < 0 ? node.left : node.right;
        KDNode farther = diff < 0 ? node.right : node.left;

        knnRec(closer, query, k, depth + 1, distFn, maxHeap);

        if (maxHeap.size() < k || Math.abs(diff) < maxHeap.peek().getDistance()) {
            knnRec(farther, query, k, depth + 1, distFn, maxHeap);
        }
    }
}
