package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import logic.Servidor;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Toolkit;
import java.net.Socket;
import java.util.ArrayList;


public class VentanaPrincipal {

	private JFrame frmChatServidor;
	private GestionEventos eventos;
	private Servidor servidor;
	private JTextArea txtChat;
	private JTextField txtMsj;
	private JTextPane txtConectados;
	private ArrayList<VentanaChatPrivado> ventanaChatsPrivados;

	//Constructor de la ventana principal
	public VentanaPrincipal(int puerto) {
		initialize();
		//se intancia la clase servidor con puerto, 30 conexiones simultaneas y una referecia de esta ventana para que
		//el servidor pueda enviar los mensajes del chat a los JTextField y JTextArea correpondientes
		servidor = new Servidor(puerto, 30, this);
		//se incia el servidor, el servidor empieza a escuchar conexiones entrantes en puerto especificado
		servidor.iniciarServidor();
	}

	
	//método que inicializa todos los componentes de la ventana
	private void initialize() {

		eventos = new GestionEventos(this);
		ventanaChatsPrivados = new ArrayList<>();
		frmChatServidor = new JFrame();
		frmChatServidor.setResizable(false);
		frmChatServidor.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaPrincipal.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		frmChatServidor.setTitle("Chat Servidor");
		frmChatServidor.setBounds(100, 100, 838, 464);
		frmChatServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatServidor.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 11, 620, 347);
		frmChatServidor.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 0, 620, 347);
		panel.add(scrollPane);

		txtChat = new JTextArea();
		txtChat.setLineWrap(true);
		scrollPane.setViewportView(txtChat);
		txtChat.setFocusable(false);
		txtChat.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		txtChat.setEditable(false);

		txtMsj = new JTextField();
		txtMsj.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		txtMsj.setBounds(10, 373, 507, 49);
		frmChatServidor.getContentPane().add(txtMsj);
		txtMsj.setColumns(10);
		txtMsj.addKeyListener(eventos);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		btnEnviar.setBounds(527, 373, 103, 49);
		btnEnviar.setActionCommand(GestionEventos.ENVIAR);
		btnEnviar.addActionListener(eventos);
		frmChatServidor.getContentPane().add(btnEnviar);

		JLabel lblClientesConectados = new JLabel("Clientes conectados:");
		lblClientesConectados.setBounds(645, 11, 172, 16);
		frmChatServidor.getContentPane().add(lblClientesConectados);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(642, 27, 175, 395);
		frmChatServidor.getContentPane().add(scrollPane_1);

		txtConectados = new JTextPane();
		txtConectados.setEditable(false);
		txtConectados.setFocusable(false);
		txtConectados.setBackground(UIManager.getColor("Button.light"));
		txtConectados.setContentType("text/html"); 
		txtConectados.addHyperlinkListener(eventos);
		scrollPane_1.setViewportView(txtConectados);

	}
	
	//método que envia un mensaje a la clase servidor para que lo distribuya a todos los clientes conectados
	public void enviarMensaje() {
		if (txtMsj.getText().trim().length() > 0) {
			servidor.enviarMensaje(txtMsj.getText());
			txtMsj.setText("");
		}

	}
	
	public void crearVentanaChatPrivado(Socket socketCliente) {
		VentanaChatPrivado ventana = new VentanaChatPrivado(socketCliente, servidor);
		ventanaChatsPrivados.add(ventana);
	}
	
	public VentanaChatPrivado getVentanaPrivadaCliente(Socket socket) {
		for (VentanaChatPrivado ventanaChatPrivado : ventanaChatsPrivados) {
			if (ventanaChatPrivado.getSocketCliente().equals(socket)) {
				return ventanaChatPrivado;
			}
		}
		return null;
	}

	public JTextArea getTxtChat() {
		return txtChat;
	}

	public JTextField getTxtMsj() {
		return txtMsj;
	}

	public JTextPane getTxtConectados() {
		return txtConectados;
	}
	
	

	public Servidor getServidor() {
		return servidor;
	}


	//metodo main que muestra la ventana principal
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					//Se pide al usuario el puerto en el cual el servidor escuchará las conexiones entrantes.
					int puerto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese puerto", "5050"));
					VentanaPrincipal window = new VentanaPrincipal(puerto);
					//Se muestra la ventana
					window.frmChatServidor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
