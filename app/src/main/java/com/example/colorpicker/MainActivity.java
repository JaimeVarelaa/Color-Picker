package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView labelRGB;
    TextView labelAlpha;
    TextView labelHex;
    String oldColor="#00000000";
    View colorActual, colorAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        labelRGB = findViewById(R.id.TextRGB);
        labelAlpha = findViewById(R.id.TextAlpha);
        labelHex = findViewById(R.id.TextHex);
        colorActual = findViewById(R.id.ColorActual);
        colorAnterior = findViewById(R.id.ColorAnterior);
        selectColor rgb = findViewById(R.id.rueda);
        SelectColorVar deg = findViewById(R.id.barra);
        SelectAlpha alpha = findViewById(R.id.alpha);
        //rgb.setrgb(0,0,255);

        rgb.setOnSelectedColorListener(new OnSelectedColorListener() {
            @Override
            public void onSelectedColor(int red, int green, int blue) {
                //mensaje.setText(red+" "+green+" "+blue);
                deg.getrgb(red,green,blue);


                String alphaText = labelAlpha.getText().toString();
                String rgbText = labelRGB.getText().toString();
                int alpha = Integer.parseInt(alphaText);
                red = Integer.parseInt(rgbText.split(",")[1].trim());
                green = Integer.parseInt(rgbText.split(",")[2].trim());
                blue = Integer.parseInt(rgbText.split(",")[3].trim());
                colorActual.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }
        });

        deg.setOnColorUpdateListener(new OnColorUpdateListener() {
            @Override
            public void onColorUpdated(int red, int green, int blue) {
                labelRGB.setText(","+red+","+green+","+blue);
                alpha.getrgb(red,green,blue);
                String alphaText = labelAlpha.getText().toString();
                int alpha = Integer.parseInt(alphaText);
                int argbValue = ColorConverter.rgbToArgb(alpha, red, green, blue);
                String hexValue = ColorConverter.argbToHex(argbValue);
                labelHex.setText(hexValue);


                alphaText = labelAlpha.getText().toString();
                String rgbText = labelRGB.getText().toString();
                alpha = Integer.parseInt(alphaText);
                red = Integer.parseInt(rgbText.split(",")[1].trim());
                green = Integer.parseInt(rgbText.split(",")[2].trim());
                blue = Integer.parseInt(rgbText.split(",")[3].trim());
                colorActual.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }
        });

        alpha.setOnSelectedAlphaListener(new OnSelectedAlphaListener() {
            @Override
            public void onselectedAlpha(int alpha) {
                labelAlpha.setText(String.valueOf(alpha));

                String rgbText = labelRGB.getText().toString();
                int red = Integer.parseInt(rgbText.split(",")[1].trim());
                int green = Integer.parseInt(rgbText.split(",")[2].trim());
                int blue = Integer.parseInt(rgbText.split(",")[3].trim());

                int argbValue = ColorConverter.rgbToArgb(alpha, red, green, blue);
                String hexValue = ColorConverter.argbToHex(argbValue);
                labelHex.setText(hexValue);

                colorActual.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }
        });

        ImageButton btnCopyRGB = findViewById(R.id.BtnCopyRGB);
        btnCopyRGB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alphaText = labelAlpha.getText().toString();
                String rgbText = labelRGB.getText().toString();
                String combinedText = alphaText + rgbText;

                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("TextoCopiado", combinedText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, "Color ARGB copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnCopyHex = findViewById(R.id.BtnCopyHex);
        btnCopyHex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alphaText = labelAlpha.getText().toString();
                String rgbText = labelRGB.getText().toString();

                int alpha = Integer.parseInt(alphaText);
                int red = Integer.parseInt(rgbText.split(",")[1].trim());
                int green = Integer.parseInt(rgbText.split(",")[2].trim());
                int blue = Integer.parseInt(rgbText.split(",")[3].trim());

                int argbValue = ColorConverter.rgbToArgb(alpha, red, green, blue);
                String hexValue = ColorConverter.argbToHex(argbValue);

                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("TextoCopiado", hexValue);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(MainActivity.this, "Color hexadecimal copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSelectColor = findViewById(R.id.BtnSeleccionar);
        btnSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alphaText = labelAlpha.getText().toString();
                String rgbText = labelRGB.getText().toString();

                int alpha = Integer.parseInt(alphaText);
                int red = Integer.parseInt(rgbText.split(",")[1].trim());
                int green = Integer.parseInt(rgbText.split(",")[2].trim());
                int blue = Integer.parseInt(rgbText.split(",")[3].trim());

                colorAnterior.setBackgroundColor(Color.argb(alpha,red,green,blue));
            }
        });
    }
}