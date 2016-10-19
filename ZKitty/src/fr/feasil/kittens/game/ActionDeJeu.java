package fr.feasil.kittens.game;

import java.io.Serializable;
import java.util.List;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Playable;

public class ActionDeJeu implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final Joueur joueur;
	private final Playable cartes;
	private final Joueur cible;
	private final Class<Carte> typeDeCarte;
	private transient final List<Joueur> joueursWaiting;
	private transient boolean enCours;
	private boolean attackEnCours;
	private boolean noped;
	
	
	public ActionDeJeu(Joueur joueur, Playable cartes, Joueur cible, Class<Carte> typeDeCarte, List<Joueur> joueursWaiting, boolean attackEnCours) 
	{
		this.joueur = joueur;
		this.cartes = cartes;
		this.cible = cible;
		this.typeDeCarte = typeDeCarte;
		this.joueursWaiting = joueursWaiting;
		this.enCours = true;
		this.attackEnCours = attackEnCours;
		this.noped = false;
	}
	
	public Joueur getJoueur() {
		return joueur;
	}
	public Playable getCartes() {
		return cartes;
	}
	public Joueur getCible() {
		return cible;
	}
	public Class<Carte> getTypeDeCarte() {
		return typeDeCarte;
	}
	public boolean isEnCours() {
		return enCours;
	}
	public void setTerminated() {
		this.enCours = false;
	}
	
	public void JoueurWaitingRetour(Joueur joueur) {
		joueursWaiting.remove(joueur);
	}
	public boolean isWaiting() {
		return joueursWaiting.size() > 0;
	}
	public boolean isWaiting(Joueur joueur)
	{
		return joueursWaiting.contains(joueur);
	}
	public boolean isAttackEnCours() {
		return attackEnCours;
	}
	public void setAttackTerminee() {
		this.attackEnCours = false;
	}
	public boolean isNoped() {
		return noped;
	}
	public void setNoped(boolean noped) {
		this.noped = noped;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj == null || !(obj instanceof ActionDeJeu) )
			return false;
		ActionDeJeu a = (ActionDeJeu) obj;
		if ( (joueur == null && a.getJoueur() != null) 
				|| (joueur != null && a.getJoueur() == null) 
				|| (joueur != null && !joueur.equals(a.getJoueur())) )
			return false;
		if ( (cartes == null && a.getCartes() != null) 
				|| (cartes != null && a.getCartes() == null)
				|| (cartes != null && !cartes.equals(a.getCartes())) ) 
			return false;
		if ( (cible == null && a.getCible() != null) 
				|| (cible != null && a.getCible() == null)
				|| (cible != null && !cible.equals(a.getCible())) )
			return false;
		if ( (typeDeCarte == null && a.getTypeDeCarte() != null)
				|| (typeDeCarte != null && a.getTypeDeCarte() == null)
				|| (typeDeCarte != null && !typeDeCarte.equals(a.getTypeDeCarte())) )
			return false;
		
		return true;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("joueur=");
		if ( getJoueur() != null )
			sb.append(getJoueur().getIdRH());
		else
			sb.append("null");
		sb.append(";cartes=");
		if ( getCartes() != null )
			for ( int i = 0 ; i < getCartes().getNombreCartes() ; i++ )
				sb.append(getCartes().getCarte(i).toString() + ",");
		else
			sb.append("null");
		sb.append(";cible=");
		if ( getCible() != null )
			sb.append(getCible().getIdRH());
		else
			sb.append("null");
		sb.append(";typeDeCarte=");
		sb.append(typeDeCarte);
		sb.append(";joueursWaiting=[");
		if ( joueursWaiting != null )
			for ( Joueur j : joueursWaiting )
				sb.append(j.getIdRH() + ",");
		else
			sb.append("null");
		sb.append("];encours=");
		sb.append(enCours);
		
		return sb.toString();
	}
}
