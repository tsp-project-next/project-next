package client;

import java.io.Serializable;

public class Packet implements Serializable {

    private String data;

    public Packet(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
