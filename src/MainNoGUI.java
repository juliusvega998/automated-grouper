package main;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;

import actors.Person;
import utilities.FileUtil;
import utilities.GrouperUtil;

public class MainNoGUI {
    public static void main(String[] arrstring) {
        Person[] arr = null;
        Scanner sc = new Scanner(System.in);

        System.out.println("THRESHOLD set to " + GrouperUtil.THRESHOLD);

        arr = FileUtil.addToArray(new File("input.in"));

        System.out.println("How many groups? (Max of " + arr.length + ") ");
        int n = sc.nextInt();

        double d = System.currentTimeMillis();

        GrouperUtil p = new GrouperUtil(arr, n);
        ArrayList<ArrayList<Person>> group = p.automatedGrouping();

        System.out.println("\n" + Person.groupToString(group));

        System.out.println("\nTime taken: " + ((double)System.currentTimeMillis() - d)/1000.0 + " seconds");
        System.out.println();

        FileUtil.printToFile(group, "groupings.out");
    }
}
