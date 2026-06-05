package com.demo.enrollment_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// This property prevents the test from trying to connect to the Eureka server!
@SpringBootTest(properties = {"eureka.client.enabled=false"})
class EnrollmentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}