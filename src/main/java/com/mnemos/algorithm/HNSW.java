package com.mnemos.algorithm;

import com.mnemos.model.SearchHit;
import com.mnemos.model.VectorItem;

import java.util.*;
import java.util.function.BiFunction;

public class HNSW {
    private final Map<Integer, Node> graph = new HashMap<>();
    private int entryPoint = -1;
    private int topLayer = -1;

    private final int M;
    private final int M0;
    private final int efConstruction;
    private final double mL;
    private final Random rng = new Random(42);

    public HNSW(int m, int efConstruction) {
        this.M = m;
        this.M0 = 2 * m;
        this.efConstruction = efConstruction;
        this.mL = 1.0 / Math.log(m);
    }

    private static class Node {
        VectorItem item;
        int maxLayer;
        List<List<Integer>> neighbors;

        Node(VectorItem item, int maxLayer) {
            this.item = item;
            this.maxLayer = maxLayer;
            this.neighbors = new ArrayList<>(maxLayer + 1);
            for (int i = 0; i <= maxLayer; i++) {
                this.neighbors.add(new ArrayList<>());
            }
        }
    }

    public static class GraphInfo {
        public int topLayer;
        public int nodeCount;
        public List<Integer> nodesPerLayer = new ArrayList<>();
        public List<Integer> edgesPerLayer = new ArrayList<>();
    }

    private int randomLevel() {
        double r = -Math.log(rng.nextDouble()) * mL;
        return (int) Math.floor(r);
    }

    private List<SearchHit> searchLayer(double[] query, int ep, int ef, int layer, BiFunction<double[], double[], Double> distFn) {
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<SearchHit> candidates = new PriorityQueue<>(Comparator.comparing(SearchHit::getDistance));
        PriorityQueue<SearchHit> found = new PriorityQueue<>(Comparator.comparing(SearchHit::getDistance).reversed());

        double d0 = distFn.apply(query, graph.get(ep).item.getEmbedding());
        visited.add(ep);
        
        SearchHit initHit = new SearchHit(graph.get(ep).item, d0);
        candidates.offer(initHit);
        found.offer(initHit);

        while (!candidates.isEmpty()) {
            SearchHit c = candidates.poll();
            if (found.size() >= ef && c.getDistance() > found.peek().getDistance()) {
                break;
            }

            Node cNode = graph.get(c.getItem().getId());
            if (layer >= cNode.neighbors.size()) continue;

            for (int neighborId : cNode.neighbors.get(layer)) {
                if (!visited.contains(neighborId) && graph.containsKey(neighborId)) {
                    visited.add(neighborId);
                    double nd = distFn.apply(query, graph.get(neighborId).item.getEmbedding());
                    if (found.size() < ef || nd < found.peek().getDistance()) {
                        SearchHit nHit = new SearchHit(graph.get(neighborId).item, nd);
                        candidates.offer(nHit);
                        found.offer(nHit);
                        if (found.size() > ef) {
                            found.poll();
                        }
                    }
                }
            }
        }

        List<SearchHit> res = new ArrayList<>();
        while (!found.isEmpty()) {
            res.add(found.poll());
        }
        Collections.reverse(res);
        return res;
    }

    private List<Integer> selectNeighbors(List<SearchHit> candidates, int maxM) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < Math.min(candidates.size(), maxM); i++) {
            res.add(candidates.get(i).getItem().getId());
        }
        return res;
    }

    public void insert(VectorItem item, BiFunction<double[], double[], Double> distFn) {
        int id = item.getId();
        int l = randomLevel();
        Node newNode = new Node(item, l);
        graph.put(id, newNode);

        if (entryPoint == -1) {
            entryPoint = id;
            topLayer = l;
            return;
        }

        int ep = entryPoint;
        for (int lc = topLayer; lc > l; lc--) {
            if (lc < graph.get(ep).neighbors.size()) {
                List<SearchHit> w = searchLayer(item.getEmbedding(), ep, 1, lc, distFn);
                if (!w.isEmpty()) {
                    ep = w.get(0).getItem().getId();
                }
            }
        }

        for (int lc = Math.min(topLayer, l); lc >= 0; lc--) {
            List<SearchHit> w = searchLayer(item.getEmbedding(), ep, efConstruction, lc, distFn);
            int maxM = (lc == 0) ? M0 : M;
            List<Integer> neighbors = selectNeighbors(w, maxM);
            newNode.neighbors.set(lc, neighbors);

            for (int neighborId : neighbors) {
                if (!graph.containsKey(neighborId)) continue;
                Node neighborNode = graph.get(neighborId);
                
                while (neighborNode.neighbors.size() <= lc) {
                    neighborNode.neighbors.add(new ArrayList<>());
                }
                
                List<Integer> conn = neighborNode.neighbors.get(lc);
                conn.add(id);
                
                if (conn.size() > maxM) {
                    List<SearchHit> dists = new ArrayList<>();
                    for (int cId : conn) {
                        if (graph.containsKey(cId)) {
                            double d = distFn.apply(neighborNode.item.getEmbedding(), graph.get(cId).item.getEmbedding());
                            dists.add(new SearchHit(graph.get(cId).item, d));
                        }
                    }
                    Collections.sort(dists);
                    conn.clear();
                    for (int i = 0; i < maxM && i < dists.size(); i++) {
                        conn.add(dists.get(i).getItem().getId());
                    }
                }
            }
            if (!w.isEmpty()) {
                ep = w.get(0).getItem().getId();
            }
        }

        if (l > topLayer) {
            topLayer = l;
            entryPoint = id;
        }
    }

    public List<SearchHit> knn(double[] query, int k, int ef, BiFunction<double[], double[], Double> distFn) {
        if (entryPoint == -1) return new ArrayList<>();

        int ep = entryPoint;
        for (int lc = topLayer; lc > 0; lc--) {
            if (lc < graph.get(ep).neighbors.size()) {
                List<SearchHit> w = searchLayer(query, ep, 1, lc, distFn);
                if (!w.isEmpty()) {
                    ep = w.get(0).getItem().getId();
                }
            }
        }

        List<SearchHit> w = searchLayer(query, ep, Math.max(ef, k), 0, distFn);
        if (w.size() > k) {
            return w.subList(0, k);
        }
        return w;
    }

    public void remove(int id) {
        if (!graph.containsKey(id)) return;
        
        for (Node node : graph.values()) {
            for (List<Integer> layer : node.neighbors) {
                layer.remove(Integer.valueOf(id));
            }
        }
        
        if (entryPoint == id) {
            entryPoint = -1;
            for (int nid : graph.keySet()) {
                if (nid != id) {
                    entryPoint = nid;
                    break;
                }
            }
        }
        graph.remove(id);
    }

    public GraphInfo getInfo() {
        GraphInfo info = new GraphInfo();
        info.topLayer = topLayer;
        info.nodeCount = graph.size();
        
        int maxL = Math.max(topLayer + 1, 1);
        for (int i = 0; i < maxL; i++) {
            info.nodesPerLayer.add(0);
            info.edgesPerLayer.add(0);
        }
        
        for (Map.Entry<Integer, Node> entry : graph.entrySet()) {
            int id = entry.getKey();
            Node node = entry.getValue();
            
            for (int lc = 0; lc <= node.maxLayer && lc < maxL; lc++) {
                info.nodesPerLayer.set(lc, info.nodesPerLayer.get(lc) + 1);
                
                if (lc < node.neighbors.size()) {
                    for (int nid : node.neighbors.get(lc)) {
                        if (id < nid) { // Avoid double counting
                            info.edgesPerLayer.set(lc, info.edgesPerLayer.get(lc) + 1);
                        }
                    }
                }
            }
        }
        return info;
    }
}
