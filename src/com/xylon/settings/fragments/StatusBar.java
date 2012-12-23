
package com.xylon.settings.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import com.xylon.settings.R;
import com.xylon.settings.SettingsPreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class StatusBar extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String PREF_BATT_ICON = "battery_icon_list";
    private static final String PREF_BATT_BAR = "battery_bar_list";
    private static final String PREF_BATT_BAR_STYLE = "battery_bar_style";
    private static final String PREF_BATT_BAR_COLOR = "battery_bar_color";
    private static final String PREF_BATT_BAR_WIDTH = "battery_bar_thickness";
    private static final String PREF_BATT_ANIMATE = "battery_bar_animate";
    private static final String PREF_STATUSBAR_BACKGROUND_STYLE = "statusbar_background_style";
    private static final String PREF_STATUSBAR_BACKGROUND_COLOR = "statusbar_background_color";

    ColorPickerPreference mStatusbarBgColor;
    ColorPickerPreference mBatteryBarColor;
    ListPreference mStatusbarBgStyle;
    ListPreference mBatteryIcon;
    ListPreference mBatteryBar;
    ListPreference mBatteryBarStyle;
    ListPreference mBatteryBarThickness;
    CheckBoxPreference mBatteryBarChargingAnimation;

    private int seekbarProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_statusbar);
        addPreferencesFromResource(R.xml.status_bar_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mBatteryIcon = (ListPreference) prefSet.findPreference(PREF_BATT_ICON);
        mBatteryIcon.setOnPreferenceChangeListener(this);
        mBatteryIcon.setValue((Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.STATUSBAR_BATTERY_ICON,
                0))
                + "");

        mBatteryBar = (ListPreference) prefSet.findPreference(PREF_BATT_BAR);
        mBatteryBar.setOnPreferenceChangeListener(this);
        mBatteryBar.setValue((Settings.System
                .getInt(getActivity().getContentResolver(),
                        Settings.System.STATUSBAR_BATTERY_BAR, 0))
                + "");

        mBatteryBarStyle = (ListPreference) prefSet.findPreference(PREF_BATT_BAR_STYLE);
        mBatteryBarStyle.setOnPreferenceChangeListener(this);
        mBatteryBarStyle.setValue((Settings.System.getInt(getActivity()
                .getContentResolver(),
                Settings.System.STATUSBAR_BATTERY_BAR_STYLE, 0))
                + "");

        mBatteryBarColor = (ColorPickerPreference) prefSet.findPreference(PREF_BATT_BAR_COLOR);
        mBatteryBarColor.setOnPreferenceChangeListener(this);

        mBatteryBarChargingAnimation = (CheckBoxPreference) prefSet.findPreference(PREF_BATT_ANIMATE);
        mBatteryBarChargingAnimation.setChecked(Settings.System.getInt(
                getActivity().getContentResolver(),
                Settings.System.STATUSBAR_BATTERY_BAR_ANIMATE, 0) == 1);

        mBatteryBarThickness = (ListPreference) prefSet.findPreference(PREF_BATT_BAR_WIDTH);
        mBatteryBarThickness.setOnPreferenceChangeListener(this);
        mBatteryBarThickness.setValue((Settings.System.getInt(getActivity()
                .getContentResolver(),
                Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, 1))
                + "");

        mStatusbarBgColor = (ColorPickerPreference) prefSet.findPreference(PREF_STATUSBAR_BACKGROUND_COLOR);
        mStatusbarBgColor.setOnPreferenceChangeListener(this);

        mStatusbarBgStyle = (ListPreference) prefSet.findPreference(PREF_STATUSBAR_BACKGROUND_STYLE);
        mStatusbarBgStyle.setOnPreferenceChangeListener(this);

        updateVisibility();
    }

    private void updateVisibility() {
        int visible = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BACKGROUND_STYLE, 2);
        if (visible == 2) {
            mStatusbarBgColor.setEnabled(false);
        } else {
            mStatusbarBgColor.setEnabled(true);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference == mBatteryBarChargingAnimation) {

            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BATTERY_BAR_ANIMATE,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mBatteryIcon) {

            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BATTERY_ICON, val);
        } else if (preference == mBatteryBarColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                    .valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);

            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BATTERY_BAR_COLOR, intHex);
            return true;
        } else if (preference == mBatteryBar) {

            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BATTERY_BAR, val);
        } else if (preference == mBatteryBarStyle) {

            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BATTERY_BAR_STYLE, val);
        } else if (preference == mBatteryBarThickness) {

            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, val);

        } else if (preference == mStatusbarBgStyle) {
            int value = Integer.valueOf((String) newValue);
            int index = mStatusbarBgStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUSBAR_BACKGROUND_STYLE, value);
            preference.setSummary(mStatusbarBgStyle.getEntries()[index]);
            updateVisibility();
            return true;

        } else if (preference == mStatusbarBgColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            preference.setSummary(hex);

            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BACKGROUND_COLOR, intHex);
            Log.e("BAKED", intHex + "");
        }
        return false;
    }
}
