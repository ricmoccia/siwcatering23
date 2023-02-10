package it.progetto.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.progetto.catering.model.Chef;
import it.progetto.catering.repository.ChefRepository;

@Service
public class ChefService {

	@Autowired
	private ChefRepository chefRepository;

	@Transactional
	public void save(Chef chef) {
		chefRepository.save(chef);
	}
	
	@Transactional
	public Chef inserisci (Chef chef) {
		return chefRepository.save(chef);
	}
	
	public Chef findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return chefRepository.findById(id).get();
	}
	
	public List<Chef> findChefByNomeOrCognome (String nome, String cognome) {
		return chefRepository.findByNomeOrCognome(nome, cognome);
	}
	
	public Chef findChefByNomeAndCognome (String nome, String cognome) {
		return chefRepository.findByNomeAndCognome(nome, cognome);
	}
	
	public List<Chef> findAll(){
		List<Chef> chefs= new ArrayList<>();
		for(Chef c: chefRepository.findAll()) {
			chefs.add(c);
		}
		return chefs;
	}
	
	/*bisogna verificare se uno chef e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Chef chef) {
		return chefRepository.existsByNomeAndCognomeAndNazionalita(chef.getNome(), chef.getCognome(), chef.getNazionalita());		 
	}

	@Transactional
	public void delete(Chef chef) {
		chefRepository.delete(chef);
	}
	
	public void deleteChefById(Long id) {
		chefRepository.deleteById(id);
	}	
	
	public long contaChef() {
		return this.chefRepository.count();
	}

	public long count() {
		return this.chefRepository.count();
	}


}
