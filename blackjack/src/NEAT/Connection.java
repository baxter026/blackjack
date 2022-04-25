package NEAT;

public class Connection {

    //node index #
    int source;
    int destination;

    float weight;

    boolean enabled;

    int innovation;
    //used for historical context in mutating and species analysis

    Connection(int s, int d, float w, boolean e){
        source = s;
        destination = d;
        weight = w;
        enabled = e;
        innovation = 0;

    }

}
