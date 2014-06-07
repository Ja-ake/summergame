package engine;

import java.util.HashMap;
import org.lwjgl.input.Keyboard;

public class Keys {

    public static HashMap<Integer, Integer> keyMap = new HashMap();

    public static boolean clicked(int key) {
        return clickedDelay(key, 1);
    }

    public static boolean clickedDelay(int key, int d) {
        return pressed(key) && keyMap.get(key).equals(Integer.valueOf(d));
    }

    public static boolean pressed(int key) {
        return keyMap.containsKey(key);
    }

    public static void update() {
        while (Keyboard.next()) {
            int key = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                keyMap.put(key, 0);
            } else {
                keyMap.remove(key);
            }
        }
        for (int k : keyMap.keySet()) {
            keyMap.put(k, keyMap.get(k) + 1);
        }
    }
}
