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
    public static final String DEF_OUT_FILE = "groupings.out";
    public static final double THRESHOLD = getThreshold();
    public static final double THRESHOLD_DEFAULT = 0.1;
    
    private Person[] a;
    private int grouping;
    private float minDiff;
    private String outFile;
    private ArrayList<ArrayList<Person>> bestGroup;
    private HashMap<String, Integer> bloclist;

    public GrouperUtil(Person[] arr, int n) {
        this.a = arr;
        this.grouping = n;
        this.minDiff = 999999999;
        this.outFile = DEF_OUT_FILE;
        this.bestGroup = new ArrayList<ArrayList<Person>>();
        this.initBloclist();
    }

    public GrouperUtil(Person[] arr, int n, String outFile) {
        this.a = arr;
        this.grouping = n;
        this.minDiff = 999999999;
        this.outFile = outFile;
        this.bestGroup = new ArrayList<ArrayList<Person>>();
        this.initBloclist();
    }

    public ArrayList<ArrayList<Person>> automatedGrouping() {
        float allAveGWA = this.aveAllGWA(this.a);
        PermUtil<Person> perm = new PermUtil<Person>(this.a);

        System.out.println("Grouping each people...\n");

        Person[] combi = null;
        while((combi = perm.next()) != null) {
            ArrayList<ArrayList<Person>> groups = new ArrayList<ArrayList<Person>>();
            ArrayList<Float> groupGWA = new ArrayList<Float>();

            clearBloc();

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

    private void aveGWAPerGroup(ArrayList<Float> groupGWA, ArrayList<ArrayList<Person>> groups) {
        for (ArrayList<Person> g : groups) {
            groupGWA.add(this.aveAllGWA(g));
        }
    }

    private void printNewGrouping(){
        new Thread(){
            @Override
            public void run(){
                System.out.println(printGroup() + minDiff + "\n");
                FileUtil.printToFile(bestGroup, outFile);
            }

            private String printGroup(){
                String s = "";
                for(ArrayList<Person> group : bestGroup){
                    for(Person p : group){
                        s += p.getName() + " ";
                    }
                    s += "\n";
                }

                return s;
            }
        }.start();
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
        int maxBlocFreq = 0;

        for (Person p : list) {
            sumGWA += p.getGWA();

            this.bloclist.put(p.getBloc(), this.bloclist.get(p.getBloc())+1);
            if(maxBlocFreq < this.bloclist.get(p.getBloc())){
                maxBlocFreq = this.bloclist.get(p.getBloc());
            }
        }
        return (sumGWA / list.length)*maxBlocFreq;
    }

    private float aveAllGWA(ArrayList<Person> list) {
        float sumGWA = 0.0f;
        for (Person p : list) {
            sumGWA += p.getGWA();
        }
        return sumGWA / list.size();
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
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

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
}
