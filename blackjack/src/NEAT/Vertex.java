package NEAT;
import java.util.*;

public class Vertex {
    NodeType type;
    int index;

    ArrayList<Edge> incoming;

    float value = 0.0f;

    public Vertex(NodeType t, int i)
    {
        type = t;
        index = i;

        incoming = new ArrayList<Edge>();
    }

}
