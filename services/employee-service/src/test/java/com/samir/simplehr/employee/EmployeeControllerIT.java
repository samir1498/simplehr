package com.samir.simplehr.employee;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndReadEmployee() throws Exception {
        String payload = """
            {
              "fullName": "Samir Bensalem",
              "email": "samir.read@example.com",
              "salary": 180000,
              "hireDate": "2026-03-03"
            }
            """;

        String responseBody = mockMvc
            .perform(
                post("/api/v1/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        String employeeId = responseJson.get("id").asText();

        mockMvc
            .perform(get("/api/v1/employees/{employeeId}", employeeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("samir.read@example.com"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldRejectInvalidEmailAtApiBoundary() throws Exception {
        String payload = """
            {
              "fullName": "Samir Bensalem",
              "email": "invalid-email",
              "salary": 180000,
              "hireDate": "2026-03-03"
            }
            """;

        mockMvc
            .perform(
                post("/api/v1/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.message").value("email email has invalid format")
            );
    }

    @Test
    void shouldReturnConflictForDuplicateEmail() throws Exception {
        String payload = """
            {
              "fullName": "Samir Bensalem",
              "email": "samir.duplicate@example.com",
              "salary": 180000,
              "hireDate": "2026-03-03"
            }
            """;

        mockMvc
            .perform(
                post("/api/v1/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isCreated());

        mockMvc
            .perform(
                post("/api/v1/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isConflict())
            .andExpect(
                jsonPath("$.message").value(
                    "employee already exists with email: samir.duplicate@example.com"
                )
            );
    }
}
