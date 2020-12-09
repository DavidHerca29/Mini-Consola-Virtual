import org.json.JSONObject;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PacMan {
    private static Color[][] coloresPantalla = new Color[50][50];
    int posXPacMan=50;
    int posYPacMan=50;

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (i == 1) {
                    coloresPantalla[i][j] = Color.BLUE;
                }
                else
                    coloresPantalla[i][j] = Color.BLACK;
                SalidaPacMan salidaPacMan = new SalidaPacMan(i, j, coloresPantalla[i][j].toString());
            }
        }
        EntradaPacMan entradaPacMan = new EntradaPacMan();

    }

    static class EntradaPacMan implements Runnable {

        public EntradaPacMan() {

            Thread hilo1 = new Thread(this);
            hilo1.start();
        }

        @Override
        public void run() {
            try {
                ServerSocket servidorPacMan = new ServerSocket(9997);

                while (true) {
                    Socket socketAux = servidorPacMan.accept();
                    DataInputStream flujoEntrada = new DataInputStream(socketAux.getInputStream());

                    String texto = flujoEntrada.readUTF();
                    JSONObject jsonObject = new JSONObject(texto);
                    System.out.println("Recibido: \n" + jsonObject.toString(2));


                    socketAux.close();
                    flujoEntrada.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static class SalidaPacMan implements Runnable{
        int columna;
        int fila;
        String color;
        public SalidaPacMan(int columnaP, int filaP, String colorP) {
            columna = columnaP;
            fila = filaP;
            color = colorP;
            Thread hilo1 = new Thread(this);
            hilo1.start();
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket("192.168.1.124", 9999);
                DataOutputStream flujoSalida =  new DataOutputStream(socket.getOutputStream());

                JSONObject jsonObject = new JSONObject();
                String llave = String.valueOf(columna)+","+String.valueOf(fila);
                jsonObject.put(llave, color);

                flujoSalida.writeUTF(jsonObject.toString());
                flujoSalida.close();
                //socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}