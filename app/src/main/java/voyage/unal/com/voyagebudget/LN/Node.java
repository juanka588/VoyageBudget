package voyage.unal.com.voyagebudget.LN;

/**
 * Created by JuanCamilo on 16/05/2015.
 */
public class Node implements Comparable<Node> {

    public int id;
    public double x;
    public double y;
    public double cost;
    public double time;
    public double rate;
    public double fitness;

    public Node(int id, double X, double Y, double COST, double TIME, double RATE) {
        this.id = id;
        x = X;
        y = Y;
        cost = COST;
        time = TIME;
        rate=RATE;
    }

    public Node(String s, String s1, String s2, String s3, String s4, String s5) {
        id = Integer.parseInt(s);
        x = Double.parseDouble(s1);
        y = Double.parseDouble(s2);
        cost = Double.parseDouble(s3);
        time = Double.parseDouble(s4);
        rate = Double.parseDouble(s5);
    }

    @Override
    public String toString() {
        return "id " + id + " x " + x + " y " + y + " cost " + cost + " time " + time;
    }

    public int compareTo(Node compareNode) {

        double compareQuantity = compareNode.fitness;

        //ascending order
        if (compareNode.fitness <= compareQuantity)
            return -1;
        return 1;
        //descending order
        //return compareQuantity - this.quantity;
    }

}
