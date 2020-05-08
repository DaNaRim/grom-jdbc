package lesson4.homework.demo;

import lesson4.homework.controller.FileController;
import lesson4.homework.controller.StorageController;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

public class Demo {
    public static void main(String[] args) throws Exception {
        File file = new File("name1", "txt", 53);
        String[] formatsSupported = {"txt", "jpeg"};
        Storage storage = new Storage(formatsSupported, "Ukraine", 4555);

//        Storage storage1 = StorageController.save(storage);
        Storage storage1 = StorageController.findById(1);
//        File file2 = FileController.findById(788463553908461362L);
//
//        File file1 = FileController.put(storage1, file2);
//        FileController.delete(storage1, file1);

        System.out.println(storage1.toString());
    }
}