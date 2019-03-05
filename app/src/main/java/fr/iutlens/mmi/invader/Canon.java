package fr.iutlens.mmi.invader;

import android.graphics.RectF;
import android.widget.ProgressBar;

import java.util.List;

import fr.iutlens.mmi.invader.utils.SpriteSheet;

/**
 * Created by dubois on 05/12/2018.
 */

class Canon extends Sprite {
    public static final int SPEED = 30;
    private final List<Projectile> laser;
    private final int dxLaser;
    private final int dyLaser;
    float vx = 0;

    Canon(int id, float x, float y, List<Projectile> laser) {
        super(id, x, y);
        this.laser = laser;

        final SpriteSheet laserSprite = SpriteSheet.get(R.mipmap.newlaser);
        dxLaser = sprite.w/2- laserSprite.w/2;

        dyLaser = -laserSprite.h;
    }

    @Override
    public boolean act() {
        RectF bounds = getBoundingBox();
        float dx = vx * SPEED;
        if (bounds.left + dx > 0 && bounds.right + dx < GameView.SIZE_X) {
            x += dx;
        } else {
            vx = 0;
        }
        if(hit){
            hit = false;
            return true;

        }else{
            return false;

        }
    }

    public void setDirection(float dx) {
        vx = dx;
    }


    public void fire() {
        laser.add(new Projectile(R.mipmap.newlaser,x+dxLaser,y+dyLaser,-20));


    }

    public void testIntersection(List<Projectile> missile) {
        for (Projectile p : missile) {
            RectF bbox = p.getBoundingBox();
            if (bbox.intersect(this.getBoundingBox())) {
                this.hit = true;
                p.hit = true;

            }

        }

    }/*
    public void vie(){
        this.vie--;
        if(vie == 0){
            return gameover();
        }
       private void gameover() {
        ProgressBar p = p.findViewById(R.id.progressBarVie);


    }
    */



}
