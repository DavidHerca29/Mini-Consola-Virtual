package Comunicacion;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable{

    public ServerSocket servidorView;
    public String mensaje = "";
    int puerto;
    public Servidor(int puertoP) {
        puerto = puertoP;
    }

    @Override
    public void run() {
        try {
            servidorView = new ServerSocket(puerto);
            /*
            while (true) {
                Socket socketAux = servidorView.accept();

                DataInputStream flujoEntrada = new DataInputStream(socketAux.getInputStream());

                String texto = flujoEntrada.readUTF();
                //JSONObject jsonObject = new JSONObject(texto);

                //System.out.println("Recibido: \n"+ jsonObject.toString(2));

                //socketAux.close();
                flujoEntrada.close();
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ServerSocket getServidorView() {
        return servidorView;
    }
}
