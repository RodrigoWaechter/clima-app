# Clima App 🌦️

Uma aplicação de desktop desenvolvida em Java com Swing para consulta de previsões meteorológicas. O projeto consome dados da API Open-Meteo para fornecer informações climáticas detalhadas, incluindo condições atuais, previsões horárias e para os próximos 7 dias.

## Funcionalidades

- **Dashboard Principal:** Visualização completa com temperatura, sensação térmica, vento, humidade e precipitação.
- **Previsão Horária:** Gráfico interativo com a previsão para as próximas 24 horas.
- **Previsão para 7 Dias:** Tabela detalhada com as condições para a semana, incluindo ícones representativos.
- **Busca por Cidade:** Pesquise o clima de qualquer cidade do mundo.
- **Histórico de Dados:** Visualize dados meteorológicos de dias anteriores com filtros por data.
- **Preferências do Utilizador:** Guarde a sua cidade favorita e personalize o tema da aplicação.
- **Temas (Look and Feel):** Suporte para múltiplos temas visuais para uma experiência personalizada.
- **Cache de Dados:** Otimização de performance através do armazenamento local de dados, evitando chamadas desnecessárias à API.

## Screenshot

![2025-07-01 14-17-36](https://github.com/user-attachments/assets/750fa441-100b-459b-87da-f3a0fa626ed3)

## Tecnologias Utilizadas

- **Java 17+**
- **Swing** para a interface gráfica
- **Maven** para gestão de dependências e build
- **Docker** para containerização do banco de dados
- **Open-Meteo API** como fonte de dados
- **MySQL** para persistência de dados

---

## Como Executar - Linux

Este projeto foi configurado para ser executado com um único comando, sem a necessidade de instalar e configurar um servidor MySQL manualmente.

### Pré-requisitos

- **Java 17 ou superior** instalado.
- **Docker Desktop** instalado e em execução.

### Passos para Executar

1.  **Clone o Repositório**
    ```bash
    git clone [https://github.com/RodrigoWaechter/clima-app.git](https://github.com/RodrigoWaechter/clima-app.git)
    cd clima-app
    ```

2.  **Dê Permissão de Execução ao Script** (Apenas na primeira vez)
    ```bash
    chmod +x clima-app.sh
    ```

3.  **Execute o Lançador**
    ```bash
    ./clima-app.sh
    ```
    O script irá automaticamente:
    - Iniciar o banco de dados MySQL dentro de um contêiner Docker.
    - Executar a aplicação de desktop.
    - Ao fechar a janela da aplicação, o script irá desligar e remover o contêiner do Docker, limpando o ambiente.

---

## Para Desenvolvedores (Execução via IDE)

Se preferir executar o projeto através de um ambiente de desenvolvimento:

1.  **Inicie o Banco de Dados com Docker**
    No terminal, na raiz do projeto, execute:
    ```bash
    docker compose up -d
    ```

2.  **Importe o Projeto no seu IDE**
    Abra o seu IDE (Eclipse, IntelliJ IDEA, etc.) e importe o projeto como um "Existing Maven Project". O IDE irá descarregar todas as dependências definidas no `pom.xml`.

3.  **Execute a Aplicação**
    Encontre a classe `MainFrm.java` no pacote `com.unisc.projeto.clima_app.view` e execute o método `main`.

4.  **Pare o Banco de Dados**
    Quando terminar de usar, pare o contêiner com o comando:
    ```bash
    docker compose down
    ```

## Estrutura do Projeto

-   `api`: Classes para comunicação com as APIs externas.
-   `controller`: Controladores que gerem a lógica de negócio.
-   `dao`: Data Access Objects para comunicação com a base de dados.
-   `domain`: Classes de modelo (POJOs) do sistema.
-   `service`: Camada de serviço que orquestra as operações.
-   `util`: Classes utilitárias.
-   `view`: Classes da interface gráfica (Swing).

## Autores

- **Lucas Souza da Silva**
- **Rodrigo Hammes Waechter**
- **Vinicius Grahl Copetti**

---
*Este projeto foi desenvolvido como parte da disciplina de Programação Avançada no curso de Ciência da Computação da Universidade de Santa Cruz do Sul (UNISC).*
