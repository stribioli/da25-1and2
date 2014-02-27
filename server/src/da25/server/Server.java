package da25.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import da25.base.NetworkInterface;
import da25.base.ProcessInterface;

/**
 * Main class for server, holding RMI logic only.
 * 
 * @author Stefano Tribioli
 * @author Casper Folkers
 *
 */
public class Server {
	public static Network network;

	public static void main(String[] args) {
		System.setProperty("java.rmi.server.codebase", NetworkInterface.class.getProtectionDomain().getCodeSource().getLocation().toString());
		
		network = new Network();
		
		try {
			NetworkInterface stub = (NetworkInterface) UnicastRemoteObject.exportObject(network, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(NetworkInterface.class.getCanonicalName(), stub);
		} catch (RemoteException e) {
			System.out.println("Unable to init RMI environment.");
			throw new RuntimeException(e);
		}
		
		System.out.println("Network is running, waiting for clients.");
		
		try {
			int c = System.in.read();
			if (c == 's') {
				for (ProcessInterface process : network.processes) {
					process.start();
				}
			}
		} catch (IOException e) {}
		
		
	}

}
