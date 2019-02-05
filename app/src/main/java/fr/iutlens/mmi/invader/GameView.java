package fr.iutlens.mmi.invader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.iutlens.mmi.invader.utils.Pad;
import fr.iutlens.mmi.invader.utils.RefreshHandler;
import fr.iutlens.mmi.invader.utils.SpriteSheet;
import fr.iutlens.mmi.invader.utils.TimerAction;


public class GameView extends View implements TimerAction{
    private RefreshHandler timer;

    // taille de l'écran virtuel
    public final static int SIZE_X = 2000;
    public final static int SIZE_Y = 2400;

    // transformation (et son inverse)
    private Matrix transform;
    private Matrix reverse;

    //liste des sprites à afficher


    private Armada armada;
    private Canon canon;
    private List<Projectile> missile;
    private List<Projectile> laser;
    private Pad pad;
    private ArrayList<Shield> shield;
    int vie;
    private TextView textView;
    private TextView vieView;


    public int getVie() {
        return vie;
    }

    public void setVie(int vie) {
        this.vie = this.vie - vie;
        if (vieView != null)
        {
            vieView.setText(":"+this.vie);
            gameover();
        }

    }

    private void gameover() {
        if(this.vie==0){

        }
    }


    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Initialisation de la vue
     *
     * Tous les constructeurs (au-dessus) renvoient ici.
     *
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {

        // Chargement des feuilles de sprites
        SpriteSheet.register(R.mipmap.alien,2,1,this.getContext());
        SpriteSheet.register(R.mipmap.missile,4,1,this.getContext());
        SpriteSheet.register(R.mipmap.laser,1,1,this.getContext());
        SpriteSheet.register(R.mipmap.canon,1,1,this.getContext());
        SpriteSheet.register(R.mipmap.carre,1,1,this.getContext());


        transform = new Matrix();
        reverse = new Matrix();

        missile = new ArrayList<>();
        laser = new ArrayList<>();
        shield = new ArrayList<>();


        for(int i = 0; i <20; i++) {
            for (int j = 0; j < 8; j++) {
                shield.add(new Shield(R.mipmap.carre, 200 + i * 4, 1800 + j * 4));
            }
        }

        armada = new Armada(R.mipmap.alien,missile);
        canon = new Canon(R.mipmap.canon,800, 2200,laser);


        vie = 10;

//        hero = new Hero(R.drawable.running_rabbit,SPEED);



        // Gestion du rafraichissement de la vue. La méthode update (juste en dessous)
        // sera appelée toutes les 30 ms
        timer = new RefreshHandler(this);

        // Un clic sur la vue lance (ou relance) l'animation
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timer.isRunning()) timer.scheduleRefresh(30);
            }
        });
    }


    public static void act(List list){
        Iterator it = list.iterator();
        while (it.hasNext()) if (((Sprite) it.next()).act()) it.remove();
    }
    /**
     * Mise à jour (faite toutes les 30 ms)
     */
    @Override
    public void update() {
        if (this.isShown()) { // Si la vue est visible
            timer.scheduleRefresh(30); // programme le prochain rafraichissement
            armada.testIntersection(laser);
            armada.act();
            if (pad != null){
                canon.setDirection(pad.getDx());
            }

            testIntersection();

            canon.testIntersection(missile);

            if(canon.act()){
                setVie(1);
            }


            act(missile);
            act(laser);
            act(shield);

            invalidate(); // demande à rafraichir la vue
        }
    }



    private void testIntersection() {
        for(Projectile p : missile){
            RectF bbox = p.getBoundingBox();
            for(Shield a: shield){
                if (bbox.intersect(a.getBoundingBox())){
                    a.hit = true;
                    p.hit = true;
                }
            }
        }
        for(Projectile p : laser){
            RectF bbox = p.getBoundingBox();
            for(Shield a: shield){
                if (bbox.intersect(a.getBoundingBox())){
                    a.hit = true;
                    p.hit = true;
                }
            }
        }
    }

    /**
     * Méthode appelée (automatiquement) pour afficher la vue
     * C'est là que l'on dessine le décor et les sprites
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // On met une IMAGE de fond

//        canvas.drawColor(0xff000077);


        // On choisit la transformation à appliquer à la vue i.e. la position
        // de la "camera"

        canvas.concat(transform);

        for(Sprite s : missile){
            s.paint(canvas);
        }
        for(Sprite s : laser){
            s.paint(canvas);
        }
        for(Sprite s : shield){
            s.paint(canvas);
        }

        canon.paint(canvas);
        armada.paint(canvas);


        // Dessin des différents éléments
/*        level.paint(canvas,current_pos);

        float x = 1;
        float y = hero.getY();
        hero.paint(canvas,level.getX(x),level.getY(y));
*/
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setZoom(w, h);
    }

    /***
     * Calcul du centrage du contenu de la vue
     * @param w
     * @param h
     */
    private void setZoom(int w, int h) {
        if (w<=0 ||h <=0) return;

        // Dimensions dans lesquelles ont souhaite dessiner
        RectF src = new RectF(0,0,SIZE_X,SIZE_Y);

        // Dimensions à notre disposition
        RectF dst = new RectF(0,0,w,h);

        // Calcul de la transformation désirée (et de son inverse)
        transform.setRectToRect(src,dst, Matrix.ScaleToFit.START);
        transform.invert(reverse);
    }







    public void onFire(){
        canon.fire();

    }


    public void setPad(Pad pad) {
        this.pad = pad;
    }

    public void setVieView(TextView vieView) {
        this.vieView = vieView;
        setVie(0);
    }

    public TextView getVieView() {
        return vieView;
    }
}
