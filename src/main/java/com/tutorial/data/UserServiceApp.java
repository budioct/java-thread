package com.tutorial.data;

public class UserServiceApp {

    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public void setUser(String user){
        threadLocal.set(user); // void set(T value) // Menyetel salinan thread saat ini dari variabel lokal thread ini ke nilai yang ditentukan.
    }

    public void doAction(){
        String user = threadLocal.get(); // T get() // Mengembalikan nilai dalam salinan thread saat ini dari variabel lokal thread ini.
        System.out.println(user + " do action");
    }

}
