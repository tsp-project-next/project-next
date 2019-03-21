package client;

import java.io.Serializable;

public class Packet implements Serializable {

    private String clientIdentifier;

    private int packetType;

    private String playlistURI;

    private String songURI;

    private String lobby;

    private int flag;

    public Packet(String cId, int packetType) {
        this.clientIdentifier = cId;
        this.packetType = packetType;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setLobby(String str) {
        lobby = str;
    }

    public String getLobby() {
        return lobby;
    }

    public void setSongURI(String str) {
        songURI = str;
    }

    public String getSongURI() {
        return songURI;
    }

    public void setPlaylistURI(String str) {
        playlistURI = str;
    }

    public String getPlaylistURI() {
        return playlistURI;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public int getPacketType() {
        return packetType;
    }

}
