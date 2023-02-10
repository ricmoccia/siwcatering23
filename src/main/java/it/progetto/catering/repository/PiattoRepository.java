package it.progetto.catering.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.Materiale;
import it.progetto.catering.model.Attivita;

public interface PiattoRepository  extends CrudRepository<Attivita, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndDescrizione(String nome, String descrizione);
	
	public Attivita findByNome(String nome);

	public List<Attivita> findByIngredientiContaining(Materiale i);
	
}
