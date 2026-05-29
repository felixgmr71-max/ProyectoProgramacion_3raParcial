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
    
    DefaultTableModel modelo = new DefaultTableModel(); 
    
    Connection Conexion = null;
    Statement SentenciaSQL = null;
    ResultSet Rs = null;
    private JTextField txtBuscar;
    private JLabel LblCantidadProductos;
        
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

    public MenuProductos() {
        setTitle("Ventana de Productos");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\Downloads\\Product-documentation_35767.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 940, 425);
        setLocationRelativeTo(null); 
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        modelo.addColumn("ID");
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio de Compra");
        modelo.addColumn("Precio de Venta");
        modelo.addColumn("Stock");
        modelo.addColumn("Proveedor");
        
        TablaProductos = new JTable(modelo);
        TablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        TablaProductos.getTableHeader().setBackground(new Color(0, 91, 159)); 
        TablaProductos.getTableHeader().setForeground(Color.WHITE);
        
        // Ajustamos las columnas
        TablaProductos.getColumnModel().getColumn(1).setPreferredWidth(80);
        

        TablaProductos.getColumnModel().getColumn(2).setPreferredWidth(310);
        TablaProductos.getColumnModel().getColumn(2).setMaxWidth(325);

        TablaProductos.getColumnModel().getColumn(3).setPreferredWidth(130);

        TablaProductos.getColumnModel().getColumn(4).setPreferredWidth(130);

        TablaProductos.getColumnModel().getColumn(5).setPreferredWidth(60);
        
        // Configuración de columna PROVEEDOR
        TablaProductos.getColumnModel().getColumn(6).setPreferredWidth(140);
        
        TablaProductos.setGridColor(new Color(102, 167, 215));
        TablaProductos.setSelectionForeground(new Color(255, 255, 255));
        TablaProductos.setSelectionBackground(new Color(0, 64, 128));
        TablaProductos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        TablaProductos.setRowHeight(25);
        scrollPane.setViewportView(TablaProductos);
        
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
                
                if (termino.isEmpty()) {
                    Mostrar_Informacion(); 
                    return;
                }
                
                termino = termino.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                                 .replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
                
                modelo.setRowCount(0);
                
                try {
                    Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
                    
                    String sql = "SELECT p.id_producto, p.codigo, p.nombre, p.precio_compra, p.precio_venta, p.stock, pr.nombre_empresa AS proveedor " +
                            "FROM productos p " +
                            "LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                            "WHERE REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(p.nombre, " +
                            "'á','a'),'é','e'),'í','i'),'ó','o'),'ú','u')," +
                            "'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U') LIKE ?";
                            
                    java.sql.PreparedStatement pstmt = Conexion.prepareStatement(sql);
                    
                    // Pasamos el término ya limpio al PreparedStatement
                    pstmt.setString(1, "%" + termino + "%");
                    
                    ResultSet rs = pstmt.executeQuery();
                    boolean hayResultados = false;
                    
                    while (rs.next()) {
                        String[] Valores = new String[7]; 
                        Valores[0] = rs.getString("id_producto");
                        Valores[1] = rs.getString("codigo");
                        Valores[2] = rs.getString("nombre");
                        Valores[3] = rs.getString("precio_compra");
                        Valores[4] = rs.getString("precio_venta");
                        Valores[5] = rs.getString("stock");
                        String prov = rs.getString("proveedor");
                        Valores[6] = (prov != null) ? prov : "Sin proveedor";

                        modelo.addRow(Valores);
                        hayResultados = true;
                    }
                    
                    if (hayResultados == false) {
                        JOptionPane.showMessageDialog(null, "No se encontraron registros.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                        txtBuscar.setText(""); 
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
        
        // --- BOTON ELIMINAR ---
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setForeground(new Color(0, 128, 192));
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int RegistroSeleccionado = TablaProductos.getSelectedRow();
                if (RegistroSeleccionado >= 0) {
                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este producto?", "CONFIRMAR ELIMINACION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        String idProducto = TablaProductos.getValueAt(RegistroSeleccionado, 0).toString();
                        try {
                            Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
                            String ConsultaSQL = "DELETE FROM Productos WHERE id_Producto = ?";
                            java.sql.PreparedStatement pstmt = Conexion.prepareStatement(ConsultaSQL);
                            pstmt.setInt(1, Integer.parseInt(idProducto));
                            int filaAfectada = pstmt.executeUpdate();
                            if (filaAfectada > 0) {
                                JOptionPane.showMessageDialog(null, "¡Producto eliminado!", "PROCESO EXITOSO", JOptionPane.INFORMATION_MESSAGE);
                                Mostrar_Informacion(); 
                            }
                            pstmt.close();
                            Conexion.close();
                        } catch (SQLException e2) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e2.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un registro.", "ACCIÓN FALTANTE", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel_1.add(btnEliminar);
        
        // --- BOTON ACTUALIZAR ---
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setForeground(new Color(0, 128, 192));
        btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int RegistroSeleccionado = TablaProductos.getSelectedRow();
                String id, codigo, nombre, precioCompra, precioVenta, stock, proveedor;
                if (RegistroSeleccionado >= 0) {
                    id = TablaProductos.getValueAt(RegistroSeleccionado, 0).toString();
                    codigo = TablaProductos.getValueAt(RegistroSeleccionado, 1).toString();
                    nombre = TablaProductos.getValueAt(RegistroSeleccionado, 2).toString();
                    precioCompra = TablaProductos.getValueAt(RegistroSeleccionado, 3).toString();
                    precioVenta = TablaProductos.getValueAt(RegistroSeleccionado, 4).toString();
                    stock = TablaProductos.getValueAt(RegistroSeleccionado, 5).toString();
                    
                    // Se lee de la celda de la columna 6 el proveedor guardado (evita errores si viene nulo)
                    Object objProv = TablaProductos.getValueAt(RegistroSeleccionado, 6);
                    proveedor = (objProv != null) ? objProv.toString() : "";
                    
                    IngresoDatos_Modificacion VentanaDatos = new IngresoDatos_Modificacion();
                    // Enviamos el 7mo parámetro (proveedor) al JDialog
                    VentanaDatos.cargarDatosParaActualizar(id, codigo, nombre, precioCompra, precioVenta, stock, proveedor);
                    VentanaDatos.setModal(true);
                    VentanaDatos.setVisible(true);
                    Mostrar_Informacion();
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un registro para poder realizar esta acción.", "ACCIÓN FALTANTE", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel_1.add(btnActualizar);
        
        // --- BOTON AGREGAR ---
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
        
        // --- BOTON SALIR ---
        JButton BtnSalir = new JButton("Volver al menú principal");
        BtnSalir.setForeground(new Color(0, 128, 192));
        BtnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        BtnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuPrincipal menuprincipal = new MenuPrincipal();
                menuprincipal.setVisible(true);
                dispose(); 
            }
        });
        panel_1.add(BtnSalir);

        LblCantidadProductos = new JLabel("Cantidad de productos: ");
        LblCantidadProductos.setFont(new Font("Tahoma", Font.BOLD, 12));
        LblCantidadProductos.setForeground(Color.WHITE);
        panel_1.add(LblCantidadProductos);
        
        javax.swing.KeyStroke esc = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        this.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "accionVolver");
        this.getRootPane().getActionMap().put("accionVolver", new javax.swing.AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                BtnSalir.doClick(); 
            }
        });
        
        Mostrar_Informacion();
    }
    
    private void Mostrar_Informacion() {
        modelo.setRowCount(0); 
        String[] Valores = new String[7]; 
        
        try {
            Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
            SentenciaSQL = Conexion.createStatement();
            String sql = "SELECT p.id_producto, p.codigo, p.nombre, p.precio_compra, p.precio_venta, p.stock, pr.nombre_empresa AS proveedor " +
                    "FROM productos p " +
                    "LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor";

            Rs = SentenciaSQL.executeQuery(sql);
       
            while (Rs.next()) {
                Valores[0] = Rs.getString("id_producto");
                Valores[1] = Rs.getString("codigo");
                Valores[2] = Rs.getString("nombre");
                Valores[3] = Rs.getString("precio_compra");
                Valores[4] = Rs.getString("precio_venta");
                Valores[5] = Rs.getString("stock");
                String prov = Rs.getString("proveedor");
                Valores[6] = (prov != null) ? prov : "Sin proveedor";
                
                modelo.addRow(Valores);
            }
            Conexion.close();
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al cargar los datos: " + e1.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        TablaProductos.getColumnModel().getColumn(0).setMinWidth(0);
        TablaProductos.getColumnModel().getColumn(0).setMaxWidth(0);
        TablaProductos.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        LblCantidadProductos.setText("Cantidad de productos: " + modelo.getRowCount()); 
    }
}