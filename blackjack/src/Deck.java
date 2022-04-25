import java.util.*;

public class Deck {

    Deck() {
        deck = new Card[52];

        int i = 0;

        for (Suit s : Suit.values()) {

            for (Rank r : Rank.values()) {

                deck[i] = new Card(r, s);
                i++;

            }
        }

    }


    Card drawCard(){

        Random r = new Random();
        Card drawn = deck[r.nextInt(52)];
        return drawn;
    }

    Card deck[];


}

/*
public class Deck {

    Deck(int numOfDecks){

        deck = new Card[52 * numOfDecks];

        int j = 0;
        for(int i = 0; i < 6; i++){

            for (Suit s : Suit.values()) {

                for (Rank r : Rank.values()) {

                    deck[j] = new Card(r,s);
                    j++;

                }

            }

        }

    }


    void shuffle(){

        Random r = new Random();

        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = deck.length-1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = r.nextInt(i+1);

            // Swap arr[i] with the element at random index
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }

    }


    void betterShuffle(){
        Random r = new Random();
        for (int i = 30; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = r.nextInt(i+1);

            // Swap arr[i] with the element at random index
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }

    }

    Card deck[];


}
 */
