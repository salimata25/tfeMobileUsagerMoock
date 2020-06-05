package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Dialog;

import sn.diotali.tfe_usager_dgid.eventActions.DoubleTapCallback;
import sn.diotali.tfe_usager_dgid.eventActions.DoubleTapListener;
import sn.diotali.tfe_usager_dgid.services.ServiceParams;
import sn.diotali.tfe_usager_dgid.services.ServiceResult;
import sn.diotali.tfe_usager_dgid.services.ServicesTask;
import sn.diotali.tfe_usager_dgid.types.LoginRequest;
import sn.diotali.tfe_usager_dgid.types.User;
import sn.diotali.tfe_usager_dgid.utils.Constants;
import sn.diotali.tfe_usager_dgid.utils.DiotaliUtils;


public class DiotaliLogin extends DiotaliMain implements DoubleTapCallback {

    private EditText login;
    private EditText password;
    private TextView errorView, pwd_oublie;

    SharedPreferences sharedPreferences;
    ImageView id_logoImage;
    EditText new_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Admin
        sharedPreferences = getSharedPreferences("MyConfigMarchand", MODE_PRIVATE);

        String tokenStorage = sharedPreferences.getString("TokenStorage","tokenStorage");
        String seConnecter = sharedPreferences.getString("SeConnecter","non");

        if (tokenStorage.equals("tokenStorage") && seConnecter.equals("non")){
            sharedPreferences.edit().putString("SeConnecter", "non").commit();
            Intent intent = new Intent(this, AccueilActivity.class);
            startActivity(intent);
        }
        else {
            sharedPreferences.edit().putString("SeConnecter", "non").commit();
            setContentView(R.layout.activity_diotali_login);

            String emailStorage = sharedPreferences.getString("EmailStorage","emailStorage");
            String roleStorage = sharedPreferences.getString("RoleStorage", "roleStorage");
            String firstNameStorage = sharedPreferences.getString("FirstNameStorage", "firstNameStorage");
            String lastNameStorage = sharedPreferences.getString("LastNameStorage", "lastNameStorage");
            String addressStorage = sharedPreferences.getString("AddressStorage", "addressStorage");
            String phoneStorage = sharedPreferences.getString("PhoneStorage", "phoneStorage");
            String ninStorage = sharedPreferences.getString("NinStorage", "ninStorage");

            Constants.newUser = new User();
            Constants.newUser.setToken(tokenStorage);
            Constants.newUser.setTerminalNumber(DiotaliUtils.getSerialNumber());
            Constants.newUser.setEmail(emailStorage);
            Constants.newUser.setRole(roleStorage);
            Constants.newUser.setFirstName(firstNameStorage);
            Constants.newUser.setLastName(lastNameStorage);
            Constants.newUser.setAddress(addressStorage);
            Constants.newUser.setPhone(phoneStorage);
            Constants.newUser.setNin(ninStorage);


            Log.i("------newUser------",Constants.newUser.toString());
            login = findViewById(R.id.edt_login);
            if ( !emailStorage.equals("emailStorage")){
                login.setText(Constants.newUser.getEmail());
            }


            password = findViewById(R.id.edt_password);
            Button btnConnect = findViewById(R.id.btn_connect);
            errorView = findViewById(R.id.txt_v_error);//5215628882
            errorView.setVisibility(View.INVISIBLE);

            btnConnect.setOnClickListener(new ClickConnect());
            login.addTextChangedListener(new ChangeParams());
            password.addTextChangedListener(new ChangeParams());

            pwd_oublie = findViewById(R.id.txt_pwd_oublie);
            pwd_oublie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DiotaliLogin.this, ReinitialiserPwdActivity.class);
                    startActivity(intent);
                }
            });

            String urlEncours = sharedPreferences.getString("NAME","url");
            if (urlEncours.equals("url")){
                Constants.newURL("192.168.1.51");
                sharedPreferences.edit().putString("NAME", "192.168.1.51").commit();
            }
            else {
                Constants.newURL(urlEncours);
            }

            myDialogSign = new Dialog(this);
            id_logoImage = (ImageView)findViewById(R.id.img_contravention);
            id_logoImage.setOnClickListener(new DoubleTapListener(this));
        }

    }

    private class ClickConnect implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            errorView.setVisibility(View.INVISIBLE);
            String loginStr = login.getText().toString();
            String passwordStr = password.getText().toString();
            String regex = " ";
            String replacement = "";

            if (loginStr.isEmpty() || loginStr.replaceAll(regex, replacement).isEmpty()) {
                login.setError("Merci de saisir le login");
            } else if (passwordStr.isEmpty() || passwordStr.replaceAll(regex, replacement).isEmpty()) {
                password.setError("Merci de saisir le mot de passe");
            } else {

                ServicesTask task =  new ServicesTask(DiotaliLogin.this);
                LoginRequest params = new LoginRequest(loginStr,passwordStr);
                ServiceParams service = new ServiceParams();
                service.setMethodName(Constants.Methods.LOGIN);
                Log.d("login  params",params.toString());
                service.setMethodParams(params);
                task.execute(service);
            }

        }
    }

    public void sendTaskResponse(ServiceResult serviceResult) {

        try {
            Log.d("DIOTALI LOGIN", "receiving response");
            if (Constants.Methods.LOGIN.equals(serviceResult.getMethod())) {
                if (Constants.ResponseStatus.OK == serviceResult.getStatus()) {
                    User response = (User) serviceResult;

                    sharedPreferences = getSharedPreferences("MyConfigMarchand", MODE_PRIVATE);
                    sharedPreferences.edit().putString("TokenStorage", response.getToken()).commit();
                    sharedPreferences.edit().putString("RoleStorage", response.getRole()).commit();
                    sharedPreferences.edit().putString("FirstNameStorage", response.getFirstName()).commit();
                    sharedPreferences.edit().putString("LastNameStorage", response.getLastName()).commit();
                    sharedPreferences.edit().putString("AddressStorage", response.getAddress()).commit();
                    sharedPreferences.edit().putString("PhoneStorage", response.getPhone()).commit();
                    sharedPreferences.edit().putString("NinStorage", response.getNin()).commit();
                    sharedPreferences.edit().putString("EmailStorage", response.getEmail()).commit();

                    Intent intent = new Intent(this, TfeMenu.class);
                    Constants.newUser = response;
                    startActivity(intent);
                } else {
                    Log.d("LOGIN", "login error");
                    errorView.setText(serviceResult.getMessage());
                    errorView.setTextColor(Color.RED);
                    errorView.setVisibility(View.VISIBLE);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class ChangeParams implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(errorView.getVisibility() == View.VISIBLE){
                errorView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }


    @Override
    public void onDoubleClick(View v) {
        // Toast to show double click
        Log.v(DiotaliLogin.class.getName(), "onDoubleClick is click in ShowPopupSign" );
        ShowPopupSign(v);
    }

    // Modal Admin
    Dialog myDialogSign;
    public void ShowPopupSign(View v) {

        Button button_ok;
        Button button_close;
        SharedPreferences sharedPreferences = getSharedPreferences("MyConfigMarchand", MODE_PRIVATE);
        String sharedUrlEncours = sharedPreferences.getString("NAME","url");

        myDialogSign.setContentView(R.layout.modal_config);

        TextView urlEnCours = (TextView) myDialogSign.findViewById(R.id.url);
        urlEnCours.setText("URL EN COURS: " + sharedUrlEncours);


        new_url = (EditText) myDialogSign.findViewById(R.id.new_url);
        button_ok = (Button) myDialogSign.findViewById(R.id.button_ok);
        button_close = (Button) myDialogSign.findViewById(R.id.button_close);
        button_ok.setOnClickListener(onButtonClickListenerModale);
        button_close.setOnClickListener(onButtonClickListenerModale);
        myDialogSign.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogSign.show();

    }

    private View.OnClickListener onButtonClickListenerModale = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.button_ok:
                    Log.v(DiotaliLogin.class.getName(), "Modal in ShowPopupSign "+ new_url.getText().toString() );
                    if (new_url.getText().toString() == null || new_url.getText().toString().replaceAll(" ", "").isEmpty()) {
                        new_url.setError(getResources().getString(R.string.new_url));
                    }else{
                        myDialogSign.dismiss();
                        Constants.newURL(new_url.getText().toString());
                        sharedPreferences.edit().putString("NAME", new_url.getText().toString()).commit();
                        //ShowPopupAnswers(MainActivity.this);
                    }
                    break;

                case R.id.button_close:
                    myDialogSign.dismiss();
                    break;
            }
        }
    };

}
