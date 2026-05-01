import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuPrincipal frame = new MenuPrincipal();
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
	public MenuPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 690, 425);
		setLocationRelativeTo(null); //Centra la ventana
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Menú Principal");
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblNewLabel.setBounds(234, 23, 224, 72);
		contentPane.add(lblNewLabel);
		
		JButton BtnProductos = new JButton("Productos");
		BtnProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana productos
				MenuProductos menuprod = new MenuProductos();
				menuprod.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnProductos.setFont(new Font("Century Gothic", Font.BOLD, 20));
		BtnProductos.setBounds(61, 142, 144, 51);
		contentPane.add(BtnProductos);
		
		JButton BtnVentas = new JButton("Ventas");
		BtnVentas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana Ventas
				MenuVentas menuvent = new MenuVentas();
				menuvent.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnVentas.setFont(new Font("Century Gothic", Font.BOLD, 20));
		BtnVentas.setBounds(274, 142, 144, 51);
		contentPane.add(BtnVentas);
		
		JButton BtnBusqueda = new JButton("Búsqueda");
		BtnBusqueda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana búsqueda
				MenuBusqueda menubusq = new MenuBusqueda();
				menubusq.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnBusqueda.setFont(new Font("Century Gothic", Font.BOLD, 20));
		BtnBusqueda.setBounds(490, 142, 144, 51);
		contentPane.add(BtnBusqueda);
		
		JButton BtnClientes = new JButton("Clientes");
		BtnClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana clientes
				MenuClientes menuclien = new MenuClientes();
				menuclien.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnClientes.setFont(new Font("Century Gothic", Font.BOLD, 20));
		BtnClientes.setBounds(116, 260, 144, 51);
		contentPane.add(BtnClientes);
		
		JButton BtnCatalogoUsuarios = new JButton("Cátalogo de Usuarios");
		BtnCatalogoUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana cátalogo de usuarios
				MenuCatalogoUsuarios menucatalogo = new MenuCatalogoUsuarios();
				menucatalogo.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnCatalogoUsuarios.setFont(new Font("Century Gothic", Font.BOLD, 20));
		BtnCatalogoUsuarios.setBounds(329, 260, 247, 51);
		contentPane.add(BtnCatalogoUsuarios);

	}
}
