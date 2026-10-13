package com.santiagomac.template;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Default configuration: authentication disabled, every endpoint is public. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TemplateApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndReadsProductWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"first\",\"description\":\"hello\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("first"));

        mockMvc.perform(get("/api/v1/products")).andExpect(status().isOk());
    }

    @Test
    void rejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void authEndpointsDoNotExistWhenAuthIsDisabled() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"a@b.co\",\"password\":\"12345678\"}"))
                .andExpect(status().isNotFound());
    }
}
