package com.example.todoapp.service;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDto> getAllTodos(){
        return todoRepository.findAll();
    }

    public TodoDto getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : id " + id));
    }

    public void deleteTodoById(Long id) {
        getTodoById(id);
        todoRepository.deleteById(id);
    }

    public TodoDto updateTodoById(Long id, TodoDto newTodo) {
        TodoDto originTodo = getTodoById(id);

        validateTitle(newTodo.getTitle());

        originTodo.setTitle(newTodo.getTitle());
        originTodo.setContent(newTodo.getContent());
        originTodo.setCompleted(newTodo.isCompleted());

        return todoRepository.save(originTodo);
    }

    public TodoDto createTodo(TodoDto todo) {
        validateTitle(todo.getTitle());
        return todoRepository.save(todo);
    }

    public List<TodoDto> searchTodos(String keyword) {
        return todoRepository.findByTitleContaining(keyword);
    }

    public List<TodoDto> getTodosByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    public TodoDto toggleCompleted(Long id) {
        TodoDto todo = getTodoById(id);
        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
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
        todoRepository.deleteCompleted();
    }



}
