package kclc.model;

public class Student_Singleton {

    private static Student student;
    private static Student_Singleton student_instance = null;


    public Student_Singleton() {
    }

    public static Student_Singleton getInstance(){
        if(student_instance == null){
            student_instance = new Student_Singleton();
        }
        return student_instance;
    }

    public static Student getStudent() {
        return student;
    }

    public static void setStudent(Student student) {
        Student_Singleton.student = student;
    }
}

