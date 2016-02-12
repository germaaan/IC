package qap;

import java.util.ArrayList;
import java.util.Stack;

public class AlgGenBaldwin {

    private final int numUnidades;
    private final int[][] distancias;
    private final int[][] flujos;

    private Individuo[] poblacion;
    private ArrayList<Individuo> generacion;

    private Individuo mejorIndividuo;

    public AlgGenBaldwin(int numUnidades, int[][] distancias, int[][] flujos) {
        this.numUnidades = numUnidades;
        this.distancias = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(distancias, 0, this.distancias, 0, distancias.length);
        this.flujos = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(flujos, 0, this.flujos, 0, flujos.length);
    }

    public Individuo getMejorIndividuo() {
        return this.mejorIndividuo;
    }

    private void inicializacion() {
        this.poblacion = new Individuo[QAP.TAM_POBLACION];

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            this.poblacion[i] = new Individuo(this.numUnidades, this.distancias, this.flujos);
            this.poblacion[i].setAptitud();
        }
    }

    public void ejecutar() {
        System.out.println("\nAlgoritmo genético baldwiniano");
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

    private Individuo seleccion() {
        Individuo[] torneo = new Individuo[QAP.TAM_TORNEO];

        for (int i = 0; i < QAP.TAM_TORNEO; i++) {
            int participante = (int) Math.floor(Math.random() * QAP.TAM_POBLACION);
            torneo[i] = new Individuo(this.poblacion[participante]);
        }

        return evaluar(torneo);
    }

    private Stack cruce(Individuo padre_1, Individuo padre_2) {
        Individuo hijo_1 = new Individuo(padre_1);
        Individuo hijo_2 = new Individuo(padre_2);

        double factor = Math.random();

        if (factor > QAP.PROBABILIDAD_CRUCE) {
            int punto = (int) Math.floor(Math.random() * this.numUnidades);

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

    private void mutacion(Individuo hijo) {
        for (int i = 0; i < this.numUnidades; i++) {
            double factor = Math.random();

            if (factor > QAP.PROBABILIDAD_MUTACION) {
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

    private Individuo evaluar(Individuo[] poblacion) {
        double mejorAptitud = 0;
        int mejorPosicion = 0;
        Individuo mejorIndividuo;

        // Modificación respecto al algoritmo original: aplicamos un algoritmo 
        // greedy basado en 2-opt para el problema QAP
        for (int i = 0; i < poblacion.length; i++) {
            for (int j = i + 1; j < QAP.TAM_POBLACION - 1; j++) {
                Individuo aux = this.poblacion[j];
                this.poblacion[j] = this.poblacion[i];
                this.poblacion[i] = aux;

                if (poblacion[i].getAptitud() < mejorAptitud) {
                    mejorPosicion = i;
                    mejorAptitud = poblacion[i].getAptitud();
                }
            }
        }

        mejorIndividuo = new Individuo(poblacion[mejorPosicion]);

        return mejorIndividuo;
    }
}

