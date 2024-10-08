package com.example.onlinelibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.onlinelibrary.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
