package com.samir.simplehr.employee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class EmployeeServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
