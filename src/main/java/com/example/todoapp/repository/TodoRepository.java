package com.example.todoapp.repository;

import com.example.todoapp.dto.TodoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TodoRepository {
    private final Map<Long, TodoDto> storage = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    public TodoDto save(TodoDto todo) {
        todo.setId(nextId++);
        storage.put(todo.getId(), todo);
        return todo;
    }

    public List<TodoDto> findAll() {
        return new ArrayList<>(storage.values());
    }

    public TodoDto findById(Long id) {
        return storage.get(id);
    }

}
