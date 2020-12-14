package Juego;

import Comms.Cliente;
import org.json.JSONObject;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PacMan {
    private static Color[][] coloresPantalla = new Color[50][50];
    int posXPacMan = 50;
    int posYPacMan = 50;


    public static void main(String[] args) {
        PacMan pacMan = new PacMan();
        EntradaPacMan entradaPacMan = new EntradaPacMan();

    }

    public PacMan() {
        iniciarMapa();
    }

    public void iniciarMapa() {
        for (int i = 0; i < 50; i++) {
            coloresPantalla[0][i] = Color.BLUE;
        }
        for (int i = 1; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (i == 1 && j == 0 || j == 24 || j == 25 || j == 26 || j == 49) {
                    coloresPantalla[i][j] = Color.BLUE;
                }
                // hay un cambio en el j==13
                else if (i == 2 || i == 3 || i == 4 || i == 5 || i == 6 && (j == 3 || j == 0 || j == 2 || j == 4 || j == 5 || j == 6 || j == 7 || j == 9 || j == 10 || j == 8 || j == 12 || j == 14 || j == 15 || j == 16 || j == 17 || j == 18 || j == 19 || j == 20 || j == 21 || j == 22 || j == 24 || j == 25 || j == 26 || j == 27 || j == 29 || j == 30 || j == 31 || j == 32 || j == 33 || j == 34 || j == 35 || j == 36 || j == 37 || j == 39 || j == 40 || j == 41 || j == 42 || j == 43 || j == 44 || j == 45 || j == 46 || j == 47 || j == 49)) {
                    coloresPantalla[i][j] = Color.BLUE;
                } else if (i == 8 || i == 9 || i == 10 && (j == 3 || j == 0 || j == 2 || j == 4 || j == 5 || j == 6 || j == 7 || j == 9 || j == 10 || j == 8 || j == 12 || j == 14 || j == 15 || j == 16 || j == 18 || j == 19 || j == 20 || j == 21 || j == 22 || j == 23 || j == 24 || j == 25 || j == 26  || j == 28 || j == 29 || j == 30 || j == 31 || j == 33 || j == 34 || j == 35 || j == 36 || j == 37 || j == 39 || j == 40 || j == 41 || j == 43 || j == 44 || j == 45 || j == 46 || j == 47 || j == 49)) {
                    coloresPantalla[i][j] = Color.BLUE;
                } else
                    coloresPantalla[i][j] = Color.BLACK;
            }
        }
        Runnable r = new EnviarMapa();
        Thread nuevoHilo = new Thread(r);
        nuevoHilo.start();
    }

    static class EnviarMapa implements Runnable {
        public EnviarMapa() {
        }

        @Override
        public void run() {
        Cliente cliente;
            String llave;
            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 50; j++) {
                    llave = i +","+ j;
                    cliente = new Cliente("192.168.1.124", 9999, llave, coloresPantalla[i][j].toString());
                }
            }
        }
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
    }/*
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
    }*/
}