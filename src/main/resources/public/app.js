// Configuration
const API = `${window.location.origin}/api`;
const DIMS = 16;
const COLORS = {
    cs: '#00d9ff',
    math: '#b388ff',
    food: '#ffb74d',
    sports: '#69f0ae',
    profile: '#f472b6',
    user: '#ff6b9d',
    default: '#90a4ae'
};

// State
let items = [];
let pcaPoints = [];
let hitIds = new Set();
let queryPoint = null;
let hoverItem = null;
let pulsePhase = 0;
let currentAlgo = 'hnsw';
let searchResults = [];

// DOM Elements
const sc = document.getElementById('scatter');
const ctx = sc.getContext('2d');
let bounds = { minX: -1, maxX: 1, minY: -1, maxY: 1 };

// Keywords for fake embedding generation
const KEYWORDS = {
    cs: ['algorithm', 'data', 'tree', 'graph', 'array', 'linked', 'hash', 'stack', 'queue', 'sort', 'binary', 'dynamic', 'programming', 'recursion', 'pointer', 'node', 'search', 'insert', 'bfs', 'dfs', 'java', 'javascript', 'python', 'react', 'nodejs', 'express', 'fastapi', 'mongodb', 'redis', 'mysql', 'api', 'backend', 'frontend', 'distributed', 'system', 'compiler', 'packet', 'bytebuffer'],
    math: ['calculus', 'matrix', 'probability', 'theorem', 'integral', 'derivative', 'linear', 'algebra', 'equation', 'function', 'prime', 'modular', 'combinatorics', 'permutation', 'eigenvalue', 'p50', 'p99', 'latency', 'throughput', 'cgpa', '621000', '5000'],
    food: ['food', 'pizza', 'sushi', 'ramen', 'pasta', 'recipe', 'cook', 'eat', 'restaurant', 'dish', 'ingredient', 'flavor', 'spice', 'noodle', 'bread', 'croissant', 'taco', 'fish', 'rice'],
    sports: ['sport', 'basketball', 'football', 'tennis', 'chess', 'swim', 'game', 'play', 'score', 'team', 'athlete', 'competition', 'match', 'tournament', 'olympic', 'dribble', 'tackle', 'leadership', 'team', 'delegated', 'technical', 'head', 'ignitia'],
    profile: ['vidit', 'pandey', 'cognizant', 'programmer', 'analyst', 'trainee', 'intern', 'aktu', 'kanpur', 'education', 'btech', 'phone', 'email', 'contact', 'portfolio', 'github', 'linkedin', 'leetcode', 'forge', 'axiomvault', 'dpi', 've-compiler', 'compiler', 'stair', 'hostinger', 'nginx', 'ssl', 'vps', 'azure', 'aws', 'ec2', 'vpc', 'vpn', 'playwright', 'selenium', 'cucumber', 'jenkins', 'devops', 'docker', 'redis', 'mongodb', 'socket', 'websocket', 'queue', 'dlq', 'backoff', 'jitter', 'encryption', 'aes', 'rsa', 'pbkdf2', 'webcrypto', 'fastapi', 'isolation', 'forest', 'packet', 'tls', 'sni', 'bytebuffer', 'pywhatkit', 'certification', 'mern', 'freelance']
};

// -----------------------------------------------------------------------------
// Initialization
// -----------------------------------------------------------------------------
window.onload = () => {
    resizeCanvas();
    window.addEventListener('resize', resizeCanvas);
    loadItems();
    requestAnimationFrame(renderScatter);

    // Event Listeners
    document.getElementById('kSlider').addEventListener('input', e => {
        document.getElementById('kLabel').textContent = e.target.value;
    });

    document.querySelectorAll('.algo-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            document.querySelectorAll('.algo-btn').forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');
            currentAlgo = e.target.dataset.algo;
            if (document.getElementById('qInput').value.trim() !== '') {
                runSearch();
            }
        });
    });

    document.getElementById('searchBtn').addEventListener('click', runSearch);
    document.getElementById('qInput').addEventListener('keypress', e => {
        if (e.key === 'Enter') runSearch();
    });
    
    // Canvas interaction
    sc.addEventListener('mousemove', handleMouseMove);
    sc.addEventListener('mouseleave', () => {
        hoverItem = null;
        document.getElementById('tooltip').style.display = 'none';
    });
};

function resizeCanvas() {
    const parent = sc.parentElement;
    sc.width = parent.clientWidth;
    sc.height = parent.clientHeight;
}

function switchTab(tabId) {
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.add('hidden'));
    
    event.target.classList.add('active');
    document.getElementById(`tab-${tabId}`).classList.remove('hidden');
}

// -----------------------------------------------------------------------------
// Logic: Embeddings & PCA
// -----------------------------------------------------------------------------
function textToEmbedding(text) {
    const t = text.toLowerCase();
    const ws = t.split(/\s+/);
    const scores = { cs: 0, math: 0, food: 0, sports: 0, profile: 0 };
    
    for (const w of ws) {
        for (const [cat, kws] of Object.entries(KEYWORDS)) {
            for (const kw of kws) {
                if (w.length >= 3 && (w.includes(kw) || kw.startsWith(w) || kw.includes(w))) {
                    scores[cat] += 0.35;
                    break;
                }
            }
        }
    }
    
    const maxScore = Math.max(...Object.values(scores), 0.01);
    const normalize = v => Math.min(v / maxScore * 0.88, 0.94);
    const jitter = () => (Math.random() - 0.5) * 0.04;
    
    const emb = new Array(16).fill(0.08);
    const fill = (startIdx, score) => {
        if (score < 0.01) return;
        const base = normalize(score);
        emb[startIdx] = Math.max(0.05, base + jitter());
        emb[startIdx + 1] = Math.max(0.05, base + jitter());
        emb[startIdx + 2] = Math.max(0.05, base * 0.92 + jitter());
        emb[startIdx + 3] = Math.max(0.05, base * 0.87 + jitter());
    };
    
    fill(0, scores.cs);
    fill(3, scores.math);
    fill(6, scores.food);
    fill(9, scores.sports);
    fill(12, scores.profile);

    // Use string hashing for the user/profile dimensions (12-15) to ensure deterministic embeddings
    let hash = 0;
    for (let i = 0; i < t.length; i++) {
        hash = ((hash << 5) - hash) + t.charCodeAt(i);
        hash |= 0;
    }
    
    // Generate pseudo-random values between 0.1 and 0.8 based on the hash
    const h1 = (Math.abs(hash % 100) / 100) * 0.7 + 0.1;
    const h2 = (Math.abs((hash >> 8) % 100) / 100) * 0.7 + 0.1;
    const h3 = (Math.abs((hash >> 16) % 100) / 100) * 0.7 + 0.1;
    const h4 = (Math.abs((hash >> 24) % 100) / 100) * 0.7 + 0.1;

    // Default dimensions based on the hash if no profile keywords match
    if (scores.profile < 0.01) {
        emb[12] = h1; emb[13] = h2; emb[14] = h3; emb[15] = h4;
    } else {
        emb[12] = Math.max(emb[12], h1 * 0.35);
        emb[13] = Math.max(emb[13], h2 * 0.35);
        emb[14] = Math.max(emb[14], h3 * 0.35);
        emb[15] = Math.max(emb[15], h4 * 0.35);
    }
    
    return emb;
}

function computePCA(embeddings) {
    const n = embeddings.length;
    if (n < 2) return embeddings.map(() => [0, 0]);
    const d = embeddings[0].length;
    
    // Mean centering
    const mean = new Array(d).fill(0);
    for (const e of embeddings) {
        for (let i = 0; i < d; i++) mean[i] += e[i] / n;
    }
    const X = embeddings.map(e => e.map((v, i) => v - mean[i]));
    
    // Power Iteration for principal components
    function powerIteration(X, excludePC) {
        let v = new Array(d).fill(0).map(() => Math.random() - 0.5);
        if (excludePC) {
            let dot = v.reduce((sum, val, i) => sum + val * excludePC[i], 0);
            v = v.map((val, i) => val - dot * excludePC[i]);
        }
        
        let norm = Math.sqrt(v.reduce((sum, val) => sum + val * val, 0));
        v = v.map(val => val / norm);
        
        for (let iter = 0; iter < 100; iter++) {
            const Xv = X.map(xi => xi.reduce((sum, xij, j) => sum + xij * v[j], 0));
            const nv = new Array(d).fill(0);
            for (let k = 0; k < n; k++) {
                for (let j = 0; j < d; j++) nv[j] += X[k][j] * Xv[k];
            }
            
            if (excludePC) {
                let dot = nv.reduce((sum, val, i) => sum + val * excludePC[i], 0);
                for (let i = 0; i < d; i++) nv[i] -= dot * excludePC[i];
            }
            
            norm = Math.sqrt(nv.reduce((sum, val) => sum + val * val, 0));
            if (norm < 1e-10) break;
            
            const prev = [...v];
            v = nv.map(val => val / norm);
            if (v.reduce((sum, val, i) => sum + Math.pow(val - prev[i], 2), 0) < 1e-12) break;
        }
        return v;
    }
    
    const pc1 = powerIteration(X, null);
    const pc2 = powerIteration(X, pc1);
    
    // Project data
    return X.map(x => [
        x.reduce((sum, val, i) => sum + val * pc1[i], 0),
        x.reduce((sum, val, i) => sum + val * pc2[i], 0)
    ]);
}

// -----------------------------------------------------------------------------
// API Interaction
// -----------------------------------------------------------------------------
async function loadItems() {
    try {
        const res = await fetch(`${API}/items`);
        items = await res.json();
        
        if (items.length >= 2) {
            const coords = computePCA(items.map(item => item.embedding));
            pcaPoints = items.map((item, i) => ({ x: coords[i][0], y: coords[i][1], item }));
            
            // Calc bounds
            let minX = Infinity, maxX = -Infinity, minY = Infinity, maxY = -Infinity;
            for (const p of pcaPoints) {
                minX = Math.min(minX, p.x); maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y); maxY = Math.max(maxY, p.y);
            }
            const padX = (maxX - minX) * 0.2 || 0.1;
            const padY = (maxY - minY) * 0.2 || 0.1;
            bounds = { minX: minX - padX, maxX: maxX + padX, minY: minY - padY, maxY: maxY + padY };
        }
        
        document.getElementById('statsLabel').textContent = `${items.length} vectors in database`;
        renderUserList();
    } catch (err) {
        console.error("Failed to load items:", err);
        document.getElementById('statsLabel').textContent = "Offline";
    }
}

async function runSearch() {
    const text = document.getElementById('qInput').value.trim();
    if (!text) return;
    
    const k = document.getElementById('kSlider').value;
    const metric = document.getElementById('metric').value;
    const emb = textToEmbedding(text);
    
    try {
        const res = await fetch(`${API}/search?v=${emb.join(',')}&k=${k}&metric=${metric}&algo=${currentAlgo}`);
        const data = await res.json();
        
        searchResults = data.hits || [];
        hitIds = new Set(searchResults.map(r => r.item.id));
        
        // Update Latency
        const us = data.latencyUs || 0;
        document.getElementById('latBig').textContent = us < 1000 ? `${us} μs` : `${(us/1000).toFixed(2)} ms`;
        document.getElementById('latSub').textContent = `${currentAlgo.toUpperCase()} · ${metric} · k=${k}`;
        
        // Compute Query Point for visualization (weighted average of top 3 hits)
        if (searchResults.length > 0) {
            let sx = 0, sy = 0, sw = 0;
            for (let i = 0; i < Math.min(3, searchResults.length); i++) {
                const pt = pcaPoints.find(p => p.item.id === searchResults[i].item.id);
                if (pt) {
                    const w = 1 / (i + 1);
                    sx += pt.x * w;
                    sy += pt.y * w;
                    sw += w;
                }
            }
            if (sw > 0) {
                queryPoint = { x: (sx/sw) + (Math.random()-0.5)*0.01, y: (sy/sw) + (Math.random()-0.5)*0.01 };
            }
        }
        
        renderSearchResults();
        drawQueryVector(emb);
        
    } catch (err) {
        console.error("Search failed:", err);
    }
}

async function insertUserData() {
    const title = document.getElementById('docTitle').value.trim();
    const text = document.getElementById('docText').value.trim();
    const status = document.getElementById('insertStatus');
    
    if (!title || !text) {
        status.innerHTML = '<span class="text-red-400">Please provide title and text.</span>';
        return;
    }
    
    status.innerHTML = '<span class="text-muted">Embedding text...</span>';
    const emb = textToEmbedding(title + " " + text);
    
    try {
        const res = await fetch(`${API}/insert`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                metadata: title + " - " + text.substring(0, 50) + "...",
                category: 'user',
                embedding: emb
            })
        });
        
        if (res.ok) {
            status.innerHTML = '<span class="text-green-400">✓ Ingested into indices</span>';
            document.getElementById('docTitle').value = '';
            document.getElementById('docText').value = '';
            await loadItems();
        }
    } catch (err) {
        status.innerHTML = '<span class="text-red-400">✗ Failed to connect to engine</span>';
    }
}

async function deleteItem(id) {
    try {
        await fetch(`${API}/delete/${id}`, { method: 'DELETE' });
        searchResults = searchResults.filter(r => r.item.id !== id);
        hitIds.delete(id);
        renderSearchResults();
        await loadItems();
    } catch (err) {}
}

// -----------------------------------------------------------------------------
// UI Rendering
// -----------------------------------------------------------------------------
function renderSearchResults() {
    const container = document.getElementById('resultsList');
    if (!searchResults.length) {
        container.innerHTML = '<div class="text-xs text-muted italic text-center py-4">No results</div>';
        return;
    }
    
    container.innerHTML = searchResults.map((result, i) => {
        const item = result.item;
        const col = COLORS[item.category] || COLORS.default;
        return `
            <div class="bg-bg border border-border rounded p-3 hover:border-border/80 transition-colors animate-fade-in" 
                 onmouseenter="hoverItem={id:${item.id}}" onmouseleave="hoverItem=null"
                 style="animation-delay: ${i * 0.05}s">
                <div class="text-[9px] tracking-widest text-muted mb-1 uppercase">#${i+1} Match</div>
                <div class="text-xs text-text mb-2 line-clamp-2">${item.metadata}</div>
                <div class="flex justify-between items-center">
                    <span class="text-[9px] px-2 py-0.5 rounded-full border tracking-widest uppercase" style="color:${col}; border-color:${col}55; background:${col}11">${item.category}</span>
                    <span class="text-[10px] text-muted font-mono">dist: ${result.distance.toFixed(4)}</span>
                </div>
            </div>
        `;
    }).join('');
}

function renderUserList() {
    const userItems = items.filter(i => i.category === 'user');
    document.getElementById('userDocCount').textContent = userItems.length;
    
    const container = document.getElementById('userList');
    if (!userItems.length) {
        container.innerHTML = '<div class="text-xs text-muted italic text-center py-4">No custom data yet</div>';
        return;
    }
    
    container.innerHTML = userItems.map(item => `
        <div class="bg-bg border border-border rounded p-3 flex justify-between items-center group">
            <div class="text-xs text-text truncate max-w-[250px]">${item.metadata}</div>
            <button onclick="deleteItem(${item.id})" class="text-red-400 opacity-0 group-hover:opacity-100 transition-opacity p-1 text-[10px] font-bold">✕</button>
        </div>
    `).reverse().join('');
}

function drawQueryVector(emb) {
    const cvs = document.getElementById('vecCvs');
    const vw = cvs.parentElement.clientWidth;
    cvs.width = vw;
    cvs.height = 60;
    
    const vctx = cvs.getContext('2d');
    vctx.clearRect(0, 0, vw, 60);
    
    const barWidth = (vw - 4) / DIMS;
    for (let i = 0; i < DIMS; i++) {
        const h = Math.min(emb[i] * 50, 50); // scale up for viz
        const x = 2 + i * barWidth;
        
        let col = '#6c63ff'; // default accent
        if (i < 3) col = COLORS.cs;
        else if (i < 6) col = COLORS.math;
        else if (i < 9) col = COLORS.food;
        else if (i < 12) col = COLORS.sports;
        else col = COLORS.user;
        
        vctx.fillStyle = col + 'aa';
        vctx.fillRect(x + 1, 55 - h, barWidth - 2, h);
    }
}

// -----------------------------------------------------------------------------
// Canvas Scatter Plot
// -----------------------------------------------------------------------------
function worldToCanvas(wx, wy) {
    const pad = 50;
    const w = sc.width;
    const h = sc.height;
    const rangeX = bounds.maxX - bounds.minX || 1;
    const rangeY = bounds.maxY - bounds.minY || 1;
    
    return [
        pad + ((wx - bounds.minX) / rangeX) * (w - 2 * pad),
        h - pad - ((wy - bounds.minY) / rangeY) * (h - 2 * pad)
    ];
}

function handleMouseMove(e) {
    const rect = sc.getBoundingClientRect();
    const mx = e.clientX - rect.left;
    const my = e.clientY - rect.top;
    
    hoverItem = null;
    let minDist = 15;
    
    for (const pt of pcaPoints) {
        const [cx, cy] = worldToCanvas(pt.x, pt.y);
        const dist = Math.hypot(mx - cx, my - cy);
        if (dist < minDist) {
            minDist = dist;
            hoverItem = pt.item;
        }
    }
    
    const tooltip = document.getElementById('tooltip');
    if (hoverItem) {
        const col = COLORS[hoverItem.category] || COLORS.default;
        tooltip.style.display = 'block';
        tooltip.style.left = (e.clientX + 15) + 'px';
        tooltip.style.top = (e.clientY + 15) + 'px';
        tooltip.innerHTML = `<span style="color:${col}" class="uppercase tracking-widest font-bold block mb-1">${hoverItem.category}</span>${hoverItem.metadata}`;
        sc.style.cursor = 'pointer';
    } else {
        tooltip.style.display = 'none';
        sc.style.cursor = 'default';
    }
}

function renderScatter() {
    ctx.clearRect(0, 0, sc.width, sc.height);
    
    // Grid
    ctx.strokeStyle = '#1a1b2e';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 10; i++) {
        const x = 50 + (i / 10) * (sc.width - 100);
        const y = 50 + (i / 10) * (sc.height - 100);
        ctx.beginPath(); ctx.moveTo(x, 50); ctx.lineTo(x, sc.height - 50); ctx.stroke();
        ctx.beginPath(); ctx.moveTo(50, y); ctx.lineTo(sc.width - 50, y); ctx.stroke();
    }
    
    // Labels
    ctx.fillStyle = '#6a6b8a';
    ctx.font = '10px "Fira Code", monospace';
    ctx.fillText('PC₁ →', sc.width / 2 - 20, sc.height - 20);
    ctx.save();
    ctx.translate(20, sc.height / 2 + 20);
    ctx.rotate(-Math.PI / 2);
    ctx.fillText('PC₂ →', 0, 0);
    ctx.restore();

    // Hit Connections
    if (queryPoint && hitIds.size > 0) {
        const [qx, qy] = worldToCanvas(queryPoint.x, queryPoint.y);
        for (const pt of pcaPoints) {
            if (!hitIds.has(pt.item.id)) continue;
            const [px, py] = worldToCanvas(pt.x, pt.y);
            ctx.strokeStyle = 'rgba(108, 99, 255, 0.2)';
            ctx.lineWidth = 1;
            ctx.setLineDash([4, 4]);
            ctx.beginPath(); ctx.moveTo(qx, qy); ctx.lineTo(px, py); ctx.stroke();
            ctx.setLineDash([]);
        }
    }

    // Points
    for (const pt of pcaPoints) {
        const [cx, cy] = worldToCanvas(pt.x, pt.y);
        const col = COLORS[pt.item.category] || COLORS.default;
        const isHit = hitIds.has(pt.item.id);
        const isHover = hoverItem && hoverItem.id === pt.item.id;
        const radius = isHit ? 7 : 5;
        
        if (isHit) {
            const pr = radius + 5 + Math.sin(pulsePhase) * 3;
            ctx.beginPath(); ctx.arc(cx, cy, pr, 0, Math.PI * 2);
            ctx.strokeStyle = col + '66'; ctx.lineWidth = 1; ctx.stroke();
        }
        
        const glow = ctx.createRadialGradient(cx, cy, 0, cx, cy, radius * 3);
        glow.addColorStop(0, col + (isHit ? 'cc' : '66'));
        glow.addColorStop(1, 'transparent');
        ctx.beginPath(); ctx.arc(cx, cy, radius * 3, 0, Math.PI * 2);
        ctx.fillStyle = glow; ctx.fill();
        
        ctx.beginPath(); ctx.arc(cx, cy, radius, 0, Math.PI * 2);
        ctx.fillStyle = col; ctx.fill();
        
        if (isHover) {
            ctx.beginPath(); ctx.arc(cx, cy, radius + 3, 0, Math.PI * 2);
            ctx.strokeStyle = '#ffffff'; ctx.lineWidth = 1.5; ctx.stroke();
        }
    }

    // Query Point
    if (queryPoint) {
        const [qx, qy] = worldToCanvas(queryPoint.x, queryPoint.y);
        ctx.save();
        ctx.translate(qx, qy);
        ctx.shadowColor = '#ffffff';
        ctx.shadowBlur = 15;
        ctx.fillStyle = '#ffffff';
        
        // Draw star
        ctx.beginPath();
        for (let i = 0; i < 10; i++) {
            const angle = (i * Math.PI) / 5 - Math.PI / 2;
            const r = i % 2 === 0 ? 10 : 4;
            if (i === 0) ctx.moveTo(Math.cos(angle) * r, Math.sin(angle) * r);
            else ctx.lineTo(Math.cos(angle) * r, Math.sin(angle) * r);
        }
        ctx.closePath();
        ctx.fill();
        ctx.shadowBlur = 0;
        ctx.restore();
    }
    
    pulsePhase += 0.05;
    requestAnimationFrame(renderScatter);
}

// -----------------------------------------------------------------------------
// Benchmark Chart
// -----------------------------------------------------------------------------
let benchChart = null;

async function runScaleBenchmark() {
    const btn = document.getElementById('benchBtn');
    btn.textContent = 'RUNNING BENCHMARK...';
    btn.disabled = true;
    
    try {
        const res = await fetch(`${API}/benchmark-scale`);
        const data = await res.json();
        
        const labels = data.map(d => `N=${d.n}`);
        const bfData = data.map(d => d.bruteForceUs);
        const hnswData = data.map(d => d.hnswUs);
        
        const ctxChart = document.getElementById('benchChart').getContext('2d');
        
        if (benchChart) {
            benchChart.destroy();
        }
        
        Chart.defaults.color = '#6a6b8a';
        Chart.defaults.font.family = "'Fira Code', monospace";
        
        benchChart = new Chart(ctxChart, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Brute Force O(N)',
                        data: bfData,
                        borderColor: '#ff6b9d',
                        backgroundColor: 'rgba(255, 107, 157, 0.1)',
                        borderWidth: 2,
                        tension: 0.3,
                        fill: true
                    },
                    {
                        label: 'HNSW O(log N)',
                        data: hnswData,
                        borderColor: '#6c63ff',
                        backgroundColor: 'rgba(108, 99, 255, 0.2)',
                        borderWidth: 2,
                        tension: 0.3,
                        fill: true
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: { display: true, text: 'Time (μs)' },
                        grid: { color: '#1a1b2e' }
                    },
                    x: {
                        grid: { color: '#1a1b2e' }
                    }
                },
                plugins: {
                    legend: { position: 'top', labels: { boxWidth: 12 } }
                }
            }
        });
        
    } catch (err) {
        console.error("Benchmark failed:", err);
    } finally {
        btn.textContent = 'RUN SCALING TEST';
        btn.disabled = false;
    }
}
