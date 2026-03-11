package com.example.dto;

import java.util.UUID;

public class hotels {
    private final UUID id;
    private final String name;
    private final int stars;


    public hotels(UUID id, String name, int stars){
        this.id = id;
        this.name = name;
        this.stars = stars;
    }

    public UUID getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getStars(){
        return stars;
    }

}
