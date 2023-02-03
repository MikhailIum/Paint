import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ColorPicker {

    Frame frame;

    public ColorPicker(Frame frame){
        this.frame = frame;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1));
        frame.add(panel, BorderLayout.EAST);
        JButton color_button = new JButton(new ImageIcon("res/color-picker1.png"));
        color_button.setBackground(new Color(238, 238, 238));
        color_button.setBorder(null);

        color_button.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                frame.is_on_painting = false;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                frame.is_on_painting = false;
            }
        });
        color_button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                frame.getContentPane().setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);


                // Create a new blank cursor.
                Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                        cursorImg, new java.awt.Point(0, 0), "blank cursor");

                // Set the blank cursor to the JFrame.
                frame.getContentPane().setCursor(blankCursor);
                frame.is_on_painting = true;
            }
        });


        color_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                Color selectedColor = JColorChooser.showDialog(frame, "Pick a Color", frame.cur_color);
                if (selectedColor != null)
                {
                    frame.cur_color = selectedColor;
                }
            }
        });
        panel.add(color_button);
        frame.setVisible(true);
    }



}





