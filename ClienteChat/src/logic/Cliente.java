package logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.text.Document;

import gui.VentanaChatPrivado;
import gui.VentanaPrincipal;

//Clase que gestiona la conexión con el servidor
public class Cliente {

	// socket de la conexion con el servidor
	private Socket socket;
	// puerto del servidor
	private int puerto;
	// dirección ip del servidor
	private String ipServidor;
	// DataInputStream que contendrá la información enviada por el servidor
	private DataInputStream entrada;
	// DataOutputStream que contendrá la información enviada al servidor
	private DataOutputStream salida;
	// apodo del cliente
	private String nick;
	// referenia de la ventana principal
	private VentanaPrincipal ventanaPrincipal;

	// constructor de la clase
	public Cliente(VentanaPrincipal ventanaPrincipal) {
		super();
		this.ventanaPrincipal = ventanaPrincipal;
		ipServidor = "";
		puerto = 5050;
		establecerParametrosInicio();
		establecerConexion();
		escucharMensajes();

	}

	// se piden los parámetros de inicio (ip, puerto y apodo) al cliente
	private void establecerParametrosInicio() {
		ipServidor = JOptionPane.showInputDialog("Ingrese dirección IP del servidor", ipServidor);
		puerto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese puerto", puerto));
	}

	// Se intenta establecer la conexión con el servidor
	private void establecerConexion() {
		try {
			socket = new Socket(ipServidor, puerto);
			entrada = new DataInputStream(socket.getInputStream());
			salida = new DataOutputStream(socket.getOutputStream());
			if (nick == null) {
				nick = JOptionPane.showInputDialog("Ingrese un apodo");
			}
			if (ventanaPrincipal.getVentanaPrivada() != null) {
				ventanaPrincipal.getVentanaPrivada().getTxtMsj().setEnabled(true);
			}
			ventanaPrincipal.getTxtMsj().setEnabled(true);
			
			salida.writeUTF(nick);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Servidor no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
			establecerParametrosInicio();
			establecerConexion();
		}
	}

	// método que crea un hilo encargado de escuchar si hay nuevos mensajes
	// provenientes del servidor
	// de haber alguno lo imprime por pantalla
	private void escucharMensajes() {
		Thread hilo = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean conexionEstablecida = true;
				String mensajeEntrante;
				while (conexionEstablecida) {

					try {
						mensajeEntrante = entrada.readUTF();
						String prefijo = mensajeEntrante.substring(0, 9);
						if (prefijo.equals("[PRIVADO]")) {
							mensajeEntrante = mensajeEntrante.substring(9);
							// recibe el mensaje de manera privada
							ventanaPrincipal.getVentanaPrivada().getFrmChatServidor().setVisible(true);
							ventanaPrincipal.getVentanaPrivada().getTxtChat().append(mensajeEntrante + "\n");
							Document d = ventanaPrincipal.getVentanaPrivada().getTxtChat().getDocument();
							ventanaPrincipal.getVentanaPrivada().getTxtChat().select(d.getLength(), d.getLength());
						} else {
							ventanaPrincipal.getTxtChat().append(mensajeEntrante + "\n");
							Document d = ventanaPrincipal.getTxtChat().getDocument();
							ventanaPrincipal.getTxtChat().select(d.getLength(), d.getLength());
						}

					} catch (IOException e) {
						// Manejo de errores
						// TODO Auto-generated catch block
						conexionEstablecida = false;
						JOptionPane.showMessageDialog(null,
								"Conexión perdida con el servidor. Se reintentará establecer la conexión.",
								"Conexión perdida", JOptionPane.INFORMATION_MESSAGE);
						ventanaPrincipal.getTxtMsj().setEnabled(false);
						ventanaPrincipal.getVentanaPrivada().getTxtMsj().setEnabled(false);
						try {
							socket.close();
						} catch (IOException e1) {
							// Manejo de errores
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						establecerConexion();
						escucharMensajes();
					}

				}

			}
		});
		hilo.start();

	}

	// método que se encarga de enviar un mensaje por parte del cliente al servidor.
	public void enviarMensaje(String msj) {
		try {
			if (!socket.isClosed()) {
				salida.writeUTF(socket.getLocalAddress().getHostAddress() + "/" + nick + ": " + msj);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// método que se encarga de enviar un mensaje privado.
	public void enviarMensajePrivado(String msj) {
		try {
			if (!socket.isClosed()) {
				salida.writeUTF("[PRIVADO]" + socket.getLocalAddress().getHostAddress() + "/" + nick + ": " + msj);
				ventanaPrincipal.getVentanaPrivada().getTxtChat()
						.append(socket.getLocalAddress().getHostAddress() + "/" + nick + ": " + msj + "\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
