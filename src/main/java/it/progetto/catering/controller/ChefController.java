package it.progetto.catering.controller;

import java.util.ArrayList;
import java.util.Collections;
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

import it.progetto.catering.controller.validator.ChefValidator;
import it.progetto.catering.model.Buffet;
import it.progetto.catering.model.Chef;
import it.progetto.catering.service.BuffetService;
import it.progetto.catering.service.ChefService;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private ChefValidator chefValidator;	


	@GetMapping("/chef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		Chef chef = chefService.findById(id);
		model.addAttribute("chef", chef);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo chef
		List<Buffet> listaBuffet= chef.getBuffets(); //ottengo tutti i buffet per uno chef
		model.addAttribute("listaBuffet", listaBuffet);
		return "chef.html"; //la vista successiva mostra i dettagli dello chef
	}


	@GetMapping(value="/chef")
	public String getAllChefs(Model model) {
		List<Chef> chefs = chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "chefs.html";
	}

	@GetMapping("/orderchefs")
	public String getAllOrderedChefsAdmin(Model model) {
		List<Chef> chefs = chefService.findAll();
		Collections.sort(chefs);
		model.addAttribute("chefs", chefs);
		model.addAttribute("numChef", chefs.size());
		return "chefs.html";
	}







	/*metodi di ChefController che riguardano l'admin*/



	@GetMapping("/admin/chef/{id}")
	public String getChefAdmin(@PathVariable("id") Long id, Model model) {
		Chef chef = chefService.findById(id);
		model.addAttribute("chef", chef);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo chef
		List<Buffet> listaBuffet= chef.getBuffets(); //ottengo tutti i buffet per uno chef
		model.addAttribute("listaBuffet", listaBuffet);
		return "admin/chef.html"; //la vista successiva mostra i dettagli dello chef
	}

	@GetMapping("/admin/chef")
	public String getAllChefsAdmin(Model model) {
		List<Chef> chefs = chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "admin/chefs.html";
	}



	@PostMapping("/admin/chef")
	public String addChefAdmin(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult, Model model) {
		chefValidator.validate(chef, bindingResult);//se lo chef che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. persona dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			chefService.save(chef);
			model.addAttribute("chef", chef);
			List<Buffet> listaBuffet= chef.getBuffets();
			model.addAttribute("listaBuffet", listaBuffet);
			return "/admin/chef.html"; //pagina con chef aggiunto
		}
		return "/admin/chefForm.html";  //altrimenti ritorna alla pagina della form(ci sono stati degli errori)
	}	

	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg chef appena creato*/
	@GetMapping("/admin/chefForm")
	public String getChefForm(Model model) {
		//in questo modo chefForm ha un ogg Chef a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("chef", new Chef());
		return "admin/chefForm.html"; 		
	}



	@GetMapping("/admin/modificaChef/{id}")
	public String getChefFormAdmin(@PathVariable Long id, Model model) { 
		Chef chef= chefService.findById(id);
		model.addAttribute("chef", chef);
		return "admin/modificaChefForm.html";
	}

//	@Transactional
//	@PostMapping("/admin/modificaChef/{id}")
//	public String modificaChefAdmin(@PathVariable Long id, @Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResults, Model model) {
//		if(!bindingResults.hasErrors()) {//se non ci sono stati err di validazione
//			Chef vecchioChef = chefService.findById(id);
//			vecchioChef.setId(chef.getId());
//			vecchioChef.setNome(chef.getNome());
//			vecchioChef.setCognome(chef.getCognome());
//			vecchioChef.setNazionalita(chef.getNazionalita());
//			this.chefService.save(vecchioChef);
//			model.addAttribute("chef", chef);
//			return "admin/chef.html";//pagina con lo chef modificato
//		} 
//		return "admin/modificaChefForm.html";//se ci sono stati degli errori ritorna alla pagina per la modifica dello chef
//	}
	
	@Transactional
	@PostMapping("/admin/chefEdited/{id}")
	public String editedChef(@PathVariable Long id, @Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResults, Model model) {

		Chef oldChef = chefService.findById(id);

		if(!chef.getNome().equals(oldChef.getNome()))
			chefValidator.validate(chef, bindingResults);

		if(!bindingResults.hasErrors()) {

			oldChef.setId(chef.getId());
			oldChef.setNome(chef.getNome());
			oldChef.setCognome(chef.getCognome());
			oldChef.setNazionalita(chef.getNazionalita());
			oldChef.setBuffets(chef.getBuffets());
			this.chefService.save(oldChef);
			model.addAttribute("chef", oldChef);
			return "admin/chef.html";
		}
			return "admin/modificaChefForm.html";

	}
	
	//	@PostMapping("/admin/modificaChef/{id}")
	//	public String modificaChefAdmin(@PathVariable("id") Long id, @ModelAttribute("chef") Chef chef, BindingResult chefBindingResult, Model model) {
	//		Chef originalChef = chefService.findById(id);
	//		originalChef.setNome(chef.getNome());
	//		originalChef.setCognome(chef.getCognome());
	//		originalChef.setNazionalita(chef.getNazionalita());
	//
	//		this.chefValidator.validate(originalChef, chefBindingResult);
	//		if (!chefBindingResult.hasErrors()) {
	//			List<Chef> chefs = chefService.findAll();
	//			model.addAttribute("chefs", chefs);
	//			chefService.save(originalChef);
	//			return "admin/chef.html";
	//		}
	//		model.addAttribute("chef", chefService.findById(id));
	//		return "admin/modificaChefForm.html";
	//	}



	@GetMapping("/admin/toDeleteChef/{id}")
	public String toDeleteChefAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", chefService.findById(id));
		return "admin/toDeleteChef.html";
	}

	/*quando elimino uno chef:
	 * -devo mettere a null lo chef nei buffet dello chef
	 */
	//	@Transactional
	//	@GetMapping("/admin/deleteChef/{id}")
	//	public String deleteChefAdmin(@PathVariable("id")Long id, Chef chef, BindingResult bindingResult,Model model) {
	//		List<Buffet> listaBuffet= (List<Buffet>) buffetService.findAll();
	//		for(Buffet buffet: listaBuffet) {
	//			if(buffet.getChef().equals(chef)) {
	//				buffet.setChef(null);
	//				buffetService.save(buffet);
	//			}
	//		}
	//		chefService.deleteChefById(id);
	//		model.addAttribute("chefs", chefService.findAll());
	//		return "admin/chefs.html";
	//	}
	@GetMapping(value="/admin/deleteChef/{id}")
	public String deleteChef(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		List<Buffet> elencoBuffet = chef.getBuffets();
		for(Buffet buffet : elencoBuffet) {
			buffet.setChef(null);
			this.buffetService.save(buffet);
		}
		chefService.deleteChefById(id);
		model.addAttribute("numChef", chefService.count());
		List<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "admin/chefs.html";
	}




	@PostMapping("/admin/deleteBuffet/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = this.buffetService.findById(id);
		Chef chef = buffet.getChef();
		buffet.setChef(null);
		chef.getBuffets().remove(buffet);
		model.addAttribute("chef", chef);
		List<Buffet> listaBuffet = chef.getBuffets();
		model.addAttribute("listaBuffet", listaBuffet);
		this.buffetService.save(buffet);
		this.chefService.save(chef);
		return "admin/chef.html";
	}


	/*quando aggiungo allo chef un buffet, devo modificare il buffet(adesso ha uno chef) e passo la lista dei buffet dello chef*/
	@PostMapping("admin/addBuffet/{idChef}/{idBuffet}")
	public String addBuffet(@PathVariable("idChef") Long idChef, @PathVariable("idBuffet") Long idBuffet, Model model) {
		Chef chef = this.chefService.findById(idChef);
		Buffet buffet = this.buffetService.findById(idBuffet);
		buffet.setChef(chef);
		this.buffetService.save(buffet);
		model.addAttribute("chef", chef);
		List<Buffet> listaBuffet = chef.getBuffets();
		model.addAttribute("listaBuffet", listaBuffet);
		return "admin/chef.html";
	}

	@GetMapping("admin/chefAddBuffet/{id}")
	public String chefAddBuffetAdmin(@PathVariable("id") Long id, Model model) {
		Chef chef = chefService.findById(id);
		model.addAttribute("chef", chef);
		/*bisogna passare nel model i buffet che non hanno uno chef assegnato(elimino dalla lista di buffet quelli con chef!=null) */
		List<Buffet> listaBuffet= new ArrayList<>();
		for(Buffet buffet:buffetService.findAll()) {
			if(buffet.getChef()==null)
				listaBuffet.add(buffet);
		}
		model.addAttribute("listaBuffet", listaBuffet);
		return "admin/chefAddBuffet.html";
	}











}
