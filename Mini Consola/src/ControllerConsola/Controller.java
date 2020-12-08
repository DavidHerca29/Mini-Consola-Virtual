package ControllerConsola;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class Controller {
    public static void main(String[] args) {
        Controles controles = new Controles();
    }
}
class Controles extends JFrame {
    public Controles() throws HeadlessException {
        setSize(300, 365);
        setLocationRelativeTo(null);
        PanelBotones panelBotones = new PanelBotones();
        add(panelBotones);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
class PanelBotones extends JPanel implements ActionListener {
    private JButton arriba;
    private JButton abajo;
    private JButton derecha;
    private JButton izquierda;
    private JButton Comando;
    public PanelBotones() {
        setVisible(true);
        setBounds(0,0, 300, 345);
        setLayout(null);

        arriba = new JButton();
        Comando = new JButton();
        abajo = new JButton();
        izquierda = new JButton();
        derecha = new JButton();
        arriba.setBounds(115, 25, 60, 80);
        arriba.setIcon(scaleImage(new ImageIcon("Mini Consola/src/Imagenes/arriba.png"),85,85));
        arriba.setVisible(true);
        arriba.addActionListener(this);
        arriba.setContentAreaFilled(false);

        abajo.setBounds(115, 195, 60, 80);
        abajo.setIcon(scaleImage(new ImageIcon("Mini Consola/src/Imagenes/abajo.png"),85,75));
        abajo.setVisible(true);
        abajo.addActionListener(this);
        abajo.setContentAreaFilled(false);

        izquierda.setBounds(25, 120, 80, 60);
        izquierda.setIcon(scaleImage(new ImageIcon("Mini Consola/src/Imagenes/izquierda.png"),85,75));
        izquierda.setVisible(true);
        izquierda.addActionListener(this);
        izquierda.setContentAreaFilled(false);

        derecha.setBounds(185, 120, 80, 60);
        derecha.setIcon(scaleImage(new ImageIcon("Mini Consola/src/Imagenes/derecha.png"),80,65));
        derecha.setVisible(true);
        derecha.addActionListener(this);
        derecha.setContentAreaFilled(false);

        Comando.setBounds(115, 120, 60, 60);
        Comando.setBackground(new Color(217, 46, 46));
        Comando.setVisible(true);
        Comando.addActionListener(this);
        //Comando.setContentAreaFilled(false);

        add(arriba);
        add(abajo);
        add(Comando);
        add(izquierda);
        add(derecha);
    }
    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object botonPresionado = e.getSource();
        try {
            Socket socket = new Socket("192.168.1.124", 9999);
            DataOutputStream flujoSalida =  new DataOutputStream(socket.getOutputStream());
            BufferedReader flujoEntrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "Hello World");

            flujoSalida.writeUTF(jsonObject.toString() + "\n");
            flujoSalida.flush();

        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println(ioException.getMessage());
        }
    }
}