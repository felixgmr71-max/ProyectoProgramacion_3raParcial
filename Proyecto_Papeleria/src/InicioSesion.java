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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//para bd
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

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
					// 1. Preparamos la BD antes de mostrar algo
					Conexion con = new Conexion();
					con.inicializarBaseDeDatos();
					
					// 2. Mostramos la ventana
					InicioSesion frame = new InicioSesion();
					frame.setLocationRelativeTo(null); 
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
		setTitle("Papelería");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 64, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Hacemos que el panel principal pueda recibir el foco inicial
		contentPane.setFocusable(true);
		
		JLabel lblNewLabel = new JLabel("Inicio de Sesión");
		lblNewLabel.setForeground(new Color(255, 255, 255));
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
		
		// Eventos de foco para txtUsuario
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
		String placeholder = "Contraseña...";
		passwordField.setForeground(new Color(192, 192, 192));
		passwordField.setText(placeholder);
		passwordField.setEchoChar((char) 0);
		contentPane.add(passwordField);
		
		// Eventos de foco para passwordField
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
		btnIngresar.setBackground(new Color(240, 240, 240));
		btnIngresar.setForeground(new Color(0, 128, 192));
		btnIngresar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String usr = txtUsuario.getText();
				String pass = new String(passwordField.getPassword());
				
				if (usr.equals("Usuario...") || pass.equals("Contraseña...") || usr.isEmpty() || pass.isEmpty()) {
					javax.swing.JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos.");
					return;
				}
				
				Conexion con = new Conexion();
				String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
				
				try (java.sql.Connection acceso = con.conectar();
				     java.sql.PreparedStatement ps = acceso.prepareStatement(sql)) {
					
					ps.setString(1, usr);
					ps.setString(2, pass);
					
					java.sql.ResultSet rs = ps.executeQuery();
					
					if (rs.next()) {
						// === AQUÍ CAPTURAMOS EL ROL DE LA BASE DE DATOS ===
						// Sustituye "rol" por el nombre real de tu columna en SQLite
						String rolUsuario = rs.getString("rol_id"); 
						
						javax.swing.JOptionPane.showMessageDialog(null, "¡Bienvenido, " + rs.getString("username") + "!");
						
						// Le mandamos el rol como parámetro al constructor del menú
						MenuPrincipal menu = new MenuPrincipal(rolUsuario);
						menu.setVisible(true);
						
						dispose(); 
					} else {
						javax.swing.JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
					}
					
				} catch (Exception ex) {
					javax.swing.JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage());
				}
			}
		});
		btnIngresar.setBounds(148, 167, 140, 45);
		contentPane.add(btnIngresar);
		
		this.getRootPane().setDefaultButton(btnIngresar);

		// Evitar que txtUsuario gane el foco al iniciar y borre el placeholder
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				contentPane.requestFocusInWindow();
			}
		});
	}
}