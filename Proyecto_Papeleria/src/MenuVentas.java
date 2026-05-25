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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

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
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuVentas frame = new MenuVentas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuVentas() {
		// --- CONFIGURACIÓN DE LA VENTANA ---
		setTitle("Ventas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 650); 
		setLocationRelativeTo(null); 
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0)); 
		
		// Columnas de la tabla de disponibles
		modelo.addColumn("ID");
		modelo.addColumn("Producto");
		modelo.addColumn("Precio");
		modelo.addColumn("Stock");
		
		// Columnas de la tabla del carrito
		modeloCarrito.addColumn("ID");
		modeloCarrito.addColumn("Producto");
		modeloCarrito.addColumn("Precio");
		modeloCarrito.addColumn("Cantidad");
		modeloCarrito.addColumn("Subtotal");

		JPanel pnlNorte = new JPanel();
		pnlNorte.setBackground(new Color(0, 64, 128));
		pnlNorte.setLayout(new BorderLayout());
		contentPane.add(pnlNorte, BorderLayout.NORTH);
		
		JLabel lblVentas = new JLabel("Ventas", SwingConstants.CENTER);
		lblVentas.setForeground(Color.WHITE);
		lblVentas.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblVentas.setBorder(new EmptyBorder(10, 0, 5, 0));
		pnlNorte.add(lblVentas, BorderLayout.NORTH);
		
		JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		pnlBusqueda.setOpaque(false); 
		
		JLabel LblNombreProducto = new JLabel("Buscar producto:");
		LblNombreProducto.setForeground(Color.WHITE);
		LblNombreProducto.setFont(new Font("Segoe UI", Font.BOLD, 14));
		pnlBusqueda.add(LblNombreProducto);
		
		TxtProductos = new JTextField();
		TxtProductos.setColumns(20);
		pnlBusqueda.add(TxtProductos);
		
		JButton BtnBuscar = new JButton("Buscar");
		BtnBuscar.setForeground(new Color(0, 128, 192));
		BtnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		BtnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarProductos(); 
			}
		});
		pnlBusqueda.add(BtnBuscar);
		pnlNorte.add(pnlBusqueda, BorderLayout.SOUTH);

		JPanel pnlSur = new JPanel();
		pnlSur.setBackground(new Color(0, 64, 128));
		pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		contentPane.add(pnlSur, BorderLayout.SOUTH);
		
		JButton BtnAgregar = new JButton("Agregar al carrito");
		BtnAgregar.setForeground(new Color(0, 128, 192));
		BtnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlSur.add(BtnAgregar);
		
		JButton BtnModificarCantidad = new JButton("Modificar Cantidad");
		BtnModificarCantidad.setForeground(new Color(0, 128, 192));
		BtnModificarCantidad.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlSur.add(BtnModificarCantidad);
		
		JButton BtnEliminar = new JButton("Eliminar Producto");
		BtnEliminar.setForeground(new Color(0, 128, 192));
		BtnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlSur.add(BtnEliminar);
		
		JButton BtnVenta = new JButton("Realizar Venta");
		BtnVenta.setForeground(new Color(0, 128, 192));
		BtnVenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlSur.add(BtnVenta);
		
		JButton BtnSalir = new JButton("Volver al menú principal");
		BtnSalir.setForeground(new Color(0, 128, 192));
		BtnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlSur.add(BtnSalir);
		
		LblSubtotal = new JLabel("Total: $0.0");
		LblSubtotal.setForeground(Color.WHITE);
		LblSubtotal.setFont(new Font("Tahoma", Font.BOLD, 18));
		LblSubtotal.setBorder(new EmptyBorder(0, 20, 0, 0));
		pnlSur.add(LblSubtotal);

		JPanel pnlCentro = new JPanel();
		pnlCentro.setBackground(Color.WHITE);
		pnlCentro.setLayout(new GridLayout(1, 2, 20, 0)); 
		pnlCentro.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.add(pnlCentro, BorderLayout.CENTER);
		
		// MITAD IZQUIERDA: PRODUCTOS 
		JPanel pnlIzquierdo = new JPanel(new BorderLayout(0, 10));
		pnlIzquierdo.setOpaque(false);
		
		JLabel LblProductosDisponibles = new JLabel("PRODUCTOS DISPONIBLES", SwingConstants.CENTER);
		LblProductosDisponibles.setFont(new Font("Tahoma", Font.BOLD, 16));
		LblProductosDisponibles.setForeground(new Color(0, 64, 128));
		pnlIzquierdo.add(LblProductosDisponibles, BorderLayout.NORTH);
		
		JScrollPane scrollPane_Productos = new JScrollPane();
		scrollPane_Productos.setBackground(Color.WHITE);
		TbProductosDsp = new JTable(modelo);
		scrollPane_Productos.setViewportView(TbProductosDsp);
		pnlIzquierdo.add(scrollPane_Productos, BorderLayout.CENTER);
		
		// MITAD DERECHA: CARRITO
		JPanel pnlDerecho = new JPanel(new BorderLayout(0, 10));
		pnlDerecho.setOpaque(false);
		
		JLabel lblCarritoDeCompras = new JLabel("CARRITO DE COMPRAS", SwingConstants.CENTER);
		lblCarritoDeCompras.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCarritoDeCompras.setForeground(new Color(0, 64, 128));
		pnlDerecho.add(lblCarritoDeCompras, BorderLayout.NORTH);
		
		JScrollPane scrollPane_Compras = new JScrollPane();
		scrollPane_Compras.setBackground(Color.WHITE);
		TbCarritoCompras = new JTable(modeloCarrito);
		scrollPane_Compras.setViewportView(TbCarritoCompras);
		pnlDerecho.add(scrollPane_Compras, BorderLayout.CENTER);
		
		pnlCentro.add(pnlIzquierdo);
		pnlCentro.add(pnlDerecho);

//ESTÉTICA Y RESTRICCIONES DE LAS TABLAS

		// Decoración Tabla Productos
		TbProductosDsp.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		TbProductosDsp.getTableHeader().setBackground(new Color(0, 91, 159)); 
		TbProductosDsp.getTableHeader().setForeground(Color.WHITE);
		TbProductosDsp.setGridColor(new Color(102, 167, 215));
		TbProductosDsp.setSelectionForeground(Color.WHITE);
		TbProductosDsp.setSelectionBackground(new Color(0, 64, 128));
		TbProductosDsp.setFont(new Font("Segoe UI", Font.BOLD, 14));
		TbProductosDsp.setRowHeight(25);
		TbProductosDsp.setDefaultEditor(Object.class, null);
		TbProductosDsp.getTableHeader().setReorderingAllowed(false);
		TbProductosDsp.getTableHeader().setResizingAllowed(false);
		
		// Decoración Tabla Carrito
		TbCarritoCompras.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		TbCarritoCompras.getTableHeader().setBackground(new Color(0, 91, 159)); 
		TbCarritoCompras.getTableHeader().setForeground(Color.WHITE);
		TbCarritoCompras.setGridColor(new Color(102, 167, 215));
		TbCarritoCompras.setSelectionForeground(Color.WHITE);
		TbCarritoCompras.setSelectionBackground(new Color(0, 64, 128));
		TbCarritoCompras.setFont(new Font("Segoe UI", Font.BOLD, 14));
		TbCarritoCompras.setRowHeight(25);
		TbCarritoCompras.setDefaultEditor(Object.class, null);
		TbCarritoCompras.getTableHeader().setReorderingAllowed(false);
		TbCarritoCompras.getTableHeader().setResizingAllowed(false);

		BtnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = TbProductosDsp.getSelectedRow();
				if (fila==-1) {
					JOptionPane.showMessageDialog(null,"Seleccione un producto de la tabla","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}
				String cantidadTexto = JOptionPane.showInputDialog(null,"Ingrese la cantidad:","Cantidad de producto",JOptionPane.INFORMATION_MESSAGE);
				if (cantidadTexto == null) return;
				
				cantidadTexto = cantidadTexto.trim();
				if (cantidadTexto.isEmpty()) {
					JOptionPane.showMessageDialog(null,"No deje el campo vacío","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				int cantidad;
				try {
					cantidad = Integer.parseInt(cantidadTexto);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,"Ingrese solamente números","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}

				int idProducto = Integer.parseInt(TbProductosDsp.getValueAt(fila, 0).toString());
				String nombre = TbProductosDsp.getValueAt(fila, 1).toString();
				double precio = Double.parseDouble(TbProductosDsp.getValueAt(fila, 2).toString());
				int stock = Integer.parseInt(TbProductosDsp.getValueAt(fila, 3).toString());

				if (cantidad<=0) {
					JOptionPane.showMessageDialog(null,"La cantidad debe ser mayor a cero","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (cantidad>stock) {
					JOptionPane.showMessageDialog(null,"No hay suficiente stock","Error con el Stock",JOptionPane.ERROR_MESSAGE);
					return;
				}

				double subtotal = precio * cantidad;
				Object datos[] = new Object[5];
				datos[0] = idProducto;
				datos[1] = nombre;
				datos[2] = precio;
				datos[3] = cantidad;
				datos[4] = subtotal;

				try {
					Conexion con = new Conexion();
					Connection cn = con.conectar();
					String sql = "UPDATE Productos SET stock = stock - ? WHERE id_producto = ?";
					PreparedStatement ps = cn.prepareStatement(sql);
					ps.setInt(1, cantidad);
					ps.setInt(2, idProducto);
					ps.executeUpdate();
					ps.close();
					cn.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,"Error al actualizar stock: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					return;
				}

				modeloCarrito.addRow(datos);
				mostrarProductos();
				calcularTotal();
				JOptionPane.showMessageDialog(null,"Producto agregado correctamente","Éxito",JOptionPane.INFORMATION_MESSAGE);
			}
		});

		BtnModificarCantidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = TbCarritoCompras.getSelectedRow();
				if (fila==-1) {
					JOptionPane.showMessageDialog(null,"Seleccione un producto del carrito","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}
				String texto = JOptionPane.showInputDialog(null,"Ingrese la nueva cantidad: ","Modificación",JOptionPane.INFORMATION_MESSAGE);
				if (texto==null) return;
				if (!texto.matches("\\d+")) {
					JOptionPane.showMessageDialog(null,"Ingrese solamente números","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				int nuevaCantidad = Integer.parseInt(texto);
				if (nuevaCantidad <= 0) {
					JOptionPane.showMessageDialog(null,"La cantidad debe ser mayor a 0","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}

				int idProducto = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 0).toString());
				int cantidadAnterior = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 3).toString());
				double precio = Double.parseDouble(TbCarritoCompras.getValueAt(fila, 2).toString());
				int diferencia =nuevaCantidad - cantidadAnterior;

				try {
					Conexion con = new Conexion();
					Connection cn = con.conectar();
					String sqlStock ="SELECT stock FROM Productos WHERE id_producto = ?";
					PreparedStatement psStock =cn.prepareStatement(sqlStock);
					psStock.setInt(1, idProducto);
					ResultSet rs =psStock.executeQuery();
					int stockActual = 0;
					if (rs.next()) {
						stockActual = rs.getInt("stock");
					}
					if (diferencia > stockActual) {
						JOptionPane.showMessageDialog(null,"No hay suficiente stock","Error con el Stock",JOptionPane.ERROR_MESSAGE);
						rs.close();
						psStock.close();
						cn.close();
						return;
					}

					String sqlUpdate ="UPDATE Productos SET stock = stock - ? WHERE id_producto = ?";
					PreparedStatement psUpdate =cn.prepareStatement(sqlUpdate);
					psUpdate.setInt(1, diferencia);
					psUpdate.setInt(2, idProducto);
					psUpdate.executeUpdate();
					psUpdate.close();
					psStock.close();
					rs.close();
					cn.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,"Error al modificar","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}

				double subtotal = precio * nuevaCantidad;
				modeloCarrito.setValueAt(nuevaCantidad, fila, 3);
				modeloCarrito.setValueAt(subtotal, fila, 4);
				mostrarProductos();
				calcularTotal();
				JOptionPane.showMessageDialog(null,"Cantidad modificada correctamente","Modificación exitosa",JOptionPane.INFORMATION_MESSAGE);
			}
		});

		BtnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = TbCarritoCompras.getSelectedRow();
				if (fila == -1) {
					JOptionPane.showMessageDialog(null,"Seleccione el producto que desea eliminar","Eliminar producto",JOptionPane.WARNING_MESSAGE);
					return;
				}
				int idProducto = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 0).toString());
				int cantidad = Integer.parseInt(TbCarritoCompras.getValueAt(fila, 3).toString());
				
				try {
					Conexion con = new Conexion();
					Connection cn = con.conectar();
					String sql = "UPDATE Productos SET stock = stock + ? WHERE id_producto = ?";
					PreparedStatement ps = cn.prepareStatement(sql);
					ps.setInt(1, cantidad);
					ps.setInt(2, idProducto);
					ps.executeUpdate();
					ps.close();
					cn.close();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Error al devolver stock: " + e1.getMessage(),"Error Stock",JOptionPane.ERROR_MESSAGE);
				}
				
				modeloCarrito.removeRow(fila);
				mostrarProductos();
				calcularTotal();
				JOptionPane.showMessageDialog(null,"Producto eliminado correctamente","Exito",JOptionPane.INFORMATION_MESSAGE);
			}
		});

		BtnVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (modeloCarrito.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null,"No hay productos en el carrito","Aviso",JOptionPane.WARNING_MESSAGE);
					return;
				}
				int totalProductos = 0;
				double subtotalVenta = 0;
				for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
					int cantidad = Integer.parseInt(modeloCarrito.getValueAt(i, 3).toString());
					double subtotal = Double.parseDouble(modeloCarrito.getValueAt(i, 4).toString());
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
				
				modeloCarrito.setRowCount(0);
				calcularTotal();
			}
		});

		BtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				dispose();
			}
		});

		// Atajo tecla ESCAPE
		javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
		this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				BtnSalir.doClick(); 
			}
		});

		mostrarProductos();
		
		// Ocultar columnas ID una vez que la tabla tiene datos
		TbProductosDsp.getColumnModel().getColumn(0).setMinWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setMaxWidth(0);
		TbProductosDsp.getColumnModel().getColumn(0).setWidth(0);
		
		TbCarritoCompras.getColumnModel().getColumn(0).setMinWidth(0);
		TbCarritoCompras.getColumnModel().getColumn(0).setMaxWidth(0);
		TbCarritoCompras.getColumnModel().getColumn(0).setWidth(0);
	}
	
	public void mostrarProductos() {
		String Valores[] =new String[4];
		String CadSQL = "";
		modelo.setRowCount(0);
		
		try {
			Conexion con = new Conexion();
			Conexion = con.conectar(); 
			CadSQL = "SELECT id_producto, nombre, precio_venta, stock FROM Productos";

			if (TxtProductos.getText().trim().length() > 0) {
				String texto = TxtProductos.getText();
			    texto = texto.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o")
			                 .replace("ú", "u").replace("Á", "A") .replace("É", "E").replace("Í", "I")
			                 .replace("Ó", "O").replace("Ú", "U");
			    CadSQL += " WHERE " + "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(nombre, " +
			              "'á','a'),'é','e'),'í','i'),'ó','o'),'ú','u')" +" LIKE '%" +texto+ "%'";
			}
			
			ps = Conexion.prepareStatement(CadSQL);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Valores[0] = rs.getString("id_producto");
				Valores[1] = rs.getString("nombre");
				Valores[2] = rs.getString("precio_venta");
				Valores[3] = rs.getString("stock");
				modelo.addRow(Valores);
			}
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
		LblSubtotal.setText("Total: $" + total);
	}
}