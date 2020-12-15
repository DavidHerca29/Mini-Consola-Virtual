package Comms;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
    private String ip;
    private int port;

    public Cliente(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    public void enviarMensaje(String llave, String color){
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
    public void enviarMensaje(int columna, int fila, int red, int green, int blue){
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream flujoSalida =  new DataOutputStream(socket.getOutputStream());

            JSONArray jsonObject = new JSONArray();
            jsonObject.put(0, columna);
            jsonObject.put(1, fila);
            jsonObject.put(2, red);
            jsonObject.put(3, green);
            jsonObject.put(4, blue);
            flujoSalida.writeUTF(jsonObject.toString());
            //flujoSalida.close();
            //socket.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println(ioException.getMessage());
        }
    }

}
