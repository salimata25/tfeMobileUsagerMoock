package sn.diotali.tfe_usager_dgid;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import sn.diotali.tfe_usager_dgid.services.ServiceParams;
import sn.diotali.tfe_usager_dgid.services.ServiceResult;
import sn.diotali.tfe_usager_dgid.services.ServicesTask;
import sn.diotali.tfe_usager_dgid.types.InscriptionRequest;
import sn.diotali.tfe_usager_dgid.types.User;
import sn.diotali.tfe_usager_dgid.utils.Constants;

public class InfoCompteActivity extends DiotaliMain implements View.OnClickListener{

    EditText txt_cni, txt_nom, txt_prenom, txt_tel, txt_adresse, txt_email;
    TextView txt_error;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_compte);

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_modifier).setOnClickListener(this);

        txt_error = findViewById(R.id.txt_v_error);
        txt_error.setVisibility(View.INVISIBLE);

        txt_cni = findViewById(R.id.txt_cni);
        txt_nom = findViewById(R.id.txt_nom);
        txt_prenom = findViewById(R.id.txt_prenom);
        txt_adresse = findViewById(R.id.txt_adresse);
        txt_tel = findViewById(R.id.txt_tel);
        txt_email = findViewById(R.id.txt_email);

        txt_cni.setText(Constants.newUser.getNin());
        txt_nom.setText(Constants.newUser.getLastName());
        txt_prenom.setText(Constants.newUser.getFirstName());
        txt_adresse.setText(Constants.newUser.getAddress());
        txt_tel.setText(Constants.newUser.getPhone());
        txt_email.setText(Constants.newUser.getEmail());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_modifier:
                String cni = txt_cni.getText().toString();
                String nom = txt_nom.getText().toString();
                String prenom = txt_prenom.getText().toString();
                String email = txt_email.getText().toString();
                String tel = txt_tel.getText().toString();
                String adresse = txt_adresse.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String regex = " ";
                String replacement = "";

                if (cni.isEmpty() || cni.replaceAll(regex, replacement).isEmpty()) {
                    txt_cni.setError("CNI");
                } else if (cni.length() != 13) {
                    txt_cni.setError("Veuillez saisir un NIN valide !");
                } else if (nom.isEmpty() || nom.replaceAll(regex, replacement).isEmpty()) {
                    txt_nom.setError("Nom");
                } else if (prenom.isEmpty() || prenom.replaceAll(regex, replacement).isEmpty()) {
                    txt_prenom.setError("Prénom");
                } else if (email.isEmpty() || email.replaceAll(regex, replacement).isEmpty()) {
                    txt_email.setError("Email");
                }else if (!email.matches(emailPattern)) {
                    txt_email.setError("Email invalide");
                } else if (tel.isEmpty() || tel.replaceAll(regex, replacement).isEmpty()) {
                    txt_tel.setError("Téléphone");
                } else if (tel.length() < 9 || tel.length() > 14) {
                    txt_tel.setError("Veuillez saisir un numéro valide !");
                } else if (adresse.isEmpty() || adresse.replaceAll(regex, replacement).isEmpty()) {
                    txt_adresse.setError("Adresse");
                }else {
                    InscriptionRequest params = new InscriptionRequest(Constants.newUser.getToken(), prenom, nom, adresse, tel, cni, email,"update");
                    Log.i(this.getClass().getName(), params.toString());
                    ServicesTask task =  new ServicesTask(InfoCompteActivity.this);
                    ServiceParams service = new ServiceParams();
                    service.setMethodName(Constants.Methods.UPDATE_INFO);
                    Log.d("login  params",params.toString());
                    service.setMethodParams(params);
                    task.execute(service);
                }
        }
    }

    @Override
    public void sendTaskResponse(ServiceResult serviceResult) {
        try {
            Log.d("DIOTALI LOGIN", "receiving response");
            if (Constants.Methods.UPDATE_INFO.equals(serviceResult.getMethod())) {
                if (Constants.ResponseStatus.OK == serviceResult.getStatus()) {
                    User response = (User) serviceResult;
                    Log.d(this.getClass().getName(), "update sendTaskResponse 1");
                    sharedPreferences = getSharedPreferences("MyConfigMarchand", MODE_PRIVATE);
                    sharedPreferences.edit().putString("FirstNameStorage", response.getFirstName()).commit();
                    sharedPreferences.edit().putString("LastNameStorage", response.getLastName()).commit();
                    sharedPreferences.edit().putString("AddressStorage", response.getAddress()).commit();
                    sharedPreferences.edit().putString("PhoneStorage", response.getPhone()).commit();
                    sharedPreferences.edit().putString("NinStorage", response.getNin()).commit();
                    sharedPreferences.edit().putString("EmailStorage", response.getEmail()).commit();
                    Log.d(this.getClass().getName(), "update sendTaskResponse 2");
                    Constants.newUser.setEmail(response.getEmail());
                    Constants.newUser.setFirstName(response.getFirstName());
                    Constants.newUser.setLastName(response.getLastName());
                    Constants.newUser.setAddress(response.getAddress());
                    Constants.newUser.setPhone(response.getPhone());
                    Constants.newUser.setNin(response.getNin());
                    Log.d(this.getClass().getName(), "update sendTaskResponse 3");
                    txt_error.setTextColor(Color.GREEN);
                    txt_error.setText("Informations modifié avec succés");
                    txt_error.setVisibility(View.VISIBLE);
                    Log.d(this.getClass().getName(), "update sendTaskResponse 4");
                } else {
                    Log.d(this.getClass().getName(), "sendTaskResponse error");
                    txt_error.setText(serviceResult.getMessage());
                    txt_error.setVisibility(View.VISIBLE);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
