package com.app.diningroom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.diningroom.dto.ClientDTO;
import com.app.diningroom.entities.Client;
import com.app.diningroom.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    public ClientDTO findById(Long id) {

        Client entity = repository.findById(id).get();
        ClientDTO dto = new ClientDTO(entity);
        return dto;
    }

}
