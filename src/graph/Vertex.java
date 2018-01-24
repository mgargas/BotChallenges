package graph;


import java.awt.*;
import java.util.ArrayList;

/**
 * graph.Vertex
 *
 * Class responsible for vertexes that create graph
 *
 * @author Marek Gargas
 */
public class Vertex {

    private Point position;
    private Vertex previous = null;
    private boolean visited = false;
    private ArrayList<Vertex> adjacencyList;

    public Vertex(Point position)
    {
        this.position = position;
    }

    public void setAdjacencyList(ArrayList<Vertex> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public Point getPosition() {
        return position;
    }

    public ArrayList<Vertex> getAdjacencyList() {
        return adjacencyList;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public Vertex getPrevious() {
        return previous;
    }
    public boolean getVisited() {
        return visited;
    }
}
