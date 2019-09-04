package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import logic.Cliente;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class VentanaPrincipal {

	private JFrame frmChatServidor;
	private GestionEventos eventos;
	private Cliente cliente;
	private JTextArea txtChat;
	private JTextField txtMsj;
	private VentanaChatPrivado ventanaPrivada;

	// Constructor de la ventana principal
	public VentanaPrincipal() {
		initialize();
		cliente = new Cliente(this);
		ventanaPrivada = new VentanaChatPrivado(cliente);
	}

	// método que inicializa todos los componentes de la ventana
	private void initialize() {

		eventos = new GestionEventos(this);		
		frmChatServidor = new JFrame();
		frmChatServidor.setResizable(false);
		frmChatServidor.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaPrincipal.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		frmChatServidor.setTitle("Chat Cliente");
		frmChatServidor.setBounds(100, 100, 647, 483);
		frmChatServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatServidor.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 32, 620, 347);
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
		txtMsj.setBounds(10, 394, 507, 49);
		frmChatServidor.getContentPane().add(txtMsj);
		txtMsj.setColumns(10);
		txtMsj.addKeyListener(eventos);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		btnEnviar.setBounds(527, 394, 103, 49);
		btnEnviar.setActionCommand(GestionEventos.ENVIAR);
		btnEnviar.addActionListener(eventos);
		frmChatServidor.getContentPane().add(btnEnviar);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 641, 21);
		frmChatServidor.getContentPane().add(menuBar);

		JMenu mnOpciones = new JMenu("Opciones");
		menuBar.add(mnOpciones);

		JMenuItem mntmAbrirChatPrivado = new JMenuItem("Abrir chat privado con el servidor.");
		mntmAbrirChatPrivado.setActionCommand(GestionEventos.ABRIR_CHAT_PRIVADO);
		mntmAbrirChatPrivado.addActionListener(eventos);
		mnOpciones.add(mntmAbrirChatPrivado);

	}

	// método que envia un mensaje a la clase cliente para que lo envíe al servidor
	public void enviarMensaje() {
		if (txtMsj.getText().trim().length() > 0) {
			cliente.enviarMensaje(txtMsj.getText());
			txtMsj.setText("");
		}

	}
	

	public JTextArea getTxtChat() {
		return txtChat;
	}

	public JTextField getTxtMsj() {
		return txtMsj;
	}

	public VentanaChatPrivado getVentanaPrivada() {
		return ventanaPrivada;
	}

	// metodo main que muestra la ventana principal
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					VentanaPrincipal window = new VentanaPrincipal();
					window.frmChatServidor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
