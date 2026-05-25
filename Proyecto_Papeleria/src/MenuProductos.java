import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Color;

public class MenuProductos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable TablaProductos;
	
	DefaultTableModel modelo = new DefaultTableModel();//Creamos el modelo para crear las columnas de la tabla
	
	//Variable para variables de acceso a datos
	
		Connection Conexion = null;
		Statement SentenciaSQL = null;
		ResultSet Rs = null;
		private JTextField txtBuscar;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuProductos frame = new MenuProductos();
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
	public MenuProductos() {
		setTitle("Ventana de Productos");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\Downloads\\papeleria (1).png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 819, 425);
		setLocationRelativeTo(null); //Centra la ventana
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		//Columnas de la tabla
		modelo.addColumn("ID");
		modelo.addColumn("CODIGO");
		modelo.addColumn("NOMBRE");
		modelo.addColumn("PRECIO DE COMPRA");
		modelo.addColumn("PRECIO DE VENTA");
		modelo.addColumn("STOCK");
		
		//Tabla
		TablaProductos = new JTable(modelo);
		// Cambiar la fuente del encabezado (letras en negrita)
		TablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

		// Cambiar el color de fondo del encabezado 
		TablaProductos.getTableHeader().setBackground(new Color(0, 91, 159)); 
		// Cambiar el color de la letra del encabezado a blanco
		TablaProductos.getTableHeader().setForeground(Color.WHITE);
		
		// Ajustamos las columnas para aprovechar espacio
		TablaProductos.getColumnModel().getColumn(1).setPreferredWidth(80);
		TablaProductos.getColumnModel().getColumn(1).setMaxWidth(100);

		// columna del nombre
		TablaProductos.getColumnModel().getColumn(2).setPreferredWidth(300);
		TablaProductos.getColumnModel().getColumn(2).setMaxWidth(310);

		// columna del precio compra
		TablaProductos.getColumnModel().getColumn(3).setPreferredWidth(140);
		TablaProductos.getColumnModel().getColumn(3).setMaxWidth(150);

		// columna del precio venta
		TablaProductos.getColumnModel().getColumn(4).setPreferredWidth(140);
		TablaProductos.getColumnModel().getColumn(4).setMaxWidth(150);

		// columna del stock
		TablaProductos.getColumnModel().getColumn(5).setPreferredWidth(60);
		TablaProductos.getColumnModel().getColumn(5).setMaxWidth(80);
		
		TablaProductos.setGridColor(new Color(102, 167, 215));
		TablaProductos.setSelectionForeground(new Color(255, 255, 255));
		TablaProductos.setSelectionBackground(new Color(0, 64, 128));
		TablaProductos.setFont(new Font("Segoe UI", Font.BOLD, 14));
		TablaProductos.setRowHeight(25);
		scrollPane.setViewportView(TablaProductos);
		
		//Personalizar tabla
		TablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TablaProductos.setRowSelectionAllowed(true);
		TablaProductos.setFillsViewportHeight(true);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 64, 128));
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblProductos = new JLabel("Productos");
		lblProductos.setForeground(new Color(255, 255, 255));
		lblProductos.setFont(new Font("Century Gothic", Font.BOLD, 30));
		panel.add(lblProductos);
		
		txtBuscar = new JTextField();
		txtBuscar.setColumns(10);
		panel.add(txtBuscar);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String termino = txtBuscar.getText().trim();
		        
		        //Si la caja está vacía, recargamos la tabla
		        if (termino.isEmpty()) {
		            Mostrar_Informacion(); 
		            return;
		        }
		        
		        modelo.setRowCount(0);
		        
		        try {
		            Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
		            
		            String sql = "SELECT id_producto, codigo, nombre, precio_compra, precio_venta, stock FROM Productos WHERE nombre LIKE ?";
		            java.sql.PreparedStatement pstmt = Conexion.prepareStatement(sql);
		            
		            // Inyectamos la palabra al '?' con los comodines %
		            pstmt.setString(1, "%" + termino + "%");
		            
		            ResultSet rs = pstmt.executeQuery();
		            
		            boolean hayResultados = false;
		            
		            // Llena la tabla usando un arreglo 
		            while (rs.next()) {
		                String[] Valores = new String[6]; 
		                Valores[0] = rs.getString("id_producto");
		                Valores[1] = rs.getString("codigo");
		                Valores[2] = rs.getString("nombre");
		                Valores[3] = rs.getString("precio_compra");
		                Valores[4] = rs.getString("precio_venta");
		                Valores[5] = rs.getString("stock");

		                modelo.addRow(Valores);
		                hayResultados = true;
		            }
		            
		            // Si no se encuentra nada, avisamos y mostramos todos de nuevo
		            if (hayResultados == false) {
		                JOptionPane.showMessageDialog(null, "No se encontraron coincidencias.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
		                txtBuscar.setText(""); // Limpiamos la caja de búsqueda
		                Mostrar_Informacion(); 
		            }
		            
		            pstmt.close();
		            Conexion.close();
		            
		        } catch (SQLException error) {
		            JOptionPane.showMessageDialog(null, "Error en la búsqueda: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
		btnBuscar.setForeground(new Color(0, 128, 192));
		btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel.add(btnBuscar);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 64, 128));
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		//---BOTON ELIMINAR-------------------------------------------------------------------------
				JButton btnEliminar = new JButton("Eliminar");
				btnEliminar.setForeground(new Color(0, 128, 192));
				btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				btnEliminar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int RegistroSeleccionado = TablaProductos.getSelectedRow();
						int confirmacion; 
						String idProducto;
						int filaAfectada;
						
						if(RegistroSeleccionado >= 0) {
							confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este producto de la base de datos? Está acción no se puede revertir.", 
									"CONFIRMAR ELIMINACION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							
							if(confirmacion == JOptionPane.YES_OPTION){
								idProducto = TablaProductos.getValueAt(RegistroSeleccionado, 0).toString();
								try {
									Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
									String ConsultaSQL = "DELETE FROM Productos WHERE id_Producto = ?";
									java.sql.PreparedStatement pstmt = Conexion.prepareStatement(ConsultaSQL);
									pstmt.setInt(1, Integer.parseInt(idProducto));
									filaAfectada = pstmt.executeUpdate();
									
									if (filaAfectada > 0) {
										JOptionPane.showMessageDialog(null, "¡Producto eliminado exitosamente!");
										Mostrar_Informacion(); 
									}
									pstmt.close();
									Conexion.close();
								}catch(SQLException e2){
									JOptionPane.showMessageDialog(null, "Sucedio un error al eliminar el registro: "+e2.getMessage());
								}
							}else {
								JOptionPane.showMessageDialog(null, "Acción cancelada.");
							}
						}else {
							JOptionPane.showMessageDialog(null, "Seleccione un registro para poder realizar esta acción.");
						}
					}
				});
				panel_1.add(btnEliminar);
				
		//---BOTON ACTUALIZAR-----------------------------------------------------------------------
				JButton btnActualizar = new JButton("Actualizar");
				btnActualizar.setForeground(new Color(0, 128, 192));
				btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				btnActualizar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int RegistroSeleccionado = TablaProductos.getSelectedRow();
						String id, codigo, nombre, precioCompra, precioVenta, stock;
						if(RegistroSeleccionado >= 0) {
							id = TablaProductos.getValueAt(RegistroSeleccionado, 0).toString();
							codigo = TablaProductos.getValueAt(RegistroSeleccionado, 1).toString();
							nombre = TablaProductos.getValueAt(RegistroSeleccionado, 2).toString();
							precioCompra = TablaProductos.getValueAt(RegistroSeleccionado, 3).toString();
							precioVenta = TablaProductos.getValueAt(RegistroSeleccionado, 4).toString();
							stock = TablaProductos.getValueAt(RegistroSeleccionado, 5).toString();
							
							IngresoDatos_Modificacion VentanaDatos = new IngresoDatos_Modificacion();
							VentanaDatos.cargarDatosParaActualizar(id, codigo, nombre, precioCompra, precioVenta, stock);
							VentanaDatos.setModal(true);
							VentanaDatos.setVisible(true);
							Mostrar_Informacion();
						}else {
							JOptionPane.showMessageDialog(null, "Seleccione un registro para poder realizar esta acción.");
						}
					}
				});
				panel_1.add(btnActualizar);
				
		//---BOTON AGREGAR--------------------------------------------------------------------------
				JButton btnAGREGAR = new JButton("Agregar");
				btnAGREGAR.setForeground(new Color(0, 128, 192));
				btnAGREGAR.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				btnAGREGAR.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						IngresoDatos_Modificacion VentanaDatos = new IngresoDatos_Modificacion();
						VentanaDatos.setModal(true);
						VentanaDatos.setVisible(true);
						Mostrar_Informacion();
					}
				});
				panel_1.add(btnAGREGAR);
				
		//---BOTON SALIR----------------------------------------------------------------------------
				JButton BtnSalir = new JButton("Volver al menú principal");
				BtnSalir.setForeground(new Color(0, 128, 192));
				BtnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				BtnSalir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						MenuPrincipal menuprincipal = new MenuPrincipal();
						menuprincipal.setVisible(true);
						dispose(); //Cerrar ventana
					}
				});
				panel_1.add(BtnSalir);
		
		//Programamos la tecla ESC para que al presionarla regrese al menú principal
		
		//1.Definimos que la tecla a escuchar es el ESC
		javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
		
		//2.Le decimos a la ventana que escuche esa tecla siempre que esté activa
		this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
		
		//3.Le decimos qué hacer cuando detecte la pulsación
		this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
				BtnSalir.doClick(); //Simula un clic en el botón de salir
				
			}
		});
		
		Mostrar_Informacion();

	}
	
	private void Mostrar_Informacion()
	{
		//Procedimiento para mostrar toda la info de la B.D. dentro de la tabla
		
		modelo.setRowCount(0);//Con esta linea nos encargamos de limpiar la tabla para que no se dupliquen datos
		
		String Valores[] = new String[6];
		
		try {
			
			Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
			
			SentenciaSQL = Conexion.createStatement();
			
			Rs = SentenciaSQL.executeQuery("SELECT id_producto, codigo, nombre, precio_compra, precio_venta, stock FROM Productos");
			
			while(Rs.next()) {
				
				Valores[0] = Rs.getString("id_producto");
				Valores[1] = Rs.getString("codigo");
				Valores[2] = Rs.getString("nombre");
				Valores[3] = Rs.getString("precio_compra");
				Valores[4] = Rs.getString("precio_venta");
				Valores[5] = Rs.getString("stock");
				
				modelo.addRow(Valores);
			}
			
			Conexion.close();
			
		}catch(SQLException e1) {
			
			JOptionPane.showMessageDialog(null, "Ocurrio un error al querer cargar los datos: "+e1.toString());
			
		}
		
		//propiedades para no mostrar el id en la tabla
	    TablaProductos.getColumnModel().getColumn(0).setMinWidth(0);
	    TablaProductos.getColumnModel().getColumn(0).setMaxWidth(0);
	    TablaProductos.getColumnModel().getColumn(0).setPreferredWidth(0);
		
	}
}
