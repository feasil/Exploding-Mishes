package fr.feasil.kittens.game;

import fr.feasil.kittens.cards.Carte;

public interface GameEventListener 
{
	/**
	 * Fin de tour
	 * @param j Le joueur à qui c'est le tour
	 */
	public void finDeTour(Joueur j);
	
	/**
	 * You get Nopped
	 */
	//Noped();
	
	
	/**
	 * Une carte ou combinaison de cartes est jouée
	 * @param j Joueur qui a joué
	 * @param c Carte qui a été jouée
	 * @param cible Joueur ciblé, s'il y a lieu   
	 */
	public void carteJoueeDebut(ActionDeJeu action);
	
	/**
	 * Une carte ou combinaison de cartes a été jouée 
	 * @param j Joueur qui a joué
	 * @param c Carte qui a été jouée
	 * @param cible Joueur ciblé, s'il y a lieu   
	 */
	public void carteJoueeFin(ActionDeJeu action, boolean hasSucceded);
	
	
	/**
	 * Un joueur a pioché
	 * @param j Joueur qui a pioché
	 */
	public void cartePiochee(Joueur j);
	
	/**
	 * Prévient qu'un joueur explose
	 * @param j joueur qui explose
	 * @param canDefuse s'il peut Defuse
	 * @param exploding 
	 */
	public void joueurExplose(Joueur joueur, boolean canDefuse, Carte exploding);
	
	/**
	 * Indique que le jeu est préparé (on n'attend plus que les joueurs)
	 * @param joueur Joueur qui commencera
	 */
	public void gameReady(Joueur joueur);
	
	
	public void notifyException(Joueur joueur, Exception ex);
	
	
	
	public void seeTheFuture(ActionDeJeu actionDeJeu, Carte[] cartes);
	public void favor(ActionDeJeu actionDeJeu);
	public void favorFin(ActionDeJeu actionDeJeu, Carte cartePrise);
	public void combinaisonDeuxCartes(ActionDeJeu actionDeJeu, Carte[] cartes);
	public void combinaisonDeuxCartesFin(ActionDeJeu actionDeJeu, Carte cartePrise);
	public void combinaisonTroisCartes(ActionDeJeu actionDeJeu);
	public void combinaisonTroisCartesFin(ActionDeJeu actionDeJeu, Carte carte);
	public void combinaisonCinqCartes(ActionDeJeu actionDeJeu, Carte[] cartes);
	public void joueurRepositionneExploding(Joueur joueur, int nombreCartes, Carte exploding);
	public void joueurRepositionneExplodingFin(Joueur joueur, Carte carteExploding);
	
}
