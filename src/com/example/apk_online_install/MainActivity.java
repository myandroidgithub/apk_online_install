package com.example.apk_online_install;

import java.io.File;

import model.InstallMessage;
import utils.HttpUtils;
import utils.PackageUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String path = "http://192.168.1.101:8080/apkOnlineInstall";
	
	private PackageUtils packageUtils;
	private ProgressDialog progressDialog;
	
	private AlertDialog.Builder builder;
	
	private int oldVersion ,newVersion;
	private InstallMessage message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		packageUtils = new PackageUtils(this);
		progressDialog = new ProgressDialog(this);
		builder = new AlertDialog.Builder(this);
		builder.setTitle("��ʾ");
		builder.setMessage("���µİ汾���Ƿ�Ҫ���أ�");
		builder.setCancelable(false);
		builder.setPositiveButton("ȷ��", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String apkUrl = message.getApkUrl();
				new DownloadApk().execute(apkUrl);
			}
		}).setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressDialog.dismiss();
			}
		});
		oldVersion = packageUtils.getVersionCode();
		try {
			message = new MyTask().execute(path).get();
			newVersion = message.getVersionCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * �������˰汾�Ŵ��ڱ��ذ汾�ŲŻᵯ�����ضԻ���
		 */
		if (packageUtils.isUpgradeVersion(oldVersion, newVersion)) {
			builder.create().show();
		}
	}

	/**
	 * ����߳���ʾ�Ƿ���Ҫ����
	 * @author acer
	 *
	 */
	public class MyTask extends AsyncTask<String, Void, InstallMessage>{

//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			progressDialog.show();
//		}
		
		@Override
		protected InstallMessage doInBackground(String... params) {
			return HttpUtils.getInstallMessage(params[0]);
		}
	}

	/*
	 * ����̸߳���ȥ����������apk�ļ�
	 */
	public class DownloadApk extends AsyncTask<String, Void, Void>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			String apk_url = HttpUtils.downLoadApk(params[0]);
			if (apk_url!=null) {
				Uri uri = Uri.fromFile(new File(apk_url));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri, "application/vnd.android.package-archive");
				startActivity(intent);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}
	}
}
