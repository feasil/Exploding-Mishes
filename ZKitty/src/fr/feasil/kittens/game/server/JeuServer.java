package fr.feasil.kittens.game.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import org.apache.commons.vfs2.FileSystemException;

import fr.feasil.kittens.cards.Attack;
import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Defuse;
import fr.feasil.kittens.cards.Exploding;
import fr.feasil.kittens.cards.Favor;
import fr.feasil.kittens.cards.Nope;
import fr.feasil.kittens.cards.Playable;
import fr.feasil.kittens.cards.SeeTheFuture;
import fr.feasil.kittens.cards.Shuffle;
import fr.feasil.kittens.cards.Skip;
import fr.feasil.kittens.cards.cats.CatKarima;
import fr.feasil.kittens.cards.cats.CatMaya;
import fr.feasil.kittens.cards.cats.CatMish;
import fr.feasil.kittens.cards.cats.CatRayan;
import fr.feasil.kittens.cards.cats.CatSofia;
import fr.feasil.kittens.cards.combinaison.CombinaisonCinqCartes;
import fr.feasil.kittens.cards.combinaison.CombinaisonDeuxCartes;
import fr.feasil.kittens.cards.combinaison.CombinaisonTroisCartes;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.ListeActionsDeJeu;
import fr.feasil.kittens.game.Pile;
import fr.feasil.kittens.game.PlayerEventListener;
import fr.feasil.kittens.game.client.ClientEventLooker;

public class JeuServer extends Observable implements Serializable, PlayerEventListener
{
	private static final long serialVersionUID = 1L;
	private static final String FOLDER_RAPPORT = "\\\\seb1psrb.ecp.sf.local\\_Commun\\_Echange\\Pour Marina\\Michel\\";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
	
	/* 56 cartes :  Attack : 4	Cat : 20 (5<>)	Defuse : 6	Exploding : 4	Favor : 4	Nope : 5	SeeTheFuture : 5	Shuffle : 4	Skip : 4	 */
	private final static int NB_ATTACK = 4;//4;
	private final static int NB_CAT1 = 4;//4;
	private final static int NB_CAT2 = 4;//4;
	private final static int NB_CAT3 = 4;//4;
	private final static int NB_CAT4 = 4;//4;
	private final static int NB_CAT5 = 4;//4;
	private final static int NB_FAVOR = 4;//4;
	private final static int NB_NOPE = 5;//5;
	private final static int NB_SEETHEFUTURE = 5;//5;
	private final static int NB_SHUFFLE = 4;//4;
	private final static int NB_SKIP = 4;//4;
	private final static int NB_DEFUSE = 6;//6;
	//private final static int NB_EXPLODING = 4;
	private final static int WAITING_TIME = 2000;
	private final static int RENOTIFY_TIME = 10000;
	
	
	private final List<Joueur> joueurs;
	private final List<Carte> cartesJeu;
	private int idxJoueurActuel;
	private Joueur joueurActuel;
	
	private Pile pioche;
	private Pile defausse;
	
	
	private boolean attack = false;
	private Joueur favorEnCours = null;
	private Carte explodingEnCours = null;
	private Joueur combinaisonDeuxEnCours = null;
	private Joueur combinaisonTroisEnCours = null;
	private Joueur combinaisonCinqEnCours = null;
	
	//private final List<Playable> playableEnCours;
	// Joueur pour qui on attend un retour de bonne réception
	//private final List<Joueur> joueursWaiting;
	private final ListeActionsDeJeu actionsDeJeu;
	private boolean isNopable;
	
	
	private final transient List<ServerEventNotifier> notifiers;
	private final transient List<ClientEventLooker> lookers;
	
	
	private final transient File rapport;
	private transient BufferedWriter buffer;
	
	
	public JeuServer(List<Joueur> joueurs) throws FileSystemException
	{
		this.joueurs = joueurs;
		this.cartesJeu = new ArrayList<Carte>();
		//playableEnCours = new ArrayList<Playable>();
		actionsDeJeu = new ListeActionsDeJeu();
		isNopable = false;
		rapport = new File(FOLDER_RAPPORT + "log_server_" + sdf.format(new Date()) + ".log");
		try {
			buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rapport)));
			buffer.write("STARTING...\r\n");
			buffer.flush();
		} catch (Exception e) { buffer = null;}
		//this.joueursWaiting = new ArrayList<Joueur>();
		
		if ( getNombreJoueurs() < 2 || getNombreJoueurs() > 5 )
			throw new IllegalArgumentException("Le nombre de joueurs doit être compris entre 3 et 5");
		
		
		pioche = new Pile();
		defausse = new Pile();
		
		//ATTENTION : passer le serveur au notifier est risqué...
		notifiers = new ArrayList<ServerEventNotifier>();
		lookers = new ArrayList<ClientEventLooker>();
		for ( Joueur j : joueurs )
		{
			notifiers.add(new ServerEventNotifier(this, j));
			lookers.add(new ClientEventLooker(this, j));
		}
		
		
		int idUniqueCarte = 0;
		idUniqueCarte = initPileBefore(idUniqueCarte);
		idUniqueCarte = distribuer(idUniqueCarte);
		idUniqueCarte = initPileAfter(idUniqueCarte);
		
		
		idxJoueurActuel = (int)(Math.random() * (4-0)) + 0;//0;
		joueurActuel = this.joueurs.get(idxJoueurActuel);
		
		for ( ServerEventNotifier n : notifiers )
			n.gameReady(joueurActuel);
	
		Object[] o = {ServerEventType.GAME_READY, joueurActuel};
		setChanged();
		notifyObservers(o);
		
		logger("GAME READY");
	}
	
	private int distribuer(int idUniqueCarte) 
	{
		Carte c;
		for ( Joueur j : joueurs )
		{
			idUniqueCarte++;
			c = new Defuse(idUniqueCarte);
			cartesJeu.add(c);
			j.recupereCarte(c);
		}
		
		for ( int i = 0 ; i < 4 ; i++ )
			for ( Joueur j : joueurs )
				j.recupereCarte(pioche.prendreLaPremiereCarte());
		
		return idUniqueCarte;
	}
	
	
	
	
	
	/**
	 * Pour jouer une carte spécifique
	 * @param joueur
	 * @param carte
	 * @param cible
	 * @return
	 */
	@Override
	public void jouer(Joueur joueur, Playable cartes, Joueur cible, Class<Carte> typeDeCarte)
	{
		if ( actionsDeJeu.getActionsDeJeuWaitingCount() > 0 )
			{ notifyException(joueur, new IllegalArgumentException("Une carte est en cours de jeu...")); return; }
		if ( !cartes.isPlayable() )
			{ notifyException(joueur, new IllegalArgumentException("Cette carte ne peut pas être jouée")); return; }
		if ( explodingEnCours != null )
			{ notifyException(joueur, new IllegalArgumentException("Impossible de jouer, exploding en cours...")); return; }
		if ( cartes instanceof Nope )
		{
			if ( !isNopable )
				{ notifyException(joueur, new IllegalArgumentException("Impossible de NOPE la dernière action !")); return; }
			if ( actionsDeJeu.getActionsDeJeuEnCoursCount() > 1 )
				{ notifyException(joueur, new IllegalArgumentException("Un Nope est déjà en cours, attendez sa fin de résolution")); return; }
		}
		else
		{
			if ( !joueur.equals(joueurActuel) )
				{ notifyException(joueur, new IllegalArgumentException("Ce n'est pas à votre tour de jouer")); return; }
			if ( favorEnCours != null )
				{ notifyException(joueur, new IllegalArgumentException("Impossible de jouer, favor en cours...")); return; }
			if ( combinaisonDeuxEnCours != null )
				{ notifyException(joueur, new IllegalArgumentException("Impossible de jouer, combinaison de deux cartes en cours...")); return; }
			if ( combinaisonTroisEnCours != null )
				{ notifyException(joueur, new IllegalArgumentException("Impossible de jouer, combinaison de trois cartes en cours...")); return; }
			if ( combinaisonCinqEnCours != null )
				{ notifyException(joueur, new IllegalArgumentException("Impossible de jouer, combinaison de cinq cartes en cours...")); return; }
		}
		if ( !joueur.hasCartes(cartes) )
			return;//Le joueur a du jouer 2 fois d'affilé la même carte avant sa résolution
		if ( cartes instanceof Favor && cible.getMain().size() == 0 )
			{ notifyException(joueur, new IllegalArgumentException("Ce joueur n'a déjà plus de carte en main")); return; }
		if ( cartes instanceof CombinaisonCinqCartes && defausse.getNombreCartes() == 0 )
			{ notifyException(joueur, new IllegalArgumentException("Impossible de jouer cette combinaison, la défausse est vide !")); return; }
		
		
		debutJeuCarte(joueur, cartes, cible, typeDeCarte);
		
	}
	private void debutJeuCarte(final Joueur j, final Playable c, final Joueur cible, final Class<Carte> typeDeCarte)
	{
		for ( int i = 0 ; i < c.getNombreCartes() ; i++ )
		{
			j.perdCarte(c.getCarte(i));
			defausse.ajouterCarte(c.getCarte(i));
		}
		if ( c instanceof Nope )
			actionsDeJeu.getLast().setNoped(true);
		final ActionDeJeu action = new ActionDeJeu(j, c, cible, typeDeCarte, getJoueursEnJeu(), attack);
		actionsDeJeu.add(action);
		
		//On lance un thread pour prévenir les joueurs tant qu'on a pas reçu de réponse de leur part
		new Thread(){
			public void run() {
				while ( action.isWaiting() )
				{
					for ( ServerEventNotifier n : notifiers )
						if ( action.isWaiting(n.getJoueur()) )
							n.carteJoueeDebut(action);
					
					try { Thread.sleep(RENOTIFY_TIME); } catch (InterruptedException e) { }
				}
			};
		}.start();
		Object[] o = {ServerEventType.CARTE_JOUEE_DEBUT, j, c, cible, typeDeCarte};
		setChanged();
		notifyObservers(o);
		
		logger("CARTE_JOUEE_DEBUT " + (j==null?"null":j.getIdRH()) + " " + c.toString() + " " + (cible==null?"null":cible.getIdRH()) + " " + typeDeCarte);
		
		isNopable = true;
	}
	private void finJeuCarte(ActionDeJeu actionDeJeu, boolean hasSucceded)
	{
		//actionDeJeu.setTerminated();
		
		for ( ServerEventNotifier n : notifiers )
			n.carteJoueeFin(actionDeJeu, hasSucceded);
		Object[] o = {ServerEventType.CARTE_JOUEE_FIN, actionDeJeu.getJoueur(), actionDeJeu.getCartes(), actionDeJeu.getCible(), actionDeJeu.getTypeDeCarte(), hasSucceded};
		setChanged();
		notifyObservers(o);
		
		logger("CARTE_JOUEE_FIN " + (actionDeJeu==null?"null":actionDeJeu.toString()) + " " + hasSucceded);
	}
	
	
	@Override
	public synchronized void jouerAfterWait(Joueur joueurNotifiant, ActionDeJeu actionDeJeu)
	{
		if ( joueurNotifiant != null )
			actionDeJeu.JoueurWaitingRetour(joueurNotifiant);
		
		if ( joueurNotifiant == null || (!actionDeJeu.isWaiting() && actionDeJeu.isEnCours()) )
		{
			actionDeJeu.setTerminated();//test 
			
			boolean notif = (joueurNotifiant != null);
			if ( notif && !(actionDeJeu.getCartes() instanceof Nope) )//TODO c'est naze ce sleep à l'arrache...
				try { Thread.sleep(WAITING_TIME); } catch (InterruptedException e) { }
			
			if ( actionDeJeu.getCartes() instanceof Attack )
			{logger("AFTER_WAIT Attack " + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
				attack = true;
				
				finJeuCarte(actionDeJeu, true);
				finDeTour(true);
			}
			else if ( actionDeJeu.getCartes() instanceof Favor )
			{logger("AFTER_WAIT Favor " + (actionDeJeu.isNoped()?"Noped ":"") + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
				if ( !actionDeJeu.isNoped() )
				{
					favorEnCours = actionDeJeu.getCible();
					for ( ServerEventNotifier n : notifiers )
						if ( n.getJoueur().equals(actionDeJeu.getCible()) )
							n.favor(actionDeJeu);
				}
				else
					finJeuCarte(actionDeJeu, false);
			}
			else if ( actionDeJeu.getCartes() instanceof SeeTheFuture )
			{
				if ( !(actionsDeJeu.getLast().getCartes() instanceof Nope) )
				{logger("AFTER_WAIT Seehefuture " + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
					isNopable = false;
					actionsDeJeu.getLast().setAttackTerminee();
					int nbCartes = Math.min(3, pioche.getNombreCartes());
					
					Carte[] o = new Carte[nbCartes]; 
					for ( int i = 0 ; i < nbCartes ; i++ ) 
						o[i] = pioche.voirLaCarte(i);
					
					for ( ServerEventNotifier n : notifiers )
						if ( n.getJoueur().equals(actionDeJeu.getJoueur()) )
							n.seeTheFuture(actionDeJeu, o);
					finJeuCarte(actionDeJeu, true);
				}
				else
				{logger("AFTER_WAIT Seehefuture noped " + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
					finJeuCarte(actionDeJeu, false);
				}
			}
			else if ( actionDeJeu.getCartes() instanceof Shuffle )
			{
				if ( !(actionsDeJeu.getLast().getCartes() instanceof Nope) )
				{logger("AFTER_WAIT shuffle " + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
					isNopable = false;
					actionsDeJeu.getLast().setAttackTerminee();
					pioche.melanger();
					finJeuCarte(actionDeJeu, true);
				}
				else
				{logger("AFTER_WAIT Shuffle noped " + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
					finJeuCarte(actionDeJeu, false);
				}
			}
			else if ( actionDeJeu.getCartes() instanceof Skip )
			{logger("AFTER_WAIT skip " + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
				finJeuCarte(actionDeJeu, true);
				finDeTour(false);
			}
			else if ( actionDeJeu.getCartes() instanceof CombinaisonDeuxCartes )
			{logger("AFTER_WAIT Combinaison2 " + (actionDeJeu.isNoped()?"Noped ":"") + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
				if ( !actionDeJeu.isNoped() )
				{
					combinaisonDeuxEnCours = actionDeJeu.getCible();
					
					List<Carte> main = new ArrayList<Carte>(actionDeJeu.getCible().getMain());
					Collections.shuffle(main);
					
					Carte[] c = new Carte[main.size()]; 
					for ( int i = 0 ; i < c.length ; i++ ) 
						c[i] = main.get(i);//TODO envoyer le nb de cartes plutôt que les cartes
					
					for ( ServerEventNotifier n : notifiers )
						if ( n.getJoueur().equals(actionDeJeu.getJoueur()) || n.getJoueur().equals(actionDeJeu.getCible()) )
							n.combinaisonDeuxCartes(actionDeJeu, c);
				}
				else
					finJeuCarte(actionDeJeu, false);
			}
			else if ( actionDeJeu.getCartes() instanceof CombinaisonTroisCartes )
			{logger("AFTER_WAIT Combinaison3 " + (actionDeJeu.isNoped()?"Noped ":"") + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
				if ( !actionDeJeu.isNoped() )
				{
					combinaisonTroisEnCours = actionDeJeu.getCible();
					
					for ( ServerEventNotifier n : notifiers )
						if ( n.getJoueur().equals(actionDeJeu.getCible()) )
							n.combinaisonTroisCartes(actionDeJeu);
				}
				else
					finJeuCarte(actionDeJeu, false);
			}
			else if ( actionDeJeu.getCartes() instanceof CombinaisonCinqCartes )
			{logger("AFTER_WAIT Combinaison5 " + (actionDeJeu.isNoped()?"Noped ":"") + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
				if ( !actionDeJeu.isNoped() )
				{	
					combinaisonCinqEnCours = actionDeJeu.getJoueur();
					//On ne prend pas les 5 dernières qui ont été défaussées (celles qui ont été jouées)
					Carte[] c = new Carte[defausse.getNombreCartes() - 5]; 
					for ( int i = 0 ; i < c.length ; i++ ) 
						c[i] = defausse.voirLaCarte(i+5);
					
					for ( ServerEventNotifier n : notifiers )
						if ( n.getJoueur().equals(actionDeJeu.getJoueur()) )
							n.combinaisonCinqCartes(actionDeJeu, c);
				}
				else
					finJeuCarte(actionDeJeu, false);
			}
			else if ( actionDeJeu.getCartes() instanceof Nope )
			{logger("AFTER_WAIT Nope " + joueurNotifiant + " " + (actionDeJeu==null?"null":actionDeJeu.toString()));
				int nbNope = 0;
				for ( int i = actionsDeJeu.size()-1 ; i >= 0 ; i-- )
					if ( actionsDeJeu.get(i).getCartes() instanceof Nope )
							nbNope++;
					else
						break;
				
				ActionDeJeu lastPNoNope = actionsDeJeu.get(actionsDeJeu.size()-1-nbNope);
				
				//ActionDeJeu lastP2NoNope = null;
				//if ( actionsDeJeu.size()-2-nbNope >= 0 )
				//	lastP2NoNope = actionsDeJeu.get(actionsDeJeu.size()-2-nbNope);
				
				if ( nbNope % 2 == 1 )
				{//annuler une carte non nope
					if ( lastPNoNope.getCartes() instanceof Attack )
					{
						//if ( lastP2NoNope == null || !lastP2NoNope.isAttackEnCours() )
						if ( !lastPNoNope.isAttackEnCours() )
							attack = false;
						finDeTour(true, false);
					}
					else if ( lastPNoNope.getCartes() instanceof Favor )
					{
						favorEnCours = null;
					}
					else if ( lastPNoNope.getCartes() instanceof SeeTheFuture )
					{
						//C'est géré directement dans l'action
					}
					else if ( lastPNoNope.getCartes() instanceof Shuffle )
					{
						//C'est géré directement dans l'action
					}
					else if ( lastPNoNope.getCartes() instanceof Skip )
					{
						//if ( lastP2NoNope != null && (lastP2NoNope.getCartes() instanceof Attack) )
						if ( lastPNoNope.isAttackEnCours() )
							attack = true;
						else
							finDeTour(true, false);
					}
					else if ( lastPNoNope.getCartes() instanceof CombinaisonDeuxCartes )
					{
						combinaisonDeuxEnCours = null;
					}
					else if ( lastPNoNope.getCartes() instanceof CombinaisonTroisCartes )
					{
						combinaisonTroisEnCours = null;
					}
					else if ( lastPNoNope.getCartes() instanceof CombinaisonCinqCartes )
					{
						combinaisonCinqEnCours = null;
					}
				}
				else//annuler un nope
				{
					//On enlève les 2 derniers nope
					actionsDeJeu.remove(actionsDeJeu.getLast());
					actionsDeJeu.remove(actionsDeJeu.getLast());
					//On enleve les 2 derniers Nope
					
					actionsDeJeu.getLast().setNoped(false);
					jouerAfterWait(null, actionsDeJeu.getLast());
				}
				
				finJeuCarte(actionDeJeu, true);
			}
			else
			{
				notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("C'est quoi cette carte ??!"));
				return;
			}
		}
	}
	
	
	@Override
	public void finishFavor(ActionDeJeu actionDeJeu, Carte carteDonnee)
	{
		//if ( favorEnCours == null )
		//	{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Pas de favor en cours")); return; }
		if ( !actionDeJeu.getJoueur().equals(joueurActuel) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur actuel n'est pas le bon")); return; }
		if ( favorEnCours != null && !favorEnCours.equals(actionDeJeu.getCible()) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur cible n'est pas le bon")); return; }
		
		if ( favorEnCours == null )
		{logger("finishFavor Noped " + actionDeJeu.toString() + " " + carteDonnee);
			finJeuCarte(actionDeJeu, false);
		}
		else
		{logger("finishFavor " + actionDeJeu.toString() + " " + carteDonnee);
		
			actionDeJeu.getCible().perdCarte(carteDonnee);
			actionDeJeu.getJoueur().recupereCarte(carteDonnee);
			favorEnCours = null;
			isNopable = false;
			if ( actionsDeJeu.getLast() != null )
				actionsDeJeu.getLast().setAttackTerminee();
			
			for ( ServerEventNotifier n : notifiers )
				if ( n.getJoueur().equals(actionDeJeu.getJoueur()) )
					n.favorFin(actionDeJeu, carteDonnee);
			
			finJeuCarte(actionDeJeu, true);
		}
	}
	@Override
	public void finishExploding(Joueur joueur, Carte carteExploding, int position)
	{
		if ( explodingEnCours == null )
			{ notifyException(joueur, new IllegalArgumentException("Pas d'exploding en cours")); return; }
		if ( !joueur.equals(joueurActuel) )
			{ notifyException(joueur, new IllegalArgumentException("Le joueur actuel n'est pas le bon")); return; }
		if ( !carteExploding.equals(explodingEnCours) )
			{ notifyException(joueur, new IllegalArgumentException("La carte exploding n'est pas la bonne")); return; }
		
		logger("finishExploding " + joueur + " " + carteExploding + " " + position);
		
		pioche.ajouterCarte(carteExploding, position);
		
		explodingEnCours = null;
		
		
		for ( ServerEventNotifier n : notifiers )
			if ( !n.getJoueur().equals(joueur) )
				n.joueurRepositionneExplodingFin(joueur, carteExploding);
		
		finDeTour(false);
	}
	@Override
	public void finishCombinaisonDeuxCartes(ActionDeJeu actionDeJeu, Carte cartePrise)
	{
		//if ( combinaisonDeuxEnCours == null )
		//	{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Pas de combinaison deux en cours")); return; }
		if ( !actionDeJeu.getJoueur().equals(joueurActuel) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur actuel n'est pas le bon")); return; }
		if ( combinaisonDeuxEnCours != null && !combinaisonDeuxEnCours.equals(actionDeJeu.getCible()) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur cible n'est pas le bon")); return; }
		
		if ( combinaisonDeuxEnCours == null )
		{logger("finishComb2 Noped " + actionDeJeu.toString() + " " + cartePrise);
			finJeuCarte(actionDeJeu, false);
		}
		else
		{logger("finishComb2 " + actionDeJeu.toString() + " " + cartePrise);
		
			actionDeJeu.getCible().perdCarte(cartePrise);
			actionDeJeu.getJoueur().recupereCarte(cartePrise);
			combinaisonDeuxEnCours = null;
			isNopable = false;
			if ( actionsDeJeu.getLast() != null )
				actionsDeJeu.getLast().setAttackTerminee();
			
			for ( ServerEventNotifier n : notifiers )
				if ( n.getJoueur().equals(actionDeJeu.getJoueur()) || n.getJoueur().equals(actionDeJeu.getCible()) )
					n.combinaisonDeuxCartesFin(actionDeJeu, cartePrise);
			
			finJeuCarte(actionDeJeu, true);
		}
	}
	@Override
	public void finishCombinaisonTroisCartes(ActionDeJeu actionDeJeu, Carte cartePrise)
	{
		//if ( combinaisonTroisEnCours == null )
		//	{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Pas de combinaison trois en cours")); return; }
		if ( !actionDeJeu.getJoueur().equals(joueurActuel) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur actuel n'est pas le bon")); return; }
		if ( combinaisonTroisEnCours != null && !combinaisonTroisEnCours.equals(actionDeJeu.getCible()) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur cible n'est pas le bon")); return; }
		
		if ( combinaisonTroisEnCours == null )
		{logger("finishComb3 Noped " + actionDeJeu.toString() + " " + cartePrise);
			finJeuCarte(actionDeJeu, false);
		}
		else
		{logger("finishComb3 " + actionDeJeu.toString() + " " + cartePrise);
		
			if ( cartePrise != null  )
			{
				actionDeJeu.getCible().perdCarte(cartePrise);
				actionDeJeu.getJoueur().recupereCarte(cartePrise);
			}
			boolean combinaisonTroisSucceded = (cartePrise != null);
			
			combinaisonTroisEnCours = null;
			isNopable = false;
			if ( actionsDeJeu.getLast() != null )
				actionsDeJeu.getLast().setAttackTerminee();
			
			for ( ServerEventNotifier n : notifiers )
				if ( n.getJoueur().equals(actionDeJeu.getJoueur()) )
					n.combinaisonTroisCartesFin(actionDeJeu, cartePrise);
			
			finJeuCarte(actionDeJeu, combinaisonTroisSucceded);
		}
	}
	@Override
	public void finishCombinaisonCinqCartes(ActionDeJeu actionDeJeu, Carte cartePrise)
	{
		//if ( combinaisonCinqEnCours == null )
		//	{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Pas de combinaison cinq en cours")); return; }
		if ( !actionDeJeu.getJoueur().equals(joueurActuel) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur actuel n'est pas le bon")); return; }
		if ( combinaisonCinqEnCours != null && !combinaisonCinqEnCours.equals(joueurActuel) )
			{ notifyException(actionDeJeu.getJoueur(), new IllegalArgumentException("Le joueur n'est pas le bon")); return; }
		
		if ( combinaisonCinqEnCours == null )
		{logger("finishComb5 Noped " + actionDeJeu.toString() + " " + cartePrise);
			finJeuCarte(actionDeJeu, false);
		}
		else
		{logger("finishComb5 " + actionDeJeu.toString() + " " + cartePrise);
		
			if ( cartePrise != null  )
			{
				defausse.retirerLaCarte(cartePrise);
				actionDeJeu.getJoueur().recupereCarte(cartePrise);
			}
			
			combinaisonCinqEnCours = null;
			isNopable = false;
			if ( actionsDeJeu.getLast() != null )
				actionsDeJeu.getLast().setAttackTerminee();
			
			finJeuCarte(actionDeJeu, true);
		}
	}
	
	
	/**
	 * Pour piocher une carte
	 * @param joueur
	 */
	@Override
	public void piocher(Joueur joueur)
	{
		if ( !joueur.equals(joueurActuel) )
			{ notifyException(joueur, new IllegalArgumentException("Ce n'est pas à votre tour de jouer")); return; }
		if ( actionsDeJeu.getActionsDeJeuWaitingCount() > 0 )
			{ notifyException(joueur, new IllegalArgumentException("Une carte est en cours de jeu...")); return; }
		if ( pioche.getNombreCartes() <= 0 ) 
			{ notifyException(joueur, new IllegalArgumentException("Plus de carte en pioche")); return; }
		if ( favorEnCours != null )
			{ notifyException(joueur, new IllegalArgumentException("Impossible de piocher, favor en cours...")); return; }
		if ( explodingEnCours != null )
			{ notifyException(joueur, new IllegalArgumentException("Impossible de piocher, exploding en cours...")); return; }
		if ( combinaisonDeuxEnCours != null )
			{ notifyException(joueur, new IllegalArgumentException("Impossible de piocher, combinaison de deux cartes en cours...")); return; }
		if ( combinaisonTroisEnCours != null )
			{ notifyException(joueur, new IllegalArgumentException("Impossible de piocher, combinaison de trois cartes en cours...")); return; }
		if ( combinaisonCinqEnCours != null )
			{ notifyException(joueur, new IllegalArgumentException("Impossible de piocher, combinaison de cinq cartes en cours...")); return; }
		
		Carte cartePiochee = pioche.prendreLaPremiereCarte();
		isNopable = false;
		if ( actionsDeJeu.getLast() != null )
			actionsDeJeu.getLast().setAttackTerminee();
		
		for ( ServerEventNotifier n : notifiers )
			n.cartePiochee(joueur);
		Object[] o = {ServerEventType.CARTE_PIOCHEE, joueur};
		setChanged();
		notifyObservers(o);
		
		logger("CARTE_PIOCHEE " + joueur.getIdRH());
		
		if ( cartePiochee instanceof Exploding )
		{
			Defuse defuse = joueur.explose();
			
			if ( defuse != null )
			{
				joueur.perdCarte(defuse);
				defausse.ajouterCarte(defuse);
				
				explodingEnCours = cartePiochee;
				
				for ( ServerEventNotifier n : notifiers )
					n.joueurExplose(joueur, defuse != null, cartePiochee);
				
				for ( ServerEventNotifier n : notifiers )
					if ( n.getJoueur().equals(joueur) )
						n.joueurRepositionneExploding(joueur, pioche.getNombreCartes(), cartePiochee);
			}
			else
			{
				//le joueur perd
				attack = false;
				joueur.setEnJeu(false);
				List<Carte> tmp = new ArrayList<Carte>(joueur.getMain());
				for ( Carte c : tmp )
				{
					joueur.perdCarte(c);
					defausse.ajouterCarte(c);
				}
				
				for ( ServerEventNotifier n : notifiers )
					n.joueurExplose(joueur, defuse != null, cartePiochee);
				
				finDeTour(false);
			}
			
		}
		else 
		{
			joueur.recupereCarte(cartePiochee);
			
			finDeTour(false);
		}
	}
	
	
	
	
	
	private void finDeTour(boolean byPassAttack)
	{
		finDeTour(byPassAttack, true);
	}
	private void finDeTour(boolean byPassAttack, boolean sensHoraire)
	{
		if ( !byPassAttack && attack )
			attack = false;
		else
		{
			do {
				idxJoueurActuel = (idxJoueurActuel + getNombreJoueurs() + (sensHoraire?1:-1)) % getNombreJoueurs();
				joueurActuel = this.joueurs.get(idxJoueurActuel);
			}
			while ( !joueurActuel.isEnJeu() );
		}
		
		for ( ServerEventNotifier n : notifiers )
			n.finDeTour(joueurActuel);
		
		Object[] o = {ServerEventType.FIN_DE_TOUR, joueurActuel};
		setChanged();
		notifyObservers(o);
	}
	
	
	
	private void notifyException(Joueur joueur, Exception e)
	{
		for ( ServerEventNotifier n : notifiers )
			if ( joueur.equals(n.getJoueur()) )
					n.notifyException(joueur, e);
	}
	
	
	public List<Joueur> getJoueurs() {
		return joueurs;
	}
	public List<Carte> getCartesJeu() {
		return cartesJeu;
	}
	public ListeActionsDeJeu getActionsDeJeu() {
		return actionsDeJeu;
	}
	
	public Joueur getJoueurActuel() {
		return joueurActuel;
	}
	public Pile getDefausse() {
		return defausse;
	}
	
	
	/**
	 * Envoie une liste neuve
	 * @return Les joueurs en jeu
	 */
	public List<Joueur> getJoueursEnJeu() {
		List<Joueur> retour = new ArrayList<Joueur>();
		for ( Joueur j : joueurs )
			if ( j.isEnJeu() ) 
				retour.add(j);
		return retour;
	}
	
	
	public int getNombrePioche() 
	{
		return pioche.getNombreCartes();
	}
	public int getNombreDefausse() 
	{
		return defausse.getNombreCartes();
	}
	
	public boolean isAttack() {
		return attack;
	}
	
	
	
	
	
	public int getNombreJoueurs() 
	{
		return this.joueurs.size();
	}
	
	
	private int initPileBefore(int idUniqueCarte)
	{
		
		//add all cards
		for ( int i = 0 ; i < NB_ATTACK ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new Attack(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_CAT1 ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new CatMish(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_CAT2 ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new CatKarima(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_CAT3 ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new CatMaya(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_CAT4 ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new CatRayan(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_CAT5 ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new CatSofia(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_FAVOR ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new Favor(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_NOPE ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new Nope(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_SEETHEFUTURE ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new SeeTheFuture(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_SHUFFLE ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new Shuffle(idUniqueCarte)); }
		for ( int i = 0 ; i < NB_SKIP ; i++ )
			{ idUniqueCarte++; cartesJeu.add(new Skip(idUniqueCarte)); }
		
		
		for ( Carte c : cartesJeu )
			pioche.ajouterCarte(c);
		//shuffle
		pioche.melanger();
		
		return idUniqueCarte;
	}
	
		
	
	private int initPileAfter(int idUniqueCarte)
	{
		Carte c;
		//add exploding
		for ( int i = 0 ; i < (getNombreJoueurs() - 1) ; i++ )
		{
			idUniqueCarte++;
			c = new Exploding(idUniqueCarte);
			cartesJeu.add(c);
			pioche.ajouterCarte(c);
		}
		//add defuse
		for ( int i = 0 ; i < (NB_DEFUSE - getNombreJoueurs()) ; i++ )
		{
			idUniqueCarte++;
			c = new Defuse(idUniqueCarte);
			cartesJeu.add(c);
			pioche.ajouterCarte(c);
		}
		
		//shuffle
		pioche.melanger();
		return idUniqueCarte;
	}
	
	
	private synchronized void logger(String message)
	{
		try {
			buffer.write("**********************************\r\n");
			buffer.write(message + "\r\n");
			buffer.write("**********************************\r\nJoueurs : ");
			for ( Joueur j : joueurs )
				buffer.write(j.getIdRH() + "[" + j.getNom() + "]" + (j.equals(joueurActuel)?"*":"") + ";");
			buffer.write("\r\nPioche : ");
			for ( int i = 0 ; i < pioche.getNombreCartes() ; i++ )
				buffer.write(pioche.voirLaCarte(i) + ";");
			buffer.write("\r\nDefausse : ");
			for ( int i = 0 ; i < defausse.getNombreCartes() ; i++ )
				buffer.write(defausse.voirLaCarte(i) + ";");
			buffer.write("\r\nattack=" + attack + 
							";favorEnCours=" + (favorEnCours==null?"null":favorEnCours.getIdRH()) + 
							";explodingEnCours=" + (explodingEnCours==null?"null":explodingEnCours.toString()) + 
							";combinaisonDeuxEnCours=" + (combinaisonDeuxEnCours==null?"null":combinaisonDeuxEnCours.getIdRH()) +
							";combinaisonTroisEnCours=" + (combinaisonTroisEnCours==null?"null":combinaisonTroisEnCours.getIdRH()) + 
							";combinaisonCinqEnCours=" + (combinaisonCinqEnCours==null?"null":combinaisonCinqEnCours.getIdRH()));
			buffer.write("\r\nActions : ");
			for ( ActionDeJeu a : actionsDeJeu )
				buffer.write("	" + a.toString());
			buffer.write("\r\nisNopable=" + isNopable);
			buffer.write("\r\n--------------------------------------------\r\n");
			
			buffer.flush();
		} catch (IOException e) { }
	}
	
	
}
