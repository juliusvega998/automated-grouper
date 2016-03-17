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
}
