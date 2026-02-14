package com.example.model;

import java.util.Set;
import java.util.TreeMap;

public class Character {

    private int id;

    private String name;

    private String status;

    private String species;

    private String type;

    TreeMap<String, Set<String>> OriginSpecies;

    private String gender;

    private String origin;

    private String location;

    private String created;

    public Character(int id, String name, String status, String species, String type, String gender, String origin, String location, String created){
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.type = type;
        this.gender = gender;
        this.origin = origin;
        this.location = location;
        this.created = created;
    }

    public int getID(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getStatus(){
        return status;
    }
    public  String getSpecies(){
        return species;
    }
    public String getType(){
        return type;
    }

    public String getGender() {
        return gender;
    }
    public String getOrigin(){
        return origin;
    }

    public String getLocation() {
        return location;
    }

    public String getCreated() {
        return created;
    }
    public String toString() {
        return "Character{" +
                "id=" + getID() +
                ", name='" + getName() + '\'' +
                ", Status='" + getStatus() + '\'' +
                ", Species='" + getSpecies() + '\'' +
                ", Type='" + getType() + '\'' +
                ", location='" + getLocation() + '\'' +
                ", Gender='" + getGender() + '\'' +
                ", Origin='" + getOrigin() + '\'' +
                ", Created='" + getCreated() + '\'' +
                '}';
    }

}

