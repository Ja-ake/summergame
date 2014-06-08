package entities;

import engine.Keys;
import engine.MouseInput;
import org.lwjgl.input.Keyboard;

class ControlPacket {

    boolean forward;
    boolean back;
    boolean left;
    boolean right;
    boolean jump;
    boolean sprint;

    boolean leftClick;
    boolean rightClick;
    double mouseX;
    double mouseY;

    void update() {
        right = Keys.pressed(Keyboard.KEY_D) && !Keys.pressed(Keyboard.KEY_A);
        left = Keys.pressed(Keyboard.KEY_A) && !Keys.pressed(Keyboard.KEY_D);
        forward = Keys.pressed(Keyboard.KEY_W) && !Keys.pressed(Keyboard.KEY_S);
        back = Keys.pressed(Keyboard.KEY_S) && !Keys.pressed(Keyboard.KEY_W);
        jump = Keys.pressed(Keyboard.KEY_SPACE);
        sprint = Keys.pressed(Keyboard.KEY_LSHIFT);
        mouseX = MouseInput.getMouseDX();
        mouseY = MouseInput.getMouseDY();
        leftClick = MouseInput.clicked(0);
        rightClick = MouseInput.clicked(1);
    }
}
