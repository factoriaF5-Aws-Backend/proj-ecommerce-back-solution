package com.factoriaf5.ecommerceManager.categories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryRepository categoryRepository;

    Category savedCategory;

    @Test
    void can_create_valid_category() throws Exception {

        String json = """
                { "name": "categoryName" }
                """;

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(jsonPath("$.name").value("categoryName"))
                .andExpect(jsonPath("$.id").value(1));

        assertEquals(1, categoryRepository.count());
    }

    @Test
    void can_list_all_categories() throws Exception {
        savedCategory = new Category("category");
        categoryRepository.save(savedCategory);
        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(savedCategory.getName()));
    }

    @Test
    void can_find_a_category_by_id() throws Exception {
        savedCategory = new Category("category");
        categoryRepository.save(savedCategory);
        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value(savedCategory.getName()));
    }

    @Test
    void can_delete_a_category() throws Exception {
        savedCategory = new Category("category");
        categoryRepository.save(savedCategory);

        assertEquals(1, categoryRepository.count());

        mockMvc.perform(delete("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, categoryRepository.count());
    }

    @Test
    void can_update_a_category() throws Exception {
        Category savedCategory = new Category("category");
        categoryRepository.save(savedCategory);

        // Perform the update request
        mockMvc.perform(put("/api/categories/" + savedCategory.getId())  // Assuming the update endpoint is a PUT request to /api/categories/{id}
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"updated name\"}"))  // JSON body for updating the name
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value("updated name"));
    }
}