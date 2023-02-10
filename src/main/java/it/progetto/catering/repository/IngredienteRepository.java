package it.progetto.catering.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.Materiale;

public interface IngredienteRepository  extends CrudRepository<Materiale, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndOrigineAndDescrizione(String nome, String origine, String descrizione);
	
	public List<Materiale> findByNomeOrOrigine (String nome, String origine);

	public List<Materiale> findByNome (String nome);

	public List<Materiale> findByOrigine (String origine);
}
