# IMDbQueryProject
## Agosto/2020 - Aplicação console em Java para consultas via socket de títulos de filmes no site IMDb.

### Requisito: 
O usuário (cliente) precisa digitar o nome de um filme que deseja e o componente (server) deve buscá-lo no site IMDb com tempo de resposta satisfatório para o usuário. Um ou mais servers devem aguardar as solicitações em background esperando as solicitações pelas suas respectivas portas TCP. O client e server devem usar um protocolo comum de comunicação, e a comunicação deve ser concorrente e performática.

### Solução: 
Uma aplicação console foi criada para o server e outra para o client. O server deve executar numa porta TCP especificada pelo usuário e ficar aguardando as solicitações. Vários clients solicitam a estes TCP servers o filme desejado, e o server responsável dispara uma Thread para cada client, permitindo assim múltiplas requisições simultâneas usando Sockets com Threads.

### Ambiente de desenvolvimento adotado:

- **Windows 10 Home**
- [**Eclipse IDE for Java Developers Version: 2020-06 (4.16.0) Download**](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2020-06/R/eclipse-inst-win64.exe&mirror_id=576)
- [**RedHat Java OpenJDK 8 Download**](https://developers.redhat.com/download-manager/file/java-1.8.0-openjdk-1.8.0.265-3.b01.redhat.windows.x86_64.msi) (faça o download apenas se você não tiver, **_no mínimo_**, um JDK 8 instalado, seja da RedHat, Oracle ou outro fornecedor).

![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) **`Versões do Java 1.7 e anteriores não irão funcionar no projeto porque foram usados recursos do Java 1.8 !!!`**

### Documentação técnica:

[IMDbQueryProject Lucidchart Package Diagram](https://app.lucidchart.com/documents/view/0d56f59b-9c80-4575-a536-f7564f94275a/0_0)

> Nota para análise dos fontes: 
> As classes responsáveis pelo request Client Socket e response Server Socket são, respectivamente, **com.imdb.query.client.impl.IMDbClientSocketImpl** e **com.imdb.query.server.impl.IMDbClientHandler**.

[IMDbQueryProject Javadoc](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/tree/master/IMDbQueryProject/javadoc/com/imdb/query)

> OBS.: Para renderizar as páginas, é melhor acessá-las pelo Eclipse porque o github apenas visualiza o conteúdo html !


### Documentação para usuário final:

#### Tutorial para executar a solução pelo prompt de comando:

> OBS.: Se você for apenas executar os _*.jar_ abaixo e não for compilar os fontes, tenha instalado um JRE 8, **no mínimo**, e devidamente setado seu diretório \bin na variável de ambiente PATH. **Digite na linha de comando _java -version_ para checar se os requisitos para execução dos _*.jar_ estão sendo atendidos !**

1) Baixar da pasta os executáveis [**IMDbServerSocket.jar**](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/raw/master/executables/IMDbServerSocket.jar) e [**IMDbClientSocket.jar**](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/raw/master/executables/IMDbClientSocket.jar) num diretório local (Ex.: **C:\Temp**).

**2) Abrir uma instância do prompt de comando e executar o servidor socket (**C:\Temp\java -jar IMDbServerSocket.jar [porta]**).** 

   O argumento é opcional. Se for omitido o argumento, a porta padrão será 20222. 
   Ex.: 
        
        C:\Temp\java -jar IMDbServerSocket.jar  (executa na porta padrão 20222).
        
        C:\Temp\java -jar IMDbServerSocket.jar 32987 (executa na porta 32987).
        
>    **Vários servidores podem ser instanciados, cada um no seu prompt e na sua porta, para futuras conexões de clientes.** 

Exemplo de execução no Windows de dois servidores socketes, um na porta padrão 20222 e outro na porta 32987. Ambos esperando requisições dos clientes:

![Exemplo ServerSocket](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/blob/master/images/ServerSocket_Exemplo.jpg)

>    **Um servidor socket é instanciado numa Thread filha para que a Thread principal possa gerenciá-lo. E o servidor aloca uma Thread para cada atendimento de solicitação de cliente. Assim múltiplas conexões podem ser estabelecidas.**

>    **Se a porta estiver ocupada por outro processo, será feita tentativas de alocação pelo servidor socket até encontrar uma porta aberta. Essa porta aberta recém alocada pelo servidor socket será impressa no console para que o cliente saiba qual porta se conectar.**

3) Abrir outra instância do prompt de comando e executar o cliente socket 

   (**C:\Temp\java -jar IMDbClientSocket.jar [ipServidor | porta]**). 

   Os argumentos são opcionais. Se forem omititos o ipServidor será localhost e a porta padrão será 20222.
   Ex.: 
        
        C:\Temp\java -jar IMDbClientSocket.jar  (conecta no servidor em localhost e porta padrão 20222).
   
        C:\Temp\java -jar IMDbClientSocket.jar 32987 (conecta no servidor em localhost e porta 32987).
   
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 (conecta no servidor em 192.168.0.16 e porta padrão 20222).
        
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 32987 (conecta no servidor em 192.168.0.16 e porta 32987).
        
>   **Cada cliente deve ser executado em sua instância de prompt de comando para simular chamadas simultâneas.**

Exemplo de execução no Windows de dois clientes sockets, um na porta padrão e outro na porta 32987. Cada um requisitando para uma instância de servidor socket:

![Exemplo ClientSocket](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/blob/master/images/ClientSocket_Exemplo.jpg)

>   **Se o cliente tentar se conectar numa porta alocada por outro processo que não seja o servidor socket da solução, poderá haver travamento no caso da porta 135 (RPC), ou         rejeição no caso da porta 6969 (serviço acmsoda - cliente bittorrent) com a mensagem personalizada ('O protocolo de comunicação está inválido') para resposta de Bad Request desse acmsoda ou outros serviços afins.**

>   **Somente será aberta uma conexão com o servidor socket quando for solicitada uma busca de filme, e após a resposta do servidor, a conexão do cliente socket será encerrada para não mantê-la aberta desnecessariamente.**

**As pesquisas por filmes podem ser feitas pelo nome completo, ou qualquer parte do nome, do título do filme. Ex.:**

        Digitando 3 e teclando ENTTER, serão listados:
        3 Idiotas
        O 3 Homem
        Toy Story 3
   
        Digitando batman e teclando ENTTER, serão listados:
        Batman Begins
        Batman: O Cavaleiro das Trevas
        Batman: O Cavaleiro das Trevas Ressurge

O arquivo de log será registrado no subdiretório dos executáveis (ex.: **C:\Temp\log\IMDbQueryProject.log**).

Exemplo de execução no Windows de três clientes sockets concorrendo em requisições para um mesmo servidor socket na porta padrão 20222:

![Exemplo de comunicação](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/blob/master/images/Exemplo_Comunicacao.jpg)

Exemplo de execução no macOS de um cliente para um servidor socket:

![Exemplo de comunicação](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/blob/master/images/Exemplo_Comunicacao_macOS.jpg)

### Referências:

[Eclipse Community](https://www.eclipse.org/community/eclipse_newsletter/2018/february/buildship.php)

[Google Guice](https://riptutorial.com/guice)

[JUnit5 User Guide](https://junit.org/junit5/docs/current/user-guide)

[JUnit5 Gradle](https://www.baeldung.com/junit-5-gradle)

[Log4j 2](https://logging.apache.org/log4j/2.x/)

[Gson](https://github.com/google/gson/blob/master/UserGuide.md)

[IMDb WebApi](http://www.omdbapi.com/)
