/**
CONCEPTOS FUNDAMENTALES DE PROGRAMACION
ANGIE OSPINA
GUSTAVO MAHECHA
MARÍA ACEVEDO
PROYECTO GRUPAL
*/

import java.io.PrintWriter;
import java.util.Random;

public class GenerateInfoFiles {

	// Arrays con datos de prueba para generar los archivos CSV con información
	// aleatoria

	private static final String[] NOMBRES = { "Carlos", "Ana", "Luis", "Maria", "Juan", "Sofia", "Natalia" };
	private static final String[] APELLIDOS = { "Gomez", "Perez", "Rodriguez", "Martinez", "Perales", "Jerez",
			"Montecristo" };
	private static final String[] TIPOS_DOC = { "CC", "CE", "TI" };
	private static final String[] PRODUCTOS_NOMBRES = { "Laptop", "Mouse", "Teclado", "Monitor", "Router",
			"Antena WI-FI", "Memoria RAM", "Disco Duro", "Parlantes", "Kit de limpieza" };
	private static final double[] PRODUCTOS_PRECIOS = { 2500000, 80000, 150000, 950000, 275600, 115000, 235000, 356000,
			457000, 23000 };

	public static void main(String[] args) {
		try {
			System.out.println("Iniciando generacion de archivos");
			createProductsFile(PRODUCTOS_NOMBRES.length);
			// Mostramos en consola la cantidad de productos con el metodo lenght
			System.out.println("Cantidad de productos --> " + PRODUCTOS_NOMBRES.length);
			// Llamado el metodo para crear los vendedores, el metodo recibe como parametro
			// de entrada la cantidad que se va a crear. Es un metodo publico y no tiene
			// valor de retorno porque es void
			createSalesManInfoFile(7);
			System.out.println("Archivos generados exitosamente!");
		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}

	}

	// Declaracion del metodo para crear el archivo con los productos el cual recibe
	// como parametro de entrada la cantidad de productos que almacenamos en el
	// arreglo
	/**
	 * Creamos el archivo .csv el cual contiene un ciclo for para recorrer los
	 * productos Genera un número de identificación aleatorio
	 */
	public static void createProductsFile(int productsCount) throws Exception {

		try (PrintWriter writer = new PrintWriter("productos.csv", "UTF-8")) {
			for (int i = 0; i < productsCount; i++) {
				writer.println((i + 1) + ";" + PRODUCTOS_NOMBRES[i] + ";" + PRODUCTOS_PRECIOS[i]);
			}

		}

	}

	// Se usa PrintWriter para generar en el archivo "vendedores.csv"
	public static void createSalesManInfoFile(int salesmanCount) throws Exception {
		Random rand = new Random();

		try (PrintWriter writer = new PrintWriter("vendedores.csv", "UTF-8")) {
			String nombre;
			String apellidos;

			for (int i = 0; i < salesmanCount; i++) {
				long id = 105478600 + rand.nextInt(105542700);
				nombre = NOMBRES[i];
				apellidos = APELLIDOS[i];
				/*
				 * Escribe la línea en el archivo CSV con tipo de documento, ID, nombre y
				 * apellido
				 */
				writer.println(TIPOS_DOC[rand.nextInt(TIPOS_DOC.length)] + ";" + id + ";" + nombre + ";" + apellidos);
				createSalesMenFile(rand.nextInt(4) + 2, nombre, apellidos, id);

			}
		}
	}

	/*
	 * Genera un archivo CSV con información básica de un vendedor y una lista de
	 * ventas simuladas. El nombre del archivo se construye usando el ID, nombre y
	 * apellidos del vendedor.
	 */

	public static void createSalesMenFile(int randomSalesCount, String name, String apellidos, long id)
			throws Exception {

		Random rand = new Random();
		// Construye el nombre del archivo con el formato: vendedor_ID_Nombre - Apellidos.csv
		String fileName = "vendedor_" + id + "_" + name + " " + apellidos + ".csv";
		try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
			writer.println(TIPOS_DOC[rand.nextInt(TIPOS_DOC.length)] + ";" + id + ";" + name + " " + apellidos);
			// Genera las lines de ventas de manera aleatoria
			for (int i = 0; i < randomSalesCount; i++) {
				writer.println((rand.nextInt(PRODUCTOS_NOMBRES.length) + 1) + ";" + (rand.nextInt(10) + 1) + ";");

			}
		}
	}

