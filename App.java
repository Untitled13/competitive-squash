import java.util.*;
import java.io.*;
import java.net.URL;

public class App {
    public static void main(String[] args) throws Exception {
        Graph graph = new Graph();
        graph.read(new URL("http://cs.oberlin.edu/~gr151/imdb/imdb.no-tv-v.txt"));
        graph.recenter("Kevin Bacon (I)");
        // System.out.println(graph.find("Carrie Fisher"));
        graph.table();
        System.out.println(graph.avgDist());
        // System.out.println(graph.topCenter().name());
        // graph.recenter("Century of Cinema, A (1994)");
        // graph.table();
        // System.out.println(graph.avgDist());
        // graph.recenter("Traffic (2000)");
        // graph.table();
        // System.out.println(graph.avgDist());
        // System.out.println(graph.find("Dave"));
        // System.out.println(graph);
    }
}
