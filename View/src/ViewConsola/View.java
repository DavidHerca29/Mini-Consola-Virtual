package ViewConsola;

import Comms.MostrarMensaje;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

public class View {
    public static void main(String[] args){
        Pantalla pantalla = new Pantalla();
    }
}
class Pantalla extends JFrame implements Runnable{
    private JLabel[][] pixels = new JLabel[50][50];
    public Pantalla(){
        getContentPane().setBackground(Color.BLACK);
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(50, 50));
        for (int i = 0; i<50;i++){
            for (int j=0;j<50;j++){
                pixels[i][j] = new JLabel();
                pixels[i][j].setOpaque(true);
                pixels[i][j].setBackground(Color.WHITE);
                pixels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                add(pixels[i][j]);
            }
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        Thread hilo1 = new Thread(this);
        hilo1.start();

    }
    @Override
    public void run() {
        try {
            ServerSocket servidorView = new ServerSocket(9999);

            while (true) {
                Socket socketAux = servidorView.accept();

                DataInputStream flujoEntrada = new DataInputStream(socketAux.getInputStream());

                String texto = flujoEntrada.readUTF();
                JSONArray jsonObject = new JSONArray(texto);

                //System.out.println("Recibido: \n"+ jsonObject.toString(2));
                procesarMensaje(jsonObject);

                //socketAux.close();
                flujoEntrada.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void procesarMensaje(JSONArray jsonObject){
        // decodifica el mensaje en el JSON para poder cambiar el color o bien, mostrar el mensaje ganador
        if (jsonObject.length()==1){
            Runnable mostrarMensaje = new MostrarMensaje(jsonObject.getInt(0));
            Thread thread = new Thread(mostrarMensaje);
            thread.start();
        }
        else {
            Color color = new Color(jsonObject.getInt(2), jsonObject.getInt(3), jsonObject.getInt(4));
            //System.out.println(jsonObject); // verificar que en verdad se mandan arreglos mediante el json
            cambiarColor(jsonObject.getInt(0), jsonObject.getInt(1), color);
        }
    }
    private void cambiarColor(int col, int fil, Color color){
        pixels[fil][col].setBackground(color);
    }
}
