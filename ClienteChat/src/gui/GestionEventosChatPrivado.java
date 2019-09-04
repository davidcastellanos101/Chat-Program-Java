package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


//Clase que permite gestionas las acciones asociadas a los botones y teclas del programa
public class GestionEventosChatPrivado implements ActionListener, KeyListener{

	public static final String ENVIAR_PRIVADO = "Enviar mensaje privado";

	private VentanaChatPrivado ventana;

	public GestionEventosChatPrivado(VentanaChatPrivado ventana) {
		super();
		this.ventana = ventana;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {

		case ENVIAR_PRIVADO:
			ventana.enviarMensajePrivado();
			break;

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getSource() == ventana.getTxtMsj()){
			if (e.getKeyCode() == 10) {
				ventana.enviarMensajePrivado();
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
