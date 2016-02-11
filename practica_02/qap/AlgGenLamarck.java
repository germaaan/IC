package qap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class AlgGenLamarck {

    private final int numUnidades;
    private final int[][] distancias;
    private final int[][] flujos;

    private Individuo[] poblacion;
    private Individuo[] ordenada;
    private Individuo[] elite;

    private Individuo mejorIndividuo;
    private int mejorPosicion;

    private int[] indices;

    private final Random aleatorio;

    public AlgGenLamarck(int numUnidades, int[][] distancias, int[][] flujos) {
        this.numUnidades = numUnidades;
        this.distancias = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(distancias, 0, this.distancias, 0, distancias.length);
        this.flujos = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(flujos, 0, this.flujos, 0, flujos.length);

        this.mejorPosicion = 0;
        this.aleatorio = new Random();
    }

    public void ejecutar() {
        System.out.println("\nAlgoritmo genético variante lamarckiana");
        this.inicializacion();
        this.evaluacion();

        int tamElite = (int) (QAP.TAM_POBLACION * QAP.ELITISMO);

        for (int i = 0; i < QAP.NUM_GENERACIONES; i++) {
            this.funcionElitista(tamElite);
            this.seleccion();
            this.cruce();
            this.mutacion();

            for (int j = 0; j < tamElite; j++) {
                this.poblacion[this.indices[(QAP.TAM_POBLACION - 1) - j]] = this.elite[j];
            }

            this.evaluacion();
            System.out.println("Generación " + i + ": " + this.mejorIndividuo.getAptitud());
        }
    }

    public Individuo getMejorIndividuo() {
        return this.mejorIndividuo;
    }

    private void inicializacion() {
        this.poblacion = new Individuo[QAP.TAM_POBLACION];

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            this.poblacion[i] = new Individuo(this.numUnidades, this.distancias, this.flujos);
            this.poblacion[i].setAptitud(this.poblacion[i].evaluar());
        }
    }

    private void evaluacion() {
        double mejorAptitud = 0;
        int mejorPosicion = 0;

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            for (int j = i + 1; j < QAP.TAM_POBLACION; j++) {
                Individuo aux = this.poblacion[j];
                this.poblacion[j] = this.poblacion[i];
                this.poblacion[i] = aux;

                if (this.poblacion[i].getAptitud() > mejorAptitud) {
                    mejorPosicion = i;
                    mejorAptitud = this.poblacion[i].getAptitud();
                }
            }
        }

        for (int i = 1; i < mejorPosicion; i++) {
            this.poblacion[i] = this.poblacion[mejorPosicion];
        }

        if (this.mejorIndividuo == null || mejorAptitud > this.mejorIndividuo.getAptitud()) {
            this.mejorIndividuo = new Individuo(this.poblacion[mejorPosicion]);
            this.mejorPosicion = mejorPosicion;
        }
    }

    private void funcionElitista(int tamElite) {
        this.indices = new int[QAP.TAM_POBLACION];
        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            this.indices[i] = i;
        }

        this.ordenada = new Individuo[QAP.TAM_POBLACION];
        System.arraycopy(this.poblacion, 0, this.ordenada, 0, this.poblacion.length);
        Arrays.sort(this.ordenada);

        int inicio = QAP.TAM_POBLACION - tamElite;

        this.elite = new Individuo[tamElite];

        for (int i = inicio; i < QAP.TAM_POBLACION; i++) {
            this.elite[i - inicio] = new Individuo(ordenada[i]);
        }
    }

    private void seleccion() {
        int[] torneo = new int[QAP.TAM_POBLACION];

        int mejorParticipante_1;
        int mejorParticipante_2;
        int mejorParticipante_3;

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            mejorParticipante_1 = this.aleatorio.nextInt(QAP.TAM_POBLACION);
            mejorParticipante_2 = this.aleatorio.nextInt(QAP.TAM_POBLACION);
            mejorParticipante_3 = this.aleatorio.nextInt(QAP.TAM_POBLACION);

            if (this.poblacion[mejorParticipante_1].getAptitud() > this.poblacion[mejorParticipante_2].getAptitud()) {
                if (this.poblacion[mejorParticipante_1].getAptitud() > this.poblacion[mejorParticipante_3].getAptitud()) {
                    torneo[i] = mejorParticipante_1;
                } else {
                    torneo[i] = mejorParticipante_3;
                }
            } else if (this.poblacion[mejorParticipante_2].getAptitud() > this.poblacion[mejorParticipante_3].getAptitud()) {
                torneo[i] = mejorParticipante_2;
            } else {
                torneo[i] = mejorParticipante_3;
            }
        }

        Individuo[] resultado = new Individuo[QAP.TAM_POBLACION];

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            resultado[i] = new Individuo(this.poblacion[torneo[i]]);
        }

        System.arraycopy(resultado, 0, this.poblacion, 0, resultado.length);
    }

    private void cruce() {
        int[] cruce = new int[QAP.TAM_POBLACION];

        int indice = 0;
        int punto_1 = -1;
        int punto_2 = -1;
        int aux;

        double probabilidad;

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            probabilidad = Math.random();

            if (probabilidad < QAP.PROBABILIDAD_CRUCE) {
                cruce[indice] = i;
                indice++;
            }
        }

        if (indice % 2 == 1) {
            indice--;
        }

        while (Math.abs(punto_1 - punto_2) < 1) {
            punto_1 = this.aleatorio.nextInt(poblacion[0].getNumUnidades());
            punto_2 = this.aleatorio.nextInt(poblacion[0].getNumUnidades());
        }

        if (punto_2 < punto_1) {
            aux = punto_1;
            punto_1 = punto_2;
            punto_2 = aux;
        }

        for (int i = 0; i < indice; i += 2) {
            this.realizarCruce(cruce[i], punto_1, punto_2);
        }
    }

    private void realizarCruce(int padre_1, int puntoCruce_1, int puntoCruce_2) {
        // Modificación respecto a la variante baldwiniana: el segundo padre inicialmente
        // siempre es el mejor individuo de la población, así se incluyen las mejoras
        // aprendidas al aplicar la técnica de búsqueda local
        int padre_2 = this.mejorPosicion;
        Individuo hijo_1 = new Individuo(poblacion[padre_1]);
        Individuo hijo_2 = new Individuo(poblacion[padre_2]);

        HashSet<Integer> usadosHijo_1 = new HashSet<Integer>();
        HashSet<Integer> usadosHijo_2 = new HashSet<Integer>();

        int aux;
        int valor;
        boolean copiado;

        for (int i = puntoCruce_1; i < puntoCruce_2; i++) {
            aux = hijo_1.getUnidad(i);
            hijo_1.setUnidad(i, hijo_2.getUnidad(i));
            hijo_2.setUnidad(i, aux);

            usadosHijo_1.add(hijo_1.getUnidad(i));
            usadosHijo_2.add(hijo_2.getUnidad(i));
        }

        for (int i = 0; i < this.numUnidades; i++) {
            if (i == puntoCruce_1) {
                i = puntoCruce_2;
            }

            if (!usadosHijo_1.contains(this.poblacion[padre_1].getUnidad(i))) {
                hijo_1.setUnidad(i, this.poblacion[padre_1].getUnidad(i));
            } else {
                valor = poblacion[padre_1].getUnidad(i);
                copiado = false;

                while (!copiado) {
                    int j = puntoCruce_1;

                    while (valor != hijo_1.getUnidad(j) && j < puntoCruce_2) {
                        j++;
                    }

                    if (!usadosHijo_1.contains(hijo_2.getUnidad(j))) {
                        hijo_1.setUnidad(i, hijo_2.getUnidad(j));
                        copiado = true;
                    } else {
                        valor = hijo_2.getUnidad(j);
                    }
                }
            }

            if (!usadosHijo_2.contains(this.poblacion[padre_2].getUnidad(i))) {
                hijo_2.setUnidad(i, poblacion[padre_2].getUnidad(i));
            } else {
                valor = poblacion[padre_2].getUnidad(i);
                copiado = false;

                while (!copiado) {
                    int j = puntoCruce_1;

                    while (valor != hijo_2.getUnidad(j) && j < puntoCruce_2) {
                        j++;
                    }

                    if (!usadosHijo_2.contains(hijo_1.getUnidad(j))) {
                        hijo_2.setUnidad(i, hijo_1.getUnidad(j));
                        copiado = true;
                    } else {
                        valor = hijo_1.getUnidad(j);
                    }
                }
            }

            usadosHijo_1.add(hijo_1.getUnidad(i));
            usadosHijo_2.add(hijo_2.getUnidad(i));
        }

        hijo_1.setAptitud(hijo_1.evaluar());
        hijo_2.setAptitud(hijo_2.evaluar());

        this.poblacion[padre_1] = hijo_1;
        this.poblacion[padre_2] = hijo_2;
    }

    private void mutacion() {
        double probabilidad;

        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            probabilidad = Math.random();

            if (probabilidad < QAP.PROBABILIDAD_MUTACION) {
                int posicion_1 = 0;
                int posicion_2 = 0;

                while (posicion_1 == posicion_2) {
                    posicion_1 = this.aleatorio.nextInt(this.numUnidades);
                    posicion_2 = this.aleatorio.nextInt(this.numUnidades);
                }

                int aux = this.poblacion[i].getUnidad(posicion_1);
                this.poblacion[i].setUnidad(posicion_1, this.poblacion[i].getUnidad(posicion_2));
                this.poblacion[i].setUnidad(posicion_2, aux);

                this.poblacion[i].setAptitud(this.poblacion[i].evaluar());
            }
        }
    }
}
