package org.anuear;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.thoughtcrime.securesms.dependencies.ApplicationDependencies;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
//import okhttp3.Request;
import okhttp3.Response;

import static org.thoughtcrime.securesms.crypto.MasterSecretUtil.PREFERENCES_NAME;

// ANUEAR-DEV
public class AnuTranslator {

    public static String translateString(String text, String toLang, String fromLang){
        OkHttpClient client = new OkHttpClient();
        //String tradictText = null;
        String tradictArray = "";
        String tradictURL = "";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationDependencies.getApplication());
        String homeLang = preferences.getString("pref_language","en");

        if (toLang == null)
            toLang = homeLang;
        if (fromLang == null)
            fromLang = homeLang;

        try {
            tradictURL = "https://translate.googleapis.com/translate_a/single?client=gtx&" +
                    "sl=" + fromLang +//+ activeLang + //sourceLanguage
                    "&tl=" + toLang + "&dt=t&ie=UTF-8&oe=UTF-8&" +
                    "q=" + URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }

        Request request = new Request.Builder().url(tradictURL).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            tradictArray = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Catch needed Text from JSON ARRAY
        JSONArray tmpArray0 = null;
        JSONArray tmpArray1 = null;
        JSONArray tmpArray2 = null;
        String concatText = "";
        try {
            tmpArray0 = new JSONArray(tradictArray);
            tmpArray1 = tmpArray0.getJSONArray(0);

            for (int i=0;i<tmpArray1.length();i++) {
                tmpArray2 = tmpArray1.getJSONArray(i);

                //String test1 = tmpArray2.toString();
                String actualText = tmpArray2.getString(0);

                concatText = concatText.isEmpty()? concatText+actualText : concatText + " " + actualText;
                int hh = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        //final String s = text + "\n\n" + concatText;
        return new StringBuilder().append(text).append("\n\n").append(concatText).toString();
    }

    public static String getLangFromNumber(int lang){
        switch (lang){
            case 10002: return "ar";
            case 10003: return "en";
            case 10004: return "fr";
            case 10005: return "el";
            case 10006: return "de";
            case 10007: return "es";
            default: return "none";

        }
    }

//    public void sendText(String text, int actualLangId,ArrayList<TLRPC.MessageEntity> entities,boolean notify, int scheduleDate){
//        OkHttpClient client = new OkHttpClient();
//        String activeLang = getLangFromNumber(actualLangId);
//        String tradictURL = "";
//        try {
//            tradictURL = "http://translate.googleapis.com/translate_a/single?client=gtx&" +
//                    "sl=auto" + //sourceLanguage
//                    "&tl=" + activeLang + "&dt=t&ie=UTF-8&oe=UTF-8&" +
//                    "q=" + URLEncoder.encode(text, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Request request = new Request.Builder()
//                .url(tradictURL)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            //Callback callback = new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                SendMessagesHelper.getInstance(currentAccount).sendMessage(text, dialog_id, replyingMessageObject, messageWebPage, messageWebPageSearch, entities, null, null, notify, scheduleDate);
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                JSONArray tmpArray0 = null;
//                JSONArray tmpArray1 = null;
//                JSONArray tmpArray2 = null;
//                String tradictArray = response.body().string();
//
//                String concatText = "";
//                try {
//                    tmpArray0 = new JSONArray(tradictArray);
//                    tmpArray1 = tmpArray0.getJSONArray(0);
//
//                    for (int i=0;i<tmpArray1.length();i++) {
//                        tmpArray2 = tmpArray1.getJSONArray(i);
//
//                        //String test1 = tmpArray2.toString();
//                        String actualText = tmpArray2.getString(0);
//
//                        concatText = concatText.isEmpty()? concatText+actualText : concatText + " " + actualText;
//                        SendMessagesHelper.getInstance(currentAccount).sendMessage(text + "\n\n" + concatText, dialog_id, replyingMessageObject, messageWebPage, messageWebPageSearch, entities, null, null, notify, scheduleDate);
//                        int hh = 1;
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


}
