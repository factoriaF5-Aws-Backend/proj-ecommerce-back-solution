package com.factoriaf5.ecommerceManager.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //to ensure that it reiniciate the id after each test
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepo userRepo;


    ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    void registerUser() throws Exception{
        UserRequest userRequest = new UserRequest("test", "test","test");

        String userRequestJson = objectMapper.writeValueAsString(userRequest);
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("userName").value("test"))
                .andExpect(jsonPath("email").value("test"))
                .andExpect(jsonPath("password").value("test"));
    }

    @Test
    void findUser() throws Exception{
        User registeredUser = new User (1L, "test", "test", "test");

        userRepo.save(registeredUser);

        mockMvc.perform(get("/api/users/test")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("userName").value(registeredUser.getUserName()))
                .andExpect(jsonPath("email").value(registeredUser.getEmail()))
                .andExpect(jsonPath("password").value(registeredUser.getPassword()));
    }
}
