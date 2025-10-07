#!/bin/bash

# --- Configuração ---
# Caminho para o executável do Docker (ajuste se 'which docker' mostrar outro caminho)
DOCKER_EXECUTABLE="/usr/bin/docker"

# Caminho para o arquivo JAR da aplicação
APP_JAR="target/clima-app-0.0.1-SNAPSHOT.jar"

# Versão mínima do Java que a aplicação requer
REQUIRED_JAVA_VERSION=17

# CAMINHO DE EMERGÊNCIA: Se o script não encontrar o Java 17 automaticamente,
# ele usará este caminho. Ajuste para a sua máquina.
JAVA_17_FALLBACK_PATH="/antara/java/jdk-17.0.12"
# --------------------

# Função para encontrar um executável Java compatível
find_java_executable() {
    # 1. Tenta usar a variável de ambiente JAVA_HOME
    if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
        _java_cmd="$JAVA_HOME/bin/java"
        _version=$("$_java_cmd" -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
        if [ "$_version" -ge "$REQUIRED_JAVA_VERSION" ]; then
            echo "-> Java $_version encontrado via JAVA_HOME."
            JAVA_CMD="$_java_cmd"
            return 0
        fi
    fi

    # 2. Tenta usar o comando 'java' padrão do sistema
    if command -v java >/dev/null 2>&1; then
        _java_cmd=$(command -v java)
        _version=$("$_java_cmd" -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
        if [ "$_version" -ge "$REQUIRED_JAVA_VERSION" ]; then
            echo "-> Java $_version encontrado no PATH do sistema."
            JAVA_CMD="$_java_cmd"
            return 0
        fi
    fi
    
    # 3. Se falhar, tenta usar o caminho de emergência (chumbado)
    if [ -n "$JAVA_17_FALLBACK_PATH" ] && [ -x "$JAVA_17_FALLBACK_PATH/bin/java" ]; then
        _java_cmd="$JAVA_17_FALLBACK_PATH/bin/java"
        _version=$("$_java_cmd" -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
        if [ "$_version" -ge "$REQUIRED_JAVA_VERSION" ]; then
            echo "-> Java padrão não compatível. Usando caminho de emergência (Java $_version)."
            JAVA_CMD="$_java_cmd"
            return 0
        fi
    fi

    # 4. Se nada funcionar, exibe um erro claro
    echo ""
    echo "ERRO: Java versão $REQUIRED_JAVA_VERSION ou superior não foi encontrado." >&2
    echo "Por favor, instale o Java $REQUIRED_JAVA_VERSION+ ou configure o caminho correto na variável JAVA_17_FALLBACK_PATH dentro do script." >&2
    return 1
}

# --- Início da Execução ---

# Encontra o executável do Java
find_java_executable
if [ $? -ne 0 ]; then
    read -p "Pressione Enter para sair..."
    exit 1
fi

echo "-> Iniciando o banco de dados com Docker..."
"$DOCKER_EXECUTABLE" compose up -d

echo "-> Aguardando o banco de dados iniciar (10 segundos)..."
sleep 10

echo "-> Iniciando a aplicação Clima-App..."
"$JAVA_CMD" -jar "$APP_JAR"

echo ""
echo "-> Aplicação finalizada."
