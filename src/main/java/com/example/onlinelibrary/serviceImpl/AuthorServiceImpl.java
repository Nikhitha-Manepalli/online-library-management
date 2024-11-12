
package com.example.onlinelibrary.serviceImpl;
 
import com.example.onlinelibrary.entity.Author;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.AuthorRepository;
import com.example.onlinelibrary.service.AuthorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class AuthorServiceImpl implements AuthorService {
 
    @Autowired
    private AuthorRepository authorRepository;
 
    @Override
    @Transactional
    public Author createAuthor(Author author) {
        try {
            return authorRepository.save(author);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("Author with this name already exists");
        }
    }
 
    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
 
    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }
 
    @Override
    @Transactional
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        author.setName(authorDetails.getName());
 
        try {
            return authorRepository.save(author);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("Author with this name already exists");
        }
    }
 
    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id " + id);
        }
        authorRepository.deleteById(id);
    }
}