import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] arrstring) {
        Person[] arr = null;
        Scanner sc = new Scanner(System.in);

        System.out.println("THRESHOLD set to " + Grouper.THRESHOLD);

        arr = addToArray();

        System.out.println("How many groups? (Max of " + arr.length + ") ");
        int n = sc.nextInt();

        double d = System.currentTimeMillis();

        Grouper p = new Grouper(arr, n);
        ArrayList<ArrayList<Person>> group = p.automatedGrouping();

        System.out.println("\n" + groupToString(group));

        System.out.println("\nTime taken: " + ((double)System.currentTimeMillis() - d)/1000.0 + " seconds");
        System.out.println();

        printToFile(group);
    }

    public static void printToFile(ArrayList<ArrayList<Person>> group){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("groupings.out")));

            writer.write(groupToString(group));
            writer.close();
        }catch(Exception ex){
            System.out.println("Cannot write to file \"groupings.out\"");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static String groupToString(ArrayList<ArrayList<Person>> group){
        int i = 1;
        String groupString = "";
        for (ArrayList<Person> pList : group) {
            groupString += "Group " + i + ":\n";
            for (Person person : pList) {
                groupString += person.toString() + "\n";
            }
            groupString += "\n";
            i++;
        }

        return groupString;
    }

    public static Person[] addToArray() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("input.in")));
            System.out.println("Reading from file \"input.in\"");

            String string = null;
            int i=0;

            int size = Integer.parseInt(reader.readLine());
            Person[] arr = new Person[size];

            while ((string = reader.readLine()) != null) {
                String[] arrstring = string.split(" ");
                float f = Float.parseFloat(arrstring[1]);

                arr[i] = new Person(arrstring[0], f);
                i++;
            }

            return arr;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\nFile \"input.in\" not found!");
            System.exit(1);
        }

        return null;
    }
}
