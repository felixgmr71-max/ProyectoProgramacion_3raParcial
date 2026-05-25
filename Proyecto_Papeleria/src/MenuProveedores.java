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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class MenuProveedores extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabla;

	private JTextField txtEmpresa;
	private JTextField txtContacto;
	private JTextField txtTelefono;

	DefaultTableModel modelo = new DefaultTableModel();

	Connection conexion = null;
	PreparedStatement ps = null; // variable que prepara la consulta SQL de forma segura
	ResultSet rs = null; // Aquí se guarda todo lo que nos responde la base de datos (los resultados)

	// Variable global para saber a qué proveedor le dimos clic. Inicia en 0.
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
		setLocationRelativeTo(null); // Centra la ventana en la pantalla
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0)); // Usamos BorderLayout para dividir en Norte, Sur y Centro

		//comenzamos por el panel norte o el de arriba
		JPanel pnlNorte = new JPanel();
		pnlNorte.setBackground(new Color(0, 64, 128)); 
		contentPane.add(pnlNorte, BorderLayout.NORTH);
		
		JLabel lblTitulo = new JLabel("Directorio de Proveedores");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0)); // Márgenes para que no se vea amontonado
		pnlNorte.add(lblTitulo);

		//continuamos con el sur que es donde se ubican los botones
		JPanel pnlSur = new JPanel();
		pnlSur.setBackground(new Color(0, 64, 128));
		pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15)); // Centrados y con espacio entre ellos
		contentPane.add(pnlSur, BorderLayout.SOUTH);
		
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.setForeground(new Color(0, 128, 192));
		btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Pone la manita al pasar el mouse
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Conexion con = new Conexion();
					conexion = con.conectar();

					/* * ¿Qué significan los signo de interrogación(?)?
					 * llamadas "Consultas Parametrizadas". 
					 * previenen inyección sql, haciendolo más profesional jajajaja
					 */
					String sql = "INSERT INTO proveedores(nombre_empresa, contacto, telefono) VALUES(?,?,?)";
					ps = conexion.prepareStatement(sql);

					// Aquí rellenamos los huecos (?), el 1, 2 y 3 corresponden al orden en el que aparecen arriba
					ps.setString(1, txtEmpresa.getText());
					ps.setString(2, txtContacto.getText());
					ps.setString(3, txtTelefono.getText());

					ps.executeUpdate(); // Ejecutamos la acción en la base de datos

					JOptionPane.showMessageDialog(null, "¡Proveedor agregado con éxito!");
					limpiar(); // Dejamos los cuadritos en blanco
					mostrarDatos(); // Refrescamos la tabla para ver los datos actualizados
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});
		pnlSur.add(btnAgregar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.setForeground(new Color(0, 128, 192));
		btnModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Conexion con = new Conexion();
					conexion = con.conectar();

					// Igual aquí, actualizamos todo basándonos en el ID del proveedor 
					String sql = "UPDATE proveedores SET nombre_empresa=?, contacto=?, telefono=? WHERE id_proveedor=?";
					ps = conexion.prepareStatement(sql);

					ps.setString(1, txtEmpresa.getText());
					ps.setString(2, txtContacto.getText());
					ps.setString(3, txtTelefono.getText());
					ps.setInt(4, idProveedor); // Le pasamos el ID que guardamos al hacer clic en la tabla

					ps.executeUpdate();

					JOptionPane.showMessageDialog(null, "¡Proveedor modificado exitosamente!");
					limpiar();
					mostrarDatos();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});
		pnlSur.add(btnModificar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setForeground(new Color(0, 128, 192));
		btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Conexion con = new Conexion();
					conexion = con.conectar();

					// Borramos todo donde el ID coincida
					String sql = "DELETE FROM proveedores WHERE id_proveedor=?";
					ps = conexion.prepareStatement(sql);
					ps.setInt(1, idProveedor);
					ps.executeUpdate();

					JOptionPane.showMessageDialog(null, "Proveedor mandado a volar (eliminado)");
					limpiar();
					mostrarDatos();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
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
				dispose(); // cierra la ventana
			}
		});
		pnlSur.add(BtnSalir);

		//panel principal
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

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTelefono.setBounds(20, 160, 80, 20);
		pnlFormulario.add(lblTelefono);

		txtTelefono = new JTextField();
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
		
		// apartado estético
		tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		tabla.getTableHeader().setBackground(new Color(0, 91, 159)); 
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.setGridColor(new Color(102, 167, 215));
		tabla.setSelectionForeground(Color.WHITE);
		tabla.setSelectionBackground(new Color(0, 64, 128));
		tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tabla.setRowHeight(25);
		tabla.setDefaultEditor(Object.class, null); // Esto evita que editen la tabla dándole doble clic
		tabla.getTableHeader().setReorderingAllowed(false);
		
		scrollPane.setViewportView(tabla);

		// --- EVENTO: CUANDO LE DAN CLIC A LA TABLA ---
		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Agarramos el número de la fila que el usuario seleccionó (empieza a contar desde 0)
				int fila = tabla.getSelectedRow();

				// Rescatamos el ID oculto de la columna 0 y lo guardamos en la variable global
				idProveedor = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

				// Pegamos los datos de la tabla en las cajitas de texto
				txtEmpresa.setText(tabla.getValueAt(fila, 1).toString());
				txtContacto.setText(tabla.getValueAt(fila, 2).toString());
				txtTelefono.setText(tabla.getValueAt(fila, 3).toString());
			}
		});

		// Atajo: Que el botón ESC del teclado haga lo mismo que el botón Salir
		javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
		this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				BtnSalir.doClick(); 
			}
		});

		mostrarDatos(); // Al abrir la ventana, mandamos llamar a los proveedores de la BD
	}

	
	public void mostrarDatos() {
		// Borramos la tabla visualmente antes de cargar para que no se duplique la info
		modelo.setRowCount(0);

		try {
			Conexion con = new Conexion();
			conexion = con.conectar();

			String sql = "SELECT * FROM proveedores"; 
			ps = conexion.prepareStatement(sql);
			rs = ps.executeQuery(); // Ejecutamos la consulta y la guardamos en 'rs'

			// Mientras la base de datos nos siga escupiendo resultados, los metemos a la tabla
			while (rs.next()) {
				Object fila[] = new Object[4];
				fila[0] = rs.getInt("id_proveedor");
				fila[1] = rs.getString("nombre_empresa");
				fila[2] = rs.getString("contacto");
				fila[3] = rs.getString("telefono");

				modelo.addRow(fila);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al mostrar datos: " + ex.getMessage());
		}
		
	    tabla.getColumnModel().getColumn(0).setMinWidth(0);
	    tabla.getColumnModel().getColumn(0).setMaxWidth(0);
	    tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
	}

	public void limpiar() {
		txtEmpresa.setText("");
		txtContacto.setText("");
		txtTelefono.setText("");

		// Reseteamos el ID a 0 para no arrastrar basura a la siguiente operación
		idProveedor = 0;
	}
}