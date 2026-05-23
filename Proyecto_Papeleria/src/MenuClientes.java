import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

public class MenuClientes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField TxtNombre;
	private JTextField TxtDireccion;
	private JTextField TxtTelefono;
	private JTextField TxtCorreo;
	private JTextField TxtFechaRegistro;
	private JTextField TxtBuscar;

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
		setBounds(100, 100, 772, 433);
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
		BtnSalir.setBounds(579, 366, 170, 20);
		contentPane.add(BtnSalir);
		
		JLabel lblNewLabel = new JLabel("Dirección:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setBounds(29, 127, 81, 20);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(313, 67, 436, 214);
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
		
		TxtTelefono = new JTextField();
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
		
		JRadioButton RdbMinorista = new JRadioButton("Minorista");
		RdbMinorista.setFont(new Font("Tahoma", Font.PLAIN, 12));
		RdbMinorista.setBounds(29, 333, 102, 20);
		contentPane.add(RdbMinorista);
		
		JLabel lblTipoDeComprador = new JLabel("Tipo de comprador:");
		lblTipoDeComprador.setFont(new Font("Arial", Font.BOLD, 14));
		lblTipoDeComprador.setBounds(29, 307, 151, 20);
		contentPane.add(lblTipoDeComprador);
		
		JRadioButton RdbMayorista = new JRadioButton("Mayorista");
		RdbMayorista.setFont(new Font("Tahoma", Font.PLAIN, 12));
		RdbMayorista.setBounds(143, 333, 102, 20);
		contentPane.add(RdbMayorista);
		
		JLabel lblFechaDeRegistro = new JLabel("Fecha de Registro:");
		lblFechaDeRegistro.setFont(new Font("Arial", Font.BOLD, 14));
		lblFechaDeRegistro.setBounds(29, 255, 151, 20);
		contentPane.add(lblFechaDeRegistro);
		
		TxtFechaRegistro = new JTextField();
		TxtFechaRegistro.setColumns(10);
		TxtFechaRegistro.setBounds(174, 257, 90, 18);
		contentPane.add(TxtFechaRegistro);
		
		JLabel lblNewLabel_1_1 = new JLabel("Buscar:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(183, 26, 81, 20);
		contentPane.add(lblNewLabel_1_1);
		
		TxtBuscar = new JTextField();
		TxtBuscar.setBounds(245, 28, 404, 18);
		contentPane.add(TxtBuscar);
		TxtBuscar.setColumns(10);
		
		JButton BtnBuscar = new JButton("Buscar");
		BtnBuscar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnBuscar.setBounds(659, 26, 90, 20);
		contentPane.add(BtnBuscar);
		
		JButton BtnAgregar = new JButton("Agregar");
		BtnAgregar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnAgregar.setBounds(313, 308, 90, 31);
		contentPane.add(BtnAgregar);
		
		JButton BtnModificar = new JButton("Modificar");
		BtnModificar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnModificar.setBounds(413, 308, 102, 31);
		contentPane.add(BtnModificar);
		
		JButton BtnEliminar = new JButton("Eliminar");
		BtnEliminar.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnEliminar.setBounds(525, 308, 90, 31);
		contentPane.add(BtnEliminar);
		
		JButton BtnEliminarTodo = new JButton("Eliminar todo");
		BtnEliminarTodo.setFont(new Font("Tahoma", Font.BOLD, 12));
		BtnEliminarTodo.setBounds(625, 308, 124, 31);
		contentPane.add(BtnEliminarTodo);
		
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
}
