package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;

import actors.Person;

public abstract class FileUtil{
	public static void printToFile(ArrayList<ArrayList<Person>> group, String outFile){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));

            writer.write(Person.groupToString(group));
            writer.close();
        }catch(Exception ex){
            System.out.println("Cannot write to file \"groupings.out\"");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static Person[] addToArray(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
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
        catch(IndexOutOfBoundsException i){
            i.printStackTrace();
            System.out.println("Wrong format on file " + file.getFileName() + ".");
            JOptionPane.showMessageDialog(null, "Wrong format on file " + file.getFileName() + ".");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\nFile \""+file.getFileName()+"\" not found!");
            System.exit(1);
        }

        return null;
    }
}