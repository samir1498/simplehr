package com.samir.simplehr.leave;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
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
class LeaveControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRequestAndApproveLeave() throws Exception {
        String payload = """
            {
              "employeeId": "%s",
              "startDate": "2026-07-01",
              "endDate": "2026-07-05"
            }
            """.formatted(UUID.randomUUID());

        String responseBody = mockMvc
            .perform(
                post("/api/v1/leaves")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        String leaveId = responseJson.get("id").asText();

        mockMvc
            .perform(
                patch("/api/v1/leaves/{leaveId}/approve", leaveId)
                    .header("X-Role", "MANAGER")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"reviewerId\":\"manager-1\"}")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));

        mockMvc
            .perform(get("/api/v1/leaves/{leaveId}", leaveId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"))
            .andExpect(jsonPath("$.reviewedBy").value("manager-1"));
    }

    @Test
    void shouldRejectInvalidDateRangeAtApiBoundary() throws Exception {
        String payload = """
            {
              "employeeId": "%s",
              "startDate": "2026-07-05",
              "endDate": "2026-07-01"
            }
            """.formatted(UUID.randomUUID());

        mockMvc
            .perform(
                post("/api/v1/leaves")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.message").value(
                    "dateRangeValid endDate cannot be before startDate"
                )
            );
    }

    @Test
    void shouldRejectLeaveReviewWithoutReasonAtApiBoundary() throws Exception {
        String requestPayload = """
            {
              "employeeId": "%s",
              "startDate": "2026-08-01",
              "endDate": "2026-08-02"
            }
            """.formatted(UUID.randomUUID());

        String responseBody = mockMvc
            .perform(
                post("/api/v1/leaves")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestPayload)
            )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String leaveId = objectMapper.readTree(responseBody).get("id").asText();

        mockMvc
            .perform(
                patch("/api/v1/leaves/{leaveId}/reject", leaveId)
                    .header("X-Role", "MANAGER")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"reviewerId\":\"manager-1\"}")
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.message").value(
                    "rejectionReason rejectionReason is required"
                )
            );
    }
}
