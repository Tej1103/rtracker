package com.rtracker.controller;

import com.rtracker.model.Book;
import com.rtracker.model.Book.ReadingStatus;
import com.rtracker.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(@RequestParam(required = false) String status, Model model) {
        if (status != null && !status.isEmpty()) {
            try {
                ReadingStatus readingStatus = ReadingStatus.valueOf(status);
                model.addAttribute("books", bookService.getBooksByStatus(readingStatus));
                model.addAttribute("filterStatus", status);
            } catch (IllegalArgumentException e) {
                model.addAttribute("books", bookService.getAllBooksForCurrentUser());
            }
        } else {
            model.addAttribute("books", bookService.getAllBooksForCurrentUser());
        }

        model.addAttribute("statuses", ReadingStatus.values());
        return "books/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("statuses", ReadingStatus.values());
        return "books/form";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ReadingStatus.values());
            return "books/form";
        }

        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Book book = bookService.getBookById(id);
            model.addAttribute("book", book);
            model.addAttribute("statuses", ReadingStatus.values());
            return "books/form";
        } catch (RuntimeException e) {
            return "redirect:/books?error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ReadingStatus.values());
            return "books/form";
        }

        try {
            bookService.updateBook(id, book);
            return "redirect:/books";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/books?error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return "redirect:/books?deleted";
        } catch (RuntimeException e) {
            return "redirect:/books?error";
        }
    }

    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        try {
            Book book = bookService.getBookById(id);
            model.addAttribute("book", book);
            return "books/view";
        } catch (RuntimeException e) {
            return "redirect:/books?error";
        }
    }
}