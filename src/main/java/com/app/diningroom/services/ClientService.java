package com.app.diningroom.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional
    public Client create(ClientDTO clientDTO) {
        if(this.checkEmptyFields(clientDTO)) {
            throw new IllegalArgumentException("Nem todos os campos foram preenchidos.");
        }

        Client newClient = new Client();
        newClient.setName(clientDTO.getName());
        newClient.setAddress(clientDTO.getAddress());
        newClient.setEmail(clientDTO.getEmail());
        newClient.setPhone(clientDTO.getPhone());
        return repository.save(newClient);
    }

    @Transactional
    public void delete(Long id) {
        ClientDTO clientDTO = this.findById(id);
        repository.deleteById(clientDTO.getId());
    }

    @Transactional
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {

        Client clientToUpdate = repository.findById(clientId).get();
        BeanUtils.copyProperties(clientDTO, clientToUpdate, this.getNullPropertyNames(clientDTO));
        Client clientUpdated = repository.save(clientToUpdate);

        return new ClientDTO(clientUpdated);
    }

    @Transactional
    public List<ClientDTO> findAll() {
        List<Client> clients = repository.findAll();

        return clients.stream()
                .map(this::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Transactional
    public Client findEntityById(Long id) {
        return repository.findById(id).get();
    }

    private ClientDTO clientToClientDTO(Client client) {
        return new ClientDTO(client);
    }

    private Boolean checkEmptyFields(ClientDTO clientDTO) {
        return clientDTO.getName() == null || clientDTO.getName().isEmpty()
            || clientDTO.getAddress() == null || clientDTO.getAddress().isEmpty()
            || clientDTO.getEmail() == null || clientDTO.getEmail().isEmpty()
            || clientDTO.getPhone() == null || clientDTO.getPhone().isEmpty();
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
