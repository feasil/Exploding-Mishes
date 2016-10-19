package fr.feasil.kittens.graphic.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.graphic.JoueurCellRenderer;

public class DialogPickPlayer extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JList listeJoueurs;
	private JScrollPane scrollJoueurs;
	
	private JButton btnOk, btnCancel;
	
	private boolean isCanceled = true;
	private Joueur joueur = null;
	
	private Joueur[] joueurs;
	
	public DialogPickPlayer(String title, ImageIcon icone, Joueur[] joueurs) 
	{
		super();
		this.joueurs = joueurs;
		
		setTitle(title);
		setIconImage(icone.getImage());
		
		initComponents();
		addComponents();
		
		setMinimumSize(new Dimension(250, 300));
		setSize(getMinimumSize());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initComponents()
	{
		listeJoueurs = new JList(joueurs);
		listeJoueurs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeJoueurs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if ( SwingUtilities.isLeftMouseButton(evt) 
						&& evt.getClickCount() == 2 )
				{
					actionOK();
				}
			}
		});
		listeJoueurs.setCellRenderer(new JoueurCellRenderer()); 
		
		scrollJoueurs = new JScrollPane(listeJoueurs);
		
		
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionOK();
			}
		});
		btnCancel = new JButton("Annuler");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionCancel();
			}
		});
		
		
		/*KeyAdapter escapeAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
					actionCancel();
			}
		};
		
		listeJoueurs.addKeyListener(escapeAdapter);
		btnOk.addKeyListener(escapeAdapter);
		btnCancel.addKeyListener(escapeAdapter);
		scrollJoueurs.addKeyListener(escapeAdapter);*/
		
	}
	
	
	private void addComponents()
	{
		setLayout(new GridBagLayout());
		
		JPanel panTop = new JPanel(new GridBagLayout());
		{
			panTop.setBorder(new TitledBorder("Joueurs"));
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 1; cPan.weighty = 1;
			cPan.insets = new Insets(4, 4, 4, 4);
			panTop.add(scrollJoueurs, cPan);
		}
		
		JPanel panBot = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 0; cPan.weighty = 0;
			cPan.anchor = GridBagConstraints.NORTHWEST;
			cPan.insets = new Insets(4, 4, 4, 4);
			panBot.add(btnOk, cPan);
			
			cPan.gridx = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 0; cPan.weighty = 0;
			panBot.add(btnCancel, cPan);
		}
		
		
		GridBagConstraints cPan = new GridBagConstraints();
		cPan.gridx = 0; cPan.gridy = 0;
		cPan.gridwidth = 1; cPan.gridheight = 1;
		cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 1; cPan.weighty = 1;
		cPan.insets = new Insets(4, 4, 4, 4);
		add(panTop, cPan);
		
		
		cPan.gridx = 0; cPan.gridy = 1;
		cPan.gridwidth = 1; cPan.gridheight = 1;
		cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 0; cPan.weighty = 0;
		cPan.insets = new Insets(0, 4, 4, 4);
		add(panBot, cPan);
		
	}
	
	
	
	
	
	
	
	
	//-- ACTIONS ----------------------
	private void actionOK()
	{
		if ( listeJoueurs.getSelectedIndex() == -1 )
		{
			JOptionPane.showMessageDialog(DialogPickPlayer.this, "Vous n'avez sélectionné aucun joueur !", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		joueur = (Joueur) listeJoueurs.getSelectedValue();
		
		isCanceled = false;
		dispose();
	}
	private void actionCancel()
	{
		isCanceled = true;
		dispose();
	}
	public boolean isCanceled() {
		return isCanceled;
	}
	public Joueur getJoueur() 
	{
		if ( isCanceled )
			return null;
		
		return joueur;
	}
	//---------------------------------
	
}
