package client;

import User.HostPage;
import User.UserInterface;
import User.UserPage;
import javafx.application.Platform;

import java.io.EOFException;
import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Client {

    //Host name or ip address
    private String host = "localhost";

    //Socket for establishing connection
    private Socket socket;
    //Object output stream to the server
    private ObjectOutputStream toServer;
    //Object input stream from the server
    private ObjectInputStream fromServer;

    private List<String> awaitingResponseList = Collections.synchronizedList(new ArrayList<>());
    private ConcurrentHashMap<String, String> responses = new ConcurrentHashMap<>();

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

    @SuppressWarnings("Duplicates")
    public boolean sendPacket(String packetIdentifier, int packetType, String playlistURI, String songURI, String lobby) {
        try {
            //Create a packet to send
            Packet packet = new Packet(packetIdentifier, packetType, playlistURI, songURI, lobby);
            toServer.writeObject(packet);
            System.out.println("Object sent to server...");
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("Duplicates")
    public String sendPacketWaitResponse(String packetIdentifier, int packetType, String playlistURI, String songURI, String lobby) {
        try {
            //Create a packet to send
            Packet packet = new Packet(packetIdentifier, packetType, playlistURI, songURI, lobby);
            toServer.writeObject(packet);
            System.out.println("Object sent to server...");
            awaitingResponseList.add(packetIdentifier);
            while(awaitingResponse()) {
                //wait for one second.
                try {
                    System.out.println("awaiting server response");
                    UserInterface.t.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return responses.get(packetIdentifier);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return null;
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

    public boolean awaitingResponse() {
        if(awaitingResponseList.isEmpty()) {
            return false;
        }
        return true;
    }

    //Use this method to switch through and handle different received packets
    public void handleReceivedPacket(Packet packet) {

        System.out.println("Received packet...");

        switch(packet.getPacketType()) {
            //packet type 0 = lobby creation
            case 0:
                System.out.println("Packet type: 0");
                if(packet.getLobby() != null) {
                    System.out.println("Lobby code: " + packet.getLobby());
                    System.out.println("Playlist Uri " + packet.getPlaylistURI());
                    responses.put(packet.getPacketIdentifier(), packet.getLobby());
                    awaitingResponseList.remove(packet.getPacketIdentifier());
                }
                break;
            //packet type 1 = lobby join
            case 1:
                System.out.println("Packet type: 1");
                responses.put(packet.getPacketIdentifier(), packet.getPlaylistURI());
                awaitingResponseList.remove(packet.getPacketIdentifier());
                break;
            //packet type 2 = song update
            case 2:
                System.out.println("Packet type: 2");


                if(HostPage.isInitialized()) {
                    Platform.runLater(() -> {
                        HostPage.updateQueue();
                    });
                }
                if(UserPage.isInitialized()) {
                    Platform.runLater(() -> {
                        UserPage.updateQueue();
                    });
                }

                break;
            case 3:
                System.out.println("Packet type: 3");
                String[] sName = new String[] {packet.getSongURI()};
                LobbyHost.addSong(sName);
                break;
            case 4:
                System.out.println("Packet type: 4");
                break;
            case 5:
                System.out.println("Packet type: 5");
                break;
            default:
                System.out.println("Packet Type Mismatch...");
                break;
        }

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
