package com.example.todoapp;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoappApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(TodoRepository todoRepository) {
        return args -> {
//            TodoRepository todoRepository = new TodoRepository();
            todoRepository.save(new TodoDto(null, "study", "JAVA", false));
            todoRepository.save(new TodoDto(null, "cook", "kimbob", true));
            todoRepository.save(new TodoDto(null, "workout", "run", false));

        };
    }
}
