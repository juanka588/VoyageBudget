package voyage.unal.com.voyagebudget.LN;

/**
 * Created by Christian on 17/05/2015.
 */
public class Step {

    public Node from;
    public Node to;
    public double time;
    public double cost;
    Step(){}
    Step(Node e1, Node e2, double t, double c){
        from = e1;
        to = e2;
        time = t;
        c = cost;
    }
}
