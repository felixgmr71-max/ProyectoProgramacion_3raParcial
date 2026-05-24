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

	// Panel principal
	private JPanel contentPane;

	// Caja de texto para buscar productos
	private JTextField TxtProductos;

	// Tablas
	private JTable TbProductosDsp;
	private JTable TbCarritoCompras;

	// Label para mostrar subtotal
	private JLabel LblSubtotal;

	// Modelo de tabla de productos
	DefaultTableModel modelo = new DefaultTableModel();

	// Modelo de tabla del carrito
	DefaultTableModel modeloCarrito = new DefaultTableModel();

	// Variables para conexión SQL
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
		setLocationRelativeTo(null); //Centra la pantalla
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setResizable(false); // Evita cambiar tamaño o maximizar

		
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
		
		// Evita la edicion de la tabla
		TbProductosDsp.setDefaultEditor(Object.class, null);
		
		TbProductosDsp.getColumnModel().getColumn(0).setMinWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setMaxWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setWidth(0);
		
		// Evita mover columnas
		TbProductosDsp.getTableHeader().setReorderingAllowed(false);

		// Evita cambiar tamaño columnas
		TbProductosDsp.getTableHeader().setResizingAllowed(false);
		
		JLabel lblCarritoDeCompras = new JLabel("CARRITO DE COMPRAS");
		lblCarritoDeCompras.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCarritoDeCompras.setBounds(506, 64, 201, 19);
		contentPane.add(lblCarritoDeCompras);
		
		JScrollPane scrollPane_Compras = new JScrollPane();
		scrollPane_Compras.setBounds(437, 90, 349, 306);
		contentPane.add(scrollPane_Compras);
		
		TbCarritoCompras = new JTable();
		
		scrollPane_Compras.setViewportView(TbCarritoCompras);
		
		// Columnas de la tabla del carrito
		modeloCarrito.addColumn("ID");
		modeloCarrito.addColumn("Producto");
		modeloCarrito.addColumn("Precio");
		modeloCarrito.addColumn("Cantidad");
		modeloCarrito.addColumn("Subtotal");

		// Asignamos el modelo a la tabla
		TbCarritoCompras.setModel(modeloCarrito);
		
		// Evita la edicion de la tabla
		TbCarritoCompras.setDefaultEditor(Object.class, null);
		
		// Ocultamos la columna ID
		TbCarritoCompras.getColumnModel().getColumn(0).setMinWidth(0);
		TbCarritoCompras.getColumnModel().getColumn(0).setMaxWidth(0);
		TbCarritoCompras.getColumnModel().getColumn(0).setWidth(0);

		// Hacemos que la tabla no se pueda editar
		TbCarritoCompras.setDefaultEditor(Object.class, null);
		
		// Evita mover columnas
		TbCarritoCompras.getTableHeader().setReorderingAllowed(false);

		// Evita cambiar tamaño columnas
		TbCarritoCompras.getTableHeader().setResizingAllowed(false);
		
		
		JButton BtnEliminar = new JButton("Eliminar Producto");
		BtnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				int fila = TbCarritoCompras.getSelectedRow();

				if (fila == -1) {
					JOptionPane.showMessageDialog(null,"Seleccione el producto que desea eliminar",
							"Eliminar producto",JOptionPane.WARNING_MESSAGE);
					return;
				}
				 
				// Recuperamos datos
				int idProducto = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 0).toString());

				int cantidad = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 3).toString());
				
				try {

					Conexion con = new Conexion();
					Connection cn = con.conectar();
					
					// Regresamos el stock
					String sql = "UPDATE Productos SET stock = stock + ? " + "WHERE id_producto = ?";
					
					PreparedStatement ps = cn.prepareStatement(sql);
					
					ps.setInt(1, cantidad);
					ps.setInt(2, idProducto);
					
					ps.executeUpdate();

					ps.close();
					cn.close();

				} catch (Exception e1) {

					JOptionPane.showMessageDialog(null,"Error al devolver stock: " 
					+ e1.getMessage(),"Error Stock",JOptionPane.ERROR_MESSAGE);
				}
				
				// Eliminamos del carrito
				modeloCarrito.removeRow(fila);
				
				// Actualizamos tabla
				mostrarProductos();
				
				// Actualizamos total
				calcularTotal();
				
				JOptionPane.showMessageDialog(null,"Producto eliminado correctamente",
						"Exito",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		BtnEliminar.setBounds(437, 406, 141, 42);
		contentPane.add(BtnEliminar);
		
		JButton BtnModificarCantidad = new JButton("Modificar Cantidad");
		BtnModificarCantidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int fila = TbCarritoCompras.getSelectedRow();

				if (fila==-1) {

					JOptionPane.showMessageDialog(null,"Seleccione un producto del carrito",
							"Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}

				String texto = JOptionPane.showInputDialog(null,"Ingrese la nueva cantidad: ",
						"Modificación",JOptionPane.INFORMATION_MESSAGE);

				if (texto==null) {
					return;
				}

				// Validamos números
				if (!texto.matches("\\d+")) {

					JOptionPane.showMessageDialog(null,"Ingrese solamente números",
							"Error",JOptionPane.ERROR_MESSAGE);

					return;
				}

				int nuevaCantidad = Integer.parseInt(texto);

				if (nuevaCantidad <= 0) {

					JOptionPane.showMessageDialog(null,"La cantidad debe ser mayor a 0",
							"Aviso",JOptionPane.WARNING_MESSAGE);

					return;
				}

				int idProducto = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 0).toString());

				int cantidadAnterior = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 3).toString());

				double precio = Double.parseDouble(TbCarritoCompras.getValueAt(fila, 2).toString());

				int diferencia =nuevaCantidad - cantidadAnterior;

				try {

					Conexion con = new Conexion();

					Connection cn = con.conectar();

					// Consultamos stock
					String sqlStock ="SELECT stock " + "FROM Productos " + "WHERE id_producto = ?";

					PreparedStatement psStock =cn.prepareStatement(sqlStock);

					psStock.setInt(1, idProducto);

					ResultSet rs =psStock.executeQuery();

					int stockActual = 0;

					if (rs.next()) {

						stockActual = rs.getInt("stock");
					}

					// Validamos stock
					if (diferencia > stockActual) {

						JOptionPane.showMessageDialog(null,"No hay suficiente stock",
								"Error con el Stock",JOptionPane.ERROR_MESSAGE);

						rs.close();
						psStock.close();
						cn.close();

						return;
					}

					// Actualizamos stock
					String sqlUpdate ="UPDATE Productos " + "SET stock = stock - ? " + "WHERE id_producto = ?";

					PreparedStatement psUpdate =cn.prepareStatement(sqlUpdate);

					psUpdate.setInt(1, diferencia);
					psUpdate.setInt(2, idProducto);

					psUpdate.executeUpdate();

					psUpdate.close();
					psStock.close();
					rs.close();
					cn.close();

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(null,"Error al modificar",
							"Error",JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Nuevo subtotal
				double subtotal = precio * nuevaCantidad;

				// Actualizamos tabla
				modeloCarrito.setValueAt(nuevaCantidad, fila, 3);

				modeloCarrito.setValueAt(subtotal, fila, 4);

				// Refrescamos
				mostrarProductos();

				calcularTotal();

				JOptionPane.showMessageDialog(null,"Cantidad modificada correctamente",
						"Modificación exitosa",JOptionPane.INFORMATION_MESSAGE);
			}
		});
			
				
		BtnModificarCantidad.setBounds(639, 406, 147, 42);
		contentPane.add(BtnModificarCantidad);
		
		JButton BtnAgregar = new JButton("Aregar al carrito");
		BtnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Verificamos que haya un producto seleccionado
				int fila = TbProductosDsp.getSelectedRow();

				if (fila==-1) {

					JOptionPane.showMessageDialog(null,"Seleccione un producto de la tabla",
							"Aviso",JOptionPane.WARNING_MESSAGE);

					return;
				}

				// Pedimos la cantidad
				String cantidadTexto = JOptionPane.showInputDialog(null,"Ingrese la cantidad:",
						"Cantidad de producto",JOptionPane.INFORMATION_MESSAGE);

				// Si cancela
				if (cantidadTexto == null) {
					return;
				}

				// Quitamos espacios
				cantidadTexto = cantidadTexto.trim();

				// Validamos vacío
				if (cantidadTexto.isEmpty()) {

					JOptionPane.showMessageDialog(null,"No deje el campo vacío",
							"Error",JOptionPane.ERROR_MESSAGE);

					return;
				}

				// Validamos que sea número
				int cantidad;

				try {

					cantidad = Integer.parseInt(cantidadTexto);

				} catch (NumberFormatException ex) {

					JOptionPane.showMessageDialog(null,"Ingrese solamente números",
							"Error",JOptionPane.ERROR_MESSAGE);

					return;
				}

				// Obtenemos datos del producto
				int idProducto = Integer.parseInt(TbProductosDsp.getValueAt(fila, 0).toString());

				String nombre = TbProductosDsp.getValueAt(fila, 1).toString();

				double precio = Double.parseDouble(TbProductosDsp.getValueAt(fila, 2).toString());

				int stock = Integer.parseInt(TbProductosDsp.getValueAt(fila, 3).toString());

				// Validamos cantidad
				if (cantidad<=0) {

					JOptionPane.showMessageDialog(null,"La cantidad debe ser mayor a cero",
							"Error",JOptionPane.ERROR_MESSAGE);

					return;
				}

				// Validamos stock
				if (cantidad>stock) {

					JOptionPane.showMessageDialog(null,"No hay suficiente stock",
							"Error con el Stock",JOptionPane.ERROR_MESSAGE);

					return;
				}

				// Calculamos subtotal
				double subtotal = precio * cantidad;

				// Creamos fila
				Object datos[] = new Object[5];

				datos[0] = idProducto;
				datos[1] = nombre;
				datos[2] = precio;
				datos[3] = cantidad;
				datos[4] = subtotal;

				try {

					Conexion con = new Conexion();
					Connection cn = con.conectar();

					// Bajamos stock
					String sql = "UPDATE Productos " + "SET stock = stock - ? " + "WHERE id_producto = ?";

					PreparedStatement ps = cn.prepareStatement(sql);

					ps.setInt(1, cantidad);
					ps.setInt(2, idProducto);

					ps.executeUpdate();

					ps.close();
					cn.close();

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(null,"Error al actualizar stock: " + ex.getMessage(),
							"Error",JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Agregamos al carrito
				modeloCarrito.addRow(datos);

				// Actualizamos tabla
				mostrarProductos();

				// Actualizamos subtotal
				calcularTotal();

				JOptionPane.showMessageDialog(null,"Producto agregado correctamente",
						"Éxito",JOptionPane.INFORMATION_MESSAGE);
			}
		});

		BtnAgregar.setBounds(236, 443, 136, 44);
		contentPane.add(BtnAgregar);
		
		JButton BtnVenta = new JButton("Venta");
		BtnVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					//Verificacion del carrito, si hay productos
				if (modeloCarrito.getRowCount() == 0) {
					 	 
					JOptionPane.showMessageDialog(null,"No hay productos en el carrito","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Variables
				int totalProductos = 0;
				
				double subtotalVenta = 0;

				// Recorremos el carrito
				for (int i = 0; i < modeloCarrito.getRowCount(); i++) {

					int cantidad = Integer.parseInt(
							modeloCarrito.getValueAt(i, 3).toString());

					double subtotal = Double.parseDouble(
							modeloCarrito.getValueAt(i, 4).toString());

					totalProductos += cantidad;
					
					subtotalVenta += subtotal;
				}

				double iva = subtotalVenta * 0.16;
				
				double totalFinal = subtotalVenta + iva;

				
				JOptionPane.showMessageDialog(null,"VENTA REALIZADA CORRECTAMENTE"
						+ "\n\nProductos vendidos: " + totalProductos
						+ "\nSubtotal: $" + String.format("%.2f", subtotalVenta)
						+ "\n+ IVA (16%): $" + String.format("%.2f", iva)
						+ "\nTOTAL: $" + String.format("%.2f", totalFinal),
						"¡VENTA EXITOSA!",JOptionPane.INFORMATION_MESSAGE);
				
				// Limpia el carrito
				modeloCarrito.setRowCount(0);

				// Actualiza el total
				calcularTotal();
		
			}
		});
		
		BtnVenta.setBounds(639, 469, 147, 42);
		contentPane.add(BtnVenta);
		
		LblSubtotal = new JLabel("Subtotal: $0.0");
		LblSubtotal.setFont(new Font("Tahoma", Font.BOLD, 16));
		LblSubtotal.setBounds(490, 479, 170, 19);
		contentPane.add(LblSubtotal);
		
		// Cargamos los productos al abrir la ventana
		mostrarProductos();
		
		TbProductosDsp.getColumnModel().getColumn(0).setMinWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setMaxWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setWidth(0);
			
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
	
		String Valores[] =new String[4];
		String CadSQL = "";
	
		// Limpiamos la tabla para evitar duplicados
			modelo.setRowCount(0);
		
			try {
				// Abre la conexión a la base de datos
				Conexion con = new Conexion();
				Conexion = con.conectar(); //conexion miniscula da error
			
				// Consulta SQL para obtener los productos
				CadSQL = "SELECT id_producto, nombre, precio_venta, stock FROM Productos";

				// Si escribió algo
			if (TxtProductos.getText().trim().length() > 0)
			{
				String texto = TxtProductos.getText();
				
				//Funcion para los acentos, quitamos acentos
			    texto = texto.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o")
			                 .replace("ú", "u").replace("Á", "A") .replace("É", "E").replace("Í", "I")
			                 .replace("Ó", "O").replace("Ú", "U");
			    
			    //Búsqueda
			    CadSQL += " WHERE " + "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(nombre, " +
			              "'á','a'),'é','e'),'í','i'),'ó','o'),'ú','u')" +" LIKE '%" +texto+ "%'";
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
		
				JOptionPane.showMessageDialog(null,"Error al mostrar productos: " + e1.toString(),
				"Error de productos",JOptionPane.ERROR_MESSAGE);
		}
	
	}

	public void calcularTotal() {
		
		double total = 0;

		for (int i=0; i<modeloCarrito.getRowCount(); i++) {

			total += Double.parseDouble(modeloCarrito.getValueAt(i, 4).toString());

		}

		//Mostramos el subtotal
		LblSubtotal.setText("Total: $" + total);
	}
}