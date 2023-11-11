package sv.edu.universidad.cuponfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Configuracion extends AppCompatActivity {
    Button btnRegresarConfig;
    Button btnCambiarLenguaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        btnRegresarConfig=(Button) findViewById(R.id.btnRegresarConfig);
        btnRegresarConfig.setOnClickListener(v -> onBackPressed());

        btnCambiarLenguaje = findViewById(R.id.btnCambiarLenguaje);
        btnCambiarLenguaje.setOnClickListener(v -> showChangeLangDialog());
    }
    public void showChangeLangDialog() {
        String[] listItems = {"English", "Español(Aun no disponible)"};

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Choose a language");
        builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
            if (which == 0) {
                ((MyApplication) getApplication()).updateLocale("en");
            } else if (which == 1) {
                ((MyApplication) getApplication()).updateLocale("en");
            }
            dialog.dismiss();
            recreate(); // Reinicia la actividad para aplicar los cambios de idioma
        });
        builder.show();
    }
}