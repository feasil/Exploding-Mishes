package fr.feasil.kittens.game;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Playable;

public interface PlayerEventListener 
{
	public void jouer(Joueur joueur, Playable cartes, Joueur cible, Class<Carte> typeDeCarte);
	public void piocher(Joueur joueur);
	
	public void jouerAfterWait(Joueur joueurNotifiant, ActionDeJeu actionDeJeu);
	
	public void finishFavor(ActionDeJeu actionDeJeu, Carte carteDonnee);
	public void finishExploding(Joueur joueur, Carte carteExploding, int position);
	public void finishCombinaisonDeuxCartes(ActionDeJeu actionDeJeu, Carte cartePrise);
	public void finishCombinaisonTroisCartes(ActionDeJeu actionDeJeu, Carte cartePrise);
	public void finishCombinaisonCinqCartes(ActionDeJeu actionDeJeu, Carte cartePrise);
}
