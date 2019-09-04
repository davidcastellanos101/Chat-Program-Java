package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

//Clase que permite gestionas las acciones asociadas a los botones y teclas del programa
public class GestionEventos implements ActionListener, KeyListener, HyperlinkListener{

	public static final String ENVIAR = "Envía mensaje grupal";


	private VentanaPrincipal ventanaPrincipal;

	public GestionEventos(VentanaPrincipal ventanaPrincipal) {
		super();
		this.ventanaPrincipal = ventanaPrincipal;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {

		case ENVIAR:
			ventanaPrincipal.enviarMensaje();
			break;
			
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getSource() == ventanaPrincipal.getTxtMsj()){
			if (e.getKeyCode() == 10) {
				ventanaPrincipal.enviarMensaje();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
		HyperlinkEvent.EventType type = hyperlinkEvent.getEventType();
	    int codigoSocket = Integer.parseInt(hyperlinkEvent.getDescription());
	    if (type == HyperlinkEvent.EventType.ACTIVATED) {
	    	ventanaPrincipal.getServidor().mostrarVentanaPrivada(codigoSocket);
	    } 
		
	}
}
