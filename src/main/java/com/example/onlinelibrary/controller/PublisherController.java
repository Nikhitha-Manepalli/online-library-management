package com.example.onlinelibrary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlinelibrary.entity.Publisher;
import com.example.onlinelibrary.service.PublisherService;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

	    @Autowired
	    private PublisherService publisherService;
	 
	    @PostMapping
	    public Publisher createPublisher(@RequestBody Publisher publisher) {
	        return publisherService.createPublisher(publisher);
	    }
	 
	    @GetMapping
	    public List<Publisher> getAllPublishers() {
	        return publisherService.getAllPublishers();
	    }
	 
	    @GetMapping("/{id}")
	    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
	        Publisher publisher = publisherService.getPublisherById(id);
	        if (publisher != null) {
	            return ResponseEntity.ok(publisher);
	        }
	        return ResponseEntity.notFound().build();
	    }
	 
	    @PutMapping("/{id}")
	    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @RequestBody Publisher publisherDetails) {
	        Publisher updatedPublisher = publisherService.updatePublisher(id, publisherDetails);
	        if (updatedPublisher != null) {
	            return ResponseEntity.ok(updatedPublisher);
	        }
	        return ResponseEntity.notFound().build();
	    }
	 
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
	        publisherService.deletePublisher(id);
	        return ResponseEntity.noContent().build();
	    }
}
