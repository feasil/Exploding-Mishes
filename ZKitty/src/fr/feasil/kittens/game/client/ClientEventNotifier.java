package fr.feasil.kittens.game.client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Playable;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.PlayerEventListener;

public class ClientEventNotifier implements PlayerEventListener
{
	public static String getFileName(Joueur joueur)
	{
		return "\\\\seb1psrb.ecp.sf.local\\_Commun\\_Echange\\Pour Marina\\Michel\\client" + joueur.getIdRH() +  ".ser";
	}
	
	private final String eventFile;
	private List<ClientEvent> events;
	
	public ClientEventNotifier(Joueur joueur) 
	{
		eventFile = getFileName(joueur);
		events = new ArrayList<ClientEvent>();
	}
	
	
	
	
	@Override
	public void jouer(Joueur joueur, Playable cartes, Joueur cible, Class<Carte> typeDeCarte) 
	{
		events.add(new ClientEvent(ClientEventType.JOUER, joueur, cartes, cible, typeDeCarte));
		writeClientEvent();
	}
	@Override
	public void jouerAfterWait(Joueur joueurNotifiant, ActionDeJeu action) 
	{
		events.add(new ClientEvent(ClientEventType.JOUER_AFTER_WAIT, joueurNotifiant, action));
		writeClientEvent();
	}
	
	@Override
	public void piocher(Joueur joueur)
	{
		events.add(new ClientEvent(ClientEventType.PIOCHER, joueur));
		writeClientEvent();
	}
	@Override
	public void finishFavor(ActionDeJeu actionDeJeu, Carte carteDonnee)
	{
		events.add(new ClientEvent(ClientEventType.FINISH_FAVOR, actionDeJeu, carteDonnee));
		writeClientEvent();
	}
	@Override
	public void finishExploding(Joueur joueur, Carte carteExploding, int position)
	{
		events.add(new ClientEvent(ClientEventType.FINISH_EXPLODING, joueur, carteExploding, position));
		writeClientEvent();
	}
	@Override
	public void finishCombinaisonDeuxCartes(ActionDeJeu actionDeJeu, Carte cartePrise)
	{
		events.add(new ClientEvent(ClientEventType.FINISH_COMBINAISON_DEUX_CARTES, actionDeJeu, cartePrise));
		writeClientEvent();
	}
	@Override
	public void finishCombinaisonTroisCartes(ActionDeJeu actionDeJeu, Carte cartePrise) 
	{
		events.add(new ClientEvent(ClientEventType.FINISH_COMBINAISON_TROIS_CARTES, actionDeJeu, cartePrise));
		writeClientEvent();
	}
	@Override
	public void finishCombinaisonCinqCartes(ActionDeJeu actionDeJeu, Carte cartePrise)
	{
		events.add(new ClientEvent(ClientEventType.FINISH_COMBINAISON_CINQ_CARTES, actionDeJeu, cartePrise));
		writeClientEvent();
	}
	
	
	
	private void writeClientEvent()
	{
		//System.out.println("write");
		ObjectOutput output=null;
		try {
			output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(eventFile)));
			
			output.writeObject(events);
		}  
		catch(IOException ex){
			ex.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.flush();
					output.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
		
}
