package com.example.onlinelibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.onlinelibrary.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
