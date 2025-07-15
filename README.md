# traffic-management-system
A Java-based smart traffic control system with emergency vehicle prioritization
Absolutely! Here's a full-featured `README.md` file for your **Traffic Management System** Java project, suitable for GitHub. It includes sections like project description, features, usage, structure, and more.

---

### 📄 `README.md`

```markdown
# 🚦 Traffic Management System (Java)

This is a smart Traffic Management System implemented in Java using Apache NetBeans. It simulates intelligent traffic flow by handling emergency vehicles, calculating average wait times, and optimizing routing using Dijkstra's algorithm.

---

## 📌 Project Objective

To simulate and manage a smart intersection system that:
- Gives priority to emergency vehicles
- Calculates average waiting time for vehicles from different directions
- Uses custom data structures like heaps and graphs
- Implements Dijkstra’s algorithm for optimal path calculation between junctions

---

## 🧠 Key Features

✅ **Vehicle Prioritization**  
- Emergency vehicles are given priority over others  
- Queues are scanned and emergency vehicles are removed first

✅ **Smart Signal Processing**  
- Each intersection processes vehicles direction-wise  
- Custom traffic queue implemented using Java collections

✅ **Emergency Heap (Max Heap)**  
- Intersections with more emergency vehicles are prioritized  
- Built from scratch without using built-in heap structures

✅ **Dijkstra’s Algorithm**  
- Calculates shortest travel paths between traffic points  
- Helps simulate real-time route optimization

✅ **Average Wait Time Calculation**  
- For every direction in each intersection  
- Converts milliseconds to readable minutes and seconds

---

## 🏗 Project Structure

```

traffic-management-system/
│
├── src/
│   └── trafic\_manegment\_system/
│       ├── Vehicle.java
│       ├── TrafficQueue.java
│       ├── Intersection.java
│       ├── EmergencyHeap.java
│       ├── Graph.java
│       └── TrafficManagementSystem.java (Main)
│
├── README.md
├── .gitignore
└── build.xml (optional if using Ant)

```

---

## 📷 Output Example

```

Processing intersection: HighwayJunction2
HighwayJunction2 | emergency passed West | Wait: 0m 10s

Average wait times at HighwayJunction2:
West: 0m 10s

\--- Shortest Paths from MainCross ---
To MainCross: 0 units
To HighwayJunction: 6 units
To TownSquare: 9 units

```

---

## 🚀 How to Run

1. Open this project in **Apache NetBeans**
2. Right-click on `TrafficManagementSystem.java`
3. Choose **Run File** (or press Shift+F6)

Ensure your folder structure matches the package declaration:
```

package trafic\_manegment\_system;

```

---

## ⚙ Technologies Used

- ✅ Java SE
- ✅ Apache NetBeans (IDE)
- ✅ Java Collections (List, Map, PriorityQueue)
- ✅ Custom Data Structures (Max Heap, Graph)
- ✅ Algorithm: Dijkstra’s Shortest Path

---

## 📖 Future Improvements

- Add GUI interface using JavaFX or Swing
- Support for real-time vehicle simulation
- Integration with Google Maps API for actual traffic data
- Store stats in a database (e.g., SQLite or Firebase)

---

## 🙋‍♂️ Author

**R.M.B.D Rathnayaka**  
*Software Engineering Student – Sri Lanka

