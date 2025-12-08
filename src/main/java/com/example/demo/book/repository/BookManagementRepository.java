package com.example.demo.book.repository;

import com.example.demo.book.entity.BookManagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookManagementRepository extends JpaRepository<BookManagement, Long> {
    Optional<BookManagement> findFirstByBookIdAndIsLoanedFalse(Long bookId);
}
