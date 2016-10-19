package fr.feasil.kittens.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Defuse;
import fr.feasil.kittens.cards.Nope;
import fr.feasil.kittens.cards.Playable;

public class Joueur implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String nom;
	private final String idRH;
	private List<Carte> main;
	private boolean enJeu;
	private boolean isConnecte;
	
	
	public Joueur(String nom, String idRH) 
	{
		this.nom = nom;
		this.idRH = idRH.toLowerCase();
		main = new ArrayList<Carte>();
		enJeu = true;
	}
	
	public String getNom() {
		return nom;
	}
	public String getIdRH() {
		return idRH;
	}
	public List<Carte> getMain() {
		return main;
	}
	
	public boolean isEnJeu() {
		return enJeu;
	}
	public void setEnJeu(boolean enJeu) {
		this.enJeu = enJeu;
	}
	
	public boolean isConnecte() {
		return isConnecte;
	}
	public void setConnecte(boolean isConnecte) {
		this.isConnecte = isConnecte;
	}
	
	public boolean hasNope() 
	{
		for ( Carte c : getMain() )
			if ( c instanceof Nope )
				return true;
		return false;
	}
	public boolean hasCartes(Playable cartes) 
	{
		for ( int i = 0 ; i < cartes.getNombreCartes() ; i++ )
			if ( !getMain().contains(cartes.getCarte(i)) )
				return false;
		return true;
	}
	
	
	
	public void recupereCarte(Carte carte)
	{
		main.add(carte);
		Collections.sort(main);
	}
	public void perdCarte(Carte carte)
	{
		main.remove(carte);
	}
	
	
	/**
	 * 
	 * @return Si le joueur a un defuse ou non
	 */
	public Defuse explose()
	{
		for ( Carte c : main )
			if ( c instanceof Defuse )
				return (Defuse) c;
		return null;
	}
	
	
	
	@Override
	public boolean equals(Object j2) 
	{
		if ( j2 == null || !(j2 instanceof Joueur) )
			return false;
		if ( ((Joueur) j2).getIdRH() == null && getIdRH() == null )
			return true;
		if ( ((Joueur) j2).getIdRH() == null )
			return false;
		if ( getIdRH() == null )
			return false;
		return getIdRH().equals(((Joueur) j2).getIdRH());
	}
	
	
}
