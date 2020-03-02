package com.example.movegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class Dessin extends View {


    Point ref = null;

    ArrayList<Point> lp = new ArrayList<>();

    ArrayList<Point> feu = new ArrayList<>();

    int color= Color.BLACK;
    float epaisseur = 10;
    float echelle = 40;





    Paint paint = new Paint();


    public Dessin(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

void removeAll(){
        ref = null;
        lp.clear();
        feu.clear();
}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        epaisseur = (canvas.getWidth()/echelle);

        Point p;
        float x;
        float y;

        for (int i=0; i<lp.size();i++){
            p= lp.get(i);
// ?????? modifir presision zoom echelle
            paint.setStrokeWidth(epaisseur);
            paint.setColor(color);//p.couleur);
            x= ((p.x)*(canvas.getWidth()/echelle));
            y= ((p.y)*(canvas.getWidth()/echelle));
            canvas.drawPoint(x,y,paint);
            System.out.println("------x:"+p.x+" y:"+p.y+"---DrawArtivé"+i);
            // recherche sur paint et Canvas;
        }


        if(!feu.isEmpty()){
            paint.setStrokeWidth(epaisseur);
            paint.setColor(Color.RED);//p.couleur);

            for (Point f: feu) {
                x = ((f.x) * (canvas.getWidth() / echelle));
                y = ((f.y) * (canvas.getWidth() / echelle));
                canvas.drawPoint(x, y, paint);
            }
        }

        if(ref != null) {
            paint.setStrokeWidth(epaisseur);
            paint.setColor(Color.GREEN);//p.couleur);
            x= ((ref.x)*(canvas.getWidth()/echelle));
            y= ((ref.y)*(canvas.getWidth()/echelle));
            canvas.drawPoint(x, y, paint);
        }

        System.out.println("----------------------------------DrawArtivéFin");
    }


    public void modifEchelle(float e){
        echelle = e;

    //    invalidate();
    }


    public boolean modifRef(int x, int y){
        Point p=new Point(x,y);
        return modifRef(p);
    }

    public boolean modifFeu(int x, int y, int num){
        Point p=new Point(x,y);
        return modifFeu(p,num);
    }

    public boolean ajoutPoint(int x, int y) {
        Point p=new Point(x,y);
        return ajoutPoint(p);
    }


    public boolean modifRef(Point p){
        ref = p;
        invalidate ();
        return true;
    }

    public boolean modifFeu(Point p, int num){

        if( feu.isEmpty() || feu.size() <= num){
            feu.add(p);
        }else{
            feu.set(num,p);
        }
        invalidate ();
        return true;
    }

    public boolean ajoutPoint(Point p) {
        lp.add(p);
        return true;
    }

    void rePaint(){
        invalidate();
    }






}
