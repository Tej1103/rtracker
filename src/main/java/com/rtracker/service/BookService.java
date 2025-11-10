package com.rtracker.service;

import com.rtracker.model.Book;
import com.rtracker.model.Book.ReadingStatus;
import com.rtracker.model.User;
import com.rtracker.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    public List<Book> getAllBooksForCurrentUser() {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        return bookRepository.findByUserIdOrderByStartDateDesc(user.getId());
    }

    public List<Book> getBooksByStatus(ReadingStatus status) {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        return bookRepository.findByUserIdAndStatus(user.getId(), status);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book saveBook(Book book) {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }

        book.setUser(user);

        // Auto-set dates based on status
        if (book.getStatus() == ReadingStatus.READING && book.getStartDate() == null) {
            book.setStartDate(LocalDate.now());
        }
        if (book.getStatus() == ReadingStatus.COMPLETED && book.getFinishDate() == null) {
            book.setFinishDate(LocalDate.now());
            book.setCurrentPage(book.getTotalPages());
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        User currentUser = userService.getCurrentUser();

        if (!book.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized to delete this book");
        }

        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);
        User currentUser = userService.getCurrentUser();

        if (!existingBook.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized to update this book");
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setDescription(updatedBook.getDescription());
        existingBook.setStatus(updatedBook.getStatus());
        existingBook.setTotalPages(updatedBook.getTotalPages());
        existingBook.setCurrentPage(updatedBook.getCurrentPage());
        existingBook.setRating(updatedBook.getRating());
        existingBook.setStartDate(updatedBook.getStartDate());
        existingBook.setFinishDate(updatedBook.getFinishDate());
        existingBook.setNotes(updatedBook.getNotes());

        return bookRepository.save(existingBook);
    }
}
