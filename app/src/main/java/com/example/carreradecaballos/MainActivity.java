package com.example.carreradecaballos;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView[] horses = new ImageView[10];
    private int[] horseProgress = new int[10];
    private boolean[] horseStopped = new boolean[10];
    private String[] horseNames = {"Caballo 1", "Caballo 2", "Caballo 3", "Caballo 4", "Caballo 5",
            "Caballo 6", "Caballo 7", "Caballo 8", "Caballo 9", "Caballo 10"};
    private Button stopAllButton, stopSingleButton;
    private EditText horseNumberInput;
    private Handler handler = new Handler();
    private Button startRaceButton, resumeRaceButton;
    private int[] horseSpeeds = new int[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRaceButton = findViewById(R.id.startRaceButton);
        resumeRaceButton = findViewById(R.id.resumeRaceButton);
        startRaceButton.setOnClickListener(v -> startRace());
        resumeRaceButton.setOnClickListener(v -> resumeRace());
        stopAllButton = findViewById(R.id.stopAllButton);
        stopSingleButton = findViewById(R.id.stopSingleButton);
        horseNumberInput = findViewById(R.id.horseNumberInput);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            horseSpeeds[i] = random.nextInt(5) + 1; // Velocidades entre 1 y 5
        }
        horses[0] = findViewById(R.id.horse1);
        horses[1] = findViewById(R.id.horse2);
        horses[2] = findViewById(R.id.horse3);
        horses[3] = findViewById(R.id.horse4);
        horses[4] = findViewById(R.id.horse5);
        horses[5] = findViewById(R.id.horse6);
        horses[6] = findViewById(R.id.horse7);
        horses[7] = findViewById(R.id.horse8);
        horses[8] = findViewById(R.id.horse9);
        horses[9] = findViewById(R.id.horse10);

        for (int i = 0; i < 10; i++) {
            horseProgress[i] = 0;
            horseStopped[i] = false;
        }

        stopAllButton.setOnClickListener(v -> {
            for (int i = 0; i < 10; i++) {
                horseStopped[i] = true;
            }
        });

        stopSingleButton.setOnClickListener(v -> {
            int horseNumber;
            try {
                horseNumber = Integer.parseInt(horseNumberInput.getText().toString()) - 1;
                if (horseNumber >= 0 && horseNumber < 10) {
                    horseStopped[horseNumber] = true;
                } else {
                    Toast.makeText(this, "Número de caballo inválido", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor, introduce un número válido", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void resumeRace() {
        for (int i = 0; i < 10; i++) {
            if (horseStopped[i]) {
                horseStopped[i] = false;
                moveHorse(i);
            }
        }
    }
    private void moveHorse(int horseIndex) {
        new Thread(() -> {
            while (horseProgress[horseIndex] < 100 && !horseStopped[horseIndex]) {
                try {
                    Thread.sleep(900 / horseSpeeds[horseIndex]); // La velocidad del caballo afecta el tiempo de espera
                    horseProgress[horseIndex] += 1;
                    handler.post(() -> {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) horses[horseIndex].getLayoutParams();
                        params.leftMargin = horseProgress[horseIndex] * 10;
                        horses[horseIndex].setLayoutParams(params);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (horseProgress[horseIndex] >= 100) {
                handler.post(() -> {
                    Toast.makeText(this, horseNames[horseIndex] + " ha ganado!", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    private void startRace() {
        for (int i = 0; i < 10; i++) {
            final int horseIndex = i;
            new Thread(() -> {
                while (horseProgress[horseIndex] < 100 && !horseStopped[horseIndex]) {
                    try {
                        Thread.sleep(900 / horseSpeeds[horseIndex]); // Puedes ajustar este valor para controlar la velocidad
                        horseProgress[horseIndex] += 1; // Aumentar el progreso del caballo
                        handler.post(() -> {
                            // Aquí puedes actualizar la posición del caballo en la UI
                            // Por ejemplo, cambiar el margen izquierdo del ImageView
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) horses[horseIndex].getLayoutParams();

                            params.leftMargin = horseProgress[horseIndex] * 10; // Ajusta el factor de multiplicación según el tamaño de la pantalla
                            horses[horseIndex].setLayoutParams(params);
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (horseProgress[horseIndex] >= 100) {
                    // Este caballo ha ganado
                    handler.post(() -> {
                        Toast.makeText(this, horseNames[horseIndex] + " ha ganado!", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }
    }
}
