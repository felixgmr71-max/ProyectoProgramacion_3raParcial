import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class InicioSesion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InicioSesion frame = new InicioSesion();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InicioSesion() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Inicio de Sesión");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		lblNewLabel.setBounds(95, 10, 253, 45);
		contentPane.add(lblNewLabel);
		
		txtUsuario = new JTextField();
		txtUsuario.setForeground(new Color(192, 192, 192));
		txtUsuario.setFont(new Font("Garamond", Font.PLAIN, 25));
		txtUsuario.setText("Usuario...");
		txtUsuario.setBounds(64, 65, 310, 27);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		txtUsuario.setFocusable(false);
		
		txtUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
			    	txtUsuario.setFocusable(true);
			    	txtUsuario.requestFocusInWindow();
		    }
		});
		
		txtUsuario.addFocusListener(new FocusAdapter() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (txtUsuario.getText().equals("Usuario...")) {
		            txtUsuario.setText("");
		            txtUsuario.setForeground(Color.BLACK);
		        }
		    }

		    @Override
		    public void focusLost(FocusEvent e) {
		        if (txtUsuario.getText().isEmpty()) {
		            txtUsuario.setText("Usuario...");
		            txtUsuario.setForeground(new Color(192, 192, 192));
		        }
		    }
		});
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Garamond", Font.PLAIN, 25));
		passwordField.setBounds(64, 116, 310, 27);
		contentPane.add(passwordField);
		String placeholder = "Contraseña...";
		passwordField.setForeground(new Color(192, 192, 192));
		passwordField.setText(placeholder);
		passwordField.setEchoChar((char) 0);
		contentPane.add(passwordField);
		passwordField.setFocusable(false);
		
		passwordField.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    	passwordField.setFocusable(true);
		    	passwordField.requestFocusInWindow();
		    }
		});
		
		passwordField.addFocusListener(new FocusAdapter() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        String pass = new String(passwordField.getPassword());
		        
		        if (pass.equals(placeholder)) {
		            passwordField.setText("");
		            passwordField.setForeground(Color.BLACK);
		            passwordField.setEchoChar('•'); 
		        }
		    }

		    @Override
		    public void focusLost(FocusEvent e) {
		        String pass = new String(passwordField.getPassword());
		        
		        if (pass.isEmpty()) {
		            passwordField.setText(placeholder);
		            passwordField.setForeground(new Color(192, 192, 192));
		            passwordField.setEchoChar((char) 0); 
		        }
		    }
		});
		
		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.setBounds(183, 174, 84, 20);
		contentPane.add(btnIngresar);

	}
}
