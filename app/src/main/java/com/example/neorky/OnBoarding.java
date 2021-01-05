package com.example.neorky;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class OnBoarding extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new Step.Builder().setTitle("Give or get Job")
                .setContent("Get services according to your needs around you at your convince")
                .setBackgroundColor(R.color.colorAccent) // int background color
                .setDrawable(R.drawable.find_jobs) // int top drawable
                .setSummary("#VocalForLocal")
                .build());
        addFragment(new Step.Builder().setTitle("Give work")
                .setContent("Just Post service that you need and let people related to that services reach you")
                .setBackgroundColor(R.color.colorAccent) // int background color
                .setDrawable(R.drawable.give_job) // int top drawable
                .setSummary("#Employment")
                .build());
        addFragment(new Step.Builder().setTitle("Get Work")
                .setContent("Neorky gives everyone an opportunity of getting started with a new work or leveling up their business")
                .setBackgroundColor(R.color.colorAccent) // int background color
                .setDrawable(R.drawable.get_job) // int top drawable
                .setSummary("#CustomerFirst")
                .build());
        addFragment(new Step.Builder().setTitle("Get Started")
                .setContent("Get started by placing requests for your services you need")
                .setBackgroundColor(R.color.colorAccent) // int background color
                .setDrawable(R.drawable.get_started) // int top drawable
                .setSummary("#LetsStart")
                .build());


    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}
