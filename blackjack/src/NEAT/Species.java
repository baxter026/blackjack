package NEAT;
import java.util.*;
import java.lang.Math;

public class Species {

    ArrayList<Genotype> members;

    float topFitness;//previous generations fitness

    int staleness;//consecutive generations without improvement

    float fitnessSum;//current generations fitness sum

    Species(){
        members = new ArrayList<Genotype>();
        topFitness = 0;
        staleness = 0;
        fitnessSum =0;
    }

    Genotype breed(){
        Random r = new Random();

        float roll = r.nextFloat();

        if(roll < Crossover.CROSSOVER_CHANCE && members.size() > 1){

            int s1 = r.nextInt(members.size());
            int s2 = r.nextInt(members.size() - 1);

            if(s2 >= s1){
                s2++;

            }
            if(s1 > s2){
                int temp = s1;
                s1 = s2;
                s2 = temp;
            }
            Genotype child = Crossover.produceOffspring(members.get(s1),members.get(s2));
            Mutation.mutate(child);
            return child;

        }
        else{
            int selection = r.nextInt(members.size());
            Genotype child = members.get(selection).cloneGenotype();
            Mutation.mutate(child);
            return child;
        }


    }


    void cullToPortion(float portion){

        if (members.size() <= 1)
        {
            return;
        }

        int remaining = (int)Math.ceil(members.size() * portion);

        while(members.size() > remaining){
            members.remove(members.size()-1);
        }
        
    }

    void cullToOne(){

        if (members.size() <= 1){
            return;
        }

        while (members.size() > 1){
            members.remove(1);
        }

    }

    void calculateAdjustedFitnessSum(){
        float sum = 0.0f;
        int memberCount = members.size();

        for(int i = 0; i < memberCount; i++){

            sum += members.get(i).adjustedFitness;
        }
        fitnessSum = sum;
    }

    //sorting functions

    void sortMembers(){

        members.sort(this::sortGenotypeByFitness);

    }

    int sortGenotypeByFitness(Genotype a, Genotype b){

        if(a.adjustedFitness > b.adjustedFitness){
            return -1;

        }
        else if(a.adjustedFitness == b.adjustedFitness){
            return 0;

        }
        return 1;
    }



}
