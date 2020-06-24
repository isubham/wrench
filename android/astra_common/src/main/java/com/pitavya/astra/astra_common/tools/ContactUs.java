package com.pitavya.astra.astra_common.tools;

import android.content.Context;
import android.content.Intent;

public class ContactUs {

    private static String[] toList = new String[]{"contact@pitavya.com","pitavya-dev@gmail.com"};
    private static String[] ccList = new String[]{"faizrocks9211@gmail.com", "subhamkumarchandrawansi@gmail.com", "coolmanishks@gmail.com", "sthakur1920@gmail.com"};
    private static String[] bccList = new String[]{};
    private static String bugSubject = Constants.SUBJECT_FOR_BUG;
    private static String bugDescriptionHere = Constants.BUG_DESCRIPTION_HERE_HELP_TEXT;
    private static String contactSubject = Constants.CONTACT_SUBJECT;


    public static void reportBug(Context context){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, bugDescriptionHere);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, toList);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, bugSubject);
        sendIntent.putExtra(Intent.EXTRA_CC, ccList);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }

    public static void contactUs(Context context){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, toList);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,contactSubject );
        sendIntent.putExtra(Intent.EXTRA_CC, ccList);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);

    }

}
