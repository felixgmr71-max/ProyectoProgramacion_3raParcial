import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;

public class MenuClientes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	
	private JTextField txtNombre;
	private JTextField txtDireccion;
	private JFormattedTextField txtTelefono; 
	private JTextField txtCorreo;
	private JFormattedTextField txtFechaRegistro; 
	private JTextField txtBuscar;
	private JLabel lblContador; 

	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnEliminarTodo;

	DefaultTableModel modelo = new DefaultTableModel();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	int idCliente = 0; 
	String nombreOriginal = "";
	String direccionOriginal = "";
	String telefonoOriginal = "";
	String correoOriginal = "";
	String fechaOriginal = "";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuClientes frame = new MenuClientes();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuClientes() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\Downloads\\business_application_addmale_useradd_insert_add_user_client_2312.png"));
		setTitle("Catálogo de Clientes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 550); 
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
		
		JLabel lblTitulo = new JLabel("Directorio de Clientes");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
		pnlNorte.add(lblTitulo);

		// --- PANEL SUR (BOTONES) ---
		JPanel pnlSur = new JPanel();
		pnlSur.setBackground(new Color(0, 64, 128));
		pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		contentPane.add(pnlSur, BorderLayout.SOUTH);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setForeground(new Color(0, 128, 192));
		btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String telefonoLimpio = txtTelefono.getText().replace("_", "").trim();
				String fechaLimpia = txtFechaRegistro.getText().replace("_", "").replace("/", "").trim();

				if (txtNombre.getText().trim().isEmpty() ||
					txtDireccion.getText().trim().isEmpty() ||
					telefonoLimpio.length() < 10 || 
					txtCorreo.getText().trim().isEmpty() ||
					fechaLimpia.length() < 8) { 
					JOptionPane.showMessageDialog(null, "Por favor rellena todos los campos correctamente antes de agregar.", "DATOS INCOMPLETOS", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// --- VALIDACIÓN DE DUPLICADOS ---
				if(existeCliente(txtNombre.getText().trim(), 0)) {
					JOptionPane.showMessageDialog(null, "El cliente '" + txtNombre.getText().trim() + "' ya está registrado.", "REGISTRO DUPLICADO", JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					Conexion conexion = new Conexion();
					con = conexion.conectar();
					
					String sql = "INSERT INTO clientes(nombre_completo, direccion, telefono, correo, fecha_registro) VALUES(?,?,?,?,?)";
					ps = con.prepareStatement(sql);
					ps.setString(1, txtNombre.getText().trim());
					ps.setString(2, txtDireccion.getText().trim());
					ps.setString(3, txtTelefono.getText().trim());
					ps.setString(4, txtCorreo.getText().trim());
					ps.setString(5, txtFechaRegistro.getText().trim());

					ps.executeUpdate();
					JOptionPane.showMessageDialog(null, "¡Cliente agregado con éxito!", "PROCESO EXITOSO", JOptionPane.INFORMATION_MESSAGE);

					con.close();
					limpiar();
					mostrarDatos();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Error: " + e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnlSur.add(btnAgregar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setForeground(new Color(0, 128, 192));
		btnModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (idCliente == 0) {
					JOptionPane.showMessageDialog(null, "Primero selecciona un cliente de la tabla para modificarlo.", "ACCIÓN REQUERIDA", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if(btnModificar.getText().equals("Modificar")) {
					int fila = table.getSelectedRow();
					
					txtNombre.setText(table.getValueAt(fila, 1).toString());
					txtDireccion.setText(table.getValueAt(fila, 2).toString());
					txtTelefono.setText(table.getValueAt(fila, 3).toString());
					txtCorreo.setText(table.getValueAt(fila, 4).toString());
					txtFechaRegistro.setText(table.getValueAt(fila, 5).toString());
					
					nombreOriginal = txtNombre.getText().trim();
					direccionOriginal = txtDireccion.getText().trim();
					telefonoOriginal = txtTelefono.getText().trim();
					correoOriginal = txtCorreo.getText().trim();
					fechaOriginal = txtFechaRegistro.getText().trim();

					btnModificar.setText("Guardar Cambios");
					btnAgregar.setEnabled(false);
					btnEliminar.setEnabled(false);
					btnEliminarTodo.setEnabled(false);
					table.setEnabled(false);
				} 
				else {
					String nuevoNombre = txtNombre.getText().trim();
					String nuevaDireccion = txtDireccion.getText().trim();
					String nuevoTelefono = txtTelefono.getText().trim();
					String nuevoCorreo = txtCorreo.getText().trim();
					String nuevaFecha = txtFechaRegistro.getText().trim();

					if (nuevoNombre.equals(nombreOriginal) &&
						nuevaDireccion.equals(direccionOriginal) &&
						nuevoTelefono.equals(telefonoOriginal) &&
						nuevoCorreo.equals(correoOriginal) &&
						nuevaFecha.equals(fechaOriginal)) {
						JOptionPane.showMessageDialog(null, "No se detectaron cambios. Modifica al menos un campo.", "AVISO", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// validacion
					if(existeCliente(nuevoNombre, idCliente)) {
						JOptionPane.showMessageDialog(null, "El cliente '" + nuevoNombre + "' ya pertenece a otro registro.", "REGISTRO DUPLICADO", JOptionPane.WARNING_MESSAGE);
						return;
					}

					try {
						Conexion conexion = new Conexion();
						con = conexion.conectar();
						
						String sql = "UPDATE clientes SET nombre_completo=?, direccion=?, telefono=?, correo=?, fecha_registro=? WHERE id_cliente=?";
						ps = con.prepareStatement(sql);
						ps.setString(1, nuevoNombre);
						ps.setString(2, nuevaDireccion);
						ps.setString(3, nuevoTelefono);
						ps.setString(4, nuevoCorreo);
						ps.setString(5, nuevaFecha);
						ps.setInt(6, idCliente);

						ps.executeUpdate();
						JOptionPane.showMessageDialog(null, "¡Cliente modificado exitosamente!", "ACTUALIZACIÓN EXITOSA", JOptionPane.INFORMATION_MESSAGE);

						con.close();
						limpiar();
						mostrarDatos();
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Error: " + e2.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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
				if (idCliente == 0) {
					JOptionPane.showMessageDialog(null, "Primero selecciona un cliente de la tabla para eliminarlo.", "ACCIÓN REQUERIDA", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este cliente por completo?", "CONFIRMAR ELIMINACIÓN", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (respuesta == JOptionPane.YES_OPTION) {
					try {
						Conexion conexion = new Conexion();
						con = conexion.conectar();
						
						String sql = "DELETE FROM clientes WHERE id_cliente=?";
						ps = con.prepareStatement(sql);
						ps.setInt(1, idCliente);
						ps.executeUpdate();

						JOptionPane.showMessageDialog(null, "El cliente ha sido eliminado.", "ELIMINACIÓN EXITOSA", JOptionPane.INFORMATION_MESSAGE);

						con.close();
						limpiar();
						mostrarDatos();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlSur.add(btnEliminar);
		
		btnEliminarTodo = new JButton("Eliminar Todo");
		btnEliminarTodo.setForeground(Color.RED); 
		btnEliminarTodo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEliminarTodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int respuesta1 = JOptionPane.showConfirmDialog(null, "¡ATENCIÓN!\n¿Seguro que desea eliminar TODOS los clientes?", "CONFIRMACIÓN CRÍTICA", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

				if (respuesta1 == JOptionPane.YES_OPTION) {
					int respuesta2 = JOptionPane.showConfirmDialog(null, "Esta acción NO se puede deshacer.\n¿Está absolutamente seguro?", "ÚLTIMA ADVERTENCIA", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

					if (respuesta2 == JOptionPane.YES_OPTION) {
						try {
							Conexion conexion = new Conexion();
							con = conexion.conectar();
							
							String sql = "DELETE FROM clientes";
							ps = con.prepareStatement(sql);
							ps.executeUpdate();

							JOptionPane.showMessageDialog(null, "Todos los registros de clientes han sido eliminados.", "BASE DE DATOS VACIADA", JOptionPane.INFORMATION_MESSAGE);

							con.close();
							limpiar();
							mostrarDatos();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		pnlSur.add(btnEliminarTodo);
		
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

		JLabel lblSubtitulo = new JLabel("Datos del Cliente");
		lblSubtitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSubtitulo.setForeground(new Color(0, 64, 128));
		lblSubtitulo.setBounds(20, 10, 250, 20);
		pnlFormulario.add(lblSubtitulo);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNombre.setBounds(20, 60, 100, 20);
		pnlFormulario.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(130, 60, 250, 25);
		
		txtNombre.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				// Si el carácter presionado es un número (del 0 al 9), se ignora
				if (Character.isDigit(c)) {
					e.consume(); 
				}
			}
		});
		pnlFormulario.add(txtNombre);
		
		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblDireccion.setBounds(20, 110, 100, 20);
		pnlFormulario.add(lblDireccion);
		
		txtDireccion = new JTextField();
		txtDireccion.setBounds(130, 110, 250, 25);
		pnlFormulario.add(txtDireccion);
		
		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTelefono.setBounds(20, 160, 100, 20);
		pnlFormulario.add(lblTelefono);
		
		try {
			MaskFormatter mascaraTel = new MaskFormatter("##########");
			mascaraTel.setPlaceholderCharacter('_');
			txtTelefono = new JFormattedTextField(mascaraTel);
		} catch (Exception e) {
			txtTelefono = new JFormattedTextField();
		}
		txtTelefono.setBounds(130, 160, 250, 25);
		pnlFormulario.add(txtTelefono);
		
		JLabel lblCorreo = new JLabel("Correo:");
		lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblCorreo.setBounds(20, 210, 100, 20);
		pnlFormulario.add(lblCorreo);
		
		txtCorreo = new JTextField();
		txtCorreo.setBounds(130, 210, 250, 25);
		pnlFormulario.add(txtCorreo);
		
		JLabel lblFecha = new JLabel("Fecha Registro:");
		lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblFecha.setBounds(20, 260, 110, 20);
		pnlFormulario.add(lblFecha);
		
		try {
		    MaskFormatter mascaraFecha = new MaskFormatter("##/##/####");
		    mascaraFecha.setPlaceholderCharacter('_');
		    txtFechaRegistro = new JFormattedTextField(mascaraFecha);
		} catch (Exception e) {
		    txtFechaRegistro = new JFormattedTextField();
		}
		txtFechaRegistro.setBounds(130, 260, 120, 25);
		pnlFormulario.add(txtFechaRegistro);
		

		// Sub-panel derecho: Búsqueda y Tabla
		JPanel pnlDerecho = new JPanel(new BorderLayout(0, 10));
		pnlDerecho.setBackground(Color.WHITE);
		pnlCentro.add(pnlDerecho);
		
		// Búsqueda
		JPanel pnlBusqueda = new JPanel(new BorderLayout(10, 0));
		pnlBusqueda.setBackground(Color.WHITE);
		
		JLabel lblBuscar = new JLabel("Buscar Cliente:");
		lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		pnlBusqueda.add(lblBuscar, BorderLayout.WEST);
		
		txtBuscar = new JTextField();
		pnlBusqueda.add(txtBuscar, BorderLayout.CENTER);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setForeground(Color.WHITE);
		btnBuscar.setBackground(new Color(0, 128, 192));
		btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtBuscar.getText().trim().isEmpty()) {
					mostrarDatos();
					return;
				}
				try {
					Conexion conexion = new Conexion();
					con = conexion.conectar();
					
					String sql = "SELECT * FROM clientes WHERE nombre_completo LIKE ?";
					ps = con.prepareStatement(sql);
					ps.setString(1, "%" + txtBuscar.getText().trim() + "%");
					rs = ps.executeQuery();

					modelo.setRowCount(0); 

					while (rs.next()) {
						Object fila[] = new Object[6];
						fila[0] = rs.getInt("id_cliente");
						fila[1] = rs.getString("nombre_completo");
						fila[2] = rs.getString("direccion");
						fila[3] = rs.getString("telefono");
						fila[4] = rs.getString("correo");
						fila[5] = rs.getString("fecha_registro");
						modelo.addRow(fila);
					}
					
					actualizarContador(); 
					con.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnBuscar.doClick();
				}
			}
		});
		
		pnlBusqueda.add(btnBuscar, BorderLayout.EAST);
		pnlDerecho.add(pnlBusqueda, BorderLayout.NORTH);

		// Tabla
		JScrollPane scrollPane = new JScrollPane();
		pnlDerecho.add(scrollPane, BorderLayout.CENTER);
		
		modelo.addColumn("ID");
		modelo.addColumn("Nombre");
		modelo.addColumn("Dirección");
		modelo.addColumn("Teléfono");
		modelo.addColumn("Correo");
		modelo.addColumn("Fecha Registro");
		
		table = new JTable(modelo);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(0, 91, 159));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setGridColor(new Color(102, 167, 215));
		table.setSelectionForeground(Color.WHITE);
		table.setSelectionBackground(new Color(0, 64, 128));
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// Ocultar ID
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setPreferredWidth(0);
		
		// Nombre
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		
		// Dirección (Más ancha porque suele ser la más larga)
		table.getColumnModel().getColumn(2).setPreferredWidth(230);
		
		// Teléfono
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMaxWidth(120);
		
		// Correo
		table.getColumnModel().getColumn(4).setPreferredWidth(160);
		
		// Fecha Registro
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setMaxWidth(130);
		
		scrollPane.setViewportView(table);
		
		// Evento MouseListener
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = table.getSelectedRow();
				if(fila >= 0) {
					idCliente = Integer.parseInt(table.getValueAt(fila, 0).toString());
				}
			}
		});
		
		// CONTADOR DE CLIENTES
		lblContador = new JLabel("Total de clientes: 0");
		lblContador.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblContador.setForeground(new Color(0, 64, 128));
		pnlDerecho.add(lblContador, BorderLayout.SOUTH);

		javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
		this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				btnSalir.doClick(); 
			}
		});

		mostrarDatos();
	}

	// Función para checar repetidos en la BD
	private boolean existeCliente(String nombreCliente, int idExcluir) {
		boolean existe = false;
		try {
			Conexion conEx = new Conexion();
			Connection conexion = conEx.conectar();
			
			String sql = "SELECT id_cliente FROM clientes WHERE nombre_completo = ? AND id_cliente != ?";
			PreparedStatement pst = conexion.prepareStatement(sql);
			pst.setString(1, nombreCliente.trim());
			pst.setInt(2, idExcluir);
			
			ResultSet rst = pst.executeQuery();
			if(rst.next()) {
				existe = true; 
			}
			conexion.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return existe;
	}

	public void mostrarDatos() {
		modelo.setRowCount(0);

		try {
			Conexion conexion = new Conexion();
			con = conexion.conectar();
			
			String sql = "SELECT * FROM clientes";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Object fila[] = new Object[6];
				fila[0] = rs.getInt("id_cliente");
				fila[1] = rs.getString("nombre_completo");
				fila[2] = rs.getString("direccion");
				fila[3] = rs.getString("telefono");
				fila[4] = rs.getString("correo");
				fila[5] = rs.getString("fecha_registro");
				modelo.addRow(fila);
			}
			con.close();
			
			actualizarContador(); 
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void actualizarContador() {
		int totalRegistros = modelo.getRowCount();
		lblContador.setText("Total de clientes: " + totalRegistros);
	}

	public void limpiar() {
		txtNombre.setText("");
		txtDireccion.setText("");
		txtTelefono.setValue(null); 
		txtCorreo.setText("");
		txtFechaRegistro.setValue(null); 
		
		idCliente = 0;
		nombreOriginal = "";
		direccionOriginal = "";
		telefonoOriginal = "";
		correoOriginal = "";
		fechaOriginal = "";
		
		if(btnModificar != null) {
			btnModificar.setText("Modificar");
			btnAgregar.setEnabled(true);
			btnEliminar.setEnabled(true);
			btnEliminarTodo.setEnabled(true);
			table.setEnabled(true);
			table.clearSelection();
		}
	}
}