package com.jonmid.tallerasynctask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jonmid.tallerasynctask.Adapters.PostAdapter;
import com.jonmid.tallerasynctask.Adapters.UserAdapter;
import com.jonmid.tallerasynctask.Models.Post;
import com.jonmid.tallerasynctask.Models.User;
import com.jonmid.tallerasynctask.Parser.Json;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressBar cargador;
    Button boton;
    //List<Post> myPost;
    List<User> myPost;
    RecyclerView recyclerView;
    PostAdapter adapter;
    UserAdapter adapterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargador = (ProgressBar) findViewById(R.id.cargador);
        boton = (Button) findViewById(R.id.boton);
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);

        // Permite manegar los componentes en un RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // Establecer la orientacion en vertical
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Asignar a orientacion a mi RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public Boolean isOnLine(){
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connec.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }

    public void onClickButton(View view){
        if (isOnLine()){
            MyTask task = new MyTask();
            task.execute("https://jsonplaceholder.typicode.com/users");
        }else {
            Toast.makeText(this, "Sin conexión", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarDatos(){
        // Crear un objeto de tipo "PostAdapter" y retorna el item de mi layout (item.xml)
        //adapter = new PostAdapter(getApplicationContext(), myPost);
        adapterUser = new UserAdapter(getApplicationContext(), myPost);

        // inyectar el item en mi RecyclerView
        recyclerView.setAdapter(adapterUser);

    }

    private class MyTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cargador.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String content = null;
            try {
                content = HttpManager.getData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                myPost = Json.getData(s);
            } catch (Exception e) {
                e.printStackTrace();
            }

            cargarDatos();
            cargador.setVisibility(View.GONE);
        }
    }
}
