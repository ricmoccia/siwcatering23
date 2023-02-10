package it.progetto.catering.controller;

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

import it.progetto.catering.controller.validator.PiattoValidator;
import it.progetto.catering.model.Buffet;
import it.progetto.catering.model.Chef;
import it.progetto.catering.model.Materiale;
import it.progetto.catering.model.Attivita;
import it.progetto.catering.service.BuffetService;
import it.progetto.catering.service.IngredienteService;
import it.progetto.catering.service.PiattoService;

@Controller
public class PiattoController {

	@Autowired
	private PiattoService piattoService;

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private PiattoValidator piattoValidator;	




	@GetMapping("/piatto/{id}")
	public String getPiatto(@PathVariable("id") Long id, Model model) {
		Attivita piatto = piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		model.addAttribute("listaBuffet", piatto.getBuffets());
		model.addAttribute("listaIngredienti", piatto.getIngredienti());
		return "piatto.html"; //la vista successiva mostra i dettagli del piatto
	}

	@GetMapping("/piatti")
	public String getAllPiatti(Model model) {
		List<Attivita> listaPiatti = piattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "piatti.html";
	}




	/*metodi di PiattoController relativi all'admin*/



	@GetMapping("/admin/piatto/{id}")
	public String getPiattoAdmin(@PathVariable("id") Long id, Model model) {
		Attivita piatto = piattoService.findById(id);
		model.addAttribute("piatto", piatto);
		model.addAttribute("listaBuffet", piatto.getBuffets());
		model.addAttribute("listaIngredienti", piatto.getIngredienti());
		return "admin/piatto.html"; //la vista successiva mostra i dettagli del piatto
	}

	@GetMapping("/admin/piatto")
	public String getAllPiattiAdmin(Model model) {
		List<Attivita> listaPiatti = piattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "admin/piatti.html";
	}

	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg piatto appena creato*/
	@GetMapping("/admin/piattoForm")
	public String getPiattoFormAdmin(Model model) {
		//in questo modo piattoForm ha un ogg Piatto a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("piatto", new Attivita());
		return "admin/piattoForm.html"; 		
	}	


	@PostMapping("/admin/piatto")
	public String addPiattoAdmin(@Valid @ModelAttribute("piatto") Attivita piatto, BindingResult bindingResult, Model model) {
		piattoValidator.validate(piatto, bindingResult);//se lo chef che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. persona dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			piattoService.save(piatto);
			model.addAttribute("piatto", piatto);
			return "admin/piatto.html";
		}
		else {
			List<Materiale> listaIngredienti= ingredienteService.findAll();
			model.addAttribute("listaIngredienti", listaIngredienti);
			return "admin/piattoForm.html";  //altrimenti ritorna alla pagina della form
		}
	}


	@Transactional
	@PostMapping("/admin/piattoEdited/{id}")
	public String modificaPiattoAdmin(@PathVariable Long id, @Valid @ModelAttribute("piatto") Attivita piatto, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {
			Attivita vecchioPiatto = piattoService.findById(id);
			vecchioPiatto.setNome(piatto.getNome());
			vecchioPiatto.setDescrizione(piatto.getDescrizione());
			this.piattoService.save(vecchioPiatto);
			model.addAttribute("piatto", piatto);
			return "admin/piatto.html";
		} 
		else {
			model.addAttribute("listaIngredienti", ingredienteService.findAll());
			return "admin/modificaPiattoForm.html";
		}
	}	
	@GetMapping("/admin/modificaPiatto/{id}")
	public String getPiattoFormAdmin(@PathVariable Long id, Model model) {
		model.addAttribute("piatto", piattoService.findById(id));
		return "admin/modificaPiattoForm.html";
	}

	@GetMapping("/admin/toDeletePiatto/{id}")
	public String toDeletePiattoAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("piatto", piattoService.findById(id));
		return "admin/toDeletePiatto.html";
	}

	//	@Transactional
	//	@GetMapping("/admin/deletePiatto/{id}")
	//	public String deleteChefAdmin(@PathVariable("id")Long id, Piatto piatto, BindingResult bindingResult,Model model) {
	//		piattoService.deletePiattoById(id);
	//		model.addAttribute("piatti", piattoService.findAll());
	//		return "admin/piatti.html";
	//	}

	/*quando elimino un piatto, lo devo eliminare anche dalla lista di piatti nel buffet*/
	@Transactional
	@GetMapping(value="/admin/deletePiatto/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
		Attivita piatto = this.piattoService.findById(id);
		List<Buffet> elencoBuffet = piatto.getBuffets();
		for(Buffet buffet : elencoBuffet) {
			if(buffet.getPiatti().contains(piatto)) {
				buffet.getPiatti().remove(piatto);
				this.buffetService.save(buffet);
			}
		}
		piattoService.deletePiattoById(id);
		List<Attivita> listaPiatti = this.piattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "admin/piatti.html";
	}






	@GetMapping("admin/piattoAddIngrediente/{id}")
	public String buffetAddIngredienteAdmin(@PathVariable("id") Long id, Model model) {
		Attivita piatto= this.piattoService.findById(id);
		List<Materiale> ingredientiNelPiatto= piatto.getIngredienti();
		List<Materiale> listaIngredienti = this.ingredienteService.findAll();
		listaIngredienti.removeAll(ingredientiNelPiatto); //ottengo tutti gli ingredienti meno quello presenti nel piatto
		model.addAttribute("piatto", piatto);
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/piattoAddIngrediente.html";
	}
	/*aggiungo un ingrediente al piatto*/ 
	@PostMapping("admin/addIngrediente/{idPiatto}/{idIngrediente}")
	public String addIngredienteAdmin(@PathVariable("idPiatto") Long idPiatto, @PathVariable("idIngrediente") Long idIngrediente, Model model) {
		Attivita piatto = this.piattoService.findById(idPiatto);
		model.addAttribute("piatto", piatto);
		Materiale ingrediente = this.ingredienteService.findById(idIngrediente);
		if(!piatto.getIngredienti().contains(ingrediente)) {
			piatto.getIngredienti().add(ingrediente);
		}
		piattoService.save(piatto);
		List <Materiale> listaIngredienti = piatto.getIngredienti();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/piatto.html";
	}







}
