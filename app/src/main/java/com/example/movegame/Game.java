package com.example.movegame;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;

public class Game {

class BouleDeFeu {
    public BouleDeFeu(Point pos0, Point direction0){
        pos = pos0;
        direction = direction0;
    }
    Point pos;
    Point direction;

    @Override
    public boolean equals(Object obj) {
        if( obj == null) return false;
        Point p = new Point();
        if (obj.getClass() == p.getClass() ){
            if(pos.equals(obj)){
                return true;
            }
        }
        return false;
    }
}

    public int fin = 0;

    Dessin dessin;

    ArrayList<Point> mur = new ArrayList<>();

    Point ref = null;
    Point directionRef = null;


    ArrayList<BouleDeFeu> feuX = new ArrayList<>();

    static int MAX_PERIODE_FEU = 500;

    int periode = MAX_PERIODE_FEU ;



    int echelle = 40;

    public Game(Dessin d){
        dessin = d;
        int echelle = (int)dessin.echelle;
        System.out.println("----------------------------------------@@@@@@@@@@@"+echelle);

        initialisation();

    }

    void initialisation(){
        fin = 0;
        periode = MAX_PERIODE_FEU;
        mur.clear();
        feuX.clear();
        ref = null;
        dessin.removeAll();


        for(int i = 0 ; i < echelle ; i++){
            for(int j = 0 ; j < echelle ; j++){
                if( i <= 2 || i >= echelle-2 || j <= 2 || j >= echelle-2 ) {
                    mur.add(new Point(i,j));
                    dessin.ajoutPoint(i, j);
                    System.out.println("----------------------------------------dd  "+i+" @ "+j+" ?");
                }
            }
        }
        ref = new Point(2,10);
        dessin.modifRef(ref);


        creeBouleDeFeu();

    }

    Boolean creeBouleDeFeu() {


        if(feuX.size() >= 100){
            fin = 4;
            return false;
        }


        for (int i = 2 ; i< echelle -2 ;i ++){
            for (int j = 2 ; j< echelle -2 ;j ++){
                Point p = new Point(i,j);
                if( !mur.contains(p) &

                        mur.contains(new Point(i,j+1)) &
                        mur.contains(new Point(i,j-1))&
                        mur.contains(new Point(i+1,j))&
                        mur.contains(new Point(i-1,j))&
                        mur.contains(new Point(i+1,j+1))&
                        mur.contains(new Point(i+1,j-1))&
                        mur.contains(new Point(i-1,j+1))&
                        mur.contains(new Point(i-1,j-1)) ){

                    if( !feuX.contains(p)){
                        mur.add(p);
                        dessin.ajoutPoint(p);
                    }

                }
            }
        }





        int inteval = 6;

        ArrayList<Point> espace = new ArrayList<>();
        for (int x = ref.x-inteval ; x < ref.x+inteval ; x++){
            for (int y = ref.y-inteval ; y < ref.y+inteval ; y++){
                Point p = new Point(x,y);
                if(x > 0 & y > 0 & x < 40 & y < 40 & !mur.contains(p) & !feuX.contains(p)){
                    espace.add(p);
                }
            }
        }

        int placeLibre = (echelle * echelle) - (mur.size() + feuX.size() + espace.size() );

        if( placeLibre == 0 ){
            fin=2;
            return false;
        }

        Random random = new Random();
        int choix = random.nextInt(placeLibre);
        int num = 0;
        for (int i = 0; i < echelle; i++){
            for (int j = 0; j < echelle; j++) {
                Point p = new Point(i, j);
                if (!mur.contains(p)) {
                    if (!feuX.contains(p)) {
                        if(!espace.contains(p)){
                            if(num == choix){
                                //
                                int dx = random.nextInt(3)-1;
                                int dy = random.nextInt(3)-1;

                                if( dx == 0){
                                    dx = -1;
                                }
                                if (dy == 0){
                                    dy = -1;
                                }

                                feuX.add(new BouleDeFeu(p,new Point(dx,dy)));
                                dessin.modifFeu(feuX.get(feuX.size()-1).pos,feuX.size()-1);
                                return true;
                            }else {
                                num += 1;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    void modifDirectionRef(int x,int y){
        directionRef= new Point(x,y);
    }

    boolean step(){



        if(directionRef != null){
            Point prédiction = new Point(ref);
            prédiction.offset(directionRef.x, directionRef.y);
            if( prédiction.x > 1 & prédiction.x < echelle - 1 & prédiction.y > 1 & prédiction.y < echelle - 1 ) {

                if( !mur.contains(ref)) {
                    Point a = new Point(ref);
                    mur.add(a);
                    dessin.ajoutPoint(a);
                }
                ref = prédiction;
                dessin.modifRef(ref);
            }
        }

        if(!feuX.isEmpty()) {
            int i = 0;



            for (BouleDeFeu feu : feuX) {


                BouleDeFeu bdf = reactionBDF(feu);
                feu.pos = bdf.pos;
                feu.direction = bdf.direction;

                dessin.modifFeu(feu.pos, i);

                if (ref.equals(feu.pos)) { // ?? zonz ?
                    fin = 1;
                    return false;
                }
                i++;
            }
        }

        ///??
        if( periode <= 0){
            if( !creeBouleDeFeu() ){
                dessin.rePaint();
                //fin = 2;
                return false;
            }
            periode = MAX_PERIODE_FEU;
        }else{
            periode --;
        }

        dessin.rePaint();
        return true;
    }

BouleDeFeu reactionBDF(BouleDeFeu feu) {

    Point pos;
    Point directionFeu;
    pos = new Point(feu.pos);
    Point prédiction = new Point(pos);
    directionFeu = new Point(feu.direction);
    prédiction.offset(directionFeu.x, directionFeu.y);

    if (mur.contains(prédiction)) { // ??? zone???


        if(directionFeu.x != 0 & directionFeu.y != 0){

            prédiction = new Point(pos);
            prédiction.offset(directionFeu.x,-directionFeu.y);
            if(!mur.contains(prédiction)){
                directionFeu = new Point(directionFeu.x, -directionFeu.y);
            }else{
                prédiction = new Point(pos);
                prédiction.offset(-directionFeu.x,directionFeu.y);
                if(!mur.contains(prédiction)){
                    directionFeu = new Point(-directionFeu.x, directionFeu.y);
                }else {
                    directionFeu = new Point(-directionFeu.x, -directionFeu.y);
                }
            }
        }else {
            directionFeu = new Point(-directionFeu.x, -directionFeu.y);
        }
        pos.offset(directionFeu.x, directionFeu.y);
    } else {
        pos = prédiction;
    }

    return new BouleDeFeu(pos,directionFeu);

}


}

