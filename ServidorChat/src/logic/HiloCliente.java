package logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import gui.VentanaPrincipal;

//clase encargada de gestionar la conexion con un cliente
public class HiloCliente extends Thread {

	// Socket de la conexión
	private Socket socket;
	// DataInputStream que contentrá la información enviada por el cliente
	private DataInputStream entrada;
	// referencia del servidor
	private Servidor servidor;
	// apodo del cliente
	private String nick;

	// constructor de la clase, recibe el nuevo socket instanciado por el servidor
	public HiloCliente(Socket socket, Servidor servidor) {
		super();
		this.socket = socket;
		this.servidor = servidor;
		nick = "nn";
		try {
			entrada = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Se pone en marcha el hilo
	@Override
	public void run() {
		boolean conexionEstablecida = true;
		try {
			// lee el apodo elegido por el usuario
			nick = entrada.readUTF();
		} catch (IOException e2) {
			// Manejo de errores
			servidor.replicarMensaje(socket.getInetAddress().getHostAddress() + "/" + nick + " se ha desconectado.");
			servidor.getVentanaPrincipal().getVentanaPrivadaCliente(socket).getFrmChatServidor().dispose();
			servidor.actualizarConectados();
		}
		// informa a todos los clientes que este cliente se ha conectado
		servidor.replicarMensaje("Cliente " + socket.getInetAddress().getHostAddress() + "/" + nick + " conectado.");
		servidor.actualizarConectados();
		String mensaje;
		// While que comprueba todo el tiempo si hay algún mensaje entrante
		while (conexionEstablecida) {
			try {
				// detecta un nuevo mesnaje
				mensaje = entrada.readUTF();
				String prefijo = mensaje.substring(0, 9);
				if (prefijo.equals("[PRIVADO]")) {
					mensaje = mensaje.substring(9);
					// recibe el mensaje de manera privada
					servidor.mostrarMensajePrivado(mensaje, socket, nick);
				} else {
					// lo replica a todos los conectados
					servidor.replicarMensaje(mensaje);
				}

			} catch (IOException e) {
				// Manejo de errores
				// TODO Auto-generated catch block
				try {
					socket.close();
				} catch (IOException e1) {
					// Manejo de errores
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				conexionEstablecida = false;
				servidor.replicarMensaje(
						socket.getInetAddress().getHostAddress() + "/" + nick + " se ha desconectado.");
				servidor.getVentanaPrincipal().getVentanaPrivadaCliente(socket).getFrmChatServidor().dispose();
				servidor.actualizarConectados();
			}

		}
	}

	public String getNick() {
		return nick;
	}

	public Socket getSocket() {
		return socket;
	}

}
