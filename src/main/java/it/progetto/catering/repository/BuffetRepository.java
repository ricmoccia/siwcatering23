package it.progetto.catering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.Buffet;
import it.progetto.catering.model.Chef;

public interface BuffetRepository  extends CrudRepository<Buffet, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndDescrizione(String nome, String descrizione);
	
	public Buffet findByNome(String nome);
	
	/*trova i buffet di un determinato chef*/
	public List<Buffet> findByChef (Chef chef);
}
