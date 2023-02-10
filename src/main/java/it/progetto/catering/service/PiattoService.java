package it.progetto.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.progetto.catering.model.Attivita;
import it.progetto.catering.repository.PiattoRepository;

@Service
public class PiattoService {

	@Autowired
	private PiattoRepository piattoRepository;

	@Transactional
	public void save(Attivita piatto) {
		piattoRepository.save(piatto);
	}
	
	@Transactional
	public Attivita inserisci (Attivita piatto) {
		return piattoRepository.save(piatto);
	}
	
	public Attivita findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return piattoRepository.findById(id).get();
	}
	
	public Attivita findByNome (String nome) {
		return piattoRepository.findByNome(nome);
	}
	
	public List<Attivita> findByIds (List<Long> ids) {
		var i = piattoRepository.findAllById(ids);
		List<Attivita> listaPiatti = new ArrayList<>(); 
		for(Attivita piatto : i)
			listaPiatti.add(piatto);
		return listaPiatti;
	}
	
	public List<Attivita> findAll(){
		List<Attivita> piatti= new ArrayList<>();
		for(Attivita p: piattoRepository.findAll()) {
			piatti.add(p);
		}
		return piatti;
	}
	
	/*bisogna verificare se uno chef e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Attivita piatto) {
		return piattoRepository.existsByNomeAndDescrizione(piatto.getNome(), piatto.getDescrizione());		 
	}

	@Transactional
	public void delete(Attivita piatto) {
		piattoRepository.delete(piatto);
	}
	
	public void deletePiattoById(Long id) {
		piattoRepository.deleteById(id);
	}	
}
