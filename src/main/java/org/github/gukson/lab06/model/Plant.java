package org.github.gukson.lab06.model;

public class Plant {
    private Integer age;
    private String name;

    public Plant(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
    public void grow(){
        if(age < 10){
            age +=1;
        }
    }
}
