package NEAT;

public class Edge {

    EdgeType type = EdgeType.Forward;
    int source = 0;
    int destination = 0;

    float weight = 0.0f;
    boolean enabled = true;

    float signal = 0.0f;

    Edge(int s, int d, float w, boolean e){

        source = s;
        destination = d;
        weight = w;
        enabled = e;


    }



}
