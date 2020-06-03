package hibernate.lesson4.model;

import java.util.Date;

public class Filter {
    private final Integer numberOfGuests;
    private final Double price;
    private final Boolean breakfastIncluded;
    private final Boolean petsAllowed;
    private final Date dateAvailableFrom;
    private final String country;
    private final String city;

    public Filter(Integer numberOfGuests, Double price, Boolean breakfastIncluded, Boolean petsAllowed,
                  Date dateAvailableFrom, String country, String city) {
        this.numberOfGuests = numberOfGuests;
        this.price = price;
        this.breakfastIncluded = breakfastIncluded;
        this.petsAllowed = petsAllowed;
        this.dateAvailableFrom = dateAvailableFrom;
        this.country = country;
        this.city = city;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getBreakfastIncluded() {
        return breakfastIncluded;
    }

    public Boolean getPetsAllowed() {
        return petsAllowed;
    }

    public Date getDateAvailableFrom() {
        return dateAvailableFrom;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}