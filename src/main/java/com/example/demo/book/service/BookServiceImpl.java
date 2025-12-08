package com.example.demo.book.service;

import com.example.demo.book.dto.BookListResponse;
import com.example.demo.book.dto.BookResponse;
// Note: BookRequest is removed as it's only used by non-Read methods
import com.example.demo.book.entity.Book;
import com.example.demo.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
        private final BookRepository bookRepository;

        @Override
        public List<Book> getAllBooks() {
                return bookRepository.findAll();
        }

        @Override
        public Book getBook(Long id) {
                return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        }

        @Override
        public Book createBook(Book book) {
                return bookRepository.save(book);
        }
}
