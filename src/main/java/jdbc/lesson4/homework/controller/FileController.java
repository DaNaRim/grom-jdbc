package jdbc.lesson4.homework.controller;

import jdbc.lesson4.homework.exceptions.BadRequestException;
import jdbc.lesson4.homework.exceptions.InternalServerException;
import jdbc.lesson4.homework.model.File;
import jdbc.lesson4.homework.model.Storage;
import jdbc.lesson4.homework.service.FileService;

import java.util.List;

public class FileController {

    private static final FileService fileService = new FileService();

    public String save(File file) {
        try {
            fileService.save(file);

            return "save success";
        } catch (BadRequestException e) {

            return "save failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "save failed: something went wrong";
        }
    }

    public String findById(long id) {
        try {
            fileService.findById(id);

            return "findById success";
        } catch (BadRequestException e) {

            return "findById failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "findById failed: something went wrong";
        }
    }

    public String update(File file) {
        try {
            fileService.update(file);

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
            fileService.delete(id);

            return "delete success";
        } catch (BadRequestException e) {

            return "delete failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "delete failed: something went wrong";
        }
    }

    public String put(Storage storage, File file) {
        try {
            fileService.put(storage, file);

            return "put success";
        } catch (BadRequestException e) {

            return "put failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "put failed: something went wrong";
        }
    }

    public String putAll(Storage storage, List<File> files) {
        try {
            fileService.putAll(storage, files);

            return "putAll success";
        } catch (BadRequestException e) {

            return "putAll failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "putAll failed: something went wrong";
        }
    }

    public String deleteFromStorage(Storage storage, File file) {
        try {
            fileService.deleteFromStorage(storage.getId(), file);

            return "deleteFromStorage success";
        } catch (BadRequestException e) {

            return "deleteFromStorage failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "deleteFromStorage failed: something went wrong";
        }
    }

    public String transferAll(Storage storageFrom, Storage storageTo) {
        try {
            fileService.transferAll(storageFrom, storageTo);

            return "transferAll success";
        } catch (BadRequestException e) {

            return "transferAll failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "transferAll failed: something went wrong";
        }
    }

    public String transferFile(Storage storageFrom, Storage storageTo, long id) {
        try {
            fileService.transferFile(storageFrom.getId(), storageTo, fileService.findById(id));

            return "transferFile success";
        } catch (BadRequestException e) {

            return "transferFile failed: " + e.getMessage();
        } catch (InternalServerException e) {

            System.err.println(e.getMessage());
            return "transferFile failed: something went wrong";
        }
    }
}