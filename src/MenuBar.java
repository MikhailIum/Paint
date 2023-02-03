import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuBar extends JMenuBar {


    Frame frame;
    PrintWriter out;

    public MenuBar(Frame frame){
        this.frame = frame;

        this.addMouseListener(new MouseListener() {
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
                frame.is_on_painting = false;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.is_on_painting = true;
            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                frame.is_on_painting = false;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                frame.is_on_painting = false;
            }
        });

        Font font = new Font("Verdana", Font.PLAIN, 11);

        JMenu file = new JMenu("File");
        file.setFont(font);
        this.add(file);

        JMenuItem save = new JMenuItem("save as");
        save.setFont(font);
        file.add(save);

        JMenuItem open = new JMenuItem("open");
        open.setFont(font);
        file.add(open);

        JMenuItem newPainting = new JMenuItem("new");
        newPainting.setFont(font);
        file.add(newPainting);


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog dialog = new FileDialog((Frame) null);
                dialog.setVisible(true);
                String directory = dialog.getDirectory();
                String filename = dialog.getFile();
                dialog.dispose();
                if (directory == null || filename == null) {
                    System.out.println("Файл не выбран!");
                    return;
                }
                String path = directory + filename;
                System.out.println("Выбранный файл: " + path);
                try {
                    out = new PrintWriter(path);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }



                for (Point point : frame.clicks) {
                    out.println("Point");

                    out.println(point.x);
                    out.println(point.y);
                    out.println(point.color.getRGB());
                    out.println(point.stroke);
                }

                for (Brushstroke brushstroke: frame.brushstrokes){
                    for(int i = 0; i < brushstroke.lines.size(); ++i){
                        out.println("Brushstroke");

                        Point firstPoint = brushstroke.lines.get(i).prev_point;
                        out.println("first");
                        out.println(firstPoint.x);
                        out.println(firstPoint.y);
                        out.println(firstPoint.color.getRGB());
                        out.println(firstPoint.stroke);


                        out.println("Brushstroke");
                        Point secondPoint = brushstroke.lines.get(i).cur_point;
                        out.println("second");
                        out.println(secondPoint.x);
                        out.println(secondPoint.y);
                        out.println(secondPoint.color.getRGB());
                        out.println(secondPoint.stroke);
                    }
                    out.println("End of a Brushstroke");
                }

                out.close();
            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog dialog = new FileDialog((Frame) null);
                dialog.setVisible(true);
                String directory = dialog.getDirectory();
                String filename = dialog.getFile();
                dialog.dispose();
                if (filename == null || directory == null){
                    System.out.println("Файл не выбран!");
                    return;
                }
                String path = directory + filename;
                File file = new File(path);
                Scanner scanner = null;
                try {
                    scanner = new Scanner(file);
                    frame.brushstrokes.clear();
                    frame.lines.clear();
                    frame.clicks.clear();
                    frame.repaint();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                Point firstPoint = null;
                ArrayList<Line> lines = new ArrayList<>();
                while (true){
                    assert scanner != null;
                    if (!scanner.hasNext()) break;
                    switch (scanner.nextLine()) {
                        case "Point":
                            int x = Integer.parseInt(scanner.nextLine());
                            int y = Integer.parseInt(scanner.nextLine());
                            Color color = new Color(Integer.parseInt(scanner.nextLine()));
                            int stroke = Integer.parseInt(scanner.nextLine());

                            frame.clicks.add(new Point(x, y, stroke, color));
                            break;
                        case "Brushstroke":
                            if (scanner.nextLine().equals("first")) {
                                x = Integer.parseInt(scanner.nextLine());
                                y = Integer.parseInt(scanner.nextLine());
                                color = new Color(Integer.parseInt(scanner.nextLine()));
                                stroke = Integer.parseInt(scanner.nextLine());

                                firstPoint = new Point(x, y, stroke, color);
                            } else {
                                x = Integer.parseInt(scanner.nextLine());
                                y = Integer.parseInt(scanner.nextLine());
                                color = new Color(Integer.parseInt(scanner.nextLine()));
                                stroke = Integer.parseInt(scanner.nextLine());

                                assert firstPoint != null;
                                lines.add(new Line(firstPoint, new Point(x, y, stroke, color), stroke, color));
                            }
                            break;
                        case "End of a Brushstroke":
                            frame.brushstrokes.add(new Brushstroke(lines));
                            lines = new ArrayList<>();
                            break;
                    }
                }

                frame.repaint();
            }
        });

        newPainting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.brushstrokes.clear();
                frame.lines.clear();
                frame.clicks.clear();

                frame.repaint();
            }
        });
    }



}
