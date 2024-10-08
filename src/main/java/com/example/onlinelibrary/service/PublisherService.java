package com.example.onlinelibrary.service;

import java.util.List;

import com.example.onlinelibrary.entity.Publisher;

public interface PublisherService {
	
	   Publisher createPublisher(Publisher publisher);
	    List<Publisher> getAllPublishers();
	    Publisher getPublisherById(Long id);
	    Publisher updatePublisher(Long id, Publisher publisherDetails);
	    void deletePublisher(Long id);

}
