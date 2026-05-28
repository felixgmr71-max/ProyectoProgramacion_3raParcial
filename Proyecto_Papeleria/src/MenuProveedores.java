import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class MenuProveedores extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabla;

	private JTextField txtEmpresa;
	private JTextField txtContacto;
	private JFormattedTextField txtTelefono; 
	
	private JButton btnAgregar;
	private JButton btnModificar; 
	private JButton btnEliminar;

	DefaultTableModel modelo = new DefaultTableModel();

	Connection conexion = null;
	PreparedStatement ps = null; 
	ResultSet rs = null; 

	int idProveedor = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuProveedores frame = new MenuProveedores();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuProveedores() {
		setTitle("Proveedores");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 500); 
		setLocationRelativeTo(null); 
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0)); 

		// Panel Norte
		JPanel pnlNorte = new JPanel();
		pnlNorte.setBackground(new Color(0, 64, 128)); 
		contentPane.add(pnlNorte, BorderLayout.NORTH);
		
		JLabel lblTitulo = new JLabel("Directorio de Proveedores");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0)); 
		pnlNorte.add(lblTitulo);

		// Panel Sur (Botones)
		JPanel pnlSur = new JPanel();
		pnlSur.setBackground(new Color(0, 64, 128));
		pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15)); 
		contentPane.add(pnlSur, BorderLayout.SOUTH);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setForeground(new Color(0, 128, 192));
		btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// --- VALIDACIÓN DE CAMPOS VACÍOS ---
				if(txtEmpresa.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "El nombre de la empresa es obligatorio.", "DATO REQUERIDO", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(txtContacto.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "El nombre del contacto es obligatorio.", "DATO REQUERIDO", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// Limpiamos los guiones y espacios de la máscara para contar cuántos números reales escribió
				String telefonoLimpio = txtTelefono.getText().replace("-", "").replace("_", "").trim();
				if(telefonoLimpio.length() < 10) {
					JOptionPane.showMessageDialog(null, "El teléfono debe contener los 10 dígitos completos.", "DATO REQUERIDO", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// --- PROTECCIÓN: Evitamos el error de UNIQUE constraint ---
				if(existeEmpresa(txtEmpresa.getText(), 0)) {
					JOptionPane.showMessageDialog(null, "La empresa '" + txtEmpresa.getText() + "' ya está registrada.\nPor favor, ingrese un nombre diferente.", "EMPRESA DUPLICADA", JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					Conexion con = new Conexion();
					conexion = con.conectar();

					String sql = "INSERT INTO proveedores(nombre_empresa, contacto, telefono) VALUES(?,?,?)";
					ps = conexion.prepareStatement(sql);

					ps.setString(1, txtEmpresa.getText());
					ps.setString(2, txtContacto.getText());
					ps.setString(3, txtTelefono.getText());

					ps.executeUpdate(); 

					JOptionPane.showMessageDialog(null, "¡Proveedor agregado con éxito!", "PROCESO EXITOSO", JOptionPane.INFORMATION_MESSAGE);
					limpiar(); 
					mostrarDatos(); 
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Ocurrió un error: " + ex.getMessage(), "ERROR DE BASE DE DATOS", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnlSur.add(btnAgregar);

		btnModificar = new JButton("Modificar");
		btnModificar.setForeground(new Color(0, 128, 192));
		btnModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(idProveedor == 0) {
					JOptionPane.showMessageDialog(null, "Seleccione un proveedor de la tabla para modificarlo.", "ACCIÓN REQUERIDA", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// PASO 1: Subir datos y BLOQUEAR botones
				if(btnModificar.getText().equals("Modificar")) {
					int fila = tabla.getSelectedRow();
					txtEmpresa.setText(tabla.getValueAt(fila, 1).toString());
					txtContacto.setText(tabla.getValueAt(fila, 2).toString());
					txtTelefono.setText(tabla.getValueAt(fila, 3).toString());
					
					btnModificar.setText("Guardar Cambios");
					
					btnAgregar.setEnabled(false);
					btnEliminar.setEnabled(false);
					tabla.setEnabled(false); 
				} 
				// PASO 2: Guardar
				else {
					
					// --- VALIDACIÓN DE CAMPOS VACÍOS (Misma que al agregar) ---
					if(txtEmpresa.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "El nombre de la empresa es obligatorio.", "DATO REQUERIDO", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(txtContacto.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "El nombre del contacto es obligatorio.", "DATO REQUERIDO", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					String telefonoLimpio = txtTelefono.getText().replace("-", "").replace("_", "").trim();
					if(telefonoLimpio.length() < 10) {
						JOptionPane.showMessageDialog(null, "El teléfono debe contener los 10 dígitos completos.", "DATO REQUERIDO", JOptionPane.WARNING_MESSAGE);
						return;
					}

					// --- PROTECCIÓN: Evitamos el error de UNIQUE al modificar ---
					if(existeEmpresa(txtEmpresa.getText(), idProveedor)) {
						JOptionPane.showMessageDialog(null, "El nombre '" + txtEmpresa.getText() + "' ya pertenece a otro registro.\nPor favor, ingrese un nombre diferente.", "EMPRESA DUPLICADA", JOptionPane.WARNING_MESSAGE);
						return;
					}

					try {
						Conexion con = new Conexion();
						conexion = con.conectar();
	
						String sql = "UPDATE proveedores SET nombre_empresa=?, contacto=?, telefono=? WHERE id_proveedor=?";
						ps = conexion.prepareStatement(sql);
	
						ps.setString(1, txtEmpresa.getText());
						ps.setString(2, txtContacto.getText());
						ps.setString(3, txtTelefono.getText());
						ps.setInt(4, idProveedor); 
	
						ps.executeUpdate();
	
						JOptionPane.showMessageDialog(null, "¡Proveedor modificado exitosamente!", "ACTUALIZACIÓN EXITOSA", JOptionPane.INFORMATION_MESSAGE);
						limpiar();
						mostrarDatos();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Ocurrió un error: " + ex.getMessage(), "ERROR DE BASE DE DATOS", JOptionPane.ERROR_MESSAGE);
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
				
				if(idProveedor == 0) {
					JOptionPane.showMessageDialog(null, "Seleccione un proveedor de la tabla para eliminarlo.", "ACCIÓN REQUERIDA", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				int respuesta = JOptionPane.showConfirmDialog(
						null,
						"¿Está seguro que desea eliminar este proveedor por completo?\nEsta acción no se puede deshacer.",
						"CONFIRMAR ELIMINACIÓN",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (respuesta == JOptionPane.YES_OPTION) {
					try {
						Conexion con = new Conexion();
						conexion = con.conectar();

						String sql = "DELETE FROM proveedores WHERE id_proveedor=?";
						ps = conexion.prepareStatement(sql);
						ps.setInt(1, idProveedor);
						ps.executeUpdate();

						JOptionPane.showMessageDialog(null, "El proveedor ha sido eliminado correctamente.", "ELIMINACIÓN EXITOSA", JOptionPane.INFORMATION_MESSAGE);
						limpiar();
						mostrarDatos();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Ocurrió un error: " + ex.getMessage(), "ERROR DE BASE DE DATOS", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlSur.add(btnEliminar);

		JButton BtnSalir = new JButton("Volver al menú principal");
		BtnSalir.setForeground(new Color(0, 128, 192));
		BtnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		BtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				dispose(); 
			}
		});
		pnlSur.add(BtnSalir);

		// Panel Centro
		JPanel pnlCentro = new JPanel();
		pnlCentro.setBackground(Color.WHITE);
		pnlCentro.setLayout(new GridLayout(1, 2, 20, 0)); 
		pnlCentro.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.add(pnlCentro, BorderLayout.CENTER);

		JPanel pnlFormulario = new JPanel();
		pnlFormulario.setBackground(Color.WHITE);
		pnlFormulario.setLayout(null); 
		pnlCentro.add(pnlFormulario);

		JLabel lblSubtitulo = new JLabel("Datos del Proveedor");
		lblSubtitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSubtitulo.setForeground(new Color(0, 64, 128));
		lblSubtitulo.setBounds(20, 10, 250, 20);
		pnlFormulario.add(lblSubtitulo);

		JLabel lblEmpresa = new JLabel("Empresa:");
		lblEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblEmpresa.setBounds(20, 60, 80, 20);
		pnlFormulario.add(lblEmpresa);

		txtEmpresa = new JTextField();
		txtEmpresa.setBounds(110, 60, 230, 25);
		pnlFormulario.add(txtEmpresa);

		JLabel lblContacto = new JLabel("Contacto:");
		lblContacto.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblContacto.setBounds(20, 110, 80, 20);
		pnlFormulario.add(lblContacto);

		txtContacto = new JTextField();
		txtContacto.setBounds(110, 110, 230, 25);
		pnlFormulario.add(txtContacto);

		txtContacto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isLetter(c) && c != ' ' && c != KeyEvent.VK_BACK_SPACE) {
					e.consume();
				}
			}
		});

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTelefono.setBounds(20, 160, 80, 20);
		pnlFormulario.add(lblTelefono);

		try {
			MaskFormatter mascara = new MaskFormatter("###-###-####");
			mascara.setPlaceholderCharacter('_'); 
			txtTelefono = new JFormattedTextField(mascara);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			txtTelefono = new JFormattedTextField(); 
		}
		txtTelefono.setBounds(110, 160, 230, 25);
		pnlFormulario.add(txtTelefono);

		JPanel pnlTabla = new JPanel(new BorderLayout());
		pnlTabla.setBackground(Color.WHITE);
		pnlCentro.add(pnlTabla);

		JScrollPane scrollPane = new JScrollPane();
		pnlTabla.add(scrollPane, BorderLayout.CENTER);

		modelo.addColumn("ID");
		modelo.addColumn("Empresa");
		modelo.addColumn("Contacto");
		modelo.addColumn("Teléfono");

		tabla = new JTable(modelo);
		
		tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		tabla.getTableHeader().setBackground(new Color(0, 91, 159)); 
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.setGridColor(new Color(102, 167, 215));
		tabla.setSelectionForeground(Color.WHITE);
		tabla.setSelectionBackground(new Color(0, 64, 128));
		tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tabla.setRowHeight(25);
		tabla.setDefaultEditor(Object.class, null); 
		tabla.getTableHeader().setReorderingAllowed(false);
		
		scrollPane.setViewportView(tabla);

		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tabla.getSelectedRow();
				if(fila >= 0) {
					idProveedor = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
				}
			}
		});

		javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
		this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				BtnSalir.doClick(); 
			}
		});

		mostrarDatos(); 
	}
	
	private boolean existeEmpresa(String nombreEmpresa, int idExcluir) {
		boolean existe = false;
		try {
			Conexion con = new Conexion();
			conexion = con.conectar();
			
			String sql = "SELECT id_proveedor FROM proveedores WHERE nombre_empresa = ? AND id_proveedor != ?";
			ps = conexion.prepareStatement(sql);
			ps.setString(1, nombreEmpresa.trim());
			ps.setInt(2, idExcluir);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				existe = true; 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return existe;
	}

	public void mostrarDatos() {
		modelo.setRowCount(0);

		try {
			Conexion con = new Conexion();
			conexion = con.conectar();

			String sql = "SELECT * FROM proveedores"; 
			ps = conexion.prepareStatement(sql);
			rs = ps.executeQuery(); 

			while (rs.next()) {
				Object fila[] = new Object[4];
				fila[0] = rs.getInt("id_proveedor");
				fila[1] = rs.getString("nombre_empresa");
				fila[2] = rs.getString("contacto");
				fila[3] = rs.getString("telefono");

				modelo.addRow(fila);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al mostrar datos: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
	    tabla.getColumnModel().getColumn(0).setMinWidth(0);
	    tabla.getColumnModel().getColumn(0).setMaxWidth(0);
	    tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
	}

	public void limpiar() {
		txtEmpresa.setText("");
		txtContacto.setText("");
		txtTelefono.setValue(null); 

		idProveedor = 0;
		
		if(btnModificar != null && btnAgregar != null && btnEliminar != null && tabla != null) {
			btnModificar.setText("Modificar");
			btnAgregar.setEnabled(true);
			btnEliminar.setEnabled(true);
			tabla.setEnabled(true);
			tabla.clearSelection(); 
		}
	}
}