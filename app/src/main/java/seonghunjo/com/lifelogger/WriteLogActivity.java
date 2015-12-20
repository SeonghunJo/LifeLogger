package seonghunjo.com.lifelogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WriteLogActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editContent;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_log);

        editTitle = (EditText)findViewById(R.id.writeTitle);
        editContent = (EditText)findViewById(R.id.writeContent);


        saveButton = (Button)findViewById(R.id.writeSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", editTitle.getText().toString());
                resultIntent.putExtra("content", editContent.getText().toString());

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}

