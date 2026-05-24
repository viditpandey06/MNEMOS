package com.mnemos.store;

import com.mnemos.algorithm.BruteForce;
import com.mnemos.algorithm.HNSW;
import com.mnemos.algorithm.KDTree;
import com.mnemos.metric.DistanceMetrics;
import com.mnemos.model.BenchmarkResult;
import com.mnemos.model.SearchHit;
import com.mnemos.model.VectorItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class VectorStore {
    private final BruteForce bruteForce;
    private final KDTree kdTree;
    private final HNSW hnsw;
    private final Map<Integer, VectorItem> store;
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final int dims;

    public VectorStore(int dims) {
        this.dims = dims;
        this.bruteForce = new BruteForce();
        this.kdTree = new KDTree(dims);
        this.hnsw = new HNSW(16, 200);
        this.store = new HashMap<>();
    }

    public synchronized int insert(String metadata, String category, double[] embedding) {
        int id = nextId.getAndIncrement();
        VectorItem item = new VectorItem(id, metadata, category, embedding);
        store.put(id, item);
        
        // We use Euclidean for KDTree, but Cosine/Euclidean/Manhattan can be passed to search
        // Default insert uses Cosine for HNSW
        bruteForce.insert(item);
        kdTree.insert(item);
        hnsw.insert(item, DistanceMetrics.getFunction("cosine"));
        
        return id;
    }

    public synchronized boolean remove(int id) {
        if (!store.containsKey(id)) {
            return false;
        }
        store.remove(id);
        bruteForce.remove(id);
        hnsw.remove(id);
        
        // KDTree needs full rebuild
        kdTree.rebuild(new ArrayList<>(store.values()));
        return true;
    }

    public synchronized SearchResult search(double[] query, int k, String metric, String algo) {
        BiFunction<double[], double[], Double> distFn = DistanceMetrics.getFunction(metric);
        long startTime = System.nanoTime();
        
        List<SearchHit> rawHits;
        if ("bruteforce".equalsIgnoreCase(algo)) {
            rawHits = bruteForce.knn(query, k, distFn);
        } else if ("kdtree".equalsIgnoreCase(algo)) {
            rawHits = kdTree.knn(query, k, distFn);
        } else {
            rawHits = hnsw.knn(query, k, 50, distFn);
            if (rawHits.isEmpty() || rawHits.get(0).getDistance() > 0.35) {
                rawHits = bruteForce.knn(query, k, distFn);
            }
        }
        
        long endTime = System.nanoTime();
        long latencyUs = (endTime - startTime) / 1000;
        
        return new SearchResult(rawHits, latencyUs, algo, metric);
    }

    public synchronized BenchmarkResult benchmark(double[] query, int k, String metric) {
        BiFunction<double[], double[], Double> distFn = DistanceMetrics.getFunction(metric);
        
        long t1 = System.nanoTime();
        bruteForce.knn(query, k, distFn);
        long bfTime = (System.nanoTime() - t1) / 1000;
        
        long t2 = System.nanoTime();
        kdTree.knn(query, k, distFn);
        long kdTime = (System.nanoTime() - t2) / 1000;
        
        long t3 = System.nanoTime();
        hnsw.knn(query, k, 50, distFn);
        long hnswTime = (System.nanoTime() - t3) / 1000;
        
        return new BenchmarkResult(bfTime, kdTime, hnswTime, store.size());
    }

    public synchronized List<VectorItem> getAll() {
        return new ArrayList<>(store.values());
    }

    public synchronized HNSW.GraphInfo getHnswInfo() {
        return hnsw.getInfo();
    }

    public synchronized int size() {
        return store.size();
    }
    
    public int getDims() {
        return dims;
    }
    
    public static class SearchResult {
        public List<SearchHit> hits;
        public long latencyUs;
        public String algo;
        public String metric;

        public SearchResult(List<SearchHit> hits, long latencyUs, String algo, String metric) {
            this.hits = hits;
            this.latencyUs = latencyUs;
            this.algo = algo;
            this.metric = metric;
        }
    }
}
