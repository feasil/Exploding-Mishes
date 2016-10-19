package fr.feasil.kittens.game.server;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.GameEventListener;
import fr.feasil.kittens.game.Joueur;

public class ServerEventNotifier implements GameEventListener
{
	public static String getFileName(Joueur joueur)
	{
		return "\\\\seb1psrb.ecp.sf.local\\_Commun\\_Echange\\Pour Marina\\Michel\\srv" + joueur.getIdRH() +  ".ser";
	}
	
	private final JeuServer jeuServer;
	private final String eventFile;
	private final Joueur joueurIci;
	
	private List<ServerEvent> events;
	
	public ServerEventNotifier(JeuServer jeuServer, Joueur joueur) 
	{
		this.jeuServer = jeuServer;
		this.joueurIci = joueur;
		this.eventFile = getFileName(joueur);
		events = new ArrayList<ServerEvent>();
	}
	
	public JeuServer getJeuServer() {
		return jeuServer;
	}
	public Joueur getJoueur() {
		return joueurIci;
	}
	
	
	@Override
	public void finDeTour(Joueur j) {
		events.add(new ServerEvent(jeuServer, ServerEventType.FIN_DE_TOUR, j));
		writeServerEvent();
	}

	@Override
	public void carteJoueeDebut(ActionDeJeu action) {
		events.add(new ServerEvent(jeuServer, ServerEventType.CARTE_JOUEE_DEBUT, action));
		writeServerEvent();
	}

	@Override
	public void carteJoueeFin(ActionDeJeu action, boolean hasSucceded) {
		events.add(new ServerEvent(jeuServer, ServerEventType.CARTE_JOUEE_FIN, action, hasSucceded));
		writeServerEvent();
	}

	@Override
	public void cartePiochee(Joueur j) {
		events.add(new ServerEvent(jeuServer, ServerEventType.CARTE_PIOCHEE, j));
		writeServerEvent();
	}

	@Override
	public void joueurExplose(Joueur j, boolean canDefuse, Carte exploding) {
		Carte[] c = {exploding};
		events.add(new ServerEvent(jeuServer, ServerEventType.JOUEUR_EXPLOSE, j, canDefuse,  c));
		writeServerEvent();
	}
	
	@Override
	public void gameReady(Joueur joueur) {
		events.add(new ServerEvent(jeuServer, ServerEventType.GAME_READY, joueur));
		writeServerEvent();
	}
	
	@Override
	public void notifyException(Joueur joueur, Exception ex) {
		events.add(new ServerEvent(jeuServer, ServerEventType.NOTIFY_EXCEPTION, joueur, ex));
		writeServerEvent();
	}
	
	@Override
	public void seeTheFuture(ActionDeJeu actionDeJeu, Carte[] cartes) {
		events.add(new ServerEvent(jeuServer, ServerEventType.SEETHEFUTURE, actionDeJeu, cartes));
		writeServerEvent();
	}
	@Override
	public void favor(ActionDeJeu actionDeJeu) {
		events.add(new ServerEvent(jeuServer, ServerEventType.FAVOR, actionDeJeu));
		writeServerEvent();
	}
	@Override
	public void favorFin(ActionDeJeu actionDeJeu, Carte carteDonnee) {
		Carte[] c = {carteDonnee};
		events.add(new ServerEvent(jeuServer, ServerEventType.FAVOR_FIN, actionDeJeu, c));
		writeServerEvent();
	}
	@Override
	public void combinaisonDeuxCartes(ActionDeJeu actionDeJeu, Carte[] cartes) {
		events.add(new ServerEvent(jeuServer, ServerEventType.COMBINAISONDEUXCARTES, actionDeJeu, cartes));
		writeServerEvent();
	}
	@Override
	public void combinaisonDeuxCartesFin(ActionDeJeu actionDeJeu, Carte cartePrise) {
		Carte[] c = {cartePrise};
		events.add(new ServerEvent(jeuServer, ServerEventType.COMBINAISONDEUXCARTES_FIN, actionDeJeu, c));
		writeServerEvent();
	}
	@Override
	public void combinaisonTroisCartes(ActionDeJeu actionDeJeu) {
		events.add(new ServerEvent(jeuServer, ServerEventType.COMBINAISONTROISCARTES, actionDeJeu));
		writeServerEvent();
	}
	@Override
	public void combinaisonTroisCartesFin(ActionDeJeu actionDeJeu, Carte carte) {
		Carte[] c = {carte};
		events.add(new ServerEvent(jeuServer, ServerEventType.COMBINAISONTROISCARTES_FIN, actionDeJeu, c));
		writeServerEvent();
	}
	@Override
	public void combinaisonCinqCartes(ActionDeJeu actionDeJeu, Carte[] cartes) {
		events.add(new ServerEvent(jeuServer, ServerEventType.COMBINAISONCINQCARTES, actionDeJeu, cartes));
		writeServerEvent();
	}
	
	@Override
	public void joueurRepositionneExploding(Joueur joueur, int nombreCartes, Carte exploding) {
		Carte[] c = {exploding};
		events.add(new ServerEvent(jeuServer, ServerEventType.REPOSITIONNEEXPLODING, joueur, nombreCartes, c));
		writeServerEvent();
	}
	
	@Override
	public void joueurRepositionneExplodingFin(Joueur joueur, Carte carteExploding) {
		Carte[] c = {carteExploding};
		events.add(new ServerEvent(jeuServer, ServerEventType.REPOSITIONNEEXPLODING_FIN, joueur, c));
		writeServerEvent();
	}
	
	
	private void writeServerEvent()
	{
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
