package com.esi.sig.tramoran;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.kml.KmlLayer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<LatLng> allStations = new ArrayList<>();
    private String[] allStationNames = new String[32];
    private TextView departure;
    private TextView destination;
    private Button ok;
    private TextView distance;
    private TextView time;
    private int selectedDeparture = 0;
    private int selectedDestination = 0;
    private LatLng departureStation;
    private LatLng destinationStation;
    private Dialog departureDialog;
    private Dialog destinationDialog;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        allStations.add(new LatLng(35.642448, -0.617186));
        allStations.add(new LatLng(35.64608, -0.621681));
        allStations.add(new LatLng(35.650023, -0.625216));
        allStations.add(new LatLng(35.65948, -0.628191));
        allStations.add(new LatLng(35.661255, -0.632013));
        allStations.add(new LatLng(35.665557, -0.634674));
        allStations.add(new LatLng(35.67129, -0.638179));
        allStations.add(new LatLng(35.676081, -0.641119));
        allStations.add(new LatLng(35.679829, -0.64342));
        allStations.add(new LatLng(35.683406, -0.64562));
        allStations.add(new LatLng(35.68851, -0.647666));
        allStations.add(new LatLng(35.689805, -0.648914));
        allStations.add(new LatLng(35.693643, -0.650386));
        allStations.add(new LatLng(35.697207, -0.651499));
        allStations.add(new LatLng(35.700565, -0.650335));
        allStations.add(new LatLng(35.7036, -0.649053));
        allStations.add(new LatLng(35.70035, -0.645274));
        allStations.add(new LatLng(35.699699, -0.63856));
        allStations.add(new LatLng(35.69857, -0.631919));
        allStations.add(new LatLng(35.699093, -0.625549));
        allStations.add(new LatLng(35.698084, -0.617953));
        allStations.add(new LatLng(35.697087, -0.613248));
        allStations.add(new LatLng(35.69691, -0.606122));
        allStations.add(new LatLng(35.699232, -0.602112));
        allStations.add(new LatLng(35.701842, -0.596278));
        allStations.add(new LatLng(35.702005, -0.589613));
        allStations.add(new LatLng(35.702214, -0.583653));
        allStations.add(new LatLng(35.70446, -0.576789));
        allStations.add(new LatLng(35.70593, -0.57367));
        allStations.add(new LatLng(35.703478, -0.570046));
        allStations.add(new LatLng(35.696601, -0.571867));
        allStations.add(new LatLng(35.69272849503011, -0.5711258287005827));

        allStationNames[0] = "Es Sénia terminus";
        allStationNames[1] = "Es Sénia Sud";
        allStationNames[2] = "Es Sénia Centre";
        allStationNames[3] = "Moulay Abdelkader";
        allStationNames[4] = "IGMO Université Docteur TALEB";
        allStationNames[5] = "Cité Volontaire ENSET";
        allStationNames[6] = "Lycée les Palmiers";
        allStationNames[7] = "Jardin Othmania";
        allStationNames[8] = "Cité Universitaire - Hai el Badr";
        allStationNames[9] = "Sureté de la Wilaya - BD ANP";
        allStationNames[10] = "Plais des sports";
        allStationNames[11] = "Ghaouti - Dar el Hayat";
        allStationNames[12] = "M'dine el Djadida";
        allStationNames[13] = "Houha Tlemcen";
        allStationNames[14] = "Place Mokrani";
        allStationNames[15] = "Place 1er novembre";
        allStationNames[16] = "Emir Abd el Kader";
        allStationNames[17] = "Gare SNTF";
        allStationNames[18] = "Bd Colonel A Benabderezzak";
        allStationNames[19] = "les Freres Moulay";
        allStationNames[20] = "Maalem Bentayeb";
        allStationNames[21] = "Les castors";
        allStationNames[22] = "Mosquée Ibn Badis";
        allStationNames[23] = "Plais de la Justice";
        allStationNames[24] = "Carrefours le 3 Cliniques";
        allStationNames[25] = "Cité USTO";
        allStationNames[26] = "Hôpital 1er novembre";
        allStationNames[27] = "Université USTO";
        allStationNames[28] = "USTO- Bifurcation - BD Pépinière";
        allStationNames[29] = "Cite El Yasmine";
        allStationNames[30] = "Hai Esabah";
        allStationNames[31] = "Gare Routière Sidi Maarouf - Terminus";

        departure = (TextView) findViewById(R.id.departure);
        departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departureDialog.show();
            }
        });

        destination = (TextView) findViewById(R.id.destination);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationDialog.show();
            }
        });
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double mdistance = 0.0;
                if (selectedDeparture < selectedDestination) {
                    try {
                        Log.i("ici c'est 19","<");
                        for (int i = selectedDeparture; i < selectedDestination; i++) {

                            mdistance = mdistance + SphericalUtil.computeDistanceBetween(allStations.get(i), allStations.get(i + 1));
                            mdistance = mdistance + 55;

                            Log.i("info123", i + "sss" + mdistance);
                        }
                        String mDistnace = mdistance / 1000 + "";
                        String mTime = ((mdistance / 1000) * 60) / 18 + "";
                        distance.setText("La distance est : "+mDistnace.substring(0,5)+ " KM");
                        time.setText("Le temps estimé : "+mTime.substring(0,5)+" Minutes");
                    } catch (Exception e) {
                        Log.i("hereeee", e.toString());
                    }
                }
                else if (selectedDeparture > selectedDestination){
                    try {
                        Log.i("ici",">");
                        for (int i = selectedDeparture; i > selectedDestination; i--) {

                            mdistance = mdistance + SphericalUtil.computeDistanceBetween(allStations.get(i), allStations.get(i -1));
                            mdistance = mdistance + 55;

                            Log.i("info123", i + "sss" + mdistance);
                        }
                        String mDistnace = mdistance / 1000 + "";
                        String mTime = ((mdistance / 1000) * 60) / 18 + "";
                        distance.setText("La distance est : "+mDistnace.substring(0,5)+ " KM");
                        time.setText("Le temps estimé : "+mTime.substring(0,5)+" Minutes");
                    } catch (Exception e) {
                        Log.i("hereeee", e.toString());
                    }
                }


            }
        });
        distance = (TextView) findViewById(R.id.distance);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selectionner la station de depart");
        builder.setMultiChoiceItems(allStationNames, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedDeparture = which;
                    Log.i("aaa",selectedDeparture+"");
                }
            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        departure.setText(allStationNames[selectedDeparture]);
                        Log.i("aaa",allStationNames[selectedDeparture]);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        departureDialog = builder.create();

        final String[] aa1 = allStationNames;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Selectionner la station destination");
        builder1.setMultiChoiceItems(aa1, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedDestination = which;
                }
            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        destination.setText(aa1[selectedDestination]);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        destinationDialog = builder1.create();
        time = (TextView) findViewById(R.id.time);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng oran = new LatLng(35.6878582, -0.6104216);

        try {
            KmlLayer layer = new KmlLayer(mMap, R.raw.ligne_oran_new7, getApplicationContext());
            layer.addLayerToMap();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(oran));

            for (int i =0 ; i<allStations.size();i++){
                mMap.addMarker(new MarkerOptions().position(allStations.get(i)).title(allStationNames[i]).snippet("latitude = "+allStations.get(i).latitude+" "+"longitude: "+allStations.get(i).longitude));
            }

            mMap.setOnMarkerClickListener(this);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getDistanceOnRoad(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        String result_in_kms = "";

        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            Log.i("info", url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = (HttpResponse) httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add("0.0");
                    }
                }
                result_in_kms = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("error", e.toString());

        }
        return result_in_kms;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }
}
