package entities;

import collisions.Vector;
import graphics.Graphics;

public class Wall extends Solid {

    public Wall(Vector pos) {
        super(pos);
        bounds.size = new Vector(16, 16, 16);
        setSprite("Wall");
    }
    
    @Override
    public void draw() {
        //Top
        Graphics.drawSprite(sprite, pos.x, pos.y, pos.z + 16, (int) Math.round(imageIndex), 0);
        //Bottom
        Graphics.drawSprite(sprite, pos.x, pos.y, pos.z - 16, (int) Math.round(imageIndex), 0);
        
        //Sides
        Graphics.drawSprite(sprite, pos.x, pos.y, pos.z, (int) Math.round(imageIndex), 0);
        Graphics.drawSprite(sprite, pos.x, pos.y - 16, pos.z, (int) Math.round(imageIndex), 0);
    }

}
