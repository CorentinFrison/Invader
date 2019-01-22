package fr.iutlens.mmi.invader;

import android.graphics.RectF;

import java.util.List;

import fr.iutlens.mmi.invader.utils.SpriteSheet;

/**
 * Created by dubois on 05/12/2018.
 */

class Canon extends Sprite {
    public static final int SPEED = 12;
    private final List<Projectile> laser;
    private final int dxLaser;
    private final int dyLaser;
    float vx = 0;
    private Canon canon;

    Canon(int id, float x, float y, List<Projectile> laser) {
        super(id, x, y);
        this.laser = laser;
        final SpriteSheet laserSprite = SpriteSheet.get(R.mipmap.laser);
        dxLaser = sprite.w/2- laserSprite.w/2;
        dyLaser = -laserSprite.h;
    }

    @Override
    public boolean act() {
        RectF bounds = getBoundingBox();
        float dx = vx * SPEED;
        if (bounds.left+dx>0 && bounds.right+dx< GameView.SIZE_X){
            x += dx;
        } else {
            vx = 0;
        }
        return false;
    }

    public void setDirection(float dx) {
        vx = dx;
    }


    public void fire() {
        laser.add(new Projectile(R.mipmap.laser,x+dxLaser,y+dyLaser,-20));

    }


}
