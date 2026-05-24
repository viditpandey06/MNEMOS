package com.mnemos.model;

public class BenchmarkResult {
    private long bruteForceUs;
    private long kdTreeUs;
    private long hnswUs;
    private int n;

    public BenchmarkResult(long bruteForceUs, long kdTreeUs, long hnswUs, int n) {
        this.bruteForceUs = bruteForceUs;
        this.kdTreeUs = kdTreeUs;
        this.hnswUs = hnswUs;
        this.n = n;
    }
    
    public long getBruteForceUs() { return bruteForceUs; }
    public long getKdTreeUs() { return kdTreeUs; }
    public long getHnswUs() { return hnswUs; }
    public int getN() { return n; }
}
