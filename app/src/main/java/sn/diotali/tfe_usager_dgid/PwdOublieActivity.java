package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
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

public class PwdOublieActivity extends DiotaliMain implements View.OnClickListener{

    EditText nouveau_pwd, confim_pwd;
    TextView txt_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_oublie);

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_modifier).setOnClickListener(this);

        txt_error = findViewById(R.id.txt_v_error);
        txt_error.setVisibility(View.INVISIBLE);

        Log.d("PwdOublie USER PHONE", Constants.newUser.toString());

        nouveau_pwd = findViewById(R.id.txt_nouveau_pwd);
        confim_pwd = findViewById(R.id.txt_confirmer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_modifier:
                String nouveau = nouveau_pwd.getText().toString();
                String confirmer = confim_pwd.getText().toString();

                String regex = " ";
                String replacement = "";

                if (nouveau.isEmpty() || nouveau.replaceAll(regex, replacement).isEmpty()) {
                    nouveau_pwd.setError("Nouveau mot de pass");
                } else if (confirmer.isEmpty() || confirmer.replaceAll(regex, replacement).isEmpty()) {
                    confim_pwd.setError("Confirmer le mot de passe");
                }else if (!(confirmer.equals(nouveau))) {
                    txt_error.setText("Veuillez confirmer le nouveau mot de passe !");
                    txt_error.setVisibility(View.VISIBLE);
                } else {

                    txt_error.setVisibility(View.INVISIBLE);
                    InscriptionRequest params = new InscriptionRequest("", Constants.newUser.getEmail(), Constants.newUser.getPhone(), nouveau, "");
                    Log.i(this.getClass().getName(), params.toString());
                    ServicesTask task =  new ServicesTask(PwdOublieActivity.this);
                    ServiceParams service = new ServiceParams();
                    service.setMethodName(Constants.Methods.NEW_PWD);
                    service.setMethodParams(params);
                    task.execute(service);

                }
        }
    }

    @Override
    public void sendTaskResponse(ServiceResult serviceResult) {
        try {
            Log.d("DIOTALI PwdOubli", "receiving response");
            if (Constants.Methods.NEW_PWD.equals(serviceResult.getMethod())) {
                if (Constants.ResponseStatus.OK == serviceResult.getStatus()) {
                    User response = (User) serviceResult;
                    Log.d("PwdOubli sendTaskRespon", response.toString());
                    Intent intent = new Intent(this, DiotaliLogin.class);
                    Constants.newUser = response;
                    startActivity(intent);
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
