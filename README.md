# EmitesJavaJobApplicationChallenge
## Aplicação console em Java para consultas via socket de títulos de filmes no site IMDB.

Tutorial para avaliação da solução:

1) Baixar da pasta https://github.com/fbentes/EmitesJavaJobApplicationChallenge/tree/master/executables os arquivos **IMDbServerSocket.jar** e **IMDbClientSocket.jar** num diretório local (Ex.: C:\Temp).

2) Abrir uma instância do prompt de comando e executar o servidor socket (C:\Temp\java -jar IMDbServerSocket.jar [porta]). 
   O argumento é opcional. Se for omitido o argumento, a porta padrão será 20222. 
   Ex.: 
        
        C:\Temp\java -jar IMDbServerSocket.jar  (executa na porta padrão 20222).
        
        C:\Temp\java -jar IMDbServerSocket.jar 32987 (executa na porta 32987).
        
*    Vários servidores podem ser instanciados, cada um no seu prompt e na sua porta, para futuras conexões de clientes.     

*    Se a porta estiver ocupada por outro processo, será feito tentativas de alocação até encontrar uma porta aberta. Essa porta será impressa no console para que o cliente      saiba qual porta se conectar.

3) Abrir outra instância do prompt de comando e executar o cliente socket (C:\Temp\java -jar IMDbClientSocket.jar [porta] | [ipServidor] [porta]). 

   Ex.: 
        
        C:\Temp\java -jar IMDbClientSocket.jar  (conecta no servidor em localhost e porta padrão 20222).
   
        C:\Temp\java -jar IMDbClientSocket.jar 34985 (conecta no servidor em localhost e porta 34985).
   
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 (conecta no servidor em 192.168.0.16 e porta padrão 20222).
        
        C:\Temp\java -jar IMDbClientSocket.jar 192.168.0.16 33845 (conecta no servidor em 192.168.0.16 e porta 33845).
        
   Cada cliente deve ser executado em sua instância do prompt para simular chamadas simultâneas;

*    Os argumentos são opcionais. Se forem omititos o ip será localhost e a porta padrão será 20222.
   

Referências:

Java openjdk version "1.8.0_41"

https://developers.redhat.com/download-manager/file/java-1.8.0-openjdk-1.8.0.265-3.b01.redhat.windows.x86_64.msi

Version: 2020-06 (4.16.0)

https://www.eclipse.org/downloads/  

https://www.eclipse.org/community/eclipse_newsletter/2018/february/buildship.php

https://riptutorial.com/guice

https://junit.org/junit5/docs/current/user-guide

https://www.baeldung.com/junit-5-gradle
