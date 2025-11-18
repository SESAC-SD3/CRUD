package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

    @GetMapping("/todos")
    public String todos(){
        return "todos";
    }

    @GetMapping("/todos/new")
    public String newTodo(){
        return "new";
    }

    @GetMapping("/todos/create")
    public String create(
            @RequestParam String title,
            @RequestParam String content,
            Model model
    ){
        TodoDto todoDto = new TodoDto(null, title, content, false);
        TodoRepository todoRepository = new TodoRepository();

        TodoDto todo = todoRepository.save(todoDto);
        model.addAttribute("todo", todo);

        return "create";
    }

}
