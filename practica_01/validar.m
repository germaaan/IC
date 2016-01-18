function [error] = validar(funcionActivacion, pesos, pesosSalida, 
  valoresEntrada, etiquetas)
% Ejecuta una prueba de reconocimiento con los datos de prueba sobre la red 
% neuronal ya entrenada.
%
% Entrada:
%   funcionActivacion:  la función de activación que se usa en todas las capas 
%                       (función logística sigmoidal)
%   pesos:              pesos definitivos en la capa oculta
%   pesosSalida:        pesos definitivos en la capa de salida
%   valoresEntrada:     datos de prueba para la red neuronal
%   etiquetas:          las etiquetas de los datos de prueba
%
% Salida:
%   error:              tasa de errores cometidos en la clasificación durante la validación

    tamConjunto = size(valoresEntrada, 2);
    error = 0;

    for i = 1: tamConjunto
        % Realizamos la comprobación
        vectorEntrada = valoresEntrada(:, i);
        vectorSalida = funcionActivacion(pesosSalida*funcionActivacion(pesos*vectorEntrada));

        max = 0;
        clase = 1;

        for j = 1: size(vectorSalida, 1)
            if vectorSalida(j) > max
                max = vectorSalida(j);
                clase = j;
            end;
        end;

        if clase != etiquetas(i)+1
          error = error+1;
        end;
    end;
    
    error = error/tamConjunto;
end
