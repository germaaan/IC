package qap;

import java.util.Random;

public class Individuo {

    private int numUnidades;
    private int[][] distancias;
    private int[][] flujos;
    private int[] unidades;
    private double aptitud;

    /**
     * Constructor de los invididuos que va a contener la población (las permutaciones)
     * @param numUnidades Número de unidades
     * @param distancias Matriz de distancias
     * @param flujos Matriz de flujos
     */
    public Individuo(int numUnidades, int[][] distancias, int[][] flujos) {
        this.numUnidades = numUnidades;

        this.distancias = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(distancias, 0, this.distancias, 0, distancias.length);

        this.flujos = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(flujos, 0, this.flujos, 0, flujos.length);

        this.unidades = new int[this.numUnidades];
        for (int i = 0; i < this.numUnidades; i++) {
            this.unidades[i] = i;
        }
        
        // Las unidades son inializadas de forma aleatoria
        Random aleatorio = new Random();
        for (int i = 0; i < this.numUnidades; i++) {
            int posicion = aleatorio.nextInt(this.numUnidades);
            int aux = this.unidades[i];
            this.unidades[i] = this.unidades[posicion];
            this.unidades[posicion] = aux;
        }

        this.aptitud = 0;
    }

    /**
     * Constructor de copia
     * @param individuo Individuo que va a copiarse
     */
    public Individuo(Individuo individuo) {
        this.numUnidades = individuo.numUnidades;
        this.aptitud = individuo.aptitud;

        this.distancias = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(individuo.distancias, 0, this.distancias, 0, individuo.distancias.length);

        this.flujos = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(individuo.flujos, 0, this.flujos, 0, individuo.flujos.length);

        this.unidades = new int[this.numUnidades];
        System.arraycopy(individuo.unidades, 0, this.unidades, 0, individuo.unidades.length);
    }

    public int getNumUnidades() {
        return this.numUnidades;
    }

    public double getAptitud() {
        return this.aptitud;
    }

    public int getUnidad(int i) {
        return this.unidades[i];
    }

    public void setAptitud() {
        this.aptitud = this.evaluar();
    }

    public void setUnidad(int i, int valor) {
        this.unidades[i] = valor;
    }

    /**
     * Función de fitness del individuo
     * @return Coste de la permutación
     */
    public int evaluar() {
        int valor = 0;

        for (int i = 0; i < this.numUnidades; i++) {
            for (int j = 0; j < this.numUnidades; j++) {
                valor += this.flujos[i][j] * this.distancias[this.unidades[i]][this.unidades[j]];
            }
        }

        return valor;
    }

    @Override
    public String toString() {
        String cadena = "[";

        for (int i : unidades) {
            cadena += "" + i + " ";
        }

        return cadena.substring(0, cadena.length() - 1) + "]";
    }
}

