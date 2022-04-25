package NEAT;
import java.util.*;

public class Mutation {

    static float MUTATE_CONNECTION = 0.2f;
    static float MUTATE_NODE = 0.1f;
    static float MUTATE_ENABLE = 0.6f;
    static float MUTATE_DISABLE = 0.2f;
    static float MUTATE_WEIGHT = 2.0f;

    static float SHIFT_CHANCE = 0.9f;
    static float WEIGHT_SHIFT = 0.1f;

    static ArrayList<Innovation> history = new ArrayList<Innovation>();

    static void mutate(Genotype genotype){

        Random r = new Random();

        float roll;

        float probability = MUTATE_WEIGHT;

        while(probability > 0.0f){
            roll = r.nextFloat();

            if(roll < probability){
                mutateWeight(genotype);
            }

            probability--;
        }

        probability = MUTATE_CONNECTION;

        while(probability > 0.0f){
            roll = r.nextFloat();

            if(roll < probability){
                mutateConnection(genotype);
            }

            probability--;
        }

        probability = MUTATE_NODE;

        while(probability > 0.0f){
            roll = r.nextFloat();

            if(roll < probability){
                mutateNode(genotype);
            }

            probability--;
        }

        probability = MUTATE_ENABLE;

        while(probability > 0.0f){
            roll = r.nextFloat();

            if(roll < probability){
                mutateEnable(genotype);
            }

            probability--;
        }

        probability = MUTATE_DISABLE;

        while(probability > 0.0f){
            roll = r.nextFloat();

            if(roll < probability){
                mutateDisable(genotype);
            }

            probability--;
        }

    }

    static int registerInnovation(Connection info){
        int count = history.size();

        for(int i = 0; i < count; i++){

            Innovation inno = history.get(i);

            if(inno.source == info.source && inno.destination == info.destination){
                return inno.order;
            }
        }

        Innovation creation = new Innovation();
        creation.order = history.size();
        creation.source = info.source;
        creation.destination = info.destination;

        history.add(creation);

        return history.size() - 1;

    }

    static void mutateWeight(Genotype genotype){
        Random r = new Random();
        int selection = r.nextInt(genotype.connections.size());
        Connection connection = genotype.connections.get(selection);

        float roll = r.nextFloat();
        if(roll < SHIFT_CHANCE){ // shifts weight
            float weightChange = r.nextFloat();
            weightChange = (weightChange * WEIGHT_SHIFT) - (WEIGHT_SHIFT * 0.5f);
            connection.weight += weightChange;

        }
        else{ //randomized weight
            float w = r.nextFloat();
            w = (w * 4.0f) - 2.0f;

        }

    }

    static void mutateConnection(Genotype genotype){
        //finds possible new connections
        //chooses one with random weight
        //registers mutation in innovation array
        //adds to connection array

        Random r = new Random();
        int connectionCount = genotype.connections.size();
        int nodeCount = genotype.nodes.size();

        ArrayList<Connection> potential = new ArrayList<>();

        for(int i = 0; i< nodeCount; i++){
            for(int j = 0; j < nodeCount; j++){
                int source = genotype.nodes.get(i).index;
                int destination = genotype.nodes.get(j).index;

                NodeType sourceType = genotype.nodes.get(i).type;
                NodeType destinationType = genotype.nodes.get(j).type;

                if(sourceType == NodeType.Output || destinationType == NodeType.Input){
                    continue;
                }
                if(sourceType == destinationType){

                    continue;
                }

                boolean search = false;
                for(int k = 0; k < connectionCount; k++){//checks to see if connection exists
                    Connection connection = genotype.connections.get(k);

                    if(connection.source == source && connection.destination == destination){
                        search = true;
                        break;
                    }

                }
                if(!search){

                    float weight = r.nextFloat();
                    weight = (weight * 4.0f) - 2.0f;
                    Connection creation = new Connection(source, destination,weight,true);
                    potential.add(creation);
                }
            }


        }

        if(potential.size() <= 0){
            return;
        }

        int selection = r.nextInt(potential.size());
        Connection mutation = potential.get(selection);
        mutation.innovation = registerInnovation(mutation);

        genotype.addConnection(mutation.source,mutation.destination,mutation.weight,mutation.enabled,mutation.innovation);


    }

    static void mutateNode(Genotype genotype){
        //selects connection to split
        //creates new connection
            //both get a new innovation number
            //connection going to new node has

        //disables old connection

        int connectionCount = genotype.connections.size();

        Random r = new Random();

        int selection = r.nextInt(connectionCount);

        Connection connection = genotype.connections.get(selection);

        if(connection.enabled == false){
            return;
        }

        connection.enabled = false;

        int newNode = genotype.nodes.get(genotype.nodes.size() - 1).index + 1;

        Node node = new Node(NodeType.Hidden, newNode);

        Connection first = new Connection(connection.source, newNode, 1.0f, true);
        Connection second = new Connection(newNode, connection . destination, connection.weight, true);

        first.innovation = registerInnovation(first);
        second.innovation = registerInnovation(second);

        genotype.addNode(node.type, node.index);

        genotype.addConnection(first.source, first.destination, first.weight, first.enabled, first.innovation);
        genotype.addConnection(second.source, second.destination, second.weight, second.enabled, second.innovation);
    }

    static void mutateEnable(Genotype genotype){
        //find all disabled connections
        //enable one

        int connectionCount = genotype.connections.size();

        Random r = new Random();

        ArrayList<Connection> canidates = new ArrayList<Connection>();

        for(int i = 0; i < connectionCount; i++){

            if(!genotype.connections.get(i).enabled){
                canidates.add(genotype.connections.get(i));
            }
        }

        if(canidates.size() == 0){
            return;
        }

        int selection = r.nextInt(canidates.size());

        Connection connection = canidates.get(selection);

        connection.enabled = true;

    }

    static void mutateDisable(Genotype genotype){
        //find all enabled connections
        //enable one

        int connectionCount = genotype.connections.size();

        Random r = new Random();

        ArrayList<Connection> canidates = new ArrayList<Connection>();

        for(int i = 0; i < connectionCount; i++){

            if(genotype.connections.get(i).enabled){
                canidates.add(genotype.connections.get(i));
            }
        }

        if(canidates.size() == 0){
            return;
        }

        int selection = r.nextInt(canidates.size());

        Connection connection = canidates.get(selection);

        connection.enabled = false;

    }


}
