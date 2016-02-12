package qap;

import java.util.ArrayList;
import java.util.Stack;

public class AlgoritmoGenetico {

    private final int numUnidades;
    private final int[][] distancias;
    private final int[][] flujos;

    private Individuo[] poblacion;
    private ArrayList<Individuo> generacion;

    private Individuo mejorIndividuo;

    /**
     * Constructor del algoritmo genético con los parámetros iniciales
     *
     * @param numUnidades Número de unidades
     * @param distancias Matriz de distancias
     * @param flujos Matriz de flujos
     */
    public AlgoritmoGenetico(int numUnidades, int[][] distancias, int[][] flujos) {
        this.numUnidades = numUnidades;
        this.distancias = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(distancias, 0, this.distancias, 0, distancias.length);
        this.flujos = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(flujos, 0, this.flujos, 0, flujos.length);
    }

    /**
     * Devuelve el mejor individuo en función de su coste asociado
     *
     * @return Mejor individuo
     */
    public Individuo getMejorIndividuo() {
        return this.mejorIndividuo;
    }

    /**
     * Crea la población inicial y asigna los valores de coste iniciales
     */
    private void inicializacion() {
        this.poblacion = new Individuo[QAP.TAM_POBLACION];

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            this.poblacion[i] = new Individuo(this.numUnidades, this.distancias, this.flujos);
            this.poblacion[i].setAptitud();
        }
    }

    /**
     * Ejecuta el algoritmo
     */
    public void ejecutar() {
        System.out.println("\nAlgoritmo genético estándar");
        this.inicializacion();

        for (int i = 1; i <= QAP.NUM_GENERACIONES; i++) {
            this.generacion = new ArrayList<Individuo>();

            for (int j = 0; j < QAP.TAM_POBLACION / 2; j++) {
                Individuo padre_1 = new Individuo(this.seleccion());
                Individuo padre_2 = new Individuo(this.seleccion());

                Stack descendientes = this.cruce(padre_1, padre_2);

                Individuo hijo_2 = new Individuo((Individuo) descendientes.pop());
                Individuo hijo_1 = new Individuo((Individuo) descendientes.pop());

                this.mutacion(hijo_1);
                this.mutacion(hijo_2);

                hijo_1.setAptitud();
                hijo_2.setAptitud();

                this.generacion.add(hijo_1);
                this.generacion.add(hijo_2);
            }

            this.poblacion = this.generacion.toArray(new Individuo[QAP.TAM_POBLACION]);
            this.mejorIndividuo = new Individuo(this.evaluar(this.poblacion));

            System.out.println("Generación " + i + ": " + this.mejorIndividuo.getAptitud());
        }
    }

    /**
     * Selección de individuos mediante método de torneo
     */
    private Individuo seleccion() {
        Individuo[] torneo = new Individuo[QAP.TAM_TORNEO];

        // Elegimos los participantes de forma aleatoria
        for (int i = 0; i < QAP.TAM_TORNEO; i++) {
            int participante = (int) Math.floor(Math.random() * QAP.TAM_POBLACION);
            torneo[i] = new Individuo(this.poblacion[participante]);
        }

        // Devolvemos el que tenga una mejor función de aptitud
        return evaluar(torneo);
    }

    /** 
     * Cruce de individuos mediante un punto de corte aleatorio
     * @param padre_1 Primer padre
     * @param padre_2 segundo padre
     * @return Hijos producto de la reproducción
     */
    private Stack cruce(Individuo padre_1, Individuo padre_2) {
        Individuo hijo_1 = new Individuo(padre_1);
        Individuo hijo_2 = new Individuo(padre_2);

        // Calculamos la probabilidad de que se produzca la reproducción
        double factor = Math.random();

        if (factor > QAP.PROBABILIDAD_CRUCE) {
            // Si se produce reproducción, obtenemos un punto de corte aleatorio
            int punto = (int) Math.floor(Math.random() * this.numUnidades);

            // Cada hijo tendra una parte como un padre y la otra parte como el otro padre
            for (int i = punto; i < this.numUnidades; i++) {
                int aux = hijo_1.getUnidad(i);
                hijo_1.setUnidad(i, hijo_2.getUnidad(i));
                hijo_2.setUnidad(i, aux);
            }
        }

        Stack descendientes = new Stack();
        descendientes.push(hijo_1);
        descendientes.push(hijo_2);

        return descendientes;
    }

    /**
     * Mutación mediante intercambio de dos posiciones aleatorias
     */
    private void mutacion(Individuo hijo) {
        for (int i = 0; i < this.numUnidades; i++) {
            // Calculamos la probabilidad de que se produzca la mutación
            double factor = Math.random();

            if (factor > QAP.PROBABILIDAD_MUTACION) {
                // Si se produce reproducción, obtenemos los puntos a intercambiar
                int punto_1 = 0;
                int punto_2 = 0;

                while (punto_1 == punto_2) {
                    punto_1 = (int) Math.floor(Math.random() * this.numUnidades);
                    punto_2 = (int) Math.floor(Math.random() * this.numUnidades);
                }

                int aux = hijo.getUnidad(punto_1);
                hijo.setUnidad(punto_1, hijo.getUnidad(punto_2));
                hijo.setUnidad(punto_1, aux);
            }
        }
    }

    /**
     * Evalua la población actual
     */
    private Individuo evaluar(Individuo[] poblacion) {
        double mejorAptitud = 0;
        int mejorPosicion = 0;
        Individuo mejorIndividuo;

        // Buscamos el individuo de la pobliación con el mejor coste y su posición
        for (int i = 0; i < poblacion.length; i++) {
            if (poblacion[i].getAptitud() < mejorAptitud) {
                mejorPosicion = i;
                mejorAptitud = poblacion[i].getAptitud();
            }
        }

        mejorIndividuo = new Individuo(poblacion[mejorPosicion]);

        return mejorIndividuo;
    }
}

