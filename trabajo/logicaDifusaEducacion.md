# Lógica difusa en la educación

## Germán Martínez Maldonado
## Inteligencia Computacional
### Máster Universitario en Ingeniería Informática

### 1. Introducción al problema

Generalmente la evaluación de un alumno en una asignatura solo se realiza mediante una única dimensión, una nota final que ha obtenido en base a los diferentes trabajos/examenes evaluables, esta nota es la que indicará si el alumno es apto o no para superar la asignatura. El problema que puede presentar este tipo de sistemas de evaluación es el de decidir si siempre que un alumno no llega a entrar en el umbral de calificación que se considera aprobado se debe considerar que el alumno no ha superado la asignatura, o si de forma general se puede definir una zona de proximidad numérica de dicho umbral en el que tambén se podría considerar que el alumno supera la matería.

Es por eso que se puede plantear el uso de más variables para realizar dicha evaluación que no tienen por qué basarse exclusivamente en medidas cuantitativas como son las notas numéricas parar representar el rendimiento del alumno, también se puede tener en cuenta otros factores cualitativos como es el propio interés del alumno por la materia dada, lo que podría darnos una nueva dimensión que nos permita que los resultados sean más globales.

Para explicar esto, voy a exponer un método alternativo de calificación que usaron los autores de un artículo sobre esta temática: Iván Darío Gómez Araújo, Jabid Eduardo Quiroga Méndez y Neyid Mauricio Jasbón Carvajal de la Universidad Industrial de Santander, Bucaramanga (Colombia).

Estos autores proponen un método alternativo de calificación usando un sistema de inteligencia artificial que se basa en la lógica difusa usando como datos de entrada las calificaciones de trabajos, examenes y el interés del estudiante respecto a la asignatura, una dimensión subjetiva que puede abarcar la disposición y participación del alumno ante la asignatura, elementos que se pueden evaluar mediante términos lingüisticos que tradicionalmente no toman parte en la calificación numérica de una asignatura.

#### 2. Marco teórico

Si entendemos que la lógica difusa como un mecanismo que nos permite interpretar una información en un entorno ambigüo comprenderemos rapidamente la aplicación para este caso concreto en el que deberiamos concretar exactamente que podriamos entender como un "estudiante deficiente" o un "estudiante excelente". Una vez aclaro esto, debemos pasar a definir los distintos elementos básicos necesarios como son los conjuntos difusos, las funciones de permanencia y las diferentes operaciones aritméticas y sistemas de inferencia difusos.

#### 2.1 Conjuntos difusos y funciones de pertenencia

Suponemos que X es una colección de objetos nombramos como x, X = {x<sub>1</sub>, x<sub>2</sub>, ..., x<sub>n</sub>}, un subconjunto difuso de A en X es un conjunto de pares ordenados A = {µ<sub>A</sub>(x)|x,x∈X}; donde µ<sub>A</sub>(x) es la función de permanencia de x en A, esta función define el conjunto difuso indicando el grado de pertenencia en el cual el elemento x está incluido en el subconjunto A.

Por ejemplo, si quisieramos representar el grado de pertenencia de un estudiante al conjunto "estudiante excelente" basada en una nota final representada en un rango [0-5], podriamos establecer que el eje horizontal x se corresponda con el valor de la nota final del alumno y que el eje vertical y se corresponda con el grado de pertenencia de ese alumno al conjunto "estudiante excelente" en un rango [0-1].

![graficaPertenencia](images/graficaPertenencia.png)

La construcción de un conjunto difuso supone la construcción de una función de pertenencia que se podrá determinar en base a diferentes criterios o procedimientos y que implica el uso de diferentes técnicas estadísticas y de intelgencia artificial.
