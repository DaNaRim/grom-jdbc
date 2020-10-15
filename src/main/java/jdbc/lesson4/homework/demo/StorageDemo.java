package jdbc.lesson4.homework.demo;

import jdbc.lesson4.homework.controller.StorageController;
import jdbc.lesson4.homework.model.Storage;

public class StorageDemo {
    private static final StorageController storageController = new StorageController();

    public static void main(String[] args) {

        Storage storage = new Storage(new String[]{"txt", "jpeg"}, "2", 100);
        System.out.println(storageController.save(storage));

        System.out.println(storageController.findById(0));

        Storage storage2 = new Storage(1, new String[]{"txt", "mp3"}, "test", 100);
        System.out.println(storageController.update(storage2));

        System.out.println(storageController.delete(1238376555057004794L));
    }
}