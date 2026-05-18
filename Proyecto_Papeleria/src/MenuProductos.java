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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 819, 425);
		setLocationRelativeTo(null); //Centra la ventana
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblProductos = new JLabel("Productos");
		lblProductos.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblProductos.setBounds(10, 0, 157, 72);
		contentPane.add(lblProductos);
		
		JButton BtnSalir = new JButton("Volver al menú principal");
		BtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Volvemos al menú principal
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnSalir.setBounds(625, 358, 170, 20);
		contentPane.add(BtnSalir);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 59, 785, 240);
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
		scrollPane.setViewportView(TablaProductos);
		
		//Personalizar tabla
		TablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TablaProductos.setRowSelectionAllowed(true);
		TablaProductos.setFillsViewportHeight(true);
		
//---BOTON AGREGAR-----------------------------------------------------------------------------------------------------------------------------		
		JButton btnAGREGAR = new JButton("AGREGAR");
		btnAGREGAR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				IngresoDatos_Modificacion VentanaDatos = new IngresoDatos_Modificacion();//Se instancia la ventana donde se ingresaran los datos
				
				VentanaDatos.setModal(true);
				VentanaDatos.setVisible(true);//La ventana se hace visible
				
				
				Mostrar_Informacion();
				
			}
		});
		btnAGREGAR.setBounds(251, 309, 110, 31);
		contentPane.add(btnAGREGAR);
		
		JButton btnActualizar = new JButton("ACTUALIZAR");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int RegistroSeleccionado = TablaProductos.getSelectedRow();//Declaramos una variable y en ella guardamos el valor del registro seleccionado
				
				String id, codigo, nombre, precioCompra, precioVenta, stock;
				if(RegistroSeleccionado >= 0) {
					
					//Extraemos los datos de la fila seleccionada
					id = TablaProductos.getValueAt(RegistroSeleccionado, 0).toString();
					codigo = TablaProductos.getValueAt(RegistroSeleccionado, 1).toString();
					nombre = TablaProductos.getValueAt(RegistroSeleccionado, 2).toString();
					precioCompra = TablaProductos.getValueAt(RegistroSeleccionado, 3).toString();
					precioVenta = TablaProductos.getValueAt(RegistroSeleccionado, 4).toString();
					stock = TablaProductos.getValueAt(RegistroSeleccionado, 5).toString();
					
					//Instanciamos la ventana
					IngresoDatos_Modificacion VentanaDatos = new IngresoDatos_Modificacion();
					
					//Mandamos los datos
					VentanaDatos.cargarDatosParaActualizar(id, codigo, nombre, precioCompra, precioVenta, stock);
					
					// 4. Mostramos la ventana
					VentanaDatos.setModal(true);
					VentanaDatos.setVisible(true);
					
					// 5. Al cerrarse la ventana, refrescamos la tabla para ver los cambios
					Mostrar_Informacion();
					
					
					
				}else {
					
					JOptionPane.showMessageDialog(null, "Seleccione un registro para poder realizar esta acción.");
				}
			}
		});
		btnActualizar.setBounds(130, 309, 110, 31);
		contentPane.add(btnActualizar);
		
		
//---BOTON ELIMINAR-----------------------------------------------------------------------------------------------------------------------------
		JButton btnEliminar = new JButton("ELIMINAR");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int RegistroSeleccionado = TablaProductos.getSelectedRow();//Declaramos una variable y en ella guardamos el valor del registro seleccionado
				int confirmacion; //Esta variable nos ayudara a saber si el usuario deseaa eliminar el registro
				String idProducto;
				int filaAfectada;
				
				if(RegistroSeleccionado >= 0) {
					
					confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este producto de la base de datos? Está acción no se puede revertir.", 
							"CONFIRMAR ELIMINACION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
					if(confirmacion == JOptionPane.YES_OPTION){//Si la respuesta es si se ejecutara este registro
						
						idProducto = TablaProductos.getValueAt(RegistroSeleccionado, 0).toString();//Extraemos el id delproducto, el 0 es porque en esa fila se encuentra el id
						
						try {
							
							Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
							
							String ConsultaSQL = "DELETE FROM Productos WHERE id_Producto = ?";
							java.sql.PreparedStatement pstmt = Conexion.prepareStatement(ConsultaSQL);//Agarra tu conexión a la base de datos y le envía el texto que se creo arriba para que lo prepare.
							
							pstmt.setInt(1, Integer.parseInt(idProducto));// Le pasamos el ID que extrajimos de la tabla
							
							filaAfectada = pstmt.executeUpdate();//Ejecuta la accion
							
							if (filaAfectada > 0) {
								JOptionPane.showMessageDialog(null, "¡Producto eliminado exitosamente!");
								
								Mostrar_Informacion(); //Se actualiza la tabla
							}
							
							pstmt.close();//Destruye el objeto para liberar memoria tanto en la B.D. como en el equipo
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
		btnEliminar.setBounds(10, 309, 110, 31);
		contentPane.add(btnEliminar);
		
		txtBuscar = new JTextField();
		txtBuscar.setBounds(171, 29, 376, 20);
		contentPane.add(txtBuscar);
		txtBuscar.setColumns(10);
		
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
		btnBuscar.setBounds(557, 29, 109, 20);
		contentPane.add(btnBuscar);
		
		//Programamos la tecla ESC para que al presionarla regrese al menú principal
		
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
		
		
	}
}
