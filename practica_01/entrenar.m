function [pesos, pesosSalida] = entrenar(funcionActivacion, dFuncionActivacion, 
  numUnidades, valoresEntrada, valoresObjetivo, numIteraciones, tamLote, 
  tasaAprendizaje, etiquetas)
% Entrena un perceptrón de dos capas (una capa oculta y capa de salida) con 
% neuronas sigmoidales con un algoritmo de lotes completos con gradiente 
% descendente estocástico
%
% Entrada:
%   funcionActivacion:  la función de activación que se usa en todas las capas 
%                       (función logística sigmoidal)
%   dFuncionActivacion: la derivada de la función de activación
%   numUnidades:        número de unidades que tiene cada capa
%   valoresEntrada:     datos de entrenamiento para la red neuronal
%   valoresObjetivo:    valores esperados para los datos de entrenamiento
%   numIteraciones:     número de iteraciones del algoritmo de entrenamiento
%   tamLote:            tamaño del lote completo usado en el algoritmo de entrenamiento
%   tasaAprendizaje:    tasa de aprendizaje que usa el algoritmo para actualizar los pesos
%   etiquetas:          las etiquetas de los datos de entrenamiento
%
% Salida:
%   pesos:              pesos en la capa oculta
%   pesosSalida:        pesos en la capa de salida

    tamConjunto = size(valoresEntrada, 2);

    tamEntrada = size(valoresEntrada, 1);
    tamSalida = size(valoresObjetivo, 1);

    % Inicializamos los pesos con valores aleatorios
    pesos = rand(numUnidades, tamEntrada);
    pesosSalida = rand(tamSalida, numUnidades);

    pesos = pesos./size(pesos, 2);
    pesosSalida = pesosSalida./size(pesosSalida, 2);

    vector = zeros(tamLote);
    
    for i = 1: numIteraciones
        for j = 1: tamLote
            % Seleccionamos el vector a entrenar
            vector(j) = floor(rand(1)*tamConjunto+1);

            % Propagamos el vector por la red
            vectorEntrada = valoresEntrada(:, vector(j));
            entradaCapaOculta = pesos*vectorEntrada;
            salidaCapaOculta = funcionActivacion(entradaCapaOculta);
            entradaCapaSalida = pesosSalida*salidaCapaOculta;
            salidaCapaSalida = funcionActivacion(entradaCapaSalida);

            vectorSalida = valoresObjetivo(:, vector(j));

            % Retropropagamos los errores
            deltaCapaSalida = dFuncionActivacion(entradaCapaSalida).*(salidaCapaSalida-vectorSalida);
            deltaCapaOculta = dFuncionActivacion(entradaCapaOculta).*(pesosSalida'*deltaCapaSalida);

            % Actualizamos los pesos
            pesosSalida = pesosSalida-tasaAprendizaje.*deltaCapaSalida*salidaCapaOculta';
            pesos = pesos-tasaAprendizaje.*deltaCapaOculta*vectorEntrada';
        end;
    end;
 end
