package com.samir.simplehr.leave;

import org.springframework.boot.SpringApplication;

public class TestLeaveServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(LeaveServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
