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
import java.sql.Statement;
import java.sql.ResultSet;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;

public class MenuClientes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField TxtNombre;
	private JTextField TxtDireccion;
	private JFormattedTextField TxtTelefono; //Máscara para telefono
	private JTextField TxtCorreo;
	private JFormattedTextField TxtFechaRegistro; //Máscara para fecha de registro
	private JTextField TxtBuscar;

	//Modelo de tabla, conexión y variables de BD
	DefaultTableModel modelo = new DefaultTableModel();

	Connection con = null;
	Statement sentencia = null;
	ResultSet Rs = null;

	int idCliente = 0; //Guarda el ID del cliente seleccionado en la tabla
	
	//Guardamos los datos originales al seleccionar una fila, para detectar si hubo cambios en Modificar
	String nombreOriginal = "";
	String direccionOriginal = "";
	String telefonoOriginal = "";
	String correoOriginal = "";
	String fechaOriginal = "";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuClientes frame = new MenuClientes();
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
	public MenuClientes() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 946, 433);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblClientes = new JLabel("Clientes");
		lblClientes.setBounds(29, 10, 130, 38);
		lblClientes.setFont(new Font("Century Gothic", Font.BOLD, 30));
		contentPane.add(lblClientes);
		
		JButton BtnSalir = new JButton("Volver al menú principal");
		BtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Volvemos al menú principal
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnSalir.setBounds(733, 366, 170, 20);
		contentPane.add(BtnSalir);
		
		JLabel lblNewLabel = new JLabel("Dirección:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setBounds(29, 127, 81, 20);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(313, 67, 590, 214);
		contentPane.add(scrollPane);
		
		table = new JTable((TableModel) null);
		scrollPane.setViewportView(table);
		
		TxtNombre = new JTextField();
		TxtNombre.setBounds(94, 88, 170, 18);
		contentPane.add(TxtNombre);
		TxtNombre.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setBounds(29, 86, 81, 20);
		contentPane.add(lblNewLabel_1);
		
		TxtDireccion = new JTextField();
		TxtDireccion.setColumns(10);
		TxtDireccion.setBounds(106, 129, 170, 18);
		contentPane.add(TxtDireccion);
		
		JLabel lblTelfono = new JLabel("Teléfono:");
		lblTelfono.setFont(new Font("Arial", Font.BOLD, 14));
		lblTelfono.setBounds(29, 168, 81, 20);
		contentPane.add(lblTelfono);
		
		//Máscara de teléfono (solo 10 digitos)
		try {
			MaskFormatter mascara = new MaskFormatter("##########"); //10 dígitos
			TxtTelefono = new JFormattedTextField(mascara);
		} catch (Exception e) {
			TxtTelefono = new JFormattedTextField(); //Si falla la máscara
		}
		
		TxtTelefono.setColumns(10);
		TxtTelefono.setBounds(106, 170, 170, 18);
		contentPane.add(TxtTelefono);
		
		JLabel lblCorreo = new JLabel("Correo:");
		lblCorreo.setFont(new Font("Arial", Font.BOLD, 14));
		lblCorreo.setBounds(29, 210, 81, 20);
		contentPane.add(lblCorreo);
		
		TxtCorreo = new JTextField();
		TxtCorreo.setColumns(10);
		TxtCorreo.setBounds(94, 212, 170, 18);
		contentPane.add(TxtCorreo);
		
		JLabel lblFechaDeRegistro = new JLabel("Fecha de Registro:");
		lblFechaDeRegistro.setFont(new Font("Arial", Font.BOLD, 14));
		lblFechaDeRegistro.setBounds(29, 255, 151, 20);
		contentPane.add(lblFechaDeRegistro);
		
		//Máscara de fecha (formato DD/MM/AAAA)
		try {
		    MaskFormatter mascaraFecha = new MaskFormatter("##/##/####");
		    TxtFechaRegistro = new JFormattedTextField(mascaraFecha);
		} catch (Exception e) {
		    TxtFechaRegistro = new JFormattedTextField();
		}
		
		TxtFechaRegistro.setColumns(10);
		TxtFechaRegistro.setBounds(174, 257, 90, 18);
		contentPane.add(TxtFechaRegistro);
		
		JLabel lblNewLabel_1_1 = new JLabel("Buscar:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(183, 26, 81, 20);
		contentPane.add(lblNewLabel_1_1);
		
		TxtBuscar = new JTextField();
		TxtBuscar.setBounds(245, 28, 558, 18);
		contentPane.add(TxtBuscar);
		TxtBuscar.setColumns(10);
		
		JButton BtnBuscar = new JButton("Buscar");
		BtnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Si el campo está vacío mostramos todos
				if (TxtBuscar.getText().isEmpty()) {
					mostrarDatos();
					return;
				}

				try {

					Conexion conexion = new Conexion();
					con = conexion.conectar();

					//Statement ejecuta el SQL directo como String
					sentencia = con.createStatement();

					//Concatenamos el texto buscado dentro del LIKE
					String query = "SELECT * FROM clientes WHERE nombre_completo LIKE '%" + TxtBuscar.getText() + "%'";

					Rs = sentencia.executeQuery(query);

					modelo.setRowCount(0); //Limpia la tabla antes de llenarla

					while (Rs.next()) {
						Object fila[] = new Object[6];
						
						fila[0] = Rs.getInt("id_cliente");
						fila[1] = Rs.getString("nombre_completo");
						fila[2] = Rs.getString("direccion");
						fila[3] = Rs.getString("telefono");
						fila[4] = Rs.getString("correo");
						fila[5] = Rs.getString("fecha_registro");
						modelo.addRow(fila);
					}

					//Cerramos conexión
					con.close();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});
		BtnBuscar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnBuscar.setBounds(813, 26, 90, 20);
		contentPane.add(BtnBuscar);
		
		JButton BtnAgregar = new JButton("Agregar");
		BtnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String telefono = TxtTelefono.getText().trim();
				String fecha = TxtFechaRegistro.getText().trim();

				//Validamos
				//Todos los campos obligatorios deben estar llenos
				if (TxtNombre.getText().trim().isEmpty() ||
					TxtDireccion.getText().trim().isEmpty() ||
					telefono.length() < 10 || //Valida que sean 10 digitos
					TxtCorreo.getText().trim().isEmpty() ||
					fecha.length() < 10) {

					JOptionPane.showMessageDialog(null, "Por favor rellena todos los campos antes de agregar");
					return; //Detenemos la ejecución si falta algo
				}

				try {
					Conexion conexion = new Conexion();
					con = conexion.conectar();
					sentencia = con.createStatement();

					String query = "INSERT INTO clientes(nombre_completo, direccion, telefono, correo, fecha_registro) "
							+ "VALUES('"
							+ TxtNombre.getText().trim() + "','"
							+ TxtDireccion.getText().trim() + "','"
							+ telefono + "','"
							+ TxtCorreo.getText().trim() + "','"
							+ TxtFechaRegistro.getText().trim() + "')";

					sentencia.executeUpdate(query);
					JOptionPane.showMessageDialog(null, "Cliente agregado");

					con.close();
					limpiar();
					mostrarDatos();

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Error: " + e1.getMessage());
				}
			}
		});
		BtnAgregar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnAgregar.setBounds(313, 308, 90, 31);
		contentPane.add(BtnAgregar);
		
		JButton BtnModificar = new JButton("Modificar");
		BtnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Validamos
				//Debe haberse seleccionado una fila primero
				if (idCliente == 0) {
					JOptionPane.showMessageDialog(null, "Primero selecciona una fila a modificar");
					return;
				}

				String nuevoNombre = TxtNombre.getText().trim();
				String nuevaDireccion = TxtDireccion.getText().trim();
				String nuevoTelefono = TxtTelefono.getText().trim();
				String nuevoCorreo = TxtCorreo.getText().trim();
				String nuevaFecha = TxtFechaRegistro.getText().trim();

				//Validamos
				//Debe haber al menos un campo diferente al original
				if (nuevoNombre.equals(nombreOriginal) &&
					nuevaDireccion.equals(direccionOriginal) &&
					nuevoTelefono.equals(telefonoOriginal) &&
					nuevoCorreo.equals(correoOriginal) &&
					nuevaFecha.equals(fechaOriginal)) {

					JOptionPane.showMessageDialog(null, "No se detectaron cambios: Modifica al menos un campo");
					return;
				}

				try {
					Conexion conexion = new Conexion();
					con = conexion.conectar();
					sentencia = con.createStatement();

					String query = "UPDATE clientes SET "
							+ "nombre_completo='" + nuevoNombre + "',"
							+ "direccion='" + nuevaDireccion + "',"
							+ "telefono='" + nuevoTelefono + "',"
							+ "correo='" + nuevoCorreo + "',"
							+ "fecha_registro='" + nuevaFecha + "' "
							+ "WHERE id_cliente=" + idCliente;

					sentencia.executeUpdate(query);
					JOptionPane.showMessageDialog(null, "Cliente modificado");

					con.close();
					limpiar();
					mostrarDatos();

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Error: " + e2.getMessage());
				}
			}
		});
		BtnModificar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnModificar.setBounds(461, 308, 102, 31);
		contentPane.add(BtnModificar);
		
		JButton BtnEliminar = new JButton("Eliminar");
		BtnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Validamos
				//Debe haberse seleccionado una fila primero
				if (idCliente == 0) {
					JOptionPane.showMessageDialog(null, "Primero selecciona una fila a eliminar");
					return;
				}

				int respuesta = JOptionPane.showConfirmDialog(null,
						"¿Desea eliminar este cliente?", "Confirmar",
						JOptionPane.YES_NO_OPTION);

				if (respuesta == JOptionPane.YES_OPTION) {
					try {
						Conexion conexion = new Conexion();
						con = conexion.conectar();
						sentencia = con.createStatement();

						String query = "DELETE FROM clientes WHERE id_cliente=" + idCliente;
						sentencia.executeUpdate(query);

						JOptionPane.showMessageDialog(null, "Cliente eliminado");

						con.close();
						limpiar();
						mostrarDatos();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Cliente NO eliminado");
				}
			}
		});
		BtnEliminar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnEliminar.setBounds(629, 308, 90, 31);
		contentPane.add(BtnEliminar);
		
		JButton BtnEliminarTodo = new JButton("Eliminar todo");
		BtnEliminarTodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Primera confirmación
				int respuesta1 = JOptionPane.showConfirmDialog(null,
						"¿Seguro que desea eliminar TODOS los clientes?", "Confirmar",
						JOptionPane.YES_NO_OPTION);

				if (respuesta1 == JOptionPane.YES_OPTION) {

					//Segunda confirmación por razones de seguridad
					int respuesta2 = JOptionPane.showConfirmDialog(null,
							"Esta acción no se puede deshacer ¿Continuar?", "Última confirmación",
							JOptionPane.YES_NO_OPTION);

					if (respuesta2 == JOptionPane.YES_OPTION) {

						try {

							Conexion conexion = new Conexion();
							con = conexion.conectar();

							sentencia = con.createStatement();

							//Sin WHERE porque elimina todos los registros de la tabla
							String query = "DELETE FROM clientes";

							sentencia.executeUpdate(query);

							JOptionPane.showMessageDialog(null, "Todos los clientes han sido eliminados");

							//Cerramos conexión
							con.close();

							limpiar();
							mostrarDatos();

						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "Clientes NO eliminados");
					}
				}else {
					JOptionPane.showMessageDialog(null, "Clientes NO eliminados");
				}
			}
		});
		BtnEliminarTodo.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnEliminarTodo.setBounds(779, 308, 124, 31);
		contentPane.add(BtnEliminarTodo);
		
		//Columnas visibles de la tabla
		modelo.addColumn("ID"); //Esta se va a ocultar
		modelo.addColumn("Nombre");
		modelo.addColumn("Dirección");
		modelo.addColumn("Teléfono");
		modelo.addColumn("Correo");
		modelo.addColumn("Fecha Registro");
		
		//Se asigna el modelo al JTable
		table = new JTable(modelo);
		scrollPane.setViewportView(table);
		
		JButton BtnLimpiar = new JButton("Limpiar Cuadros");
		BtnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiar();
			}
		});
		BtnLimpiar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnLimpiar.setBounds(60, 297, 178, 20);
		contentPane.add(BtnLimpiar);

		//Ocultamos la columna ID — ancho 0 y no redimensionable
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setWidth(0);
		table.getColumnModel().getColumn(0).setResizable(false);
	
		//MouseListener (al hacer clic en fila carga los datos en los campos)
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int fila = table.getSelectedRow();

				//Guardamos el ID para usarlo en Modificar y Eliminar
				idCliente = Integer.parseInt(table.getValueAt(fila, 0).toString());

				TxtNombre.setText(table.getValueAt(fila, 1).toString());
				TxtDireccion.setText(table.getValueAt(fila, 2).toString());
				TxtTelefono.setText(table.getValueAt(fila, 3).toString());
				TxtCorreo.setText(table.getValueAt(fila, 4).toString());
				TxtFechaRegistro.setText(table.getValueAt(fila, 5).toString());
				
				//Guardamos los valores originales para comparar en Modificar
				nombreOriginal = TxtNombre.getText().trim(); //.trim() Elimina espacios de al principio y final del texto
				direccionOriginal = TxtDireccion.getText().trim();
				telefonoOriginal = TxtTelefono.getText().trim();
				correoOriginal = TxtCorreo.getText().trim();
				fechaOriginal = TxtFechaRegistro.getText().trim();
			}
		});
				
		
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

		//Cargamos los datos al abrir la ventana
		mostrarDatos();
	}

	//Método mostrarDatos
	public void mostrarDatos() {

		modelo.setRowCount(0); //Limpiar filas anteriores

		try {

			Conexion conexion = new Conexion();
			con = conexion.conectar();

			sentencia = con.createStatement();

			String query = "SELECT * FROM clientes";

			Rs = sentencia.executeQuery(query);

			while (Rs.next()) {

				Object fila[] = new Object[6];
				
				fila[0] = Rs.getInt("id_cliente");
				fila[1] = Rs.getString("nombre_completo");
				fila[2] = Rs.getString("direccion");
				fila[3] = Rs.getString("telefono");
				fila[4] = Rs.getString("correo");
				fila[5] = Rs.getString("fecha_registro");

				modelo.addRow(fila);
			}

			//Cerramos conexión
			con.close();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
		}
	}

	//Método limpiar
	public void limpiar() {

		TxtNombre.setText("");
		TxtDireccion.setText("");
		TxtTelefono.setValue(null); //Se usa este para que reseteé respetando la máscara 
		TxtCorreo.setText("");
		TxtFechaRegistro.setValue(null); //Se usa este para que reseteé respetando la máscara 
		
		//Reseteamos el ID y los valores originales
		idCliente = 0;
		nombreOriginal = "";
		direccionOriginal = "";
		telefonoOriginal = "";
		correoOriginal = "";
		fechaOriginal = "";
	}
}
