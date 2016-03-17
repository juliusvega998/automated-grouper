package actors;

import java.util.ArrayList;

public class Person {
    private String name;
    private float gwa;

    public Person(String n, float f) {
        this.name = n;
        this.gwa = f;
    }

    public String getName() {
        return this.name;
    }

    public float getGWA() {
        return this.gwa;
    }

    @Override
    public String toString() {
        return this.name + " " + this.gwa;
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
}
