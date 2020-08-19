# IMDbQueryProject
## Aplicação console em Java para consultas via socket de títulos de filmes no site IMDb.
> Eclipse IDE for Java Developers Version: 2020-06 (4.16.0)

Uso do [RedHat OpenJDK 8 Download](https://developers.redhat.com/download-manager/file/java-1.8.0-openjdk-1.8.0.265-3.b01.redhat.windows.x86_64.msi)

> OBS.: Suspendi o uso do Oracle JDK 14 porque ele dispara uma exceção quando há tentativa de conexão com algum site https: **_Exception in thread "main" javax.net.ssl.SSLException: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty_**.
 Por não ser o foco do desafio resolver essa questão, foi usado o RedHat OpenJDK 8 que não tem essa restrição de segurança !

### Documentação técnica:

[IMDbQueryProject Lucidchart Package Diagram](https://app.lucidchart.com/documents/view/0d56f59b-9c80-4575-a536-f7564f94275a/0_0)

[IMDbQueryProject Javadoc](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/tree/master/IMDbQueryProject/javadoc/com/imdb/query)

> OBS.: Para renderizar as páginas, é melhor acessá-las pelo Eclipse porque o github apenas visualiza o conteúdo html !


### Documentação para usuário final:

#### Tutorial para executar a solução pelo prompt de comando:


1) Baixar da pasta [Executables](https://github.com/fbentes/EmitesJavaJobApplicationChallenge/tree/master/executables) os arquivos **IMDbServerSocket.jar** e **IMDbClientSocket.jar** num diretório local (Ex.: C:\Temp).

2) Abrir uma instância do prompt de comando e executar o servidor socket (**C:\Temp\java -jar IMDbServerSocket.jar [porta]**). 

   O argumento é opcional. Se for omitido o argumento, a porta padrão será 20222. 
   Ex.: 
        
        C:\Temp\java -jar IMDbServerSocket.jar  (executa na porta padrão 20222).
        
        C:\Temp\java -jar IMDbServerSocket.jar 32987 (executa na porta 32987).
        
*    Vários servidores podem ser instanciados, cada um no seu prompt e na sua porta, para futuras conexões de clientes.     

*    Se a porta estiver ocupada por outro processo, será feito tentativas de alocação até encontrar uma porta aberta. Essa porta será impressa no console para que o cliente      saiba qual porta se conectar.

3) Abrir outra instância do prompt de comando e executar o cliente socket 

   (**C:\Temp\java -jar IMDbClientSocket.jar [ipServidor | porta]**). 

   Os argumentos são opcionais. Se forem omititos o ipServidor será localhost e a porta padrão será 20222.
   Ex.: 
        
        C:\Temp\java -jar IMDbClientSocket.jar  (conecta no servidor em localhost e porta padrão 20222).
   
        C:\Temp\java -jar IMDbClientSocket.jar 34985 (conecta no servidor em localhost e porta 34985).
   
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 (conecta no servidor em 192.168.0.16 e porta padrão 20222).
        
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 33845 (conecta no servidor em 192.168.0.16 e porta 33845).
        
*   Cada cliente deve ser executado em sua instância de prompt de comando para simular chamadas simultâneas.

O arquivo de log será registrado no subdiretório dos executáveis (ex.: **C:\Temp\log\IMDbQueryProject.log**).

### Referências:

[Eclipse Version: 2020-06 (4.16.0) Download](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2020-06/R/eclipse-inst-win64.exe&mirror_id=576)

[Eclipse Community](https://www.eclipse.org/community/eclipse_newsletter/2018/february/buildship.php)

[Google Guice](https://riptutorial.com/guice)

[JUnit5 User Guide](https://junit.org/junit5/docs/current/user-guide)

[JUnit5 Gradle](https://www.baeldung.com/junit-5-gradle)

[Log4j 2](https://logging.apache.org/log4j/2.x/)
