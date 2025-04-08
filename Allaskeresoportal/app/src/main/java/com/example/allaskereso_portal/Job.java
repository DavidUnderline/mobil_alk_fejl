package com.example.allaskereso_portal;

public class Job {
    private String title;
    private String category;
    private String salary;
    private String description;

    public Job(String title, String category, String salary, String description){
        this.title = title;
        this.category = category;
        this.salary = salary;
        this.description = description;
    }

    public String getTitle(){ return this.title; }
    public String getCategory(){ return this.category; }
    public String getSalary(){ return this.salary; }
    public String getDescription(){ return this.description; }
}
