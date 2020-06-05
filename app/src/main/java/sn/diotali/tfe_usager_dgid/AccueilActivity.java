package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccueilActivity extends AppCompatActivity {
    TextView txt_connexion;
    Button btn_inscription;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        txt_connexion = findViewById(R.id.txt_connexion);
        btn_inscription = findViewById(R.id.btn_inscription);

        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccueilActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });

        txt_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getSharedPreferences("MyConfigMarchand", MODE_PRIVATE);
                sharedPreferences.edit().putString("SeConnecter", "oui").commit();


                Intent intent = new Intent(AccueilActivity.this, DiotaliLogin.class);
                startActivity(intent);
            }
        });
    }
}
