package lesson4.homework.service;

import lesson4.homework.DAO.FileDAO;
import lesson4.homework.DAO.StorageDAO;
import lesson4.homework.Exceptions.BadRequestException;
import lesson4.homework.Exceptions.InternalServerException;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

public class FileService {

    public static File put(Storage storage, File file) throws BadRequestException, InternalServerException {
        try {
            checkFileFormat(storage, file);
            checkSize(storage, file);
            FileDAO.checkFileName(storage, file);

            return FileDAO.save(storage, file);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot put file " + file.getId() + " in storage " + storage.getId() + ": " +
                    e);
        }
    }

    public static void delete(Storage storage, File file) throws BadRequestException, InternalServerException {
        try {
            FileDAO.findById(file.getId());

            FileDAO.delete(storage, file);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot delete file " + file.getId() + " from storage " + storage.getId() +
                    " : " + e);
        }
    }

    public static void transferAll(Storage storageFrom, Storage storageTo)
            throws BadRequestException, InternalServerException {
        try {
            checkFilesFormat(storageFrom, storageTo);
            checkSize(storageFrom, storageTo);
            checkFiles(storageFrom, storageTo);

            FileDAO.transferAll(storageFrom, storageTo);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot transfer files from storage " + storageFrom.getId() + " to storage " +
                    storageTo.getId() + " : " + e.getMessage());
        }
    }

    public static void transferFile(Storage storageFrom, Storage storageTo, long id)
            throws BadRequestException, InternalServerException {
        try {
            File file = FileDAO.findById(id);

            checkFileFormat(storageTo, file);
            checkSize(storageTo, file);
            FileDAO.checkFileName(storageTo, file);

            FileDAO.transferFile(storageFrom, storageTo, id);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot transfer file " + id + " from storage " + storageFrom.getId() +
                    " to storage " + storageTo.getId() + " : " + e);
        }
    }

    public static File update(File file) throws InternalServerException, BadRequestException {
        try {
            findById(file.getId());

            Storage storage = StorageDAO.findById(file.getStorage().getId());
            checkFileFormat(storage, file);
            checkSize(storage, file);
            FileDAO.checkFileName(storage, file);

            return FileDAO.update(file);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot update file " + file.getId() + " : " + e);
        }
    }

    public static File findById(long id) throws BadRequestException, InternalServerException {
        return FileDAO.findById(id);
    }

    private static void checkFileFormat(Storage storage, File file) throws BadRequestException {
        for (String str : storage.getFormatsSupported()) {
            if (file.getFormat().equals(str)) return;
        }
        throw new BadRequestException("Unsuitable format");
    }

    private static void checkFilesFormat(Storage storageFrom, Storage storageTo) throws BadRequestException {
        for (File file : storageFrom.getFiles()) {
            if (file != null) checkFileFormat(storageTo, file);
        }
    }

    private static void checkSize(Storage storage, File file) throws BadRequestException {
        if (storage.getFreeSpace() < file.getSize()) throw new BadRequestException("No storage space");
    }

    private static void checkSize(Storage storageFrom, Storage storageTo) throws BadRequestException {
        long filesSize = storageFrom.getStorageSize() - storageFrom.getFreeSpace();
        if (filesSize > storageTo.getFreeSpace()) {
            throw new BadRequestException("No storage space");
        }
    }

    private static void checkFiles(Storage storageFrom, Storage storageTo)
            throws BadRequestException, InternalServerException {
        for (File file : storageFrom.getFiles()) {
            FileDAO.checkFileName(storageTo, file);
        }
    }
}