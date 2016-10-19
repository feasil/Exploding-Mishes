package fr.feasil.kittens.game.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import org.apache.commons.vfs2.FileSystemException;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Playable;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.GameEventListener;
import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.Pile;
import fr.feasil.kittens.game.PlayerEventListener;
import fr.feasil.kittens.game.server.ServerEventLooker;
import fr.feasil.kittens.game.server.ServerEventType;

public class JeuClient extends Observable implements Serializable, GameEventListener
{
	private static final long serialVersionUID = 1L;
	private static final String FOLDER_RAPPORT = "\\\\seb1psrb.ecp.sf.local\\_Commun\\_Echange\\Pour Marina\\Michel\\";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
	
	private final Joueur joueur;
	
	
	private final transient PlayerEventListener notifier;
	private final transient ServerEventLooker looker;
	
	private final transient File rapport;
	private transient BufferedWriter buffer;
	
	
	public JeuClient(Joueur joueur) throws FileSystemException 
	{
		this.joueur = joueur;
		rapport = new File(FOLDER_RAPPORT + "log_client_" + joueur.getIdRH() + "_" + sdf.format(new Date()) + ".log");
		try {
			buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rapport)));
			buffer.write("STARTING for " + joueur.getIdRH() + "[" + joueur.getNom() + "]...\r\n");
			buffer.flush();
		} catch (Exception e) { buffer = null;}
		
		
		notifier = new ClientEventNotifier(joueur);
		looker = new ServerEventLooker(this, joueur);
		
		if ( !getJoueurs().contains(joueur) )
			throw new IllegalArgumentException("Ce joueur n'est pas attendu sur le serveur");
		
	}
	
	
	
	
	
	
	
	// N'évoluera pas
	public Joueur getJoueur() {
		return joueur;
	}
	public int getNombreJoueurs() {
		return looker.getNombreJoueurs();
	}
	public List<Joueur> getJoueurs() {
		return looker.getJoueurs();
	}
	//
	
	
	
	// Peut évoluer
	public List<Joueur> getJoueursEnJeu() {
		return looker.getJoueursEnJeu();
	}
	public Pile getDefausse() {
		return looker.getDefausse();
	}
	public Joueur getJoueurActuel() {
		return looker.getJoueurActuel();
	}
	public int getNombreDefausse() {
		return looker.getNombreDefausse();
	}
	public int getNombrePioche() {
		return looker.getNombrePioche();
	}
	public boolean isAttack() {
		return looker.isAttack();
	}
	//
	
	
	
	// Actions
	public void jouer(Joueur joueur, Playable cartes, Joueur cible, Class<Carte> typeDeCarte) 
	{logger("jouer " + joueur.getIdRH() + " " + cartes.toString());
		notifier.jouer(joueur, cartes, cible, typeDeCarte);
	}
	public void piocher(Joueur joueur)
	{logger("piocher " + joueur.getIdRH());
		notifier.piocher(joueur);
	}
	
	
	public void finishFavor(ActionDeJeu actionDeJeu, Carte carteDonnee)
	{logger("finishFavor " + actionDeJeu.toString() + " " + carteDonnee);
		notifier.finishFavor(actionDeJeu, carteDonnee);
	}
	public void finishExploding(Joueur joueur, Carte carteExploding, int position)
	{logger("finishExploding " + joueur.getIdRH() + " " + carteExploding + " " + position);
		notifier.finishExploding(joueur, carteExploding, position);
	}
	public void finishCombinaisonDeuxCartes(ActionDeJeu actionDeJeu, Carte cartePrise)  
	{logger("finishCombinaison2 " + actionDeJeu.toString() + " " + cartePrise);
		notifier.finishCombinaisonDeuxCartes(actionDeJeu, cartePrise);
	}
	public void finishCombinaisonTroisCartes(ActionDeJeu actionDeJeu, Carte cartePrise) 
	{logger("finishCombinaison3 " + actionDeJeu.toString() + " " + cartePrise);
		notifier.finishCombinaisonTroisCartes(actionDeJeu, cartePrise);
	}
	public void finishCombinaisonCinqCartes(ActionDeJeu actionDeJeu, Carte cartePrise)
	{logger("finishCombinaison5 " + actionDeJeu.toString() + " " + cartePrise);
		notifier.finishCombinaisonCinqCartes(actionDeJeu, cartePrise);
	}
	///-------
	
	
	




	@Override
	public void finDeTour(Joueur j) 
	{logger("FIN_DE_TOUR prochain=" + j.getIdRH());
		Object[] o = {ServerEventType.FIN_DE_TOUR, j};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void carteJoueeDebut(ActionDeJeu action) 
	{logger("CARTE_JOUEE_DEBUT " + action.toString());
		Object[] o = {ServerEventType.CARTE_JOUEE_DEBUT, action.getJoueur(), action.getCartes(), action.getCible(), action.getTypeDeCarte()};
		setChanged();
		notifyObservers(o);
		
		logger("jouerAfterWait --> " + action.toString());
		notifier.jouerAfterWait(getJoueur(), action);
	}
	@Override
	public void seeTheFuture(ActionDeJeu actionDeJeu, Carte[] cartes) 
	{logger("seeTheFuture " + actionDeJeu.toString() + " " + cartes);
		Object[] o = {"seeTheFuture", actionDeJeu, cartes};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void favor(ActionDeJeu actionDeJeu) 
	{logger("favor " + actionDeJeu.toString());
		Carte[] cartes = new Carte[actionDeJeu.getCible().getMain().size()];
		for ( int i = 0 ; i < cartes.length ; i++ )
			cartes[i] = actionDeJeu.getCible().getMain().get(i);
		
		Object[] o = {"favor", actionDeJeu, cartes};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void favorFin(ActionDeJeu actionDeJeu, Carte cartePrise) 
	{logger("favorFin " + actionDeJeu.toString() + " " + cartePrise);
		if ( getJoueur().equals(actionDeJeu.getJoueur()) && cartePrise != null )
		{//Si c'est le demandeur, on affiche la carte prise
			Object[] o = {"favorFin", actionDeJeu, cartePrise};
			setChanged();
			notifyObservers(o);
		}
	}
	@Override
	public void combinaisonDeuxCartes(ActionDeJeu actionDeJeu, Carte[] cartes) 
	{
		if ( getJoueur().equals(actionDeJeu.getJoueur()) )
		{//Si c'est le demandeur, on affiche les cartes de la cible (cachées)
			logger("combinaison2 " + actionDeJeu.toString() + " " + cartes);
			Object[] o = {"combinaisonDeuxCartes", actionDeJeu, cartes};
			setChanged();
			notifyObservers(o);
		}
		else
		{//Pour celui qui se prend la combinaison
			logger("combinaison2DTC " + actionDeJeu.toString() + " " + cartes);
			Object[] o = {"combinaisonDeuxCartesDTC", actionDeJeu};
			setChanged();
			notifyObservers(o);
		}
	}
	@Override
	public void combinaisonDeuxCartesFin(ActionDeJeu actionDeJeu, Carte cartePrise) {
		if ( getJoueur().equals(actionDeJeu.getJoueur()) )
		{//Si c'est le demandeur, on affiche la carte prise
			logger("combinaison2Fin " + actionDeJeu.toString() + " " + cartePrise);
			Object[] o = {"combinaisonDeuxCartesFin", actionDeJeu, cartePrise};
			setChanged();
			notifyObservers(o);
		}
		else
		{//Pour celui qui se prend la combinaison
			logger("combinaison2FinDTC " + actionDeJeu.toString() + " " + cartePrise);
			Object[] o = {"combinaisonDeuxCartesFinDTC", actionDeJeu, cartePrise};
			setChanged();
			notifyObservers(o);
		}
	}
	@Override
	public void combinaisonTroisCartes(ActionDeJeu actionDeJeu) 
	{logger("combinaison3 " + actionDeJeu.toString());
		//Pour celui qui se prend la combinaison
		Object[] o = {"combinaisonTroisCartes", actionDeJeu};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void combinaisonTroisCartesFin(ActionDeJeu actionDeJeu, Carte carte) 
	{logger("combinaison3Fin " + actionDeJeu.toString() + " " + carte);
		Object[] o = {"combinaisonTroisCartesFin", actionDeJeu, carte};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void combinaisonCinqCartes(ActionDeJeu actionDeJeu, Carte[] cartes) 
	{logger("combinaison5 " + actionDeJeu.toString() + " " + cartes);
		Object[] o = {"combinaisonCinqCartes", actionDeJeu, cartes};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void joueurRepositionneExploding(Joueur joueur, int nombreCartes, Carte exploding) 
	{logger("joueurRepositionneExploding " + joueur.getIdRH() + " " + nombreCartes + " " + exploding);
		Object[] o = {"exploding", joueur, nombreCartes, exploding};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void joueurRepositionneExplodingFin(Joueur joueur, Carte carteExploding) 
	{logger("joueurRepositionneExplodingFin " + joueur.getIdRH() + " " + carteExploding);
		Object[] o = {"explodingFin", joueur, carteExploding};
		setChanged();
		notifyObservers(o);
	}
	
	
	
	
	
	
	
	
	@Override
	public void carteJoueeFin(ActionDeJeu action, boolean hasSucceded) 
	{logger("CARTE_JOUEE_FIN " + action.toString() + " " + hasSucceded);
		Object[] o = {ServerEventType.CARTE_JOUEE_FIN, action.getJoueur(), action.getCartes(), action.getCible(), action.getTypeDeCarte(), hasSucceded};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void cartePiochee(Joueur joueur) 
	{logger("CARTE_PIOCHEE " + joueur.getIdRH());
		Object[] o = {ServerEventType.CARTE_PIOCHEE, joueur};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void joueurExplose(Joueur joueur, boolean canDefuse, Carte exploding) 
	{logger("JOUEUR_EXPLOSE " + joueur.getIdRH() + " " + canDefuse + " " + exploding);
		Object[] o = {ServerEventType.JOUEUR_EXPLOSE, joueur, canDefuse, exploding};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void gameReady(Joueur joueur) 
	{logger("GAME_READY " + joueur.getIdRH());
		Object[] o = {ServerEventType.GAME_READY, joueur};
		setChanged();
		notifyObservers(o);
	}
	@Override
	public void notifyException(Joueur joueur, Exception ex) 
	{logger("NOTIFY_EXCEPTION " + joueur.getIdRH() + " " + ex.getMessage());
		//if ( getJoueur().equals(joueur) )
		{
			Object[] o = {ServerEventType.NOTIFY_EXCEPTION, joueur, ex};
			setChanged();
			notifyObservers(o);
		}
	}
	
	
	
	
	
	private synchronized void logger(String message)
	{
		try {
			buffer.write("**********************************\r\n");
			buffer.write(message + "\r\n");
			buffer.write("--------------------------------------------\r\n");
			
			buffer.flush();
		} catch (IOException e) { }
	}
	
}
