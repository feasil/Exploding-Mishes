package fr.feasil.kittens.game.server;

import java.io.Serializable;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.Joueur;

public class ServerEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ServerEventType typeEvent;
	private ActionDeJeu actionDeJeu;
	private Joueur joueur;
	private boolean canDefuse;
	private boolean hasSucceded;
	private Exception ex;
	private Carte[] cartes;
	private int nombreCartes;
	
	private final JeuServer jeuServer;
	
	public ServerEvent(JeuServer jeuServer, ServerEventType type, Joueur joueur) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.joueur = joueur;
	}

	public ServerEvent(JeuServer jeuServer, ServerEventType type, ActionDeJeu actionDeJeu) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.actionDeJeu = actionDeJeu;
	}
	
	public ServerEvent(JeuServer jeuServer, ServerEventType type, ActionDeJeu actionDeJeu, boolean hasSucceded) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.actionDeJeu = actionDeJeu;
		this.hasSucceded = hasSucceded;
	}

	public ServerEvent(JeuServer jeuServer, ServerEventType type, Joueur joueur, boolean canDefuse, Carte[] cartes) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.joueur = joueur;
		this.canDefuse = canDefuse;
		this.cartes = cartes;
	}
	
	public ServerEvent(JeuServer jeuServer, ServerEventType type, Joueur joueur, Exception ex) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.joueur = joueur;
		this.ex = ex;
	}
	public ServerEvent(JeuServer jeuServer, ServerEventType type, ActionDeJeu actionDeJeu, Carte[] cartes) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.actionDeJeu = actionDeJeu;
		this.cartes = cartes;
	}
	
	public ServerEvent(JeuServer jeuServer, ServerEventType type, Joueur joueur, Carte[] cartes) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.joueur = joueur;
		this.cartes = cartes;
	}
	public ServerEvent(JeuServer jeuServer, ServerEventType type, Joueur joueur, int nombreCartes, Carte[] cartes) 
	{
		this.jeuServer = jeuServer;
		this.typeEvent = type;
		this.joueur = joueur;
		this.nombreCartes = nombreCartes;
		this.cartes = cartes;
	}
	

	public ServerEventType getTypeEvent() {
		return typeEvent;
	}
	public ActionDeJeu getActionDeJeu() {
		return actionDeJeu;
	}
	public Joueur getJoueur() {
		return joueur;
	}
	public boolean isCanDefuse() {
		return canDefuse;
	}
	public boolean isHasSucceded() {
		return hasSucceded;
	}
	public Exception getException() {
		return ex;
	}
	public Carte[] getCartes() {
		return cartes;
	}
	public int getNombreCartes() {
		return nombreCartes;
	}
	
	public JeuServer getJeuServer() {
		return jeuServer;
	}
	
}
