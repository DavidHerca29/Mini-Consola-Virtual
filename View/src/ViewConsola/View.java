package ViewConsola;

import com.sun.source.util.ParameterNameProvider;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class View {
    public static void main(String[] args){
        Pantalla pantalla = new Pantalla();
    }
}
class Pantalla extends JFrame implements Runnable{
    private JLabel[][] pixels = new JLabel[50][50];
    //private ModeloPantalla viewPixels;
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
                //pixels[i][j].setPreferredSize(new Dimension(10, 10));
                add(pixels[i][j]);
            }
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        //viewPixels = new ModeloPantalla();
        //add(viewPixels);
        Thread hilo1 = new Thread(this);
        hilo1.start();

    }
    @Override
    public void run() {
        try {
            ServerSocket servidorView = new ServerSocket(9999);

            Socket socketAux = servidorView.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class ModeloPantalla extends JPanel{
    private JLabel[][] pixels = new JLabel[50][50];

    public ModeloPantalla() {
        setVisible(true);
        setBounds(820, 640, 0,0);
        setLayout(new GridLayout(50, 50));
        for (int i = 0; i<50;i++){
            for (int j=0;j<50;j++){
                pixels[i][j] = new JLabel();
                pixels[i][j].setBackground(Color.RED);
            }
        }
        for (int i=0; i<50; i++){
            for (int j=0; j<50;j++) {
                this.add(pixels[i][j]);
            }
        }
    }
}

