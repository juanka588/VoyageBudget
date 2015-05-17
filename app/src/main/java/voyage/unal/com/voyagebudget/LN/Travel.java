package voyage.unal.com.voyagebudget.LN;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by JuanCamilo on 16/05/2015.
 */
public class Travel {

    public static ArrayList<Node> path;
    public static ArrayList<Node> place;
    public static Stack<Node> tmpPath=new Stack<>();
    public static double maxCost;
    public static double maxTime;
    public static double bestTime;
    public static int maxPlaces;
    public static int n;


    public static double distance(Node currentPlace, Node nextPlace){ //improve distance function
        //distance between two nodes
        double dx = currentPlace.x - nextPlace.x;
        double dy = currentPlace.y - nextPlace.y;
        return 1.2 * Math.sqrt(dx*dx + dy * dy);

    }
    public static void permutation(double cost, double time, int mask, int currentPlace){

        boolean finish = true;
        tmpPath.push(place.get(currentPlace));
        for(int i = 0; i < n; i ++){
            if((mask&(1<<i)) == 0){
                if(cost + place.get(i).cost > maxCost || time + place.get(i).time + distance(place.get(currentPlace), place.get(i)) > maxTime){
                    continue;
                }
                else{
                    permutation(cost + place.get(i).cost, time + place.get(i).time + distance(place.get(currentPlace), place.get(i)), mask|(1<<i), i);
                    finish = false;
                }
            }
        }

        if(finish){
            int cont = 0;
            ArrayList<Node> tmpSolution = new ArrayList(tmpPath);
            for(int i = 0; i < n; i ++){
                if((mask&(1<<i)) >= 1){
                    cont ++;
                }
            }
            if(cont == maxPlaces){
                if(time < bestTime){
                    bestTime = time;
                    path = tmpSolution;
                }
            }
            if(cont > maxPlaces){
                maxPlaces = cont;
                bestTime = time;
                path = tmpSolution;
            }

        }
        if(!tmpPath.isEmpty())
            tmpPath.pop();

    }

    public ArrayList<Step> createPath(Node current, ArrayList<Node> graph , double budget, double time){

        place = graph;
        path = new ArrayList();
        maxPlaces = 0;
        maxCost = budget;
        maxTime = time;
        bestTime =(double)Integer.MAX_VALUE;
        n = place.size();

        Collections.sort(place);

        /*for(int i = 0; i < graph.size(); i ++){
            System.out.println("x " + graph.get(i).x + " y " + graph.get(i).y +
                    ", cost " + graph.get(i).cost);
        }*/

        int sumCost = 0, sumTime = 0;

        n = Math.min(10,place.size());

        for(int i = 0; i < n; i ++) {
            permutation(place.get(i).cost,place.get(i).time + distance(current, place.get(i)), (1<<i), i);
        }
        int idx = 0;
        /*for(Node it : path){
            System.out.println("lugar#"+ ++idx + " " + it.id);
        }*/
        //Log.e("best time was ", ""+bestTime);
        ArrayList<Step>trip = new ArrayList();

        if(!path.isEmpty())
            trip.add(new Step(current,path.get(0), maxTime - path.get(0).time - distance(current,path.get(0)), maxCost - path.get(0).cost));

        double currentCost =  maxCost - path.get(0).cost;
        double currentTime = maxTime - path.get(0).time + distance(current,path.get(0));

        for( int i = 1 ; i < n; i ++ ){
            Node e1 = new Node(place.get(i - 1));
            Node e2 = new Node(place.get(i));
            trip.add(new Step(e1,e2,currentTime - e1.time - e2.time - distance(e1,e2), currentCost - e1.cost - e2.cost));
            currentTime -= e1.time - e2.time;
            currentCost -= e1.cost - e2.cost;
        }

        return trip;
    }


    public static void main(String[] args) {
    }

}
