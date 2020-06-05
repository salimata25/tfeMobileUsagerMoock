package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class ReinitialiserPwdActivity extends DiotaliMain implements View.OnClickListener {
    EditText email, phone;
    TextView txt_error;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reinitialiser_pwd);

        findViewById(R.id.btn_connect).setOnClickListener(this);

        email = findViewById(R.id.edt_email);
        phone = findViewById(R.id.edt_tel);

        txt_error = findViewById(R.id.txt_v_error);
        txt_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                txt_error.setVisibility(View.INVISIBLE);

                String mail = email.getText().toString();
                String tel = phone.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String regex = " ";
                String replacement = "";

                if (mail.isEmpty() || mail.replaceAll(regex, replacement).isEmpty()) {
                    email.setError("Adresse email");
                }else if (!mail.matches(emailPattern)) {
                    email.setError("Email invalide");
                }  else if (tel.isEmpty() || tel.replaceAll(regex, replacement).isEmpty()) {
                    phone.setError("Numéro de téléphone");
                } else {
                    InscriptionRequest params = new InscriptionRequest(tel, mail);
                    Log.i(this.getClass().getName(), params.toString());
                    /*Intent intent = new Intent(this, CodeSmsPwdActivity.class);
                    startActivity(intent);*/
                    ServicesTask task =  new ServicesTask(ReinitialiserPwdActivity.this);
                    ServiceParams service = new ServiceParams();
                    service.setMethodName(Constants.Methods.PWD_OUBLIE);
                    Log.d("login  params",params.toString());
                    service.setMethodParams(params);
                    task.execute(service);
                }

                break;
        }
    }

    @Override
    public void sendTaskResponse(ServiceResult serviceResult) {
        try {
            Log.d("DIOTALI LOGIN", "receiving response");
            if (Constants.Methods.PWD_OUBLIE.equals(serviceResult.getMethod())) {
                if (Constants.ResponseStatus.OK == serviceResult.getStatus()) {
                    User response = (User) serviceResult;
                    Log.d("DIOTALI sendTaskRespon", response.toString());
                    sharedPreferences = getSharedPreferences("MyConfigMarchand", MODE_PRIVATE);
                    sharedPreferences.edit().putString("PhoneStorage", response.getPhone()).commit();
                    sharedPreferences.edit().putString("EmailStorage", response.getEmail()).commit();
                    //Constants.newUser = new User();
                    Constants.newUser.setEmail(response.getEmail());
                    Constants.newUser.setPhone(response.getPhone());

                    Log.d("ReinitialiserPwd USERPH", Constants.newUser.toString());

                    Intent intent = new Intent(this, CodeSmsPwdActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("LOGIN", "login error");
                    txt_error.setText(serviceResult.getMessage());
                    txt_error.setVisibility(View.VISIBLE);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
