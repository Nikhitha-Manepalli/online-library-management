package com.example.onlinelibrary.service.impl;

import com.example.onlinelibrary.entity.Publisher;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.PublisherRepository;
import com.example.onlinelibrary.service.PublisherService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository; 

   
    private static final String PUBLISHER_NOT_FOUND_MESSAGE = "Publisher not found with id ";

    
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    @Transactional
    public Publisher createPublisher(Publisher publisher) {
        try {
            return publisherRepository.save(publisher);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("Publisher with this name already exists");
        }
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PUBLISHER_NOT_FOUND_MESSAGE + id));
    }

    @Override
    @Transactional
    public Publisher updatePublisher(Long id, Publisher publisherDetails) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PUBLISHER_NOT_FOUND_MESSAGE + id));
        publisher.setName(publisherDetails.getName());

        try {
            return publisherRepository.save(publisher);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("Publisher with this name already exists");
        }
    }

    @Override
    @Transactional
    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new ResourceNotFoundException(PUBLISHER_NOT_FOUND_MESSAGE + id);
        }
        publisherRepository.deleteById(id);
    }
}