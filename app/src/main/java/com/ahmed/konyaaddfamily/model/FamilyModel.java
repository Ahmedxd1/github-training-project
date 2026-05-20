package com.ahmed.konyaaddfamily.model;

public class FamilyModel {
    private int id;
    private String fatherName;
    private String familyType;

    public FamilyModel(int id, String fatherName, String familyType) {
        this.id = id;
        this.fatherName = fatherName;
        this.familyType = familyType;
    }

    public int getId() {
        return id;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getFamilyType() {
        return familyType;
    }
}
