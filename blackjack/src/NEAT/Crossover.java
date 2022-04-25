package NEAT;
import java.util.*;

public class Crossover {

    static float CROSSOVER_CHANCE = .75f;

    static float C1 = 1.0f;
    static float C2 = 1.0f;
    static float C3 = 0.4f;
    static float DISTANCE = 1.0f;



    static Genotype produceOffspring(Genotype first, Genotype second){

        ArrayList<Connection> copy_first = new ArrayList<>();
        ArrayList<Connection> copy_second = new ArrayList<>();

        copy_first.addAll(first.connections);
        copy_second.addAll(first.connections);

        ArrayList<Connection> match_first = new ArrayList<>();
        ArrayList<Connection> match_second = new ArrayList<>();

        ArrayList<Connection> disjoint_first = new ArrayList<>();
        ArrayList<Connection> disjoint_second = new ArrayList<>();

        ArrayList<Connection> excess_first = new ArrayList<>();
        ArrayList<Connection> excess_second = new ArrayList<>();

        int genes_first = first.connections.size();
        int genes_second = second.connections.size();

        int invmax_first = first.connections.get(first.connections.size() - 1).innovation;
        int invmax_second = second.connections.get(second.connections.size() - 1).innovation;

        int invmin = invmax_first > invmax_second ? invmax_second : invmax_first;

        for (int i = 0; i < genes_first; i++)
        {
            for (int j = 0; j < genes_second; j++)
            {
                Connection info_first = copy_first.get(i);
                Connection info_second = copy_second.get(j);

                //matching genes
                if (info_first.innovation == info_second.innovation)
                {
                    match_first.add(info_first);
                    match_second.add(info_second);

                    copy_first.remove(info_first);
                    copy_second.remove(info_second);

                    i--;
                    genes_first--;
                    genes_second--;
                    break;
                }
            }
        }

        for (int i = 0; i < copy_first.size(); i++)
        {
            if (copy_first.get(i).innovation > invmin)
            {
                excess_first.add(copy_first.get(i));
            }
            else
            {
                disjoint_first.add(copy_first.get(i));
            }
        }

        for (int i = 0; i < copy_second.size(); i++)
        {
            if (copy_second.get(i).innovation > invmin)
            {
                excess_second.add(copy_second.get(i));
            }
            else
            {
                disjoint_second.add(copy_second.get(i));
            }
        }

        Genotype child = new Genotype();

        int matching = match_first.size();
        Random r = new Random();
        for (int i = 0; i < matching; i++)
        {
            int roll = r.nextInt(2);

            if (roll == 0 || !match_second.get(i).enabled)
            {
                child.addConnection(match_first.get(i).source, match_first.get(i).destination, match_first.get(i).weight, match_first.get(i).enabled, match_first.get(i).innovation);
            }
            else
            {
                child.addConnection(match_second.get(i).source, match_second.get(i).destination, match_second.get(i).weight, match_second.get(i).enabled, match_second.get(i).innovation);
            }
        }

        for (int i = 0; i < disjoint_first.size(); i++)
        {
            child.addConnection(disjoint_first.get(i).source, disjoint_first.get(i).destination, disjoint_first.get(i).weight, disjoint_first.get(i).enabled, disjoint_first.get(i).innovation);
        }

        for (int i = 0; i < excess_first.size(); i++)
        {
            child.addConnection(excess_first.get(i).source, excess_first.get(i).destination, excess_first.get(i).weight, excess_first.get(i).enabled, excess_first.get(i).innovation);
        }

        child.sortConnections();



        ArrayList<Integer> ends = new ArrayList<Integer>();

        int nodeCount = first.nodes.size();

        for (int i = 0; i < first.nodes.size(); i++)
        {
            Node node = first.nodes.get(i);

            if (node.type == NodeType.Hidden)
            {
                break;
            }

            ends.add(node.index);
            child.addNode(node.type, node.index);
        }

        addUniqueVertices(child, ends);

        child.sortNodes();

        return child;
    }


    static void addUniqueVertices(Genotype genotype, ArrayList<Integer> ends)
    {
        ArrayList<Integer> unique = new ArrayList<Integer>();

        int connectionCount = genotype.connections.size();

        for (int i = 0; i < connectionCount; i++)
        {
            Connection info = genotype.connections.get(i);

            if (!ends.contains(info.source) && !unique.contains(info.source))
            {
                unique.add(info.source);
            }

            if (!ends.contains(info.destination) && !unique.contains(info.destination))
            {
                unique.add(info.destination);
            }
        }

        int uniques = unique.size();

        for (int i = 0; i < uniques; i++)
        {
            genotype.addNode(NodeType.Hidden, unique.get(i));
        }
    }


    static float speciationDistance(Genotype first, Genotype second)
    {
        ArrayList<Connection> copy_first = new ArrayList<>();
        ArrayList<Connection> copy_second = new ArrayList<>();

        copy_first.addAll(first.connections);
        copy_second.addAll(second.connections);

        ArrayList<Connection> match_first = new ArrayList<>();
        ArrayList<Connection> match_second = new ArrayList<>();

        ArrayList<Connection> disjoint_first = new ArrayList<>();
        ArrayList<Connection> disjoint_second = new ArrayList<>();

        ArrayList<Connection> excess_first = new ArrayList<>();
        ArrayList<Connection> excess_second = new ArrayList<>();

        int genes_first = first.connections.size();
        int genes_second = second.connections.size();

        int invmax_first = first.connections.get(first.connections.size() - 1).innovation;
        int invmax_second = second.connections.get(second.connections.size() - 1).innovation;

        int invmin = invmax_first > invmax_second ? invmax_second : invmax_first;

        float diff = 0.0f;

        for (int i = 0; i < genes_first; i++)
        {
            for (int j = 0; j < genes_second; j++)
            {
                Connection info_first = copy_first.get(i);
                Connection info_second = copy_second.get(j);

                //matching genes
                if (info_first.innovation == info_second.innovation)
                {
                    float weightDiff = Math.abs(info_first.weight - info_second.weight);
                    diff += weightDiff;

                    match_first.add(info_first);
                    match_second.add(info_second);

                    copy_first.remove(info_first);
                    copy_second.remove(info_second);

                    i--;
                    genes_first--;
                    genes_second--;
                    break;
                }
            }
        }

        for (int i = 0; i < copy_first.size(); i++)
        {
            if (copy_first.get(i).innovation > invmin)
            {
                excess_first.add(copy_first.get(i));
            }
            else
            {
                disjoint_first.add(copy_first.get(i));
            }
        }

        for (int i = 0; i < copy_second.size(); i++)
        {
            if (copy_second.get(i).innovation > invmin)
            {
                excess_second.add(copy_second.get(i));
            }
            else
            {
                disjoint_second.add(copy_second.get(i));
            }
        }

        int match = match_first.size();
        int disjoint = disjoint_first.size() + disjoint_second.size();
        int excess = excess_first.size() + excess_second.size();

        int n = Math.max(first.connections.size(), second.connections.size());

        float E = excess / (float)n;
        float D = disjoint / (float)n;
        float W = diff / (float)match;

        return E * C1 + D * C2 + W * C3;
    }

    //genes with shared innovation numbers are chosen at random
    //genes that are not shared are completely inheirited from more fit parent

}
