package ro.pub.cs.systems.eim.practicaltest02;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String city;
    private ImageView pokeImage;
    
    private TextView abilities;
    
    private TextView powers;

    private Socket socket;

    public ClientThread(String address, int port, String city, ImageView pokeImage, TextView powers, TextView abilities) {
        this.address = address;
        this.port = port;
        this.city = city;
        this.pokeImage = pokeImage;
        this.powers = powers;
        this.abilities = abilities;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e("0", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("0", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            if (city == null) {
                String weatherInformation;
                while ((weatherInformation = bufferedReader.readLine()) != null) {
                    final String finalizedWeateherInformation = weatherInformation;
                    pokeImage.post(new Runnable() {
                        @Override
                        public void run() {
                            powers.setText(weatherInformation);

                        }
                    });
                }
            }


            printWriter.println(city);
            printWriter.flush();



            String weatherInformation;
            while ((weatherInformation = bufferedReader.readLine()) != null) {
                final String finalizedWeateherInformation = weatherInformation;
                pokeImage.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap mIcon11 = null;
//                        pokeImage.setText(finalizedWeateherInformation);
                        String[] res = finalizedWeateherInformation.split("[$]", 0);
                        abilities.setText(res[0]);
                        powers.setText(res[1]);
                        String url = res[2];
                        new ImageLoadTask(url, pokeImage).execute();

                    }
                });
            }
        } catch (IOException ioException) {
            Log.e("0", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("0", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
