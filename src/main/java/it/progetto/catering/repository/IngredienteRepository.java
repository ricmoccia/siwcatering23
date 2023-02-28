package it.progetto.catering.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.Ingrediente;

public interface IngredienteRepository  extends CrudRepository<Ingrediente, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndOrigineAndDescrizione(String nome, String origine, String descrizione);
	
	public List<Ingrediente> findByNomeOrOrigine (String nome, String origine);

	public List<Ingrediente> findByNome (String nome);

	public List<Ingrediente> findByOrigine (String origine);
}
