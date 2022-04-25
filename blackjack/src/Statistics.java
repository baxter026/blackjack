import Player.Decision;
import java.text.*;

public class Statistics {

    static int decisions[] = new int [7];
    //0 surrender
    //1 didnt surrender
    //2 split
    //3 didnt split but could
    //4 double
    //5 hit
    //6 stand

    static int gameStats[] = new int [5];
    //0 hands played
    //1 blackjack pushs
    //2 player blackjacks
    //3 dealer blackjacks
    //4 player busts



    static int totalHitCardStats[][] = new int [18][10];//stats for when no ace is present 4 - 21
    static int totalStandCardStats[][] = new int [18][10];//stats for when no ace is present 4 - 21

    static int totalHitAceCardStats[][] = new int [10][10];//stats for when ace is present 11 - 21
    static int totalStandAceCardStats[][] = new int [10][10];//stats for when ace is present 11 - 21



    static int currentHitCardStats[][] = new int [18][10];//stats for when no ace is present 4 - 21
    static int currentStandCardStats[][] = new int [18][10];//stats for when no ace is present 4 - 21

    static int currentHitAceCardStats[][] = new int [10][10];//stats for when ace is present 11 - 21
    static int currentStandAceCardStats[][] = new int [10][10];//stats for when ace is present 11 - 21



    static int bestHitCardStats[][] = new int [18][10];//stats for when no ace is present 4 - 21
    static int bestStandCardStats[][] = new int [18][10];//stats for when no ace is present 4 - 21

    static int bestHitAceCardStats[][] = new int [10][10];//stats for when ace is present 11 - 21
    static int bestStandAceCardStats[][] = new int [10][10];//stats for when ace is present 11 - 21


    static void printTotalStats(){
        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("      2     3     4     5     6     7     8     9     10     Ace");
        System.out.println("================================================================");
        for(int i = 0; i < 17; i++){
            System.out.printf("%-6s", (i+4));
            for(int j = 0; j < 10; j++){

                System.out.printf("%-6s", df.format((float)totalHitCardStats[i][j]/(totalHitCardStats[i][j] + totalStandCardStats[i][j])) );

            }
            System.out.println();
        }
        System.out.println("\nAce present\n");
        System.out.println("     2     3     4      5      6      7     8     9     10    Ace");
        System.out.println("==============================================================");
        for(int i = 0; i < 9; i++){
            System.out.printf("%-6s",(i+12) );
            for(int j = 0; j < 10; j++){

                System.out.printf("%-6s",df.format((float)totalHitAceCardStats[i][j]/(totalHitAceCardStats[i][j] + totalStandAceCardStats[i][j])));

            }
            System.out.println();
        }

    }

    static void printBestStats(){
        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("      2     3     4     5     6     7     8     9     10     Ace");
        System.out.println("================================================================");
        for(int i = 0; i < 17; i++){
            System.out.printf("%-6s", (i+4));
            for(int j = 0; j < 10; j++){

                System.out.printf("%-6s", df.format((float)bestHitCardStats[i][j]/(bestHitCardStats[i][j] + bestStandCardStats[i][j])) );

            }
            System.out.println();
        }
        System.out.println("\nAce present\n");
        System.out.println("     2     3     4      5      6      7     8     9     10    Ace");
        System.out.println("==============================================================");
        for(int i = 0; i < 9; i++){
            System.out.printf("%-6s",(i+12) );
            for(int j = 0; j < 10; j++){

                System.out.printf("%-6s",df.format((float)bestHitAceCardStats[i][j]/(bestHitAceCardStats[i][j] + bestStandAceCardStats[i][j])));

            }
            System.out.println();
        }

    }

    static void copyToBest(){

        for(int i = 0; i < 17; i++){

            for(int j = 0; j < 10; j++){

                bestHitCardStats[i][j] = currentHitCardStats[i][j];
                bestStandCardStats[i][j] = currentStandCardStats[i][j];
            }
        }
        for(int i = 0; i < 9; i++){

            for(int j = 0; j < 10; j++){

                bestHitAceCardStats[i][j] = currentHitAceCardStats[i][j];
                bestStandAceCardStats[i][j] = currentStandAceCardStats[i][j];
            }
        }



    }

    static void addToTotal(){
        for(int i = 0; i < 17; i++){

            for(int j = 0; j < 10; j++){

                totalHitCardStats[i][j] += currentHitCardStats[i][j];
                totalStandCardStats[i][j] += currentStandCardStats[i][j];
            }
        }
        for(int i = 0; i < 9; i++){

            for(int j = 0; j < 10; j++){

                totalHitAceCardStats[i][j] += currentHitAceCardStats[i][j];
                totalStandAceCardStats[i][j] += currentStandAceCardStats[i][j];
            }
        }

    }

    static void resetTotalStats(){

        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 10; j++){

                totalHitCardStats[i][j] = 0;
                totalStandCardStats[i][j] = 0;

            }
        }

        for(int i = 0; i < 9; i++){

            for(int j = 0; j < 10; j++) {

                totalHitAceCardStats[i][j] = 0;
                totalStandAceCardStats[i][j] = 0;

            }
        }

    }

    static void resetCurrentStats(){

        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 10; j++){

                currentHitCardStats[i][j] = 0;
                currentStandCardStats[i][j] = 0;

            }
        }

        for(int i = 0; i < 9; i++){

            for(int j = 0; j < 10; j++) {

                currentHitAceCardStats[i][j] = 0;
                currentStandAceCardStats[i][j] = 0;

            }
        }

    }

    static void resetBestStats(){

        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 10; j++){

                bestHitCardStats[i][j] = 0;
                bestStandCardStats[i][j] = 0;

            }
        }

        for(int i = 0; i < 9; i++){

            for(int j = 0; j < 10; j++) {

                bestHitAceCardStats[i][j] = 0;
                bestStandAceCardStats[i][j] = 0;

            }
        }

    }

/*  2 3 4 5 6 7 8 9 10 a
4
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
20
21
2s      17
3s
4s
5s
6s
7s
8s
9s
10s
ad      26
2d      27
3d
4d
5d
6d
7d
8d
9d
10d

 */

}
