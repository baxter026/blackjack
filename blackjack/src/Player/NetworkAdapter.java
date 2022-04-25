package Player;

public class NetworkAdapter {
    float pack[];//inputs 0-3

    //0 dealer
    //1 total
    //2 soft ace


    public NetworkAdapter(){

        pack = new float[3];
    }

    public void setDealerCard(int dealer){

        pack[0] = (dealer - 2.0f) / 9.0f;

    }

    public void setHandTotal(int total){

        pack[1] = (total - 4.0f) / 17.0f;

    }


    public void setAce(boolean ace){
        if(ace){
            pack[2] = 1.0f;
        }
        else{
            pack[2] = 0.0f;
        }
    }


}
