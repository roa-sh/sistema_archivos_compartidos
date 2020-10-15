package Peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Peer {
	
	public static final int PORT = 3400;
	public static final String SERVER = "localhost";
	
	public static final String SHARE_FOLDER = "compartida/peer1"; 
	public static final String DOWNLOAD_FOLDER = "descargado/peer1";
	
	private int transferListeningPort;
	private int transferRequestingPort;
	
	private Socket peerSideSocket;
	private ServerSocket listener;
	
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	
	//private PeerThread peerThread;
	
	
	// Metodo constructor.
	public Peer() {
		System.out.println("PEER CONNECTION ...");

		try {
			peerSideSocket = new Socket(SERVER, PORT);
			
			//peerThread = new PeerThread(3401);
			//peerThread.start();
			
			writer = new ObjectOutputStream(peerSideSocket.getOutputStream());
			reader = new ObjectInputStream(peerSideSocket.getInputStream());
			
			
			String message = new Scanner(System.in).nextLine();
			if (message.equals("REGISTER")){
			/*/System.out.println(message);
			String personas = "a";*/

				writer.writeObject(message);

				ArrayList<String> files = new ArrayList<String>();
				files.add("reboñeñe.mp4");
				files.add("lamento_boliviano.mp3");
				files.add("cancun.jpg");

				// Envio del mensaje al servidor.
				writer.writeObject(files);
				writer.flush();

				// Lectura del mensaje que el servidor le envia al cliente.
				String receivedMessage = (String)reader.readObject();

				// Impresion del mensaje recibido en la consola.
				System.out.println("REGISTRADOS: " + receivedMessage);
				peerSideSocket.close();

			}
			else if(message.equals("REQUEST")){
				writer.writeObject(message);

				writer.writeObject("cancun.jpg");
				// Lectura del mensaje que el servidor le envia al cliente.
				String receivedMessage = (String)reader.readObject().toString();

				// Impresion del mensaje recibido en la consola.
				System.out.println("IPS con el archivo solicitado: " + receivedMessage);
				peerSideSocket.close();

			}


		}
		// Puede lanzar una excepcion de host desconocido.
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// Puede lanzar una excepcion de entrada y salida.
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Finalmente se cierran los flujos y el socket.
		finally {
			try {
				reader.close();
				writer.close();
//				peerSideSocket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	 public synchronized void stopThread() {
		 //peerThread.setStop(true);
	 }

	
	// Este metodo se encarga de conectar los flujos de entrada y salida de
	// caracteres con el socket que ha establecido conexion con el servidor.
	private void createStreams() throws IOException {
		// Creacion del flujo de salida de datos al cual se le conecta el flujo
		// salida del socket. Este flujo de salida de datos se utiliza para
		// enviar un flujo de caracteres al servidor.
		writer = new ObjectOutputStream(peerSideSocket.getOutputStream());

		// Creacion del buffer de lectura al cual se le conecta un lector de un
		// flujo de entrada que a su vez se conecta con el flujo de entrada del
		// socket. Este flujo de entrada se utiliza para leer un flujo
		// de caractere que viene del servidor.
		reader = new ObjectInputStream(peerSideSocket.getInputStream());
	}
		
		
	/**
	 * Metodo principal de la aplicacion.
	 */
	public static void main(String args[]) {
		new Peer();
	}
	
}