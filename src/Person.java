package actors;

import java.util.ArrayList;

public class Person {
    private String name;
    private float gwa;
    private String bloc;

    public Person(String n, float f, String b) {
        this.name = n;
        this.gwa = f;
        this.bloc = b;
    }

    public String getName() {
        return this.name;
    }

    public float getGWA() {
        return this.gwa;
    }

    public String getBloc(){
        return this.bloc;
    }

    @Override
    public String toString() {
        return this.name + "," + this.bloc;
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
