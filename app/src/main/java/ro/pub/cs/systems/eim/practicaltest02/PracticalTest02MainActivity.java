package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;
    private Button getList = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText cityEditText = null;

    private TextView powers = null;
    private TextView list = null;

    private TextView abilities = null;
    private Spinner informationTypeSpinner = null;
    private Button getWeatherForecastButton = null;
    private ImageView pokeImage = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;




    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e("0", "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
    private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String city = cityEditText.getText().toString();
            if (city == null || city.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            pokeImage.setImageURI(Uri.parse(""));

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), city, pokeImage, list, abilities
            );
            clientThread.start();
        }

    }

    private GetListListener getListListener = new GetListListener();
    private class GetListListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }


            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), null, pokeImage, powers, abilities
            );
            clientThread.start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);


        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        getList = (Button)findViewById(R.id.get_list);
        getList.setOnClickListener(getListListener);
        connectButton.setOnClickListener(connectButtonClickListener);


        clientAddressEditText = (EditText)findViewById(R.id.address_edit_text);
        list = (TextView) findViewById(R.id.list);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        powers = (TextView) findViewById(R.id.powers);
        abilities = (TextView) findViewById(R.id.abilities);
        cityEditText = (EditText)findViewById(R.id.city_edit_text);
        getWeatherForecastButton = (Button)findViewById(R.id.get_weather_button);
        getWeatherForecastButton.setOnClickListener(getWeatherForecastButtonClickListener);
        pokeImage = (ImageView) findViewById(R.id.text_weather_field);


    }


    @Override
    protected void onDestroy() {
        Log.i("0", "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}