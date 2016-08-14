/*
* Copyright (C) 2016 RR
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.settings.rr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import com.android.settings.rr.SeekBarPreference;
import android.provider.Settings;
import com.android.settings.util.Helpers;
import org.cyanogenmod.internal.util.CmLockPatternUtils;
import com.android.settings.Utils;
import android.provider.SearchIndexableResource;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.util.Log;

import com.android.internal.logging.MetricsLogger;
import cyanogenmod.providers.CMSettings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.util.Helpers;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

import java.util.List;
import java.util.ArrayList;

public class QsColors extends SettingsPreferenceFragment  implements Preference.OnPreferenceChangeListener ,Indexable{

 private static final String QS_HEADER_TEXT_COLOR = "qs_header_text_color";
 private static final String QS_HEADER_COLOR = "qs_header_color";
 private static final String QS_TEXT_COLOR = "qs_text_color";
 private static final String QS_ICON_COLOR = "qs_icon_color";
 private static final String QS_BACKGROUND_COLOR = "qs_bg_color";
 private static final String QS_COLOR_SWITCH = "qs_color_switch";
 private static final String QS_ICON = "qs_brightness_icon_color";
 private static final String PREF_GRADIENT_ORIENTATION = "qs_background_gradient_orientation";
 private static final String PREF_USE_CENTER_COLOR = "qs_background_gradient_use_center_color";
 private static final String PREF_START_COLOR = "qs_background_color_start";
 private static final String PREF_CENTER_COLOR = "qs_background_color_center";
 private static final String PREF_END_COLOR ="qs_background_color_end";
 private static final String BG_COLORS = "qs_bg_colors";
 private static final String RR_COLORS = "rr_qs_bg_colors";
 private static final String GENERAL_COLORS = "rr_qs_bg_colors_general";
 private static final String PREF_GRADIENT_ORIENTATION_HEADER = "header_background_gradient_orientation";
 private static final String PREF_USE_CENTER_COLOR_HEADER = "header_background_gradient_use_center_color";
 private static final String PREF_HEADER_START_COLOR = "header_background_color_start";
 private static final String PREF_CENTER_COLOR_HEADER = "header_background_color_center";
 private static final String PREF_HEADER_END_COLOR = "header_background_color_end";

    static final int DEFAULT = 0xffffffff;
    static final int DEFAULT_BG = 0xff263238;
    static final int DEFAULT_HEADER_BG = 0xff384248;
    static final int DEFAULT_SECONDARY_TEXT = 0xb3ffffff;
    static final int DEFAULT_TEXT = 0xffffffff;
    private static final int BLACK = 0xff000000;

    private static final int BACKGROUND_ORIENTATION_T_B = 270;
    
    private static final int MENU_RESET = Menu.FIRST;
	

    private ColorPickerPreference mHeaderTextColor;
    private ColorPickerPreference mHeaderColor;
    private ColorPickerPreference mQsTextColor;
    private ColorPickerPreference mQsIconColor;	
    private ColorPickerPreference mQsBgColor;	
    private ColorPickerPreference mSliderIconColor;
    private ListPreference mQsColorSwitch;
    private SwitchPreference mUseCenterColor;
    private ColorPickerPreference mStartColor;
    private ColorPickerPreference mCenterColor;
    private ColorPickerPreference mEndColor;
    private ListPreference mGradientOrientationHeader;
    private SwitchPreference mHeaderUseCenterColor;
    private ColorPickerPreference mHeaderStartColor;
    private ColorPickerPreference mHeaderCenterColor;
    private ColorPickerPreference mHeaderEndColor;
    private ListPreference mGradientOrientation;
   
    private ContentResolver mResolver;
   

 @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
	refreshSettings();
	}
   
   public void refreshSettings() {
        addPreferencesFromResource(R.xml.rr_qs_colors);
        PreferenceScreen prefSet = getPreferenceScreen();

        final ContentResolver resolver = getActivity().getContentResolver();
	mResolver = getActivity().getContentResolver();

   	int intColor;
        String hexColor;

        mHeaderTextColor = (ColorPickerPreference) findPreference(QS_HEADER_TEXT_COLOR);
        mHeaderTextColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_HEADER_TEXT_COLOR, DEFAULT);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mHeaderTextColor.setSummary(hexColor);
        mHeaderTextColor.setNewPreviewColor(intColor);

        mHeaderColor = (ColorPickerPreference) findPreference(QS_HEADER_COLOR);
        mHeaderColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_HEADER_COLOR,DEFAULT_HEADER_BG);
        hexColor = String.format("#%08x", (0xff384248 & intColor));
        mHeaderColor.setSummary(hexColor);
        mHeaderColor.setNewPreviewColor(intColor);

        mQsTextColor = (ColorPickerPreference) findPreference(QS_TEXT_COLOR);
        mQsTextColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_TEXT_COLOR, DEFAULT_SECONDARY_TEXT);
        hexColor = String.format("#%08x", (0xb3ffffff & intColor));
        mQsTextColor.setSummary(hexColor);
        mQsTextColor.setNewPreviewColor(intColor);

        mQsIconColor = (ColorPickerPreference) findPreference(QS_ICON_COLOR);
        mQsIconColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_ICON_COLOR, DEFAULT);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mQsIconColor.setSummary(hexColor);
        mQsIconColor.setNewPreviewColor(intColor);

       	mQsBgColor = (ColorPickerPreference) findPreference(QS_BACKGROUND_COLOR);
        mQsBgColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_BACKGROUND_COLOR, DEFAULT_BG);
        hexColor = String.format("#%08x", (0xff263238 & intColor));
        mQsBgColor.setSummary(hexColor);
        mQsBgColor.setNewPreviewColor(intColor);

       	mSliderIconColor = (ColorPickerPreference) findPreference(QS_ICON);
        mSliderIconColor.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.QS_BRIGHTNESS_ICON_COLOR, DEFAULT);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mSliderIconColor.setSummary(hexColor);
        mSliderIconColor.setNewPreviewColor(intColor);

	int qscolor = Settings.System.getIntForUser(mResolver,
                            Settings.System.QS_COLOR_SWITCH, 0,
                            UserHandle.USER_CURRENT);
        mQsColorSwitch = (ListPreference) prefSet.findPreference(QS_COLOR_SWITCH);
        mQsColorSwitch.setValue(String.valueOf(qscolor));
        mQsColorSwitch.setSummary(mQsColorSwitch.getEntry());
        mQsColorSwitch.setOnPreferenceChangeListener(this);


	PreferenceCategory catBgColors =
                (PreferenceCategory) findPreference(BG_COLORS);

	PreferenceCategory catrrColors =
                (PreferenceCategory) findPreference(RR_COLORS);

	PreferenceCategory catgenColors =
                (PreferenceCategory) findPreference(GENERAL_COLORS);

        mGradientOrientationHeader =
                (ListPreference) findPreference(PREF_GRADIENT_ORIENTATION_HEADER);
        final int orientation = Settings.System.getInt(mResolver,
                Settings.System.HEADER_BACKGROUND_GRADIENT_ORIENTATION,
                BACKGROUND_ORIENTATION_T_B);
         mGradientOrientationHeader.setValue(String.valueOf(orientation));
         mGradientOrientationHeader.setSummary(mGradientOrientationHeader.getEntry());
         mGradientOrientationHeader.setOnPreferenceChangeListener(this);
 
         mStartColor =
                 (ColorPickerPreference) findPreference(PREF_START_COLOR);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.QS_BACKGROUND_COLOR_START, BLACK); 
         mStartColor.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mStartColor.setSummary(hexColor);
         mStartColor.setOnPreferenceChangeListener(this);
 
         final boolean useCenterColor = Settings.System.getInt(mResolver,
                 Settings.System.QS_BACKGROUND_GRADIENT_USE_CENTER_COLOR, 0) == 1;;
 
         mUseCenterColor = (SwitchPreference) findPreference(PREF_USE_CENTER_COLOR);
         mUseCenterColor.setChecked(useCenterColor);
         mUseCenterColor.setOnPreferenceChangeListener(this);
 
         mStartColor.setTitle(getResources().getString(R.string.background_start_color_title));

             mCenterColor =
                     (ColorPickerPreference) findPreference(PREF_CENTER_COLOR);
             intColor = Settings.System.getInt(mResolver,
                     Settings.System.QS_BACKGROUND_COLOR_CENTER, BLACK); 
             mCenterColor.setNewPreviewColor(intColor);
             hexColor = String.format("#%08x", (0xffffffff & intColor));
             mCenterColor.setSummary(hexColor);
             mCenterColor.setOnPreferenceChangeListener(this);

 
         mEndColor =
                 (ColorPickerPreference) findPreference(PREF_END_COLOR);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.QS_BACKGROUND_COLOR_END, BLACK); 
         mEndColor.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mEndColor.setSummary(hexColor);
         mEndColor.setOnPreferenceChangeListener(this);

	   mGradientOrientation =
                   (ListPreference) findPreference(PREF_GRADIENT_ORIENTATION);
           final int orientation1 = Settings.System.getInt(mResolver,
                   Settings.System.QS_BACKGROUND_GRADIENT_ORIENTATION,
                   BACKGROUND_ORIENTATION_T_B);
           mGradientOrientation.setValue(String.valueOf(orientation1));
           mGradientOrientation.setSummary(mGradientOrientation.getEntry());
           mGradientOrientation.setOnPreferenceChangeListener(this);
   
           mHeaderStartColor =
                   (ColorPickerPreference) findPreference(PREF_HEADER_START_COLOR);
           intColor = Settings.System.getInt(mResolver,
                   Settings.System.HEADER_BACKGROUND_COLOR_START, BLACK); 
           mHeaderStartColor.setNewPreviewColor(intColor);
           hexColor = String.format("#%08x", (0xffffffff & intColor));
           mHeaderStartColor.setSummary(hexColor);
           mHeaderStartColor.setOnPreferenceChangeListener(this);
   
           final boolean useheaderCenterColor = Settings.System.getInt(mResolver,
                   Settings.System.HEADER_BACKGROUND_GRADIENT_USE_CENTER_COLOR, 0) == 1;;
   
           mHeaderUseCenterColor = (SwitchPreference) findPreference(PREF_USE_CENTER_COLOR_HEADER);
           mHeaderUseCenterColor.setChecked(useheaderCenterColor);
           mHeaderUseCenterColor.setOnPreferenceChangeListener(this);
   
           mStartColor.setTitle(getResources().getString(R.string.background_start_color_title));
   
               mHeaderCenterColor =
                       (ColorPickerPreference) findPreference(PREF_CENTER_COLOR_HEADER);
               intColor = Settings.System.getInt(mResolver,
                       Settings.System.HEADER_BACKGROUND_COLOR_CENTER, BLACK); 
               mHeaderCenterColor.setNewPreviewColor(intColor);
               hexColor = String.format("#%08x", (0xffffffff & intColor));
               mHeaderCenterColor.setSummary(hexColor);
               mHeaderCenterColor.setOnPreferenceChangeListener(this);
   
           mHeaderEndColor =
                   (ColorPickerPreference) findPreference(PREF_HEADER_END_COLOR);
           intColor = Settings.System.getInt(mResolver,
                   Settings.System.HEADER_BACKGROUND_COLOR_END, BLACK); 
           mHeaderEndColor.setNewPreviewColor(intColor);
           hexColor = String.format("#%08x", (0xffffffff & intColor));
           mHeaderEndColor.setSummary(hexColor);
           mHeaderEndColor.setOnPreferenceChangeListener(this);
	   setcolordisabler(qscolor);
	   setHasOptionsMenu(true);

}


    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.NOTIFICATION_DRAWER_SETTINGS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
	ContentResolver resolver = getActivity().getContentResolver();
	Resources res = getResources();
	  if (preference == mHeaderTextColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.QS_HEADER_TEXT_COLOR, intHex);
            return true;
         } else if (preference == mHeaderColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.QS_HEADER_COLOR, intHex);
            return true;
         } else if (preference == mQsTextColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.QS_TEXT_COLOR, intHex);
            return true;
         } else if (preference == mQsIconColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.QS_ICON_COLOR, intHex);
            return true;
         }  else if (preference == mQsBgColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.QS_BACKGROUND_COLOR, intHex);	
            return true;
         }  else if (preference == mQsColorSwitch) {
                int qscolor = Integer.parseInt((String) newValue);
                int index = mQsColorSwitch.findIndexOfValue((String) newValue);
                Settings.System.putIntForUser(mResolver, Settings.System.
                        QS_COLOR_SWITCH, qscolor, UserHandle.USER_CURRENT);
                mQsColorSwitch.setSummary(mQsColorSwitch.getEntries()[index]);
		setcolordisabler(qscolor);
                return true;
         }  else if (preference == mSliderIconColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.QS_BRIGHTNESS_ICON_COLOR, intHex);	
            return true;
         } else if (preference == mUseCenterColor) {
                boolean value = (Boolean) newValue;
                Settings.System.putInt(mResolver,
                        Settings.System.QS_BACKGROUND_GRADIENT_USE_CENTER_COLOR,
                        value ? 1 : 0);
                return true;
            } else if (preference == mStartColor) {
                String hex = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                int intHex = ColorPickerPreference.convertToColorInt(hex);
                Settings.System.putInt(mResolver,
                        Settings.System.QS_BACKGROUND_COLOR_START, intHex);
                preference.setSummary(hex);
                return true;
            } else if (preference == mCenterColor) {
                String hex = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                int intHex = ColorPickerPreference.convertToColorInt(hex);
                Settings.System.putInt(mResolver,
                        Settings.System.QS_BACKGROUND_COLOR_CENTER, intHex);
                preference.setSummary(hex);
                return true;
            } else if (preference == mEndColor) {
                String hex = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                int intHex = ColorPickerPreference.convertToColorInt(hex);
                Settings.System.putInt(mResolver,
                        Settings.System.QS_BACKGROUND_COLOR_END, intHex);
                preference.setSummary(hex);
                return true;
            } else if (preference == mGradientOrientation) {
                int intValue = Integer.valueOf((String) newValue);
                int index = mGradientOrientation.findIndexOfValue((String) newValue);
                Settings.System.putInt(mResolver,
                        Settings.System.QS_BACKGROUND_GRADIENT_ORIENTATION,
                        intValue);
                mGradientOrientation.setSummary(mGradientOrientation.getEntries()[index]);
                return true;
         }  else if (preference == mHeaderUseCenterColor) {
               boolean value = (Boolean) newValue;
               Settings.System.putInt(mResolver,
                       Settings.System.HEADER_BACKGROUND_GRADIENT_USE_CENTER_COLOR,
                       value ? 1 : 0);
               return true;
           } else if (preference == mHeaderStartColor) {
               String hex = ColorPickerPreference.convertToARGB(
                       Integer.valueOf(String.valueOf(newValue)));
               int intHex = ColorPickerPreference.convertToColorInt(hex);
               Settings.System.putInt(mResolver,
                       Settings.System.HEADER_BACKGROUND_COLOR_START, intHex);
               preference.setSummary(hex);
               return true;
           } else if (preference == mHeaderCenterColor) {
               String hex = ColorPickerPreference.convertToARGB(
                       Integer.valueOf(String.valueOf(newValue)));
               int intHex  = ColorPickerPreference.convertToColorInt(hex);
               Settings.System.putInt(mResolver,
                       Settings.System.HEADER_BACKGROUND_COLOR_CENTER, intHex);
               preference.setSummary(hex);
               return true;
           } else if (preference == mHeaderEndColor) {
               String hex= ColorPickerPreference.convertToARGB(
                       Integer.valueOf(String.valueOf(newValue)));
               int intHex = ColorPickerPreference.convertToColorInt(hex);
               Settings.System.putInt(mResolver,
                       Settings.System.HEADER_BACKGROUND_COLOR_END, intHex);
               preference.setSummary(hex);
               return true;
           }  else if (preference == mGradientOrientationHeader) {
               int intValue = Integer.valueOf((String) newValue);
               int index = mGradientOrientationHeader.findIndexOfValue((String) newValue);
               Settings.System.putInt(mResolver,
                       Settings.System.HEADER_BACKGROUND_GRADIENT_ORIENTATION,
                       intValue);
               mGradientOrientationHeader.setSummary(mGradientOrientationHeader.getEntries()[index]);
               return true;
         }
	return false;
	}


 @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_settings_reset)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                resetToDefault();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void resetToDefault() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.header_colors_reset_title);
        alertDialog.setMessage(R.string.header_colors_reset_message);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetValues();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, null);
        alertDialog.create().show();
    }

    private void resetValues() {
        Settings.System.putInt(getContentResolver(),
                Settings.System.QS_HEADER_TEXT_COLOR, DEFAULT);
        mHeaderTextColor.setNewPreviewColor(DEFAULT);
        mHeaderTextColor.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.QS_HEADER_COLOR, DEFAULT_HEADER_BG);
        mHeaderColor.setNewPreviewColor(DEFAULT_HEADER_BG);
        mHeaderColor.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.QS_ICON_COLOR, DEFAULT);
        mQsIconColor.setNewPreviewColor(DEFAULT);
        mQsIconColor.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.QS_BACKGROUND_COLOR, DEFAULT_BG);
        mQsBgColor.setNewPreviewColor(DEFAULT_BG);
        mQsBgColor.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.QS_BRIGHTNESS_ICON_COLOR, DEFAULT);
        mSliderIconColor.setNewPreviewColor(DEFAULT);
        mSliderIconColor.setSummary(R.string.default_string);

    }
    
    public void setcolordisabler(int qscolor) {
	PreferenceCategory catBgColors =
                (PreferenceCategory) findPreference(BG_COLORS);

	PreferenceCategory catrrColors =
                (PreferenceCategory) findPreference(RR_COLORS);


	PreferenceCategory catgenColors =
                (PreferenceCategory) findPreference(GENERAL_COLORS);
	 if (qscolor == 0) {
		 catrrColors.setEnabled(false);
		 catBgColors.setEnabled(false);
		 catgenColors.setEnabled(false);
         } else if (qscolor == 1) {
		 catrrColors.setEnabled(true);
	 	 catBgColors.setEnabled(false);
	         catgenColors.setEnabled(true);
	 } else if (qscolor == 2) {
		 catrrColors.setEnabled(false);
		 catBgColors.setEnabled(true);
		 catgenColors.setEnabled(true);
	   }
	}
    
    
    	    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                             boolean enabled) {
                     ArrayList<SearchIndexableResource> result =
                             new ArrayList<SearchIndexableResource>();
 
                     SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.rr_qs_colors;
                     result.add(sir);
 
                     return result;
                 }
 
                 @Override
                 public List<String> getNonIndexableKeys(Context context) {
                     final List<String> keys = new ArrayList<String>();
                     return keys;
                 }
         };

}
