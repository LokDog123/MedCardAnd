package com.example.medcard;

public class Record {
    private int id;
    private String title;
    private String category;
    private String date;
    private String description;
    private String doctor;
    private String drug;
    private String dosage;
    private String reaction;
    public Record(int id, String title, String category, String date, String description,
                  String doctor, String drug, String dosage, String reaction) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.date = date;
        this.description = description;
        this.doctor = doctor;
        this.drug = drug;
        this.dosage = dosage;
        this.reaction = reaction;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getDoctor() { return doctor; }
    public String getDrug() { return drug; }
    public String getDosage() { return dosage; }
    public String getReaction() { return reaction; }

    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setDoctor(String doctor) { this.doctor = doctor; }
    public void setDrug(String drug) { this.drug = drug; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setReaction(String reaction) { this.reaction = reaction; }
}
