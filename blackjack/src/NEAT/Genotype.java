package NEAT;

import java.util.*;

public class Genotype {

    ArrayList<Node> nodes;
    ArrayList<Connection> connections;
    public float fitness;
    float adjustedFitness;

    Genotype(){
        nodes = new ArrayList<>();
        connections = new ArrayList<>();

    }


    void addConnection(int source, int destination, float weight,boolean enabled, int innovation){
        Connection temp = new Connection(source,destination,weight,enabled);
        temp.innovation = innovation;
        connections.add(temp);
    }
    void addNode(NodeType type, int index){

        nodes.add(new Node(type, index));

    }

    Genotype cloneGenotype(){
        Genotype copy = new Genotype();

        int nodeCount = nodes.size();

        for(int i = 0; i < nodeCount; i++){


            copy.addNode(nodes.get(i).type,nodes.get(i).index);
        }
        int connectionCount = connections.size();

        for(int i = 0; i < connectionCount; i++){


            copy.addConnection(connections.get(i).source, connections.get(i).destination, connections.get(i).weight, connections.get(i).enabled, connections.get(i).innovation);
        }
        return copy;
    }

    void sortNodes(){
        nodes.sort(this::compareNodeByOrder);
    }

    void sortConnections(){
        connections.sort(this::compareConnectionByInnovation);
    }

    int compareNodeByOrder(Node a, Node b){
        if(a.index > b.index){
            return 1;
        }
        else if(a.index == b.index){
            return 0;
        }
        return -1;

    }

    public int compareConnectionByInnovation(Connection a, Connection b)
    {
        if (a.innovation > b.innovation)
        {
            return 1;
        }
        else if (a.innovation == b.innovation)
        {
            return 0;
        }

        return -1;
    }

    public void printGenotype(){//used for initial testing to make sure it was working

        for(int i = 0; i < nodes.size();i++){
            System.out.println(nodes.get(i).type + " " + nodes.get(i).index);

        }
        System.out.println();
        for(int i = 0; i < connections.size();i++){
            System.out.println(connections.get(i).source + " " + connections.get(i).destination+ " " +
                                connections.get(i).weight+ " " + connections.get(i).enabled);

        }

    }

}
