package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


//Clase que permite gestionas las acciones asociadas a los botones y teclas del programa
public class GestionEventos implements ActionListener, KeyListener{

	public static final String ENVIAR = "Enviar mensaje";
	public static final String ABRIR_CHAT_PRIVADO = "Abrir chat privado con el servidor";

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
			
		case ABRIR_CHAT_PRIVADO:
			ventanaPrincipal.getVentanaPrivada().getFrmChatServidor().setVisible(true);
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
}
