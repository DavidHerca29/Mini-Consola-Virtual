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
    private int posXPacMan = 25;
    private int posYPacMan = 25;
    protected short direccion = 1;
    private boolean mapaConstruir = false;
    private Cliente cliente = new Cliente("192.168.1.124", 9999);

    public static void main(String[] args) {
        PacMan pacMan = new PacMan();

    }

    public PacMan() {

        EntradaPacMan entradaPacMan = new EntradaPacMan();
        iniciarJuego();
    }
    public void ponerPixel(int col, int fil, Color color){
        coloresPantalla[col][fil] = color;
    }

    public void mapLineaH(int columna, int columna1, int fila, Color color){
        if (columna==columna1){
            ponerPixel(columna, fila, color);
        }
        else if (columna<columna1){
            while (columna!=columna1){
                ponerPixel(columna, fila, color);
                columna++;
            }
        }
        else {
            while (columna!=columna1){
                ponerPixel(columna1, fila, color);
                columna1++;
            }
        }
    }
    public void mapLineaV(int columna, int fila, int fila1, Color color){
        if (fila==fila1){
            ponerPixel(columna, fila, color);
        }
        else if (fila<fila1){
            while (fila!=fila1){
                ponerPixel(columna, fila, color);
                fila++;
            }
        }
        else {
            while (fila!=fila1){
                ponerPixel(columna, fila1, color);
                fila1++;
            }
        }
    }
    public void mapRectangulo(int col1, int fil1, int col2, int fil2, Color color){
        if (col1==col2){ // en caso de que se envie de manera que es linea vertical
            mapLineaV(col1, fil1,fil2, color);
        }
        else if (fil1==fil2){
            mapLineaH(col1, col2, fil1, color);
        }
        else if (col1<col2){
            while (col1!=col2) {
                mapLineaV(col1, fil1, fil2, color);
                col1++;
            }
        }
        else {
            while (col1!=col2) {
                mapLineaV(col1, fil1, fil2, color);
                col1--;
            }
        }
    }
    public void iniciarMapa() {
        for (int i=1;i<49;i++){
            for (int j=1;j<49;j++){
                coloresPantalla[i][j] = Color.CYAN;
            }
        }
        // colocamos al pacman
        coloresPantalla[posXPacMan][posYPacMan] = Color.YELLOW;
        mapLineaH(0, 49, 0, Color.blue);
        mapLineaH(0, 50, 49, Color.blue);
        mapLineaV(0, 0, 49, Color.blue);
        mapLineaV(49, 0, 49, Color.blue);
        EnviarMapa();

    }
    public void iniciarJuego(){
        iniciarMapa();

        while (true){
            if (mapaConstruir) {
                movePacMan();
                enviarPacMan();
            }
        }
    }
    public void movePacMan(){
        coloresPantalla[posXPacMan][posYPacMan] = Color.black;
        enviarPacMan();
        if (direccion==1){
            posYPacMan-=1;
        }
        else if (direccion==2){
            posYPacMan+=1;
        }
        else if (direccion==3){
            posXPacMan-=1;
        }
        else posXPacMan+=1;
        if (posXPacMan>48){
            posXPacMan=48;
        }
        else if (posXPacMan<1){
            posXPacMan=1;
        }
        if (posYPacMan>48){
            posYPacMan=49;
        }
        else if (posYPacMan<1){
            posYPacMan=1;
        }
        actualizarPacMan();
    }
    public void actualizarPacMan(){
        coloresPantalla[posXPacMan][posYPacMan] = Color.YELLOW;
    }
    public void enviarPacMan(){
        cliente.enviarMensaje(posXPacMan,posYPacMan, coloresPantalla[posXPacMan][posYPacMan].getRed(), coloresPantalla[posXPacMan][posYPacMan].getGreen(), coloresPantalla[posXPacMan][posYPacMan].getBlue());
    }

    public void EnviarMapa() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                cliente.enviarMensaje(i,j, coloresPantalla[i][j].getRed(), coloresPantalla[i][j].getGreen(), coloresPantalla[i][j].getBlue());
            }
        }
        mapaConstruir = true;
    }

    class EntradaPacMan implements Runnable {

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

                    //System.out.println("Recibido: \n" + jsonObject.toString(2));
                    procesarMensaje(jsonObject);

                    //socketAux.close();
                    //flujoEntrada.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void procesarMensaje(JSONObject jsonObject){
            if (jsonObject.has("arriba")){
                direccion = 1;
            }else if (jsonObject.has("abajo")){
                direccion = 2;
            }
            else if (jsonObject.has("derecha")){
                direccion = 4;
            }
            else
                direccion = 3;
        }
    }
    /*
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