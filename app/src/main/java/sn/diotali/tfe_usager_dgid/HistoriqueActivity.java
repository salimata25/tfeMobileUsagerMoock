package sn.diotali.tfe_usager_dgid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import sn.diotali.tfe_usager_dgid.services.ServiceParams;
import sn.diotali.tfe_usager_dgid.services.ServiceResult;
import sn.diotali.tfe_usager_dgid.services.ServicesTask;
import sn.diotali.tfe_usager_dgid.types.HistoriqueAchat;
import sn.diotali.tfe_usager_dgid.types.TransactionRequest;
import sn.diotali.tfe_usager_dgid.types.TransactionResponse;
import sn.diotali.tfe_usager_dgid.utils.Constants;
import sn.diotali.tfe_usager_dgid.utils.HistoriqueListAdapter;

public class HistoriqueActivity extends DiotaliMain implements View.OnClickListener{

    List<HistoriqueAchat> listTimbre;
    ListView listTimbreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        Log.d(this.getClass().getName(), "HistoriqueActivity ");

        findViewById(R.id.menu_bar).setOnClickListener(this);

        listTimbreView = findViewById(R.id.list_timbre);
        listTimbreView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), InfoTimbreActivity.class);
                intent.putExtra("INFO_TIMBRE", listTimbre.get(i));
                startActivity(intent);
            }
        });

        ServicesTask service = new ServicesTask(HistoriqueActivity.this);
        ServiceParams method = new ServiceParams();
        method.setMethodName(Constants.Methods.HISTORIQUE_ACHAT);
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setToken(Constants.newUser.getToken());
        Log.d(this.getClass().getName(),transactionRequest.toString());
        method.setMethodParams(transactionRequest);
        service.execute(method);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_bar:
                Intent intent = new Intent (getApplicationContext(), NavBarActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
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
