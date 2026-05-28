import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private JRadioButton rdbAdmin;
	private JRadioButton rdbtnVendedor;
	private ButtonGroup grupoRoles;
	
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	
	// Variables globales para la modificación
	int idUserModify = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuCatalogoUsuarios frame = new MenuCatalogoUsuarios();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuCatalogoUsuarios() {
		setTitle("Catálogo de Usuarios");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 500); // Mismo tamaño que proveedores
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// --- PANEL NORTE (TÍTULO) ---
		JPanel pnlNorte = new JPanel();
		pnlNorte.setBackground(new Color(0, 64, 128));
		contentPane.add(pnlNorte, BorderLayout.NORTH);
		
		JLabel lblTitulo = new JLabel("Catálogo de Usuarios");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlNorte.add(lblTitulo);

		// --- PANEL SUR (BOTONES) ---
		JPanel pnlSur = new JPanel();
		pnlSur.setBackground(new Color(0, 64, 128));
		pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
		contentPane.add(pnlSur, BorderLayout.SOUTH);
		
		btnAgregar = new JButton("Agregar Usuario");
		btnAgregar.setForeground(new Color(0, 128, 192));
		btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usuario = txtUsuario.getText().trim();
				String password = new String(passwordField.getPassword()).trim();
				
				int rolId = 0;
				if (rdbAdmin.isSelected()) {
					rolId = 1;
				} else if (rdbtnVendedor.isSelected()) {
					rolId = 2;
				}
				
				// Validaciones
				if (usuario.isEmpty() || password.isEmpty() || rolId == 0) {
					JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos y selecciona un rol.", "AVISO", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				Conexion conDB = new Conexion();
				String sql = "INSERT INTO usuarios (username, password, rol_id) VALUES (?, ?, ?)";

				try (Connection conn = conDB.conectar();
					 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

					pstmt.setString(1, usuario);
					pstmt.setString(2, password);
					pstmt.setInt(3, rolId);
					
					int registros = pstmt.executeUpdate();
					if (registros > 0) {
						JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.", "PROCESO EXITOSO", JOptionPane.INFORMATION_MESSAGE);
						limpiar();
						cargarDatos();
					}
					
				} catch (SQLException error) {
					JOptionPane.showMessageDialog(null, "Ocurrió un error: " + error.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnlSur.add(btnAgregar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setForeground(new Color(0, 128, 192));
		btnModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (idUserModify == 0) {
					JOptionPane.showMessageDialog(null, "Por favor, selecciona un usuario de la tabla para modificar.", "ACCIÓN REQUERIDA", JOptionPane.WARNING_MESSAGE);
					return;
				}

				
				if(btnModificar.getText().equals("Modificar")) {
					int fila = table.getSelectedRow();
					
					txtUsuario.setText(table.getValueAt(fila, 1).toString());
					// JALAMOS LA CONTRASEÑA DE LA COLUMNA OCULTA (Índice 4)
					passwordField.setText(table.getValueAt(fila, 4).toString());
					
					String rol = table.getValueAt(fila, 3).toString();
					if(rol.equals("Admin")) {
						rdbAdmin.setSelected(true);
					} else {
						rdbtnVendedor.setSelected(true);
					}

					btnModificar.setText("Guardar Cambios");
					btnAgregar.setEnabled(false);
					btnEliminar.setEnabled(false);
					table.setEnabled(false);
				} 
		
				else {
					String usuario = txtUsuario.getText().trim();
					String password = new String(passwordField.getPassword()).trim();
					int rolId = rdbAdmin.isSelected() ? 1 : (rdbtnVendedor.isSelected() ? 2 : 0);
					
					if (usuario.isEmpty() || password.isEmpty() || rolId == 0) {
						JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos y selecciona un rol.", "AVISO", JOptionPane.WARNING_MESSAGE);
						return;
					}

					Conexion conDB = new Conexion();
					String sql = "UPDATE usuarios SET username = ?, password = ?, rol_id = ? WHERE id_usuario = ?";

					try (Connection conn = conDB.conectar();
						 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

						pstmt.setString(1, usuario);
						pstmt.setString(2, password);
						pstmt.setInt(3, rolId);
						pstmt.setInt(4, idUserModify);
						
						int registros = pstmt.executeUpdate();
						if (registros > 0) {
							JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente.", "ACTUALIZACIÓN EXITOSA", JOptionPane.INFORMATION_MESSAGE);
							limpiar();
							cargarDatos();
						}
					} catch (SQLException error) {
						JOptionPane.showMessageDialog(null, "Ocurrió un error: " + error.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlSur.add(btnModificar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setForeground(new Color(0, 128, 192));
		btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (idUserModify == 0) {
					JOptionPane.showMessageDialog(null, "Por favor, selecciona un usuario de la tabla para removerlo.", "ACCIÓN REQUERIDA", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				int respuesta = JOptionPane.showConfirmDialog(null, 
						"¿Estás completamente seguro de que deseas eliminar a este usuario?", 
						"CONFIRMAR ELIMINACIÓN", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE);

				if (respuesta == JOptionPane.YES_OPTION) {
					Conexion conDB = new Conexion();
					String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

					try (Connection conn = conDB.conectar();
						 java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
						
						pstmt.setInt(1, idUserModify);
						int registros = pstmt.executeUpdate();
						
						if (registros > 0) {
							JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.", "ELIMINACIÓN EXITOSA", JOptionPane.INFORMATION_MESSAGE);
							limpiar();
							cargarDatos();
						}
					} catch (SQLException error) {
						JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar: " + error.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlSur.add(btnEliminar);
		
		JButton btnSalir = new JButton("Volver al menú principal");
		btnSalir.setForeground(new Color(0, 128, 192));
		btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				dispose();
			}
		});
		pnlSur.add(btnSalir);

		// --- PANEL CENTRO (FORMULARIO Y TABLA) ---
		JPanel pnlCentro = new JPanel();
		pnlCentro.setBackground(Color.WHITE);
		pnlCentro.setLayout(new GridLayout(1, 2, 20, 0));
		pnlCentro.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.add(pnlCentro, BorderLayout.CENTER);

		// Sub-panel izquierdo: Formulario
		JPanel pnlFormulario = new JPanel();
		pnlFormulario.setBackground(Color.WHITE);
		pnlFormulario.setLayout(null);
		pnlCentro.add(pnlFormulario);

		JLabel lblSubtitulo = new JLabel("Datos del Usuario");
		lblSubtitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSubtitulo.setForeground(new Color(0, 64, 128));
		lblSubtitulo.setBounds(20, 10, 250, 20);
		pnlFormulario.add(lblSubtitulo);

		JLabel lblUsuario = new JLabel("Nombre de Usuario:");
		lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblUsuario.setBounds(20, 60, 150, 20);
		pnlFormulario.add(lblUsuario);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(20, 85, 250, 25);
		pnlFormulario.add(txtUsuario);
		
		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblContrasena.setBounds(20, 125, 150, 20);
		pnlFormulario.add(lblContrasena);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(20, 150, 250, 25);
		pnlFormulario.add(passwordField);
		
		JLabel lblRol = new JLabel("Rol del Sistema:");
		lblRol.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblRol.setBounds(20, 190, 150, 20);
		pnlFormulario.add(lblRol);
		
		rdbtnVendedor = new JRadioButton("Vendedor");
		rdbtnVendedor.setBackground(Color.WHITE);
		rdbtnVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		rdbtnVendedor.setBounds(20, 215, 100, 20);
		pnlFormulario.add(rdbtnVendedor);

		rdbAdmin = new JRadioButton("Admin");
		rdbAdmin.setBackground(Color.WHITE);
		rdbAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		rdbAdmin.setBounds(120, 215, 100, 20);
		pnlFormulario.add(rdbAdmin);
		
		grupoRoles = new ButtonGroup();
		grupoRoles.add(rdbAdmin);
		grupoRoles.add(rdbtnVendedor);

		// Sub-panel derecho: Tabla
		JPanel pnlTabla = new JPanel(new BorderLayout());
		pnlTabla.setBackground(Color.WHITE);
		pnlCentro.add(pnlTabla);

		JScrollPane scrollPane = new JScrollPane();
		pnlTabla.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		
		// Diseño de la tabla
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(0, 91, 159));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setGridColor(new Color(102, 167, 215));
		table.setSelectionForeground(Color.WHITE);
		table.setSelectionBackground(new Color(0, 64, 128));
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.setDefaultEditor(Object.class, null); // No editable dando doble clic
		table.getTableHeader().setReorderingAllowed(false);
		
		scrollPane.setViewportView(table);
		
		// Evento al dar clic en la tabla
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = table.getSelectedRow();
				if(fila >= 0) {
					// Guardamos el ID en sistema
					idUserModify = Integer.parseInt(table.getValueAt(fila, 0).toString());
				}
			}
		});

		// Atajo ESC
		javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
		this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				btnSalir.doClick();
			}
		});

		cargarDatos();
	}
	
	public void cargarDatos() {
		DefaultTableModel modelo = new DefaultTableModel();
		modelo.addColumn("ID");
		modelo.addColumn("Usuario");
		modelo.addColumn("Contraseña"); // Visual
		modelo.addColumn("Rol");
		modelo.addColumn("ContraseñaReal"); // Oculta
		
		table.setModel(modelo);
		modelo.setRowCount(0);

		Conexion conDB = new Conexion();
		String sql = "SELECT id_usuario, username, password, rol_id FROM usuarios";

		try (Connection conn = conDB.conectar();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Object[] fila = new Object[5];
				fila[0] = rs.getInt("id_usuario");
				fila[1] = rs.getString("username");
				fila[2] = "********"; // Máscara para la vista del usuario
				
				// Transformamos el número a Texto para que se vea bonito
				int rolId = rs.getInt("rol_id");
				fila[3] = (rolId == 1) ? "Admin" : "Vendedor";
				
				// Guardamos la contraseña real en la última columna invisible
				fila[4] = rs.getString("password");
				
				modelo.addRow(fila);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
		}
		
		// Ocultar ID
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setPreferredWidth(0);
		
		// Ocultar Contraseña Real
		table.getColumnModel().getColumn(4).setMinWidth(0);
		table.getColumnModel().getColumn(4).setMaxWidth(0);
		table.getColumnModel().getColumn(4).setPreferredWidth(0);
	}
	
	public void limpiar() {
		txtUsuario.setText("");
		passwordField.setText("");
		grupoRoles.clearSelection();
		idUserModify = 0;
		
		if(btnModificar != null && btnAgregar != null && btnEliminar != null && table != null) {
			btnModificar.setText("Modificar");
			btnAgregar.setEnabled(true);
			btnEliminar.setEnabled(true);
			table.setEnabled(true);
			table.clearSelection();
		}
	}
}
