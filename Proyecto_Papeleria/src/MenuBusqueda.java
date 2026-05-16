import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuBusqueda extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabla;

	private JTextField txtEmpresa;
	private JTextField txtContacto;
	private JTextField txtTelefono;

	DefaultTableModel modelo = new DefaultTableModel();

	Connection conexion = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	int idProveedor = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuBusqueda frame = new MenuBusqueda();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuBusqueda() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblEmpresa = new JLabel("Empresa:");
		lblEmpresa.setBounds(29, 38, 80, 14);
		contentPane.add(lblEmpresa);

		txtEmpresa = new JTextField();
		txtEmpresa.setBounds(119, 35, 180, 20);
		contentPane.add(txtEmpresa);
		txtEmpresa.setColumns(10);

		JLabel lblContacto = new JLabel("Contacto:");
		lblContacto.setBounds(29, 82, 80, 14);
		contentPane.add(lblContacto);

		txtContacto = new JTextField();
		txtContacto.setBounds(119, 79, 180, 20);
		contentPane.add(txtContacto);
		txtContacto.setColumns(10);

		JLabel lblTelefono = new JLabel("Telefono:");
		lblTelefono.setBounds(29, 126, 80, 14);
		contentPane.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setBounds(119, 123, 180, 20);
		contentPane.add(txtTelefono);
		txtTelefono.setColumns(10);
		
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

		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					Conexion con = new Conexion();
					conexion = con.conectar();

					String sql = "INSERT INTO proveedores(nombre_empresa, contacto, telefono) VALUES(?,?,?)";

					ps = conexion.prepareStatement(sql);

					ps.setString(1, txtEmpresa.getText());
					ps.setString(2, txtContacto.getText());
					ps.setString(3, txtTelefono.getText());

					ps.executeUpdate();

					JOptionPane.showMessageDialog(null, "Proveedor agregado");

					limpiar();
					mostrarDatos();

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});

		btnAgregar.setBounds(29, 186, 110, 23);
		contentPane.add(btnAgregar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					Conexion con = new Conexion();
					conexion = con.conectar();

					String sql = "UPDATE proveedores SET nombre_empresa=?, contacto=?, telefono=? WHERE id_proveedor=?";

					ps = conexion.prepareStatement(sql);

					ps.setString(1, txtEmpresa.getText());
					ps.setString(2, txtContacto.getText());
					ps.setString(3, txtTelefono.getText());
					ps.setInt(4, idProveedor);

					ps.executeUpdate();

					JOptionPane.showMessageDialog(null, "Proveedor modificado");

					limpiar();
					mostrarDatos();

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});

		btnModificar.setBounds(160, 186, 110, 23);
		contentPane.add(btnModificar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					Conexion con = new Conexion();
					conexion = con.conectar();

					String sql = "DELETE FROM proveedores WHERE id_proveedor=?";

					ps = conexion.prepareStatement(sql);

					ps.setInt(1, idProveedor);

					ps.executeUpdate();

					JOptionPane.showMessageDialog(null, "Proveedor eliminado");

					limpiar();
					mostrarDatos();

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});

		btnEliminar.setBounds(95, 235, 110, 23);
		contentPane.add(btnEliminar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(330, 35, 350, 280);
		contentPane.add(scrollPane);

		modelo.addColumn("ID");
		modelo.addColumn("Empresa");
		modelo.addColumn("Contacto");
		modelo.addColumn("Telefono");

		tabla = new JTable(modelo);

		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int fila = tabla.getSelectedRow();

				idProveedor = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

				txtEmpresa.setText(tabla.getValueAt(fila, 1).toString());
				txtContacto.setText(tabla.getValueAt(fila, 2).toString());
				txtTelefono.setText(tabla.getValueAt(fila, 3).toString());
			}
		});

		scrollPane.setViewportView(tabla);

		mostrarDatos();
	}

	public void mostrarDatos() {

		modelo.setRowCount(0);

		try {

			Conexion con = new Conexion();
			conexion = con.conectar();

			String sql = "SELECT * FROM proveedores";

			ps = conexion.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {

				Object fila[] = new Object[4];

				fila[0] = rs.getInt("id_proveedor");
				fila[1] = rs.getString("nombre_empresa");
				fila[2] = rs.getString("contacto");
				fila[3] = rs.getString("telefono");

				modelo.addRow(fila);
			}

		} catch (Exception ex) {

			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
		}
	}

	public void limpiar() {

		txtEmpresa.setText("");
		txtContacto.setText("");
		txtTelefono.setText("");

		idProveedor = 0;
	}
}