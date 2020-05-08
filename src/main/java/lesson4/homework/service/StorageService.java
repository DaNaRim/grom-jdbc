package lesson4.homework.service;

import lesson4.homework.DAO.StorageDAO;
import lesson4.homework.Exceptions.BadRequestException;
import lesson4.homework.Exceptions.InternalServerException;
import lesson4.homework.model.Storage;

public class StorageService {

    public static Storage save(Storage storage) throws InternalServerException {
        return StorageDAO.save(storage);
    }

    public static void delete(long id) throws BadRequestException, InternalServerException {
        findById(id);
        StorageDAO.delete(id);
    }

    public static Storage update(Storage storage) throws InternalServerException, BadRequestException {
        findById(storage.getId());
        return StorageDAO.update(storage);
    }

    public static Storage findById(long id) throws BadRequestException, InternalServerException {
        return StorageDAO.findById(id);
    }
}