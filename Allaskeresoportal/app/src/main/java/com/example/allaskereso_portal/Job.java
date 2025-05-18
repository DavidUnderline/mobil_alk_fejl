package com.example.allaskereso_portal;

public class Job {
    private String name;
    private String company_id;
    private String title;
    private String description;
    private String salary;
    private String id;
    private String category;

    public Job() {
    }

    public Job(String category, String name, String company_id, String title, String description, String salary, String id){
        this.name = name;
        this.company_id = company_id;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.id = id;
        this.category = category;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String value){
        this.category = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
