import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

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
import javax.swing.SwingConstants;

public class MenuHistventas extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable TablaHistorial;
    
    DefaultTableModel modelo = new DefaultTableModel(); 
    
    // Variables de acceso a datos
    Connection Conexion = null;
    Statement SentenciaSQL = null;
    ResultSet Rs = null;
    private JTextField txtBuscar;
    private JLabel LblCantidadVentas;
        
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	MenuHistventas frame = new MenuHistventas();
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
    public MenuHistventas() {
        setTitle("Historial de Ventas");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\Downloads\\sales_finance_commerce_shopping_bars_chart_line_business_icon_255559.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 819, 425);
        setLocationRelativeTo(null); // Centra la ventana
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        // Columnas de la tabla principal
        modelo.addColumn("ID");
        modelo.addColumn("COMPRADOR");
        modelo.addColumn("FECHA");
        modelo.addColumn("TOTAL");
        
        // Tabla
        TablaHistorial = new JTable(modelo);
        TablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        TablaHistorial.getTableHeader().setBackground(new Color(0, 91, 159)); 
        TablaHistorial.getTableHeader().setForeground(Color.WHITE);
        
        TablaHistorial.setGridColor(new Color(102, 167, 215));
        TablaHistorial.setSelectionForeground(new Color(255, 255, 255));
        TablaHistorial.setSelectionBackground(new Color(0, 64, 128));
        TablaHistorial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        TablaHistorial.setRowHeight(25);
        scrollPane.setViewportView(TablaHistorial);
        
        // Personalizar tabla
        TablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TablaHistorial.setRowSelectionAllowed(true);
        TablaHistorial.setFillsViewportHeight(true);
        
        // PANEL SUPERIOR --------------------------------------------------------------------------
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 64, 128));
        contentPane.add(panel, BorderLayout.NORTH);
        
        JLabel lblTitulo = new JLabel("Historial de Ventas");
        lblTitulo.setForeground(new Color(255, 255, 255));
        lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 30));
        panel.add(lblTitulo);
        
        txtBuscar = new JTextField();
        txtBuscar.setColumns(10);
        panel.add(txtBuscar);
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String termino = txtBuscar.getText().trim();
                
                // Si la caja está vacía, recargamos la tabla
                if (termino.isEmpty()) {
                    Mostrar_Informacion(); 
                    return;
                }
                
                modelo.setRowCount(0);
                
                try {
                    Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
                    
                    // Usamos un JOIN para traer el nombre del cliente desde su tabla
                    String sql = "SELECT v.id_venta, c.nombre_completo, v.fecha, v.total " +
                                 "FROM ventas v " +
                                 "LEFT JOIN clientes c ON v.id_cliente = c.id_cliente " +
                                 "WHERE c.nombre_completo LIKE ?";
                    
                    java.sql.PreparedStatement pstmt = Conexion.prepareStatement(sql);
                    pstmt.setString(1, "%" + termino + "%");
                    ResultSet rs = pstmt.executeQuery();
                    
                    boolean hayResultados = false;
                    
                    while (rs.next()) {
                        String[] Valores = new String[4]; 
                        Valores[0] = rs.getString("id_venta");
                        
                        // Si el cliente es null  ponemos Público General
                        Valores[1] = rs.getString("nombre_completo");
                        if(Valores[1] == null) Valores[1] = "Público General"; 
                        
                        Valores[2] = rs.getString("fecha");
                        Valores[3] = "$" + rs.getString("total");

                        modelo.addRow(Valores);
                        hayResultados = true;
                    }
                    
                    if (hayResultados == false) {
                        JOptionPane.showMessageDialog(null, "No se encontraron ventas para ese cliente.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
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
        
        // PANEL INFERIOR --------------------------------------------------------------------------
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(0, 64, 128));
        contentPane.add(panel_1, BorderLayout.SOUTH);
        
        // --- BOTON VER DESGLOSE ---
        JButton btnDesglose = new JButton("Ver Desglose");
        btnDesglose.setToolTipText("Muestra exactamente qué artículos se vendieron en este ticket.");
        btnDesglose.setForeground(new Color(0, 128, 192));
        btnDesglose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDesglose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int RegistroSeleccionado = TablaHistorial.getSelectedRow();
                String idVenta, comprador;
                
                if (RegistroSeleccionado >= 0) {
                    idVenta = TablaHistorial.getValueAt(RegistroSeleccionado, 0).toString();
                    comprador = TablaHistorial.getValueAt(RegistroSeleccionado, 1).toString();
                    
                    // Llamamos al método que abre la ventanita
                    abrirVentanaDesglose(idVenta, comprador);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla para poder ver su desglose.", "ACCIÓN FALTANTE", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel_1.add(btnDesglose);
        
        // --- BOTON SALIR ---
        JButton BtnSalir = new JButton("Volver al menú principal");
        BtnSalir.setToolTipText("Con este botón podrá volver al menú principal");
        BtnSalir.setForeground(new Color(0, 128, 192));
        BtnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        BtnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuPrincipal menuprincipal = new MenuPrincipal();
                menuprincipal.setVisible(true);
                dispose(); // Cerrar ventana
            }
        });
        panel_1.add(BtnSalir);

        // Etiqueta de cantidad de ventas
        LblCantidadVentas = new JLabel("Cantidad de ventas: ");
        LblCantidadVentas.setFont(new Font("Tahoma", Font.BOLD, 12));
        LblCantidadVentas.setForeground(Color.WHITE);
        panel_1.add(LblCantidadVentas);
        
        // Programamos la tecla ESC para salir
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
        
        String[] Valores = new String[4];
        
        try {
            Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
            SentenciaSQL = Conexion.createStatement();
            
            // JOIN para cargar toda la tabla
            String sql = "SELECT v.id_venta, c.nombre_completo, v.fecha, v.total " +
                         "FROM ventas v " +
                         "LEFT JOIN clientes c ON v.id_cliente = c.id_cliente";
            
            Rs = SentenciaSQL.executeQuery(sql);
            
            while (Rs.next()) {
                Valores[0] = Rs.getString("id_venta");
                
                Valores[1] = Rs.getString("nombre_completo");
                if(Valores[1] == null) Valores[1] = "Público General";
                
                Valores[2] = Rs.getString("fecha");
                Valores[3] = "$" + Rs.getString("total");
                
                modelo.addRow(Valores);
            }
            
            Conexion.close();
            
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al querer cargar los datos: " + e1.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        // Propiedades para no mostrar el id en la tabla
        TablaHistorial.getColumnModel().getColumn(0).setMinWidth(0);
        TablaHistorial.getColumnModel().getColumn(0).setMaxWidth(0);
        TablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        LblCantidadVentas.setText("Cantidad de ventas registradas: " + modelo.getRowCount()); 
    }
    
    // --- VENTANA EMERGENTE MAESTRO-DETALLE ---
    private void abrirVentanaDesglose(String idVenta, String comprador) {
        JDialog dialogo = new JDialog(this, "Desglose de Ticket", true);
        dialogo.setSize(450, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setLayout(new BorderLayout(0, 0));
        
        // Panel superior del diálogo respetando tu diseño azul
        JPanel panelSupDialogo = new JPanel();
        panelSupDialogo.setBackground(new Color(0, 64, 128));
        dialogo.getContentPane().add(panelSupDialogo, BorderLayout.NORTH);
        
        JLabel lblInfo = new JLabel("Cliente: " + comprador);
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Century Gothic", Font.BOLD, 16));
        panelSupDialogo.add(lblInfo);
        
        DefaultTableModel modeloDesglose = new DefaultTableModel();
        modeloDesglose.addColumn("Producto");
        modeloDesglose.addColumn("Cant.");
        modeloDesglose.addColumn("Precio U.");
        modeloDesglose.addColumn("Subtotal");
        
        JTable tablaDesglose = new JTable(modeloDesglose);
        tablaDesglose.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaDesglose.getTableHeader().setBackground(new Color(0, 91, 159)); 
        tablaDesglose.getTableHeader().setForeground(Color.WHITE);
        tablaDesglose.setGridColor(new Color(102, 167, 215));
        tablaDesglose.setSelectionForeground(new Color(255, 255, 255));
        tablaDesglose.setSelectionBackground(new Color(0, 64, 128));
        tablaDesglose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaDesglose.setRowHeight(25);
        
        // Ajustamos las columnas para que quepan bien
        tablaDesglose.getColumnModel().getColumn(1).setPreferredWidth(40);
        
        JScrollPane scrollDialogo = new JScrollPane(tablaDesglose);
        dialogo.getContentPane().add(scrollDialogo, BorderLayout.CENTER);
        
        // Consulta a la BD con JOIN para conectar detalle_ventas con Productos
        try {
            Connection conDialogo = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
            
            String sqlDetalle = "SELECT p.nombre, dv.cantidad, dv.precio_unitario, dv.subtotal " +
                                "FROM detalle_ventas dv " +
                                "INNER JOIN Productos p ON dv.id_producto = p.id_producto " +
                                "WHERE dv.id_venta = ?";
            
            java.sql.PreparedStatement ps = conDialogo.prepareStatement(sqlDetalle);
            ps.setInt(1, Integer.parseInt(idVenta));
            ResultSet rsDetalle = ps.executeQuery();
            
            while(rsDetalle.next()) {
                Object[] fila = new Object[4];
                fila[0] = rsDetalle.getString("nombre");
                fila[1] = rsDetalle.getString("cantidad");
                fila[2] = "$" + rsDetalle.getString("precio_unitario");
                fila[3] = "$" + rsDetalle.getString("subtotal");
                modeloDesglose.addRow(fila);
            }
            
            rsDetalle.close();
            ps.close();
            conDialogo.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialogo, "Error al cargar el desglose: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        dialogo.setVisible(true);
    }
}