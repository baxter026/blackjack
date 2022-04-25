package NEAT;
import java.util.*;

public class Phenotype {

    //created by genotype
    //represents nodes and connections
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;

    ArrayList<Vertex> vertices_inputs;
    ArrayList<Vertex> vertices_outputs;

    public float score = 0;


    Phenotype(){
        vertices = new ArrayList<>();
        edges = new ArrayList<>();

        vertices_inputs = new ArrayList<>();
        vertices_outputs = new ArrayList<>();

    }


    void inscribeGenotype(Genotype code) {
        vertices.clear();
        edges.clear();

        int vertexCount = code.nodes.size();
        int edgeCount = code.connections.size();

        for (int i = 0; i < vertexCount; i++)
        {
            //cast to int then to other enumerator type --what?
            addVertex(code.nodes.get(i).type, code.nodes.get(i).index);
        }

        for (int i = 0; i < edgeCount; i++)
        {
            addEdge(code.connections.get(i).source, code.connections.get(i).destination, code.connections.get(i).weight, code.connections.get(i).enabled);
        }
    }

    void addVertex(NodeType type, int index){
        Vertex v = new Vertex(type, index);
        vertices.add(v);
    }

    void addEdge(int source, int destination, float weight, boolean enabled)
    {
        Edge e = new Edge(source, destination, weight, enabled);
        edges.add(e);

        vertices.get(e.destination).incoming.add(e);
    }

    public void processGraph()
    {
        int verticesCount = vertices.size();

        //populate input and output sub-lists
        for (int i = 0; i < verticesCount; i++)
        {
            Vertex vertex = vertices.get(i);

            if (vertex.type == NodeType.Input)
            {
                vertices_inputs.add(vertex);
            }
            else if (vertex.type == NodeType.Output)
            {
                vertices_outputs.add(vertex);
            }
        }
    }

    public float[] propagate(float[] X)
    {
        int repeats = 10;

        for (int e = 0; e < repeats; e++)
        {
            for (int i = 0; i < vertices_inputs.size(); i++)
            {
                vertices_inputs.get(i).value = X[i];
            }

            for (int i = 0; i < vertices.size(); i++)
            {
                if (vertices.get(i).type == NodeType.Output)
                {
                    continue;
                }

                int paths = vertices.get(i).incoming.size();

                for (int j = 0; j < paths; j++)
                {
                    vertices.get(i).value += vertices.get(vertices.get(i).incoming.get(j).source).value * vertices.get(i).incoming.get(j).weight * (vertices.get(i).incoming.get(j).enabled ? 1.0f : 0.0f);
                }

                if (vertices.get(i).incoming.size() > 0)
                {
                    vertices.get(i).value = Sigmoid(vertices.get(i).value);
                }
            }

            float[] Y = new float[vertices_outputs.size()];

            for (int i = 0; i < vertices_outputs.size(); i++)
            {
                int paths = vertices_outputs.get(i).incoming.size();

                for (int j = 0; j < paths; j++)
                {
                    vertices_outputs.get(i).value += vertices.get(vertices_outputs.get(i).incoming.get(j).source).value * vertices_outputs.get(i).incoming.get(j).weight * (vertices_outputs.get(i).incoming.get(j).enabled ? 1.0f : 0.0f);
                }

                if (vertices_outputs.get(i).incoming.size() > 0)
                {
                    vertices_outputs.get(i).value = Sigmoid(vertices_outputs.get(i).value);
                    Y[i] = vertices_outputs.get(i).value;
                }
            }

            if (e == repeats - 1)
            {
                return Y;
            }
        }

        return new float[0];
    }

    public float Sigmoid(float x)
    {

        return 1.0f / (1.0f + (float)Math.pow(Math.E, -1.0f * x));
    }
}
