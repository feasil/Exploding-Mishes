package fr.feasil.kittens.graphic.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
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


public class DialogPickPosition extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JList listeCartes;
	private JScrollPane scrollCartes;
	
	private JButton btnOk, btnCancel;
	
	private boolean isCancellable;
	
	private boolean isCanceled = true;
	private Integer position = null;
	
	private int taille;
	
	public DialogPickPosition(String title, ImageIcon icone, int taille, boolean isCancellable) 
	{
		super();
		this.taille = taille;
		this.isCancellable = isCancellable;
		
		setTitle(title);
		setIconImage(icone.getImage());
		
		initComponents();
		addComponents();
		
		setMinimumSize(new Dimension(250, 400));
		setSize(getMinimumSize());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initComponents()
	{
		Integer[] valeurs = new Integer[taille+1];
		for ( int i = 0 ; i < taille+1 ; i++ )
			valeurs[i] = i;
		listeCartes = new JList(valeurs);
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
		listeCartes.setCellRenderer(new DefaultListCellRenderer(){
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
			{
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				Integer valeur = (Integer) value;
				
				if ( valeur.intValue() == 0 )
				{
					if ( !isSelected )
						setBackground(Color.lightGray);
					setText("Haut de la pioche");
				}
				else if ( valeur.intValue() == 1 )
					setText(valeur.toString() + "ère carte");
				else
					setText(valeur.toString() + "ème carte");
				
				return this;
			}
		}); 
		
		scrollCartes = new JScrollPane(listeCartes);
		
		
		
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
			panTop.setBorder(new TitledBorder("Au dessous de : "));
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 1;
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
				cPan.gridx = 1;
				cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 0; cPan.weighty = 0;
				panBot.add(btnCancel, cPan);
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
			JOptionPane.showMessageDialog(DialogPickPosition.this, "Vous n'avez pas sélectionné de position !", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		position = (Integer) listeCartes.getSelectedValue();
		
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
	public Integer getPosition() 
	{
		if ( isCanceled )
			return null;
		
		return position;
	}
	//---------------------------------
	
}
