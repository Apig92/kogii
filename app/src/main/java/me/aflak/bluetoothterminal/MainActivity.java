package me.aflak.bluetoothterminal;


import com.mongodb.DBCollection;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.mongodb.MongoClient;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.aflak.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity implements Bluetooth.CommunicationCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    // UI Widgets.
    //protected Button mStartUpdatesButton;
    //protected Button mStopUpdatesButton;
//    protected TextView mLastUpdateTimeTextView;
//    protected TextView mLatitudeTextView;
//    protected TextView mLongitudeTextView;
    //protected TextView mDistanceTextView;
    protected TextView mSpeedTextView;
    protected ImageView kogii;




    // Labels.
//    protected String mLatitudeLabel;
//    protected String mLongitudeLabel;
//    protected String mLastUpdateTimeLabel;
    //protected String currentDistance;
    protected String currentSpeed;


    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    /* My variables, for distance and lat and long */
//    private Double oldLat;
//    private Double oldLong;
//    private Double Distance=0.0;
//    private boolean starting=false;



    //original
    private String name;
    private Bluetooth b;
    private EditText message;
    private Button send;
    private TextView text;
    private TextView text2;

    private ScrollView scrollView;
    private boolean registered=false;
    private Button butten;
    private Float speed;




    //original

    private DatabaseHelper myDB;
    private String timestamp, lat, lon, lateraldistance;
    private Handler m_handler;
    private Runnable m_handlerTask ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //original

        super.onCreate(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        setContentView(R.layout.activity_main);

        //Testing





        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);




        text = (TextView)findViewById(R.id.text);
        text2 = (TextView)findViewById(R.id.textView);


        message = (EditText)findViewById(R.id.message);
        send = (Button)findViewById(R.id.send);
        butten= (Button)findViewById(R.id.button2);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        kogii = (ImageView) findViewById(R.id.imageView2);

        text.setMovementMethod(new ScrollingMovementMethod());
        send.setEnabled(false);

        b = new Bluetooth(this);
        b.enableBluetooth();

        b.setCommunicationCallback(this);

        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();

        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                message.setText("");
                b.send(msg);
                Display("You: "+msg);
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;

        //original

        // Locate the UI widgets.
        //mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        //mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
//        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
//        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
//        mLastUpdateTimeTextView = (TextView) findViewById(R.id.timeText);
        mSpeedTextView = (TextView) findViewById(R.id.speedText);
        //mDistanceTextView = (TextView) findViewById(R.id.distanceText);

        // mDistanceTextView = (TextView) findViewById(R.id.distance_text);

        // Set labels.
//        mLatitudeLabel = getResources().getString(R.string.latitude_label);
//        mLongitudeLabel = getResources().getString(R.string.longitude_label);
//        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);
        currentSpeed = getResources().getString(R.string.speedMessage);
        //currentDistance= getResources().getString(R.string.distanceMessage);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);



        //oldLat=mCurrentLocation.getLatitude();
        //oldLong=mCurrentLocation.getLongitude();

        //Create a SQLite database
        myDB = new DatabaseHelper(this);


        //Location updates

        mRequestingLocationUpdates = true;


        if(mCurrentLocation!=null)
            startLocationUpdates();


        kogii.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.kogii.bike"));
                startActivity(intent);
            }
        });



    }



    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                //setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }


            updateUI();
        }
    }



    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();


    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
//    public void startUpdatesButtonHandler(View view) {
//        if (!mRequestingLocationUpdates) {
//            mRequestingLocationUpdates = true;
//            setButtonsEnabledState();
//            startLocationUpdates();
//
//
//
//
//        }
//    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
//    public void stopUpdatesButtonHandler(View view) {
//        if (mRequestingLocationUpdates) {
//            mRequestingLocationUpdates = false;
//            setButtonsEnabledState();
//            stopLocationUpdates();
//            Distance=0.0;
//
//
//
//        }
//    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
//    private void setButtonsEnabledState() {
//        if (mRequestingLocationUpdates) {
//            mStartUpdatesButton.setEnabled(false);
//            mStopUpdatesButton.setEnabled(true);
//        } else {
//            mStartUpdatesButton.setEnabled(true);
//            mStopUpdatesButton.setEnabled(false);
//        }
//    }

    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        if (mCurrentLocation !=null) {
//            mLatitudeTextView.setText(String.format("%s %f", mLatitudeLabel,
//                    mCurrentLocation.getLatitude()));
//            mLongitudeTextView.setText(String.format("%s %f", mLongitudeLabel,
//                    mCurrentLocation.getLongitude()));
//            mLastUpdateTimeTextView.setText(String.format("%s %s", mLastUpdateTimeLabel,
//                    mLastUpdateTime));

            //mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel,
            //mLastUpdateTime));

//            if (oldLat == null && oldLong == null)
//                oldLat = mCurrentLocation.getLatitude();
//                oldLong = mCurrentLocation.getLongitude();


            Double currentLat = mCurrentLocation.getLatitude();
            Double currentLong = mCurrentLocation.getLongitude();
//            Distance += DistanceTravelled(oldLat, oldLong, currentLat, currentLong);
//            Distance = Double.parseDouble(new DecimalFormat("##.###").format(Distance));
//            oldLat = currentLat;
//            oldLong = currentLong;


            speed = mCurrentLocation.getSpeed();
            speed = speed * (float) 3.6;
            speed = (float)((int)( speed *100f ))/100f;

            mSpeedTextView.setText(String.format(currentSpeed + " " + speed + " km/h"));

           // mDistanceTextView.setText(String.format(currentDistance + " " + Distance + " km"));

        }


    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        mRequestingLocationUpdates = true;
        if(mCurrentLocation!=null)
            startLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.



        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
        //Parse all data from sqlite database to remote MongoDB
        parseAll();

        //Clears all data from android sqlite database
        myDB.deleteAll();

//        mRequestingLocationUpdates = false;
//        stopLocationUpdates();
//        Distance=0.0;
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        speed = mCurrentLocation.getSpeed();
        updateUI();
        //Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                //Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }




    //original
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.close:
                b.removeCommunicationCallback();
                b.disconnect();
                Intent intent = new Intent(this, Select.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Display(final String s){

        // ----------------------------------- Database --------------------------------------------

        //Create a SQLite database
        myDB = new DatabaseHelper(this);

//        for (int i = 0; i< 10; i++){
//            lat = i+"";
//            lon = i+"";
//            lateraldistance = i +"";
//            timestamp = i+"";
//            AddData();
//        }




        // -----------------------------------------------------------------------------------------
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Pattern p = Pattern.compile(".*?\\bLateral\\b.*?");
                Matcher m = p.matcher(s);
                if (m.find()) {
                    Pattern pattern = Pattern.compile("(\\d{3})");
                    Matcher matcher = pattern.matcher(s);
                    String val = "";
                    if (matcher.find()) {
                        val = matcher.group(1);
                        int result = Integer.parseInt(val);
                        result= result-100;


                        //Save the values to these variables, they are in String as we need to store them in arraylist
                        // later and it's less complex having it all as 1 type
                        //Data to parse into SQLite db
                        lat = Double.toString(mCurrentLocation.getLatitude());
                        lon = Double.toString(mCurrentLocation.getLongitude());

                        //Creates timestamp, make sure in the loop we refresh the timestamp
                        timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        timestamp = timestamp.toString();

                        lateraldistance = Double.toString(result);

                        //Add the data values above to sqlite db



                        if (result <= 150) {
                            text.append("\n"+result + " cm SIDE CAR SHIIIIIIT!\n");

                            Toast.makeText(MainActivity.this, "Distance "+lateraldistance+ ",lat "+lat+ " ,lon " + lon + " , time "+timestamp, Toast.LENGTH_LONG).show();

                            AddData();
                        }
                    }
                }else{
                    Pattern pattern = Pattern.compile("(\\d{3})");
                    Matcher matcher = pattern.matcher(s);
                    String val = "";
                    if (matcher.find()) {
                        val = matcher.group(1);
                        int result = Integer.parseInt(val);
                        result= result-100;
                        if (result <= 150) {
                            String danger = "Dangerously Close!";
                            text.append(result + " cm " + danger + "\n");
                            butten.setBackgroundColor(Color.RED);
                            butten.setText(danger);
                        }else if(result >= 150 && result < 300){
                            String detected = "Vehicle Detected!";
                            text.append(result + " cm - " + detected +  "\n");
                            butten.setBackgroundColor(Color.YELLOW);
                            butten.setText(detected);
                        }else if(result >= 300 && result < 400){
                            String nothing = "NO Vehicle Detected";
                            text.append(result + " cm - " + nothing + "\n");
                            butten.setBackgroundColor(Color.WHITE);
                            butten.setText(nothing);
                        }else{
                            text.append("No cars.\n");
                        }
                    }
                }
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        Display("Connected to "+device.getName()+" - "+device.getAddress());
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                send.setEnabled(true);
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Display("Disconnected!");
        Display("Connecting again...");
        b.connectToDevice(device);
    }

    @Override
    public void onMessage(String message) {
        Display(name+": "+message);
    }

    @Override
    public void onError(String message) {
        Display("Error: "+message);
    }

    @Override
    public void onConnectError(final BluetoothDevice device, String message) {
        Display("Error: "+message);
        Display("Trying again in 3 sec.");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b.connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }






    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(MainActivity.this, Select.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };

    //original
    //insert data to local sqlite android database
    public void AddData(){

        //Insert to android database
        boolean insertToDB = myDB.insertData(lat, lon, lateraldistance, timestamp);

        //Displays message to state if successful
//        if (insertToDB){
//            Toast.makeText(MainActivity.this, "Data stored to android database", Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(MainActivity.this, "Unsuccessful", Toast.LENGTH_LONG).show();
//        }
    }

    //Stores data to remote mongodb database
    public void parseAll(){
        ArrayList data = myDB.getAll();

        // Connect to db
        new dbConnect(getApplicationContext()).execute(data);
    }


    /* Method to calculate the distance between two coordinates*/

    public Double DistanceTravelled(Double lat1,Double long1,Double lat2,Double long2){
        /*Double lat1 = Double.parseDouble(latitude1);
        Double long1 = Double.parseDouble(longitude1);
        Double lat2 = Double.parseDouble(latitude2);
        Double long2 = Double.parseDouble(longitude2);*/

//        final double R = 6378137;
//        Double phi1=Math.toRadians(lat1);
//        Double phi2=Math.toRadians(lat2);
//        Double DeltaPhi=Math.toRadians(lat2-lat1);
//        Double DeltaLambda=Math.toRadians(long2-long1);
//        Double a= Math.sin(DeltaPhi/2)*Math.sin(DeltaPhi/2)+ Math.cos(phi1)*Math.cos(phi2)* Math.sin(DeltaLambda/2)*Math.sin(DeltaLambda/2);
//        Double c= 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
//        Double distance=R*c/1000;
//        return distance;
        double theta = long1 - long2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515*1.609344;
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}


class dbConnect extends AsyncTask<ArrayList<ArrayList<String>>, Void, ArrayList>  {

    private Context mContext;

    public dbConnect (Context context){
        this.mContext = context;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<ArrayList<String>>... passing) {

        //Create connection to MongoDb on AWS
        MongoCredential credential = MongoCredential.createCredential("Kogii", "admin", "Kogii".toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("54.190.29.49", 27017), Arrays.asList(credential));


//        MongoClient mongoClient = new MongoClient("54.190.29.49", 27017);
        MongoDatabase database = mongoClient.getDatabase("KogiiDB");

        MongoCollection<Document> collection = database.getCollection("LateralData");

        ArrayList<ArrayList<String>> data = passing[0];


        for(List<String> innerList : data) {
            String yes = innerList.get(0) + innerList.get(1) + innerList.get(2) + innerList.get(3);
//            Log.d("\nArrayFeed", yes+"\n\n\n")


//            //Creates mongodb document and stores the doc to a collection
//            Document document = new Document();
//            document.put("lat", "test");
//            document.put("lon", "test");
//            document.put("distance", "test");
//            document.put("timestamp", "test");


//            //Creates mongodb document and stores the doc to a collection
            Document document = new Document();
            document.put("lat", innerList.get(0));
            document.put("lon", innerList.get(1));
            document.put("distance", innerList.get(2));
            document.put("timestamp", innerList.get(3));

            collection.insertOne(document); //Insert the doc into mongodb
        }
        mongoClient.close();


        return null;
    }

    @Override
    protected void onPostExecute(ArrayList result) {

        Toast toast = Toast.makeText(mContext, "Successfully stored all data to mongoDB", Toast.LENGTH_SHORT);
        toast.show();
    }



}