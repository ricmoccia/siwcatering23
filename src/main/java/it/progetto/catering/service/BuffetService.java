package it.progetto.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.progetto.catering.model.Buffet;
import it.progetto.catering.model.Chef;
import it.progetto.catering.repository.BuffetRepository;

@Service
public class BuffetService {

	@Autowired
	private BuffetRepository buffetRepository;

	@Transactional
	public void save(Buffet buffet) {
		buffetRepository.save(buffet);
	}
	
	@Transactional
	public Buffet inserisci (Buffet buffet) {
		return buffetRepository.save(buffet);
	}
	
	public Buffet findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return buffetRepository.findById(id).get();
	}
	
	public Buffet findByNome (String nome) {
		return buffetRepository.findByNome(nome);
	}
	
	public List<Buffet> findByChef (Chef chef) {
		return buffetRepository.findByChef(chef);
	}

	
	public List<Buffet> findAll(){
		List<Buffet> buffet= new ArrayList<>();
		for(Buffet b: buffetRepository.findAll()) {
			buffet.add(b);
		}
		return buffet;
	}
	
	/*bisogna verificare se uno chef e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Buffet buffet) {
		return buffetRepository.existsByNomeAndDescrizione(buffet.getNome(), buffet.getDescrizione());		 
	}

	@Transactional
	public void delete(Buffet chef) {
		buffetRepository.delete(chef);
	}
	
	@Transactional
	public void deleteBuffetById (Long id) {
		buffetRepository.deleteById(id);
	}
	
	public void deleteChefById(Long id) {
		buffetRepository.deleteById(id);
	}	
}
