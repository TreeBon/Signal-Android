package org.anuear;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import androidx.appcompat.app.AlertDialog;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.dependencies.ApplicationDependencies;
import org.thoughtcrime.securesms.recipients.RecipientId;

// ANUEAR-DEV
public class AnuLanguageDialog extends AlertDialog {


  protected AnuLanguageDialog(Context context) {
    super(context);
  }

  protected AnuLanguageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }

  protected AnuLanguageDialog(Context context, int theme) {
    super(context, theme);
  }

  //public static void show(final Context context, final @NonNull MuteSelectionListener listener, @Nullable Runnable cancelListener) {
  public static void show(final Context context, RecipientId recipientId) { // String actLang) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);

    //MAKE THE ACTUAL BOLD
    String actLang = DatabaseFactory.getRecipientDatabase(ApplicationDependencies.getApplication()).
            getAnuLanguage(recipientId);
    int actNumber = getNumberFromLanguage(actLang);
    CharSequence[] mItems = builder.getContext().getResources().getTextArray(R.array.anu_languages);
    SpannableStringBuilder str = new SpannableStringBuilder(mItems[actNumber].toString());
    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    mItems[actNumber] = str; //.replace(mItems[2].toString(), "<b>" + mItems[2].toString() + "<\b>");

    // set the things
    builder.setTitle("ANUEAR Translator");
    builder.setItems(mItems, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, final int which) {
        final long muteUntil;

        DatabaseFactory.getRecipientDatabase(ApplicationDependencies.getApplication()).setAnuLanguage(recipientId, getLanguageFromNumber(which));
      }
    });
    builder.show();
  }

  private static int getNumberFromLanguage(String lang){
    if (lang == null) return 0;
    switch (lang) {
      case "ar":
        return 1;
      case "en": return 2;
      case "es": return 3;
      case "fr": return 4;
      case "ge": return 5;
      case "gr": return 6;
      default: return 0;
    }
  }
  private static String getLanguageFromNumber(int num){
    switch (num) {
      case 1: return "ar";
      case 2: return "en";
      case 3: return "es";
      case 4: return "fr";
      case 5: return "ge";
      case 6: return "gr";
      default: return null;
    }
  }
}
