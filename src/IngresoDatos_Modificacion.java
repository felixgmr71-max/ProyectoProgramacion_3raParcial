import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;

//

public class IngresoDatos_Modificacion extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JTextField txtPrecioCompra;
	private JTextField txtPrecioVenta;
	private JTextField txtStock;
	
	//-------------------------------------------
	public boolean Actualizar = false; // Nuestra bandera
	public String idProductoActualizar = ""; // Para saber qué id vamos a modificar
	public JLabel lblTitulo; // Para poder cambiar el texto del título
	
	
	Connection Conexion = null;
	Statement SentenciaSQL = null;
	ResultSet Rs = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			IngresoDatos_Modificacion dialog = new IngresoDatos_Modificacion();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public IngresoDatos_Modificacion() {
		setBounds(100, 100, 370, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			txtCodigo = new JTextField();
			txtCodigo.setToolTipText("En este campo podra ingresar el codigo de su producto");
			txtCodigo.setBounds(104, 78, 203, 19);
			contentPanel.add(txtCodigo);
			txtCodigo.setColumns(10);
		}
		
		JLabel LblCodigo = new JLabel("Código:");
		LblCodigo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		LblCodigo.setBounds(49, 80, 52, 17);
		contentPanel.add(LblCodigo);
		
		JLabel LblNombre = new JLabel("Nombre:");
		LblNombre.setFont(new Font("Tahoma", Font.PLAIN, 13));
		LblNombre.setBounds(49, 107, 52, 17);
		contentPanel.add(LblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setToolTipText("En este campo podra ingresar el nombre de su producto");
		txtNombre.setColumns(10);
		txtNombre.setBounds(114, 107, 193, 19);
		contentPanel.add(txtNombre);
		
		txtPrecioCompra = new JTextField();
		txtPrecioCompra.addKeyListener(new KeyAdapter() {
			
			@Override//Esto nos ayuda a indicarle que el método que estás escribiendo abajo está sobrescribiendo un método que ya existe en una clase padre (herencia) o en una interfaz.

			public void keyTyped(KeyEvent e) {
				
				// Obtiene el caracter que se acaba de ingresar
			    char c = e.getKeyChar();

			    //Si el caracter es un número o un punto
			    if (!Character.isDigit(c) && c != '.') {
			    	
			        e.consume(); //El caracter no se escribe
			    }
			    
			    //Si es un punto, revisamos si el texto ya tiene un punto antes
				if (c == '.' && txtPrecioCompra.getText().contains(".")) {
					
					e.consume(); // Si ya existe un punto, bloquea el segundo
				}
				
			}
		});
		txtPrecioCompra.setToolTipText("En este campo podra ingresar el precio de compra de su producto");
		txtPrecioCompra.setColumns(10);
		txtPrecioCompra.setBounds(170, 138, 137, 19);
		contentPanel.add(txtPrecioCompra);
		
		JLabel lblPrecio = new JLabel(" Precio de compra:");
		lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPrecio.setBounds(49, 138, 115, 17);
		contentPanel.add(lblPrecio);
		
		txtPrecioVenta = new JTextField();
		txtPrecioVenta.addKeyListener(new KeyAdapter() {
			
			@Override//Esto nos ayuda a indicarle que el método que estás escribiendo abajo está sobrescribiendo un método que ya existe en una clase padre (herencia) o en una interfaz.
			
			public void keyTyped(KeyEvent e) {
				
				// Obtiene el caracter que se acaba de ingresar
			    char c = e.getKeyChar();

			    // Si el caracter es un número o un punto
			    if (!Character.isDigit(c) && c != '.') {
					e.consume(); 
				}
			    
			  //Si es un punto, revisamos si el texto ya tiene un punto antes
				if (c == '.' && txtPrecioCompra.getText().contains(".")) {
					
					e.consume(); // Si ya existe un punto, bloquea el segundo
				}
			}
		});
		txtPrecioVenta.setToolTipText("En este campo podra ingresar el precio de venta de su producto");
		txtPrecioVenta.setColumns(10);
		txtPrecioVenta.setBounds(151, 170, 156, 19);
		contentPanel.add(txtPrecioVenta);
		
		txtStock = new JTextField();
		txtStock.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				// Obtiene el caracter que se acaba de ingresar
			    char c = e.getKeyChar();

			    // Si el caracter es un número 
			    if (!Character.isDigit(c)) {
			        e.consume(); //El caracter no se escribe
			    }
				
			}
		});
		txtStock.setToolTipText("En este campo podra ingresar la cantidad de unidades que tienen de este producto");
		txtStock.setColumns(10);
		txtStock.setBounds(94, 199, 213, 19);
		contentPanel.add(txtStock);
		
		JLabel lblStock = new JLabel("Stock:");
		lblStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblStock.setBounds(49, 199, 52, 17);
		contentPanel.add(lblStock);
		
		JLabel lblPrecioDeVenta = new JLabel("Precio de venta:");
		lblPrecioDeVenta.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPrecioDeVenta.setBounds(49, 170, 115, 17);
		contentPanel.add(lblPrecioDeVenta);
		
		
		lblTitulo = new JLabel("Agregar Producto");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblTitulo.setBounds(10, 10, 336, 38);
		contentPanel.add(lblTitulo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(213, 224, 253));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("GUARDAR");
				okButton.setToolTipText("Guardar los datos ingresados");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
				        // Verificamos si alguno de los campos está vacío
				        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() || 
				            txtPrecioCompra.getText().trim().isEmpty() ||  txtPrecioVenta.getText().trim().isEmpty() || 
				            txtStock.getText().trim().isEmpty()) {
				        	
				        	// .trim: elimina los espacios en blanco al principio y al final del texto
				        	//.isEmpty: Verifica si la cadena de texto resultante no tiene ningún carácter
				            
				            // Mostramos un mensaje de advertencia al usuario
				            JOptionPane.showMessageDialog(null, 
				                "Por favor, llena todos los campos antes de continuar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
				                
				            return; //Detiene la ejecución del método aquí
				        }
						
						try {

				            String codigo = txtCodigo.getText();
				            String nombre = txtNombre.getText();
				            Double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
				            Double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
				            int stock = Integer.parseInt(txtStock.getText());
				            String CadSQL;
				            

				            Conexion = DriverManager.getConnection("jdbc:sqlite:papeleria.db");
				            
				            if(Actualizar == false){
				            	// La sentencia SQL usando ? en los VALUES
					            CadSQL = "INSERT INTO Productos(codigo, nombre, precio_compra, precio_venta, stock) VALUES (?, ?, ?, ?, ?)";
					            
				            }else {
				            	
				            	//Llenamos la sentencia usando ? para que despues coincidan con los datos de cada campo, el where es para actualizar el registro donde coincida la casilla
				            	CadSQL = "UPDATE Productos SET codigo = ?, nombre = ?, precio_compra = ?, precio_venta = ?, stock = ? WHERE id_producto = ?";
				            }
				            				            
				            // Preparamos la sentencia
				            java.sql.PreparedStatement pstmt = Conexion.prepareStatement(CadSQL);
				            
				            //Llenamos las casillas (?) en orden estricto del 1 al 5 con los datos
				            pstmt.setString(1, codigo);       // Esta linea reemplaza el primer ?
				            pstmt.setString(2, nombre);       // Esta linea reemplaza el segundo ?
				            pstmt.setDouble(3, precioCompra); // Esta linea reemplaza el tercer ? (y lo manda como número real)
				            pstmt.setDouble(4, precioVenta);  // Esta linea reemplaza el cuarto ?  (y lo manda como número real)
				            pstmt.setInt(5, stock);           // Esta linea reemplaza el quinto ?  (y lo manda como entero)
				            
				            
				            // Si es actualización, hay un sexto signo de interrogación que llenar el id
				            if (Actualizar == true) {
				            	
				                pstmt.setInt(6, Integer.parseInt(idProductoActualizar));
				            }
				            
				            //Se ejecuta la orden en la base de datos
				            int Registros = pstmt.executeUpdate();
				            
				            
				            if(Registros >= 1) {
				                JOptionPane.showMessageDialog(null, "¡El producto registrado!", "ACCIÓN COMPLETADA", JOptionPane.INFORMATION_MESSAGE);
				                
				                // Cerramos flujos y la ventana
				                pstmt.close();
				                Conexion.close();
				                dispose(); 
				                
				            } else {
				                JOptionPane.showMessageDialog(null, "No se pudo guardar, intentalo de nuevo.", "ERROR", JOptionPane.ERROR_MESSAGE);
				            }
				            
				        } catch (NumberFormatException ex) {
				        	
				            // Este catch saltará automáticamente si el usuario mete letras en los campos numéricos
				            JOptionPane.showMessageDialog(null, "Error, asegúrate de ingresar todos los datos solicitados.", "ERROR", JOptionPane.ERROR_MESSAGE);
				            
				        } catch (SQLException e2) {
				        	
				            JOptionPane.showMessageDialog(null, "Ocurrio un error al querer conectar con la base de datos: " + e2.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				        }
						
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("CANCELAR");
				cancelButton.setToolTipText("Cancelar registro/actualización");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						
						dispose(); //Cerrar la ventana
					
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
			}
			
		}
		
	}
	
	public void cargarDatosParaActualizar(String id, String codigo, String nombre, String precioCompra, String precioVenta, String stock) {
	    this.Actualizar = true; // Encendemos la "bandera"
	    this.idProductoActualizar = id; // Guardamos el id

	    // Llenamos las casillas
	    txtCodigo.setText(codigo);
	    txtCodigo.setEnabled(false);
	    
	    txtNombre.setText(nombre);
	    txtPrecioCompra.setText(precioCompra);
	    txtPrecioVenta.setText(precioVenta);
	    txtStock.setText(stock);

	    // Cambiamos el título
	    lblTitulo.setText("Actualizar"); 
	}
}
