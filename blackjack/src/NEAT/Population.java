package NEAT;
import java.util.*;

public class Population {

    public static int generation = 0;

    static int POPULATION_SIZE;

    static int INPUTS ;
    static int OUTPUTS;
    static int MAX_STALENESS = 20;//generations without improvement.
    static float PORTION = 0.2f;

    static ArrayList<Species> species = new ArrayList<Species>();

    public static ArrayList<Genotype> genetics = new ArrayList<Genotype>();

    public static ArrayList<Phenotype> population = new ArrayList<Phenotype>();;


    public static void generateBasePopulation(int size,int inputs, int outputs){

        POPULATION_SIZE = size;
        INPUTS = inputs;
        OUTPUTS = outputs;

        for(int i = 0; i < POPULATION_SIZE; i++){
            Genotype genotype = generateBaseGenotype(inputs,outputs);
            genetics.add(genotype);
            addToSpecies(genotype);
        }

        registerBaseInnovations(inputs,outputs);

        for(int i = 0; i < POPULATION_SIZE; i++){
            Mutation.mutate(genetics.get(i));
        }

        inscribePopulation();
    }

    static Genotype generateBaseGenotype(int inputs, int outputs){
        Genotype genotype = new Genotype();
        for(int i = 0; i < inputs; i++){
            genotype.addNode(NodeType.Input, i);
        }
        for(int i = 0; i < outputs; i++){
            genotype.addNode(NodeType.Output,i + inputs);
        }

        genotype.addConnection(0, inputs,0.0f,true,0);
        return genotype;
    }

    static void registerBaseInnovations(int inputs, int outputs){

        for (int i = 0; i < inputs; i++)
        {
            for (int j = 0; j < outputs; j++)
            {
                int input = i;
                int output = j + inputs;

                Connection info = new Connection(input, output, 0.0f, true);

                Mutation.registerInnovation(info);
            }
        }

    }

    public static void newGeneration(){

        calculateAdjustedFitness();

        for(int i = 0; i < species.size(); i++){

            species.get(i).sortMembers();

            species.get(i).cullToPortion(PORTION);

            if(species.get(i).members.size() <=1){
                species.remove(i);
                i--;
            }
        }

        updateStaleness();

        float fitnessSum = 0.0f;

        for (int i = 0; i < species.size(); i++)
        {
            species.get(i).calculateAdjustedFitnessSum();
            fitnessSum += species.get(i).fitnessSum;
        }

        ArrayList<Genotype> children = new ArrayList<>();

        for(int i = 0; i < species.size(); i++){
            int build = (int)(POPULATION_SIZE * (species.get(i).fitnessSum / fitnessSum)) - 1;

            for(int j = 0; j < build; j++){
                Genotype child = species.get(i).breed();
                children.add(child);

            }
        }

        while(POPULATION_SIZE > species.size() + children.size()){
            Random r = new Random();
            int selection = r.nextInt(species.size());
            Genotype child = species.get(selection).breed();

            children.add(child);
        }

        for(int i = 0; i < species.size(); i++){
            species.get(i).cullToOne();
        }
        int childrenCount = children.size();

        for (int i = 0; i < childrenCount; i++){

            addToSpecies(children.get(i));
        }

        genetics.clear();


        for (int i = 0; i < species.size(); i++)
        {
            for (int j = 0; j < species.get(i).members.size(); j++)
            {
                genetics.add(species.get(i).members.get(j));
            }
        }
        inscribePopulation();

        generation++;
    }

    static void calculateAdjustedFitness(){
        int speciesCount = species.size();

        for (int i = 0; i < speciesCount; i++)
        {
            int membersCount = species.get(i).members.size();

            for (int j = 0; j < membersCount; j++)
            {
                species.get(i).members.get(j).adjustedFitness = species.get(i).members.get(j).fitness / membersCount;
            }
        }
    }

    static void updateStaleness(){

        int speciesCount = species.size();

        for (int i = 0; i < speciesCount; i++)
        {
            if (speciesCount == 1)
            {
                return;
            }

            float top = species.get(i).members.get(0).fitness;

            if (species.get(i).topFitness < top)
            {
                species.get(i).topFitness = top;
                species.get(i).staleness = 0;
            }
            else
            {
                species.get(i).staleness++;
            }

            if (species.get(i).staleness >= MAX_STALENESS)
            {
                System.out.println("Removing genome from species...");//test
                species.remove(i);
                i--;
                speciesCount--;
            }
        }

    }

    static void inscribePopulation(){
        population.clear();

        for(int i = 0; i < POPULATION_SIZE; i++){
            genetics.get(i).fitness = 0.0f;
            genetics.get(i).adjustedFitness = 0.0f;

            Phenotype physical = new Phenotype();
            physical.inscribeGenotype(genetics.get(i));
            physical.processGraph();

            population.add(physical);
        }
    }

    static void addToSpecies(Genotype genotype){

        if (species.size() == 0) {
            Species new_species = new Species();
            new_species.members.add(genotype);

            species.add(new_species);
        }
        else {
            int speciesCount = species.size();

            boolean found = false;

            for (int i = 0; i < speciesCount; i++) {

                float distance = Crossover.speciationDistance(species.get(i).members.get(0), genotype);

                if (distance < Crossover.DISTANCE)
                {
                    species.get(i).members.add(genotype);
                    found = true;
                    break;
                }
            }

            if (!found){
                Species new_species = new Species();
                new_species.members.add(genotype);

                species.add(new_species);
            }
        }

    }




}
