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

    public static void main(String[] args) {

        File file = new File("92", "txt", 50);
        System.out.println(fileController.save(file));

        System.out.println(fileController.findById(9L));

        File file2 = new File(9, "trum22", "txt", 130, null);
        System.out.println(fileController.update(file2));

        System.out.println(fileController.delete(2055521456558983064L));


        File file3 = new File(9, "trum22", "txt", 130, null);
        Storage storage3 = new Storage(1, new String[]{"txt", "mp3"}, "test", 1000);
        System.out.println(fileController.put(storage3, file3));

        List<File> files = new ArrayList<>();
        files.add(new File(6898168605961111254L, "90", "txt", 50, null));
        files.add(new File(3116545270738536768L, "91", "txt", 50, null));
        files.add(new File(1307919980345050077L, "92", "txt", 50, null));
        Storage storage4 = new Storage(1, new String[]{"txt", "mp3"}, "test", 1000);
        System.out.println(fileController.putAll(storage4, files));

        Storage storage5 = new Storage(1, new String[]{"txt", "mp3"}, "test", 1000);
        File file5 = new File(6898168605961111254L, "90", "txt", 50, storage5);
        System.out.println(fileController.deleteFromStorage(storage5, file5));

        Storage storage6 = new Storage(1, new String[]{"txt"}, "test", 1000);
        Storage storage61 = new Storage(2, new String[]{"txt"}, "test2", 800);
        System.out.println(fileController.transferAll(storage6, storage61));

        Storage storage7 = new Storage(2, new String[]{"txt"}, "test2", 800);
        Storage storage71 = new Storage(1, new String[]{"txt"}, "test", 1000);
        System.out.println(fileController.transferFile(storage7, storage71, 3116545270738536768L));
    }
}
