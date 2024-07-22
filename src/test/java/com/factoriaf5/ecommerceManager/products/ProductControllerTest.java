package com.factoriaf5.ecommerceManager.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //to ensure that it reiniciate the id after each test
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper; //transform class(object) to string

    @Autowired
    ProductRepo productRepo;

    @Test
    void createProduct() throws Exception {
        //1. create a request we need to pass to the controller
        ProductRequest productRequest;
        productRequest = new ProductRequest("name", "description", 10.0);
        //2. transform to string using ObjectMapper
        String jsonProductRequest = objectMapper.writeValueAsString(productRequest);
        //3. use it in mockMvc.content
        //4. assert the response (see if it's saved in DB or not)
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProductRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("description").value("description"))
                .andExpect(jsonPath("price").value(10.0));
    }

    @Test
    void negativePriceCanNotBeAccepted() throws Exception {
        ProductRequest productRequest;
        productRequest = new ProductRequest("name", "description", -10.0);
        String jsonProductRequest = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProductRequest))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertEquals("The price must be positive", result.getResolvedException().getMessage()));
    }
    @Test
    void listAllProducts() throws Exception {
        productRepo.save(
                new Product("name", "description", 10.0)
        );
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].price").value(10.0));
    }

}