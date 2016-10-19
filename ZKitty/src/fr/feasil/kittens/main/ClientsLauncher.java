package fr.feasil.kittens.main;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.client.JeuClient;
import fr.feasil.kittens.graphic.FenetreClient;


public class ClientsLauncher {
	
	
	public static void main(String args[])
	{
		
		//Fichiers non externalisés
		try {
			System.getProperties().load(ClientsLauncher.class.getResourceAsStream("/fr/feasil/kittens/main/version.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//-------------------
		
		//
		String version, subversion, revision;
		if ( System.getProperty(ClientsLauncher.class.getCanonicalName() + ".VERSION") != null )
			version = System.getProperty(ClientsLauncher.class.getCanonicalName() + ".VERSION");
		else version = "?";
		if ( System.getProperty(ClientsLauncher.class.getCanonicalName() + ".SUBVERSION") != null )
			subversion = System.getProperty(ClientsLauncher.class.getCanonicalName() + ".SUBVERSION");
		else subversion = "?";
		if ( System.getProperty(ClientsLauncher.class.getCanonicalName() + ".REVISION") != null )
			revision = System.getProperty(ClientsLauncher.class.getCanonicalName() + ".REVISION");
		else revision = "?";
		//
		
		final String title = "Exploding Mishes" + " v" + version + "." + subversion + "." + revision;
		
		
		/*ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		joueurs.add(new Joueur("Fabien", "vokw983"));
		joueurs.add(new Joueur("Yannick", "vjya592"));
		joueurs.add(new Joueur("Bertrand", "voex436"));
		joueurs.add(new Joueur("Julien", "xznx265"));
		
		JeuServer server = new JeuServer(joueurs);*/
		
		
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		final int height = (int)dimension.getHeight();
		final int width  = (int)dimension.getWidth();
		
		//fen.setJeu(jeu);
		new Thread(){
			@Override
			public void run() {
				fenetre(title + " Fabien", new Joueur("Fabien", "vokw983"), width/2-200, 0);
			}
		}.start();
		new Thread(){
			@Override
			public void run() {
				fenetre(title + " Yannick", new Joueur("Yannick", "vjya592"), width-500, height/2-300);
			}
		}.start();
		new Thread(){
			@Override
			public void run() {
				fenetre(title + " Bertrand", new Joueur("Bertrand", "voex436"), width/2-200, height-650);
			}
		}.start();
		new Thread(){
			@Override
			public void run() {
				fenetre(title + " Julien", new Joueur("Julien", "xznx265"), 0, height/2-300);
			}
		}.start();
		
		
		
		
		
	}
	
	private static void fenetre(String title, Joueur joueur, int posX, int posY)
	{
		FenetreClient fen = new FenetreClient(title);
		fen.setLocation(posX, posY);
		fen.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					if ( evt.getSource() instanceof FenetreClient )
						((FenetreClient) evt.getSource()).quitter();
					else
						System.exit(0);
				}
		});
		fen.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		
		JeuClient jc;
		try {
			jc = new JeuClient(joueur);
			fen.setJeuClient(jc);
		} catch (Exception e) {
			e.printStackTrace();
			if ( e.getMessage() != null )
				JOptionPane.showMessageDialog(fen, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			else
				JOptionPane.showMessageDialog(fen, "Serveur introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
			fen.quitter();
		}
	}
	

}
