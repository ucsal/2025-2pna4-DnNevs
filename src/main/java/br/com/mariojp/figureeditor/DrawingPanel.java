
package br.com.mariojp.figureeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel {

   private static final long serialVersionUID = 1L;
   private final List<Shape> shapes = new ArrayList<>();
   private Point startDrag = null;
   private Shape previewShape = null;
   private Color currentColor = new Color(30,144,255);
   private boolean isCircleMode = false;
   private static final int DEFAULT_SIZE = 50;

   public boolean toggleShapeType(){
       this.isCircleMode = !this.isCircleMode;
       return this.isCircleMode;
   }


   public void setCurrentColor(Color color) {
       if (color != null) {
           this.currentColor = color;
           repaint();
       }
   }

   DrawingPanel() {
       setBackground(Color.WHITE);
       setOpaque(true);
       setDoubleBuffered(true);


       var mouse = new MouseAdapter() {
           private boolean wasDragging = false;

           @Override
           public void mousePressed(MouseEvent e) {
               startDrag = e.getPoint();
               previewShape = null;
               wasDragging = false;
           }

           @Override
           public void mouseDragged(MouseEvent e) {
               wasDragging = true;
               if (startDrag != null) {
                   int x = Math.min(startDrag.x, e.getX());
                   int y = Math.min(startDrag.y, e.getY());
                   int w = Math.abs(e.getX() - startDrag.x);
                   int h = Math.abs(e.getY() - startDrag.y);
                   if(isCircleMode){
                       int size = Math.min(w, h);
                       previewShape = new Ellipse2D.Double(x, y, size, size);
                   }else{
                       previewShape = new Rectangle2D.Double(x,y,w,h);
                   }
                   repaint();
               }
           }

           @Override
           public void mouseReleased(MouseEvent e) {
               if (startDrag != null) {
                   Shape shapeToAdd = null;

                   if(!wasDragging){
                       int x = startDrag.x - DEFAULT_SIZE / 2;
                       int y = startDrag.y - DEFAULT_SIZE / 2;

                       if(isCircleMode){
                           shapeToAdd = new Ellipse2D.Double(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
                       }else {
                           shapeToAdd = new Rectangle2D.Double(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
                       }
                   } else if (previewShape != null) {
                       shapeToAdd = previewShape;

                   }

                   if (shapeToAdd != null) {
                       shapes.add(shapeToAdd);
                   }
               }
               startDrag = null;
               previewShape = null;
               wasDragging = false;
               repaint();
           }
       };

       addMouseListener(mouse);
       addMouseMotionListener(mouse);
   }

   void clear() {
       shapes.clear();
       repaint();
   }

   @Override
    protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2d = (Graphics2D) g.create();
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

       for(Shape shape : shapes) {
           g2d.setColor(currentColor);
           g2d.fill(shape);
           g2d.setColor(new Color(0,0,0,70));
           g2d.setStroke(new BasicStroke(1.2f));
           g2d.draw(shape);
       }

       if(previewShape != null) {
           float[] dash = {5f,5f};
           g2d.setColor(Color.GRAY);
           g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10F, dash, 0f));
           g2d.draw(previewShape);
       }

       g2d.dispose();
   }

}
