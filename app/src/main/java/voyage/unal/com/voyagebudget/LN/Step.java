package voyage.unal.com.voyagebudget.LN;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
        cost = c;
    }

    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("#0.0");
        String hours= formatter.format(time);
        String costos= formatter.format(cost)+"";
        return "Ve desde : "+from.name+" Hasta: "+to.name+" quedan "+hours+" horas y  "+costos +"UDS";
    }
}
