package com.example.onlinelibrary.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.onlinelibrary.entity.Publisher;
import com.example.onlinelibrary.repository.PublisherRepository;
import com.example.onlinelibrary.service.PublisherService;

@Service
public class PublisherServiceImpl  implements PublisherService{

	@Autowired
	private PublisherRepository publisherRepository;
	
	@Override
	public Publisher createPublisher(Publisher publisher) {
		return publisherRepository.save(publisher);
	}

	@Override
	public List<Publisher> getAllPublishers() {
		return publisherRepository.findAll();
	}

	@Override
	public Publisher getPublisherById(Long id) {
		return publisherRepository.findById(id).orElse(null);
	}

	@Override
	public Publisher updatePublisher(Long id, Publisher publisherDetails) {
		Publisher publisher = publisherRepository.findById(id).orElse(null);
	    if (publisher != null) {
	        publisher.setName(publisherDetails.getName());
	        return publisherRepository.save(publisher);
	    }
	    return null;
	}

	@Override
	public void deletePublisher(Long id) {
		 publisherRepository.deleteById(id);
		
	}
	

}

