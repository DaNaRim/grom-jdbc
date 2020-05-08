package lesson4.homework.controller;

import lesson4.homework.Exceptions.BadRequestException;
import lesson4.homework.Exceptions.InternalServerException;
import lesson4.homework.model.Storage;
import lesson4.homework.service.StorageService;

public class StorageController {

    public static Storage save(Storage storage) throws InternalServerException {
        return StorageService.save(storage);
    }

    public static void delete(long id) throws BadRequestException, InternalServerException {
        StorageService.delete(id);
    }

    public static Storage update(Storage storage) throws InternalServerException, BadRequestException {
        return StorageService.update(storage);
    }

    public static Storage findById(long id) throws BadRequestException, InternalServerException {
        return StorageService.findById(id);
    }
}