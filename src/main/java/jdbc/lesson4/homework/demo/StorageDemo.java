package jdbc.lesson4.homework.demo;

import jdbc.lesson4.homework.controller.StorageController;
import jdbc.lesson4.homework.model.Storage;

public class StorageDemo {
    private static final StorageController storageController = new StorageController();

    public static void main(String[] args) throws Exception {

        Storage storage = new Storage(new String[]{"txt", "jpeg"}, "2", 100);
        storageController.save(storage);

        storageController.findById(0);

        Storage storage2 = storageController.findById(1L);
        storage2.setStorageCountry("Germany");
        storageController.update(storage2);

        storageController.delete(1238376555057004794L);
    }
}