package jdbc.lesson4.homework.model;

import jdbc.lesson4.homework.Exceptions.BadRequestException;

public class File {
    private long id;
    private String name;
    private String format;
    private long size;
    private Storage storage;

    public File(String name, String format, long size) throws BadRequestException {
        if (name == null || format == null || size <= 0 || name.equals("") || format.equals("")) {
            throw new BadRequestException("Fields are not filed correctly");
        }
        if (name.length() > 10) throw new BadRequestException("Name length must be <= 10");
        this.name = name;
        this.format = format;
        this.size = size;
    }

    public File(long id, String name, String format, long size, Storage storage) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.size = size;
        this.storage = storage;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public long getSize() {
        return size;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}