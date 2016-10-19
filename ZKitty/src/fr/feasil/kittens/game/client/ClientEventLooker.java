package fr.feasil.kittens.game.client;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Combinaison;
import fr.feasil.kittens.cards.Playable;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.PlayerEventListener;
import fr.feasil.kittens.game.server.JeuServer;

/**
 * Permet aux clients de voir ce que fait le serveur
 * @author vokw983
 *
 */
public class ClientEventLooker
{
	
	private List<ClientEvent> clientEvents;
	private int elementsRead;
	private final JeuServer serveur;
	private final Joueur joueur;
	
	private final String fichier;
	
	public ClientEventLooker(JeuServer serveur, Joueur joueur) throws FileSystemException 
	{
		this.serveur = serveur;
		this.joueur = joueur;
		this.fichier = ClientEventNotifier.getFileName(this.joueur);
		
		FileSystemManager manager = VFS.getManager();
		FileObject file = manager.resolveFile(fichier);
		if ( file.exists() )
			file.delete();
		
		DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {
			@Override
			public void fileDeleted(FileChangeEvent arg0) throws Exception { }
			@Override
			public void fileCreated(FileChangeEvent arg0) throws Exception 
			{ 
				if ( arg0.getFile().isReadable() )
					refreshClient();
			}
			@Override
			public void fileChanged(FileChangeEvent arg0) throws Exception 
			{
				if ( arg0.getFile().isReadable() )
					refreshClient();
			}
		});
		fm.setDelay(100);
		fm.addFile(file); 
		fm.start();
		
		
		refreshClient();
	}
	
	/*public synchronized ClientEvent getClientEvent() {
		return clientEvent;
	}*/
	
	
	@SuppressWarnings("unchecked")
	private synchronized void refreshClient() 
	{
		//System.out.println("refreshServer !");
		ObjectInput input = null;
		try{
			input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fichier)));
			clientEvents = (List<ClientEvent>)input.readObject();
			//System.out.println("done !");
			
			if ( clientEvents != null )
			{
				PlayerEventListener pel = (PlayerEventListener)serveur;
				ClientEvent ce;
				for ( ; elementsRead < clientEvents.size() ; elementsRead++ )
				{
					ce = clientEvents.get(elementsRead);
					switch ( ce.getTypeEvent() )
					{
					case JOUER : 
						pel.jouer(getJoueurServeur(ce.getJoueur()), 
								getPlayableServeur(ce.getCartesJouees()), 
								getJoueurServeur(ce.getCible()), 
								ce.getTypeDeCarte());
						break;
					case PIOCHER : 
						pel.piocher(getJoueurServeur(ce.getJoueur()));
						break;
					case FINISH_FAVOR : 
						pel.finishFavor(getActionDeJeuServeur(ce.getActionDeJeu()), 
								getCarteServeur(ce.getCartePerdue()));
						break;
					case FINISH_EXPLODING : 
						pel.finishExploding(getJoueurServeur(ce.getJoueur()), 
								getCarteServeur(ce.getCarteExploding()), 
								ce.getPositionExploding());
						break;
					case FINISH_COMBINAISON_DEUX_CARTES : 
						pel.finishCombinaisonDeuxCartes(getActionDeJeuServeur(ce.getActionDeJeu()), 
								getCarteServeur(ce.getCartePerdue()));
						break;
					case FINISH_COMBINAISON_TROIS_CARTES : 
						pel.finishCombinaisonTroisCartes(getActionDeJeuServeur(ce.getActionDeJeu()), 
								getCarteServeur(ce.getCartePerdue()));
						break;
					case FINISH_COMBINAISON_CINQ_CARTES : 
						pel.finishCombinaisonCinqCartes(getActionDeJeuServeur(ce.getActionDeJeu()), 
								getCarteServeur(ce.getCartePerdue()));
						break;
					case JOUER_AFTER_WAIT : 
						pel.jouerAfterWait(getJoueurServeur(ce.getJoueurNotifiant()),
								getActionDeJeuServeur(ce.getActionDeJeu()));
						break;
					}
				}
			}
			
		}
		catch (IOException ioe)
		{
			//ioe.printStackTrace();
			//Le fichier est en cours d'écriture, la méthode sera rappelée automatiquement
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		finally{
			if ( input != null )
				try { input.close(); } catch (IOException e){}
		}
		
	}
	
	
	private Joueur getJoueurServeur(Joueur joueurClient) 
	{
		for ( Joueur j : serveur.getJoueurs() )
			if ( j.equals(joueurClient) )
				return j;
		return null;
	}
	
	private Playable getPlayableServeur(Playable p)
	{
		if ( p instanceof Carte )
		{
			return getCarteServeur(p.getCarte(0));
		}
		else if ( p instanceof Combinaison )
		{
			Carte[] cartes = new Carte[p.getNombreCartes()];
			for ( int i = 0 ; i < p.getNombreCartes() ; i++ )
				cartes[i] = getCarteServeur(p.getCarte(i));
			try {
				return Combinaison.getCombinaison(cartes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Carte getCarteServeur(Carte carteClient)
	{
		for ( Carte c : serveur.getCartesJeu() )
			if ( c.equals(carteClient) )
				return c;
		return null;
	}
	private ActionDeJeu getActionDeJeuServeur(ActionDeJeu actionDeJeuClient)
	{
		for ( ActionDeJeu a : serveur.getActionsDeJeu() )
			if ( a.equals(actionDeJeuClient) )
				return a;
		return null;
	}
	
}
