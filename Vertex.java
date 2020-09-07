import java.util.*;

public class Vertex extends ArrayDeque<Integer> implements Comparable<Vertex> {
    
    /**
     *
     */
    private static final long serialVersionUID = 8739351015736144598L;
    private final int id;
    private final String name;
    private boolean exists = true;
    private int dist = Integer.MAX_VALUE;
    private Vertex prev = null;
    // double avgDist = Double.MAX_VALUE;
    
    public Vertex(int id_, String name_) {
        id = id_;
        name = name_;
    }
    public int id() {
        return id;
    }
    public String name() {
        return name;
    }
    public boolean equals(Vertex v) {
        return id() == v.id();
    }
    public void connect(Vertex v) {
        add(v.id());
        v.add(this.id());
    }
    public Vertex dist(Vertex v) {
        if (v.dist() + 1 < dist()) {
            prev = v;
            dist = v.dist() + 1;
        }
        return this;
    }
    public int dist() {
        return dist;
    }
    public Vertex dist(int x) {
        dist = x;
        return this;
    }
    public Vertex reset() {
        dist = Integer.MAX_VALUE;
        prev = null;
        exists = true;
        // avgDist = Double.MAX_VALUE;
        return this;
    }

    @Override
    public String toString() {
        return 
        super.toString();
    }
    public boolean exists() {
        return exists;
    }
    public boolean exists(boolean bool) {
        return exists = bool;
    }
    public String path() {
        if (prev==null)
            return name;
        return name + " < " + prev.path();
    }
    public boolean connected() {
        return dist() != Integer.MAX_VALUE;
    }
    public int compareTo(Vertex v) {
        return Integer.compare(dist(),v.dist());
    }
    // public double avgDist() {
    //     return avgDist;
    // }
    // public double avgDist(double x) {
    //     return avgDist = x;
    // }

}
