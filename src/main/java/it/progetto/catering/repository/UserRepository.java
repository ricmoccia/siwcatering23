package it.progetto.catering.repository;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}