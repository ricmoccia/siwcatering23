package it.progetto.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.progetto.catering.model.Materiale;
import it.progetto.catering.repository.IngredienteRepository;

@Service
public class IngredienteService {

	@Autowired
	private IngredienteRepository ingredienteRepository;

	@Transactional
	public void save(Materiale ingrediente) {
		ingredienteRepository.save(ingrediente);
	}
	
	@Transactional
	public Materiale inserisci (Materiale ingrediente) {
		return ingredienteRepository.save(ingrediente);
	}
	
	public Materiale findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return ingredienteRepository.findById(id).get();
	}
	
	public List<Materiale> findByNomeOrOrigine (String nome, String origine) {
		return ingredienteRepository.findByNomeOrOrigine(nome, origine);
	}
	
	public List<Materiale> findAll(){
		List<Materiale> ingredienti= new ArrayList<>();
		for(Materiale i: ingredienteRepository.findAll()) {
			ingredienti.add(i);
		}
		return ingredienti;
	}
	
	/*bisogna verificare se uno chef e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Materiale ingrediente) {
		return ingredienteRepository.existsByNomeAndOrigineAndDescrizione(ingrediente.getNome(), ingrediente.getOrigine(), ingrediente.getDescrizione());		 
	}

	@Transactional
	public void delete(Materiale ingrediente) {
		ingredienteRepository.delete(ingrediente);
	}
	
	public void deleteIngredienteById(Long id) {
		ingredienteRepository.deleteById(id);
	}

	public void deleteById(Long id) {
		this.ingredienteRepository.deleteById(id);		
	}	

}
