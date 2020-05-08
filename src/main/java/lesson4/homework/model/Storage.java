package lesson4.homework.model;

import lesson4.homework.Exceptions.BadRequestException;

import java.util.Arrays;

public class Storage {
    private long id;
    private String[] formatsSupported;
    private String storageCountry;
    private long storageSize;
    private long freeSpace;

    public Storage(String[] formatsSupported, String storageCountry, long storageSize) throws BadRequestException {
        if (formatsSupported == null || formatsSupported.length == 0 || storageCountry == null ||
                storageCountry.equals("") || storageSize <= 0) {
            throw new BadRequestException("Fields are not filed correctly");
        }
        this.formatsSupported = formatsSupported;
        this.storageCountry = storageCountry;
        this.storageSize = storageSize;
        this.freeSpace = storageSize;
    }

    public Storage(long id, String[] formatsSupported, String storageCountry, long storageSize, long freeSpace) {
        this.id = id;
        this.formatsSupported = formatsSupported;
        this.storageCountry = storageCountry;
        this.storageSize = storageSize;
        this.freeSpace = freeSpace;
    }

    public long getId() {
        return id;
    }

    public String[] getFormatsSupported() {
        return formatsSupported;
    }

    public String getStorageCountry() {
        return storageCountry;
    }

    public long getStorageSize() {
        return storageSize;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFreeSpace(long freeSpace) {
        this.freeSpace = freeSpace;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", formatsSupported=" + Arrays.toString(formatsSupported) +
                ", storageCountry='" + storageCountry + '\'' +
                ", storageSize=" + storageSize +
                ", freeSpace=" + freeSpace +
                '}';
    }
}