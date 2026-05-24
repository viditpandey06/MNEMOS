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

        // Vidit Pandey profile and resume knowledge (12-15)
        store.insert("Vidit Pandey: Software Engineer, Java and full-stack developer, portfolio project author", "profile",
            new double[]{0.72, 0.68, 0.66, 0.18, 0.08, 0.08, 0.08, 0.08, 0.08, 0.16, 0.08, 0.08, 0.88, 0.88, 0.81, 0.77});
        store.insert("Contact: Vidit Pandey, phone +91 7355258137, email viditpandey06@gmail.com", "profile",
            new double[]{0.22, 0.20, 0.18, 0.10, 0.09, 0.08, 0.06, 0.07, 0.06, 0.08, 0.07, 0.08, 0.92, 0.94, 0.88, 0.90});
        store.insert("Education: B.Tech Computer Science from AKTU Kanpur, 2021 to 2025, CGPA 7.65", "profile",
            new double[]{0.70, 0.66, 0.62, 0.48, 0.42, 0.40, 0.06, 0.07, 0.06, 0.08, 0.07, 0.08, 0.90, 0.86, 0.92, 0.88});
        store.insert("Cognizant: Programmer Analyst Trainee since Jan 2025, promoted from intern within 6 months", "profile",
            new double[]{0.66, 0.62, 0.58, 0.12, 0.10, 0.10, 0.06, 0.07, 0.06, 0.16, 0.14, 0.12, 0.93, 0.89, 0.91, 0.90});
        store.insert("Automation frameworks: Playwright, Selenium Java, Cucumber, task delegation and QA architecture", "profile",
            new double[]{0.76, 0.70, 0.68, 0.14, 0.12, 0.10, 0.06, 0.07, 0.06, 0.18, 0.16, 0.14, 0.90, 0.92, 0.88, 0.91});
        store.insert("HLD and AI solutions: contributed to high-level design discussions and proposed AI-driven delivery approaches", "profile",
            new double[]{0.78, 0.72, 0.74, 0.20, 0.16, 0.18, 0.06, 0.07, 0.06, 0.12, 0.10, 0.11, 0.92, 0.90, 0.89, 0.93});
        store.insert("Freelance Web Developer at Stair's in 2024: React UI, Hostinger VPS, Nginx reverse proxy and SSL", "profile",
            new double[]{0.72, 0.68, 0.60, 0.10, 0.09, 0.08, 0.08, 0.07, 0.06, 0.12, 0.11, 0.10, 0.91, 0.90, 0.93, 0.88});
        store.insert("Freelance results: improved site reliability by 30 percent, cut deployment time by 40 percent, reduced load times by 50 percent", "profile",
            new double[]{0.58, 0.54, 0.50, 0.36, 0.32, 0.30, 0.08, 0.07, 0.06, 0.10, 0.09, 0.08, 0.89, 0.91, 0.87, 0.92});
        store.insert("Skills: Java, JavaScript, Python, C/C++, SQL, Node.js, Express.js, FastAPI, React, Redux", "profile",
            new double[]{0.86, 0.82, 0.78, 0.16, 0.14, 0.12, 0.06, 0.07, 0.06, 0.10, 0.09, 0.08, 0.92, 0.88, 0.91, 0.90});
        store.insert("Databases and backend: REST APIs, WebSockets, Socket.io, MongoDB, Redis, MySQL, distributed systems", "profile",
            new double[]{0.84, 0.80, 0.74, 0.14, 0.12, 0.12, 0.06, 0.07, 0.06, 0.14, 0.12, 0.10, 0.91, 0.89, 0.92, 0.90});
        store.insert("Cloud and infrastructure: AWS EC2, VPC, VPN, Microsoft Azure, Docker, Jenkins, Azure DevOps, CI/CD", "profile",
            new double[]{0.76, 0.72, 0.68, 0.12, 0.10, 0.10, 0.06, 0.07, 0.06, 0.18, 0.16, 0.14, 0.93, 0.90, 0.88, 0.92});
        store.insert("Security and systems: TCP/IP, TLS/SNI, system design, AES-256, RSA-OAEP, PBKDF2, concurrency", "profile",
            new double[]{0.82, 0.78, 0.76, 0.18, 0.14, 0.16, 0.06, 0.07, 0.06, 0.12, 0.11, 0.10, 0.92, 0.91, 0.89, 0.93});
        store.insert("Data structures and algorithms: 350+ LeetCode problems solved, strong DSA and problem solving", "profile",
            new double[]{0.90, 0.86, 0.88, 0.24, 0.20, 0.18, 0.06, 0.07, 0.06, 0.10, 0.09, 0.08, 0.93, 0.89, 0.91, 0.90});
        store.insert("FORGE project: distributed task queue engine with producer broker consumer architecture", "profile",
            new double[]{0.88, 0.84, 0.82, 0.18, 0.14, 0.12, 0.06, 0.07, 0.06, 0.16, 0.14, 0.12, 0.94, 0.91, 0.90, 0.92});
        store.insert("FORGE architecture: Redis Sorted Sets, Pub/Sub, MongoDB Atlas, Socket.io, React, Vite", "profile",
            new double[]{0.86, 0.82, 0.78, 0.16, 0.12, 0.12, 0.06, 0.07, 0.06, 0.14, 0.12, 0.10, 0.93, 0.90, 0.92, 0.91});
        store.insert("FORGE reliability: 5000+ concurrent jobs, priority routing, delayed execution, exponential backoff retries with jitter", "profile",
            new double[]{0.88, 0.84, 0.86, 0.20, 0.16, 0.14, 0.06, 0.07, 0.06, 0.18, 0.16, 0.14, 0.94, 0.92, 0.90, 0.91});
        store.insert("FORGE observability: real-time dashboard streaming P50 P99 latency, throughput and queue depth over WebSockets", "profile",
            new double[]{0.82, 0.78, 0.76, 0.22, 0.18, 0.16, 0.06, 0.07, 0.06, 0.16, 0.14, 0.12, 0.92, 0.94, 0.90, 0.91});
        store.insert("AXIOMVAULT project: zero-knowledge encrypted messaging platform with privacy-first architecture", "profile",
            new double[]{0.86, 0.80, 0.82, 0.16, 0.14, 0.12, 0.06, 0.07, 0.06, 0.12, 0.10, 0.10, 0.94, 0.90, 0.93, 0.92});
        store.insert("AXIOMVAULT encryption: AES-256-GCM, RSA-OAEP-2048, WebCrypto API, PBKDF2 600K iterations", "profile",
            new double[]{0.84, 0.78, 0.80, 0.18, 0.14, 0.16, 0.06, 0.07, 0.06, 0.10, 0.09, 0.08, 0.93, 0.92, 0.94, 0.91});
        store.insert("AXIOMVAULT AI moderation: Python FastAPI, Isolation Forest, metadata-only anomaly detection, Redis rate limiting", "profile",
            new double[]{0.82, 0.76, 0.78, 0.26, 0.22, 0.24, 0.06, 0.07, 0.06, 0.10, 0.09, 0.08, 0.92, 0.90, 0.94, 0.93});
        store.insert("Enterprise DPI Engine: Java 17 deep packet inspection with ByteBuffer and memory-mapped files", "profile",
            new double[]{0.90, 0.84, 0.82, 0.18, 0.14, 0.16, 0.06, 0.07, 0.06, 0.12, 0.10, 0.10, 0.94, 0.92, 0.91, 0.93});
        store.insert("DPI performance: 621000+ packets per second, zero-copy IO, Flyweight pattern, near-zero GC pressure", "profile",
            new double[]{0.88, 0.82, 0.84, 0.22, 0.18, 0.16, 0.06, 0.07, 0.06, 0.12, 0.10, 0.10, 0.93, 0.94, 0.91, 0.92});
        store.insert("DPI networking: consistent hashing on five tuple, lock-free concurrency, TCP stream reassembly, TLS SNI extraction", "profile",
            new double[]{0.90, 0.86, 0.84, 0.18, 0.14, 0.16, 0.06, 0.07, 0.06, 0.14, 0.12, 0.10, 0.94, 0.91, 0.93, 0.92});
        store.insert("VE-COMPILER project: multi-language code compilation package for Java, C++, Python, Bash and PowerShell", "profile",
            new double[]{0.86, 0.82, 0.80, 0.14, 0.12, 0.10, 0.06, 0.07, 0.06, 0.10, 0.09, 0.08, 0.92, 0.94, 0.90, 0.91});
        store.insert("VE-COMPILER adoption: published JavaScript package with unified API and 300+ active users", "profile",
            new double[]{0.78, 0.74, 0.70, 0.20, 0.16, 0.14, 0.06, 0.07, 0.06, 0.10, 0.09, 0.08, 0.91, 0.93, 0.89, 0.92});
        store.insert("Certifications: MERN full stack development, Selenium with Java and Cucumber, AWS Cloud Practitioner in progress", "profile",
            new double[]{0.76, 0.72, 0.68, 0.14, 0.12, 0.10, 0.06, 0.07, 0.06, 0.14, 0.12, 0.10, 0.92, 0.90, 0.91, 0.93});
        store.insert("Open source: PyWhatKit contributor, merged PR 323 for cross-browser message dispatch using OS-level window focus", "profile",
            new double[]{0.82, 0.76, 0.74, 0.14, 0.12, 0.10, 0.06, 0.07, 0.06, 0.12, 0.10, 0.10, 0.91, 0.94, 0.90, 0.92});
        store.insert("Leadership: Technical Head for Ignitia college techno-cultural fest for two consecutive years, 2023 and 2024", "profile",
            new double[]{0.60, 0.56, 0.52, 0.12, 0.10, 0.10, 0.08, 0.07, 0.06, 0.26, 0.24, 0.22, 0.90, 0.92, 0.88, 0.94});
        store.insert("Personal positioning: Vidit is strongest in backend systems, Java, distributed systems, security, automation and full-stack delivery", "profile",
            new double[]{0.88, 0.84, 0.82, 0.18, 0.14, 0.14, 0.06, 0.07, 0.06, 0.16, 0.14, 0.12, 0.94, 0.92, 0.93, 0.91});
    }
}
