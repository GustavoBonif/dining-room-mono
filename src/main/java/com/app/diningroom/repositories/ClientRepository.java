package com.app.diningroom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.diningroom.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
