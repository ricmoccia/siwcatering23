package it.progetto.catering.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.Chef;

public interface ChefRepository  extends CrudRepository<Chef, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndCognomeAndNazionalita(String nome, String cognome, String nazionalita);
	
	public Chef findByNomeAndCognome(String nome, String cognome);
	
	public List<Chef> findByNome(String nome);
	
	public List<Chef> findByCognome(String cognome);
	
	public List<Chef> findByNomeOrCognome (String nome, String cognome);
}
