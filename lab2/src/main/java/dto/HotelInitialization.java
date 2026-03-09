package dto;

public class HotelInitialization {
    private String name;
    private int stars;

    public HotelInitialization(){}

    public HotelInitialization(String name, int stars){
        this.name = name;
        this.stars = stars;
    }

    public String getName(){
        return name;
    }

    public int getStars(){
        return stars;
    }
}
