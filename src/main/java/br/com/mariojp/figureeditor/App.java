package br.com.mariojp.figureeditor;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Editor de figura");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            DrawingPanel panel = new DrawingPanel();

            JButton btCor = new JButton("Cor...");
            btCor.addActionListener(e -> {
                Color novaCor = JColorChooser.showDialog(frame, "Escolha a cor",Color.blue);
                if (novaCor != null) {
                    panel.setCurrentColor(novaCor);
                }
            });

            JButton btClear = new JButton("Clear");
            btClear.addActionListener(e -> panel.clear());

            JButton btShape = new JButton("Retângulo");
            btShape.addActionListener(e -> {
                boolean isCircle = panel.toggleShapeType();
                btShape.setText(isCircle ? "Circulo" : "Retângulo");
            });

            JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
            barra.add(btShape);
            barra.add(btCor);
            barra.add(btClear);

            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.getContentPane().add(barra, BorderLayout.NORTH);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
