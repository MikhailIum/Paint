import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;


public class Frame extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener {
    ArrayList<Point> clicks = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();
    Point prev_point;
    int stroke_size = 5;
    Color cur_color = Color.BLACK;
    Point cur_point;
    boolean is_dragging = false;
    boolean is_on_painting = true;
    boolean is_last_step_on_painting = true;
    ArrayList<Brushstroke> brushstrokes = new ArrayList<>();
    MenuBar menu = new MenuBar(this);

    int screen_width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int screen_height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();


    public Frame() {
        this.setSize(screen_width / 3 * 2, screen_height / 4 * 3);
        this.setLocation(screen_width / 2 - screen_width / 3, screen_height / 10);
        this.setMinimumSize(new Dimension((int) (screen_width / 1.4), (int)(screen_height / 1.4)));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Paint");

        this.setVisible(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

        this.setJMenuBar(menu);
        ColorPicker color_picker = new ColorPicker(this);


        this.createBufferStrategy(2);

    }

    @Override
    public void paint(Graphics g){
        BufferStrategy bufferStrategy = getBufferStrategy();        // Обращаемся к стратегии буферизации
        while (bufferStrategy == null) {                               // Если она еще не создана
            createBufferStrategy(2);                                // то создаем ее
            bufferStrategy = getBufferStrategy();                   // и опять обращаемся к уже наверняка созданной стратегии
        }
        g = bufferStrategy.getDrawGraphics();                       // Достаем текущую графику (текущий буфер)

        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        if (!is_dragging && cur_point != null && is_on_painting) {
            g.setColor(cur_color);
            g.drawOval(cur_point.x, cur_point.y, stroke_size, stroke_size);
        }
        for (Point point : clicks) {
            g.setColor(point.color);
            g.fillOval(point.x, point.y, point.stroke, point.stroke);
        }

        for (Brushstroke brushstroke : brushstrokes) {
            for (Line line: brushstroke.lines){
                g.setColor(line.color);
                g2d.setStroke(line.stroke);
                g.drawLine(line.prev_point.x, line.prev_point.y, line.cur_point.x, line.cur_point.y);
            }
        }

        for (Line line : lines) {
            g.setColor(line.color);
            g2d.setStroke(line.stroke);
            g.drawLine(line.prev_point.x, line.prev_point.y, line.cur_point.x, line.cur_point.y);
        }


        g.dispose();                // Освободить все временные ресурсы графики (после этого в нее уже нельзя рисовать)
        bufferStrategy.show();      // Сказать буферизирующей стратегии отрисовать новый буфер (т.е. поменять показываемый и обновляемый буферы местами)

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // быстро нажал-отпустил
        if (is_on_painting)
            clicks.add(prev_point);

        this.repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // нажал

        prev_point = new Point(e.getX(), e.getY(), stroke_size, cur_color);

        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // отпустил
        is_dragging = false;
        cur_point = new Point(e.getX(), e.getY(), stroke_size, cur_color);

        brushstrokes.add(new Brushstroke(lines));
        lines = new ArrayList<>();

        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // ввёл мышку в область окна

        is_on_painting = true;
        this.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // вывел мышку из области окна

        is_on_painting = false;
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // перемещение мышки с зажатой левой кнопкой
        is_dragging = true;

//        is_on_painting = menu.getHeight() <= e.getY() - 30;
        if (is_on_painting) {
            if (!is_last_step_on_painting) {
                prev_point = new Point(e.getX(), e.getY(), stroke_size, cur_color);
                is_last_step_on_painting = true;
            }
            lines.add(new Line(prev_point, new Point(e.getX(), e.getY(), stroke_size, cur_color), stroke_size, cur_color));
        }
        else {
            is_last_step_on_painting = false;
            if (!lines.isEmpty()) {
                brushstrokes.add(new Brushstroke(lines));
            }
        }
        prev_point = new Point(e.getX(), e.getY(), stroke_size, cur_color);


        this.repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // перемещение мышки

        cur_point = new Point(e.getX(), e.getY(), stroke_size, cur_color);
        is_on_painting = menu.getHeight() <= cur_point.y - 30;
        this.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() == 1){
            if (stroke_size >= 5)
                stroke_size--;
        } else{
            if (stroke_size <= 30)
                stroke_size++;
        }

        this.repaint();
    }
}

class Brushstroke{
    ArrayList<Line> lines;

    public Brushstroke(ArrayList<Line> lines){
        this.lines = lines;
    }

}
class Line{
    Point prev_point;
    Point cur_point;
    Stroke stroke;
    Color color;

    public Line(Point prev_point, Point cur_point, int stroke, Color color){
        this.cur_point = cur_point;
        this.prev_point = prev_point;
        this.stroke = new BasicStroke(stroke);
        this.color = color;
    }
}

class Point{
    int x;
    int y;
    int stroke;
    Color color;

    public Point(int x, int y, int stroke, Color color){
        this.x = x;
        this.y = y;
        this.stroke = stroke;
        this.color = color;
    }

}