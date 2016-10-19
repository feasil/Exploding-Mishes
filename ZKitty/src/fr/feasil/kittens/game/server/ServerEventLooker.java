package fr.feasil.kittens.game.server;

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

import fr.feasil.kittens.game.GameEventListener;
import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.Pile;

/**
 * Permet aux clients de voir ce que fait le serveur
 * @author vokw983
 *
 */
public class ServerEventLooker
{
	
	private List<ServerEvent> serverEvents;
	private int elementsRead;
	private final GameEventListener gel;
	
	private final String fichier;
	
	public ServerEventLooker(GameEventListener gel, Joueur joueur) throws FileSystemException 
	{
		this.gel = gel;
		this.fichier = ServerEventNotifier.getFileName(joueur);
		this.elementsRead = 0;
		
		FileSystemManager manager = VFS.getManager();
		FileObject file = manager.resolveFile(fichier);
		
		DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {
			@Override
			public void fileDeleted(FileChangeEvent arg0) throws Exception { }
			@Override
			public void fileCreated(FileChangeEvent arg0) throws Exception { }
			@Override
			public void fileChanged(FileChangeEvent arg0) throws Exception 
			{
				if ( arg0.getFile().isReadable() )
					refreshServer();
			}
		});
		fm.setDelay(100);
		fm.addFile(file); 
		fm.start();
		
		
		refreshServer();
	}
	

	private synchronized ServerEvent getLastServerEvent() {
		return serverEvents.get(serverEvents.size()-1);
	}
	
	
	@SuppressWarnings("unchecked")
	private synchronized void refreshServer() {
		//System.out.println("refreshServer !");
		ObjectInput input = null;
		try{
			input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fichier)));
			serverEvents = (List<ServerEvent>)input.readObject();
			//System.out.println("done !");
			
			if ( serverEvents != null )
			{
				ServerEvent se;
				for ( ; elementsRead < serverEvents.size() ; elementsRead++ )
				{
					se = serverEvents.get(elementsRead);
					
					switch ( se.getTypeEvent() )
					{
					case FIN_DE_TOUR : 
						gel.finDeTour(se.getJoueur());
						break;
					case CARTE_JOUEE_DEBUT : 
						gel.carteJoueeDebut(se.getActionDeJeu());
						break;
					case CARTE_JOUEE_FIN : 
						gel.carteJoueeFin(se.getActionDeJeu(), se.isHasSucceded());
						break;
					case CARTE_PIOCHEE : 
						gel.cartePiochee(se.getJoueur());
						break;
					case JOUEUR_EXPLOSE : 
						gel.joueurExplose(se.getJoueur(), se.isCanDefuse(), se.getCartes()[0]);
						break;
					case GAME_READY : 
						gel.gameReady(se.getJoueur());
						break;
					case NOTIFY_EXCEPTION :
						gel.notifyException(se.getJoueur(), se.getException());
						break;
					case SEETHEFUTURE : 
						gel.seeTheFuture(se.getActionDeJeu(), se.getCartes());
						break;
					case FAVOR : 
						gel.favor(se.getActionDeJeu());
						break;
					case FAVOR_FIN : 
						gel.favorFin(se.getActionDeJeu(), se.getCartes()[0]);
						break;
					case COMBINAISONDEUXCARTES : 
						gel.combinaisonDeuxCartes(se.getActionDeJeu(), se.getCartes());
						break;
					case COMBINAISONDEUXCARTES_FIN : 
						gel.combinaisonDeuxCartesFin(se.getActionDeJeu(), se.getCartes()[0]);
						break;
					case COMBINAISONTROISCARTES : 
						gel.combinaisonTroisCartes(se.getActionDeJeu());
						break;
					case COMBINAISONTROISCARTES_FIN : 
						gel.combinaisonTroisCartesFin(se.getActionDeJeu(), se.getCartes()[0]);
						break;
					case COMBINAISONCINQCARTES : 
						gel.combinaisonCinqCartes(se.getActionDeJeu(), se.getCartes());
						break;
					case REPOSITIONNEEXPLODING : 
						gel.joueurRepositionneExploding(se.getJoueur(), se.getNombreCartes(), se.getCartes()[0]);
						break;
					case REPOSITIONNEEXPLODING_FIN : 
						gel.joueurRepositionneExplodingFin(se.getJoueur(), se.getCartes()[0]);
						break;
					//default:
					//	break;
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
	
	
	
	
	public int getNombreJoueurs() {
		return getLastServerEvent().getJeuServer().getNombreJoueurs();
	}
	public List<Joueur> getJoueurs() {
		return getLastServerEvent().getJeuServer().getJoueurs();
	}
	
	public List<Joueur> getJoueursEnJeu() {
		return getLastServerEvent().getJeuServer().getJoueursEnJeu();
	}
	public Pile getDefausse() {
		return getLastServerEvent().getJeuServer().getDefausse();
	}
	public Joueur getJoueurActuel() {
		return getLastServerEvent().getJeuServer().getJoueurActuel();
	}
	public int getNombreDefausse() {
		return getLastServerEvent().getJeuServer().getNombreDefausse();
	}
	public int getNombrePioche() {
		return getLastServerEvent().getJeuServer().getNombrePioche();
	}
	public boolean isAttack() {
		return getLastServerEvent().getJeuServer().isAttack();
	}
	
}
