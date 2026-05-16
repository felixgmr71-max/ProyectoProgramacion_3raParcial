import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;

public class MenuCatalogoUsuarios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField txtUsuario;
	private JPasswordField passwordField;
	
	
	
	boolean EsModificacion = false;
	//DefaultTableModel modelo = new DefaultTableModel();
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuCatalogoUsuarios frame = new MenuCatalogoUsuarios();
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
	public MenuCatalogoUsuarios() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 690, 425);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCatalogosDeUsuarios = new JLabel("Cátalogos de Usuarios");
		lblCatalogosDeUsuarios.setBounds(177, 10, 338, 38);
		lblCatalogosDeUsuarios.setFont(new Font("Century Gothic", Font.BOLD, 30));
		contentPane.add(lblCatalogosDeUsuarios);
		
		JButton BtnSalir = new JButton("Volver al menú principal");
		BtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Volvemos al menú principal
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnSalir.setBounds(496, 358, 170, 20);
		contentPane.add(BtnSalir);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(177, 58, 489, 290);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		cargarDatos();
		
		
		
		JLabel lblNewLabel = new JLabel("Nombre de Usuario");
		lblNewLabel.setBounds(10, 60, 110, 12);
		contentPane.add(lblNewLabel);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(10, 82, 143, 18);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblContrasea = new JLabel("Contraseña");
		lblContrasea.setBounds(10, 112, 110, 12);
		contentPane.add(lblContrasea);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(10, 134, 143, 18);
		contentPane.add(passwordField);
		
		JRadioButton rdbAdmin = new JRadioButton("Admin");
		rdbAdmin.setBounds(91, 196, 64, 20);
		contentPane.add(rdbAdmin);
		
		JRadioButton rdbtnVendedor = new JRadioButton("Vendedor");
		rdbtnVendedor.setBounds(6, 196, 83, 20);
		contentPane.add(rdbtnVendedor);
		
		ButtonGroup grupo = new ButtonGroup();
		grupo.add(rdbAdmin);
		grupo.add(rdbtnVendedor);
		
		JLabel lblNewLabel_1 = new JLabel("Rol del usuario");
		lblNewLabel_1.setBounds(10, 178, 102, 12);
		contentPane.add(lblNewLabel_1);
		
		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			        // 1. Obtener los datos de los componentes
			        String usuario = txtUsuario.getText().trim();
			        // Para los JPasswordField se recomienda usar getPassword() y convertirlo a String
			        String password = new String(passwordField.getPassword()).trim(); 
			        
			        // Determinar el Rol (Suponiendo que 1 = Admin y 2 = Vendedor)
			        int rolId = 0;
			        if (rdbAdmin.isSelected()) {
			            rolId = 1;
			        } else if (rdbtnVendedor.isSelected()) {
			            rolId = 2;
			        }
			        
			        // 2. Validaciones básicas
			        if (usuario.isEmpty() || password.isEmpty() || rolId == 0) {
			            JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos y selecciona un rol.", "Aviso", JOptionPane.WARNING_MESSAGE);
			            return; // Detenemos la ejecución si falta algo
			        }
			        
			        // 3. Conexión y Sentencia SQL con PreparedStatement (Uso de '?')
			        Conexion conDB = new Conexion();
			        String sql = "INSERT INTO usuarios (username, password, rol_id) VALUES (?, ?, ?)";
			        
			        // Usamos try-with-resources para que la conexión se cierre sola al terminar
			        try (Connection conn = conDB.conectar();
			             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
			            
			            // Sustituimos los '?' por nuestras variables
			            pstmt.setString(1, usuario);
			            pstmt.setString(2, password);
			            pstmt.setInt(3, rolId);
			            
			            // Ejecutamos la inserción
			            int registros = pstmt.executeUpdate();
			            
			            if (registros > 0) {
			                JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
			                
			                // Limpiamos el formulario para el siguiente ingreso
			                txtUsuario.setText("");
			                passwordField.setText("");
			                grupo.clearSelection();
			                
			                // ¡Actualizamos la tabla
			                cargarDatos(); 
			            }
			            
			        } catch (SQLException error) {
			            // Manejo de errores (por ejemplo, si el username ya existe porque es UNIQUE)
			            JOptionPane.showMessageDialog(null, "Ocurrió un error al guardar: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			        }
			}
		});
		btnIngresar.setBounds(10, 235, 143, 20);
		contentPane.add(btnIngresar);
		
		JButton btnModificar = new JButton("Modificar");
		btnModificar.setBounds(10, 265, 143, 20);
		contentPane.add(btnModificar);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(10, 294, 143, 20);
		contentPane.add(btnEliminar);
		
		
		/*Programamos la tecla ESC para que al presionarla regrese al menú principal*/
		
		//1.Definimos que la tecla a escuchar es el ESC
		javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
				
		//2.Le decimos a la ventana que escuche esa tecla siempre que esté activa
		this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
				
		//3.Le decimos qué hacer cuando detecte la pulsación
		this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
			public void actionPerformed(ActionEvent e) {
						
				BtnSalir.doClick(); //Simula un clic en tu botón de salir
				
			}
		});

	}
	
	public void cargarDatos() {
		// 1. Definir el modelo de la tabla
	    DefaultTableModel modelo = new DefaultTableModel();
	    modelo.addColumn("ID");
	    modelo.addColumn("Nombre de Usuario");
	    modelo.addColumn("Contraseña");
	    modelo.addColumn("Rol");
	    
	    table.setModel(modelo);
	    modelo.setRowCount(0);

	    // NUEVO: Asegurarnos de que la BD existe y tiene los datos de prueba antes de consultarla
	    Conexion conDB = new Conexion();
	    conDB.inicializarBaseDeDatos();

	    // 2. Conectar a SQLite y consultar
	    // Usamos el método conectar() de tu propia clase para no repetir código
	    String sql = "SELECT id_usuario, username, password, rol_id FROM usuarios"; 

	    try (Connection conn = conDB.conectar();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        // 3. Recorrer los resultados y agregarlos al modelo
	        while (rs.next()) {
	            Object[] fila = new Object[4]; 
	            fila[0] = rs.getInt("id_usuario");
	            fila[1] = rs.getString("username");
	            fila[2] = rs.getString("password");
	            fila[3] = rs.getInt("rol_id");
	            
	            modelo.addRow(fila);
	        }

	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
	    }
    }
}
	

