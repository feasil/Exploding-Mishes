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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.graphic.CarteAveugleCellRenderer;
import fr.feasil.kittens.graphic.CarteCellRenderer;

public class DialogPickCard extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JList listeCartes;
	private JScrollPane scrollCartes;
	
	private JButton btnOk, btnCancel, btnNope;
	
	private boolean isCancellable;
	private boolean isAveugle;
	private boolean isNopable;
	
	private boolean isCanceled = true;
	private boolean isNoped = false;
	private Carte carte = null;
	
	private Carte[] cartes;
	
	public DialogPickCard(String title, ImageIcon icone, Carte[] cartes, boolean isCancellable, boolean isAveugle, boolean isNopable) 
	{
		super();
		this.cartes = cartes;
		this.isCancellable = isCancellable;
		this.isAveugle = isAveugle;
		this.isNopable = isNopable;
		
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
		listeCartes = new JList(cartes);
		listeCartes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeCartes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if ( SwingUtilities.isLeftMouseButton(evt) 
						&& evt.getClickCount() == 2 )
				{
					actionOK();
				}
			}
		});
		listeCartes.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				btnOk.setEnabled(listeCartes.getSelectedValue() != null);
			}
		});
		
		if ( isAveugle )
			listeCartes.setCellRenderer(new CarteAveugleCellRenderer());
		else
			listeCartes.setCellRenderer(new CarteCellRenderer()); 
		
		scrollCartes = new JScrollPane(listeCartes);
		
		
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionOK();
			}
		});
		btnOk.setEnabled(false);
		btnCancel = new JButton("Annuler");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionCancel();
			}
		});
		btnNope = new JButton("Nope");
		btnNope.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionNope();
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
			panTop.setBorder(new TitledBorder("Cartes"));
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 1; cPan.weighty = 1;
			cPan.insets = new Insets(4, 4, 4, 4);
			panTop.add(scrollCartes, cPan);
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
			
			if ( isCancellable )
			{
				cPan.gridx++;
				panBot.add(btnCancel, cPan);
			}
			if ( isNopable )
			{
				cPan.gridx++;
				panBot.add(btnNope, cPan);
			}
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
		if ( listeCartes.getSelectedIndex() == -1 )
		{
			JOptionPane.showMessageDialog(DialogPickCard.this, "Vous n'avez sélectionné aucune carte !", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		carte = (Carte) listeCartes.getSelectedValue();
		
		isCanceled = false;
		dispose();
	}
	private void actionCancel()
	{
		isCanceled = true;
		dispose();
	}
	private void actionNope() {
		isCanceled = false;
		isNoped = true;
		dispose();
	}
	
	public boolean isCanceled() {
		return isCanceled;
	}
	public boolean isNoped() {
		return isNoped;
	}
	public Carte getCarte() 
	{
		if ( isCanceled )
			return null;
		
		return carte;
	}
	//---------------------------------
	
}
