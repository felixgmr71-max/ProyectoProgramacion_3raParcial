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
	int idUserModify = 0;
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
				
				dispose(); //Cerramos la ventana
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
			        // Recuperamos el usuario de las cajas de texto
			        String usuario = txtUsuario.getText().trim();
			        // Obtenemos la contraseña y convertimos a texto
			        String password = new String(passwordField.getPassword()).trim(); 
			        
			        // Determinamos el rol
			        int rolId = 0;
			        if (rdbAdmin.isSelected()) {
			            rolId = 1;
			        } else if (rdbtnVendedor.isSelected()) {
			            rolId = 2;
			        }
			        
			        // validaciones
			        if (usuario.isEmpty() || password.isEmpty() || rolId == 0) {
			            JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos y selecciona un rol.", "Aviso", JOptionPane.WARNING_MESSAGE);
			            return; // detenemos al usuario en caso de faltar info
			        }
			        
			        // hacemos la conexion y dejamos preparada la sentencia
			        Conexion conDB = new Conexion();
			        String sql;

			        // checamos si es modificacion o insercion
			        if (EsModificacion == true) {
			            sql = "UPDATE usuarios SET username = ?, password = ?, rol_id = ? WHERE id_usuario = ?";
			        } else {
			            sql = "INSERT INTO usuarios (username, password, rol_id) VALUES (?, ?, ?)";
			        }

			        try (Connection conn = conDB.conectar();
			             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

			            pstmt.setString(1, usuario);
			            pstmt.setString(2, password);
			            pstmt.setInt(3, rolId);
			            
			            // Si es modificación agregamos el ID al final para el WHERE
			            if (EsModificacion == true) {
			                pstmt.setInt(4, idUserModify);
			            }
			            
			            int registros = pstmt.executeUpdate();
			            
			            if (registros > 0) {
			                if (EsModificacion == true) {
			                    JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente.");
			                    EsModificacion = false; // Apagamos la bandera
			                    btnIngresar.setText("Ingresar"); // Regresamos el texto a la normalidad
			                } else {
			                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
			                }
			                
			                // limpiamos los campos
			                txtUsuario.setText("");
			                passwordField.setText("");
			                grupo.clearSelection();
			                table.clearSelection();
			                
			                // se actualiza la tabla
			                cargarDatos(); 
			            }
			            
			        } catch (SQLException error) {			        
			            JOptionPane.showMessageDialog(null, "Ocurrió un error: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			        }
			}
		});
		btnIngresar.setBounds(10, 235, 143, 20);
		contentPane.add(btnIngresar);
		
		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = table.getSelectedRow();
		        
		        if (filaSeleccionada == -1) {
		            JOptionPane.showMessageDialog(null, "Por favor, selecciona un usuario de la tabla para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        // le pasamos el id a la variable global
		        idUserModify = (int) table.getValueAt(filaSeleccionada, 0);

		        // cargamos las cajas
		        txtUsuario.setText(table.getValueAt(filaSeleccionada, 1).toString());
		        passwordField.setText(table.getValueAt(filaSeleccionada, 2).toString());
		        
		        // se selecciona el rd button
		        int rol = (int) table.getValueAt(filaSeleccionada, 3);
		        if(rol == 1) {
		            rdbAdmin.setSelected(true);
		        } else if(rol == 2) {
		            rdbtnVendedor.setSelected(true);
		        }

		        // se prende la bandera
		        EsModificacion = true;
		        
		        //Le cambiamos el texto al botón Ingresar para que el usuario entienda
		        btnIngresar.setText("Guardar Cambios");
			}
		});
		btnModificar.setBounds(10, 265, 143, 20);
		contentPane.add(btnModificar);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				
				if (fila == -1) {
		            JOptionPane.showMessageDialog(null, "Por favor, selecciona un usuario de la tabla para removerlo", "Aviso", JOptionPane.WARNING_MESSAGE);
		            return;
		        }
				
		        // sacamos el id del usuario
		        int idUsuarioBorrar = (int) table.getValueAt(fila, 0);

		        // verificamos
		        int respuesta = JOptionPane.showConfirmDialog(null, 
		                "¿Estás completamente seguro de que deseas eliminar a este usuario?", 
		                "Confirmar Eliminación", 
		                JOptionPane.YES_NO_OPTION, 
		                JOptionPane.WARNING_MESSAGE);

		        // en caso de que sí, borramos
		        if (respuesta == JOptionPane.YES_OPTION) {
		            
		            Conexion conDB = new Conexion();
		            // Sentencia SQL: Borra de la tabla usuarios SOLAMENTE donde el ID coincida
		            String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

		            try (Connection conn = conDB.conectar();
		                 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
		                
		                // pasamos el id al signo de interrogacion
		                pstmt.setInt(1, idUsuarioBorrar);
		                
		                // se borra
		                int registros = pstmt.executeUpdate();
		                
		                if (registros > 0) {
		                    JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.");
		                    
		                    // Limpiamos las cajas y deseleccionamos todo
		                    txtUsuario.setText("");
		                    passwordField.setText("");
		                    grupo.clearSelection();
		                    table.clearSelection();
		                    
		                    // Actualizamos la tabla para que el usuario desaparezca de la pantalla
		                    cargarDatos(); 
		                }
		                
		            } catch (SQLException error) {			        
		                JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		        // Si responde que "No", no hacemos nada y la ventana se queda igual
			}
		});
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
	
	//funcion para mostrar los datos de nuetra base en la tabla
	public void cargarDatos() {
		// definimos el modelo
	    DefaultTableModel modelo = new DefaultTableModel();
	    modelo.addColumn("ID");
	    modelo.addColumn("Nombre de Usuario");
	    modelo.addColumn("Contraseña");
	    modelo.addColumn("Rol");
	    
	    table.setModel(modelo);
	    modelo.setRowCount(0); //limpiar la tabla

	    Conexion conDB = new Conexion();
	    conDB.inicializarBaseDeDatos();

	    //nos conectamos a la bd y mandamos sentencia
	    String sql = "SELECT id_usuario, username, password, rol_id FROM usuarios"; 

	    try (Connection conn = conDB.conectar();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        // se van recorriendo y agregando los datos al modelo
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
	    //propiedades para no mostrar el id en la tabla
	    table.getColumnModel().getColumn(0).setMinWidth(0);
	    table.getColumnModel().getColumn(0).setMaxWidth(0);
	    table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
}
	

