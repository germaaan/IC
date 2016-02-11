package qap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QAP {

    // Definimos parámetros de la ejecución del algoritmo
    public static final int TAM_POBLACION = 100;
    public static int NUM_GENERACIONES = 100;
    public static double PROBABILIDAD_CRUCE = 0.6;
    public static double PROBABILIDAD_MUTACION = 0.05;
    public static double ELITISMO = 0.1;

    private final int numUnidades;
    private int[][] distancias;
    private int[][] flujos;

    private AlgGenetico genetico1;
    private AlgGenBaldwin genetico2;
    private AlgGenLamarck genetico3;

    public static void main(String[] args) {
        QAP problema = new QAP();
    }

    public QAP() {
        Scanner escaner = null;

        // Leemos el conjunto de datos para la evaluación "tai256c.dat"
        try {
            escaner = new Scanner(new File("qap.datos/tai256c.dat"));
        } catch (FileNotFoundException e) {
            System.out.print("Archivo de datos no encontrado: ");
            System.out.println(e.getMessage());
        }

        // Recuperamos el número de unidades
        numUnidades = escaner.nextInt();

        // Creamos las matrices de distancias y flujos con el número de unidades
        distancias = new int[numUnidades][numUnidades];
        flujos = new int[numUnidades][numUnidades];

        // Recuperamos los valores de las matrices
        for (int n = 0; n < 2; n++) {

            for (int i = 0; i < numUnidades; i++) {
                for (int j = 0; j < numUnidades; j++) {

                    if (n == 0) {
                        distancias[i][j] = escaner.nextInt();
                    } else {
                        flujos[i][j] = escaner.nextInt();
                    }
                }
            }
        }

        // Ejecutamos las 3 variantes del algoritmo
        genetico1 = new AlgGenetico(numUnidades, distancias, flujos);
        genetico2 = new AlgGenBaldwin(numUnidades, distancias, flujos);
        genetico3 = new AlgGenLamarck(numUnidades, distancias, flujos);

        long ini_1 = System.currentTimeMillis();
        genetico1.ejecutar();
        long fin_1 = System.currentTimeMillis();
        long ini_2 = System.currentTimeMillis();
        genetico2.ejecutar();
        long fin_2 = System.currentTimeMillis();
        long ini_3 = System.currentTimeMillis();
        genetico3.ejecutar();
        long fin_3 = System.currentTimeMillis();

        // Calculamos tiempos de ejecución
        long eje_1 = fin_1 - ini_1;
        long eje_2 = fin_2 - ini_2;
        long eje_3 = fin_3 - ini_3;

        // Obtenemos el mejor resultado para cada variante
        Individuo mejorIndividuo1 = genetico1.getMejorIndividuo();
        Individuo mejorIndividuo2 = genetico2.getMejorIndividuo();
        Individuo mejorIndividuo3 = genetico3.getMejorIndividuo();

        System.out.println("\nAlgoritmo genético estándar\n\tTiempo:"
                + eje_1/1000 + "\n\tMejor permutación: "
                + mejorIndividuo1.toString() + "\n\tCoste asociado: "
                + mejorIndividuo1.evaluar());

        System.out.println("Algoritmo genético baldwiniano\n\tTiempo:"
                + eje_2/1000 + "\n\tMejor permutación: "
                + mejorIndividuo2.toString() + "\n\tCoste asociado: "
                + mejorIndividuo2.evaluar());

        System.out.println("Algoritmo genético lamarckiano\n\tTiempo:"
                + eje_3/1000 + "\n\tMejor permutación: "
                + mejorIndividuo3.toString() + "\n\tCoste asociado: "
                + mejorIndividuo3.evaluar());
    }
}
