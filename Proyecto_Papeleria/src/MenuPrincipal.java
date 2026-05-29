import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.SwingConstants;
import java.awt.Toolkit;

public class MenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String rolActual;
	
	// globalizamos botones
	private JButton BtnProveedores;
	private JButton BtnCatalogoUsuarios;
	private JButton BtnProductos;
	private JButton BtnVentas;
	private JButton BtnClientes;
	
	public static String rolGlobal = "1";
	private JButton btnHistorial;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuPrincipal frame = new MenuPrincipal("Admin");
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
		this(rolGlobal); // Esto llama al constructor de abajo usando el rol guardado
	}
	
	public MenuPrincipal(String rol) {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\Downloads\\1486564398-menu2_81519.png"));

		rolGlobal = rol;
		this.rolActual = rol; // Guardamos el rol que nos mandó el login
		setTitle("Menú Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 690, 470);
		setLocationRelativeTo(null); //Centra la ventana
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0)); 
		
		JPanel panelHeader = new JPanel();
		panelHeader.setBackground(new Color(0, 64, 128)); 
		panelHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(panelHeader, BorderLayout.NORTH);
		
		JLabel lblTituloMenu = new JLabel("Menú Principal");
		lblTituloMenu.setForeground(Color.WHITE);
		lblTituloMenu.setFont(new Font("Century Gothic", Font.BOLD, 28));
		lblTituloMenu.setHorizontalAlignment(SwingConstants.CENTER);
		panelHeader.add(lblTituloMenu);
		
		// --- CONTENEDOR CENTRAL PARA LOS BOTONES ---
		JPanel panelCentralBotones = new JPanel();
		panelCentralBotones.setLayout(new GridLayout(2, 1, 0, 0));
		contentPane.add(panelCentralBotones, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panelCentralBotones.add(panel);
		panel.setLayout(new GridLayout(3, 1, 0, 0));
		
		BtnProductos = new JButton("Productos");
		BtnProductos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnProductos.setForeground(new Color(0, 128, 192));
		panel.add(BtnProductos);
		BtnProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuProductos menuprod = new MenuProductos();
				menuprod.setVisible(true);
				dispose();
			}
		});
		BtnProductos.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		BtnVentas = new JButton("Ventas");
		BtnVentas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnVentas.setForeground(new Color(0, 128, 192));
		panel.add(BtnVentas);
		BtnVentas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuVentas menuvent = new MenuVentas();
				menuvent.setVisible(true);
				dispose(); 
			}
		});
		BtnVentas.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		BtnProveedores = new JButton("Proveedores");
		BtnProveedores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnProveedores.setForeground(new Color(0, 128, 192));
		panel.add(BtnProveedores);
		BtnProveedores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuProveedores menubusq = new MenuProveedores();
				menubusq.setVisible(true);
				dispose();
			}
		});
		BtnProveedores.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		JPanel panel_1 = new JPanel();
		panelCentralBotones.add(panel_1);
		panel_1.setLayout(new GridLayout(3, 1, 0, 0));
		
		BtnClientes = new JButton("Clientes");
		BtnClientes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnClientes.setForeground(new Color(0, 128, 192));
		panel_1.add(BtnClientes);
		BtnClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuClientes menuclien = new MenuClientes();
				menuclien.setVisible(true);
				dispose(); 
			}
		});
		BtnClientes.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		BtnCatalogoUsuarios = new JButton("Cátalogo de Usuarios");
		BtnCatalogoUsuarios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnCatalogoUsuarios.setForeground(new Color(0, 128, 192));
		panel_1.add(BtnCatalogoUsuarios);
		BtnCatalogoUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuCatalogoUsuarios menucatalogo = new MenuCatalogoUsuarios();
				menucatalogo.setVisible(true);
				dispose(); 
			}
		});
		BtnCatalogoUsuarios.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		btnHistorial = new JButton("Historial");
		btnHistorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuHistventas menuhist = new MenuHistventas();
				menuhist.setVisible(true);
				dispose(); 
			}
		});
		btnHistorial.setForeground(new Color(0, 128, 192));
		btnHistorial.setFont(new Font("Century Gothic", Font.BOLD, 20));
		panel_1.add(btnHistorial);
		
		aplicarPermisos();
	}
	
	private void aplicarPermisos() {		
		if (rolActual != null) {
			// Limpiamos espacios basura
			String rolLimpio = rolActual.trim();
			
			if (rolLimpio.equals("2")) {
				BtnCatalogoUsuarios.setEnabled(false);
				BtnProveedores.setEnabled(false);
				BtnProductos.setEnabled(false);
				btnHistorial.setEnabled(false);
			}
		}
	}
}