package jdbc.lesson4.homework.controller;

import jdbc.lesson4.homework.exceptions.BadRequestException;
import jdbc.lesson4.homework.exceptions.InternalServerException;
import jdbc.lesson4.homework.model.Storage;
import jdbc.lesson4.homework.service.StorageService;

public class StorageController {

    private static final StorageService storageService = new StorageService();

    public String save(Storage storage) {
        try {
            storageService.save(storage);

            return "save success";
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "save failed: something went wrong";
        }
    }

    public String findById(long id) {
        try {
            storageService.findById(id);

            return "findById success";
        } catch (BadRequestException e) {

            return "findById failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "findById failed: something went wrong";
        }
    }

    public String update(Storage storage) {
        try {
            storageService.update(storage);

            return "update success";
        } catch (BadRequestException e) {

            return "update failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "update failed: something went wrong";
        }
    }

    public String delete(long id) {
        try {
            storageService.delete(id);

            return "delete success";
        } catch (BadRequestException e) {

            return "delete failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "delete failed: something went wrong";
        }
    }
}