package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Environment;

import model.InstallMessage;

public class HttpUtils {

	public HttpUtils() {
	}

	public static InstallMessage getInstallMessage(String path) {
		InstallMessage installMessage = new InstallMessage();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(path);
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				String jsonString = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(jsonString)
						.getJSONObject("message");
				installMessage.setVersionCode(jsonObject.getInt("VersionCode"));
				installMessage.setApkUrl(jsonObject.getString("apkUrl"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return installMessage;
	}

	public static String downLoadApk(String path) {

		byte[] data = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(path);
		File file = Environment.getExternalStorageDirectory();
		FileOutputStream outputStream = null;
		try {

			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				data = EntityUtils.toByteArray(response.getEntity());
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					outputStream = new FileOutputStream(new File(file,
							"apk_online_install.apk"));
					outputStream.write(data, 0, data.length);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file.getAbsolutePath() + File.separator
				+ "apk_online_install.apk";
	}
}
