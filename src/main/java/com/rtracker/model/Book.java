package com.rtracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus status = ReadingStatus.TO_READ;

    @Min(value = 1, message = "Total pages must be at least 1")
    @Column(nullable = false)
    private Integer totalPages;

    @Min(value = 0, message = "Current page cannot be negative")
    private Integer currentPage = 0;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Column(length = 1000)
    private String notes;

    private String isbn;

    private String publisher;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum ReadingStatus {
        TO_READ,
        READING,
        COMPLETED,
        DNF
    }
}