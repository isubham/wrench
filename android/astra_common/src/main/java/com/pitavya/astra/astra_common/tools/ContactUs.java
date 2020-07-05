package com.pitavya.astra.astra_common.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ContactUs {

    public static final String greetTeam = Constants.GREET_TEAM;
    private static String[] toList = new String[]{"contact@pitavya.com"};
    private static String[] ccList = new String[]{"faizrocks9211@gmail.com", "subhamkumarchandrawansi@gmail.com", "coolmanishks@gmail.com", "sthakur1920@gmail.com"};
    private static String[] bccList = new String[]{};
    private static String bugSubject = Constants.SUBJECT_FOR_BUG;
    private static String bugDescriptionHere = Constants.BUG_DESCRIPTION_HERE_HELP_TEXT;
    private static String contactSubject = Constants.CONTACT_SUBJECT;

    public static void reportBug(Context context, Uri fileUri) {

        Intent shareBugIntent = new Intent(Intent.ACTION_SEND);

        shareBugIntent.putExtra(Intent.EXTRA_TEXT, bugDescriptionHere);
        shareBugIntent.putExtra(Intent.EXTRA_EMAIL, toList);
        shareBugIntent.putExtra(Intent.EXTRA_SUBJECT, bugSubject);
        //shareBugIntent.putExtra(Intent.EXTRA_CC, ccList);
        shareBugIntent.setType("text/html");
        shareBugIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        context.startActivity(Intent.createChooser(shareBugIntent, "Pitavya : Sharing With Team Astra"));


    }

    public static void contactUs(Context context) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, toList);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, contactSubject);
        //sendIntent.putExtra(Intent.EXTRA_CC, ccList);
        sendIntent.putExtra(Intent.EXTRA_TEXT, greetTeam+",");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Pitavya : Contact Us");
        context.startActivity(shareIntent);

    }

}
