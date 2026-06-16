package com.demo.enrollment_service.clients;

import com.demo.enrollment_service.dto.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CourseClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<CourseDTO> getCourseById(String courseId) {
        return webClientBuilder.build().get()
                .uri("http://COURSE-SERVICE/api/courses/" + courseId)
                .retrieve()
                .bodyToMono(CourseDTO.class);
    }
}