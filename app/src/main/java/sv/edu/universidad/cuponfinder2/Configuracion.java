package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

public class Configuracion extends AppCompatActivity {
    Button btnRegresarConfig;
    Button btnCambiarLenguaje, btnAcercaDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        btnRegresarConfig= findViewById(R.id.btnRegresarConfig);
        btnAcercaDe= findViewById(R.id.btnAcercadeConfig);
        btnAcercaDe.setOnClickListener(v -> {
            Intent i =new Intent(getApplicationContext(), AcercaDe.class);
            startActivity(i);
        });
        btnRegresarConfig.setOnClickListener(v -> {
            Intent intent =new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        btnCambiarLenguaje = findViewById(R.id.btnCambiarLenguaje);
        btnCambiarLenguaje.setOnClickListener(v -> showChangeLangDialog());
    }
    public void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    public void showChangeLangDialog() {
        String[] listItems = {"English", "Español", "Português"};

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.choose_a_language);
        builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
            if (which == 0) {
                updateLocale("en");
                Toast.makeText(getApplicationContext(), "English Language", Toast.LENGTH_SHORT).show();
            } else if (which == 1) {
                updateLocale("es");
                Toast.makeText(getApplicationContext(), "Español establecido", Toast.LENGTH_SHORT).show();
            }else if (which == 2) {
                updateLocale("pt");
                Toast.makeText(getApplicationContext(), "Português estabelecido", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            recreate(); // Reinicia la actividad para aplicar los cambios de idioma
        });
        builder.show();
    }
}
