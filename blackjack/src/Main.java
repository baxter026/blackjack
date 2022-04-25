import NEAT.*;
import Player.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Game g = new Game();
        Population.generateBasePopulation(200,3,2);//
        for(int i = 0; i < 500; i++){
            Game.playGame(10000);
            Population.newGeneration();

        }


    }

}
