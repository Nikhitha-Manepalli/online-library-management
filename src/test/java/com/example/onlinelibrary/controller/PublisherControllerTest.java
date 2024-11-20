package com.example.onlinelibrary.controller;
 
import com.example.onlinelibrary.entity.Publisher;
import com.example.onlinelibrary.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import java.util.Collections;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
class PublisherControllerTest {
 
    @Mock
    private PublisherService publisherService;
 
    @InjectMocks
    private PublisherController publisherController;
 
    private MockMvc mockMvc;
 
    private Publisher publisher;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController).build();
        publisher = new Publisher(1L, "Geetha Publishers", Collections.emptySet());
    }
 
    @Test
    void createPublisher_ShouldReturnCreatedPublisher() throws Exception {
        when(publisherService.createPublisher(any(Publisher.class))).thenReturn(publisher);
 
        mockMvc.perform(post("/api/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Geetha Publishers\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Geetha Publishers"));
 
        verify(publisherService, times(1)).createPublisher(any(Publisher.class));
    }
 
    @Test
    void getAllPublishers_ShouldReturnListOfPublishers() throws Exception {
        when(publisherService.getAllPublishers()).thenReturn(Collections.singletonList(publisher));
 
        mockMvc.perform(get("/api/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Geetha Publishers"));
 
        verify(publisherService, times(1)).getAllPublishers();
    }
 
    @Test
    void testGetPublisherById_Found() {
        Publisher mockPublisher = new Publisher();
        mockPublisher.setId(1L);
        when(publisherService.getPublisherById(1L)).thenReturn(mockPublisher);
 
        ResponseEntity<Publisher> response = publisherController.getPublisherById(1L);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPublisher, response.getBody());
    }
 
    @Test
    void testGetPublisherById_NotFound() {
        when(publisherService.getPublisherById(1L)).thenReturn(null);
 
        ResponseEntity<Publisher> response = publisherController.getPublisherById(1L);
 
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
 
    @Test
    void testUpdatePublisher_Success() {
        Publisher existingPublisher = new Publisher();
        existingPublisher.setId(1L);
        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setId(1L);
        updatedPublisher.setName("Updated Name");
 
        when(publisherService.updatePublisher(eq(1L), any(Publisher.class))).thenReturn(updatedPublisher);
 
        ResponseEntity<Publisher> response = publisherController.updatePublisher(1L, updatedPublisher);
 
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPublisher, response.getBody());
    }
 
    @Test
    void testUpdatePublisher_NotFound() {
        when(publisherService.updatePublisher(eq(1L), any(Publisher.class))).thenReturn(null);
 
        ResponseEntity<Publisher> response = publisherController.updatePublisher(1L, new Publisher());
 
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
 
    @Test
    void updatePublisher_ShouldReturnUpdatedPublisher() throws Exception {
        Publisher updatedPublisher = new Publisher(1L, "The Indian Publisher", Collections.emptySet());
        when(publisherService.updatePublisher(eq(1L), any(Publisher.class))).thenReturn(updatedPublisher);
 
        mockMvc.perform(put("/api/publishers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"The Indian Publisher\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("The Indian Publisher"));
 
        verify(publisherService, times(1)).updatePublisher(eq(1L), any(Publisher.class));
    }
 
    @Test
    void updatePublisher_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        when(publisherService.updatePublisher(eq(1L), any(Publisher.class))).thenReturn(null);
 
        mockMvc.perform(put("/api/publishers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Non Existent Publisher\"}"))
                .andExpect(status().isNotFound());
 
        verify(publisherService, times(1)).updatePublisher(eq(1L), any(Publisher.class));
    }
 
    @Test
    void deletePublisher_ShouldReturnNoContent() throws Exception {
        doNothing().when(publisherService).deletePublisher(1L);
 
        mockMvc.perform(delete("/api/publishers/1"))
                .andExpect(status().isNoContent());
 
        verify(publisherService, times(1)).deletePublisher(1L);
    }
}