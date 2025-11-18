package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TodoController {
//    private final TodoRepository todoRepository = new TodoRepository();
    private final TodoRepository todoRepository;


    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/todos")
    public String todos(Model model){
        // 이전에 만들었던 respository와 다른객체를 사용하면 안됨
        // TodoRepository todoRepository = new TodoRepository();
        List<TodoDto> todos = todoRepository.findAll();
        model.addAttribute("todos", todos);
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
//        TodoRepository todoRepository = new TodoRepository();

        TodoDto todo = todoRepository.save(todoDto);
        model.addAttribute("todo", todo);

//        return "create";
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}")
    public String detail(@PathVariable Long id, Model model) {
        TodoDto todo = todoRepository.findById(id);
        model.addAttribute("todo", todo);
        return "detail";
    }

}
