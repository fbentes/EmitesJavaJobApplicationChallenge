# IMDbQueryProject
## Aplicação console em Java para consultas via socket de títulos de filmes no site IMDb.
### Ambiente de desenvolvimento adotado:

- **Windows 10 Home**
- [**Eclipse IDE for Java Developers Version: 2020-06 (4.16.0) Download**](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2020-06/R/eclipse-inst-win64.exe&mirror_id=576)
- [**RedHat Java OpenJDK 8 Download**](https://developers.redhat.com/download-manager/file/java-1.8.0-openjdk-1.8.0.265-3.b01.redhat.windows.x86_64.msi) (faça o download apenas se você não tiver, **_no mínimo_**, um JDK 8 instalado, seja da RedHat, Oracle ou outro fornecedor).

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

2) Abrir uma instância do prompt de comando e executar o servidor socket (**C:\Temp\java -jar IMDbServerSocket.jar [porta]**). 

   O argumento é opcional. Se for omitido o argumento, a porta padrão será 20222. 
   Ex.: 
        
        C:\Temp\java -jar IMDbServerSocket.jar  (executa na porta padrão 20222).
        
        C:\Temp\java -jar IMDbServerSocket.jar 32987 (executa na porta 32987).
        
>    Vários servidores podem ser instanciados, cada um no seu prompt e na sua porta, para futuras conexões de clientes. 

Exemplo de execução:

![Exemplo ServerSocket](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/blob/master/images/ServerSocket_Exemplo.jpg)

>    Um servidor socket é instanciado numa Thread filha para que a Thread principal possa gerenciá-lo. E o servidor aloca uma Thread para cada atendimento de solicitação de cliente. Assim múltiplas conexões podem ser estabelecidas.

>    Se a porta estiver ocupada por outro processo, será feita tentativas de alocação pelo servidor socket até encontrar uma porta aberta. Essa porta aberta recém alocada pelo servidor socket será impressa no console para que o cliente saiba qual porta se conectar.

3) Abrir outra instância do prompt de comando e executar o cliente socket 

   (**C:\Temp\java -jar IMDbClientSocket.jar [ipServidor | porta]**). 

   Os argumentos são opcionais. Se forem omititos o ipServidor será localhost e a porta padrão será 20222.
   Ex.: 
        
        C:\Temp\java -jar IMDbClientSocket.jar  (conecta no servidor em localhost e porta padrão 20222).
   
        C:\Temp\java -jar IMDbClientSocket.jar 34985 (conecta no servidor em localhost e porta 34985).
   
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 (conecta no servidor em 192.168.0.16 e porta padrão 20222).
        
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 33845 (conecta no servidor em 192.168.0.16 e porta 33845).
        
>   **Cada cliente deve ser executado em sua instância de prompt de comando para simular chamadas simultâneas.**

![Exemplo ClientSocket](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/blob/master/images/ClientSocket_Execucao.jpg)

>   Se o cliente tentar se conectar numa porta alocada por outro processo que não seja o servidor socket da solução, poderá haver travamento no caso da porta 135 (RPC), ou         rejeição no caso da porta 6969 (serviço acmsoda - cliente bittorrent) com a mensagem personalizada ('O protocolo de comunicação está inválido') para resposta de Bad Request desse acmsoda ou outros serviços afins.

>   **As pesquisas por títulos de filmes podem ser feitas pelo nome completo, ou pelo início do nome, do título do filme (Ex.: 'Batman' para retornar todos os filmes que comecem por essa palavra)**.

![Exemplo de comunicação](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/blob/master/images/Exemplo_Comunicacao.jpg)

O arquivo de log será registrado no subdiretório dos executáveis (ex.: **C:\Temp\log\IMDbQueryProject.log**).

### Referências:

[Eclipse Community](https://www.eclipse.org/community/eclipse_newsletter/2018/february/buildship.php)

[Google Guice](https://riptutorial.com/guice)

[JUnit5 User Guide](https://junit.org/junit5/docs/current/user-guide)

[JUnit5 Gradle](https://www.baeldung.com/junit-5-gradle)

[Log4j 2](https://logging.apache.org/log4j/2.x/)
