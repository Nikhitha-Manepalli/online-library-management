package com.example.onlinelibrary.controller;

import com.example.onlinelibrary.entity.Publisher;
import com.example.onlinelibrary.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
    void getPublisherById_ShouldReturnPublisher_WhenExists() throws Exception {
        when(publisherService.getPublisherById(1L)).thenReturn(publisher);

        mockMvc.perform(get("/api/publishers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Geetha Publishers"));

        verify(publisherService, times(1)).getPublisherById(1L);
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
    void deletePublisher_ShouldReturnNoContent() throws Exception {
        doNothing().when(publisherService).deletePublisher(1L);

        mockMvc.perform(delete("/api/publishers/1"))
                .andExpect(status().isNoContent());

        verify(publisherService, times(1)).deletePublisher(1L);
    }
}