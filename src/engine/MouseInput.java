package engine;

import java.util.HashMap;
import org.lwjgl.input.Mouse;

public class MouseInput {

    public static HashMap<Integer, Integer> buttonMap = new HashMap();

    public static boolean clicked(int button) {
        return clickedDelay(button, 1);
    }

    public static boolean clickedDelay(int button, int d) {
        return pressed(button) && buttonMap.get(button).equals(d);
    }

    public static boolean pressed(int button) {
        return buttonMap.containsKey(button);
    }

    public static void update() {
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
