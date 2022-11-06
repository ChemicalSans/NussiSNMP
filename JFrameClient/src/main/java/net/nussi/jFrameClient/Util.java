package net.nussi.jFrameClient;

import java.awt.*;

public class Util {

    public static final Color dark_red = new Color(100, 0, 0, 255);
    public static final Color dark_green = new Color(0, 100, 0,250);
    public static final Color dark_gray = new Color(50,50,50,255);

    public static String StringPadLeft(String string, int length){
        return String.format("%1$"+length+ "s", string);
    }

}
