package com.example.demo.book.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class BookResponse {

    private Long bookNo;
    private String title;
    private String author;
    private String publisher;
    private String coverImageUrl;
    private Integer price;
    private LocalDate registerDate;

    private String summary;

}