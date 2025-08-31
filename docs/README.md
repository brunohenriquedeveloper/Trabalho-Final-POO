Trabalho Final de Programação Orientada a Objetos

Equipe:
Bruno Henrique Carneiro da Silva
Gabriel Rodrigues dos Santos

Link do repositório: https://github.com/brunohenriquedeveloper/Trabalho-Final-POO

-------------------------------------------------------------------------------------

Tema do Projeto

Este projeto realiza uma análise exploratória dos dados do Campeonato Brasileiro de Futebol. A partir de um arquivo CSV contendo os resultados das partidas, o sistema processa e organiza as informações para gerar visualizações gráficas diversas. Essas visualizações permitem identificar padrões, tendências e estatísticas relevantes, como desempenho das equipes, ranking de gols, o time que fez mais pontos desde o início do arquivo, a melhor defesa e o melhor ataque ao longo da competição. O objetivo é fornecer uma visão clara e intuitiva dos dados, facilitando a interpretação e a tomada de decisões baseadas em informações concretas do campeonato.

--------------------------------------------------------------------------------------

Discussão sobre o Desenvolvimento do Projeto

O projeto tem como objetivo realizar uma análise exploratória de dados do Campeonato Brasileiro de Futebol, utilizando como entrada arquivos CSV com os resultados das partidas. A proposta central foi estruturar o sistema de forma modular e orientada a objetos, garantindo clareza, manutenibilidade e extensibilidade.

O ponto de entrada do programa é a classe Main, que funciona como controlador de alto nível. Ela não processa os dados diretamente, mas lida com a execução do programa, criando um objeto LeitorCsv para interpretar o arquivo CSV, obtendo a lista de times e partidas, criando um objeto Campeonato com essas informações e imprimindo um resumo dos resultados no console. Essa abordagem evidencia o princípio de responsabilidade única da Programação Orientada a Objetos (POO), pois a classe Main apenas coordena o fluxo, enquanto outras classes cuidam do processamento e do armazenamento de dados.

A classe LeitorCsv é responsável por toda a lógica de leitura e interpretação do CSV, transformando cada linha em objetos do domínio, como Time e Partida. Ela garante que cada time seja criado uma única vez e que as estatísticas sejam atualizadas corretamente à medida que as partidas são processadas. Além disso, contém métodos auxiliares para lidar com conversões de tipos, limpeza de caracteres e tratamento de dados ausentes, uma vez que a falta desses tratamentos poderia causar inconsistências. Ao isolar a lógica de leitura em uma classe dedicada, o projeto segue os princípios de encapsulamento e coesão, evitando repetição de código e facilitando a manutenção.

As classes Time e Partida foram desenvolvidas como elementos centrais do domínio do Campeonato Brasileiro de Futebol, com foco na organização clara dos dados e na manutenção da integridade das informações. A classe Time é responsável por representar cada clube, armazenando dados básicos como nome, técnico (mesmo não sendo utilizado no projeto, optou-se por declará-lo para manter a coesão) e estado, bem como estatísticas de desempenho, incluindo vitórias, empates, derrotas, gols marcados e sofridos, vitórias em casa e fora, e partidas jogadas. Ela também possui métodos que permitem atualizar automaticamente essas estatísticas sempre que uma nova partida é registrada, calcular métricas importantes como pontuação e saldo de gols e apresentar um resumo legível do estado do time.

A classe Partida, por sua vez, encapsula os dados de cada jogo, registrando informações essenciais como mandante, visitante e gols marcados por cada equipe. Essa abordagem garante que cada partida seja tratada de forma isolada, mantendo coerência e confiabilidade nos dados ao longo do programa.

No desenvolvimento dessas classes, aplicaram-se conceitos fundamentais da programação orientada a objetos. A abstração é evidente, pois cada classe modela exatamente um conceito do domínio: Time abstrai o clube com seus atributos e estatísticas, enquanto Partida abstrai um jogo com suas informações específicas. O encapsulamento foi utilizado ao proteger os atributos internos, permitindo acesso e alterações apenas por meio de métodos definidos, assegurando que os dados não sejam corrompidos. Além disso, cada classe segue o princípio de responsabilidade única, concentrando apenas as funcionalidades relacionadas ao seu papel: Time cuida da gestão de informações e estatísticas de clubes, enquanto Partida cuida do registro de partidas, sem se envolver com leitura de arquivos ou interface do usuário.

Essas escolhas favoreceram a modularização do sistema, permitindo que cada componente fosse reutilizável e independente. A modularização torna o código mais organizado e facilita manutenção, testes e futuras expansões, como integração com interfaces gráficas ou análises estatísticas adicionais.

A classe Campeonato agrega os objetos Time e Partida e fornece funcionalidades para classificação, filtragem e análise de desempenho dos times. Ela centraliza a lógica relacionada ao campeonato em si, permitindo que consultas e estatísticas sejam feitas de maneira consistente e organizada.

Para a análise estatística, o projeto utiliza uma classe abstrata AnaliseEstatistica, estendida por CalculoAnaliseEstatistica, que implementa métodos específicos para rankings e cálculos de desempenho. Essa abordagem evidencia o uso de polimorfismo e herança, permitindo que diferentes tipos de análises sejam implementados sem alterar a estrutura principal do sistema.

Por fim, a interface gráfica construída em Java Swing é composta pelas classes AppGUI e MainGUI. A AppGUI funciona como ponto de entrada da aplicação no contexto gráfico. Sua função principal é carregar os dados do Campeonato Brasileiro a partir de um arquivo CSV, criar os objetos de domínio (Time e Partida) e instanciar o Campeonato com essas informações. Em seguida, ela inicializa a interface gráfica chamando a MainGUI dentro da thread de eventos do Swing, garantindo que a execução da interface seja segura e responsiva.

A MainGUI é responsável por apresentar os dados ao usuário e permitir a interação com diferentes análises estatísticas. Ela organiza a janela principal da aplicação, incluindo botões para seleção de rankings e filtros, campos de entrada para especificar o ano de interesse e um painel central para exibição de gráficos. Cada botão de ação possui um listener que, ao ser acionado, consulta os dados do Campeonato ou da análise estatística e chama métodos especializados para desenhar gráficos de barras, linhas ou pizza, mostrando informações como gols marcados, saldo de gols, vitórias em casa e fora, melhor ataque ou defesa por ano, entre outros.

O painel de gráficos da MainGUI é totalmente independente da lógica de cálculo ou do carregamento de dados, recebendo apenas listas de objetos Time e o tipo de métrica a ser exibida. Métodos como desenhar gráficos em barras, horizontais, de linhas e de pizza encapsulam toda a lógica de renderização, garantindo modularidade e clareza no código. A interface também fornece feedback ao usuário, exibindo mensagens quando os dados não estão disponíveis ou quando entradas inválidas são fornecidas, como um ano inexistente.

Em resumo, a AppGUI gerencia a inicialização da aplicação e o carregamento dos dados, enquanto a MainGUI cuida da apresentação, interação e visualização gráfica. Juntas, elas permitem que o usuário explore as estatísticas do campeonato de forma dinâmica e intuitiva, separando claramente a camada de interface da lógica de domínio e análise de dados.

--------------------------------------------------------------------------------------

Discussão da Programação Orientada a Objetos em Python

No projeto desenvolvido em Python, a programação orientada a objetos foi aplicada de forma clara e estruturada, refletindo os mesmos princípios utilizados na versão Java. As classes de domínio, como Time, Partida e Campeonato, foram desenhadas para representar conceitos centrais do sistema, encapsulando atributos e comportamentos relevantes.

A classe Time gerencia dados de desempenho de um clube, incluindo vitórias, empates, derrotas, gols marcados e sofridos, além de vitórias em casa e fora, e partidas jogadas. Já a classe Partida armazena informações específicas de cada jogo, como mandante, visitante e placar, garantindo que cada partida seja tratada de forma isolada e consistente. A classe Campeonato agrega esses objetos, fornecendo métodos para classificação, filtragem e análise de desempenho, centralizando a lógica relacionada ao campeonato de maneira organizada e coesa.

Em Python, o encapsulamento foi implementado por meio de convenções de nomes, utilizando atributos protegidos (_atributo) para indicar que o acesso direto não é recomendado, garantindo que alterações nos dados ocorram apenas através de métodos definidos. Essa abordagem segue a mesma lógica conceitual da versão Java, que utiliza modificadores de acesso como private e public para proteger atributos e métodos, embora a sintaxe em Python seja mais flexível e menos verbosa.

A modularidade do sistema também está presente, com a separação de responsabilidades entre classes: LeitorCSV cuida exclusivamente da leitura e interpretação dos arquivos CSV, convertendo linhas em objetos de domínio; CalculoEstatistica realiza as análises e rankings; e MainGui lida com a interface gráfica, exibindo gráficos e painéis para interação com o usuário. Essa divisão clara exemplifica o princípio de responsabilidade única, facilitando manutenção, testes e futuras expansões.

O projeto também faz uso de herança e polimorfismo. A classe abstrata AnaliseEstatistica define métodos genéricos de análise, enquanto a classe CalculoEstatistica implementa esses métodos de forma concreta, permitindo a criação de diferentes tipos de análises sem a necessidade de alterar a estrutura principal do sistema. Esse uso de herança e polimorfismo é equivalente ao que foi aplicado na versão Java, demonstrando consistência na aplicação dos princípios de POO nas duas linguagens.

A interface gráfica em Python, construída com PyQt, é modular e independente da lógica de análise ou leitura de dados, recebendo apenas listas de objetos Time e o tipo de métrica a ser exibida, garantindo que a renderização e interação com o usuário estejam separadas da lógica de negócios, assim como ocorre na versão Java com Swing.

Quando comparamos Python e Java no contexto do mesmo projeto, é possível observar tanto semelhanças quanto diferenças significativas. Em termos de semelhanças, ambas as linguagens seguem rigorosamente os princípios de POO, incluindo encapsulamento, abstração, modularidade e responsabilidade única. As classes de domínio mantêm a mesma função em ambas as versões, organizando dados e métodos relacionados. A lógica de análise estatística é isolada em classes separadas, evidenciando o uso de herança e polimorfismo. As interfaces gráficas em ambas as linguagens consomem dados de objetos de domínio e análise sem alterar a lógica interna, garantindo a separação de responsabilidades.

Já em termos de diferenças, Python apresenta uma sintaxe mais concisa e expressiva, permitindo manipulação direta de listas e dicionários de objetos, ordenações e filtragens com lambda e list comprehensions, enquanto Java exige declarações de tipos explícitos, loops mais detalhados e getters/setters formais. O encapsulamento em Python é baseado em convenções, ao passo que em Java é aplicado de forma rigorosa através de modificadores de acesso. A construção de interfaces gráficas em Python com PyQt é mais declarativa e flexível, enquanto Java Swing demanda configuração detalhada de layouts, listeners e componentes.

Um ponto adicional de discussão, levantado em sala de aula, foi a possibilidade de criação ou emulação de interfaces em Python. Diferentemente de Java, onde interfaces são um recurso nativo e obrigatório para a definição de contratos entre classes, em Python esse conceito não existe formalmente. No entanto, é possível alcançar efeito semelhante utilizando o módulo abc (Abstract Base Classes), que permite declarar métodos abstratos e garantir que classes derivadas implementem determinados comportamentos. Embora não tenha sido utilizado no projeto, essa pesquisa e a discussão mostraram que é uma alternativa viável em Python, sendo mais um exemplo de como cada linguagem possui especificidades próprias.

Em resumo, o projeto em Python demonstra que os conceitos de orientação a objetos podem ser aplicados de forma consistente e eficaz, mantendo modularidade, encapsulamento, abstração e responsabilidades bem definidas, assim como na versão Java.

--------------------------------------------------------------------------------------

Tutorial

Como rodar em Java:

Para executar a versão Java, não é necessário compreender todos os detalhes internos das classes. Basta localizar o arquivo AppGUI e executá-lo. Esse arquivo inicializa toda a aplicação e abrirá a interface gráfica construída em Swing.

Na interface, existem dois tipos principais de botões:

Botões gerais: exibem gráficos diretamente ao serem clicados, sem necessidade de entrada adicional.

Botões que dependem de ano: nesses casos, é necessário informar o ano desejado para que o gráfico seja gerado corretamente, já que determinadas estatísticas (como melhor defesa ou melhor ataque) variam a cada edição do Campeonato Brasileiro.

Assim, de forma intuitiva, você poderá interagir com os gráficos e explorar as estatísticas do campeonato diretamente na tela.

Como rodar em Python:

No caso da versão Python, a execução exige um pequeno passo adicional em relação à versão Java. Todos os códigos relacionados à lógica estão dentro da pasta Python, enquanto a interface gráfica está localizada na pasta uiPython.

Para rodar a interface gráfica em Python, siga os passos:

1 - Abra o terminal.
2 - Navegue até a pasta onde se encontra o arquivo MainGUI.py (ou o nome equivalente utilizado). Para isso, utilize o comando cd seguido do caminho da pasta.
3- Execute o seguinte comando no terminal: python MainGUI.py

Ao rodar esse comando, a interface gráfica será aberta e funcionará de maneira muito próxima à versão em Java. Assim como no Swing, os botões que exibem estatísticas gerais mostram resultados imediatos ao clique, enquanto aqueles que dependem de ano exigem a inserção de um valor correspondente à edição do campeonato.

Seguindo essas instruções, você deverá conseguir executar tanto a interface gráfica em Java quanto em Python sem maiores problemas. O objetivo desse tutorial é garantir que a experiência do usuário seja fluida e intuitiva, permitindo explorar os dados do Campeonato Brasileiro sem entraves técnicos.

--------------------------------------------------------------------------------------

Tecnologias:

Java
Python
Swing GUI
PyQt5