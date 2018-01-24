package graph;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
/**
 * graph.BFS
 *
 * Class responsible for creating BFS graph with start point
 * that is passed in constructor. It also contains all methods that
 * help traverse this graph and find the best path.
 *
 * @author Marek Gargas
 */
public class BFS {

    private Point start;
    private HashMap<Point,Vertex> BFSGraph;

    public BFS(Point start)
    {
        this.start = start;
    }

    /**
     * Creates BFS graph out of the graph passed as an argument.
     * @param graph HashMap parsed by GraphParser
     */
    public void init(HashMap<Point,Vertex> graph) {
        HashMap<Point, Vertex> BFSGraph = new HashMap<>(graph);
        Queue<Point> queue = new LinkedList<>();

        Vertex actual = BFSGraph.get(start);
        actual.setVisited(true);
        BFSGraph.put(start, actual);
        queue.add(start);
        while (!queue.isEmpty()) {
            if(!BFSGraph.containsKey(queue.element()))
            {
                queue.remove();
            }
            else {
                actual = BFSGraph.get(queue.element());
                queue.remove();
                if (actual.getAdjacencyList().size() > 0) {
                    for (Vertex neighbour : actual.getAdjacencyList()) {
                        if(BFSGraph.containsKey(neighbour.getPosition())) {
                            neighbour = BFSGraph.get(neighbour.getPosition());
                            if (!neighbour.getVisited()) {
                                neighbour.setVisited(true);
                                neighbour.setPrevious(actual);
                                queue.add(neighbour.getPosition());
                                BFSGraph.put(neighbour.getPosition(), neighbour);

                            }
                        }
                    }
                }
            }
        }
        this.BFSGraph = BFSGraph;
    }
    /**
     * Returns path from BFS graph's start to  the point passed as argument
     * or null if this path does not exist.
     * @param point Point that is our destination
     */
    public ArrayList<Point> getPathToStart(Point point)
    {
        ArrayList<Point> path = new ArrayList<>();
        while(!point.equals(start))
        {
            path.add(point);
            if(!BFSGraph.containsKey(point))
            {
                return null;
            }
            Vertex vertex = BFSGraph.get(point);
            if(vertex.getPrevious()==null) {
                return null;
            }
            point=vertex.getPrevious().getPosition();
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Returns distance from BFS graph's start to the point passed as argument
     * or -1 if it is impossible to get there.
     * @param point Point that is our destination
     */
    public int getDistanceToStart(Point point)
    {
        if(getPathToStart(point)==null) return -1;
        return getPathToStart(point).size();
    }

    /**
     * Returns position of the object that is nearest to
     * the BFS graph's start or null
     * if it doesn't find any in this graph.
     * @param objectPositions For example, list of snippets
     */
    public Point getClosestObjectPosition(ArrayList<Point> objectPositions)
    {
        if(objectPositions==null || objectPositions.size()==0)
        {
            return null;
        }
        objectPositions = objectPositions.stream().
                filter(position->getDistanceToStart(position)!=-1).
                collect(Collectors.toCollection(ArrayList::new)); //only objects that can be reached in a path
        if(objectPositions.size()==0)
        {
            return null;
        }
        Point closestObjectPosition = objectPositions.get(0);
        for(Point objectPosition : objectPositions)
        {
            if(getDistanceToStart(objectPosition) < getDistanceToStart(closestObjectPosition))
            {
                closestObjectPosition = objectPosition;
            }
        }
        return closestObjectPosition;
    }
    /**
     * Returns first position in path or
     * null if endPosition is not accessible
     * or player is on this position
     * @param endPosition Point that is our destination
     */
    public Point getNextPosition(Point endPosition)
    {
        if(getPathToStart(endPosition)==null) return null; // you can't reach this point
        if(getPathToStart(endPosition).size()==0) return null; //you are on this position
        return getPathToStart(endPosition).get(0);
    }
}
