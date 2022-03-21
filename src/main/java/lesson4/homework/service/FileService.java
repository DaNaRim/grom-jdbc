package lesson4.homework.service;

import lesson4.homework.DAO.FileDAO;
import lesson4.homework.exceptions.BadRequestException;
import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.exceptions.NotFoundException;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileService {

    private static final FileDAO fileDAO = new FileDAO();

    public File save(File file) throws InternalServerException, BadRequestException {
        try {
            validateFile(file);
            if (!fileDAO.isNameUnique(file.getName())) {
                throw new BadRequestException("File with name " + file.getName() + " is already exist");
            }
            return fileDAO.save(file);
        } catch (BadRequestException e) {
            throw new BadRequestException("save file with name " + file.getName() + " failed: " + e.getMessage());
        }
    }

    public File findById(long id) throws InternalServerException, NotFoundException {
        return fileDAO.findById(id);
    }

    public File update(File file) throws InternalServerException, BadRequestException {
        try {
            validateFile(file);
            validateUpdate(file);

            return fileDAO.update(file);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot update file " + file.getId() + " : " + e.getMessage());
        }
    }

    public void delete(long id) throws InternalServerException, BadRequestException {
        if (!fileDAO.isExists(id)) {
            throw new BadRequestException("Cannot delete file " + id + " : missing file with id " + id);
        }
        fileDAO.delete(id);
    }

    public File put(Storage storage, File file) throws BadRequestException, InternalServerException {
        try {
            validatePut(storage, file);

            return fileDAO.put(storage, file);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot put file in storage " + storage.getId() + " : " + e.getMessage());
        }
    }

    public void putAll(Storage storage, List<File> files) throws BadRequestException, InternalServerException {
        try {
            validatePutAll(storage, files);

            fileDAO.putAll(storage, files);
        } catch (BadRequestException e) {
            throw new BadRequestException("Cannot put all files to storage " + storage.getId() + " : "
                    + e.getMessage());
        }
    }

    public File deleteFromStorage(long storageId, File file) throws BadRequestException, InternalServerException {
        try {
            validateDeleteFromStorage(storageId, file.getStorage());

            return fileDAO.deleteFromStorage(storageId, file);
        } catch (BadRequestException e) {
            throw new BadRequestException(String.format(
                    "Cannot delete file %d from storage %d : %s",
                    file.getId(), storageId, e.getMessage()));
        }
    }

    public void transferAll(Storage storageFrom, Storage storageTo)
            throws BadRequestException, InternalServerException {
        try {
            validateTransferAll(storageFrom.getId(), storageFrom.getFormatsSupported(), storageTo);

            fileDAO.transferAll(storageFrom.getId(), storageTo.getId());
        } catch (BadRequestException e) {
            throw new BadRequestException(String.format(
                    "Cannot transfer files from storage %d to storage %d : %s",
                    storageFrom.getId(), storageTo.getId(), e.getMessage()));
        }
    }

    public void transferFile(long storageFromId, Storage storageTo, long id)
            throws BadRequestException, InternalServerException {
        try {
            File file = findById(id);

            validateTransferFile(storageFromId, storageTo, file);

            fileDAO.transferFile(storageFromId, storageTo.getId(), file.getId());
        } catch (BadRequestException | NotFoundException e) {
            throw new BadRequestException(String.format(
                    "Cannot transfer file %d from storage %d to storage %d : %s",
                    id, storageFromId, storageTo.getId(), e.getMessage()));
        }
    }

    private void validateFile(File file) throws BadRequestException {
        if (file == null) {
            throw new BadRequestException("File can`t be null");
        }
        if (file.getName() == null
                || file.getFormat() == null
                || file.getSize() == null
                || file.getSize() <= 0) {
            throw new BadRequestException("Fields filed incorrect");
        }
        if (file.getName().length() > 10) {
            throw new BadRequestException("Name length must be less then 11");
        }
        if (file.getFormat().length() > 20) {
            throw new BadRequestException("Name length must be less then 21");
        }
    }

    private void validatePut(Storage storage, File file) throws BadRequestException, InternalServerException {
        if (file.getStorage() != null) {
            throw new BadRequestException("The file is in already in storage " + file.getStorage().getId());
        }
        validateFormatAndSize(file.getFormat(), file.getSize(), storage);
    }

    private void validatePutAll(Storage storage, List<File> files) throws BadRequestException, InternalServerException {
        long filesSize = 0;
        List<String> formats = new ArrayList<>();
        for (File file : files) {

            if (file.getStorage() != null) {
                throw new BadRequestException("The file " + file.getId() + " is in already in storage");
            }
            if (!formats.contains(file.getFormat())) {
                formats.add(file.getFormat());
            }
            filesSize += file.getSize();
        }
        long freeSpace = storage.getStorageSize() - fileDAO.getFilesSizeByStorageId(storage.getId());

        if (freeSpace < filesSize) {
            throw new BadRequestException("No storage space");
        }

        List<String> storageFormats = Arrays.asList(storage.getFormatsSupported());
        for (String format : formats) {
            if (storageFormats.contains(format)) continue;
            throw new BadRequestException("Unsuitable format " + format);
        }
    }

    private void validateDeleteFromStorage(long storageId, Storage fileStorage) throws BadRequestException {
        if (fileStorage == null) {
            throw new BadRequestException("The file is not in the storage");
        }
        if (storageId != fileStorage.getId()) {
            throw new BadRequestException("The file is not in the given storage");
        }
    }

    private void validateUpdate(File file) throws BadRequestException, InternalServerException {
        String oldName = fileDAO.getFileName(file.getId());
        if (!oldName.equals(file.getName())) {
            fileDAO.isNameUnique(file.getName());
        }
        if (file.getStorage() != null) {
            validateFormatAndSize(file.getFormat(), file.getSize(), file.getStorage());
        }
    }

    private void validateTransferAll(long storageFromId, String[] formatSupportedFrom, Storage storageTo)
            throws BadRequestException, InternalServerException {

        if (storageFromId == storageTo.getId()) {
            throw new BadRequestException("Can`t transfer to the same storage");
        }
        for (String formatFrom : formatSupportedFrom) {
            if (Arrays.asList(storageTo.getFormatsSupported()).contains(formatFrom)) continue;
            throw new BadRequestException("Unsuitable format");
        }
        if (fileDAO.isStorageEmpty(storageFromId)) {
            throw new BadRequestException("Storage is Empty");
        }
        long filesSizeFrom = fileDAO.getFilesSizeByStorageId(storageFromId);
        long freeSpaceTo = storageTo.getStorageSize() - fileDAO.getFilesSizeByStorageId(storageTo.getId());

        if (filesSizeFrom > freeSpaceTo) {
            throw new BadRequestException("No storage space");
        }
    }

    private void validateTransferFile(long storageFromId, Storage storageTo, File file)
            throws BadRequestException, InternalServerException {

        if (storageFromId == storageTo.getId()) {
            throw new BadRequestException("Can`t transfer to the same storage");
        }
        if (file.getStorage() == null) {
            throw new BadRequestException("The file is not in the storage");
        }
        if (storageFromId != file.getStorage().getId()) {
            throw new BadRequestException("The file is not in the given storage");
        }
        if (file.getStorage().getId().equals(storageTo.getId())) {
            throw new BadRequestException("The file is already in current storage");
        }
        validateFormatAndSize(file.getFormat(), file.getSize(), storageTo);
    }

    private void validateFormatAndSize(String fileFormat, long fileSize, Storage storage)
            throws BadRequestException, InternalServerException {

        if (!Arrays.asList(storage.getFormatsSupported()).contains(fileFormat)) {
            throw new BadRequestException("Unsuitable format");
        }
        long freeSpace = storage.getStorageSize() - fileDAO.getFilesSizeByStorageId(storage.getId());
        if (freeSpace < fileSize) {
            throw new BadRequestException("No storage space");
        }
    }
}
