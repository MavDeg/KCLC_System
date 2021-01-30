package kclc.model;

public class Teacher_Singleton {

    private static Teacher teacher;
    private static Teacher_Singleton teacher_instance = null;


    public Teacher_Singleton() {
    }

    public static Teacher_Singleton getInstance(){
        if(teacher_instance == null){
            teacher_instance = new Teacher_Singleton();
        }
        return teacher_instance;
    }

    public static Teacher getTeacher() {
        return teacher;
    }

    public static void setTeacher(Teacher teacher) {
        Teacher_Singleton.teacher = teacher;
    }
}

