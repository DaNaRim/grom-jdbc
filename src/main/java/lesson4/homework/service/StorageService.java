package lesson4.homework.service;

import lesson4.homework.DAO.FileDAO;
import lesson4.homework.DAO.StorageDAO;
import lesson4.homework.exceptions.BadRequestException;
import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.exceptions.NotFoundException;
import lesson4.homework.model.Storage;

import java.util.Arrays;

public class StorageService {

    private static final StorageDAO storageDAO = new StorageDAO();
    private static final FileDAO fileDAO = new FileDAO();

    public Storage save(Storage storage) throws InternalServerException, BadRequestException {
        validateStorage(storage);
        return storageDAO.save(storage);
    }

    public Storage findById(long id) throws InternalServerException, NotFoundException {
        return storageDAO.findById(id);
    }

    public Storage update(Storage storage) throws BadRequestException, InternalServerException {
        try {
            validateStorage(storage);
            validateUpdate(storage);
            return storageDAO.update(storage);
        } catch (BadRequestException | NotFoundException e) {
            throw new BadRequestException("Cannot update storage " + storage.getId() + " : " + e.getMessage());
        }
    }

    public void delete(long id) throws BadRequestException, InternalServerException {
        if (!storageDAO.isExists(id)) {
            throw new BadRequestException("Cannot delete storage " + id + " : missing storage " + id);
        }
        storageDAO.delete(id);
    }

    private void validateStorage(Storage storage) throws BadRequestException {
        if (storage == null) {
            throw new BadRequestException("Storage can`t be null");
        }
        if (storage.getFormatsSupported() == null
                || storage.getStorageCountry() == null
                || storage.getStorageSize() == null
                || storage.getFormatsSupported().length == 0
                || storage.getStorageCountry().isBlank()
                || storage.getStorageSize() <= 0) {
            throw new BadRequestException("Fields filed incorrect");
        }
    }

    private void validateUpdate(Storage storage)
            throws InternalServerException, BadRequestException, NotFoundException {

        if (!storageDAO.isExists(storage.getId())) {
            throw new NotFoundException("Storage not found");
        }

        long filesSize = fileDAO.getFilesSizeByStorageId(storage.getId());

        if (filesSize > storage.getStorageSize()) {
            throw new BadRequestException("Not enough space for the files in this repository");
        }

        String[] filesFormats = fileDAO.getFilesFormatsByStorage(storage.getId());

        for (String format : filesFormats) {
            if (Arrays.asList(storage.getFormatsSupported()).contains(format)) continue;
            throw new BadRequestException("Files from this storage have a format that is no longer available");
        }
    }
}
