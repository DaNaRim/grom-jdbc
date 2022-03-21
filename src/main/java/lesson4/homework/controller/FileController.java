package lesson4.homework.controller;

import lesson4.homework.exceptions.BadRequestException;
import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.exceptions.NotFoundException;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;
import lesson4.homework.service.FileService;

import java.util.List;

public class FileController {

    private static final FileService fileService = new FileService();

    public File save(File file) throws BadRequestException, InternalServerException {
        return fileService.save(file);
    }

    public File findById(long id) throws InternalServerException, NotFoundException {
        return fileService.findById(id);
    }

    public File update(File file) throws BadRequestException, InternalServerException {
        return fileService.update(file);
    }

    public void delete(long id) throws BadRequestException, InternalServerException {
        fileService.delete(id);
    }

    public File put(Storage storage, File file) throws BadRequestException, InternalServerException {
        return fileService.put(storage, file);
    }

    public void putAll(Storage storage, List<File> files) throws BadRequestException, InternalServerException {
        fileService.putAll(storage, files);
    }

    public File deleteFromStorage(Storage storage, File file) throws BadRequestException, InternalServerException {
        return fileService.deleteFromStorage(storage.getId(), file);
    }

    public void transferAll(Storage storageFrom, Storage storageTo)
            throws BadRequestException, InternalServerException {
        fileService.transferAll(storageFrom, storageTo);
    }

    public void transferFile(Storage storageFrom, Storage storageTo, long id)
            throws BadRequestException, InternalServerException {
        fileService.transferFile(storageFrom.getId(), storageTo, id);
    }
}
