package lesson4.homework.demo;

import lesson4.homework.controller.FileController;
import lesson4.homework.controller.StorageController;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

public class Demo {
    public static void main(String[] args) throws Exception {
//        File file = new File("name3", "jpeg", 539);
//        Storage storage = new Storage(new String[]{"txt", "jpeg", "mp3"}, "Test", 4555);

//        Storage storage2 = StorageController.save(storage);
        Storage storage1 = StorageController.findById(2127443797907688257L);
        Storage storage3 = StorageController.findById(1);
//       File file2 = FileController.findById(2137333597638708282L);

//       file2.setStorage(storage3);
//       FileController.update(file2);
//
//       File file1 = FileController.put(storage1, file);
//        FileController.delete(storage1, file2);

//        FileController.transferAll(storage3, storage1);
        FileController.transferFile(storage3, storage1, 2137333597638708282L);

//
//        System.out.println(storage1.toString());
//        System.out.println(file2.toString());
    }
}