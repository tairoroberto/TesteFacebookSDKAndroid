package br.com.tairoroberto.testefacebooksdkandroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

// para gerar a hash no terminla linux usar o comando
// keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
// e gerar uma hash da palavra "android"

public class MainActivity extends ActionBarActivity {
    private TextView txtUsuario;
    private Button btnLogout;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (TextView)findViewById(R.id.txtUsuario);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogout = (Button)findViewById(R.id.btnLogout);
    }

    public void loginFacebook(View view){
        Session.openActiveSession(this,true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                if(session.isOpened()){
                    Request.newMeRequest(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser graphUser, Response response) {
                            if (graphUser != null){
                                //Se o usuario está logado mostramos uma mensagem e escondemos o botão de login
                                txtUsuario.setText("Hello "+graphUser.getName());
                                btnLogin.setVisibility(View.GONE);
                                btnLogout.setVisibility(View.VISIBLE);

                            }
                        }
                    }).executeAsync();
                }
            }
        });
    }

    public void logoutFacebook(View view){
        if (Session.getActiveSession() != null){
            Session.getActiveSession().closeAndClearTokenInformation();
            Session.setActiveSession(null);

            //Se o usuario foi deslogado mostramos uma mensagem e escondemos o botão de logout
            txtUsuario.setText("Usuário Desconectado do facebook");
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this,requestCode,resultCode,data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
