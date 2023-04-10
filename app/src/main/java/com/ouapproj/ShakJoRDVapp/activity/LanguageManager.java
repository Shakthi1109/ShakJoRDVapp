package com.ouapproj.ShakJoRDVapp.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {private Context ct;

    public LanguageManager(Context ctx){
        ct=ctx;
    }

    // This method updates the application configuration to use the language specified by the provided code.
    public void updateResource(String code){
        Locale locale = new Locale(code); // Sets the default language for the application.
        Locale.setDefault(locale); // Gets the resources for the application, such as strings, colors, etc.
        Resources resources = ct.getResources(); // Current configuration for the application.
        Configuration configuration = resources.getConfiguration(); // Updating language in the configuration.
        configuration.locale = locale; // Updating the application's configuration with the new language.
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}
