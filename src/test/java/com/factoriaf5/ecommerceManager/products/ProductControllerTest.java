package com.factoriaf5.ecommerceManager.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //to ensure that it reiniciate the id after each test
class ProductControllerTest {

    public static final String IMAGE_DIRECTORY = "uploads/images/";
    public static final String IMAGE_URL = "http://localhost:8080/uploads/images/";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductRepo productRepo;

    ProductRequest productRequest;
    MockMultipartFile mockImage;
    Product savedProduct;
    UUID uuid;
    String localDirectory;

    MockedStatic<UUID> mockedUUID;

    @BeforeEach
    public void beforeEachTest() {
        this.uuid = UUID.randomUUID();
        this.mockedUUID = mockStatic(UUID.class);
        mockedUUID.when(UUID::randomUUID).thenReturn(uuid);

        this.mockImage = mock(MockMultipartFile.class);
        this.productRequest = new ProductRequest("test", "test", 1.0, true, mockImage, "test");
        this.savedProduct = new Product(1L, "test", "test", 1.0, true, IMAGE_URL + uuid.toString() + ".", "test");
        this.localDirectory = IMAGE_DIRECTORY + uuid.toString() + ".";
    }

    private void cleanFilesAfterTest(UUID uuid) throws IOException {
        String fileLocalDirectory = IMAGE_DIRECTORY + uuid.toString() + ".";
        Files.delete(Path.of(fileLocalDirectory));
    }

    @Test
    void createProduct() throws Exception {
        //1. create a request we need to pass to the controller
        //2. transform to string using ObjectMapper
        //3. use it in mockMvc.content
        //4. assert the response (see if it's saved in DB or not)
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .file(new MockMultipartFile("image", "bytes".getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name", "name")
                        .param("description", "description")
                        .param("price", "10.0")
                        .param("featured", "true")
                        .param("category", "category"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("description").value("description"))
                .andExpect(jsonPath("price").value(10.0))
                .andExpect(jsonPath("featured").value(true))
                .andExpect(jsonPath("imageUrl").value(IMAGE_URL + uuid.toString() + "."))
                .andExpect(jsonPath("category").value("category"));

        //Assert that the image stored locally
        assertTrue(Files.exists(Path.of(localDirectory)));

        //To delete the image stored after the test
        cleanFilesAfterTest(uuid);

        //You have to close the mock of UUID after each test
        mockedUUID.close();
    }


    @Test
    void listAllProducts() throws Exception {
        productRepo.save(savedProduct);
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(savedProduct.getName()))
                .andExpect(jsonPath("$[0].description").value(savedProduct.getDescription()))
                .andExpect(jsonPath("$[0].price").value(savedProduct.getPrice()))
                .andExpect(jsonPath("$[0].featured").value(savedProduct.getFeatured()))
                .andExpect(jsonPath("$[0].imageUrl").value(savedProduct.getImageUrl()))
                .andExpect(jsonPath("$[0].category").value(savedProduct.getCategory()));
        mockedUUID.close();
    }

    @Test
    void findAProduct() throws Exception {
        productRepo.save(savedProduct);
        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value(savedProduct.getName()))
                .andExpect(jsonPath("description").value(savedProduct.getDescription()))
                .andExpect(jsonPath("price").value(savedProduct.getPrice()))
                .andExpect(jsonPath("featured").value(savedProduct.getFeatured()))
                .andExpect(jsonPath("imageUrl").value(savedProduct.getImageUrl()))
                .andExpect(jsonPath("category").value(savedProduct.getCategory()));
        mockedUUID.close();
    }

    @Test
    void deleteAProduct() throws Exception {
        productRepo.save(savedProduct);

        assertEquals(1, productRepo.count());

        mockMvc.perform(delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, productRepo.count());
        mockedUUID.close();
    }

    @Test
    void updateAProduct() throws Exception {
        productRepo.save(savedProduct);
        ProductRequest productRequest;
        productRequest = new ProductRequest("updated name", "updated description", 2.0, true, mockImage, "updated category");

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/products/1")
                        .file(new MockMultipartFile("image", "bytes".getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name", "updated name")
                        .param("description", "updated description")
                        .param("price", "2.0")
                        .param("featured", "true")
                        .param("category", "updated category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value(productRequest.name()))
                .andExpect(jsonPath("description").value(productRequest.description()))
                .andExpect(jsonPath("price").value(productRequest.price()))
                .andExpect(jsonPath("featured").value(productRequest.featured()))
                .andExpect(jsonPath("category").value(productRequest.category()));

        //Assert that the image stored locally
        assertTrue(Files.exists(Path.of(localDirectory)));

        //To delete the image stored after the test
        cleanFilesAfterTest(uuid);
        mockedUUID.close();

    }

    @Test
    void listFeaturedProducts() throws Exception {
        productRepo.save(savedProduct);
        mockMvc.perform(get("/api/products/featured")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(savedProduct.getName()))
                .andExpect(jsonPath("$[0].description").value(savedProduct.getDescription()))
                .andExpect(jsonPath("$[0].price").value(savedProduct.getPrice()))
                .andExpect(jsonPath("$[0].featured").value(true))
                .andExpect(jsonPath("$[0].imageUrl").value(savedProduct.getImageUrl()))
                .andExpect(jsonPath("$[0].category").value(savedProduct.getCategory()));
        mockedUUID.close();
    }

    @Test
    void listProductsByCategory() throws Exception {
        productRepo.save(savedProduct);
        mockMvc.perform(get("/api/products/categories/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(savedProduct.getName()))
                .andExpect(jsonPath("$[0].description").value(savedProduct.getDescription()))
                .andExpect(jsonPath("$[0].price").value(savedProduct.getPrice()))
                .andExpect(jsonPath("$[0].featured").value(savedProduct.getFeatured()))
                .andExpect(jsonPath("$[0].imageUrl").value(savedProduct.getImageUrl()))
                .andExpect(jsonPath("$[0].category").value(savedProduct.getCategory()));
        mockedUUID.close();
    }
}