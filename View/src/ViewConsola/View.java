package ViewConsola;

import com.sun.source.util.ParameterNameProvider;

import javax.swing.*;
import java.awt.*;

public class View {
    public static void main(String[] args){
        Pantalla pantalla = new Pantalla();
    }
}
class Pantalla extends JFrame{
    private ModeloPantalla viewPixels = new ModeloPantalla();
    public Pantalla() throws HeadlessException {
        setVisible(true);
        setSize(820, 640);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(50, 50));
        add(viewPixels);
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
                pixels[i][j].setBackground(new Color(i, j, 123));
            }
        }
        for (int i=0; i<50; i++){
            for (int j=0; j<50;j++) {
                this.add(pixels[i][j]);
            }
        }
    }
}