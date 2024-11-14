package com.example.onlinelibrary.serviceImpl;

import com.example.onlinelibrary.entity.Publisher;
import com.example.onlinelibrary.exception.DataIntegrityException;
import com.example.onlinelibrary.exception.ResourceNotFoundException;
import com.example.onlinelibrary.repository.PublisherRepository;
import com.example.onlinelibrary.service.impl.PublisherServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("O'Reilly Media");
    }

    @Test
    void createPublisher_ShouldReturnPublisher_WhenPublisherIsValid() {
        when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);

        Publisher createdPublisher = publisherService.createPublisher(publisher);

        assertNotNull(createdPublisher);
        assertEquals("O'Reilly Media", createdPublisher.getName());
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    void createPublisher_ShouldThrowDataIntegrityException_WhenPublisherAlreadyExists() {
        when(publisherRepository.save(any(Publisher.class))).thenThrow(new DataIntegrityViolationException(""));

        Exception exception = assertThrows(DataIntegrityException.class, () -> {
            publisherService.createPublisher(publisher);
        });

        assertEquals("Publisher with this name already exists", exception.getMessage());
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    void getAllPublishers_ShouldReturnListOfPublishers() {
        when(publisherRepository.findAll()).thenReturn(List.of(publisher));

        List<Publisher> publishers = publisherService.getAllPublishers();

        assertNotNull(publishers);
        assertEquals(1, publishers.size());
        assertEquals("O'Reilly Media", publishers.get(0).getName());
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    void getPublisherById_ShouldReturnPublisher_WhenExists() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        Publisher foundPublisher = publisherService.getPublisherById(1L);

        assertNotNull(foundPublisher);
        assertEquals("O'Reilly Media", foundPublisher.getName());
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    void getPublisherById_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            publisherService.getPublisherById(1L);
        });

        assertEquals("Publisher not found with id 1", exception.getMessage());
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    void updatePublisher_ShouldReturnUpdatedPublisher_WhenExists() {
        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setName("New Publisher Name");

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(updatedPublisher);

        Publisher result = publisherService.updatePublisher(1L, updatedPublisher);

        assertNotNull(result);
        assertEquals("New Publisher Name", result.getName());
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void updatePublisher_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            publisherService.updatePublisher(1L, publisher);
        });

        assertEquals("Publisher not found with id 1", exception.getMessage());
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    void updatePublisher_ShouldThrowDataIntegrityException_WhenPublisherAlreadyExists() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any(Publisher.class))).thenThrow(new DataIntegrityViolationException(""));

        Exception exception = assertThrows(DataIntegrityException.class, () -> {
            publisherService.updatePublisher(1L, publisher);
        });

        assertEquals("Publisher with this name already exists", exception.getMessage());
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void deletePublisher_ShouldDeletePublisher_WhenExists() {
        when(publisherRepository.existsById(1L)).thenReturn(true);

        publisherService.deletePublisher(1L);

        verify(publisherRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePublisher_ShouldThrowResourceNotFoundException_WhenDoesNotExist() {
        when(publisherRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            publisherService.deletePublisher(1L);
        });

        assertEquals("Publisher not found with id 1", exception.getMessage());
        verify(publisherRepository, never()).deleteById(any(Long.class));
    }
}