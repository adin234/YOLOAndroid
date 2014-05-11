package org.ydd.yolo.utiility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppPreferences {
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String get(String index) {
    	return _sharedPrefs.getString(index, "");
    }
    
    public String get(String index, String def) {
    	return _sharedPrefs.getString(index, def);
    }

    public void set(String index, String text) {
        _prefsEditor.putString(index, text);
        _prefsEditor.commit();
    }
}
