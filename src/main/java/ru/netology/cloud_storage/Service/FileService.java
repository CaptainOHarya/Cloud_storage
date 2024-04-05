package ru.netology.cloud_storage.Service;

import org.springframework.stereotype.Service;
import ru.netology.cloud_storage.entity.File;
import ru.netology.cloud_storage.model.FileData;

import java.util.List;

@Service
public interface FileService {
    boolean uploadFile(String authToken, String fileName, String contentType, long size, byte[] bytes);
   // boolean overwriteFile(String authToken, String fileName, String contentType, long size, byte[] bytes);
    String deleteFile(String authToken, String fileName);
    File getFile(String authToken, String fileName);
    boolean renameFile(String authToken, String fileName, String newFileName);
    List<FileData> getAllFiles(String authToken, int numberOfFiles);

    Long verifyUser(String authToken);

    File verifyFile(Long userId, String fileName);







}
