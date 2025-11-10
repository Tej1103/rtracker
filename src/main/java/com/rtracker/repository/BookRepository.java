package com.rtracker.repository;

import com.rtracker.model.Book;
import com.rtracker.model.Book.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUserId(Long userId);
    List<Book> findByUserIdAndStatus(Long userId, ReadingStatus status);
    List<Book> findByUserIdOrderByStartDateDesc(Long userId);
}