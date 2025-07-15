package trafic_manegment_system;

import java.util.*;

// Vehicle Class
class Vehicle {
    String type; // "regular", "public", "emergency"
    String direction;
    long arrivalTime;

    public Vehicle(String type, String direction, long arrivalTime) {
        this.type = type;
        this.direction = direction;
        this.arrivalTime = arrivalTime;
    }
}

// Traffic Queue
class TrafficQueue {
    List<Vehicle> queue = new ArrayList<>();

    public void enqueue(Vehicle v) {
        queue.add(v);
    }

    public Vehicle dequeue() {
        if (!queue.isEmpty()) return queue.remove(0);
        return null;
    }

    public boolean hasEmergency() {
        return queue.stream().anyMatch(v -> v.type.equals("emergency"));
    }

    public Vehicle removeEmergency() {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).type.equals("emergency")) {
                return queue.remove(i);
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public List<Vehicle> getVehicles() {
        return queue;
    }
}

// Intersection
class Intersection {
    String name;
    Map<String, TrafficQueue> queues;
    Map<String, Long> totalWaitTime;
    Map<String, Integer> vehicleCount;

    public Intersection(String name) {
        this.name = name;
        queues = new HashMap<>();
        totalWaitTime = new HashMap<>();
        vehicleCount = new HashMap<>();

        for (String dir : Arrays.asList("North", "South", "East", "West")) {
            queues.put(dir, new TrafficQueue());
            totalWaitTime.put(dir, 0L);
            vehicleCount.put(dir, 0);
        }
    }

    public boolean hasEmergency() {
        return queues.values().stream().anyMatch(TrafficQueue::hasEmergency);
    }

    public int emergencyCount() {
        int count = 0;
        for (TrafficQueue q : queues.values()) {
            for (Vehicle v : q.getVehicles()) {
                if (v.type.equals("emergency")) count++;
            }
        }
        return count;
    }

    public List<String> getGreenDirections() {
        List<String> priority = new ArrayList<>();
        for (String dir : queues.keySet()) {
            if (queues.get(dir).hasEmergency()) priority.add(dir);
        }
        if (!priority.isEmpty()) return priority;
        return Arrays.asList("North", "South", "East", "West");
    }

    public void addVehicle(Vehicle v) {
        queues.get(v.direction).enqueue(v);
    }

    public void processGreenLight(String direction) {
        TrafficQueue q = queues.get(direction);
        Vehicle v = q.hasEmergency() ? q.removeEmergency() : q.dequeue();
        if (v != null) printVehiclePassed(v);
    }

    private void printVehiclePassed(Vehicle v) {
        long wait = System.currentTimeMillis() - v.arrivalTime;
        long min = wait / 60000;
        long sec = (wait % 60000) / 1000;

        // Update stats
        totalWaitTime.put(v.direction, totalWaitTime.get(v.direction) + wait);
        vehicleCount.put(v.direction, vehicleCount.get(v.direction) + 1);

        System.out.println(name + " | " + v.type + " passed " + v.direction + " | Wait: " + min + "m " + sec + "s");
    }

    public void printAverageWaitTimes() {
        System.out.println("\nAverage wait times at " + name + ":");
        for (String dir : totalWaitTime.keySet()) {
            int count = vehicleCount.get(dir);
            long total = totalWaitTime.get(dir);
            if (count > 0) {
                long avg = total / count;
                long min = avg / 60000;
                long sec = (avg % 60000) / 1000;
                System.out.println(dir + ": " + min + "m " + sec + "s");
            } else {
                System.out.println(dir + ": No vehicles.");
            }
        }
    }
}

// Emergency Heap (Max Heap)
class EmergencyHeap {
    List<Intersection> heap = new ArrayList<>();

    public void add(Intersection i) {
        heap.add(i);
        upHeap(heap.size() - 1);
    }

    public Intersection extractMax() {
        if (heap.isEmpty()) return null;
        Collections.swap(heap, 0, heap.size() - 1);
        Intersection max = heap.remove(heap.size() - 1);
        downHeap(0);
        return max;
    }

    private void upHeap(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            if (heap.get(i).emergencyCount() > heap.get(p).emergencyCount()) {
                Collections.swap(heap, i, p);
                i = p;
            } else break;
        }
    }

    private void downHeap(int i) {
        int size = heap.size();
        while (i < size) {
            int l = 2 * i + 1, r = 2 * i + 2, largest = i;
            if (l < size && heap.get(l).emergencyCount() > heap.get(largest).emergencyCount()) largest = l;
            if (r < size && heap.get(r).emergencyCount() > heap.get(largest).emergencyCount()) largest = r;
            if (largest != i) {
                Collections.swap(heap, i, largest);
                i = largest;
            } else break;
        }
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}

// Weighted Graph and Dijkstra
class Graph {
    Map<String, Map<String, Integer>> adj = new HashMap<>();

    public void addEdge(String from, String to, int weight) {
        adj.computeIfAbsent(from, k -> new HashMap<>()).put(to, weight);
        adj.computeIfAbsent(to, k -> new HashMap<>()).put(from, weight);
    }

    public Map<String, Integer> getNeighbors(String node) {
        return adj.getOrDefault(node, new HashMap<>());
    }

    public Map<String, Integer> dijkstra(String start) {
        Map<String, Integer> dist = new HashMap<>();
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());
        pq.offer(Map.entry(start, 0));
        dist.put(start, 0);

        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> current = pq.poll();
            String node = current.getKey();
            int cost = current.getValue();

            for (Map.Entry<String, Integer> neighbor : getNeighbors(node).entrySet()) {
                int newCost = cost + neighbor.getValue();
                if (!dist.containsKey(neighbor.getKey()) || newCost < dist.get(neighbor.getKey())) {
                    dist.put(neighbor.getKey(), newCost);
                    pq.offer(Map.entry(neighbor.getKey(), newCost));
                }
            }
        }
        return dist;
    }
}

// Main Simulation
public class TrafficManagementSystem {
    public static void main(String[] args) {
        Intersection i1 = new Intersection("MainCross");
        Intersection i2 = new Intersection("HighwayJunction");
        Intersection i3 = new Intersection("HighwayJunction2");

        long now = System.currentTimeMillis();

        // Simulated arrivals (delayed arrivals for testing wait time)
        i1.addVehicle(new Vehicle("regular", "North", now - 7000));      // arrived 7s ago
        i1.addVehicle(new Vehicle("emergency", "South", now - 2000));    // arrived 2s ago
        i2.addVehicle(new Vehicle("public", "East", now - 6000));        // arrived 6s ago
        i2.addVehicle(new Vehicle("emergency", "West", now - 5000));     // arrived 5s ago
        i3.addVehicle(new Vehicle("emergency", "West", now - 10000));    // arrived 10s ago

        EmergencyHeap heap = new EmergencyHeap();
        if (i1.hasEmergency()) heap.add(i1);
        if (i2.hasEmergency()) heap.add(i2);
        if (i3.hasEmergency()) heap.add(i3);

        while (!heap.isEmpty()) {
            Intersection top = heap.extractMax();
            System.out.println("\nProcessing intersection: " + top.name);
            for (String dir : top.getGreenDirections()) {
                top.processGreenLight(dir);
            }
        }

        // Show average wait time
        i1.printAverageWaitTimes();
        i2.printAverageWaitTimes();
        i3.printAverageWaitTimes();

        // Create traffic network graph
        Graph g = new Graph();
        g.addEdge("MainCross", "HighwayJunction", 6);
        g.addEdge("HighwayJunction", "TownSquare", 3);

        // Dijkstra's algorithm to find shortest paths
        System.out.println("\n--- Shortest Paths from MainCross ---");
        Map<String, Integer> shortest = g.dijkstra("MainCross");
        for (String dest : shortest.keySet()) {
            System.out.println("To " + dest + ": " + shortest.get(dest) + " units");
        }
    }
}
