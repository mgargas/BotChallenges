package graph;

import field.Field;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * graph.GraphParser
 *
 * Class responsible for parsing Field to the Graph
 * and setting each vertex's adjacency list
 *
 * @author Marek Gargas
 */
public class GraphParser {

    private Field field;

    public GraphParser(Field field)
    {
        this.field = field;
    }

    /**
     * Creates graph map from field
     * skipping cells that contain  wall.
     * @param bugIsWall treats each bug as wall when we pass true
     */
    public HashMap<Point,Vertex> getGraphMap(boolean bugIsWall)
    {
        HashMap<Point,Vertex> graph = new HashMap<>();
        for(int y=0;y<field.getHeight();y++)
        {
            for(int x=0;x<field.getWidth();x++)
            {
                Point point = new Point(x,y);
                if(field.isPointSafe(point,bugIsWall))
                {
                    Vertex vertex = new Vertex(point);
                    vertex.setAdjacencyList(makeAdjacencyList(point,bugIsWall));
                    graph.put(point, vertex);
                }
            }
        }
        return graph;
    }
    /**
     * Creates adjacencyList considering vertex's position on field
     * and skipping cells that contain wall
     * @param bugIsWall treats each bug as wall when we pass true
     */
    private ArrayList<Vertex> makeAdjacencyList(Point point, boolean bugIsWall)
    {
        ArrayList<Vertex> adjacencyList = new ArrayList<>();
        int x = point.x;
        int y = point.y;
        if(field.isGate(point))
        {
            Point gateDestination = field.getGateDestination(point);
            Vertex gateDestinationVertex = new Vertex(gateDestination);
            adjacencyList.add(gateDestinationVertex);
        }
        Point upPoint = new Point(x,y+1);
        if (field.isPointSafe(upPoint,bugIsWall)) {
            Vertex upVertex = new Vertex(upPoint);
            adjacencyList.add(upVertex);
        }
        Point downPoint = new Point(x,y-1);
        if (field.isPointSafe(downPoint,bugIsWall)) {
            Vertex downVertex = new Vertex(downPoint);
            adjacencyList.add(downVertex);
        }
        Point rightPoint = new Point(x+1,y);
        if (field.isPointSafe(rightPoint,bugIsWall)) {
            Vertex rightVertex = new Vertex(rightPoint);
            adjacencyList.add(rightVertex);
        }
        Point leftPoint = new Point(x-1,y);
        if (field.isPointSafe(leftPoint,bugIsWall)) {
            Vertex leftVertex = new Vertex(leftPoint);
            adjacencyList.add(leftVertex);
        }
        return adjacencyList;
    }

}
