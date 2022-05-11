package com.example.inotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ShowNoteActivity extends AppCompatActivity {

    TextView NotesDescription,NotesTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        NotesTitle = findViewById(R.id.textView_title);
        NotesDescription = findViewById(R.id.textView_description);
        NotesDescription.setMovementMethod(LinkMovementMethod.getInstance());

        Intent intent = getIntent();
        String Description,Title;
        Description = (String) intent.getSerializableExtra("notesDesc");
        Title = (String) intent.getSerializableExtra("notesTitle");

        NotesTitle.setText(Title);
        NotesDescription.setText(Description);
    }
}