package ControllerConsola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public PanelBotones() {
        setLayout(null);
        arriba = new JButton();
        abajo = new JButton();
        izquierda = new JButton();
        derecha = new JButton();
        arriba.setBounds(20, 20, 60, 90);
        arriba.setIcon(scaleImage(new ImageIcon("Mini Consola/src/Imagenes/arriba.png"),85,95));
        arriba.setVisible(true);
        arriba.addActionListener(this);
        arriba.setContentAreaFilled(false);
        //arriba.setFocusPainted(false);
        //arriba.setBorderPainted(false);

        setVisible(true);
        setBounds(0,0, 300, 365);
        add(arriba);
        //add(abajo);
        //add(izquierda);
        //add(derecha);
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
        System.out.println("Yes");
    }
}