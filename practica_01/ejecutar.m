function [] = ejecutar()
% Crea un perceptrón de dos capas, lo entrena para reconocer números reconocidos
% a mano y después comprueba su efectividad

    % Carga los datos de entrenamiento
    valoresEntrada = loadMNISTImages('data/train-images.idx3-ubyte');
    etiquetas = loadMNISTLabels('data/train-labels.idx1-ubyte');

    % Transforma las etiquetas en valores utilizables
    valoresObjetivo = 0.*ones(10, size(etiquetas, 1));
    for n = 1: size(etiquetas, 1)
        valoresObjetivo(etiquetas(n)+1, n) = 1;
    end;

    % Define los parámetros de la red neuronal y el algoritmo de entrenamiento
    numUnidades = 500;
    funcionActivacion = @funcLogisticaSigmoidal;
    dFuncionActivacion = @dFuncLogisticaSigmoidal;
    numIteraciones = 600;
    tamLote = 400;
    tasaAprendizaje = 0.1;

    fprintf('\nTOPOLOGÍA RED\n');
    fprintf('=============\n');
    fprintf('\tTipo de red neuronal: perceptrón\n');
    fprintf('\tNúmero de capas: 2 (una capa oculta y capa de salida)\n');
    fprintf('\tNúmero de unidades por capa: %d\n', numUnidades);
    fprintf('\tTipo de neurona: sigmoidal\n');

    fprintf('\nALGORITMO ENTRENAMIENTO\n');
    fprintf('=======================\n');
    fprintf('\tTipo de algoritmo: lotes completos con gradiente descendente estocástico\n');
    fprintf('\tNúmero de iteraciones: %d\n', numIteraciones);
    fprintf('\tTamaño del lote: %d\n', tamLote);
    fprintf('\tTasa de aprendizaje: %d%%\n', tasaAprendizaje*100);

    % Ejecuta el entrenamiento
    tEntI = clock;
    [pesos, pesosSalida] = entrenar(funcionActivacion, dFuncionActivacion, 
      numUnidades, valoresEntrada, valoresObjetivo, numIteraciones, tamLote,
      tasaAprendizaje, etiquetas);
    tEntF = clock;

    % Carga los datos de prueba
    valoresEntrada = loadMNISTImages('data/t10k-images.idx3-ubyte');
    etiquetas = loadMNISTLabels('data/t10k-labels.idx1-ubyte');

    % Ejecuta la validación
    tValI = clock;
    [error] = validar(funcionActivacion, pesos, pesosSalida, valoresEntrada,
     etiquetas);
    tValF = clock;

    % Calcula tiempos de ejecución
    tEnt = etime(tEntF, tEntI);
    tVal = etime(tValF, tValI);
    
    fprintf('\nRESULTADOS\n');
    fprintf('==========\n');
    fprintf('\tTiempo de entrenamiento: %d segundos\n', tEnt);
    fprintf('\tTiempo de validación: %d segundos\n', tVal);
    fprintf('\tTasa de error en validación: %d%%\n', error*100);
end
