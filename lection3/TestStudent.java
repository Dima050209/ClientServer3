package lection3;

import java.io.Serializable;

public class TestStudent implements Serializable {

    public String name;
    public int course;

    TestStudent(String name, int course) {
        this.name = name;
        this.course = course;
    }

    public String toString() {
        return "Студент " + name + " " + course + " курсу";
    }
}
