package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.navigation.ui.AppBarConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sn.diotali.tfe_usager_dgid.services.ServiceParams;
import sn.diotali.tfe_usager_dgid.services.ServiceResult;
import sn.diotali.tfe_usager_dgid.services.ServicesTask;
import sn.diotali.tfe_usager_dgid.types.HistoriqueAchat;
import sn.diotali.tfe_usager_dgid.types.TransactionRequest;
import sn.diotali.tfe_usager_dgid.types.TransactionResponse;
import sn.diotali.tfe_usager_dgid.utils.Constants;
import sn.diotali.tfe_usager_dgid.utils.HistoriqueListAdapter;

public class TfeMenu extends DiotaliMain {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView tfe_menu_timbre, menu_autres;
    ImageView tfe_menu_quittance, menu_bar, menu_info;
    private Intent intent;
    List<HistoriqueAchat> listTimbre;
    HistoriqueAchat historiqueAchat;
    ListView listTimbreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfe_menu);

        getObjectById();

        listTimbreView = findViewById(R.id.list_timbre);
        listTimbreView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), InfoTimbreActivity.class);
                intent.putExtra("INFO_TIMBRE", listTimbre.get(i));
                startActivity(intent);
            }
        });

        ServicesTask service = new ServicesTask(TfeMenu.this);
        ServiceParams method = new ServiceParams();
        method.setMethodName(Constants.Methods.HISTORIQUE_ACHAT);
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setToken(Constants.newUser.getToken());
        Log.d(this.getClass().getName(),transactionRequest.toString());
        method.setMethodParams(transactionRequest);
        service.execute(method);

        menu_bar.setOnClickListener(onButtonClickListener);

        tfe_menu_timbre.setOnClickListener(onButtonClickListener);
        tfe_menu_quittance.setOnClickListener(onButtonClickListener);
        menu_autres.setOnClickListener(onButtonClickListener);
        menu_info.setOnClickListener(onButtonClickListener);
    }

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.tfe_menu_timbre:
                    intent = new Intent (getApplicationContext(), TfeTimbreActivity.class);
                    startActivity(intent);
                    break;

                case R.id.tfe_menu_quittance:
                    intent = new Intent (getApplicationContext(), TfeQuittanceActivity.class);
                    startActivity(intent);
                    break;

                case R.id.menu_autres:
                    intent = new Intent (getApplicationContext(), AutreMontantActivity.class);
                    startActivity(intent);
                    break;

                case R.id.menu_info:

                    break;
                case R.id.menu_bar:
                    intent = new Intent (getApplicationContext(), NavBarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DiotaliLogin.class);
        startActivity(intent);
        finish();
    }

    public void getObjectById(){
        tfe_menu_timbre = (ImageView)findViewById(R.id.tfe_menu_timbre);
        tfe_menu_quittance = (ImageView)findViewById(R.id.tfe_menu_quittance);
        menu_autres = findViewById(R.id.menu_autres);
        menu_bar = findViewById(R.id.menu_bar);
        menu_info = findViewById(R.id.menu_info);
    }

    @Override
    public void sendTaskResponse(ServiceResult serviceResult) {
        try {
            if(Constants.ResponseStatus.OK == serviceResult.getStatus()){
                TransactionResponse response = (TransactionResponse) serviceResult;

                Log.d(this.getClass().getName(), "sendTaskResponse success " +response.toString());
                listTimbre = response.getListUsers();
                listTimbreView.setAdapter(new HistoriqueListAdapter(this, listTimbre));
            } else {
                Log.d(this.getClass().getName(), "sendTaskResponse error " +serviceResult);


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
