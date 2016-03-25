package utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import actors.Person;

public class GrouperUtil {
    public static final String CONFIG_FILE = "config.cfg";
    public static final String OUT_FILE = "groupings.out";
    public static final double THRESHOLD = getThreshold();
    public static final double THRESHOLD_DEFAULT = 0.1;
    
    private Person[] a;
    private int grouping;
    private float minDiff;
    private ArrayList<ArrayList<Person>> bestGroup;
    private HashMap<String, Integer> bloclist;
    private boolean isBlocGroup;

    public GrouperUtil(Person[] arr, int n, boolean isBlocGroup) {
        this.a = arr;
        this.grouping = n;
        this.minDiff = 999999999;
        this.bestGroup = new ArrayList<ArrayList<Person>>();
        this.isBlocGroup = isBlocGroup;
        if(this.isBlocGroup) this.initBloclist();
    }

    public ArrayList<ArrayList<Person>> automatedGrouping() {
        float allAveGWA = this.aveAllGWA(this.a);
        PermUtil<Person> perm = new PermUtil<Person>(this.a);

        System.out.println("Grouping each people...\n");

        Person[] combi = null;
        while((combi = perm.next()) != null) {
            ArrayList<ArrayList<Person>> groups = 
                new ArrayList<ArrayList<Person>>();
            ArrayList<Float> groupGWA = new ArrayList<Float>();

            intoGroups(groups, combi, this.grouping);
            aveGWAPerGroup(groupGWA, groups);

            float groupDiff = this.getAbsDiff(groupGWA, allAveGWA);

            if (groupDiff < minDiff){
                minDiff = groupDiff;
                bestGroup = groups;
                printNewGrouping();
            }

            if(minDiff < THRESHOLD){
                System.out.println("Minimum Diff: " + minDiff);
                return bestGroup;
            }
        }

        System.out.println("Minimum Diff: " + minDiff);
        return bestGroup;
    }

    private void aveGWAPerGroup(ArrayList<Float> groupGWA, 
            ArrayList<ArrayList<Person>> groups) {
        for (ArrayList<Person> g : groups) {
            groupGWA.add(this.aveAllGWA(g));
        }
    }

    private void printNewGrouping(){
        new Thread(){
            @Override
            public void run(){
                System.out.println(printGroup(bestGroup) + minDiff + "\n");
                FileUtil.printToFile(bestGroup, OUT_FILE);
            }
        }.start();
    }

    public String printGroup(ArrayList<ArrayList<Person>> group){
        String s = "";
        for(ArrayList<Person> g : group){
            for(Person p : g){
                s += p.getName() + " ";
            }
            s += "\n";
        }

        return s;
    }

    private float getAbsDiff(ArrayList<Float> groupGWA, float aveGWA) {
        float sumOfDiff = 0.0f;

        for (Float f : groupGWA) {
            sumOfDiff += Math.abs(aveGWA - f.floatValue());
        }

        return sumOfDiff;
    }

    private void intoGroups(ArrayList<ArrayList<Person>> groups, 
            Person[] list, int grouping) {
        for (int i=0; i<grouping; i++)
            groups.add(new ArrayList<Person>());

        for(int i=0; i<list.length; i++)
            groups.get(i%grouping).add(list[i]);
    }

    private float aveAllGWA(Person[] list) {
        float sumGWA = 0.0f;

        for (Person p : list) {
            sumGWA += p.getGWA();
        }
        return sumGWA / list.length;
    }

    private float aveAllGWA(ArrayList<Person> list) {
        float sumGWA = 0.0f;
        int maxBlocFreq = this.isBlocGroup? 0: 1;

        for (Person p : list) {
            sumGWA += p.getGWA();

            if(this.isBlocGroup){
                this.bloclist.put(p.getBloc(), this.bloclist.get(p.getBloc())+1);
                if(maxBlocFreq < this.bloclist.get(p.getBloc()))
                    maxBlocFreq = this.bloclist.get(p.getBloc());
            }
        }

        if(this.isBlocGroup) clearBloc();

        return (sumGWA / list.size())*maxBlocFreq;
    }

    private void initBloclist(){
        this.bloclist = new HashMap<String, Integer>();

        for(Person p : this.a){
            if(!bloclist.containsKey(p.getBloc())){
                this.bloclist.put(p.getBloc(), 0);
            }
        }
    }

    private void clearBloc(){
        for(String k : this.bloclist.keySet()){
            this.bloclist.put(k, 0);
        }
    }

    public static double getThreshold(){
        File file = new File(CONFIG_FILE);
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return Double.parseDouble(reader.readLine());
        }
        catch (Exception e) {
            try{
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(file));

                writer.write(Double.toString(THRESHOLD_DEFAULT));
                writer.close();

                return THRESHOLD_DEFAULT;
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.out.println("Cannot write \"" + CONFIG_FILE + "\"");
                System.exit(1);

                return 0.0;
            }
        }
    }

    public ArrayList<ArrayList<Person>> getBestGroup(){
        return this.bestGroup;
    }
}
