package it.progetto.catering.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.progetto.catering.model.Ingrediente;
import it.progetto.catering.model.Piatto;

public interface PiattoRepository  extends CrudRepository<Piatto, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndDescrizione(String nome, String descrizione);
	
	public Piatto findByNome(String nome);

	public List<Piatto> findByIngredientiContaining(Ingrediente i);
	
}
