package com.example.pokedroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.mbms.StreamingServiceInfo;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //Pux ando arquivos do XML para controle de tela

    ProgressDialog progressDialog; // componente pra mostrar o carregamento dos dados
    TextView textocidade;
    Button botaoBuscar;
    TextView textJson;
    TextView texthoje;
    TextView textdata;
    TextView textcelsius;
    TextView texttemp;
    TextView textcondicao;
    TextView textdesc;
    TextView textnascer;
    TextView textnascersol;
    TextView textpor;
    TextView textporsol;
    TextView textamanha2;
    TextView textdata2;
    TextView texttemp2;
    TextView textdesc2;
    TextView textmin;
    TextView textmax;
    TextView textamanha3;
    TextView textdata3;
    TextView texttemp3;
    TextView textdesc3;
    TextView textmin2;
    TextView textmax2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textocidade = findViewById(R.id.textocidade);
        botaoBuscar = findViewById(R.id.botaoBuscar);
        textJson = findViewById(R.id.textJson);
        texthoje = findViewById(R.id.textdata);
        textdata = findViewById(R.id.textdata);
        textcelsius = findViewById(R.id.textcelsius);
        texttemp = findViewById(R.id.texttemp);
        textcondicao = findViewById(R.id.textcondicao);
        textdesc = findViewById(R.id.textdesc);
        textnascer = findViewById(R.id.textnascer);
        textnascersol = findViewById(R.id.textnascersol);
        textpor = findViewById(R.id.textpor);
        textporsol = findViewById(R.id.textporsol);
        textamanha2 = findViewById(R.id.textamanha2);
        textdata2 = findViewById(R.id.textdata2);
        textdesc2 = findViewById(R.id.textdesc2);
        textmin = findViewById(R.id.textmin);
        textmax = findViewById(R.id.textmax);
        textamanha3 = findViewById(R.id.textamanha3);
        textdata3 = findViewById(R.id.textdata3);
        textdesc3 = findViewById(R.id.textdesc3);
        textmin2 = findViewById(R.id.textmin2);
        textmax2 = findViewById(R.id.textmax2);


        botaoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cidade = textocidade.getText().toString();
                String hgapi = "https://api.hgbrasil.com/weather?key=b39e16a9&city_name=" + cidade;
                HGAPI consulta = new HGAPI();
                consulta.execute(hgapi);
            }
        });

    }//onCreate

    protected class HGAPI extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET"); // parametro GET para enviar url
                InputStream is = con.getInputStream(); // pega o response
                BufferedReader br = new BufferedReader(new InputStreamReader(is)); // pega os dados pra passar pra string
                String line;
                StringBuffer out = new StringBuffer();
                while ((line = br.readLine()) != null) { // vai lendo os dados linha por linha
                    out.append(line + "\n");
                }
                is.close();
                return
                        out.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }// DoInBackgroud


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Abrindo mapa mundi, procurando cidade...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }// classe pra mostrar texto de carregamento

        @Override
        protected void onPostExecute(String dados) {
            // metodo para baixar os dados da API
            try {
                textJson.setText(dados);
                parseJSON(dados);
                progressDialog.hide();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Cidade não encontrada", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }//lendo o JSON recebido do GET na API

        private void parseJSON(String data) throws MalformedURLException {

            try {
                if (data.contains("erro")) {
                    Toast.makeText(MainActivity.this, "Cidade não encontrada", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject(data); // recebe o JSON da consulta

                    // preenche os textviews principais
                    textdata.setText(jsonObject.getJSONObject("results").getString("date"));
                    texttemp.setText(jsonObject.getJSONObject("results").getString("temp"));
                    textdesc.setText(jsonObject.getJSONObject("results").getString("description"));
                    textnascersol.setText(jsonObject.getJSONObject("results").getString("sunrise"));
                    textporsol.setText(jsonObject.getJSONObject("results").getString("sunset"));

                    //textview secundario
                    textdata2.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(0).getString("date"));
                    textdesc2.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(0).getString("description"));
                    textmax.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(0).getString("max") + "Cº");
                    textmin.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(0).getString("min") + "Cº");

                    //textview grupo3

                    textdata3.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(1).getString("date"));
                    textdesc3.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(1).getString("description"));
                    textmax2.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(1).getString("max") + "Cº");
                    textmin2.setText(jsonObject.getJSONObject("results").getJSONArray("forecast").getJSONObject(1).getString("min") + "Cº");


                }


            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }


        }
    }// Método HGAPI
}//MainActivity
