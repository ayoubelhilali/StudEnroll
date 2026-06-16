package com.demo.enrollment_service.clients;

import com.demo.enrollment_service.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StudentClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<StudentDTO> getStudentByCnie(String cnie) {
        return webClientBuilder.build().get()
                .uri("http://STUDENT-SERVICE/api/students/cnie/" + cnie)
                .retrieve()
                .bodyToMono(StudentDTO.class);
    }
}