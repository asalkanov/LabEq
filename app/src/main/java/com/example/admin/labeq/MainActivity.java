package com.example.admin.labeq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String TAG = this.getClass().getSimpleName();

    EditText etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://cons.riteh.hr/labeq/index.php";
                //String url = "http://139.59.142.254/mobilne/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);
                                if (response.contains("uspjesan_login")) {
                                    Intent i = new Intent(MainActivity.this, SubActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Netocna lozinka!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof com.android.volley.TimeoutError) {
                             Toast.makeText(getApplicationContext(), "Timeout Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof com.android.volley.NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "Provjerite internet vezu!", Toast.LENGTH_LONG).show();
                        } else if (error instanceof com.android.volley.AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Pogreska autentikacije.", Toast.LENGTH_LONG).show();
                        } else if (error instanceof com.android.volley.NetworkError) {
                            Toast.makeText(getApplicationContext(), "Pogreska mreze.", Toast.LENGTH_LONG).show();
                        } else if (error instanceof com.android.volley.ServerError) {
                            Toast.makeText(getApplicationContext(), "Pogreska posluzitelja.", Toast.LENGTH_LONG).show();
                        } else if (error instanceof com.android.volley.ParseError) {
                            Toast.makeText(getApplicationContext(), "JSON Parse Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }){
                    // ovdje mozemo slati podatke (username i password)
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        // kljuc i value ISTI kao i na PHP stranici $_POST
                        params.put("txtPassword", etPassword.getText().toString());
                        return params;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

    }

}


