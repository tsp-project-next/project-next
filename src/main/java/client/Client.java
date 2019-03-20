package client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import javafx.application.Application;

import java.io.EOFException;
import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {

    //Host name or ip address
    private String host = "localhost";

    //Socket for establishing connection
    private Socket socket;
    //Object output stream to the server
    private ObjectOutputStream toServer;
    //Object input stream from the server
    private ObjectInputStream fromServer;

    public Client() {
        System.out.println("client created...");

        //Establish connection with server
        if(establishConnection(host) == false) {
            return;
        }

        //establish object data streams with server
        if(establishObjectStreams(socket) == false) {
            return;
        }

        //create a new thread to read in all new information from the server
        new Thread(new HandleServer(socket, fromServer)).start();

        //Send a test packet to the server
        sendPacket("Testing", "host");
    }

    public boolean establishConnection(String host) {
        try {
            socket = new Socket(host, 9000);
            System.out.println("Socket connected");
            return true;
        } catch (ConnectException ex) {
            System.out.println("No server connection found.");
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean establishObjectStreams(Socket socket) {
        try {
            //Output stream has to be created before input stream.
            //Create and object output stream to the server
            toServer = new ObjectOutputStream(socket.getOutputStream());
            //Create and object input stream to the server
            fromServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("Streams established");
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();
            System.out.println("failed to establish object streams");
            return false;
        }
    }

    public boolean sendPacket(String data, String cid) {
        try {
            //Create a packet to send
            Packet packet = new Packet(cid);
            toServer.writeObject(packet);
            System.out.println("Object sent to server...");
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void closeStreams() {
        try {
            System.out.println("Attempting to close streams...");
            if(fromServer != null) fromServer.close();
            if(toServer != null) toServer.close();
            if(socket != null) socket.close();
            System.out.println("Streams closed.");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isConnectionEstablished() {
        if(socket != null) return true;
        return false;
    }

    public boolean isStreamEstablished() {
        if(toServer != null && fromServer != null) return true;
        return false;
    }

    //Use this method to switch through and handle different received packets
    public void handleReceivedPacket(Packet packet) {

        // Need to determine what kind of packet we have and what we want to do with it

        System.out.println("Received packet with data: ");
    }

    class HandleServer implements Runnable {

        //The connected socket
        private Socket socket;
        private ObjectInputStream fromServer;

        public HandleServer(Socket socket, ObjectInputStream fromServer) {
            this.socket = socket;
            this.fromServer = fromServer;
        }

        @Override
        public void run() {
            try {
                System.out.println("New thread created for socket: " + socket.getInetAddress().getHostName());

                while(true){
                    //Read from input
                    Packet packetReceived = (Packet) fromServer.readObject();

                    //Handle the packet read from the server
                    handleReceivedPacket(packetReceived);
                }
            } catch(ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch(SocketException ex) {
                System.out.println("Connection reset/closed for client: " + socket.getInetAddress().getHostName());
                return;
            } catch(EOFException ex) {
                // System.out.println("We're catching this in the final block....");
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
