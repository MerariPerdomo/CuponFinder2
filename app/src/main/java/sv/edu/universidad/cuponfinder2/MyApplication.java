package sv.edu.universidad.cuponfinder2;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        updateLocale("en"); // Establece el idioma predeterminado aquí
    }

    public void updateLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
