package utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import actors.Person;

public class GrouperUtil {
    public static final double THRESHOLD = getThreshold();
    public static final String CONFIG_FILE = "config.cfg";
    private static final double THRESHOLD_DEFAULT = 0.1;
    
    private Person[] a;
    private int grouping;
    private float minDiff;
    private ArrayList<ArrayList<Person>> bestGroup;

    public GrouperUtil(Person[] arr, int n) {
        this.a = arr;
        this.grouping = n;
        minDiff = 999999999;
        bestGroup = new ArrayList<ArrayList<Person>>();
    }

    public ArrayList<ArrayList<Person>> automatedGrouping() {
        float allAveGWA = this.aveAllGWA(this.a);
        PermUtil<Person> perm = new PermUtil<Person>(this.a);

        System.out.println("Grouping each people...\n");

        Person[] combi = null;
        while((combi = perm.next()) != null) {
            ArrayList<ArrayList<Person>> groups = new ArrayList<ArrayList<Person>>();
            ArrayList<Float> groupGWA = new ArrayList<Float>();

            intoGroups(groups, combi, this.grouping);
            aveGWAPerGroup(groupGWA, groups);

            float groupDiff = this.getAbsDiff(groupGWA, allAveGWA);

            if (groupDiff < minDiff){
                minDiff = groupDiff;
                bestGroup = groups;
                new Thread(){
                    @Override
                    public void run(){
                        System.out.println(printGroup() + minDiff + "\n");
                        FileUtil.printToFile(bestGroup);
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
            groupGWA.add(Float.valueOf(this.aveAllGWA(g)));
        }
    }

    private float getAbsDiff(ArrayList<Float> groupGWA, float aveGWA) {
        float sumOfDiff = 0.0f;

        for (Float f : groupGWA) {
            sumOfDiff += Math.abs(aveGWA - f.floatValue());
        }

        return sumOfDiff;
    }

    private void intoGroups(ArrayList<ArrayList<Person>> groups, Person[] list, int grouping) {
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
        for (Person p : list) {
            sumGWA += p.getGWA();
        }
        return sumGWA / list.size();
    }

    public static double getThreshold(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(CONFIG_FILE)));
            return Double.parseDouble(reader.readLine());
        }
        catch (Exception e) {
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(CONFIG_FILE)));

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
