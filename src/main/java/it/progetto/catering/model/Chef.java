package it.progetto.catering.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;


@Entity
public class Chef implements Comparable<Chef>{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	private String cognome;

	@NotBlank
	private String nazionalita;

	@OneToMany(mappedBy="chef")
	private List<Buffet> buffets;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public List<Buffet> getBuffets() {
		return buffets;
	}

	public void setBuffets(List<Buffet> buffets) {
		this.buffets = buffets;
	}

	public Chef() {
		super();
	}

	

	@Override
	public int hashCode() {
		return Objects.hash(cognome, nazionalita, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chef other = (Chef) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(nazionalita, other.nazionalita)
				&& Objects.equals(nome, other.nome);
	}

	/*ordina per numero di buffet(inverso) e per cognome*/
	@Override
	public int compareTo(Chef that) {
		int cmp= that.getBuffets().size()-this.getBuffets().size();
		if(cmp==0) cmp = this.getCognome().compareTo(that.getCognome());
		return cmp;
	}




}
