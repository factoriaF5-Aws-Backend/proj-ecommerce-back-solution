package com.factoriaf5.ecommerceManager.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {
    @InjectMocks
    private FileStorageService fileStorageService;

    MockMultipartFile mockMultipartFile;

    @BeforeEach
    public void beforeEachTest() {
        this.mockMultipartFile = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    private final String IMAGE_DIRECTORY = "uploads/images/";
    private final String IMAGE_URL = "http://localhost:8080/uploads/images/";

    @Test
        //Integration Test
    void fileStore_WhenFileIsValid_ShouldStoreFileAndReturnUrl() {
        UUID mockUUID = UUID.randomUUID();
        String mockImageFileName = mockUUID + ".jpg";

        MockedStatic<UUID> mockedUUID = mockStatic(UUID.class);

        mockedUUID.when(UUID::randomUUID).thenReturn(mockUUID);

        String expectedUrl = IMAGE_URL + mockImageFileName;

        // Call the method
        String fileUrl = fileStorageService.fileStore(mockMultipartFile);

        String fileLocalPath = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Boolean isExist = Files.exists(Path.of(IMAGE_DIRECTORY + fileLocalPath));

        // Assertions
        assertEquals(expectedUrl, fileUrl);
        assertTrue(isExist);

        fileStorageService.fileDelete(fileUrl);
    }

    @Test
        //Unit test
    void fileStore_WhenIOExceptionOccurs_ShouldThrowRuntimeException() {
        UUID mockUUID = UUID.randomUUID();
        String mockImageFileName = mockUUID + ".jpg";
        Path mockPath = Paths.get(IMAGE_DIRECTORY + mockImageFileName);

        try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class)) {

            mockedUUID.when(UUID::randomUUID).thenReturn(mockUUID);
            filesMock.when(() -> Files.createDirectories(mockPath.getParent())).thenReturn(mockPath.getParent());
            filesMock.when(() -> Files.write(mockPath, mockMultipartFile.getBytes())).thenThrow(new IOException("Disk is full"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                fileStorageService.fileStore(mockMultipartFile);
            });

            assertTrue(exception.getMessage().contains("Problem uploading file:"));
            filesMock.verify(() -> Files.createDirectories(mockPath.getParent()), times(1));
            filesMock.verify(() -> Files.write(mockPath, mockMultipartFile.getBytes()), times(1));
        }
    }

    @Test
        //Integration Test
    void fileDelete_WhenFileExists_ShouldDeleteFile() {
        String fileUrl = fileStorageService.fileStore(mockMultipartFile);
        String fileLocalName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path fileLocalPath = Path.of(IMAGE_DIRECTORY + fileLocalName);
        Boolean isExist = Files.exists(fileLocalPath);

        assertTrue(isExist);
        fileStorageService.fileDelete(fileUrl);

        Boolean isStillExist = Files.exists(fileLocalPath);
        assertFalse(isStillExist);
    }


}
