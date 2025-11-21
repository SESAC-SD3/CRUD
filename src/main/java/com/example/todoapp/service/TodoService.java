package com.example.todoapp.service;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.entity.TodoEntity;
import com.example.todoapp.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoService {

    private static final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoDto createTodo(TodoDto dto) {
        validateTitle(dto.getTitle());

        TodoEntity entity = new TodoEntity(
                dto.getTitle(), dto.getContent(), dto.isCompleted()
        );

        TodoEntity saved = todoRepository.save(entity);

        return toDto(saved);
    }

    public List<TodoDto> getAllTodos(){
        return todoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TodoEntity findEntityById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : id " + id));
    }

    public TodoDto getTodoById(Long id) {
        TodoEntity entity = findEntityById(id);
        return toDto(entity);
    }

    public void deleteTodoById(Long id) {
//        getTodoById(id);
        findEntityById(id);
        todoRepository.deleteById(id);
    }

    public TodoDto updateTodoById(Long id, TodoDto dto) {
        validateTitle(dto.getTitle());

        TodoEntity entity = findEntityById(id);


        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCompleted(dto.isCompleted());

        return toDto(entity);
    }


    public List<TodoDto> searchTodos(String keyword) {
        return todoRepository.findByTitleContaining(keyword).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TodoDto> getTodosByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TodoDto toggleCompleted(Long id) {
        TodoEntity entity = findEntityById(id);
        entity.setCompleted(!entity.isCompleted());
        return toDto(entity);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if (title.length() > 50) {
            throw new IllegalArgumentException("제목은 50자를 넘을 수 없습니다.");
        }
    }

    public long getTotalCount() {
        return todoRepository.findAll().size();
    }

    public long getCompletedCount() {
        return todoRepository.findByCompleted(true).size();
    }

    public long getActiveCount() {
        return todoRepository.findByCompleted(false).size();
    }

    public void deleteCompletedTodos() {
//        todoRepository.findByCompleted(true);
//        todoRepository.deleteCompleted();
        todoRepository.deleteByCompleted(true);

    }

    private TodoDto toDto(TodoEntity entity) {
        TodoDto dto = new TodoDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCompleted(entity.isCompleted());
        return dto;
    }



}
