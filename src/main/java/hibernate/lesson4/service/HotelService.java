package hibernate.lesson4.service;

import hibernate.lesson4.DAO.HotelDAO;
import hibernate.lesson4.exception.BadRequestException;
import hibernate.lesson4.exception.InternalServerException;
import hibernate.lesson4.exception.NoAccessException;
import hibernate.lesson4.model.Hotel;

import java.util.List;

public class HotelService {
    private static final HotelDAO hotelDAO = new HotelDAO(Hotel.class);
    private static final UserService userService = new UserService();

    public List<Hotel> findHotelByName(String name) throws BadRequestException, InternalServerException {
        validateName(name);
        return hotelDAO.findHotelByName(name);
    }

    public List<Hotel> findHotelByCity(String city) throws BadRequestException, InternalServerException {
        validateCity(city);
        return hotelDAO.findHotelByCity(city);
    }

    public Hotel addHotel(Hotel hotel) throws NoAccessException, BadRequestException, InternalServerException {
        userService.checkAccess();
        validateHotel(hotel);

        return hotelDAO.save(hotel);
    }

    public void deleteHotel(long hotelId) throws NoAccessException, BadRequestException, InternalServerException {
        userService.checkAccess();
        Hotel hotel = hotelDAO.findById(hotelId);

        hotelDAO.delete(hotel);
    }

    private void validateName(String name) throws BadRequestException {
        if (name == null || name.equals("") || !name.equals(name.trim())) {
            throw new BadRequestException("validateName failed: the field is not filled correctly");
        }
    }

    private void validateCity(String city) throws BadRequestException {
        if (city == null || city.equals("") || !city.equals(city.trim())) {
            throw new BadRequestException("validateCity failed: the field is not filled correctly");
        }
    }

    private void validateHotel(Hotel hotel) throws BadRequestException, InternalServerException {
        if (hotel == null) {
            throw new BadRequestException("validateHotel failed: impossible to process null hotel");
        }
        if (hotel.getName() == null || hotel.getCity() == null ||
                hotel.getCountry() == null || hotel.getStreet() == null ||
                hotel.getName().equals("") || hotel.getCity().equals("") ||
                hotel.getCountry().equals("") || hotel.getStreet().equals("")) {
            throw new BadRequestException("validateHotel failed: not all fields are filled");
        }
        if (!hotel.getName().equals(hotel.getName().trim()) ||
                !hotel.getCountry().equals(hotel.getCountry().trim()) ||
                !hotel.getCity().equals(hotel.getCity().trim()) ||
                !hotel.getStreet().equals(hotel.getStreet().trim())) {
            throw new BadRequestException("validateHotel failed: fields must not begin and end with spaces");
        }
        if (hotel.getName().contains(", ") || hotel.getCountry().contains(", ") ||
                hotel.getCity().contains(", ") || hotel.getStreet().contains(", ")) {
            throw new BadRequestException("validateHotel failed: fields must not have ', '");
        }
        hotelDAO.isHotelExist(hotel);
    }
}