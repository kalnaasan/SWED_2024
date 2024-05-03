package edu.fra.uas;

import java.util.Objects;

public class Developer {
    // Attributes
    private String name;
    private int age;
    private String programmingLanguage;
    private boolean isEmployed;
    private int yearsOfExperience;

    public Developer() {
    }

    public Developer(String name, int age, String programmingLanguage, boolean isEmployed, int yearsOfExperience) {
        this.name = name;
        this.age = age;
        this.programmingLanguage = programmingLanguage;
        this.isEmployed = isEmployed;
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new RuntimeException("name must not be null or empty");
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age <= 0) throw new RuntimeException("Age must be greater than zero");
        this.age = age;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        if (programmingLanguage == null || programmingLanguage.isEmpty())
            throw new RuntimeException(", programmingLanguage must not be null or empty");
        this.programmingLanguage = programmingLanguage;
    }

    public boolean isEmployed() {
        return isEmployed;
    }

    public void setEmployed(boolean employed) {
        isEmployed = employed;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        if (yearsOfExperience < 0) throw new RuntimeException("yearsOfExperience must be positive");
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * toString method to represent object state
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Developer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", programmingLanguage='" + programmingLanguage + '\'' +
                ", isEmployed=" + isEmployed +
                ", yearsOfExperience=" + yearsOfExperience +
                '}';
    }

    /**
     * equals and hashCode methods for object comparison
     *
     * @param o object to compare
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Developer developer = (Developer) o;
        return age == developer.age &&
                isEmployed == developer.isEmployed &&
                yearsOfExperience == developer.yearsOfExperience &&
                Objects.equals(name, developer.name) &&
                Objects.equals(programmingLanguage, developer.programmingLanguage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, programmingLanguage, isEmployed, yearsOfExperience);
    }

    public void code() {
        System.out.println(name + " is coding in " + programmingLanguage);
    }

    public void debug() {
        System.out.println(name + " is debugging");
    }

    public void deploy() {
        System.out.println(name + " is deploying the application");
    }

    public void learnNewLanguage(String newLanguage) {
        System.out.println(name + " is learning " + newLanguage);
    }

    public void changeJob(boolean newEmploymentStatus) {
        if (newEmploymentStatus != isEmployed) {
            isEmployed = newEmploymentStatus;
            if (isEmployed) {
                System.out.println(name + " has been hired by a new company.");
            } else {
                System.out.println(name + " has left their job.");
            }
        }
    }

    public static void main(String[] args) {
        Developer developer = new Developer("Test", 19, "JAVA", false, 7);
        developer.code();
        developer.debug();
        developer.deploy();
        developer.learnNewLanguage("C#");
        developer.changeJob(true);
    }
}
