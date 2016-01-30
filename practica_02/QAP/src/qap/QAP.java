package qap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QAP {

    public static final int TAM_POBLACION = 100;
    public static int NUM_GENERACIONES = 100;
    public static double PROBABILIDAD_CRUCE = 0.6;
    public static double PROBABILIDAD_MUTACION = 0.05;
    public static double ELITISMO = 0.1;

    private final int numUnidades;
    private int[][] distancias;
    private int[][] flujos;

    private AlgGenetico genetico;

    public static void main(String[] args) {
        QAP problema = new QAP();
    }

    public QAP() {
        Scanner escaner = null;

        try {
            escaner = new Scanner(new File("src/qap.datos/nug15.dat"));
        } catch (FileNotFoundException e) {
            System.out.print("Archivo de datos no encontrado: ");
            System.out.println(e.getMessage());
        }

        numUnidades = escaner.nextInt();

        distancias = new int[numUnidades][numUnidades];
        flujos = new int[numUnidades][numUnidades];

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

        genetico = new AlgGenetico(numUnidades, distancias, flujos);

        genetico.ejecutar();

        Individuo mejorIndividuo = genetico.getMejorIndividuo();

        System.out.println(mejorIndividuo.evaluar());
        System.out.println(mejorIndividuo.toString());
    }
}
