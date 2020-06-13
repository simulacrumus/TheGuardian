package com.example.theguardian;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Class that is called when the Android application is run on a phone. Gets the information from Bundle that is passed and creates activity with the fragment layout.
 * @author Emrah Kinay
 * @version 1.0
 */
public class TheGuardianEmpty extends AppCompatActivity {

    /**
     * Instantiates a new TheGuardianFragment object, and begins fragment transaction using the values in the passed Bundle object.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_fragment);

        //set toolbar
        Toolbar toolbar = findViewById(R.id.article_toolbar);
        Bundle bundle = getIntent().getExtras();
        toolbar.setTitle(bundle.getString(TheGuardianMain.ARTICLE_SECTION));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //back arrow button
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        //fragment
        TheGuardianFragment fragment = new TheGuardianFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.the_guardian_empty_fragment, fragment).commit();
    }
}
