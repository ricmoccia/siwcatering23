package it.progetto.catering.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
	
	public Optional<Credentials> findByUsername(String username);

}