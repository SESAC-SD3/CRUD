package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoController {
//    private final TodoRepository todoRepository = new TodoRepository();
    private final TodoRepository todoRepository;


    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public String todos(Model model){
        // 이전에 만들었던 respository와 다른객체를 사용하면 안됨
        // TodoRepository todoRepository = new TodoRepository();
        List<TodoDto> todos = todoRepository.findAll();
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/new")
    public String newTodo(Model model){
        model.addAttribute("todo", new TodoDto());
        return "form";
    }

//    @GetMapping("/create")
    @PostMapping
    public String create(
//            @RequestParam String title,
//            @RequestParam String content,
            @ModelAttribute TodoDto todo,
            RedirectAttributes redirectAttributes
//            Model model
    ){
//        TodoDto todoDto = new TodoDto(null, title, content, false);
//        TodoRepository todoRepository = new TodoRepository();

//        TodoDto todo = todoRepository.save(todoDto);
        todoRepository.save(todo);
//        model.addAttribute("todo", todo);
        redirectAttributes.addFlashAttribute("message", "할 일이 생성되었습니다.");

//        return "create";
        return "redirect:/todos";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
//        TodoDto todo = todoRepository.findById(id);

        try {
            TodoDto todo = todoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("todo not found!!!!!!"));

            model.addAttribute("todo", todo);
            return "detail";

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Model model) {
        // 삭제로직
        todoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "할일이 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("status", "delete");
        return "redirect:/todos";
    }

    @GetMapping("/{id}/update")
    public String edit(@PathVariable Long id, Model model) {
        try {
            TodoDto todo = todoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("todo not found!!!!!!!!"));
            model.addAttribute("todo", todo);
            return "form";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
//            @RequestParam String title,
//            @RequestParam String content,
//            @RequestParam(defaultValue = "false") Boolean completed,
            @ModelAttribute TodoDto todo,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
//            TodoDto todo = todoRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("todo not found!!!!!!!!"));
//
//            todo.setTitle(title);
//            todo.setContent(content);
//            todo.setCompleted(completed);
            todo.setId(id);
            todoRepository.save(todo);
            redirectAttributes.addFlashAttribute("message", "할 일이 수정되었습니다.");

            return "redirect:/todos/" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", "없는 할일입니다.");
            return "redirect:/todos";
        }

    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<TodoDto> todos = todoRepository.findByTitleContaining(keyword);

        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/active")
    public String active(Model model) {
        List<TodoDto> todos = todoRepository.findByCompleted(false);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/completed")
    public String completed(Model model) {
        List<TodoDto> todos = todoRepository.findByCompleted(true);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, Model model) {
        try {
            TodoDto todo = todoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("todo not found!!!!!!!!"));
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
            return "redirect:/todos/" + id;

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }


}








