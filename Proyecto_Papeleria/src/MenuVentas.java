import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class MenuVentas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField TxtProductos;
	private JTable TbProductosDsp;
	private JTable TbCarritoCompras;
	private JLabel LblSubtotal;
	
	//Variable para agregar filas y columnas a la tabla 
	DefaultTableModel modelo = new DefaultTableModel();
	
	DefaultTableModel modeloCarrito = new DefaultTableModel();

	//Variables para conectarnos a la base de datos
	Connection Conexion = null; 
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuVentas frame = new MenuVentas();
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
	public MenuVentas() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 833, 628);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVentas = new JLabel("Ventas");
		lblVentas.setBounds(296, 10, 113, 38);
		lblVentas.setFont(new Font("Century Gothic", Font.BOLD, 30));
		contentPane.add(lblVentas);
		
		JButton BtnSalir = new JButton("Volver al menú principal");
		BtnSalir.setBounds(639, 559, 170, 31);
		BtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Volvemos al menú principal
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		contentPane.add(BtnSalir);
		
		JLabel LblBusquedaProductos = new JLabel("BUSQUEDA DE PRODUCTOS");
		LblBusquedaProductos.setFont(new Font("Tahoma", Font.BOLD, 16));
		LblBusquedaProductos.setBounds(20, 64, 237, 19);
		contentPane.add(LblBusquedaProductos);
		
		JLabel LblNombreProducto = new JLabel("Nombre del producto:");
		LblNombreProducto.setBounds(20, 93, 129, 12);
		contentPane.add(LblNombreProducto);
		
		TxtProductos = new JTextField();
		TxtProductos.setBounds(150, 90, 129, 18);
		contentPane.add(TxtProductos);
		TxtProductos.setColumns(10);
		
		JButton BtnBuscar = new JButton("Buscar");
		BtnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Ejecutamos el método que mostrará los productos
				mostrarProductos();
				
			}
		});
		BtnBuscar.setBounds(285, 89, 84, 20);
		contentPane.add(BtnBuscar);
		
		JLabel LblProductosDisponibles = new JLabel("PRODUCTOS DISPONIBLES");
		LblProductosDisponibles.setFont(new Font("Tahoma", Font.BOLD, 16));
		LblProductosDisponibles.setBounds(20, 125, 237, 19);
		contentPane.add(LblProductosDisponibles);
		
		JScrollPane scrollPane_Productos = new JScrollPane();
		scrollPane_Productos.setBounds(20, 154, 349, 279);
		contentPane.add(scrollPane_Productos);
		
		//Crear la tabla
		TbProductosDsp = new JTable();
		scrollPane_Productos.setViewportView(TbProductosDsp);
		
		//Columna de la tabla
		modelo.addColumn("ID");
		modelo.addColumn("Producto");
		modelo.addColumn("Precio");
		modelo.addColumn("Stock");
		
		//Le asignamos el modelo a la tabla
		TbProductosDsp.setModel(modelo);
		
		TbProductosDsp.setDefaultEditor(Object.class, null);
		
		JLabel lblCarritoDeCompras = new JLabel("CARRITO DE COMPRAS");
		lblCarritoDeCompras.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCarritoDeCompras.setBounds(506, 64, 201, 19);
		contentPane.add(lblCarritoDeCompras);
		
		JScrollPane scrollPane_Compras = new JScrollPane();
		scrollPane_Compras.setBounds(437, 90, 349, 306);
		contentPane.add(scrollPane_Compras);
		
		TbCarritoCompras = new JTable();
		
		// Columnas de la tabla del carrito
		modeloCarrito.addColumn("ID");
		modeloCarrito.addColumn("Producto");
		modeloCarrito.addColumn("Precio");
		modeloCarrito.addColumn("Cantidad");
		modeloCarrito.addColumn("Subtotal");

		// Asignamos el modelo a la tabla
		TbCarritoCompras.setModel(modeloCarrito);
		
		TbCarritoCompras.setDefaultEditor(Object.class, null);
		// Ocultamos la columna ID
		TbCarritoCompras.getColumnModel().getColumn(0).setMinWidth(0);
		TbCarritoCompras.getColumnModel().getColumn(0).setMaxWidth(0);
		TbCarritoCompras.getColumnModel().getColumn(0).setWidth(0);

		// Hacemos que la tabla no se pueda editar
		TbCarritoCompras.setDefaultEditor(Object.class, null);
		
		scrollPane_Compras.setViewportView(TbCarritoCompras);
		
		JButton BtnEliminar = new JButton("Eliminar Producto");
		BtnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				int fila = TbCarritoCompras.getSelectedRow();

				if (fila == -1) {
					JOptionPane.showMessageDialog(
							null,
							"Seleccione el producto que desea eliminar");
					return;
				}
				 
				// Recuperamos datos
				int idProducto = Integer.parseInt(
						TbCarritoCompras.getValueAt(fila, 0).toString());

				int cantidad = Integer.parseInt(
						TbCarritoCompras.getValueAt(fila, 3).toString());
				
				try {

					Conexion con = new Conexion();
					Connection cn = con.conectar();
					
					// Regresamos el stock
					String sql = "UPDATE Productos SET stock = stock + ? "
							   + "WHERE id_producto = ?";
					
					PreparedStatement ps = cn.prepareStatement(sql);
					
					ps.setInt(1, cantidad);
					ps.setInt(2, idProducto);
					
					ps.executeUpdate();

					ps.close();
					cn.close();

				} catch (Exception e1) {

					JOptionPane.showMessageDialog(
							null,
							"Error al devolver stock: " + e1.getMessage());
				}
				
				// Eliminamos del carrito
				modeloCarrito.removeRow(fila);
				
				// Actualizamos tabla
				mostrarProductos();
				
				// Actualizamos total
				calcularTotal();
				
				// Mensaje de éxito
				JOptionPane.showMessageDialog(
						null,
						"Producto eliminado correctamente.");
			}
		});
		BtnEliminar.setBounds(437, 406, 141, 42);
		contentPane.add(BtnEliminar);
		
		JButton BtnModificarCantidad = new JButton("Modificar Cantidad");
		BtnModificarCantidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int fila = TbCarritoCompras.getSelectedRow();

				if (fila == -1) {

					JOptionPane.showMessageDialog(
							null,
							"Seleccione un producto del carrito.");

					return;
				}

				String texto = JOptionPane.showInputDialog(
						null,
						"Ingrese la nueva cantidad:");

				if (texto == null) {
					return;
				}

				int nuevaCantidad = Integer.parseInt(texto);

				if (nuevaCantidad <= 0) {

					JOptionPane.showMessageDialog(
							null,
							"La cantidad debe ser mayor a cero.");

					return;
				}

				int idProducto = Integer.parseInt(
						TbCarritoCompras.getValueAt(fila, 0).toString());

				int cantidadAnterior = Integer.parseInt(
						TbCarritoCompras.getValueAt(fila, 3).toString());

				double precio = Double.parseDouble(
						TbCarritoCompras.getValueAt(fila, 2).toString());

				int diferencia = nuevaCantidad - cantidadAnterior;

				try {

					Conexion con = new Conexion();
					Connection cn = con.conectar();

					// Consultamos stock actual
					String sqlStock =
							"SELECT stock FROM Productos "
						  + "WHERE id_producto = ?";

					PreparedStatement psStock =
							cn.prepareStatement(sqlStock);

					psStock.setInt(1, idProducto);

					ResultSet rs = psStock.executeQuery();

					int stockActual = 0;

					if (rs.next()) {

						stockActual = rs.getInt("stock");
					}

					// Validamos stock
					if (diferencia > stockActual) {

						JOptionPane.showMessageDialog(
								null,
								"No hay suficiente stock.");

						rs.close();
						psStock.close();
						cn.close();

						return;
					}

					// Actualizamos stock
					String sqlUpdate =
							"UPDATE Productos "
						  + "SET stock = stock - ? "
						  + "WHERE id_producto = ?";

					PreparedStatement psUpdate =
							cn.prepareStatement(sqlUpdate);

					psUpdate.setInt(1, diferencia);
					psUpdate.setInt(2, idProducto);

					psUpdate.executeUpdate();

					psUpdate.close();
					psStock.close();
					rs.close();
					cn.close();

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(
							null,
							"Error: " + ex.getMessage());

					return;
				}

				double subtotal = precio * nuevaCantidad;

				modeloCarrito.setValueAt(nuevaCantidad, fila, 3);
				modeloCarrito.setValueAt(subtotal, fila, 4);

				mostrarProductos();
				calcularTotal();

				JOptionPane.showMessageDialog(
						null,
						"Cantidad modificada correctamente.");
			}
		});
			
				
		BtnModificarCantidad.setBounds(639, 406, 147, 42);
		contentPane.add(BtnModificarCantidad);
		
		JButton BtnAgregar = new JButton("Aregar al carrito");
		BtnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Verificamos que haya un producto seleccionado
				int fila = TbProductosDsp.getSelectedRow();
				
				if (fila == -1) {
					JOptionPane.showMessageDialog(null,
							"Seleccione un producto de la tabla.");
					return;
				}
				
				// Pedimos la cantidad al usuario
				String cantidadTexto = JOptionPane.showInputDialog(
						null,
						"Ingrese la cantidad:");
				
				// Si cancela, no hacemos nada
				if (cantidadTexto == null) {
					return;
				}
				
				int cantidad = Integer.parseInt(cantidadTexto);
				
				// Obtenemos los datos del producto seleccionado
				int idProducto = Integer.parseInt(
						TbProductosDsp.getValueAt(fila, 0).toString());
				
				String nombre = TbProductosDsp.getValueAt(fila, 1).toString();
				
				double precio = Double.parseDouble(
						TbProductosDsp.getValueAt(fila, 2).toString());
				
				int stock = Integer.parseInt(
						TbProductosDsp.getValueAt(fila, 3).toString());
				
				// Verificamos que la cantidad sea válida
				if (cantidad <= 0) {
					JOptionPane.showMessageDialog(null,
							"La cantidad debe ser mayor a cero.");
					return;
				}
				
				if (cantidad > stock) {
					JOptionPane.showMessageDialog(null,
							"No hay suficiente stock.");
					return;
				}
				
				// Calculamos el subtotal
				double subtotal = precio * cantidad;
				
				// Creamos la fila
				Object datos[] = new Object[5];
				
				datos[0] = idProducto;
				datos[1] = nombre;
				datos[2] = precio;
				datos[3] = cantidad;
				datos[4] = subtotal;
				
				try {

					Conexion con = new Conexion();
					Connection cn = con.conectar();

					// Bajamos el stock
					String sql = "UPDATE Productos "
							   + "SET stock = stock - ? "
							   + "WHERE id_producto = ?";

					PreparedStatement ps = cn.prepareStatement(sql);

					ps.setInt(1, cantidad);
					ps.setInt(2, idProducto);

					ps.executeUpdate();

					ps.close();
					cn.close();

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(
							null,
							"Error al actualizar stock: "
							+ ex.getMessage());

					return;
				}

				// Agregamos la fila al carrito
				modeloCarrito.addRow(datos);

				// Actualizamos tabla
				mostrarProductos();

				// Actualizamos total
				calcularTotal();
			}
		});
		BtnAgregar.setBounds(236, 443, 136, 44);
		contentPane.add(BtnAgregar);
		
		JButton BtnVenta = new JButton("Venta");
		BtnVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				if (modeloCarrito.getRowCount() == 0) {
					JOptionPane.showMessageDialog(
							null,
							"No hay productos en el carrito.");
					return;
				}

				// Variables
				int totalProductos = 0;
				double totalVenta = 0;

				// Recorremos el carrito
				for (int i = 0; i < modeloCarrito.getRowCount(); i++) {

					int cantidad = Integer.parseInt(
							modeloCarrito.getValueAt(i, 3).toString());

					double subtotal = Double.parseDouble(
							modeloCarrito.getValueAt(i, 4).toString());

					totalProductos += cantidad;
					totalVenta += subtotal;
				}

				// Mostramos resumen
				JOptionPane.showMessageDialog(
						null,
						"Venta realizada correctamente."
						+ "\n\nProductos vendidos: " + totalProductos
						+ "\nTotal pagado: $" + totalVenta);

				// Limpiamos carrito
				modeloCarrito.setRowCount(0);

				// Actualizamos total
				calcularTotal();
			}
		});
		
		BtnVenta.setBounds(639, 469, 147, 42);
		contentPane.add(BtnVenta);
		
		LblSubtotal = new JLabel("Subtotal: $0.0");
		LblSubtotal.setFont(new Font("Tahoma", Font.BOLD, 16));
		LblSubtotal.setBounds(490, 479, 170, 19);
		contentPane.add(LblSubtotal);
		
		TbProductosDsp.getColumnModel().getColumn(0).setMinWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setMaxWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setWidth(0);
		
		// Cargamos los productos al abrir la ventana
		mostrarProductos();
		
		//public void calcularTotal() {

		
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
	

	public void mostrarProductos() {
	
		String Valores[] = new String[4];
		String CadSQL = "";
	
		// Limpiamos la tabla para evitar duplicados
			modelo.setRowCount(0);
		
			try {
				// Abre la conexión a la base de datos
				Conexion con = new Conexion();
				Conexion = con.conectar(); //conexion miniscula da error
			
				// Consulta SQL para obtener los productos
				CadSQL = "SELECT id_producto, nombre, precio_venta, stock FROM Productos";

			if (TxtProductos.getText().trim().length() > 0)
			{
				String texto = TxtProductos.getText();

			    texto = texto.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o")
			                 .replace("ú", "u").replace("Á", "A") .replace("É", "E").replace("Í", "I")
			                 .replace("Ó", "O").replace("Ú", "U");

			    CadSQL += " WHERE " + "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(nombre, " +
			              "'á','a'),'é','e'),'í','i'),'ó','o'),'ú','u')" +" LIKE '%" + texto + "%'";
			}
			
			// Prepara la consulta
			ps = Conexion.prepareStatement(CadSQL);
			
			// Ejecuta la consulta
			rs = ps.executeQuery();
			
			// Mientras haya registros
			while (rs.next()) {

				// Guardamos cada dato en el arreglo
				Valores[0] = rs.getString("id_producto");
				Valores[1] = rs.getString("nombre");
				Valores[2] = rs.getString("precio_venta");
				Valores[3] = rs.getString("stock");

				// Agregamos la fila al modelo
				modelo.addRow(Valores);
				
				}

				// Cerramos recursos
				Conexion.close();

			} catch (Exception e1) {
		
				JOptionPane.showMessageDialog(null,"Error al mostrar productos: " + e1.toString());
		}
	
	}

	public void calcularTotal() {
		
		double total = 0;

		for (int i = 0; i < modeloCarrito.getRowCount(); i++) {

			total += Double.parseDouble(modeloCarrito.getValueAt(i, 4).toString());

		}

		LblSubtotal.setText("Total: $" + total);
	}
}