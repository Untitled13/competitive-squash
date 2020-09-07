import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.*;
import javax.lang.model.util.ElementScanner14;


// import sun.security.provider.certpath.Vertex;

public class Graph {
    private HashMap<String, Integer> ids = new HashMap<>();
    private HashMap<Integer, Vertex> vertices = new HashMap<>();
    private PriorityQueue<Pair<Integer,Integer>> queue = new PriorityQueue<>(new Comparator<>() {
        public int compare(Pair<Integer,Integer> a, Pair<Integer,Integer> b) {
            return a.left().compareTo(b.left());
        }
    }); 

    String currentCenter = "";

    private int maxID = -1;

    public int id(String name) {
        return ids.get(name);
    }

    public Vertex vertex(int id) {
        return vertices.get(id);
    }

    public Vertex vertex(String name) {
        return vertex(id(name));
    }

    public Vertex addVertex(String name) {
        if (ids.containsKey(name))
            return vertex(name);
        maxID += 1;
        ids.put(name, maxID);
        Vertex temp;
        vertices.put(maxID, temp = new Vertex(maxID, name));
        return temp;
    }

    public void addEdge(String a, String b) {
        Vertex A = addVertex(a);
        Vertex B = addVertex(b);
        A.connect(B);
        vertices.put(A.id(), A);
        vertices.put(B.id(), B);
    }

    public void read(URL url) throws MalformedURLException, IOException {
        Scanner scan = new Scanner(url.openStream());
        String[] arr = new String[3];
        while (scan.hasNextLine()) {
             arr = scan.nextLine().split("\\|");
             addEdge(arr[0],arr[1]);
        }
    }
    public String toString() {
        return vertices.toString();
    }
    public Graph reset() {
        vertices.forEach((i,v) -> vertices.put(i, v.reset()) );
        return this;
    }
    public String find(String name) {
       return vertex(name).path();
    }
    public void recenter(String name) {
        currentCenter = name;
        reset();
        vertices.put(id(name), vertex(name).dist(0));
        // Queue.add(vertex(name));
        Optional<Vertex> min = Optional.of(vertex(name));
        while(min.isPresent()) {
            Vertex v = min.get();
            v.exists(false);
            vertices.put(v.id(), v);
            v.stream().map(i -> vertex(i)).filter(a -> a.exists()).forEach(a -> { 
                if (a.dist() != a.dist(v).dist()) {
                    vertices.put(a.id(),a);
                    queue.add(new Pair<Integer,Integer>(a.dist(),a.id()));
                }
            });
            min = getMin();
        }
        // // Vertex b;
        // // for (int i : v) {
        // //     if ((b = vertex(i)).exists() && b.dist() != b.dist(v).dist()) {
        // //         vertices.put(b.id(),b);
        // //         queue.add(new Pair<Integer,Integer>(b.dist(),b.id()));
        // //     }
        // // }
        // Optional<Vertex> a = getMin();
        // if (a.isPresent())
        //     recenter(a.get());
    }
    // public void updateNeighbors(Vertex v) {
    //     v.stream().map(i -> vertex(i)).filter(a -> a.exists()).forEach(a -> vertices.put(a.id(),a.dist(v)));
    // }
    public void recenter(Vertex v) {
        v.exists(false);
        vertices.put(v.id(), v);
        v.stream().map(i -> vertex(i)).filter(a -> a.exists()).forEach(a -> { 
            if (a.dist() != a.dist(v).dist()) {
                vertices.put(a.id(),a);
                queue.add(new Pair<Integer,Integer>(a.dist(),a.id()));
            }
        });
        // Vertex b;
        // for (int i : v) {
        //     if ((b = vertex(i)).exists() && b.dist() != b.dist(v).dist()) {
        //         vertices.put(b.id(),b);
        //         queue.add(new Pair<Integer,Integer>(b.dist(),b.id()));
        //     }
        // }
        Optional<Vertex> a = getMin();
        if (a.isPresent())
            recenter(a.get());
    }
    private Optional<Vertex> getMin() {
        if (queue.isEmpty())
            return Optional.empty();
        else if (vertex(queue.peek().right()).exists())
            return Optional.of(vertex(queue.poll().right()));
        queue.poll();
        return getMin();
    }
    public long unreachable() {
        return vertices.values().stream().filter(v -> !v.connected()).count();
    }
    public double avgDist() {
        double sum = (double) 
                    vertices.values()
                    .stream()
                    .filter(v -> v.connected())
                    .map(v -> v.dist()).reduce(0, (a,b) -> a + b);
        return sum / ids.size();
    }
    public Vertex topCenter() {
        Optional<Pair<Integer,Double>> min = vertices.values()
            .stream()
            .map(v -> {
                recenter(v.name());
                return new Pair<Integer,Double>(v.id(), avgDist());
            })
            .min(new Comparator<Pair<Integer,Double>>() {
                public int compare(Pair<Integer,Double> a, Pair<Integer,Double> b) {
                    return Double.compare(a.right(), b.right());
                }
            });
        if (min.isPresent())
            return vertex(min.get().left());
        return null;
    }
    public void table() {
        if (currentCenter.equals("")) {
            System.out.println("No current center, table failed");
            return;
        }
        System.out.println("Table of Distances for " + currentCenter);
        int[] table = new int[vertices.values().stream().filter(v -> v.connected()).map(v -> v.dist()).reduce(0, (a,b) -> Math.max(a,b)) + 1];
        vertices.values().stream().filter(v -> v.connected()).map(v -> v.dist()).forEach(i -> table[i]++);
        for (int i = 0; i< table.length; i++)
            System.out.println("Number\t" + i + ":\t" + table[i]);
        System.out.println("Unreachable:\t" + unreachable());
    }
}
