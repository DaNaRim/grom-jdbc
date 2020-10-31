package jdbc.lesson4.homework.demo;

import jdbc.lesson4.homework.controller.FileController;
import jdbc.lesson4.homework.controller.StorageController;
import jdbc.lesson4.homework.model.File;
import jdbc.lesson4.homework.model.Storage;

import java.util.ArrayList;
import java.util.List;

public class FileDemo {

    private static final FileController fileController = new FileController();
    private static final StorageController storageController = new StorageController();

    public static void main(String[] args) throws Exception {

        File file = new File("92", "txt", 50);
        fileController.save(file);

        fileController.findById(9L);

        File file2 = fileController.findById(9L);
        file2.setName("RRRR");
        fileController.update(file2);

        fileController.delete(2055521456558983064L);


        File file3 = fileController.findById(9L);
        Storage storage3 = storageController.findById(4L);
        fileController.put(storage3, file3);

        List<File> files = new ArrayList<>();
        files.add(fileController.findById(3L));
        files.add(fileController.findById(4L));
        files.add(fileController.findById(5L));
        Storage storage4 = storageController.findById(3L);
        fileController.putAll(storage4, files);

        Storage storage5 = storageController.findById(2L);
        File file5 = fileController.findById(8L);
        fileController.deleteFromStorage(storage5, file5);

        Storage storage6 = storageController.findById(2L);
        Storage storage61 = storageController.findById(3L);
        fileController.transferAll(storage6, storage61);

        Storage storage7 = storageController.findById(3L);
        Storage storage71 = storageController.findById(2L);
        fileController.transferFile(storage7, storage71, 3116545270738536768L);
    }
}
