package utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtils {

	private Context context;
	PackageManager manager;
	PackageInfo info;

	public PackageUtils(Context context) {
		this.context = context;
		init();
	}

	public void init() {
		manager = context.getPackageManager();
		try {
			info = manager.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getVersionCode() {
		return info.versionCode;
	}

	public String getVersionName() {
		return info.versionName;
	}

	public boolean isUpgradeVersion(int oldVersion, int newVersion) {

		return newVersion > oldVersion ? true : false;
	}
}
