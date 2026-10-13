package com.santiagomac.template;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.auth.enabled=true",
        "app.auth.jwt.secret=test-secret-test-secret-test-secret-123456",
        "app.auth.cookie.secure=false",
        "spring.datasource.url=jdbc:h2:mem:authdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthEnabledTests {

    private static final String CREDENTIALS = "{\"email\":\"Jane@Example.com\",\"password\":\"s3cret-pass\"}";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void protectedEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/products")).andExpect(status().isUnauthorized());
    }

    @Test
    void signupLoginAccessRefreshAndLogout() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content(CREDENTIALS))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content(CREDENTIALS))
                .andExpect(status().isConflict());

        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(CREDENTIALS))
                .andExpect(status().isOk()).andReturn();
        Cookie access = login.getResponse().getCookie("access_token");
        Cookie refresh = login.getResponse().getCookie("refresh_token");
        assertThat(access).isNotNull();
        assertThat(refresh).isNotNull();

        mockMvc.perform(get("/api/v1/products").cookie(access)).andExpect(status().isOk());

        // a refresh token is not accepted as an access token
        mockMvc.perform(get("/api/v1/products").header("Authorization", "Bearer " + refresh.getValue()))
                .andExpect(status().isUnauthorized());

        // refresh rotates the token: the previous one stops working
        MvcResult refreshed = mockMvc.perform(post("/api/v1/auth/refreshToken").cookie(refresh))
                .andExpect(status().isOk()).andReturn();
        Cookie newRefresh = refreshed.getResponse().getCookie("refresh_token");
        assertThat(newRefresh.getValue()).isNotEqualTo(refresh.getValue());
        mockMvc.perform(post("/api/v1/auth/refreshToken").cookie(refresh)).andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/auth/logout").cookie(newRefresh)).andExpect(status().isNoContent());
        mockMvc.perform(post("/api/v1/auth/refreshToken").cookie(newRefresh)).andExpect(status().isUnauthorized());
    }

    @Test
    void wrongPasswordReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nobody@example.com\",\"password\":\"whatever-123\"}"))
                .andExpect(status().isUnauthorized());
    }
}
