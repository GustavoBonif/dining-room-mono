package com.app.diningroom.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.app.diningroom.dto.ClientDTO;
import com.app.diningroom.entities.Client;
import com.app.diningroom.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping(value = "/{id}")
    public ClientDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping()
    public ResponseEntity<String> createClient(@RequestBody ClientDTO clientDTO) {
        try {
            Client clientCreated = service.create(clientDTO);
            return new ResponseEntity<>("Sucesso ao criar o cliente: " + clientCreated.getName() + " - " + clientCreated.getId(), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>("Erro ao criar o cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>("Sucesso ao deletar o cliente" , HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/listAll")
    public List<ClientDTO> listAllBrands() {
        return service.findAll();
    }

    @PatchMapping(value = "/{clientId}")
    public ResponseEntity<String> updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO) {
        try {
            ClientDTO clientUpdatedDTO = service.updateClient(clientId, clientDTO);
            return new ResponseEntity<>("Cliente atualizada com sucesso. ID: " + clientUpdatedDTO.getId(), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Erro ao atualizar a cliente: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao atualizar a cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
