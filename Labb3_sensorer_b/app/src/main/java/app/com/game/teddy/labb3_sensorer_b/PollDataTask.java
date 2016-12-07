package app.com.game.teddy.labb3_sensorer_b;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

class PollDataTask extends AsyncTask<Void, Void, String> {

	private boolean running;


	protected PollDataTask(MainActivity activity, BluetoothDevice noninDevice) {
		this.activity = activity;
		this.noninDevice = noninDevice;
		this.adapter = BluetoothAdapter.getDefaultAdapter();
		running = true;
	}

	/**
	 * A simple example: poll one frame of data from the Nonin sensor
	 */
	@Override
	protected String doInBackground(Void... v) {
		String output = "";

		// an ongoing discovery will slow down the connection
		adapter.cancelDiscovery();

		BluetoothSocket socket = null;
		FileOutputStream fos = null;
		PrintWriter pr = null;
		try {
			socket = noninDevice
					.createRfcommSocketToServiceRecord(STANDARD_SPP_UUID);
			socket.connect();

			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			os.write(FORMAT);
			os.flush();
			byte[] reply = new byte[1];
			is.read(reply);

			OutputStreamWriter osw = null;
			if (reply[0] == ACK) {
				fos = null;
				pr = null;
				try {
					File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "data.txt");
					if(!path.exists()) {
						Log.v("Did not" , "exist");
						path.mkdirs();
					}
					fos = new FileOutputStream(path);
					//pr = new PrintWriter(fos);
					osw = new OutputStreamWriter(fos);
					String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
					osw.write(timestamp);
				}catch(Exception e){e.printStackTrace();}
				Log.v("Before ", " while loop");
				while(running) {
					byte[] frame = new byte[4]; // this -obsolete- format specifies
					// 4 bytes per frame

					is.read(frame);

					int value1 = unsignedByteToInt(frame[1]);
					int value2 = unsignedByteToInt(frame[2]);
					output = value1 + "; " + value2 + "\r\n";
					Log.v("In ", "while loop" + " " +value1 + " " +value2);
					//pr.write(value1 +" " +value2);
				}
			}
		} catch (Exception e) {
			output = e.getMessage();
			e.printStackTrace();
		} finally {
			try {if (socket != null)socket.close();} catch (Exception e) {}
			try {if (pr != null)pr.close();} catch (Exception e) {}
			try {if (fos != null)fos.close();} catch (Exception e) {}
		}
		Log.v("After while", " loop");
		return output;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		running = false;
	}

	/**
	 * update the UI (executed on the main thread)
	 */
	@Override
	protected void onPostExecute(String output) {
		activity.displayData(output);
	}

	// The byte sequence to set sensor to a basic, and obsolete, format
	private static final byte[] FORMAT = { 0x44, 0x31 };
	private static final byte ACK = 0x06; // ACK from Nonin sensor

	private static final UUID STANDARD_SPP_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private MainActivity activity;
	private BluetoothDevice noninDevice;
	private BluetoothAdapter adapter;

	// NB! Java does not support unsigned types
	private int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}
}
