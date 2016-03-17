package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;

import actors.Person;

public abstract class FileUtil{
	public static void printToFile(ArrayList<ArrayList<Person>> group){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("groupings.out")));

            writer.write(Person.groupToString(group));
            writer.close();
        }catch(Exception ex){
            System.out.println("Cannot write to file \"groupings.out\"");
            ex.printStackTrace();
            System.exit(1);
        }
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
                String[] arrstring = string.split(",");
                float f = Float.parseFloat(arrstring[1]);

                arr[i] = new Person(arrstring[0], f, arrstring[2]);
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