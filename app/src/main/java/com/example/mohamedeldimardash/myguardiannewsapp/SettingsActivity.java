package com.example.mohamedeldimardash.myguardiannewsapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener, DatePickerDialog.OnDateSetListener {

        SharedPreferences preferences;
        Calendar calendar;
        private int currentYear;
        private int currentMonth;
        private int currentDayofMonth;
        private String today;

        @SuppressLint("SimpleDateFormat")
        final
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            Preference orderByPref = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderByPref);

            Preference fromDatePref = findPreference(getString(R.string.settings_from_date_key));

            // Get today's Date
            calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
            today = dateFormat.format(calendar.getTime());

            // Get Yesterday's date
            calendar.add(Calendar.DATE, -1);
            String yesterday = dateFormat.format ( calendar.getTime () );

            // Always set default date to Yesterday's date when app is launched
            if (preferences.getLong(getString(R.string.settings_from_date_key), 0) == 0) {
                fromDatePref.setSummary( yesterday );
            } else {
                long longPrefDate = preferences.getLong(
                        getString(R.string.settings_from_date_key), 0
                );

                Date dateObject = new Date(longPrefDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateObject);
                fromDatePref.setSummary(dateFormat.format(calendar.getTime()));
            }

            fromDatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                            String datePickedFormatted;

                            // Set picked date on calendar; if launching for first time then it's set to today's date
                            calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date datePicked = calendar.getTime();

                            datePickedFormatted = dateFormat.format(calendar.getTime());

                            if (datePickedFormatted.compareTo(today) < 0) {
                                Toast.makeText(getActivity(), getString(R.string.setting_older_date), Toast.LENGTH_LONG).show();
                            } else if (datePickedFormatted.compareTo(today) > 0) {
                                Toast.makeText(getActivity(), getString(R.string.setting_future_date), Toast.LENGTH_LONG).show();
                            }

                            preference.setSummary(datePickedFormatted);
                            preferences.edit().putLong(getString(R.string.settings_from_date_key), datePicked.getTime()).apply();

                        }
                    }, currentYear, currentMonth, currentDayofMonth);

                    datePickerDialog.show();
                    return true;
                }
            });
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            // Default method overriden from parent class
        }

    }
}


