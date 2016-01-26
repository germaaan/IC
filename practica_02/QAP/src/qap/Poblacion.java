package qap;

import java.util.ArrayList;
import java.util.Collections;

public class Poblacion {

    private int numIndividuos;
    private ArrayList<Integer> individuos;
    private double fitness;
    private double puntos;
    private double puntosAcumulados;
    private int[][] distancia;
    private int[][] flujo;

    public Poblacion(int numIndividuos, int[][] distancia, int[][] flujo) {
        this.numIndividuos = numIndividuos;

        this.individuos = new ArrayList<Integer>();

        for (int i = 0; i < numIndividuos; i++) {
            this.individuos.add(i);
        }

        Collections.shuffle(this.individuos);
    }

    public int evaluaPoblacion() {
        int fitness = 0;

        for (int i = 0; i < this.numIndividuos; i++) {
            for (int j = 0; j < this.numIndividuos; j++) {
                fitness += this.flujo[i][j] * this.distancia[this.individuos.get(i)][this.individuos.get(i)];
            }
        }

        return fitness;
    }
}
