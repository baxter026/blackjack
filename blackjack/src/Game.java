import NEAT.*;
import Player.*;
import java.util.*;



public class Game {

    static ArrayList<NEAT.Phenotype> players;
    static ArrayList<NEAT.Genotype> players_g;
    static NetworkAdapter adapter_;
    static float highScore = 0;
    static float lowScore = 0;


    public Game(){
        players = new ArrayList<>();
        players_g = new ArrayList<>();
        adapter_ = new NetworkAdapter();
    }



    public static void playGame(int handsToPlay){
        //collects stats on the players all in 1 array
        //need to change this so stats are individually stored
        Statistics.resetBestStats();
        Statistics.resetCurrentStats();
        Statistics.resetTotalStats();

        highScore = -1000000;//better solution needed
        lowScore = 1000000;
        Statistics.decisions[0] = 0;
        Statistics.decisions[1] = 0;
        Statistics.decisions[2] = 0;
        Statistics.decisions[3] = 0;
        Statistics.decisions[4] = 0;
        Statistics.decisions[5] = 0;
        Statistics.decisions[6] = 0;
        //Statistics.decisions[7] = 0;

        Statistics.gameStats[0] = 0;
        Statistics.gameStats[1] = 0;
        Statistics.gameStats[2] = 0;
        Statistics.gameStats[3] = 0;
        Statistics.gameStats[4] = 0;



        System.out.println("Generation " + Population.generation);
        System.out.println();
        //prepares variables for new generation
        if(players.size() > 0) {
            players.clear();
            players_g.clear();
        }
        for(int i = 0; i < Population.population.size(); i++){
            Population.population.get(i).score = 0;

            players.add(Population.population.get(i));
            players_g.add(Population.genetics.get(i));

        }



        int bestIndex = 0;
        for(int i = 0; i < Population.population.size(); i++){
            Statistics.resetCurrentStats();
            session(handsToPlay, i);
            Statistics.addToTotal();

            if(players.get(i).score > highScore){
                highScore = players.get(i).score;
                //Statistics.copyToBest();
                bestIndex = i;
            }
            else if(players.get(i).score < lowScore){
                lowScore = players.get(i).score;
            }
        }


        for(int i = 0; i < Population.population.size(); i++){
            //players_g.get(i).fitness = (float)(players.get(i).score  - lowScore)/(highScore-lowScore);
            players_g.get(i).fitness = players.get(i).score + 10;//+ 10 so it wont be negative
        }

        //should make function for this mess
        //maybe in statistics class
        /*
        System.out.println("Game statistics: ");
        System.out.println("Hands dealt " + Statistics.gameStats[0]);
        System.out.println("Blackjack pushes " + Statistics.gameStats[1]);
        System.out.println("player blackjacks " + Statistics.gameStats[2]);
        System.out.println("dealer blackjacks " + Statistics.gameStats[3] );
        System.out.println("Hands not made to decision " + (Statistics.gameStats[1] + Statistics.gameStats[2]+Statistics.gameStats[3]) + "\n");


         */
        System.out.println("Generation decision statistics:");
        //System.out.println("Surrendered " + Statistics.decisions[0]);
        //System.out.println("Didnt surrendered " + Statistics.decisions[1]);
        //System.out.println("Split when could " + Statistics.decisions[2]);
        //System.out.println("Didnt split when could " + Statistics.decisions[3]);
        System.out.println("Doubled " + Statistics.decisions[4]);
        System.out.println("Hit " + Statistics.decisions[5]);
        System.out.println("Stood " + Statistics.decisions[6] );
        System.out.println("Times busted " + Statistics.gameStats[4] + "\n");

        System.out.print("Best score ");
        System.out.printf("%5.3f%n", highScore);
        System.out.print("Worst score ");
        System.out.printf("%5.3f%n", lowScore);


        if(Population.generation % 10 == 0){

            System.out.println("Best performer");
            //Statistics.printBestStats();
            System.out.println();
            printBestChart(bestIndex);


        }
        System.out.println("\n");

        //players_g.get(bestIndex).printGenotype();


    }

    static void session(int handsToPlay, int turn){

        NeuralPlayer neuralPlayer = new NeuralPlayer();
        neuralPlayer.network = players.get(turn);
        neuralPlayer.adapter = adapter_;

        Deck gameDeck = new Deck();

        Hand dealerHand;
        ArrayList<Hand> playerHands = new ArrayList<>();

        int balance = 0;

        int busted = 0;

        int improvedHand = 0;//counts amount of times the player hit without busting

        int playableHand = 0;// counts amount of times hand made it past initial dealing sequence

        for(int i = 0; i < handsToPlay; i++){
            playerHands.clear();

            Statistics.gameStats[0]++;
            playerHands.add(new Hand(gameDeck.drawCard(),gameDeck.drawCard()));
            dealerHand = new Hand(gameDeck.drawCard(),gameDeck.drawCard());
            dealerHand.eval();
            playerHands.get(0).eval();

            if(dealerHand.blackjack() && playerHands.get(0).blackjack()){
                Statistics.gameStats[1]++;
                continue;
            }
            else if(playerHands.get(0).blackjack()){
                Statistics.gameStats[2]++;
                balance += 15;

                continue;
            }
            else if(dealerHand.blackjack()){
                Statistics.gameStats[3]++;
                balance -= 15;

                continue;
            }
            playableHand++;

            dealerHand.dealerEval();// sets dealers total to only the first card

            //adds data to inputs
            neuralPlayer.adapter.setDealerCard(dealerHand.total);
            neuralPlayer.adapter.setHandTotal(playerHands.get(0).total);
            neuralPlayer.adapter.setAce(playerHands.get(0).ace);

            //neuralPlayer.adapter.setSplit(playerHands.get(0).canSplit());//removed for now
            //create loop to split hands

            for(int j = 0; j < playerHands.size(); j++) {//plays hands
                if (neuralPlayer.decideDouble() == Decision.Double) {
                    playerHands.get(j).addCard(gameDeck.drawCard());
                    playerHands.get(j).doubled = true;
                    playerHands.get(j).eval();

                    Statistics.decisions[4]++;
                    continue;
                }

                while (playerHands.get(j).total < 21 && neuralPlayer.decideHit() == Decision.Hit) {//hit
                    Statistics.decisions[5]++;

                    playerHands.get(j).addCard(gameDeck.drawCard());
                    playerHands.get(j).eval();

                    neuralPlayer.adapter.setHandTotal(playerHands.get(j).total);
                    neuralPlayer.adapter.setAce(playerHands.get(j).ace);

                }
                if (playerHands.get(j).total <= 21) {//stood
                    Statistics.decisions[6]++;
                } else {//busted
                    Statistics.gameStats[4]++;
                }


            }

            dealerHand.eval();//updates dealer hand to include second card

            while (dealerHand.total < 17) {
                dealerHand.addCard(gameDeck.drawCard());
                dealerHand.eval();

            }

            for (int j = 0; j < playerHands.size(); j++) { //process completed hands
                int bet = 10;
                if(playerHands.get(j).doubled){
                    bet *= 2;
                }
                if (playerHands.get(j).total > 21) {//player busted
                    balance -= bet;
                } else if (dealerHand.total > 21) {//dealer busted
                    balance += bet;
                } else if (dealerHand.total > playerHands.get(j).total) {//dealer won
                    balance -= bet;
                } else if (dealerHand.total < playerHands.get(j).total) {//player won
                    balance += bet;
                } else {//push

                }

            }

        }

        //players.get(turn).score = (float)balance / (float)playableHand;

        // create some kind of function that combines balance busts and hits without busting
        // maybe recreate hitting and standing feature

        players.get(turn).score = balance / (float)playableHand;


    }


    static void printBestChart(int bestIndex){//does a quick run of the best performing network.
                                              //by doing performing every possible match i can avoid keeping track of individual decisions during the game and
                                              //ensure that every possiblity will recorded

        Decision desision[][] = new Decision[25][10];


        NeuralPlayer neuralPlayer = new NeuralPlayer();
        neuralPlayer.network = players.get(bestIndex);
        neuralPlayer.adapter = adapter_;


        neuralPlayer.adapter.setAce(false);
        for(int i = 0; i < 10; i++){

            for(int j = 0; j < 17; j++){
                neuralPlayer.adapter.setDealerCard(i+2);
                neuralPlayer.adapter.setHandTotal(j+4);
                if(neuralPlayer.decideDouble() == Decision.Double){
                    desision[j][i] = Decision.Double;
                }
                else if(neuralPlayer.decideHit() == Decision.Hit){
                    desision[j][i] = Decision.Hit;
                }
                else{
                    desision[j][i] = Decision.Stand;
                }

            }

        }

        neuralPlayer.adapter.setAce(true);
        for(int i = 0; i < 10; i++){

            for(int j = 0; j < 8; j++){
                neuralPlayer.adapter.setDealerCard(i+2);
                neuralPlayer.adapter.setHandTotal(j+4);
                if(neuralPlayer.decideDouble() == Decision.Double){
                    desision[j+17][i] = Decision.Double;
                }
                else if(neuralPlayer.decideHit() == Decision.Hit){
                    desision[j+17][i] = Decision.Hit;
                }
                else{
                    desision[j+17][i] = Decision.Stand;
                }

            }

        }


        System.out.println("      2     3     4     5     6     7     8     9     10   Ace");
        System.out.println("==============================================================");
        for(int i = 0; i < 17; i++){
            System.out.printf("%-6s",i + 4 + " ");
            for(int j = 0; j < 10; j++) {
                if(desision[i][j] == Decision.Double){
                    System.out.printf("%-6s","D ");

                }
                else if(desision[i][j] == Decision.Hit ){
                    System.out.printf("%-6s","H ");
                }
                else{
                    System.out.printf("%-6s","S ");
                }

            }
            System.out.println();
        }

        for(int i = 0; i < 8; i++){
            System.out.printf("%-6s","A" + (i + 2) + " ");
            for(int j = 0; j < 10; j++) {
                if(desision[i+17][j] == Decision.Double){
                    System.out.printf("%-6s","D ");
                }
                else if(desision[i+17][j] == Decision.Hit){
                    System.out.printf("%-6s","H ");
                }
                else{
                    System.out.printf("%-6s","S ");
                }

            }
            System.out.println();
        }

    }





}

/*
                           10
        2 3 4 5 6 7 8 9 10 A
4  1
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20 17

a2 18
a3
a4
a5
a6
a7
a8
a9  25


 */
