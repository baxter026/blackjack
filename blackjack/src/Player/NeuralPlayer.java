package Player;

public class NeuralPlayer {
    public NEAT.Phenotype network;
    public NetworkAdapter adapter;

    public Decision decideHit(){
        float Y[] = network.propagate(adapter.pack);

        if(Y[1] > 0.5f){
            return Decision.Hit;
        }
        return Decision.Stand;
    }
    public Decision decideDouble(){
        float Y[] = network.propagate(adapter.pack);

        if(Y[0] > 0.5f){
            return Decision.Double;
        }
        return Decision.Continue;
    }




}
