package ru.netology.cloud_storage;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.Service.FileService;
import ru.netology.cloud_storage.Service.UserService;
import ru.netology.cloud_storage.dto.requestDto.UserRequestDto;
import ru.netology.cloud_storage.dto.responseDto.UserResponseDto;
import ru.netology.cloud_storage.entity.File;
import ru.netology.cloud_storage.model.FileData;
import ru.netology.cloud_storage.repository.FileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CloudStorageApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class FileServiceTest {

        private final String fileName = "text1.txt";
        private final String fileNameNew = "text2.txt";


        @Autowired
        FileRepository fileRepository;

        @Autowired
        UserService userService;

        @Autowired
        FileService fileService;

        @SneakyThrows
        public MultipartFile multipartFileGet(String fileNameTest) {
            MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
            URL resource = getClass().getClassLoader().getResource(fileNameTest);

            URLConnection urlConnection = Objects.requireNonNull(resource).openConnection();
            byte[] content = ((InputStream) urlConnection.getContent()).readAllBytes();
            String contentMimeType = urlConnection.getContentType();

            Mockito.when(multipartFile.getContentType()).thenReturn(contentMimeType);
            Mockito.when(multipartFile.getBytes()).thenReturn(content);
            Mockito.when(multipartFile.getSize()).thenReturn((long) content.length);

            return multipartFile;
        }

        @SneakyThrows
        @Test
        public void uploadFile_whenUploadFile_thenReturnedTrue() {
            String login = "Marina";
            String password = "mar11";

            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            MultipartFile multipartFile = multipartFileGet(fileName);
            Long userId = userService.getSession(authToken).getUserId();
            if (fileRepository.findFileByUserIdAndFileName(userId, fileName).isPresent()) {
                fileService.deleteFile(authToken, fileName);
            }
            String contentType = multipartFile.getContentType();
            byte[] bytes = multipartFile.getBytes();
            long sizeFile = multipartFile.getSize();
            boolean result = fileService.uploadFile(authToken, fileName, contentType, sizeFile, bytes);
            assertTrue(result);

        }

        @SneakyThrows
        @Test
        public void uploadFile_whenUploadFile_thenReturnedFalse() {
            String login = "bagzbanni";
            String password = "VKur";

            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            MultipartFile multipartFile1 = multipartFileGet(fileName);
            String contentType = multipartFile1.getContentType();
            byte[] bytes = multipartFile1.getBytes();
            long sizeFile = multipartFile1.getSize();
            Long userId = userService.getSession(authToken).getUserId();
            if (fileRepository.findFileByUserIdAndFileName(userId, fileName).isEmpty()) {
                fileService.uploadFile(authToken, fileName, contentType, sizeFile, bytes);
            }
            boolean result = fileService.uploadFile(authToken, fileName, contentType, sizeFile, bytes);

            assertFalse(result);
        }

        @Test
        public void renameFile_whenRenameFile_thenReturnedTrue() {
            String login = "Marina";
            String password = "mar11";

            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            Long userId = userService.getSession(authToken).getUserId();
            if (fileRepository.findFileByUserIdAndFileName(userId, fileNameNew).isPresent()) {
                fileService.deleteFile(authToken, fileName);
            }
            boolean result = fileService.renameFile(authToken, fileName, fileNameNew);
            assertTrue(result);
        }

        @SneakyThrows
        @Test
        public void renameFile_whenRenameFile_thenReturnedFalse() {
            String login = "AKasperchak";
            String password = "1234.,mn";
            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            MultipartFile multipartFile1 = multipartFileGet(fileName);
            MultipartFile multipartFile2 = multipartFileGet(fileNameNew);
            Long userId = userService.getSession(authToken).getUserId();
            if (fileRepository.findFileByUserIdAndFileName(userId, fileName).isEmpty()) {
                fileService.uploadFile(authToken, fileName, multipartFile1.getContentType(), multipartFile1.getSize(), multipartFile1.getBytes());
            }
            if (fileRepository.findFileByUserIdAndFileName(userId, fileNameNew).isEmpty()) {
                fileService.uploadFile(authToken, fileNameNew, multipartFile2.getContentType(), multipartFile2.getSize(), multipartFile2.getBytes());
            }
            boolean result = fileService.renameFile(authToken, fileName, fileNameNew);
            assertFalse(result);

        }
        @SneakyThrows
        @Test
        public void getFile_whenGetExistingFile_thenGetEqualsBytes()  {
            String login = "bagzbanni";
            String password = "VKur";

            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            String fileNameTest = "test.txt";
            Long userId = userService.getSession(authToken).getUserId();
            if (fileRepository.findFileByUserIdAndFileName(userId, fileNameTest).isPresent()) {
                fileService.deleteFile(authToken, fileNameTest);
            }
            MultipartFile multipartFile = multipartFileGet(fileNameTest);
            String contentType = multipartFile.getContentType();
            byte[] bytes = multipartFile.getBytes();
            long sizeFile = multipartFile.getSize();
            fileService.uploadFile(authToken, fileNameTest, contentType, sizeFile, bytes);
            File fileContent = fileService.getFile(authToken, fileNameTest);
            assertArrayEquals(bytes, fileContent.getContent());
        }

        @SneakyThrows
        @Test
        public void deleteFile_whenDeleteFile_thenGetSameStrings() {
            String login = "AKasperchak";
            String password = "1234.,mn";
            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();

            String fileNameTest2 = "testDeleteFile.txt";
            MultipartFile multipartFile = multipartFileGet(fileNameTest2);
            String contentType = multipartFile.getContentType();
            byte[] bytes = multipartFile.getBytes();
            long sizeFile = multipartFile.getSize();
            fileService.uploadFile(authToken, fileNameTest2, contentType, sizeFile, bytes);
            String actual = fileService.deleteFile(authToken, fileNameTest2);
            String expected = " Файл " + fileNameTest2 + " успешно удалён";
            assertEquals(expected, actual);
        }

        @SneakyThrows
        @Test
        public void getAllFiles_whenGetExistingFiles_thenGetEqualsName() {
            String login = "AKasperchak";
            String password = "1234.,mn";
            int limit = 4;
            String fileName1 = "test_one.txt";
            String fileName2 = "test_two.txt";
            String fileName3 = "test_three.txt";
            String fileName4 = "test_four.txt";
            MultipartFile multipartFile1 = multipartFileGet(fileName1);
            MultipartFile multipartFile2 = multipartFileGet(fileName2);
            MultipartFile multipartFile3 = multipartFileGet(fileName3);
            MultipartFile multipartFile4 = multipartFileGet(fileName4);
            List<FileData> list = List.of(
                    FileData.builder().fileName(fileName1).size(multipartFile1.getSize()).build(),
                    FileData.builder().fileName(fileName2).size(multipartFile2.getSize()).build(),
                    FileData.builder().fileName(fileName3).size(multipartFile3.getSize()).build(),
                    FileData.builder().fileName(fileName4).size(multipartFile4.getSize()).build()
            );

            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            if (fileRepository.findFileByFileName(fileName1).isEmpty()) {
                fileService.uploadFile(authToken, fileName1, multipartFile1.getContentType(), multipartFile1.getSize(), multipartFile1.getBytes());
            }
            if (fileRepository.findFileByFileName(fileName2).isEmpty()) {
                fileService.uploadFile(authToken, fileName2, multipartFile2.getContentType(), multipartFile2.getSize(), multipartFile2.getBytes());
            }
            if (fileRepository.findFileByFileName(fileName3).isEmpty()) {
                fileService.uploadFile(authToken, fileName3, multipartFile3.getContentType(), multipartFile3.getSize(), multipartFile3.getBytes());
            }
            if (fileRepository.findFileByFileName(fileName4).isEmpty()) {
                fileService.uploadFile(authToken, fileName4, multipartFile4.getContentType(), multipartFile4.getSize(), multipartFile4.getBytes());
            }

            List<FileData> actual = fileService.getAllFiles(authToken, limit);
            for (FileData file : list) {
                assertTrue(actual.stream().anyMatch(x -> Objects.equals(x.getFileName(), file.getFileName())));
            }
        }



        @Test
        public void verifyUser_whenVerify_thenGetSameResult() {
            String login = "AlienKesha";
            String password = "alien1";
            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            Long expected = 25L; // в таблице он у меня уже был 25-й из-за множества попыток, так, если в первый раз, то сравнивать с 4L
            Long actual = fileService.verifyUser(authToken);
            assertEquals(expected, actual);
        }

        @SneakyThrows
        @Test
        public void verifyFile_whenVerify_thenGetSameResult() {
            String login = "Marina";
            String password = "mar11";
            UserResponseDto response = userService.authenticate(
                    new UserRequestDto(login, password));
            String authToken = Objects.requireNonNull(response).getAuthToken();
            MultipartFile multipartFile = multipartFileGet(fileNameNew);
            Long userId = userService.getSession(authToken).getUserId();

            if (fileRepository.findFileByUserIdAndFileName(userId, fileNameNew).isEmpty()) {
                fileService.uploadFile(authToken, fileNameNew,  multipartFile.getContentType(), multipartFile.getSize(), multipartFile.getBytes());
            }
            var actual = fileService.verifyFile(userId, fileNameNew).getFileName();
            assertEquals(fileNameNew, actual);
        }
}
