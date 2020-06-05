package sn.diotali.tfe_usager_dgid.link;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import sn.diotali.tfe_usager_dgid.R;


/**
 * Created by lixc on 2017/7/6.
 */

public class Snackbar {

    private Snackbar() {

    }

    public static void showSnackBar(View view, String message) {
        final android.support.design.widget.Snackbar snackbar = android.support.design.widget.Snackbar.make(view, message, android.support.design.widget.Snackbar.LENGTH_LONG);
        snackbar.show();
        View snackbarView = snackbar.getView();
        TextView snackbarText = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        snackbarText.setTextColor(ContextCompat.getColor(snackbarView.getContext(), R.color.colorWhite));
        snackbar.setAction("Sure", v -> snackbar.dismiss());
    }
}
