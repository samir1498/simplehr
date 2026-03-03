package com.samir.simplehr.leave;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

		String responseBody = mockMvc.perform(post("/api/v1/leaves")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status").value("PENDING"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		JsonNode responseJson = objectMapper.readTree(responseBody);
		String leaveId = responseJson.get("id").asText();

		mockMvc.perform(patch("/api/v1/leaves/{leaveId}/approve", leaveId)
						.header("X-Role", "MANAGER")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"reviewerId\":\"manager-1\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("APPROVED"));

		mockMvc.perform(get("/api/v1/leaves/{leaveId}", leaveId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("APPROVED"))
				.andExpect(jsonPath("$.reviewedBy").value("manager-1"));
	}
}
