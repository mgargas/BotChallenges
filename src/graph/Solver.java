package graph;

import bot.BotState;
import field.Field;
import move.MoveType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
/**
 * graph.Solver
 *
 * Class responsible for predicting player's best move
 * after considering current state of the game and some BFS graphs
 *
 * @author Marek Gargas
 */
public class Solver {

    private BotState currentState;

    public Solver(BotState currentState)
    {
        this.currentState = currentState;
    }

    /**
     * Returns player's next move that should be the best
     * according to my algorithm and current state of the game
     */
    public MoveType getMove()
    {
        Field field = currentState.getField();
        GraphParser graphParser = new GraphParser(field);

        HashMap<Point,Vertex> myGraph = graphParser.getGraphMap(false);
        HashMap<Point,Vertex> myGraphBugIsWall = graphParser.getGraphMap(true);
        HashMap<Point,Vertex> myOpponentGraphBugIsWall = graphParser.getGraphMap(true);

        Point myPosition = field.getMyPosition();

        BFS myBFS = new BFS(myPosition); //Construct bfs with my player's position as a start point.
        myBFS.init(myGraph);
        BFS myBFSBugIsWall = new BFS(myPosition);
        myBFSBugIsWall.init(myGraphBugIsWall);

        Point centerPosition = new Point(9,7);
        Point nextStepToCenter = myBFS.getNextPosition(centerPosition);
        if(nextStepToCenter==null) nextStepToCenter = myBFS.getNextPosition(new Point(0,7));
        //This handles the situation when I am standing on center position and I am blocked by bugs.

        Point opponentPosition = field.getOpponentPosition();
        BFS opponentBFSBugIsWall = new BFS(opponentPosition);
        opponentBFSBugIsWall.init(myOpponentGraphBugIsWall);

        ArrayList<Point> SnippetPositions = field.getSnippetPositions();
        Point mySnippetPosition = myBFSBugIsWall.getClosestObjectPosition(SnippetPositions);
        Point opponentSnippetPosition = opponentBFSBugIsWall.getClosestObjectPosition(SnippetPositions);

        if(mySnippetPosition!=null) {
            if (mySnippetPosition.equals(opponentSnippetPosition) &&
                    myBFSBugIsWall.getDistanceToStart(mySnippetPosition) > opponentBFSBugIsWall.getDistanceToStart(opponentSnippetPosition))
            {
                ArrayList<Point> availableSnippets = SnippetPositions.stream()
                        .filter(point->!point.equals(mySnippetPosition))
                        .collect(Collectors.toCollection(ArrayList::new));
                Point mySecondSnippetPosition = myBFSBugIsWall.getClosestObjectPosition(availableSnippets);
                if(mySecondSnippetPosition==null)
                {
                    return field.getTranspositionMove(myPosition,nextStepToCenter);
                }
                return field.getTranspositionMove(myPosition,myBFSBugIsWall.getNextPosition(mySecondSnippetPosition));
            }
            return field.getTranspositionMove(myPosition,myBFSBugIsWall.getNextPosition(mySnippetPosition));
        }
        return field.getTranspositionMove(myPosition,nextStepToCenter);
    }

}
