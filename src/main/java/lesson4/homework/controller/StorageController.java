package lesson4.homework.controller;

import lesson4.homework.exceptions.BadRequestException;
import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.model.Storage;
import lesson4.homework.service.StorageService;

public class StorageController {

    private static final StorageService storageService = new StorageService();

    public Storage save(Storage storage) throws InternalServerException {
        return storageService.save(storage);
    }

    public Storage findById(long id) throws BadRequestException, InternalServerException {
        return storageService.findById(id);
    }

    public Storage update(Storage storage) throws BadRequestException, InternalServerException {
        return storageService.update(storage);
    }

    public void delete(long id) throws BadRequestException, InternalServerException {
        storageService.delete(id);
    }
}