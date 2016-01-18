function y = dFuncLogisticaSigmoidal(x)
% Derivada de la función logística de una neuronal sigmoidal
    y = funcLogisticaSigmoidal(x).*(1 - funcLogisticaSigmoidal(x));
end
