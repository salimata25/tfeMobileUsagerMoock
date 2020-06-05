package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import sn.diotali.tfe_usager_dgid.utils.Constants;

public class NavBarActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nom_prenom, adresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);

        nom_prenom = findViewById(R.id.nom_prenom);
        adresse = findViewById(R.id.adresse);

        nom_prenom.setText(Constants.newUser.getFirstName() + " " + Constants.newUser.getLastName());
        adresse.setText(Constants.newUser.getAddress());

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.menu_histo).setOnClickListener(this);
        findViewById(R.id.menu_deconnect).setOnClickListener(this);
        findViewById(R.id.menu_compte).setOnClickListener(this);
        findViewById(R.id.menu_pwd).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.menu_histo:
                Intent intent = new Intent(getApplicationContext(), HistoriqueActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.menu_deconnect:
                intent = new Intent(getApplicationContext(), DiotaliLogin.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.menu_compte:
                intent = new Intent(getApplicationContext(), InfoCompteActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.menu_pwd:
                intent = new Intent(getApplicationContext(), ModifierPwdActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
