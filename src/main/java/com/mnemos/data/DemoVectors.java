package com.mnemos.data;

import com.mnemos.store.VectorStore;

public class DemoVectors {

    public static void load(VectorStore store) {
        // CS (0-2)
        store.insert("Linked List: nodes connected by pointers", "cs",
            new double[]{0.90, 0.85, 0.72, 0.12, 0.08, 0.15, 0.10, 0.05, 0.08, 0.06, 0.09, 0.07, 0.11, 0.08, 0.06, 0.05});
        store.insert("Binary Search Tree: O(log n) search and insert", "cs",
            new double[]{0.88, 0.82, 0.78, 0.15, 0.10, 0.08, 0.12, 0.06, 0.07, 0.08, 0.05, 0.09, 0.06, 0.07, 0.10, 0.06});
        store.insert("Dynamic Programming: memoization overlapping subproblems", "cs",
            new double[]{0.82, 0.76, 0.88, 0.20, 0.18, 0.12, 0.09, 0.07, 0.06, 0.08, 0.07, 0.08, 0.09, 0.06, 0.07, 0.08});
        store.insert("Graph BFS and DFS: breadth and depth first traversal", "cs",
            new double[]{0.85, 0.80, 0.75, 0.18, 0.14, 0.10, 0.08, 0.06, 0.09, 0.07, 0.06, 0.10, 0.08, 0.09, 0.07, 0.05});
        store.insert("Hash Table: O(1) lookup with collision chaining", "cs",
            new double[]{0.87, 0.78, 0.70, 0.13, 0.11, 0.09, 0.14, 0.08, 0.07, 0.06, 0.08, 0.07, 0.10, 0.08, 0.09, 0.06});

        // Math (3-5)
        store.insert("Calculus: derivatives integrals and limits", "math",
            new double[]{0.12, 0.15, 0.18, 0.91, 0.86, 0.78, 0.08, 0.06, 0.07, 0.09, 0.07, 0.08, 0.06, 0.10, 0.09, 0.08});
        store.insert("Linear Algebra: matrices eigenvalues eigenvectors", "math",
            new double[]{0.20, 0.18, 0.15, 0.88, 0.90, 0.82, 0.09, 0.07, 0.08, 0.06, 0.10, 0.07, 0.08, 0.09, 0.07, 0.08});
        store.insert("Probability: distributions random variables Bayes theorem", "math",
            new double[]{0.15, 0.12, 0.20, 0.84, 0.80, 0.88, 0.07, 0.08, 0.06, 0.10, 0.09, 0.06, 0.09, 0.08, 0.09, 0.07});
        store.insert("Number Theory: primes modular arithmetic RSA cryptography", "math",
            new double[]{0.22, 0.16, 0.14, 0.80, 0.85, 0.76, 0.08, 0.09, 0.07, 0.06, 0.08, 0.10, 0.07, 0.06, 0.08, 0.09});
        store.insert("Combinatorics: permutations combinations generating functions", "math",
            new double[]{0.18, 0.20, 0.16, 0.86, 0.78, 0.84, 0.06, 0.07, 0.09, 0.08, 0.06, 0.09, 0.10, 0.07, 0.06, 0.05});

        // Food (6-8)
        store.insert("Neapolitan Pizza: wood-fired dough San Marzano tomatoes", "food",
            new double[]{0.08, 0.06, 0.09, 0.07, 0.08, 0.06, 0.90, 0.86, 0.78, 0.08, 0.06, 0.09, 0.07, 0.08, 0.06, 0.05});
        store.insert("Sushi: vinegared rice raw fish and nori rolls", "food",
            new double[]{0.06, 0.08, 0.07, 0.09, 0.06, 0.08, 0.86, 0.90, 0.82, 0.07, 0.09, 0.06, 0.08, 0.07, 0.09, 0.06});
        store.insert("Ramen: noodle soup with chashu pork and soft-boiled eggs", "food",
            new double[]{0.09, 0.07, 0.06, 0.08, 0.09, 0.07, 0.82, 0.78, 0.90, 0.09, 0.07, 0.08, 0.06, 0.09, 0.07, 0.06});
        store.insert("Tacos: corn tortillas with carnitas salsa and cilantro", "food",
            new double[]{0.07, 0.09, 0.08, 0.06, 0.07, 0.09, 0.78, 0.82, 0.86, 0.06, 0.08, 0.07, 0.09, 0.06, 0.08, 0.07});
        store.insert("Croissant: laminated pastry with buttery flaky layers", "food",
            new double[]{0.06, 0.07, 0.10, 0.09, 0.06, 0.07, 0.85, 0.80, 0.76, 0.09, 0.07, 0.10, 0.06, 0.09, 0.07, 0.08});

        // Sports (9-11)
        store.insert("Basketball: fast-paced shooting dribbling slam dunks", "sports",
            new double[]{0.09, 0.07, 0.08, 0.10, 0.09, 0.07, 0.08, 0.07, 0.09, 0.91, 0.85, 0.78, 0.08, 0.07, 0.09, 0.06});
        store.insert("Football: tackles touchdowns field goals and strategy", "sports",
            new double[]{0.07, 0.09, 0.06, 0.08, 0.07, 0.10, 0.07, 0.09, 0.08, 0.87, 0.89, 0.82, 0.07, 0.09, 0.06, 0.08});
        store.insert("Tennis: racket volleys groundstrokes and Wimbledon serves", "sports",
            new double[]{0.08, 0.06, 0.09, 0.07, 0.08, 0.06, 0.09, 0.06, 0.07, 0.83, 0.80, 0.88, 0.08, 0.06, 0.09, 0.07});
        store.insert("Chess: openings endgames tactics strategic board game", "sports",
            new double[]{0.25, 0.20, 0.22, 0.18, 0.18, 0.20, 0.06, 0.08, 0.07, 0.80, 0.84, 0.78, 0.09, 0.08, 0.06, 0.07});
        store.insert("Swimming: butterfly freestyle backstroke Olympic competition", "sports",
            new double[]{0.06, 0.08, 0.07, 0.09, 0.06, 0.09, 0.10, 0.08, 0.06, 0.85, 0.82, 0.86, 0.07, 0.09, 0.06, 0.08});

        // Music (12-15)
        store.insert("Guitar Rock: distorted riffs power chords and solos", "music",
            new double[]{0.08, 0.06, 0.07, 0.09, 0.08, 0.06, 0.07, 0.08, 0.06, 0.09, 0.07, 0.08, 0.90, 0.85, 0.88, 0.82});
        store.insert("Classical Piano: sonatas symphonies Beethoven and Mozart", "music",
            new double[]{0.06, 0.08, 0.09, 0.07, 0.06, 0.08, 0.09, 0.06, 0.07, 0.08, 0.06, 0.09, 0.82, 0.91, 0.85, 0.80});
        store.insert("Jazz: swing improvisation saxophones and walking bass", "music",
            new double[]{0.09, 0.07, 0.06, 0.08, 0.09, 0.07, 0.06, 0.08, 0.09, 0.07, 0.08, 0.06, 0.86, 0.84, 0.90, 0.85});
        store.insert("Electronic: synthesizers drum machines and techno beats", "music",
            new double[]{0.15, 0.12, 0.08, 0.06, 0.07, 0.09, 0.08, 0.06, 0.07, 0.06, 0.08, 0.09, 0.88, 0.80, 0.85, 0.92});
        store.insert("Hip-Hop: rapping breakbeats sampling and turntables", "music",
            new double[]{0.07, 0.09, 0.08, 0.06, 0.08, 0.07, 0.09, 0.08, 0.06, 0.09, 0.07, 0.08, 0.84, 0.88, 0.82, 0.86});
    }
}
