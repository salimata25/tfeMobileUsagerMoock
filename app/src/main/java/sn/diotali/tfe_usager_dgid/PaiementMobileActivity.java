package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import sn.diotali.tfe_usager_dgid.services.ServiceParams;
import sn.diotali.tfe_usager_dgid.services.ServiceResult;
import sn.diotali.tfe_usager_dgid.services.ServicesTask;
import sn.diotali.tfe_usager_dgid.types.HistoriqueAchat;
import sn.diotali.tfe_usager_dgid.types.LoginRequest;
import sn.diotali.tfe_usager_dgid.types.TransactionResponse;
import sn.diotali.tfe_usager_dgid.types.User;
import sn.diotali.tfe_usager_dgid.utils.Constants;

public class PaiementMobileActivity  extends DiotaliMain implements View.OnClickListener {
    TextView txt_montant, txt_frais_supp, txt_montant_net, txt_error;
    EditText edt_tel, edt_code;
    HistoriqueAchat achat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement_mobile);
        Log.d(this.getClass().getName(),"DONNEES ACHAT: "+Constants.newTransaction.toString());
        findViewById(R.id.menu_bar).setOnClickListener(this);
        findViewById(R.id.btn_valider).setOnClickListener(this);

        txt_montant = findViewById(R.id.txt_montant_total);
        txt_frais_supp = findViewById(R.id.txt_frais_supp);
        txt_montant_net = findViewById(R.id.txt_prix_net);
        edt_tel = findViewById(R.id.txt_n_tel);
        edt_code = findViewById(R.id.txt_code_auto);
        txt_error = findViewById(R.id.txt_v_error);
        txt_error.setVisibility(View.INVISIBLE);

        txt_montant.setText(" "+Constants.newTransaction.getMontantTotal() + " FCFA");
        if(Constants.newTransaction.getTransactionType().equalsIgnoreCase(Constants.Menu.TIMBRE)){
            TextView txt_timbre = findViewById(R.id.txt_timbre);
            txt_timbre.setText("Achat de timbre fiscal");
            if (Constants.newTransaction.getAutreMontant() == "Droits d'enregistrements" || Constants.newTransaction.getAutreMontant() == "Mutation de véhicule") {
                txt_timbre.setText("Autres montants");
            }

            double frais = Constants.newTransaction.getMontantTotal() * 0.025;
            double net = Constants.newTransaction.getMontantTotal() + frais;

            txt_frais_supp.setText(" "+frais + " FCFA");
            txt_montant_net.setText(" "+net + " FCFA");
        }else {
            double frais = Constants.newTransaction.getMontantTotal() * 0.05;
            double net = Constants.newTransaction.getMontantTotal() + frais;

            txt_frais_supp.setText(" "+frais + " FCFA");
            txt_montant_net.setText(" "+net + " FCFA");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_bar:
                Intent intent = new Intent(getApplicationContext(), NavBarActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.btn_valider:

                String regex = " ";
                String replacement = "";

                if (edt_tel.getText().toString().isEmpty() || edt_tel.getText().toString().replaceAll(regex, replacement).isEmpty()) {
                    edt_tel.setError("Numéro de téléphone");
                } else if (edt_code.getText().toString().isEmpty() || edt_code.getText().toString().replaceAll(regex, replacement).isEmpty()) {
                    edt_code.setError("Code d'autorisation");
                } else {

                    Constants.newTransaction.setTelephonePaiement(edt_tel.getText().toString());
                    Constants.newTransaction.setCodeAutoristionPaiement(edt_code.getText().toString());
                    Log.d(this.getClass().getName(),"DONNEES ACHAT: "+Constants.newTransaction.toString());
                    ServicesTask service = new ServicesTask(PaiementMobileActivity.this);
                    ServiceParams method = new ServiceParams();
                    if(Constants.newTransaction.getTransactionType().equalsIgnoreCase(Constants.Menu.TIMBRE)){
                        method.setMethodName(Constants.Methods.ACHETER_TIMBRE);
                    }else {
                        method.setMethodName(Constants.Methods.ACHETER_QUITTANCE);
                    }
                    Log.d("transaction params ",Constants.newTransaction.toString());
                    method.setMethodParams(Constants.newTransaction);
                    service.execute(method);

                }



        }
    }


    @Override
    public void sendTaskResponse(ServiceResult serviceResult) {
        try {
            if(Constants.ResponseStatus.OK == serviceResult.getStatus()){
                TransactionResponse response = (TransactionResponse) serviceResult;
                Constants.transactionResponse = new TransactionResponse();
                Constants.transactionResponse.setData(response.getData());
                Log.d(this.getClass().getName(), "sendTaskResponse success " +response.toString());
                Log.d(this.getClass().getName(), "transactionResponse success " + Constants.transactionResponse.toString());
                Log.d(this.getClass().getName(), response.toString());
                Intent intent = new Intent(getApplicationContext(), FinishActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d(this.getClass().getName(), "sendTaskResponse error");
                txt_error.setText(serviceResult.getMessage());
                txt_error.setVisibility(View.VISIBLE);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



}
