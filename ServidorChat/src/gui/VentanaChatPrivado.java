package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.text.Document;

import logic.Servidor;

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
import java.net.Socket;

import javax.swing.ImageIcon;

public class VentanaChatPrivado {

	private JFrame frmChatServidor;
	private GestionEventosChatPrivado eventos;
	private Servidor servidor;
	private JTextArea txtChat;
	private JTextField txtMsj;
	private Socket socketCliente;

	//Constructor de la ventana
	public VentanaChatPrivado(Socket socketCliente, Servidor servidor) {
		this.socketCliente = socketCliente;
		this.servidor = servidor;
		initialize();
	}

	
	//método que inicializa todos los componentes de la ventana
	private void initialize() {
		
		GestionEventosChatPrivado eventos = new GestionEventosChatPrivado(this);
		frmChatServidor = new JFrame();
		frmChatServidor.setResizable(false);
		frmChatServidor.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaPrincipal.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		frmChatServidor.setTitle("Chat Servidor");
		frmChatServidor.setBounds(100, 100, 649, 464);
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
		txtChat.setText("Sesi\u00F3n privada iniciada.\r\n");
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
		btnEnviar.setActionCommand(GestionEventosChatPrivado.ENVIAR_PRIVADO);
		btnEnviar.addActionListener(eventos);
		frmChatServidor.getContentPane().add(btnEnviar);

	}
	
	//método que envia un mensaje privado a un cliente
	public void enviarMensajePrivado() {
		if (txtMsj.getText().trim().length() > 0) {
			servidor.enviarMensajePrivado(txtMsj.getText(), socketCliente);
			txtChat.append("Servidor: " + txtMsj.getText() + "\n");
			txtMsj.setText("");
			//estas dos lineas mantienen los ultimos mensajes del chat visibles en pantalla
			Document d = txtChat.getDocument();
			txtChat.select(d.getLength(), d.getLength());
		}

	}


	public JTextField getTxtMsj() {
		return txtMsj;
	}


	public Socket getSocketCliente() {
		return socketCliente;
	}


	public JTextArea getTxtChat() {
		return txtChat;
	}


	public JFrame getFrmChatServidor() {
		return frmChatServidor;
	}
	
	

}
