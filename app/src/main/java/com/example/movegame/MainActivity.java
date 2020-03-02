package com.example.movegame;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    EditText mdp;


    Dessin dessin;
    Game game;
    Boolean enCour = true;
    Switch aSwitch;
    TextView textView;
    TextView niveau;

    SensorManager sm;
    Sensor s;
    SensorEventListener sel;

    float dec = 2;

    long old = 0;
    long recent = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdp = findViewById(R.id.editText);

        aSwitch = findViewById(R.id.switch1);

        aSwitch.setChecked(true);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked() & !enCour){
                    enCour = true;
                    game.initialisation();
                }else if(!aSwitch.isChecked()){
                    enCour = false;
                }
            }
        });

        textView = findViewById(R.id.textView);

        niveau = findViewById(R.id.textView2);

        dessin = findViewById(R.id.dessin);
        game = new Game(dessin);

        sm= (SensorManager) getSystemService(SENSOR_SERVICE);
        s= sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sel= new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                String s = mdp.getText().toString();
                System.out.println(" @pppppppppppppppppppppppppppppp      "+s);
                if( init() & s.contains("753159") ){
                    choixDirection(sensorEvent);
                    enCour = game.step();
                    niveau.setText("Lv: "+game.feuX.size()+" :"+game.periode );

                }


            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    void choixDirection(SensorEvent sensorEvent){
        float axeX=sensorEvent.values[0];
        float axeY=sensorEvent.values[1];
        if(axeX > dec && (axeY < axeX && axeY > (-axeX)  ) ){
            game.modifDirectionRef(-1,0);
            textView.setText("GAUCHE");
        }else if(axeX < (-dec) && (axeY > axeX && axeY < (-axeX)  ) ){
            game.modifDirectionRef(1,0);
            textView.setText("DROIT");
        }else if(axeY > dec && (axeX < axeY && axeX > (-axeY)  ) ){
            game.modifDirectionRef(0,1);
            textView.setText("BAS");
        }else if(axeY < (-dec) && (axeX > axeY && axeX < (-axeY)  ) ){
            game.modifDirectionRef(0,-1);
            textView.setText("HAUT");
        }else{
            game.modifDirectionRef(0,0);
            textView.setText("PLAT");
        }
    }

    boolean init(){
        if(enCour & aSwitch.isChecked()){
            recent = System.currentTimeMillis();
            if((recent - old) > 200 ){
                // action
                System.out.println(" =================================   STEP");
                old = recent;
                return true;
            }
        }else{
            if (!enCour & aSwitch.isChecked()) {
                aSwitch.setChecked(false);
            }
            System.out.println("////////////////////////////////////FIN");
            textView.setText("STOP");
            if(game.fin == 1){
                textView.setText("FEU");
            }else if (game.fin == 2){
                textView.setText("WIN");
            }else if(game.fin == 4){
                textView.setText("F>100");
            }
            return false;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(s!=null) sm.unregisterListener(sel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(s!=null) sm.registerListener(sel,s,SensorManager.SENSOR_DELAY_NORMAL);
    }



}
