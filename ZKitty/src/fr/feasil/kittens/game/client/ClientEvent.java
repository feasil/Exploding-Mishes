package fr.feasil.kittens.game.client;

import java.io.Serializable;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Playable;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.Joueur;

public class ClientEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ClientEventType typeEvent;
	private ActionDeJeu actionDeJeu;
	private Joueur joueur;
	private Playable cartesJouees;
	private Joueur cible;
	private Class<Carte> typeDeCarte;
	private Carte cartePerdue;
	private int positionExploding;
	
	private Joueur joueurNotifiant; 
	
	public ClientEvent(ClientEventType type, Joueur joueur, Playable cartesJouees, Joueur cible, Class<Carte> typeDeCarte) 
	{
		this.typeEvent = type;
		this.joueur = joueur;
		this.cartesJouees = cartesJouees;
		this.cible = cible;
		this.typeDeCarte = typeDeCarte;
	}
	
	public ClientEvent(ClientEventType type, Joueur joueur) 
	{
		this.typeEvent = type;
		this.joueur = joueur;
	}
	
	public ClientEvent(ClientEventType type, ActionDeJeu actionDeJeu, Carte cartePerdue) 
	{
		this.typeEvent = type;
		this.actionDeJeu = actionDeJeu;
		this.cartePerdue = cartePerdue;
	}
	
	public ClientEvent(ClientEventType type, Joueur joueur, Carte carteExploding, int positionExploding) 
	{
		this.typeEvent = type;
		this.joueur = joueur;
		this.cartePerdue = carteExploding;
		this.positionExploding = positionExploding;
	}
	
	public ClientEvent(ClientEventType type, Joueur joueurNotifiant, ActionDeJeu actionDeJeu) 
	{
		this.typeEvent = type;
		this.joueurNotifiant = joueurNotifiant;
		this.actionDeJeu = actionDeJeu;
	}
	
	
	
	public ClientEventType getTypeEvent() {
		return typeEvent;
	}
	public ActionDeJeu getActionDeJeu() {
		return actionDeJeu;
	}
	public Joueur getJoueur() {
		return joueur;
	}
	public Playable getCartesJouees() {
		return cartesJouees;
	}
	public Joueur getCible() {
		return cible;
	}
	public Class<Carte> getTypeDeCarte() {
		return typeDeCarte;
	}
	public Carte getCartePerdue() {
		return cartePerdue;
	}
	public int getPositionExploding() {
		return positionExploding;
	}
	public Carte getCarteExploding() {
		return cartePerdue;
	}
	public Joueur getJoueurNotifiant() {
		return joueurNotifiant;
	}
}
