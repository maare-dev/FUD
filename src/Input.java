import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

public class Input {

    // keyboard
    public char keyTyped;
    public Map<String, Boolean> isKeyPressed = new HashMap<>();

    // mouse
    public int lMBDown, mMBDown, rMBDown;
    public int mX, mY;
    public int lastClickX, lastClickY;
    public int lastDragX, lastDragY;

    public Input(JFrame frame, JPanel panel) {
        panel.setFocusable(true);

        // ---===Key Listener===---
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyTyped = e.getKeyChar();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                isKeyPressed.put(KeyEvent.getKeyText(e.getKeyCode()), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                isKeyPressed.put(KeyEvent.getKeyText(e.getKeyCode()), false);
            }
        });

        // ---===Mouse Listener===---
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastClickX =  e.getLocationOnScreen().x;
                lastClickY = e.getLocationOnScreen().y;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                switch(e.getButton()){
                    case MouseEvent.BUTTON1 -> lMBDown = 1;
                    case MouseEvent.BUTTON2 -> mMBDown = 1;
                    case MouseEvent.BUTTON3 -> rMBDown = 1;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1 -> lMBDown = 0;
                    case MouseEvent.BUTTON2 -> mMBDown = 0;
                    case MouseEvent.BUTTON3 -> rMBDown = 0;
                }
            }


            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        // ---===Mouse Motion Listener===---
        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                lastDragX = e.getLocationOnScreen().x;
                lastDragY = e.getLocationOnScreen().y;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mX = e.getLocationOnScreen().x;
                mY = e.getLocationOnScreen().y;
            }
        });

        frame.add(panel);
        frame.revalidate();
        frame.repaint();

        panel.requestFocusInWindow();
    }
}
