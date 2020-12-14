package Comms;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
    private String ip;
    private int port;
    private String llave;
    private String color;

    public Cliente(String ip, int port, String llave, String color) {
        this.ip = ip;
        this.port = port;
        this.llave = llave;
        this.color = color;
        enviarMensaje();
    }
    public void enviarMensaje(){
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream flujoSalida =  new DataOutputStream(socket.getOutputStream());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(llave, color);
            flujoSalida.writeUTF(jsonObject.toString());
            //flujoSalida.close();
            //socket.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println(ioException.getMessage());
        }
    }
}
