package com.imdb.query.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.imdb.query.server.IMDbServerSocket;
import com.imdb.query.server.ServerCommandThread;
import com.imdb.query.server.impl.ServerCommandThreadImpl;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
import com.imdb.query.util.network.TCPPortUtility;

/**
 *  Classe responsável para iniciar o servidor Socket no prompt de comando.
 *  
 * Tutorial para execução:
 * 
 * C:\Temp\java -jar IMDbServerSocket.jar [porta]
 * 
 * O argumento é opcional. Se for omitido o argumento, a porta padrão será 20222.  
 * 
 * C:\Temp\java -jar IMDbServerSocket.jar  (executa na porta padrão 20222).
 * 
 * C:\Temp\java -jar IMDbServerSocket.jar 32987 (executa na porta 32987).
 * 
 * - Vários servidores podem ser instanciados, cada um no seu prompt e na sua porta, para futuras conexões de clientes.
 *
 * - Um servidor socket é instanciado numa Thread filha para que a Thread principal possa gerenciá-lo. 
 *   E o servidor aloca uma Thread para cada atendimento de solicitação de cliente. Assim múltiplas conexões podem ser estabelecidas.
 *
 * - Se a porta estiver ocupada por outro processo, será feita tentativas de alocação pelo servidor socket até encontrar uma porta aberta. 
 *   Essa porta aberta recém alocada pelo servidor socket será impressa no console para que o cliente saiba qual porta se conectar.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 09/08/2020
 * 
 */
public class StartServer extends StartBase {

    private static final Logger logger;
    
    static {
    	
    	disableAccessWarnings();
    	
    	System.out.println("");
    	System.out.println("Iniciando mecanismo de log, aguarde ...");
    	
    	logger = LogManager.getLogger(StartServer.class);
    }
    
    private static int port = Constants.PORT_DEFAULT;
    
	@Inject
	private IMDbServerSocket imdbServerSocket;
	
	/**
	 * Tenta conectar na porta padrão 20222. Caso já tenha sido alocada por conexão desse 
	 * servidor socket ou algum outro processo, há a tentativa para conexão em outra porta disponível.
	 * Sendo assim, torna-se possível o atendimento de solicitações dos clientes por diferentes portas
	 * conectadas.
	 * 
	 * @param args Porta para conexão socket. Se não for informada, a porta darão 20222 será usada.
	 * @throws InterruptedException Disparado caso haja algum problema na chamada de thread.join(3000).
	 * @throws IOException Disparado caso haja algum problema na chamada de System.in.read().
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
			
		if(!readArguments(args)) {
			return;
		}

		StartServer startServer = new StartServer();
		
		// Infeção de dependência para a instância de StartServer
		
		IMDbQueryModuleInjector.initialize(startServer);
		
        // Iniciando o servidor TCP Socket para esperar solicitações do(s) cliente(s)
		// Cada instância do servidor socket iniciará numa thread única, podendo assim,
		// ser parada pelo usuário na thread principal a qualquer momento.
        
    	ServerCommandThread serverCommandThread = new ServerCommandThreadImpl(startServer.imdbServerSocket);
    	
    	startServer.imdbServerSocket.setPort(port);
    	
    	serverCommandThread.start();

    	synchronized(serverCommandThread){
    		
       		serverCommandThread.wait();
    	}

    	do {
			
			System.out.print("(Digite kill para parar o servidor)");	
		    logger.info(Constants.STRING_EMPTY);

		    // Só para o servidor se for digitado, literalmente, "quit" !
		    
	        BufferedReader movieTitleBufferedReader =  
	                new BufferedReader(new InputStreamReader(System.in)); 
	      
	        Optional<String> readKeyboard = Optional.empty();
	        
			try {
				
				readKeyboard = Optional.ofNullable(movieTitleBufferedReader.readLine());
				
			} catch (IOException e) {
				
				logger.info("Problema na leitura do teclado: " + e.getMessage());
				
				readKeyboard = Optional.of("kill");
			} 

		    if(readKeyboard.isPresent() && readKeyboard.get().toLowerCase().trim().equals("kill")) {
		    	
				startServer.imdbServerSocket.requestStopExecution();
				
				break;
		    }

	    } while (true);
			    
	    if(startServer.imdbServerSocket.closeServerSocket()) {
			
	    	serverCommandThread.interrupt();
	    	
			logger.info(Constants.STRING_EMPTY);
			logger.info("Servidor parado pelo usuário !!!");
		}	
	}
	
	/**
	 * Captura a porta informada na linha de comando. Caso não seja  
	 * informado argumento, a porta padrão 20222 será usada.
	 * 
	 * Ex.: C:\java -jar IMDbServerSocket.jar 23908
	 * 
	 * @param args Porta para conexão socket.
	 * @return Verdadeiro se não foi informado argumento ou se uma porta válida foi informada. Falso caso contrário.
	 */
	private static boolean readArguments(String[] args) {
		
		Optional<String[]> optionalArgs = Optional.ofNullable(args); 

		if(!optionalArgs.isPresent() || optionalArgs.get().length == 0) { // Se args == null ou args.length == 0.
			
			return true;  // continua com port = Constants.PORT_DEFAULT;
		}
		
		if(optionalArgs.get().length > 1) {  // Usuário informou a porta de conexão pela linha de comando.
			
			logger.info("Não pode haver mais de um argumento !");
			
			return false;
		}
		
		Optional<String> optionalFirstArg = Optional.of(args[0]);

		TCPPortUtility tcpPortUtility = new TCPPortUtility();
		
		if(tcpPortUtility.isPortValid(optionalFirstArg.get())) {
			
			port = Integer.parseInt(optionalFirstArg.get());
			
			return true;
		} 
		
		logger.info(String.format("A porta %s é inválida. O número máximo permitido é %d !", optionalFirstArg.get(), Constants.PORT_TCP_MAX));
		
		return false;
	}
	
}
