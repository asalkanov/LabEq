package com.example.admin.labeq;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity implements Response.Listener<String> {

    final String TAG = this.getClass().getSimpleName();

    ListView lvPodaci;
    Button btnKlik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        // za ListView nam treba Adapter, a potom JSON za izgradnju objekta (array list) za Adapter
        // za Adapter, koristimo FunDapter
        // za pretvaranje Stringa u JSON object - koristimo KGJsonconverter (GSON - od Googlea)

        // moramo koristiti StringRequest jer library moze uzeti Samo string, ne i JSON

        String url = "http://cons.riteh.hr/labeq/podaci.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        lvPodaci = (ListView)findViewById(R.id.lvPodaci);
        lvPodaci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.tvID)).getText().toString();

                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    // ovdje sada dobivamo response, a on je u JSON arrayu
    // response moramo pretvoriti u Object prije nego ga posaljemo Adapteru.
    @Override
    public void onResponse(String response) {
        Log.d(TAG, response);
        // KGJsonconverter library - <Stanovi> je ime klase Stanovi.java sa svim podacima iz JSON-a
        // pretvaranje JSON text u JSON object -> listaPodaci = jsonObject
        ArrayList<Podaci> listaPodaci = new JsonConverter<Podaci>().toArrayList(response, Podaci.class);

        // sada JSON objecte mozemo poslati u Adapter (FunDapter - imamo podatke ali nam treba layout)
        // BindDictionary je dio FunDapter
        BindDictionary<Podaci> dictionary = new BindDictionary<>();

        // Binding, tj. povezivanje podataka - dobivanje ID-a iz tablice
        dictionary.addStringField(R.id.tvID, new StringExtractor<Podaci>() {
            @Override
            public String getStringValue(Podaci podaci, int position) {
                return podaci.id;
            }
        });

        // dobivanje Vrste iz tablice
        dictionary.addStringField(R.id.tvVrsta, new StringExtractor<Podaci>() {
            public String getStringValue(Podaci podaci, int position) {
                return podaci.vrsta;
            }
        });

        // dobivanje Opisa iz tablice
        dictionary.addStringField(R.id.tvOpis, new StringExtractor<Podaci>() {
            public String getStringValue(Podaci podaci, int position) {
                return podaci.opis;
            }
        });


        FunDapter<Podaci> adapter = new FunDapter<>(getApplicationContext(), listaPodaci, R.layout.podaci_layout, dictionary);

        lvPodaci.setAdapter(adapter);

    }
}

