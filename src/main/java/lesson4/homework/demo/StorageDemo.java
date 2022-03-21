package lesson4.homework.demo;

import lesson4.homework.controller.StorageController;
import lesson4.homework.model.Storage;

public class StorageDemo {

    private static final StorageController storageController = new StorageController();

    public static void main(String[] args) throws Exception {

        Storage storage = new Storage(new String[]{"txt", "jpeg"}, "2", 100);
        System.out.println(storageController.save(storage).toString());

        System.out.println(storageController.findById(6395895093391083213L).toString());

        Storage storage2 = storageController.findById(6395895093391083213L);
        storage2.setStorageCountry("Germany");
        System.out.println(storageController.update(storage2).toString());

        storageController.delete(6395895093391083213L);
    }
}
