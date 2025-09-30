/**
CONCEPTOS FUNDAMENTALES DE PROGRAMACION
ANGIE OSPINA
GUSTAVO MAHECHA
MARÍA ACEVEDO
PROYECTO GRUPAL
*/

//Bloque para importar las librerias que vamos a usar en nuestro proyecto
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Definicion de la clase Producto con sus atributos
class Producto {
	String id, nombre;
	double precio;
	int cantidadVendida = 0;

	// Conctructor de la clase Producto
	Producto(String id, String nombre, double precio) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
	}

	// Metodos get para obtener los valores de los atributos de la clase
	// @retorna el ID del productos
	public String getId() {
		return id;
	}

	// Retorna el nombre del producto
	public String getNombre() {
		return nombre;
	}

	// Retorna el precio del Producto
	public double getPrecio() {
		return precio;
	}

}

//Declaracion clase Vendedor con los atributos indicados en el documeto del trabajo grupal
class Vendedor {
	String tipoDoc, numDoc, nombres, apellidos;
	double ventasTotales = 0.0;

	// Consctructor de la clase vendedor
	Vendedor(String tipoDocumento, String numerodocumento, String nombres, String apellidos) {
		this.tipoDoc = tipoDocumento;
		this.numDoc = numerodocumento;
		this.nombres = nombres;
		this.apellidos = apellidos;
	}

	public String getNumDoc() {
		return numDoc;
	}
	/*
	 * public String getTipoDoc() { return tipoDoc; }
	 * 
	 * public String getNombres() { return nombres; }
	 * 
	 * public String getApellidos() { return apellidos; }
	 */
}

/**
 * Declaracion del metodo principal de la clase
 */
public class Main {

	public static void main(String[] args) {
		try {
			System.out.println("Bienvenidos al modulo de ventas".toLowerCase());
			/*
			 * Llamado al método cargarDatos genérico para leer los registros del archivo
			 * productos.csv tener presente el nombre del archivo Para este caso se usa el
			 * punto y coma ';' como separador - split Se crea un objeto de tipo Producto
			 * usando los valores del arreglo d Se almacenan los productos en un Map, usando
			 * como clave el id del producto
			 */

			Map<String, Producto> mapaProductos = cargarDatos("productos.csv", linea -> {
				String[] d = linea.split(";");
				return new Producto(d[0], d[1], Double.parseDouble(d[2]));
			}, Producto::getId);

			/*
			 * Cargar la informacion de los vendedores desde el archivo vendedores.csv Crear
			 * objeto Vendedor, ingresarlo en un Map usando el número de documento como
			 * clave getNumDoc.
			 */
			Map<String, Vendedor> mapaVendedores = cargarDatos("vendedores.csv", linea -> {
				String[] d = linea.split(";");
				return new Vendedor(d[0], d[1], d[2], d[3]);
			}, Vendedor::getNumDoc);

			/*
			 * Recorre todos los archivos del directorio actual. Filtra aquellos cuyo nombre
			 * comienza con "vendedor_". Por cada archivo filtrado, llama al método
			 * procesarArchivoVenta, pasándole: El path del archivo, El mapa de productos,
			 * El mapa de vendedores.
			 */
			Files.walk(Paths.get(".")).filter(path -> path.getFileName().toString().startsWith("vendedor_"))
					.forEach(path -> procesarArchivoVenta(path, mapaProductos, mapaVendedores));

			generarReportes(mapaVendedores, mapaProductos);

			System.out.println("Reportes generados!");
			/*
			 * Si ocurre una excepción en cualquier parte del proceso, se captura y se
			 * muestra el mensaje de error por consola.
			 */
		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	/*
	 * Lee el archivo linea por linea. Registra cada línea a un objeto T, en este
	 * caso los archivos de productos o vendedores). Se obtiene la clave con el
	 * metodo getKey. Revisar muy bien las claves en el archivo,si hay claves
	 * duplicadas lanza IllegalStateException.
	 */
	private static <T, K> Map<K, T> cargarDatos(String archivo, java.util.function.Function<String, T> constructor,
			java.util.function.Function<T, K> getKey) throws IOException {
		return Files.lines(Paths.get(archivo)).map(constructor).collect(Collectors.toMap(getKey, item -> item));
	}

	/*
	 * Lee todas las líneas del archivo, obtenemos el id del vendedor de la primera
	 * línea. Si no existe el vendedor en el mapa, retorna (ignora archivo)
	 * datos[0]: id de producto datos[1]: cantidad (int) Luego hacemos una
	 * instruccion condicional, if producto != null && cantidad > 0 &&
	 * producto.precio > 0: Suma al vendedor: precio * cantidad. Acumula cantidad en
	 * producto.cantidadVendida. Si algo falla, imprime advertencia con el nombre
	 * del archivo.
	 */
	private static void procesarArchivoVenta(Path archivo, Map<String, Producto> prods, Map<String, Vendedor> vends) {
		try {
			List<String> lineas = Files.readAllLines(archivo);
			String idVendedor = lineas.get(0).split(";")[1];
			Vendedor vendedor = vends.get(idVendedor);

			if (vendedor == null)
				return;
			String[] datos;
			Producto producto;
			int cantidad;
			for (int i = 1; i < lineas.size(); i++) {
				// String[] datos = lineas.get(i).split(";");
				datos = lineas.get(i).split(";");
				// Producto producto;// = prods.get(datos[0]);
				producto = prods.get(datos[0]);
				cantidad = Integer.parseInt(datos[1]);
				if (producto != null && cantidad > 0 && producto.precio > 0) {
					vendedor.ventasTotales += producto.precio * cantidad;
					producto.cantidadVendida += cantidad;
				}
			}
		} catch (Exception e) {
			System.err.println("ADVERTENCIA al procesar: " + archivo.getFileName() + " Inconsistencia en datos: ");
		}
	}

	/*
	 * Edita el archivo reporte_vendedores.csv con nombres, apellidos y
	 * ventasTotales
	 */
	private static void generarReportes(Map<String, Vendedor> mapaVendedores, Map<String, Producto> mapaProductos)
			throws IOException {
		List<Vendedor> vendedoresOrdenados = mapaVendedores.values().stream()
				.sorted(Comparator.comparingDouble(v -> -v.ventasTotales)).collect(Collectors.toList());

		try (PrintWriter writer = new PrintWriter("reporte_vendedores.csv")) {
			for (Vendedor v : vendedoresOrdenados) {
				writer.printf("%s %s;%.2f%n", v.nombres, v.apellidos, v.ventasTotales);
			}
		}

		List<Producto> productosOrdenados = mapaProductos.values().stream()
				.sorted(Comparator.comparingInt(p -> -p.cantidadVendida)).collect(Collectors.toList());
		try (PrintWriter writer = new PrintWriter("reporte_productos.csv")) {
			for (Producto p : productosOrdenados) {
				writer.printf("%s;%.2f%n", p.nombre, p.precio);
			}
		}
	}
}

/**
 * Método principal del programa. Este método realiza las siguientes acciones:
 * 1. Carga los datos de productos desde el archivo 'productos.csv' y los
 * almacena en un mapa indexado por ID. 2. Carga los datos de vendedores desde
 * el archivo 'vendedores.csv' y los almacena en un mapa indexado por número de
 * documento. 3. Busca y procesa archivos de ventas que comienzan con el prefijo
 * 'vendedor_' en el directorio actual. 4. Genera reportes basados en la
 * información de ventas procesada. Si ocurre algún error durante la ejecución,
 * se captura y se muestra en la consola.
 */
