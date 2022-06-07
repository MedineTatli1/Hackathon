package com.hackathon.hackathon.main;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NodeInfo;
import net.corda.core.utilities.NetworkHostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import static net.corda.core.utilities.NetworkHostAndPort.parse;

@SpringBootApplication
public class HackathonApplication {
	private static final Logger logger = LoggerFactory.getLogger(HackathonApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HackathonApplication.class, args);
		if (args.length != 3) throw new IllegalArgumentException("Usage: Client <node address> <rpc username> <rpc password>");
		final NetworkHostAndPort nodeAddress = parse("localhost:10009");
		final String rpcUsername = "user";
		final String rpcPassword = "test";
		final CordaRPCClient client = new CordaRPCClient(nodeAddress);
		final CordaRPCConnection clientConnection = client.start(rpcUsername, rpcPassword);
		final CordaRPCOps proxy = clientConnection.getProxy();

		// Interact with the node.
		// Example #1, here we print the nodes on the network.
		final List<NodeInfo> nodes = proxy.networkMapSnapshot();
		System.out.println("\n-- Here is the networkMap snapshot --");
		logger.info("{}", nodes);

		// Example #2, here we print the PartyA's node info
		CordaX500Name name = proxy.nodeInfo().getLegalIdentities().get(0).getName();//nodeInfo().legalIdentities.first().name
		System.out.println("\n-- Here is the node info of the node that the client connected to --");
		logger.info("{}", name);

		//Close the client connection
		clientConnection.close();
	}

}
