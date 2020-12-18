package Comms;

import javax.swing.*;

public class MostrarMensaje implements Runnable{
    private int puntaje;
    public MostrarMensaje(int puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public void run() {
        JOptionPane.showMessageDialog(null, "Has ganado con un total de: "+puntaje+" puntos.");
    }
}
