package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String todos(Model model){
        List<TodoDto> todos = todoService.getAllTodos();
        model.addAttribute("todos", todos);
        model.addAttribute("totalCount", todoService.getTotalCount());
        model.addAttribute("completedCount", todoService.getCompletedCount());
        model.addAttribute("activeCount", todoService.getActiveCount());
        return "todos";
    }

    @GetMapping("/new")
    public String newTodo(Model model){
        model.addAttribute("todo", new TodoDto());
        return "form";
    }

    @PostMapping
    public String create(
            @ModelAttribute TodoDto todo,
            RedirectAttributes redirectAttributes
    ){
        try {
            todoService.createTodo(todo);
            redirectAttributes.addFlashAttribute("message", "할 일이 생성되었습니다.");

            return "redirect:/todos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/todos/new";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            TodoDto todo = todoService.getTodoById(id);
            model.addAttribute("todo", todo);
            return "detail";

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ){
        todoService.deleteTodoById(id);
        redirectAttributes.addFlashAttribute("message", "할일이 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("status", "delete");
        return "redirect:/todos";
    }

    @GetMapping("/{id}/update")
    public String edit(@PathVariable Long id, Model model) {
        try {
            TodoDto todo = todoService.getTodoById(id);
            model.addAttribute("todo", todo);
            return "form";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
            @ModelAttribute TodoDto todo,
            RedirectAttributes redirectAttributes
    ){
        try {
            todoService.updateTodoById(id, todo);
            redirectAttributes.addFlashAttribute("message", "할 일이 수정되었습니다.");
            return "redirect:/todos/" + id;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("제목")) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/todos/" + id +"/update";
            } else {
                redirectAttributes.addFlashAttribute("message", "없는 할일입니다.");
                return "redirect:/todos";
            }

        }
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<TodoDto> todos = todoService.searchTodos(keyword);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/active")
    public String active(Model model) {
        List<TodoDto> todos = todoService.getTodosByCompleted(false);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/completed")
    public String completed(Model model) {
        List<TodoDto> todos = todoService.getTodosByCompleted(false);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        try {
            todoService.toggleCompleted(id);
            return "redirect:/todos/" + id;

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    // 1. 제목 검증 추가
    //  - 제목이 비어있으면 예외
    //  - 제목이 50자 초과시 예외

    // 2. 통계 기능 추가
    //  - 전체, 완료된, 미완료 할일 개수 => /todos 에 표시

    // 3. 완료된 할일 일괄 삭제

    @GetMapping("/delete-completed")
    public String deleteCompleted(RedirectAttributes redirectAttributes) {
        // delete all
        todoService.deleteCompletedTodos();
        redirectAttributes.addFlashAttribute("message", "완료된 할일 삭제");
        return "redirect:/todos";
    }


}








