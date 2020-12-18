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
    public void enviarMensaje(int puntaje){
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream flujoSalida =  new DataOutputStream(socket.getOutputStream());

            JSONArray jsonObject = new JSONArray();
            jsonObject.put(0, puntaje);
            flujoSalida.writeUTF(jsonObject.toString());
            flujoSalida.close();
            socket.close();

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
            flujoSalida.close();
            socket.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println(ioException.getMessage());
        }
    }
    public void enviarLineaH(int columna, int columna1, int fila, int red, int green, int blue){
        if (columna==columna1){
            enviarMensaje(columna, fila, red, green, blue);
        }
        else if (columna<columna1){
            while (columna!=columna1){
                enviarMensaje(columna, fila, red, green, blue);
                columna++;
            }
        }
        else {
            while (columna!=columna1){
                enviarMensaje(columna1, fila, red, green, blue);
                columna1++;
            }
        }
    }
    public void enviarLineaV(int columna, int fila, int fila1, int red, int green, int blue){
        if (fila==fila1){
            enviarMensaje(columna, fila, red, green, blue);
        }
        else if (fila<fila1){
            while (fila!=fila1){
                enviarMensaje(columna, fila, red, green, blue);
                fila++;
            }
        }
        else {
            while (fila!=fila1){
                enviarMensaje(columna, fila1, red, green, blue);
                fila1++;
            }
        }
    }
    public void enviarRectangulo(int col1, int fil1, int col2, int fil2, int red, int green, int blue){
        if (col1==col2){ // en caso de que se envie de manera que es linea vertical
            enviarLineaV(col1, fil1,fil2, red, green, blue);
        }
        else if (fil1==fil2){
            enviarLineaH(col1, col2, fil1, red, green, blue);
        }
        else if (col1<col2){
            while (col1!=col2) {
                enviarLineaV(col1, fil1, fil2, red, green, blue);
                col1++;
            }
        }
        else {
            while (col1!=col2) {
                enviarLineaV(col1, fil1, fil2, red, green, blue);
                col1--;
            }
        }
    }
}
