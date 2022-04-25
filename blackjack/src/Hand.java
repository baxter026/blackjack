public class Hand {

    Card hand[];
    int total;
    boolean ace;
    boolean doubled;


    Hand(Card a, Card b){
        hand = new Card[2];
        hand[0] = a;
        hand[1] = b;

        total = 0;
        ace = false;

        doubled =false;
    }

    void eval(){ //calculates total and if a soft ace is present
        int aces = 0;
        total = 0;
        ace = false;

        for(int i = 0; i < hand.length; i++){

            switch(hand[i].rank){
                case Ace:
                    total += 1;//adds one for ace now checks if it can be soft later
                    aces += 1;
                    break;
                case Two:
                    total += 2;
                    break;
                case Three:
                    total += 3;
                    break;
                case Four:
                    total += 4;
                    break;
                case Five:
                    total += 5;
                    break;
                case Six:
                    total += 6;
                    break;
                case Seven:
                    total += 7;
                    break;
                case Eight:
                    total += 8;
                    break;
                case Nine:
                    total += 9;
                    break;
                default:
                    total += 10;
            }

        }

        if(total <= 11 && aces != 0 ){
            total += 10;
            ace = true;
        }

    }

    void dealerEval(){//could probably get rid of this and just only keep one card in the dealers hand then draw another
        switch(hand[0].rank){
            case Ace:
                total = 11;//adds one for ace now checks if it can be soft later
                break;
            case Two:
                total = 2;
                break;
            case Three:
                total = 3;
                break;
            case Four:
                total = 4;
                break;
            case Five:
                total = 5;
                break;
            case Six:
                total = 6;
                break;
            case Seven:
                total = 7;
                break;
            case Eight:
                total = 8;
                break;
            case Nine:
                total = 9;
                break;
            default:
                total = 10;
        }
    }



    int getTotal(){

        return total;
    }

    boolean canSplit(){
        if(hand.length == 2 && hand[0].rank == hand[1].rank){
            return true;
        }
        return false;

    }

    void addCard(Card newCard){
        Card temp[] = new Card[hand.length+1];

        for(int i = 0; i < hand.length; i++){
            temp[i] = hand[i];
        }
        temp[hand.length] = newCard;

        hand = temp;

    }

    boolean blackjack(){

        if(hand.length == 2 && this.getTotal() == 21){
            return true;
        }

        return false;

    }


}
