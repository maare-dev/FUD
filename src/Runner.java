import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Runner {
    private final static int[] mem = new int[256];
    private final static Window w = new Window(256, 255, 3, Info.lang_name + " | " + Info.version);
    private static final Input input = new Input(w.getFrame(), w.getPanel());
    public static void run(){
        /*
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                img,
                new Point(0, 0),
                "invisibleCursor"
        );
        w.getFrame().setCursor(transparentCursor);
        w.getFrame().setVisible(true);

         */
        while(true){
            int word = Files.readWithDebug(Lang.first);
            switch(word){
                case 0: plus(Files.readBytesWithDebug(Lang.first, 2)); break;        // +
                case 1: minus(Files.readBytesWithDebug(Lang.first, 2)); break;       // -
                case 2: equal(Files.readBytesWithDebug(Lang.first, 4)); break;       // =
                case 3: out(Files.readBytesWithDebug(Lang.first, 2)); break;         // out
                case 4: in(Files.readBytesWithDebug(Lang.first, 4)); break;          // in (skip)
                case 5: _if(Files.readBytesWithDebug(Lang.first, 7)); break;         // if
                case 6: Files.readBytesWithDebug(Lang.first, 2); break;              // * (skip)
                case 7: jump(Files.readBytesWithDebug(Lang.first, 2)); break;        // jump
                case 8: delay(Files.readBytesWithDebug(Lang.first, 2)); break;       // delay
                case 9: save(); break;                                                  // save
                case 10:quit(Files.readBytesWithDebug(Lang.first, 2)); break;        // quit
                case 11:draw(); break;                                                  // draw
                case 12:load(); break;                                                  // load
                case -1:Err.sendErrAndExit("unexpected program termination due to: end of file", 1);
            }
            mem[250] = (input.mX - w.getPosX()) / w.getScale();
            mem[249] = (input.mY - w.getPosY() - 37) / w.getScale();
            mem[248] = input.lMBDown;
            mem[247] = input.mMBDown;
            mem[246] = input.rMBDown;
            if(Lang.mode == 1) System.out.println();
        }
    }
    private static void checkUInt(int[] o){
        for (int i = 0; i < o.length; i++){
            if(o[i] < 0 || o[i] > 255){
                Err.sendErrAndExit("unexpected program termination due to: end of file", i++);
            }
        }
    }
    private static void _goto(int o){
        if (Lang.mode == 1) System.out.println("goto " + o);
        Files.seek(Lang.first, 0);
        int b = Files.read(Lang.first);
        while(true){
            while(b != 6){
                switch (b){
                    case 9, 11: break;
                    case 0, 1, 3, 7, 8, 10: Files.readBytes(Lang.first, 2); break;
                    case 2, 4: Files.readBytes(Lang.first, 4); break;
                    case 5: Files.readBytes(Lang.first, 7); break;
                    case -1: Err.sendErrAndExit("unexpected program termination due to: place " + o + " not found", 1);
                    default: Err.sendErrAndExit("unknown program", 1);
                }
                b = Files.read(Lang.first);
            }
            int[] o2 = Files.readBytesWithDebug(Lang.first, 2);
            int i = switch (o2[0]){
                case 0 -> o2[1];
                case 1 -> mem[o2[1]];
                case 2 -> mem[mem[o2[1]]];
                default -> 0;
            };
            if(i == o) break;
            b = Files.read(Lang.first);
        }
    }
    private static void safeGoto(int o){
        long lastPos = 0;
        try{
            lastPos = Lang.first.getFilePointer();
        } catch (Exception e){}
        if (Lang.mode == 1) System.out.println("safe goto " + o);
        Files.seek(Lang.first, 0);
        int b = Files.read(Lang.first);
        while(true){
            while(b != 6){
                switch (b){
                    case 9, 11: break;
                    case 0, 1, 3, 7, 8, 10: Files.readBytes(Lang.first, 2); break;
                    case 2, 4: Files.readBytes(Lang.first, 4); break;
                    case 5: Files.readBytes(Lang.first, 7); break;
                    case -1: Files.seek(Lang.first, lastPos); return;
                    default: Err.sendErrAndExit("unknown program", 1);
                }
                b = Files.read(Lang.first);
            }
            int[] o2 = Files.readBytesWithDebug(Lang.first, 2);
            int i = switch (o2[0]){
                case 0 -> o2[1];
                case 1 -> mem[o2[1]];
                case 2 -> mem[mem[o2[1]]];
                default -> 0;
            };
            if(i == o) break;
            b = Files.read(Lang.first);
        }
    }
    private static void in(int[] o){
        checkUInt(o);
        if(Lang.mode == 1) System.out.print("in ");
        int val1 = switch (o[0]) {
            case 0 -> o[1];
            case 1 -> mem[o[1]];
            case 2 -> mem[mem[o[1]]];
            default -> 0;
        };
        int val2 = switch (o[2]) {
            case 0 -> o[3];
            case 1 -> mem[o[3]];
            case 2 -> mem[mem[o[3]]];
            default -> 0;
        };
        if( input.isKeyPressed.getOrDefault(KeyEvent.getKeyText(val1), Boolean.FALSE) ) safeGoto(val2);
    }
    private static void plus(int[] o){
        checkUInt(o);
        switch (o[0]){
            case 0: Err.sendErrAndExit("type of data \"b\" cannot use in \"+\"", 1);
            case 1: mem[o[1]]++; if(mem[o[1]] > 255) mem[o[1]] = 0; break;
            case 2: mem[mem[o[1]]]++ ; if(mem[mem[o[1]]] > 255) mem[mem[o[1]]] = 0; break;
        }
        onMemChange();
    }
    private static void minus(int[] o){
        checkUInt(o);
        switch (o[0]){
            case 0: Err.sendErrAndExit("type of data \"b\" cannot use in \"-\"", 1);
            case 1: mem[o[1]]--; if(mem[o[1]] < 0) mem[o[1]] = 255; break;
            case 2: mem[mem[o[1]]]--; if(mem[mem[o[1]]] < 0) mem[mem[o[1]]] = 255; break;
        }
        onMemChange();
    }
    private static void equal(int[] o){
        checkUInt(o);
        int val = switch (o[2]) {
            case 0 -> o[3];
            case 1 -> mem[o[3]];
            case 2 -> mem[mem[o[3]]];
            default -> 0;
        };
        switch (o[0]){
            case 0: Err.sendErrAndExit("type of data \"b\" cannot use in \"=\"", 1);
            case 1: mem[o[1]] = val; break;
            case 2: mem[mem[o[1]]] = val; break;
        }
        onMemChange();
    }
    private static void out(int[] o){
        switch (o[0]){
            case 0: System.out.print((char)o[1]); break;
            case 1: System.out.print((char)mem[o[1]]); break;
            case 2: System.out.print((char)mem[mem[o[1]]]); break;
        }
    }
    private static void _if(int[] o){
        checkUInt(o);
        int val1 = 0;
        switch (o[0]) {
            case 0 -> Err.sendErrAndExit("type of data \"b\" cannot use in \"if\"", 1);
            case 1 -> val1 = mem[o[1]];
            case 2 -> val1 = mem[mem[o[1]]];
            default -> Err.sendErrAndExit("unknown type of data in \"if\": " + o[0], 1);
        }
        int val2 = switch (o[3]) {
            case 0 -> o[4];
            case 1 -> mem[o[4]];
            case 2 -> mem[mem[o[4]]];
            default -> 0;
        };
        int val3 = switch (o[5]) {
            case 0 -> o[6];
            case 1 -> mem[o[6]];
            case 2 -> mem[mem[o[6]]];
            default -> 0;
        };
        switch (o[2]){
            case 0: if(val1 == val2) _goto(val3);
            case 1: if(val1 > val2) _goto(val3);
        }
    }
    private static void jump(int[] o){
        checkUInt(o);
        int val = switch (o[0]) {
            case 0 -> o[1];
            case 1 -> mem[o[1]];
            case 2 -> mem[mem[o[1]]];
            default -> 0;
        };
        _goto(val);
    }
    private static void delay(int[] o){
        checkUInt(o);
        try{
            switch (o[0]){
                case 0: Thread.sleep(o[1]); break;
                case 1: Thread.sleep(mem[o[1]]); break;
                case 2: Thread.sleep(mem[mem[o[1]]]); break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void save(){
        Files.seek(Lang.second, 0);
        Files.writeBytes(Lang.second, mem);
    }
    private static void load(){
        Files.seek(Lang.second, 0);
        for(int i = 0; i < 256; i++){
            if(Files.read(Lang.second) == -1){
                mem[i] = 0;
            }else{
                mem[i] = Files.read(Lang.second);
            }
        }
    }
    private static void quit(int[] o){
        Files.close(Lang.first);
        Files.close(Lang.second);
        switch (o[0]){
            case 0: System.exit(o[1]); break;
            case 1: System.exit(mem[o[1]]); break;
            case 2: System.exit(mem[mem[o[1]]]); break;
        }
    }
    private static void draw(){
        if (Lang.mode == 1) System.out.println("draw");
        w.drawFrame();
    }
    private static int lastR, lastG, lastB, lastX, lastY;
    private static void onMemChange(){
        if (Lang.mode == 1) System.out.println("mem changed");

        if (lastX != mem[255] || lastY != mem[254]) {
            mem[253] = w.getPix(mem[255], mem[254], 2);
            mem[252] = w.getPix(mem[255], mem[254], 1);
            mem[251] = w.getPix(mem[255], mem[254], 0);
            lastX = mem[255];
            lastY = mem[254];
        }
        else if (lastR != mem[253] || lastG != mem[252] || lastB != mem[251]) {
            int argb = ((0xFF) << 24) |
                    ((mem[253]) << 16) |
                    ((mem[252]) << 8) |
                    ((mem[251]) & 0xFF);
            w.setPix(mem[255], mem[254], argb);
            lastR = mem[253]; lastG = mem[252]; lastB = mem[251];
        }
    }
}
/*
 * 255 - point_x
 * 254 - point_y
 *
 * 253 - color_r
 * 252 - color_g
 * 251 - color_b
 *
 * 250 - mouse_x
 * 249 - mouse_y
 *
 * 248 - mouse_1_status
 * 247 - mouse_2_status
 * 246 - mouse_3_status
 */
