import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:sqlite:papeleria.db";

    public Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
        return conexion;
    }

    // Este método creará las tablas y dejará listos datos base/de prueba 
    // para que puedan seguir programando lo demás sin hacerlo a "ciegas" o tan de cero
    public void inicializarBaseDeDatos() {
        try (Connection conn = conectar(); 
             Statement stmt = conn.createStatement()) {
            
            //CREACIÓN DE TABLAS
            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT NOT NULL UNIQUE, "
                    + "password TEXT NOT NULL, "
                    + "rol_id INTEGER NOT NULL"
                    + ");";

            String sqlProveedores = "CREATE TABLE IF NOT EXISTS proveedores ("
                    + "id_proveedor INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre_empresa TEXT NOT NULL UNIQUE, "
                    + "contacto TEXT, "
                    + "telefono TEXT"
                    + ");";

            String sqlProductos = "CREATE TABLE IF NOT EXISTS productos ("
                    + "id_producto INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "codigo TEXT UNIQUE, "
                    + "nombre TEXT NOT NULL, "
                    + "precio_compra REAL NOT NULL, "
                    + "precio_venta REAL NOT NULL, "
                    + "stock INTEGER NOT NULL, "
                    + "id_proveedor INTEGER, "
                    + "FOREIGN KEY(id_proveedor) REFERENCES proveedores(id_proveedor)"
                    + ");";

            String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes ("
                    + "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre_completo TEXT NOT NULL, "
                    + "telefono TEXT, "
                    + "correo TEXT"
                    + ");";

            String sqlVentas = "CREATE TABLE IF NOT EXISTS ventas ("
                    + "id_venta INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "fecha TEXT NOT NULL, "
                    + "total REAL NOT NULL, "
                    + "id_usuario INTEGER, "
                    + "id_cliente INTEGER, "
                    + "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario), "
                    + "FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente)"
                    + ");";

            String sqlDetalleVentas = "CREATE TABLE IF NOT EXISTS detalle_ventas ("
                    + "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_venta INTEGER, "
                    + "id_producto INTEGER, "
                    + "cantidad INTEGER NOT NULL, "
                    + "precio_unitario REAL NOT NULL, "
                    + "subtotal REAL NOT NULL, "
                    + "FOREIGN KEY(id_venta) REFERENCES ventas(id_venta), "
                    + "FOREIGN KEY(id_producto) REFERENCES productos(id_producto)"
                    + ");";

            // Ejecutamos la creación de tablas
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlProveedores);
            stmt.execute(sqlProductos);
            stmt.execute(sqlClientes);
            stmt.execute(sqlVentas);
            stmt.execute(sqlDetalleVentas);

            //INSERCIÓN DE DATOS DE PRUEBA 
            
            // Usuario Admin
            stmt.execute("INSERT OR IGNORE INTO usuarios (username, password, rol_id) VALUES ('admin', '12345', 1);");

            // 5 Proveedores (temporal)
            String[] proveedores = {
                "INSERT OR IGNORE INTO proveedores (nombre_empresa, contacto, telefono) VALUES ('Bic', 'Juan Pérez', '555-1234');",
                "INSERT OR IGNORE INTO proveedores (nombre_empresa, contacto, telefono) VALUES ('Scribe', 'Ana Gómez', '555-5678');",
                "INSERT OR IGNORE INTO proveedores (nombre_empresa, contacto, telefono) VALUES ('Pelikan', 'Carlos Ruiz', '555-9012');",
                "INSERT OR IGNORE INTO proveedores (nombre_empresa, contacto, telefono) VALUES ('Pritt', 'Laura Silva', '555-3456');",
                "INSERT OR IGNORE INTO proveedores (nombre_empresa, contacto, telefono) VALUES ('Crayola', 'Miguel Soto', '555-7890');"
            };
            for (String p : proveedores) stmt.execute(p);

            // 30 Productos de Papelería
            String[] productos = {
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD01', 'Bolígrafo Bic Cristal Negro', 2.50, 5.00, 100, 1);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD02', 'Bolígrafo Bic Cristal Azul', 2.50, 5.00, 100, 1);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD03', 'Bolígrafo Bic Cristal Rojo', 2.50, 5.00, 50, 1);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD04', 'Cuaderno Profesional Scribe Cuadro Chico', 20.00, 35.00, 80, 2);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD05', 'Cuaderno Profesional Scribe Cuadro Grande', 20.00, 35.00, 80, 2);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD06', 'Cuaderno Profesional Scribe Raya', 20.00, 35.00, 80, 2);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD07', 'Cuaderno Profesional Scribe Blanco', 20.00, 35.00, 40, 2);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD08', 'Goma de borrar Pelikan Miga de Pan', 3.00, 6.00, 150, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD09', 'Sacapuntas metálico Pelikan', 8.00, 15.00, 60, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD10', 'Lápiz de madera Pelikan HB', 3.00, 6.00, 200, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD11', 'Lápiz adhesivo Pritt 11g', 12.00, 22.00, 90, 4);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD12', 'Lápiz adhesivo Pritt 22g', 22.00, 38.00, 60, 4);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD13', 'Lápiz adhesivo Pritt 40g', 35.00, 55.00, 40, 4);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD14', 'Crayones Crayola 12 colores', 18.00, 32.00, 50, 5);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD15', 'Crayones Crayola 24 colores', 35.00, 58.00, 40, 5);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD16', 'Colores de madera Crayola x12', 40.00, 65.00, 60, 5);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD17', 'Colores de madera Crayola x24', 80.00, 120.00, 30, 5);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD18', 'Marcatextos amarillo Pelikan', 10.00, 18.00, 70, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD19', 'Marcatextos verde Pelikan', 10.00, 18.00, 50, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD20', 'Juego de Geometría flexible', 25.00, 45.00, 40, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD21', 'Regla de plástico 30cm', 5.00, 10.00, 100, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD22', 'Tijeras punta roma escolar', 15.00, 28.00, 60, 1);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD23', 'Corrector líquido tipo pluma', 12.00, 22.00, 80, 1);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD24', 'Paquete de 500 hojas blancas Tamaño Carta', 80.00, 110.00, 30, 2);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD25', 'Paquete de 100 hojas de colores', 35.00, 55.00, 25, 2);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD26', 'Calculadora básica 8 dígitos', 45.00, 85.00, 20, 1);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD27', 'Engrapadora estándar', 40.00, 75.00, 25, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD28', 'Caja de grapas estándar', 10.00, 18.00, 50, 3);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD29', 'Cinta adhesiva transparente chica', 8.00, 15.00, 80, 4);",
                "INSERT OR IGNORE INTO productos (codigo, nombre, precio_compra, precio_venta, stock, id_proveedor) VALUES ('PROD30', 'Post-it notas adhesivas amarillas', 15.00, 25.00, 60, 4);"
            };
            for (String prod : productos) stmt.execute(prod);

            System.out.println("Base de datos estructurada con 6 tablas y datos de prueba preparados.");
            
        } catch (Exception e) {
            System.out.println("Error al inicializar BD: " + e.getMessage());
        }
    }
}