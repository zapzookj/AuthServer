package com.spring.authserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.authserver.dto.SignupRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void signUpAndLoginTest() throws Exception {
        // GIVEN
        // 1) 회원가입 요청 데이터
        String requestBody =
            """
                {
                  "username": "testuser",
                  "password": "testpassword",
                  "nickname": "TestUser"
                }""";
        // 2) 로그인 요청 데이터
        String requestBody2 =
            """
                {
                  "username": "testuser",
                  "password": "testpassword"
                }""";
        // WHEN & THEN
        // 1) 회원가입 성공
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.nickname").value("TestUser"));
        // 2) 중복된 사용자명으로 회원가입 실패
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error.code").value("ALREADY_EXISTS"));
        // 3) 로그인 성공
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void loginFailureTest() throws Exception {
        // GIVEN
        // 1) 잘못된 로그인 요청 데이터
        String requestBody =
            """
                {
                  "username": "wronguser",
                  "password": "wrongpassword"
                }""";
        // WHEN & THEN
        // 1) 로그인 실패
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void adminSignupAndGrantAdminTest() throws Exception {
        // GIVEN
        // 1) 관리자 회원가입 요청 데이터
        String requestBody =
            """
                {
                  "username": "adminuser",
                  "password": "adminpassword",
                  "nickname": "AdminUser",
                  "adminCode": "123456"
                }""";

        // 2) 일반 사용자 생성
        String userRequestBody =
            """
                {
                  "username": "normaluser",
                  "password": "normalpassword",
                  "nickname": "NormalUser"
                }""";
        String created = mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestBody))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long userId = objectMapper.readTree(created).get("id").asLong();

        // WHEN & THEN
        // 1) 관리자 회원가입 성공
        mockMvc.perform(post("/admin/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("adminuser"))
            .andExpect(jsonPath("$.nickname").value("AdminUser"));
        // 2) 관리자 로그인
        String adminLoginRequestBody =
            """
                {
                  "username": "adminuser",
                  "password": "adminpassword"
                }""";
        String adminToken = objectMapper.readTree(
            mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminLoginRequestBody))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString()
        ).get("token").asText();
        // 3) 관리자 권한 부여 요청 (성공)
        mockMvc.perform(patch("/admin/users/" + userId + "/roles")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.roles", hasItem("ADMIN")))
            .andExpect(jsonPath("$.roles", hasItem("USER")));
        // 4) 관리자 권한 부여 요청 (실패)
        mockMvc.perform(patch("/admin/users/" + userId + "/roles"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code", is("INVALID_TOKEN")));
    }
}
