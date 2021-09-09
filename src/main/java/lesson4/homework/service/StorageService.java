package lesson4.homework.service;

import lesson4.homework.DAO.FileDAO;
import lesson4.homework.DAO.StorageDAO;
import lesson4.homework.exceptions.BadRequestException;
import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.model.Storage;

public class StorageService {

    private static final StorageDAO storageDAO = new StorageDAO();
    private static final FileDAO fileDAO = new FileDAO();

    public Storage save(Storage storage) throws InternalServerException {
        return storageDAO.save(storage);
    }

    public Storage findById(long id) throws BadRequestException, InternalServerException {
        return storageDAO.findById(id);
    }

    public Storage update(Storage storage) throws BadRequestException, InternalServerException {
        try {
            validateUpdate(storage);

            return storageDAO.update(storage);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot update storage " + storage.getId() + " : " + e.getMessage());
        }
    }

    public void delete(long id) throws BadRequestException, InternalServerException {
        try {
            findById(id);

            storageDAO.delete(id);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot delete storage " + id + " : " + e.getMessage());
        }
    }

    private void validateUpdate(Storage storage) throws InternalServerException, BadRequestException {

        findById(storage.getId());

        long filesSize = fileDAO.getFilesSizeByStorageId(storage.getId());

        if (filesSize > storage.getStorageSize()) {
            throw new BadRequestException("not enough space for the files in this repository");
        }

        fileDAO.checkFormat(storage.getFormatsSupported());
    }
}
