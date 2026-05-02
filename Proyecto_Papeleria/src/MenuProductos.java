import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuProductos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public MenuProductos() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 690, 425);
		setLocationRelativeTo(null); //Centra la ventana
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblProductos = new JLabel("Productos");
		lblProductos.setFont(new Font("Century Gothic", Font.BOLD, 30));
		lblProductos.setBounds(276, 10, 157, 72);
		contentPane.add(lblProductos);
		
		JButton BtnSalir = new JButton("Volver al menú principal");
		BtnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Volvemos al menú principal
				MenuPrincipal menuprincipal = new MenuPrincipal();
				menuprincipal.setVisible(true);
				
				dispose(); //Cerrar ventana
			}
		});
		BtnSalir.setBounds(496, 358, 170, 20);
		contentPane.add(BtnSalir);
		
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
