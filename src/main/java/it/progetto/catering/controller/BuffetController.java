package it.progetto.catering.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.progetto.catering.controller.validator.BuffetValidator;
import it.progetto.catering.model.Buffet;
import it.progetto.catering.model.Chef;
import it.progetto.catering.model.Piatto;
import it.progetto.catering.service.BuffetService;
import it.progetto.catering.service.ChefService;
import it.progetto.catering.service.PiattoService;

@Controller
public class BuffetController {

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private PiattoService PiattoService;

	@Autowired
	private ChefService chefService;

	@Autowired
	private BuffetValidator buffetValidator;	




	@GetMapping("/buffet/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = buffetService.findById(id);
		model.addAttribute("buffet", buffet);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo buffet
		Chef chef= buffet.getChef();
		model.addAttribute("chef", chef);//metto nel model lo chef che ha preparato il buffet
		List<Piatto> listaPiatti=buffet.getPiatti();
		model.addAttribute("listaPiatti", listaPiatti); //metto nel model la lista di piatti nel buffet
		return "buffet.html"; //la vista successiva mostra i dettagli del buffet
	}

	@GetMapping("/buffet")
	public String getAllBuffets(Model model) {
		List<Buffet> buffets = buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "buffets.html";
	}







	/*metodi di BuffetController relativi all'admin*/


	@GetMapping("/admin/buffet/{id}")
	public String getBuffetAdmin(@PathVariable("id") Long id, Model model) {
		Buffet buffet = buffetService.findById(id);
		model.addAttribute("buffet", buffet);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo buffet
		Chef chef= buffet.getChef();
		model.addAttribute("chef", chef);//metto nel model lo chef che ha preparato il buffet
		List<Piatto> listaPiatti=buffet.getPiatti();
		model.addAttribute("listaPiatti", listaPiatti); //metto nel model la lista di piatti nel buffet
		return "admin/buffet.html"; //la vista successiva mostra i dettagli del buffet
	}

	@GetMapping("/admin/buffet")
	public String getAllBuffetsAdmin(Model model) {
		List<Buffet> buffets = buffetService.findAll();
		model.addAttribute("buffets", buffets);
		return "admin/buffets.html";
	}


	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg buffet appena creato*/
	@GetMapping("/admin/buffetForm")
	public String getBuffetFormAdmin(Model model) {
		//in questo modo buffetForm ha un ogg Buffet a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("buffet", new Buffet());
		return "admin/buffetForm.html"; 		
	}	

	@PostMapping("/admin/buffet")
	public String addBuffetAdmin(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		buffetValidator.validate(buffet, bindingResult);//se il buffet che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. buffet dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			buffetService.save(buffet);
			model.addAttribute("buffet", buffet);
			return "admin/buffet.html";
		}
		else {
			model.addAttribute("listaPiatti", PiattoService.findAll());
			model.addAttribute("listaChef", chefService.findAll());
			return "admin/buffetForm.html";  //altrimenti ritorna alla pagina della form
		}		
	}

	@Transactional
	@PostMapping("/admin/buffetEdited/{id}")
	public String editBuffetAdmin(@PathVariable Long id, @Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {//se non ci sono errori di validazione
			Buffet vecchioBuffet = buffetService.findById(id);
			vecchioBuffet.setNome(buffet.getNome());
			vecchioBuffet.setDescrizione(buffet.getDescrizione());
			vecchioBuffet.setChef(buffet.getChef());
			vecchioBuffet.setPiatti(buffet.getPiatti());
			this.buffetService.save(vecchioBuffet);
			model.addAttribute("buffet", buffet);
			List<Piatto> listaPiatti=buffet.getPiatti();
			model.addAttribute("listaPiatti", listaPiatti); //metto nel model la lista di piatti nel buffet
			return "admin/buffet.html"; //pagina con buffet appena modificato
		} 
		else {
			model.addAttribute("listaPiatti", PiattoService.findAll());
			model.addAttribute("listaChef", chefService.findAll());
			return "admin/modificaBuffetForm.html"; // ci sono errori, torna alla form iniziale
		}
	}	
	@GetMapping("/admin/modificaBuffet/{id}")
	public String getBuffetFormAdmin(@PathVariable Long id, Model model) {
		model.addAttribute("buffet", buffetService.findById(id));
		model.addAttribute("listaChef", chefService.findAll());
		model.addAttribute("listaPiatti", PiattoService.findAll());
		return "admin/modificaBuffetForm.html";
	}

	@GetMapping("/admin/toDeleteBuffet/{id}")
	public String toDeleteBuffetAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", buffetService.findById(id));
		return "admin/toDeleteBuffet.html";
	}

	/*quando elimino un buffet:
	 * - devo mettere a null il buffet nello chef
	 * - devo mettere a null il buffet nei piatti del buffet*/
	@Transactional
	@GetMapping("/admin/deleteBuffet/{id}")
	public String deleteBuffetAdmin(@PathVariable("id")Long id, Buffet buffet, BindingResult bindingResult,Model model) {
		List<Chef> listaChef = chefService.findAll();
		for(Chef chef : listaChef) {
			if(chef.getBuffets().contains(buffet)) {
				chef.getBuffets().remove(buffet);
				this.chefService.save(chef);
			}
		}
		buffetService.deleteChefById(id);
		model.addAttribute("buffets", buffetService.findAll());
		return "admin/buffets.html";
	}


	/*aggiungi al buffet lo chef che lo ha preparato*/
	@PostMapping(value="/admin/mettiChef/{idBuffet}/{idChef}") 
	public String mettiChefInBuffet(@PathVariable("idBuffet") Long idBuffet, @PathVariable("idChef") Long idChef, Model model){
		Buffet buffet = buffetService.findById(idBuffet);
		model.addAttribute("buffet", buffet);
		Chef chef = chefService.findById(idChef);
		model.addAttribute("chef", chef);
		buffet.setChef(chef);//aggiungo lo chef che ha preparato il buffet
		chef.getBuffets().add(buffet);//aggiungo questo buffet alla lista di buffet preparati dallo chef
		this.buffetService.save(buffet);
		this.chefService.save(chef);
		model.addAttribute("piatti", buffet.getPiatti());
		return "admin/buffet.html";
	}

	@PostMapping(value="/admin/toRemovePiatto/{idBuffet}/{idPiatto}")
	public String removePiatto(@PathVariable("idBuffet") Long idBuffet, @PathVariable("idPiatto") Long idPiatto, Model model) {
		Buffet buffet = buffetService.findById(idBuffet);
		Piatto Piatto = PiattoService.findById(idPiatto);
		buffet.getPiatti().remove(Piatto);//rimuovo il Piatto dalla lista di piatti del buffet
		this.buffetService.save(buffet);
		model.addAttribute("buffet", buffet);
		model.addAttribute("chef", buffet.getChef());
		model.addAttribute("piatti", buffet.getPiatti());
		return "admin/buffet.html";
	}


	@GetMapping("admin/buffetAddPiatto/{id}")
	public String buffetAddPiattoAdmin(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		List<Piatto> piattiNelBuffet= buffet.getPiatti();
		List<Piatto> listaPiatti = this.PiattoService.findAll();
		listaPiatti.removeAll(piattiNelBuffet); //ottengo tutti i piatti meno quello presenti nel buffet
		model.addAttribute("buffet", buffet);
		model.addAttribute("listaPiatti", listaPiatti);
		return "admin/buffetAddPiatto.html";
	}
	/*quando aggiungo un Piatto al buffet*/ 
	@PostMapping("admin/addPiatto/{idBuffet}/{idPiatto}")
	public String addPiattoAdmin(@PathVariable("idBuffet") Long idBuffet, @PathVariable("idPiatto") Long idPiatto, Model model) {
		Buffet buffet = buffetService.findById(idBuffet);
		Piatto Piatto = PiattoService.findById(idPiatto);
		buffet.getPiatti().add(Piatto);
		buffetService.save(buffet);
		model.addAttribute("chef", buffet.getChef());	
		List<Piatto> listaPiatti =  buffet.getPiatti();
		model.addAttribute("piatti",listaPiatti);
		model.addAttribute("buffet", buffet);
		return "admin/buffet.html";
	}











}
