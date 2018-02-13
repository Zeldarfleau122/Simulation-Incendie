package Fenetres;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Environnement.Meteo;
import Environnement.Terrain;
import Exceptions.FenetreErreurFatale;
import Exceptions.NoTerrain;

public class FenetreInitMeteo extends JFrame {
	private JButton commencer ;
	private JButton retour ;
	private JButton passer ;
	private JLabel lDirVent ;
	private JComboBox<String> dirVent ;
	private JLabel lVitVent ;
	private JTextField vitVent ;
	private JLabel lHumidite ;
	private JTextField humidite ;
	private JLabel lTemperature ;
	private JTextField temperature ;
	private JPanel pSouth ;
	private JPanel pCenter ;
	
	public FenetreInitMeteo() {
		this.setTitle("Initialisation Simulation - Meteo");
		this.setBounds(100,100,400,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pCenter = new JPanel() ;
		this.pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.PAGE_AXIS));
		
		this.lDirVent = new JLabel("Direction du vent") ;
		this.dirVent = new JComboBox<String>(new String[] {"Est", "Nord", "Ouest", "Sud"}) ;
		this.lDirVent.setVerticalTextPosition(JLabel.TOP);
		this.lDirVent.setHorizontalAlignment(JLabel.LEFT);
		
		this.lVitVent = new JLabel("Vitesse du vent : ( km/h )") ;
		this.vitVent = new JTextField(3) ;
		this.vitVent.setText("30");
		this.lVitVent.setVerticalTextPosition(JLabel.TOP);
		this.lVitVent.setHorizontalAlignment(JLabel.LEFT);
		
		this.humidite = new JTextField(3) ;
		this.lHumidite = new JLabel("Humidite : ( en mm )") ;
		this.humidite.setText("90");
		this.lHumidite.setLabelFor(this.humidite);
		this.lHumidite.setVerticalTextPosition(JLabel.TOP);
		this.lHumidite.setHorizontalAlignment(JLabel.LEFT);
		
		this.lTemperature = new JLabel("Temparature ( °C )") ;
		this.temperature = new JTextField(3) ;
		this.temperature.setText("0");
		this.lTemperature.setVerticalTextPosition(JLabel.TOP);
		this.lTemperature.setHorizontalAlignment(JLabel.LEFT);
		
		JPanel pTemp = new JPanel() ;
		
		pTemp.add(this.lVitVent) ;
		pTemp.add(this.vitVent);
		pTemp.add(this.lDirVent);
		pTemp.add(this.dirVent);
		
		this.pCenter.add(pTemp);
		
		pTemp = new JPanel() ;
		
		pTemp.add(this.lTemperature);
		pTemp.add(this.temperature);
		
		this.pCenter.add(pTemp);
		
		pTemp = new JPanel() ;
		
		pTemp.add(this.lHumidite);
		pTemp.add(this.humidite);
		
		this.pCenter.add(pTemp);
		
		this.add(this.pCenter);
		
		this.pSouth = new JPanel() ;
		
		this.retour = new JButton("Retour") ;
		this.commencer = new JButton("Commencer") ;
		this.passer = new JButton("Passer") ;
		
		this.commencer.addActionListener(new CommencerAction());
		
		this.pSouth.add(Box.createHorizontalStrut(20)) ;
		this.pSouth.add(this.retour);
		this.pSouth.add(Box.createHorizontalStrut(80)) ;
		this.pSouth.add(this.passer);
		this.pSouth.add(this.commencer);
		this.pSouth.add(Box.createHorizontalStrut(20)) ;
		
		this.add(this.pSouth, BorderLayout.SOUTH) ;
		
		this.setVisible(true) ;
	}
	
	private class CommencerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int t, h, vitV, dirVentVert, dirVentHor ;
			
			vitV = Integer.parseInt(vitVent.getText()) ;
			dirVentVert = 0 ;
			dirVentHor = 0 ;
			switch((String) dirVent.getSelectedItem()) {
			case "Nord" :
				dirVentVert = 0 ;
				dirVentHor = 1 ;
				break ;
			case "Sud" :
				dirVentVert = 0 ;
				dirVentHor = -1 ;
				break ;
			case "Ouest" :
				dirVentVert = -1 ;
				dirVentHor = 0 ;
				break ;
			case "Est" :
				dirVentVert = 1 ;
				dirVentHor = 0 ;
				break ;
			}
			t = Integer.parseInt(temperature.getText()) ;
			h = Integer.parseInt(humidite.getText()) ;
			
			try {
				Terrain.getInstance().m = new Meteo(t, h, vitV, dirVentVert, dirVentHor) ;
			} catch (Exception exept) {}
											// Si pas de feu / pompier => fenetre erreur !
											// Ouvrir nouvelle FenetreSimulation
			setVisible(false);
			dispose() ;
			
			try {
				Terrain.getInstance().initialiser();
			} catch (NoTerrain exception) {
				new FenetreErreurFatale(exception.toString()) ;
			}
			
			Thread th = new Thread(new Simulation()) ;
											// Des que finit revenir a la fenetre PanneauCreationTerrain !		
			th.run() ;
		}
	}
	
	public static void main(String[] args) {
		new FenetreInitMeteo() ;
	}
}
