package lesson4.homework.demo;

import lesson4.homework.controller.FileController;
import lesson4.homework.controller.StorageController;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

import java.util.ArrayList;
import java.util.List;

public class FileDemo {

    private static final FileController fileController = new FileController();
    private static final StorageController storageController = new StorageController();

    public static void main(String[] args) throws Exception {

        File file = new File("N6", "ttt", 20);
        System.out.println(fileController.save(file).toString());

        System.out.println(fileController.findById(8844935201646854465L).toString());

        File file2 = fileController.findById(8844935201646854465L);
        file2.setName("RRRR");
        System.out.println(fileController.update(file2).toString());

        fileController.delete(8844935201646854465L);


        File file3 = fileController.findById(186509638922684330L);
        Storage storage3 = storageController.findById(842915809380813828L);
        System.out.println(fileController.put(storage3, file3).toString());

        List<File> files = new ArrayList<>();
        files.add(fileController.findById(3163891295253383570L));
        files.add(fileController.findById(186509638922684330L));
        files.add(fileController.findById(8972624872836449380L));
        Storage storage4 = storageController.findById(3101416146196972198L);
        fileController.putAll(storage4, files);

        Storage storage5 = storageController.findById(3101416146196972198L);
        File file5 = fileController.findById(3163891295253383570L);
        System.out.println(fileController.deleteFromStorage(storage5, file5).toString());

        Storage storage6 = storageController.findById(842915809380813828L);
        Storage storage61 = storageController.findById(3101416146196972198L);
        fileController.transferAll(storage6, storage61);

        Storage storage7 = storageController.findById(3101416146196972198L);
        Storage storage71 = storageController.findById(842915809380813828L);
        fileController.transferFile(storage7, storage71, 186509638922684330L);
    }
}
