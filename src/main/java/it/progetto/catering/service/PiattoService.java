package it.progetto.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.progetto.catering.model.Piatto;
import it.progetto.catering.repository.PiattoRepository;

@Service
public class PiattoService {

	@Autowired
	private PiattoRepository PiattoRepository;

	@Transactional
	public void save(Piatto Piatto) {
		PiattoRepository.save(Piatto);
	}
	
	@Transactional
	public Piatto inserisci (Piatto Piatto) {
		return PiattoRepository.save(Piatto);
	}
	
	public Piatto findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return PiattoRepository.findById(id).get();
	}
	
	public Piatto findByNome (String nome) {
		return PiattoRepository.findByNome(nome);
	}
	
	public List<Piatto> findByIds (List<Long> ids) {
		var i = PiattoRepository.findAllById(ids);
		List<Piatto> listaPiatti = new ArrayList<>(); 
		for(Piatto Piatto : i)
			listaPiatti.add(Piatto);
		return listaPiatti;
	}
	
	public List<Piatto> findAll(){
		List<Piatto> piatti= new ArrayList<>();
		for(Piatto p: PiattoRepository.findAll()) {
			piatti.add(p);
		}
		return piatti;
	}
	
	/*bisogna verificare se uno chef e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Piatto Piatto) {
		return PiattoRepository.existsByNomeAndDescrizione(Piatto.getNome(), Piatto.getDescrizione());		 
	}

	@Transactional
	public void delete(Piatto Piatto) {
		PiattoRepository.delete(Piatto);
	}
	
	public void deletePiattoById(Long id) {
		PiattoRepository.deleteById(id);
	}	
}
