package be.bxl.formation.demo_04_persistence_fichier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    boolean editMode = false;
    EditText etContent;
    Button btnAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContent = findViewById(R.id.et_main_content);
        btnAction = findViewById(R.id.btn_main_action);

        btnAction.setOnClickListener(v -> {
            if(editMode) {
                saveContent();
                switchToReadMode();
            }
            else {
                switchToEditMode();
            }
        });

        switchToReadMode();
        loadContent();
    }

    private void switchToReadMode() {
        closeKeyboard();
        changeEditMode(false);
        etContent.clearFocus();
        btnAction.setText(R.string.btn_edit);
    }

    private void switchToEditMode() {
        changeEditMode(true);
        btnAction.setText(R.string.btn_save);
    }

    private void changeEditMode(boolean editable) {
        editMode = editable;
        etContent.setFocusable(editable);
        etContent.setFocusableInTouchMode(editable) ;
        etContent.setClickable(editable);
        etContent.setLongClickable(editable);
        etContent.setCursorVisible(editable) ;
    }

    private void closeKeyboard() {
        View v = this.getCurrentFocus();
        if(v == null) {
            v = new View(this);
        }

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void saveContent() {
        String content = etContent.getText().toString();

        // Alternative avec un fichier externe a l'app  (Necessite une permission dans le manifest !!!)
        // File f1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "mon_fichier_ext.marie");

        try {
            FileOutputStream fos = openFileOutput("mon_fichier.txt", MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        }
        catch (IOException e) {
            Toast.makeText(this, "Error :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadContent() {
        try {
            // Flux de lecture
            FileInputStream fis = openFileInput("mon_fichier.txt");

            // Outil "reader" pour nous permettre de lire le contenu
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // Conteneur des données
            StringBuilder data = new StringBuilder();

            // Lecture du fichier (via le reader)
            String line ;
            while( (line = reader.readLine()) != null) {    // Résolution de l'affection en 1er dans la condition
                data.append(line);                          // suivis du test avec la valeur contenu dans "line"
                data.append("\n");
            }
            data.deleteCharAt(data.length() - 1);           // Supprimer le dernier saut de ligne

            // Fermeture du reader et du flux
            reader.close();
            fis.close();

            // Transformation des données sous forme de string
            String content = data.toString();

            // Affiche le contenu sur l'EditText
            etContent.setText(content);
        }
        catch (IOException e) {
            Toast.makeText(this, "Error :(", Toast.LENGTH_SHORT).show();
        }
    }
}