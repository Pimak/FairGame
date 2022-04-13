package com.pimak.huuang;

/* Simple graph drawing class
Bert Huang
COMS 3137 Data Structures and Algorithms, Spring 2009

This class is really elementary, but lets you draw
reasonably nice graphs/trees/diagrams. Feel free to
improve upon it!
 */

import com.pimak.fairGame.CoinAction;
import com.pimak.players.State;

import java.util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class GraphDraw extends JFrame {
    int width;
    int height;

    ArrayList<Node> nodes;
    ArrayList<edge> edges;

    public GraphDraw() { //Constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        width = 30;
        height = 30;
    }

    public GraphDraw(String name) { //Construct with label
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        width = 30;
        height = 30;
    }

    static class Node {
        int x, y;
        String name;
        Color color;
        public Node(String myName, int myX, int myY) {
            x = myX;
            y = myY;
            name = myName;
            color = Color.white;
        }

        public Node(String myName, int myX, int myY, Color myColor) {
            x = myX;
            y = myY;
            name = myName;
            color=myColor;
        }
    }

    static class edge {
        int i,j;

        public edge(int ii, int jj) {
            i = ii;
            j = jj;
        }
    }

    public void addNode(String name, int x, int y, Color color) {
        //add a node at pixel (x,y)
        nodes.add(new Node(name,x,y, color));
        this.repaint();
    }

    public void addNode(String name, int x, int y) {
        //add a node at pixel (x,y)
        nodes.add(new Node(name,x,y));
        this.repaint();
    }
    public void addEdge(int i, int j) {
        //add an edge between nodes i and j
        edges.add(new edge(i,j));
        this.repaint();
    }

    public void paint(Graphics g) { // draw the nodes and edges
        FontMetrics f = g.getFontMetrics();
        int nodeHeight = Math.max(height, f.getHeight());

        g.setColor(Color.black);
        for (edge e : edges) {
            g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
                    nodes.get(e.j).x, nodes.get(e.j).y);
        }

        for (Node n : nodes) {
            int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
            g.setColor(n.color);
            g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2,
                    nodeWidth, nodeHeight);
            g.setColor(Color.black);
            g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2,
                    nodeWidth, nodeHeight);

            g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
                    n.y+f.getHeight()/2);
        }
    }
}

class testGraphDraw {
    //Here is some example syntax for the GraphDraw class
    public static void main(String[] args) {
        GraphDraw frame = new GraphDraw("Test Window");

        int xInit = 800;
        int yInit = 25;
        int xDiff = 100;
        int yDiff = 15;

        frame.setSize(1000,6000);
        frame.setVisible(true);

        List<State> states = new ArrayList<>();
        ArrayDeque<State> stateQueue = new ArrayDeque<>();
        State stateInit = State.getInstance(0,0);
        states.add(stateInit);
        stateQueue.add(stateInit.throwHead());
        stateQueue.add(stateInit.throwTail());
        frame.addNode(states.get(0).toString(), xInit,yInit);

        while (!stateQueue.isEmpty()){
            State state = stateQueue.pollFirst();
            System.out.println(state);
            System.out.println(state.whatToDoNext());
            states.add(state);
            if (state.whatToDoNext() == CoinAction.THROW_AGAIN){
                State stateHead = state.throwHead();
                State stateTail = state.throwTail();
                if (!stateQueue.contains(stateHead)){
                    stateQueue.addLast(stateHead);
                    System.out.println("Head : " + stateHead);
                }
                if (!stateQueue.contains(stateTail)){
                    stateQueue.addLast(stateTail);
                    System.out.println("Tail : " + stateTail);
                }
                frame.addNode(state.toString(),
                        xInit + xDiff/2 * state.getThrowCount()-xDiff*state.getHeads(),
                        yInit+yDiff*state.getThrowCount());
            }
            if (state.whatToDoNext() == CoinAction.GUESS_FAIR){
                frame.addNode(state.toString(),
                        xInit + xDiff/2 * state.getThrowCount()-xDiff*state.getHeads(),
                        yInit+yDiff*state.getThrowCount(),
                        Color.cyan);
            }
            if (state.whatToDoNext() == CoinAction.GUESS_CHEATING){
                frame.addNode(state.toString(),
                        xInit + xDiff/2 * state.getThrowCount()-xDiff*state.getHeads(),
                        yInit+yDiff*state.getThrowCount(),
                        Color.red);
            }
        }
        for (State state : states){
            if (state.whatToDoNext() == CoinAction.THROW_AGAIN){
                frame.addEdge(states.indexOf(state),states.indexOf(state.throwHead()));
                frame.addEdge(states.indexOf(state),states.indexOf(state.throwTail()));
            }
        }
    }
}