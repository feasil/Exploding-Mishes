package fr.feasil.kittens.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Playable;
import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.server.JeuServer;
import fr.feasil.kittens.game.server.ServerEventType;
import fr.feasil.utils.Utilitaire;


public class FenetreServer extends JFrame implements Observer
{
	private static final long serialVersionUID = 1L;
	
	//*-*-*-*-*-*-*-*-
	
	private JPanel panPlateau, panJ1, panJ2, panJ3, panJ4, panPioche;
	private JLabel lblJ1, lblJ2, lblJ3, lblJ4;
	private JList listJ1, listJ2, listJ3, listJ4;
	private JButton btnPlayJ1, btnPlayJ2, btnPlayJ3, btnPlayJ4;
	private JButton btnPiocheJ1, btnPiocheJ2, btnPiocheJ3, btnPiocheJ4;
	
	
	private JLabel lblCountCartes;
	//private JLabel lblLastCardPlayed;
	private JList listDefausse;
	
	private JLabel[] lbl;
	private JList[] list;
	
	private JLabel statusBar;
	//------------------
	
	private JeuServer jeuServer;
	
	
	public FenetreServer(String arg0)
		throws HeadlessException
	{
		super(arg0);pack();
		initFrame();
		initComponents();
		addComponents();
		
		
		setMinimumSize(new Dimension(900, 600));
		
		setVisible(true);
		
	}
	
	private void initFrame()
	{
		getContentPane().setLayout(new GridBagLayout());
		setSize(900, 600);
		setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);
	}

	private void initComponents()
	{
		lblJ1 = new JLabel();
		lblJ2 = new JLabel();
		lblJ3 = new JLabel();
		lblJ4 = new JLabel();
		
		listJ1 = new JList(); listJ1.setCellRenderer(new CarteAveugleCellRenderer());
		listJ2 = new JList(); listJ2.setCellRenderer(new CarteAveugleCellRenderer());
		listJ3 = new JList(); listJ3.setCellRenderer(new CarteAveugleCellRenderer());
		listJ4 = new JList(); listJ4.setCellRenderer(new CarteAveugleCellRenderer());
		
		lbl = new JLabel[]{lblJ1, lblJ2, lblJ3, lblJ4};
		list = new JList[]{listJ1, listJ2, listJ3, listJ4};
		
		btnPlayJ1 = new JButton("Jouer");
		btnPiocheJ1 = new JButton("Piocher");
		btnPlayJ2 = new JButton("Jouer");
		btnPiocheJ2 = new JButton("Piocher");
		btnPlayJ3= new JButton("Jouer");
		btnPiocheJ3 = new JButton("Piocher");
		btnPlayJ4 = new JButton("Jouer");
		btnPiocheJ4 = new JButton("Piocher");
		
		ActionListener alPlay = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
			}
		};
		ActionListener alPioche = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
			}
		};
		btnPlayJ1.setEnabled(false); btnPlayJ2.setEnabled(false); btnPlayJ3.setEnabled(false); btnPlayJ4.setEnabled(false);
		btnPiocheJ1.setEnabled(false); btnPiocheJ2.setEnabled(false); btnPiocheJ3.setEnabled(false); btnPiocheJ4.setEnabled(false);
		
		
		btnPlayJ1.setActionCommand("0");
		btnPlayJ1.addActionListener(alPlay);
		btnPlayJ2.setActionCommand("1");
		btnPlayJ2.addActionListener(alPlay);
		btnPlayJ3.setActionCommand("2");
		btnPlayJ3.addActionListener(alPlay);
		btnPlayJ4.setActionCommand("3");
		btnPlayJ4.addActionListener(alPlay);
		
		btnPiocheJ1.setActionCommand("0");
		btnPiocheJ1.addActionListener(alPioche);
		btnPiocheJ2.setActionCommand("1");
		btnPiocheJ2.addActionListener(alPioche);
		btnPiocheJ3.setActionCommand("2");
		btnPiocheJ3.addActionListener(alPioche);
		btnPiocheJ4.setActionCommand("3");
		btnPiocheJ4.addActionListener(alPioche);
		
		//lblLastCardPlayed = new JLabel();
		lblCountCartes = new JLabel();
		listDefausse = new JList();
		listDefausse.setCellRenderer(new CarteCellRenderer());
		
		statusBar = new JLabel(" ");statusBar.setIcon(Utilitaire.getImageIcon("bomb_16_16.png"));
	}
	
	private void addComponents()
	{
		GridBagConstraints c = new GridBagConstraints();
		
		panPlateau = new JPanel(new GridBagLayout());
		
		panJ1 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ1.add(lblJ1, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ1.add(new JScrollPane(listJ1), cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ1.add(btnPlayJ1, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ1.add(btnPiocheJ1, cPan);
		}
		c.gridx = 1; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ1, c);
		
		
		panJ2 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ2.add(lblJ2, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ2.add(new JScrollPane(listJ2), cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ2.add(btnPlayJ2, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ2.add(btnPiocheJ2, cPan);
		}
		c.gridx = 2; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ2, c);
		
		
		panJ3 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ3.add(lblJ3, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ3.add(new JScrollPane(listJ3), cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ3.add(btnPlayJ3, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ3.add(btnPiocheJ3, cPan);
		}
		c.gridx = 1; c.gridy = 2;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ3, c);
		
		
		panJ4 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ4.add(lblJ4, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.gridwidth = 2; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ4.add(new JScrollPane(listJ4), cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ4.add(btnPlayJ4, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ4.add(btnPiocheJ4, cPan);
		}
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ4, c);
		
		
		panPioche = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			//panPioche.add(new JLabel("PIOCHE !!"), cPan);
			
			/*cPan.gridx = 0; cPan.gridy = 1;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panPioche.add(lblLastCardPlayed, cPan);*/
			
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panPioche.add(lblCountCartes, cPan);
			
			cPan.gridx = 1; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panPioche.add(new JScrollPane(listDefausse), cPan);
			
			
		}
		c.gridx = 1; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panPioche, c);
		
		
		
		
		
		
		c = new GridBagConstraints();
		c.gridwidth = 1;
		c.gridx = 0; c.gridy = 0;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 2, 4);
		getContentPane().add(panPlateau, c);
		
		/*c.gridy = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		getContentPane().add(splitPane, c);
		*/
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 10; c.weighty = 0;
		getContentPane().add(statusBar, c);
	}
	
	
	
	
	
	
	
	public void setJeuServer(JeuServer jeu) 
	{
		this.jeuServer = jeu;
		if ( jeu != null )
		{
			if ( jeu.getNombreJoueurs() != 4 )
				;//throw new IllegalArgumentException("Pour le moment c'est pour 4 joueurs !");
			
			listDefausse.setModel(new DefausseListModel(jeu.getDefausse()));
			
			
			actualiserJoueurs();
			jeu.addObserver(this);
		}
	}
	
	
	
	private void actualiserJoueurs() 
	{
		for ( int i = 0 ; i < jeuServer.getNombreJoueurs() ; i++ )
		{
			lbl[i].setText(jeuServer.getJoueurs().get(i).getNom());
			lbl[i].setForeground(Color.BLACK);
			if ( !jeuServer.getJoueurs().get(i).isEnJeu() )
				lbl[i].setForeground(Color.RED);
			list[i].setListData(new Vector<Carte>(jeuServer.getJoueurs().get(i).getMain()));
		}
		
		if ( jeuServer.isAttack() )
			lbl[jeuServer.getJoueurs().indexOf(jeuServer.getJoueurActuel())].setText(jeuServer.getJoueurActuel().getNom() + " x2");
		lbl[jeuServer.getJoueurs().indexOf(jeuServer.getJoueurActuel())].setForeground(Color.BLUE);
		
		lblCountCartes.setText("Pioche : " + jeuServer.getNombrePioche() + " / Defausse : " + jeuServer.getNombreDefausse());
		
	}
	
	
	

	/**
	 * Quitte l'application
	 */
	public void quitter()
	{
		System.exit(0);
	}
	
	
	
	
	
	
	
	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	public void update(Observable o, Object arg)
	{
		if ( arg instanceof Object[] 
				&& ((Object[]) arg)[0] instanceof ServerEventType )
		{
			Joueur j, cible;
			Playable c;
			Class<Carte> typeDeCarte;
			boolean hasSucceded, canDefuse;
			Exception ex;
			switch ( (ServerEventType) ((Object[]) arg)[0] )
			{
			case FIN_DE_TOUR : 
				actualiserJoueurs();
				break;
			case CARTE_JOUEE_DEBUT : 
				j = (Joueur) ((Object[]) arg)[1];
				c = (Playable) ((Object[]) arg)[2];
				cible = (Joueur) ((Object[]) arg)[3];
				typeDeCarte = ((Class<Carte>) ((Object[]) arg)[4]);
				
				statusBar.setText(j.getNom() + " joue : " + c + (cible==null?"":" et cible " + cible.getNom()) + (typeDeCarte==null?"":" et demande un " + typeDeCarte.getSimpleName()));
				actualiserJoueurs();
				break;
			case CARTE_JOUEE_FIN : 
				j = (Joueur) ((Object[]) arg)[1];
				c = (Playable) ((Object[]) arg)[2];
				cible = (Joueur) ((Object[]) arg)[3];
				typeDeCarte = (Class<Carte>) ((Object[]) arg)[4];
				hasSucceded = (Boolean) ((Object[]) arg)[5];
				
				statusBar.setText(j.getNom() + " a joué : " + c + (cible==null?"":" et a ciblé " + cible.getNom()) + (typeDeCarte==null?"":" et demande un " + typeDeCarte.getSimpleName()) + (hasSucceded?" // Réussi !":"// Raté..."));
				actualiserJoueurs();
				break;
			case CARTE_PIOCHEE : 
				j = (Joueur) ((Object[]) arg)[1];
				
				statusBar.setText(j.getNom() + " pioche");
				actualiserJoueurs();
				break;
			case JOUEUR_EXPLOSE : 
				j = (Joueur) ((Object[]) arg)[1];
				canDefuse = (Boolean) ((Object[]) arg)[2];
				
				if ( canDefuse )
					statusBar.setText(j.getNom() + " a explosé avec defuse");
				else
					statusBar.setText(j.getNom() + " a explosé sans defuse");
				actualiserJoueurs();
				break;
			case GAME_READY : 
				j = (Joueur) ((Object[]) arg)[1];
				
				actualiserJoueurs();
				break;
			case NOTIFY_EXCEPTION : 
				j = (Joueur) ((Object[]) arg)[1];
				ex = (Exception) ((Object[]) arg)[2];
				
				statusBar.setText(j.getNom() + " : " + ex.getMessage());
				actualiserJoueurs();
				break;
			}
			
		}
	}
	
	
	
	
	
}