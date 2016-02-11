package qap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class AlgGenetico {

    private final int numUnidades;
    private final int[][] distancias;
    private final int[][] flujos;

    private Individuo[] poblacion;
    private Individuo[] ordenada;
    private Individuo[] elite;

    private Individuo mejorIndividuo;

    private int[] indices;

    private final Random aleatorio;

    /**
     * Constructor del algoritmo genético con los parámetros iniciales
     *
     * @param numUnidades Número de unidades
     * @param distancias Matriz de distancias
     * @param flujos Matriz de flujos
     */
    public AlgGenetico(int numUnidades, int[][] distancias, int[][] flujos) {
        this.numUnidades = numUnidades;
        this.distancias = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(distancias, 0, this.distancias, 0, distancias.length);
        this.flujos = new int[this.numUnidades][this.numUnidades];
        System.arraycopy(flujos, 0, this.flujos, 0, flujos.length);

        this.aleatorio = new Random();
    }

    /**
     * Ejecuta el algoritmo
     */
    public void ejecutar() {
        System.out.println("\nAlgoritmo genético estándar");
        this.inicializacion();
        this.evaluacion();

        int tamElite = (int) (QAP.TAM_POBLACION * QAP.ELITISMO);

        for (int i = 1; i <= QAP.NUM_GENERACIONES; i++) {
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
            this.poblacion[i].setAptitud(this.poblacion[i].evaluar());
        }
    }

    /**
     * Evalua la población actual
     */
    private void evaluacion() {
        double mejorAptitud = 0;
        int mejorPosicion = 0;

        // Buscamos el individuo de la pobliación con el mejor coste y su posición
        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            if (this.poblacion[i].getAptitud() > mejorAptitud) {
                mejorPosicion = i;
                mejorAptitud = this.poblacion[i].getAptitud();
            }
        }

        // Guardamos el mejor individuo
        if (this.mejorIndividuo == null || mejorAptitud > this.mejorIndividuo.getAptitud()) {
            this.mejorIndividuo = new Individuo(this.poblacion[mejorPosicion]);
        }
    }

    /**
     * Selecciona la élite de los individuos de la población en función del
     * tamaño de élite especificado
     *
     * @param tamElite Tamaño de la élite
     */
    private void funcionElitista(int tamElite) {
        this.indices = new int[QAP.TAM_POBLACION];
        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            this.indices[i] = i;
        }

        // Hace una copia de la población y la ordenación según su coste
        this.ordenada = new Individuo[QAP.TAM_POBLACION];
        System.arraycopy(this.poblacion, 0, this.ordenada, 0, this.poblacion.length);
        Arrays.sort(this.ordenada);

        // Seleccionamos la "élite"
        int inicio = QAP.TAM_POBLACION - tamElite;
        this.elite = new Individuo[tamElite];
        for (int i = inicio; i < QAP.TAM_POBLACION; i++) {
            this.elite[i - inicio] = new Individuo(ordenada[i]);
        }
    }

    /**
     * Selección de individuos mediante método de torneo
     */
    private void seleccion() {
        int[] torneo = new int[QAP.TAM_POBLACION];

        int mejorParticipante_1;
        int mejorParticipante_2;
        int mejorParticipante_3;

        // Se van tomando individuos de la población de 3 en 3 de forma aleatoria
        // guardando el ganador de cada ronda
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

        // Se sustituye la población por los ganadores de cada ronda del torneo
        Individuo[] resultado = new Individuo[QAP.TAM_POBLACION];
        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            resultado[i] = new Individuo(this.poblacion[torneo[i]]);
        }
        System.arraycopy(resultado, 0, this.poblacion, 0, resultado.length);
    }

    /**
     * Cruce de individuos de la población mediante cruce por emparejamiento
     * parcial de dos padres mediante dos puntos de corte
     */
    private void cruce() {
        // Individuos que se van a reproducir
        int[] cruce = new int[QAP.TAM_POBLACION];

        int indice = 0;
        int punto_1 = -1;
        int punto_2 = -1;
        int aux;

        double probabilidad;

        // Se seleccionan los individuos que se van a reproducir de forma aleatoria
        // aunque afectado por el valor de la probabilidad de cruce
        for (int i = 0; i < QAP.TAM_POBLACION; i++) {
            probabilidad = Math.random();

            if (probabilidad < QAP.PROBABILIDAD_CRUCE) {
                cruce[indice] = i;
                indice++;
            }
        }

        // El número de individuos a reproducirse tiene que ser par
        if (indice % 2 == 1) {
            indice--;
        }

        // Elegimos dos puntos de cruce aleatorios
        while (Math.abs(punto_1 - punto_2) < 1) {
            punto_1 = this.aleatorio.nextInt(poblacion[0].getNumUnidades());
            punto_2 = this.aleatorio.nextInt(poblacion[0].getNumUnidades());
        }

        if (punto_2 < punto_1) {
            aux = punto_1;
            punto_1 = punto_2;
            punto_2 = aux;
        }

        // Vamos cruzando los individuos de 2 en 2
        for (int i = 0; i < indice; i += 2) {
            this.realizarCruce(cruce[i], cruce[i + 1], punto_1, punto_2);
        }
    }

    /**
     * Efectua cruce
     *
     * @param padre_1 Primer padre
     * @param padre_2 Segundo padre
     * @param puntoCruce_1 Primer punto de cruce
     * @param puntoCruce_2 Segundo punto de cruce
     */
    private void realizarCruce(int padre_1, int padre_2, int puntoCruce_1, int puntoCruce_2) {
        Individuo hijo_1 = new Individuo(poblacion[padre_1]);
        Individuo hijo_2 = new Individuo(poblacion[padre_2]);

        // Guardaremos los valores usados para no repetirlos
        HashSet<Integer> usadosHijo_1 = new HashSet<Integer>();
        HashSet<Integer> usadosHijo_2 = new HashSet<Integer>();

        int aux;
        int valor;
        boolean copiado;

        // Intercambiamos los valores entre los puntos de cruce
        for (int i = puntoCruce_1; i < puntoCruce_2; i++) {
            aux = hijo_1.getUnidad(i);
            hijo_1.setUnidad(i, hijo_2.getUnidad(i));
            hijo_2.setUnidad(i, aux);

            // Valores ya usados
            usadosHijo_1.add(hijo_1.getUnidad(i));
            usadosHijo_2.add(hijo_2.getUnidad(i));
        }

        // Completamos el resto de valores fuera de los puntos de cruce
        for (int i = 0; i < this.numUnidades; i++) {
            // Si la parte inicial está completada, pasamos a la final
            if (i == puntoCruce_1) {
                i = puntoCruce_2;
            }

            // Si el valor que vamos a copiar no está entre los puntos de cruce
            // copiamos al valor del primer padre, en caso contrario hacemos lo 
            // mismo, pero copiando del otro padre
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

                    // Si el valor a copiar del otro padre no ha sido ya copiado
                    // lo copiamos, en caso contrario se buscará otro elemento
                    // en otra posición
                    if (!usadosHijo_1.contains(hijo_2.getUnidad(j))) {
                        hijo_1.setUnidad(i, hijo_2.getUnidad(j));
                        copiado = true;
                    } else {
                        valor = hijo_2.getUnidad(j);
                    }
                }
            }

            // Mismo proceso para el otro hijo
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

            // Ambos son añadidos a sus correspondientes listados de valores usados
            usadosHijo_1.add(hijo_1.getUnidad(i));
            usadosHijo_2.add(hijo_2.getUnidad(i));
        }

        // Se calcula el coste de los hijos
        hijo_1.setAptitud(hijo_1.evaluar());
        hijo_2.setAptitud(hijo_2.evaluar());

        // Y estos sustituyen a sus padres
        this.poblacion[padre_1] = hijo_1;
        this.poblacion[padre_2] = hijo_2;
    }

    /**
     * Mutación mediante intercambio de dos posiciones aleatorias
     */
    private void mutacion() {
        double probabilidad;

        // Los individuos de la población mutarán en función de una probabilidad
        // aleatoria, pero que para ser efectiva tiene que superar la probabilidad
        // especificada para mutar.
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
