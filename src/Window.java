import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Window {
    private final JFrame frame;
    private final JPanel panel;
    private final BufferedImage img;
    private final int[] pixels;

    private int scale;
    private final int w, h;

    public Window(int w, int h, int scale, String title) {
        this.w = w;
        this.h = h;
        this.scale = scale;

        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

                int sw = w * scale;
                int sh = h * scale;

                g2.drawImage(img, 0, 0, sw, sh, null);
            }
        };

        panel.setPreferredSize(new Dimension(w * scale, h * scale));

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public void setPix(int x, int y, int argb) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        pixels[x + y * w] = argb;
    }
    public int getPix(int x, int y, int channel) {
        if (x < 0 || y < 0 || x >= w || y >= h) return 0;
        return (pixels[x + y * w] >> (channel * 8)) & 0xFF;
    }

    public void clear(int argb) {
        Arrays.fill(pixels, argb);
    }
    public void drawFrame() {
        panel.paintImmediately(0, 0, panel.getWidth(), panel.getHeight());
    }
    public void setScale(int s) {
        scale = s;
        panel.setPreferredSize(new Dimension(w * scale, h * scale));
        frame.pack();
    }
    public int getScale() {
        return scale;
    }
    public int getPosX(){
        return frame.getX();
    }
    public int getPosY(){
        return frame.getY();
    }

    public JFrame getFrame() { return frame; }
    public JPanel getPanel() { return panel; }
}
