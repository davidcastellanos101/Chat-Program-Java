package logic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import gui.VentanaChatPrivado;
import gui.VentanaPrincipal;

//Clase que gestiona el comportamiento del sservidor
public class Servidor {

	// referencia de la ventana principal
	private VentanaPrincipal ventanaPrincipal;
	private int puerto;
	private int maximoConexiones;
	// referencia del socket del servidor
	private ServerSocket servidor;
	// arreglo que almacena todas las conexiones con los clientes, cada HiloCLiente
	// es una conexión con un cliente distinto
	private ArrayList<HiloCliente> conexiones;

	// constructor de la clase
	public Servidor(int puerto, int maximoConexiones, VentanaPrincipal ventanaPrincipal) {
		super();
		this.ventanaPrincipal = ventanaPrincipal;
		this.puerto = puerto;
		this.maximoConexiones = maximoConexiones;
		// se informa a la ventana principal que se están escuchando conexiones en el
		// puerto designado por el usuario
		ventanaPrincipal.getTxtChat().append("Escuchando conexiones en el puerto " + puerto + "\n");
		conexiones = new ArrayList<>();
		try {
			servidor = new ServerSocket(puerto, maximoConexiones);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// método que pone en marcha el servidor
	public void iniciarServidor() {
		// se crea un hilo encargado de escuhar nuevas conexiones.
		// en caso de detectarse una nueva conexión se crea un HiloCliente encargado de
		// gestionar
		// la conexión con ese cliente, esto se hace por medio de un socket, de manera
		// que cada cliente tiene un socket asignado.
		Thread hilo = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						// se crea un socker para la nueva conexion
						Socket socket = servidor.accept();
						// se crea un HiloCLiente encargado de gestionar la nueva conexion
						HiloCliente hiloCliente = new HiloCliente(socket, getThis());
						// se inicia el hilo
						hiloCliente.start();
						// Se crea una ventana de chat privado para la nueva conexión
						ventanaPrincipal.crearVentanaChatPrivado(socket);
						// se agrega la nueva conexión al arreglo de conexiones
						conexiones.add(hiloCliente);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		hilo.start();

	}

	// método que se encarga de enviar un mensaje por parte del servidor a todos los
	// clientes conectados.
	public void enviarMensaje(String msj) {
		DataOutputStream salida;
		for (HiloCliente conexion : conexiones) {
			if (!conexion.getSocket().isClosed()) {
				try {
					// DataOutputStream que va a contener la información a enviar
					salida = new DataOutputStream(conexion.getSocket().getOutputStream());
					salida.writeUTF(conexion.getSocket().getLocalAddress().getHostAddress() + "/Servidor: " + msj);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		ventanaPrincipal.getTxtChat().append("Servidor: " + msj + "\n");
		// estas dos lineas mantienen los ultimos mensajes del chat visibles en pantalla
		Document d = ventanaPrincipal.getTxtChat().getDocument();
		ventanaPrincipal.getTxtChat().select(d.getLength(), d.getLength());

	}

	// este método se encarga de replicar el mensaje que algún cliente ha enviado a
	// todos los otros clientes
	public void replicarMensaje(String msj) {
		DataOutputStream salida;

		// este for recorre toads las conexiones
		for (HiloCliente conexion : conexiones) {
			// este if conprueba que la conexión esté activa y disponible
			if (!conexion.getSocket().isClosed()) {
				try {
					// se envía el mensaje
					// DataOutputStream que va a contener la información a enviar
					salida = new DataOutputStream(conexion.getSocket().getOutputStream());
					salida.writeUTF(msj);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		ventanaPrincipal.getTxtChat().append(msj + "\n");
		// estas dos lineas mantienen los ultimos mensajes del chat visibles en pantalla
		Document d = ventanaPrincipal.getTxtChat().getDocument();
		ventanaPrincipal.getTxtChat().select(d.getLength(), d.getLength());

	}

	// método que se encarga de enviar un mensaje privado.
	public void enviarMensajePrivado(String msj, Socket socketDestinatario) {
		DataOutputStream salida;
		if (!socketDestinatario.isClosed()) {
			try {
				// DataOutputStream que va a contener la información a enviar
				salida = new DataOutputStream(socketDestinatario.getOutputStream());
				salida.writeUTF(
						"[PRIVADO]" + socketDestinatario.getLocalAddress().getHostAddress() + "/Servidor: " + msj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// método que se encarga de mostrar un mensaje privado.
	public void mostrarMensajePrivado(String msj, Socket socketCliente, String nick) {
		VentanaChatPrivado ventanaPrivada = ventanaPrincipal.getVentanaPrivadaCliente(socketCliente);
		ventanaPrivada.getFrmChatServidor().setTitle(socketCliente.getInetAddress().getHostAddress() + "/" + nick);
		ventanaPrivada.getFrmChatServidor().setVisible(true);
		ventanaPrivada.getTxtChat().append(msj + "\n");
		// estas dos lineas mantienen los ultimos mensajes del chat visibles en pantalla
		Document d = ventanaPrivada.getTxtChat().getDocument();
		ventanaPrivada.getTxtChat().select(d.getLength(), d.getLength());
	}

	// método que se encarga de mostrar un chat privado.
	public void mostrarVentanaPrivada(int codigoHash) {
		HiloCliente conexion = getConexion(codigoHash);
		VentanaChatPrivado ventanaPrivada = ventanaPrincipal.getVentanaPrivadaCliente(conexion.getSocket());
		ventanaPrivada.getFrmChatServidor()
				.setTitle(conexion.getSocket().getInetAddress().getHostAddress() + "/" + conexion.getNick());
		ventanaPrivada.getFrmChatServidor().setVisible(true);
	}

	// este método actualiza la lista de conectados a la derecha de la ventana
	// principal
	public void actualizarConectados() {
		ventanaPrincipal.getTxtConectados().setText("");
		for (HiloCliente conexion : conexiones) {
			if (!conexion.getSocket().isClosed()) {

				ventanaPrincipal.getTxtConectados()
						.setText("<a href=\"" + conexion.getSocket().hashCode() + "\">"
								+ conexion.getSocket().getInetAddress().getHostAddress() + "/" + conexion.getNick()
								+ "</a>" + "\n" + ventanaPrincipal.getTxtConectados().getText());

			}
		}
	}

	// obtiene una conexion con un cliente a partir del código hash de su socket
	private HiloCliente getConexion(int codigo) {
		for (HiloCliente hiloCliente : conexiones) {
			if (hiloCliente.getSocket().hashCode() == codigo) {
				return hiloCliente;
			}
		}
		return null;
	}

	public Servidor getThis() {
		return this;
	}

	public VentanaPrincipal getVentanaPrincipal() {
		return ventanaPrincipal;
	}

}