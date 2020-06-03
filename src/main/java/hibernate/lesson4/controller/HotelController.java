package hibernate.lesson4.controller;

import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.model.Hotel;
import hibernate.lesson4.service.HotelService;

import java.util.List;

public class HotelController {
    private static final HotelService hotelService = new HotelService();

    public List<Hotel> findHotelByName(String name) throws BadRequestException, InternalServerException {
        return hotelService.findHotelByName(name);
    }

    public List<Hotel> findHotelByCity(String city) throws BadRequestException, InternalServerException {
        return hotelService.findHotelByCity(city);
    }

    public Hotel addHotel(Hotel hotel) throws InternalServerException, NoAccessException, BadRequestException {
        return hotelService.addHotel(hotel);
    }

    public void deleteHotel(long hotelId) throws InternalServerException, NoAccessException, BadRequestException {
        hotelService.deleteHotel(hotelId);
    }
}