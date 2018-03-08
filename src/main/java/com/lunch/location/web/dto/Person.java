package com.lunch.location.web.dto;

public class Person {

    public String id;

    public String firstname;
    public String lastname;
    public int age;

    public Person() {}

    public Person(String firstName, String lastName, int age) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.age = age;
    }

    @Override
	public String toString() {
		return "Person [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", age=" + age + "]";
	}

}