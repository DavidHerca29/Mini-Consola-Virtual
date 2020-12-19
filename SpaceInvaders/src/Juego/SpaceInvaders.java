package Juego;

import Comms.Cliente;
import org.json.JSONObject;

import javax.security.auth.x500.X500Principal;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SpaceInvaders {
    private static Color[][] coloresPantalla = new Color[50][50];
    private static Color[] coloresPosibles = new Color[15];
    private static ArrayList<Disparo> disparos = new ArrayList<Disparo>();
    private int posXNave = 25;
    private int posYNave = 47;
    protected short direccion = 3;
    private boolean mapaConstruir = false;
    private Cliente cliente = new Cliente("192.168.1.124", 9999);
    private boolean jugando = true;
    private boolean moverNave = false;
    private boolean disparando = false;
    private int puntaje = 0;
    private final int MAX_ALIENS = 40;
    private ArrayList<Alien> aliens = new ArrayList<Alien>();

    public static void main(String[] args) {
        SpaceInvaders spaceInvaders = new SpaceInvaders();

    }
    public SpaceInvaders() {
        iniciarColores();
        EntradaSpaceInv entradaSpaceInv = new EntradaSpaceInv();
        iniciarJuego();
    }

    private void iniciarColores(){
        coloresPosibles = new Color[]{
                Color.blue, Color.red, Color.white, Color.gray, Color.CYAN, Color.yellow, Color.green, Color.MAGENTA, Color.pink, Color.darkGray, Color.LIGHT_GRAY, Color.orange, new Color(90, 5, 147), new Color(212, 78, 25, 233), new Color(108, 31, 31, 255)
        };
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
                coloresPantalla[i][j] = Color.black;// iniciamos los colores en negro
            }
        }
        // colocamos los obstaculos/marcianos
        colocarMarcianos();

        coloresPantalla[posXNave][posYNave] = Color.red;
        coloresPantalla[posXNave-1][posYNave] = Color.red;
        coloresPantalla[posXNave+1][posYNave] = Color.red;
        coloresPantalla[posXNave][posYNave-1] = Color.red;
        //coloresPantalla[posXPacMan][posYPacMan-15] = Color.white; // sirve para verficar rapido que si funciona para determinar si se gana
        mapLineaH(0, 49, 0, Color.blue);
        mapLineaH(0, 50, 49, Color.blue);
        mapLineaV(0, 0, 49, Color.blue);
        mapLineaV(49, 0, 49, Color.blue);
        EnviarMapa();

    }
    private void colocarMarcianos(){
        int cantidad =(int) Math.floor(Math.random() * (MAX_ALIENS)+20);
        int posicionX;
        int posicionY;
        int color;
        for (int i = 0; i<cantidad; i++){
            posicionX = (int) Math.floor(Math.random() * (46))+2;
            posicionY = (int) Math.floor(Math.random() * (39))+1;
            if (validarPosAlien(posicionX, posicionY)) {
                color = (int) Math.floor(Math.random() * (15));
                coloresPantalla[posicionX][posicionY] = coloresPosibles[color];
                aliens.add(new Alien(posicionX, posicionY, coloresPosibles[color]));
            }
            else
                i--;
        }

    }
    private boolean validarPosAlien(int posicionX, int posicionY){
        if (coloresPantalla[posicionX][posicionY]!=Color.black){
            return false;
        }
        return true;
    }
    public void iniciarJuego(){
        iniciarMapa();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mapaConstruir) {
                    if (jugando) {
                        if (moverNave){
                            moveNave();
                            moverNave=false;
                        }
                        if (disparando){
                            nuevoDisparo(posXNave, posYNave-2);
                            disparando=false;
                        }
                        if (verificarGane()){
                            victoria();
                        }
                        if (!disparos.isEmpty()) {
                            moverDisparos();
                        }
                    }
                    else
                        terminarJuego();
                }


            }
        };
        timer.schedule(task, 200 ,60); // timer para moderar el envio de informacion
    }
    private void moverDisparos(){

        for (int i=0; i<disparos.size();i++){
            if (coloresPantalla[disparos.get(i).getPosX()][disparos.get(i).getPosY()-1]==Color.black){
                coloresPantalla[disparos.get(i).getPosX()][disparos.get(i).getPosY()] = Color.black;
                enviarDisparo(disparos.get(i).getPosX(), disparos.get(i).getPosY(), Color.BLACK);
                disparos.get(i).setPosY(disparos.get(i).getPosY()-1);
                coloresPantalla[disparos.get(i).getPosX()][disparos.get(i).getPosY()] = disparos.get(i).getColor();
                enviarDisparo(disparos.get(i).getPosX(), disparos.get(i).getPosY(), disparos.get(i).getColor());
            }
            else {
                if (disparos.get(i).getPosY() - 1 != 0) {
                    coloresPantalla[disparos.get(i).getPosX()][disparos.get(i).getPosY() - 1] = Color.black;
                    enviarDisparo(disparos.get(i).getPosX(), disparos.get(i).getPosY() - 1, Color.BLACK);
                }
                coloresPantalla[disparos.get(i).getPosX()][disparos.get(i).getPosY()] = Color.black;
                enviarDisparo(disparos.get(i).getPosX(), disparos.get(i).getPosY(), Color.black);
                disparos.remove(i);
                puntaje++;
            }
        }
    }
    public void enviarDisparo(int posX, int posY, Color color){
        cliente.enviarMensaje(posX,posY, color.getRed(), color.getGreen(), color.getBlue());
    }

    private void nuevoDisparo(int posx, int posy){
        coloresPantalla[posx][posy] = Color.white;
        enviarDisparo(posx, posy, Color.WHITE);
        disparos.add(new Disparo(posx, posy));
    }

    private void victoria(){
        cliente.enviarMensaje(puntaje);
        jugando=false;
    }
    private boolean verificarGane(){
        for (int i = 1; i<49;i++){
            for (int j = 1;j<41; j++){
                if (coloresPantalla[i][j]!=Color.black)
                    return false;
            }
        }
        return true;
    }
    public void terminarJuego(){
        for (int i = 0;i<50;i++){
            for (int j = 0; j<50;j++){
                coloresPantalla[i][j] = Color.gray;
            }
        }
        EnviarMapa();
        System.exit(1);
    }

    public void moveNave(){
        if (direccion==3){
            if (validarMovNave(posXNave-2, posYNave)) {
                posXNave -= 1;
            }
        }
        else{
            if (validarMovNave(posXNave+2, posYNave)) {
                posXNave += 1;
            }
        }
        actualizarNave();
        enviarNave();
    }
    private boolean validarMovNave(int XNave,int YNave){
        if (coloresPantalla[XNave][YNave]==Color.blue){
            return false;
        }
        return true;
    }
    public void actualizarNave(){
        if (direccion==3) { // movimiento a la izquierda
            // derecha
            coloresPantalla[posXNave+2][posYNave] = Color.black;
            enviarDisparo(posXNave+2, posYNave, Color.black);
            // nuevo arriba
            coloresPantalla[posXNave][posYNave-1] = Color.red;
            enviarDisparo(posXNave, posYNave-1, Color.red);
            // viejo arriba
            coloresPantalla[posXNave+1][posYNave-1] = Color.black;
            enviarDisparo(posXNave+1, posYNave-1, Color.black);
            //nuevo izquierda
            coloresPantalla[posXNave-1][posYNave] = Color.red;
            enviarDisparo(posXNave-1, posYNave, Color.red);
        }
        else {// movimiento a la derecha
            // izquierda
            coloresPantalla[posXNave-2][posYNave] = Color.black;
            enviarDisparo(posXNave-2, posYNave, Color.black);
            // nuevo arriba
            coloresPantalla[posXNave][posYNave-1] = Color.red;
            enviarDisparo(posXNave, posYNave-1, Color.red);
            // viejo arriba
            coloresPantalla[posXNave-1][posYNave-1] = Color.black;
            enviarDisparo(posXNave-1, posYNave-1, Color.black);
            //nuevo derecha
            coloresPantalla[posXNave+1][posYNave] = Color.red;
            enviarDisparo(posXNave+1, posYNave, Color.red);
        }
    }
    public void enviarNave(){
        cliente.enviarMensaje(posXNave,posYNave, coloresPantalla[posXNave][posYNave].getRed(), coloresPantalla[posXNave][posYNave].getGreen(), coloresPantalla[posXNave][posYNave].getBlue());
    }
    public void EnviarMapa() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                cliente.enviarMensaje(i,j, coloresPantalla[i][j].getRed(), coloresPantalla[i][j].getGreen(), coloresPantalla[i][j].getBlue());
            }
        }
        mapaConstruir = true;
    }
    class EntradaSpaceInv implements Runnable {

        public EntradaSpaceInv() {

            Thread hilo1 = new Thread(this);
            hilo1.start();
        }

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(9997);

                while (true) {
                    Socket socketAux = serverSocket.accept();
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
            if (jsonObject.has("derecha")){
                direccion = 4;
                moverNave=true;
            }
            else if (jsonObject.has("izquierda")) {
                direccion = 3;
                moverNave = true;
            }
            else if (jsonObject.has("comando"))
                disparando = true;
        }
    }
}
