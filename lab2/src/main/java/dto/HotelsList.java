package dto;

import java.util.Collection;

public class HotelsList {
    private final Collection<hotels> hotelList;

    public HotelsList(Collection<hotels> hotelList){
        this.hotelList = hotelList;
    }
    public Collection<hotels> getHotels(){
        return hotelList;
    }
}
