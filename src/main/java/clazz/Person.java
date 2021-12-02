package clazz;

public class Person {
    private String name = "Alfira";
    public void getName() {
        System.out.println(name);
    }
    public void setName(String name, int a) {
        this.name = name + a;
    }
}