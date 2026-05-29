import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;

public class IngresoDatos_Modificacion extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecioCompra;
    private JTextField txtPrecioVenta;
    private JTextField txtStock;
    
    //ComboBox que guardará objetos ProveedorItem
    private JComboBox<ProveedorItem> cmbProveedor; 
    
    //-------------------------------------------
    public boolean Actualizar = false; 
    public String idProductoActualizar = ""; 
    public JLabel lblTitulo; 
    
    Connection Conexion = null;
    Statement SentenciaSQL = null;
    ResultSet Rs = null;
    
    public static void main(String[] args) {
        try {
            IngresoDatos_Modificacion dialog = new IngresoDatos_Modificacion();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IngresoDatos_Modificacion() {
        setTitle("Formulario de Producto");
        setBounds(100, 100, 410, 430); 
        setLocationRelativeTo(null); 
        
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE); 
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        
        Font fuenteLabels = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteInputs = new Font("Segoe UI", Font.PLAIN, 13);
        Color colorAzulMenu = new Color(0, 64, 128);
        
        lblTitulo = new JLabel("Agregar Producto");
        lblTitulo.setForeground(colorAzulMenu);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 26));
        lblTitulo.setBounds(10, 15, 376, 38);
        contentPanel.add(lblTitulo);
        
        // --- CÓDIGO ---
        JLabel LblCodigo = new JLabel("Código:");
        LblCodigo.setFont(fuenteLabels);
        LblCodigo.setForeground(colorAzulMenu);
        LblCodigo.setBounds(35, 80, 120, 25);
        contentPanel.add(LblCodigo);
        
        txtCodigo = new JTextField();
        txtCodigo.setFont(fuenteInputs);
        txtCodigo.setToolTipText("En este campo podrá ingresar el código de su producto");
        txtCodigo.setBounds(165, 80, 195, 25); 
        contentPanel.add(txtCodigo);
        txtCodigo.setColumns(10);
        
        // --- NOMBRE ---
        JLabel LblNombre = new JLabel("Nombre / Desc.:");
        LblNombre.setFont(fuenteLabels);
        LblNombre.setForeground(colorAzulMenu);
        LblNombre.setBounds(35, 120, 120, 25);
        contentPanel.add(LblNombre);
        
        txtNombre = new JTextField();
        txtNombre.setFont(fuenteInputs);
        txtNombre.setToolTipText("En este campo podrá ingresar el nombre de su producto");
        txtNombre.setColumns(10);
        txtNombre.setBounds(165, 120, 195, 25);
        contentPanel.add(txtNombre);
        
        // --- PRECIO COMPRA ---
        JLabel lblPrecio = new JLabel("Precio de compra:");
        lblPrecio.setFont(fuenteLabels);
        lblPrecio.setForeground(colorAzulMenu);
        lblPrecio.setBounds(35, 160, 120, 25);
        contentPanel.add(lblPrecio);
        
        txtPrecioCompra = new JTextField();
        txtPrecioCompra.setFont(fuenteInputs);
        txtPrecioCompra.addKeyListener(new KeyAdapter() {
            @Override 
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume(); 
                }
                if (c == '.' && txtPrecioCompra.getText().contains(".")) {
                    e.consume(); 
                }
            }
        });
        txtPrecioCompra.setToolTipText("En este campo podrá ingresar el precio de compra de su producto");
        txtPrecioCompra.setColumns(10);
        txtPrecioCompra.setBounds(165, 160, 195, 25);
        contentPanel.add(txtPrecioCompra);
        
        // --- PRECIO VENTA ---
        JLabel lblPrecioDeVenta = new JLabel("Precio de venta:");
        lblPrecioDeVenta.setFont(fuenteLabels);
        lblPrecioDeVenta.setForeground(colorAzulMenu);
        lblPrecioDeVenta.setBounds(35, 200, 120, 25);
        contentPanel.add(lblPrecioDeVenta);
        
        txtPrecioVenta = new JTextField();
        txtPrecioVenta.setFont(fuenteInputs);
        txtPrecioVenta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume(); 
                }
                if (c == '.' && txtPrecioVenta.getText().contains(".")) {
                    e.consume(); 
                }
            }
        });
        txtPrecioVenta.setToolTipText("En este campo podrá ingresar el precio de venta de su producto");
        txtPrecioVenta.setColumns(10);
        txtPrecioVenta.setBounds(165, 200, 195, 25);
        contentPanel.add(txtPrecioVenta);
        
        // --- STOCK ---
        JLabel lblStock = new JLabel("Stock disponible:");
        lblStock.setFont(fuenteLabels);
        lblStock.setForeground(colorAzulMenu);
        lblStock.setBounds(35, 240, 120, 25);
        contentPanel.add(lblStock);
        
        txtStock = new JTextField();
        txtStock.setFont(fuenteInputs);
        txtStock.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); 
                }
            }
        });
        txtStock.setToolTipText("En este campo podrá ingresar la cantidad de unidades que tienen de este producto");
        txtStock.setColumns(10);
        txtStock.setBounds(165, 240, 195, 25);
        contentPanel.add(txtStock);
        
        // --- PROVEEDOR (COMBOBOX) ---
        JLabel lblProveedor = new JLabel("Proveedor:");
        lblProveedor.setFont(fuenteLabels);
        lblProveedor.setForeground(colorAzulMenu);
        lblProveedor.setBounds(35, 280, 120, 25);
        contentPanel.add(lblProveedor);
        
        cmbProveedor = new JComboBox<ProveedorItem>();
        cmbProveedor.setFont(fuenteInputs);
        cmbProveedor.setToolTipText("Seleccione la empresa que provee este producto");
        cmbProveedor.setBounds(165, 280, 195, 25);
        contentPanel.add(cmbProveedor);
        
        // Llamamos al método para llenar el ComboBox desde la BD
        cargarProveedoresEnCombo();
        
        // --- PANEL DE BOTONES ---
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setBackground(Color.WHITE); 
            buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10)); 
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("GUARDAR");
                okButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
                okButton.setBackground(new Color(0, 91, 159)); 
                okButton.setForeground(Color.WHITE);
                okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                okButton.setPreferredSize(new Dimension(130, 32)); 
                
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        
                        ProveedorItem provSeleccionado = (ProveedorItem) cmbProveedor.getSelectedItem();
                        
                        // Validamos que todo esté lleno y que se haya seleccionado un proveedor válido
                        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() || 
                            txtPrecioCompra.getText().trim().isEmpty() ||  txtPrecioVenta.getText().trim().isEmpty() || 
                            txtStock.getText().trim().isEmpty() || provSeleccionado == null || provSeleccionado.getId() == 0) {
                            
                            JOptionPane.showMessageDialog(null, 
                                "Por favor, llena todos los campos y selecciona un proveedor válido.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                            return; 
                        }
                        
                        try {
                            String codigo = txtCodigo.getText();
                            String nombre = txtNombre.getText();
                            Double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
                            Double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
                            int stock = Integer.parseInt(txtStock.getText());
                            int idProveedor = provSeleccionado.getId(); // Extraemos el ID numérico
                            
                            String CadSQL;

                            Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
                            
                            // Guardamos en la base de datos usando el campo id_proveedor
                            if(Actualizar == false){
                                CadSQL = "INSERT INTO productos(codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES (?, ?, ?, ?, ?, ?)";
                            }else {
                                CadSQL = "UPDATE productos SET codigo = ?, nombre = ?, precio_compra = ?, precio_venta = ?, stock = ?, id_proveedor = ? WHERE id_producto = ?";
                            }
                                                                        
                            java.sql.PreparedStatement pstmt = Conexion.prepareStatement(CadSQL);
                            
                            pstmt.setString(1, codigo);       
                            pstmt.setString(2, nombre);       
                            pstmt.setDouble(3, precioCompra); 
                            pstmt.setDouble(4, precioVenta);  
                            pstmt.setInt(5, stock);
                            pstmt.setInt(6, idProveedor); // Mandamos el ID del proveedor 
                            
                            if (Actualizar == true) {
                                pstmt.setInt(7, Integer.parseInt(idProductoActualizar)); 
                            }
                            
                            int Registros = pstmt.executeUpdate();
                            
                            if(Registros >= 1) {
                                JOptionPane.showMessageDialog(null, "¡El producto ha sido guardado exitosamente!", "ACCIÓN COMPLETADA", JOptionPane.INFORMATION_MESSAGE);
                                pstmt.close();
                                Conexion.close();
                                dispose(); 
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo guardar, inténtalo de nuevo.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Error, asegúrate de ingresar datos numéricos válidos en precios y stock.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } catch (SQLException e2) {
                            JOptionPane.showMessageDialog(null, "Ocurrió un error al querer conectar con la base de datos: " + e2.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("CANCELAR");
                cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
                cancelButton.setBackground(new Color(180, 50, 50)); 
                cancelButton.setForeground(Color.WHITE);
                cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                cancelButton.setPreferredSize(new Dimension(130, 32));
                
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose(); 
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }
    
    // Método para rellenar el ComboBox con datos de SQLite
    private void cargarProveedoresEnCombo() {
        cmbProveedor.addItem(new ProveedorItem(0, "Seleccione...")); // Opción por defecto
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_proveedor, nombre_empresa FROM proveedores");
            
            while (rs.next()) {
                int id = rs.getInt("id_proveedor");
                String nombre = rs.getString("nombre_empresa");
                cmbProveedor.addItem(new ProveedorItem(id, nombre));
            }
            
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para cargar datos cuando le damos en "Actualizar" desde la tabla principal
    public void cargarDatosParaActualizar(String id, String codigo, String nombre, String precioCompra, String precioVenta, String stock, String proveedorNombre) {
        this.Actualizar = true; 
        this.idProductoActualizar = id; 

        txtCodigo.setText(codigo);
        txtCodigo.setEnabled(false);
        txtNombre.setText(nombre);
        txtPrecioCompra.setText(precioCompra);
        txtPrecioVenta.setText(precioVenta);
        txtStock.setText(stock);
        
        // Buscamos el proveedor en el ComboBox que coincida con el nombre que viene de la tabla
        for (int i = 0; i < cmbProveedor.getItemCount(); i++) {
            ProveedorItem item = cmbProveedor.getItemAt(i);
            if (item.getNombre().equals(proveedorNombre)) {
                cmbProveedor.setSelectedIndex(i);
                break;
            }
        }

        lblTitulo.setText("Actualizar Producto"); 
    }
    
    // Clase que se encarga de guardar ID y Mostrar el Nombre en el Combo
    class ProveedorItem {
        private int id;
        private String nombre;

        public ProveedorItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        // Este método es crucial, ya que es lo que el JComboBox imprime en pantalla
        @Override
        public String toString() {
            return nombre;
        }
    }
}