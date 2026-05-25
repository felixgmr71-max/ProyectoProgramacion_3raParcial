import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Cursor;

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
		setTitle("Menú Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 690, 425);
		setLocationRelativeTo(null); //Centra la ventana
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(3, 1, 0, 0));
		
		JButton BtnProductos = new JButton("Productos");
		BtnProductos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnProductos.setForeground(new Color(0, 128, 192));
		panel.add(BtnProductos);
		BtnProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana productos
				MenuProductos menuprod = new MenuProductos();
				menuprod.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnProductos.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		JButton BtnVentas = new JButton("Ventas");
		BtnVentas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnVentas.setForeground(new Color(0, 128, 192));
		panel.add(BtnVentas);
		BtnVentas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana Ventas
				MenuVentas menuvent = new MenuVentas();
				menuvent.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnVentas.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		JButton BtnProveedores = new JButton("Proveedores");
		BtnProveedores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnProveedores.setForeground(new Color(0, 128, 192));
		panel.add(BtnProveedores);
		BtnProveedores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana búsqueda
				MenuProveedores menubusq = new MenuProveedores();
				menubusq.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnProveedores.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(2, 1, 0, 0));
		
		JButton BtnClientes = new JButton("Clientes");
		BtnClientes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnClientes.setForeground(new Color(0, 128, 192));
		panel_1.add(BtnClientes);
		BtnClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana clientes
				MenuClientes menuclien = new MenuClientes();
				menuclien.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnClientes.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		JButton BtnCatalogoUsuarios = new JButton("Cátalogo de Usuarios");
		BtnCatalogoUsuarios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnCatalogoUsuarios.setForeground(new Color(0, 128, 192));
		panel_1.add(BtnCatalogoUsuarios);
		BtnCatalogoUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nos lleva a ventana cátalogo de usuarios
				MenuCatalogoUsuarios menucatalogo = new MenuCatalogoUsuarios();
				menucatalogo.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnCatalogoUsuarios.setFont(new Font("Century Gothic", Font.BOLD, 20));

	}
}
