package engine;

import java.util.HashMap;
import org.lwjgl.input.Mouse;

public class MouseInput {

    private static HashMap<Integer, Integer> buttonMap = new HashMap();
    private static double mouseDX;
    private static double mouseDY;

    public static boolean clicked(int button) {
        return clickedDelay(button, 1);
    }

    public static boolean clickedDelay(int button, int d) {
        return pressed(button) && buttonMap.get(button).equals(d);
    }

    public static double getMouseDX() {
        return mouseDX;
    }

    public static double getMouseDY() {
        return mouseDY;
    }

    public static boolean pressed(int button) {
        return buttonMap.containsKey(button);
    }

    public static void update() {
        mouseDX = Mouse.getDX();
        mouseDY = Mouse.getDY();
        while (Mouse.next()) {
            int button = Mouse.getEventButton();
            if (Mouse.getEventButtonState()) {
                buttonMap.put(button, 0);
            } else {
                buttonMap.remove(button);
            }
        }
        for (int b : buttonMap.keySet()) {
            buttonMap.put(b, buttonMap.get(b) + 1);
        }
    }
}
