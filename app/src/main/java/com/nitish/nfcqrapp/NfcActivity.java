package com.nitish.nfcqrapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nitish.nfcqrapp.data.ProductDetail;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NfcActivity extends Activity {
    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private static final String LOGING_TAG = "NfcActivity";
    private LinearLayout mTagContent;
    private String LOGGING_TAG = "NfcActivity";

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private AlertDialog mDialog;
    private boolean nfcFindFlag = false;

    private List<Tag> mTags = new ArrayList<Tag>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        mTagContent = (LinearLayout) findViewById(R.id.list);
        resolveIntent(getIntent());

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            // finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true)});
    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private String resolveIntent(Intent intent) {
        String tegId = "";
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            byte[] empty = new byte[0];
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            tegId = dumpTagData(tag);
        }
        return tegId;
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();

        return String.valueOf(toReversedDec(id));

    }


    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        String nfcId = resolveIntent(intent);
        Log.d(LOGGING_TAG, "resolveIntent(intent); = " + nfcId);
        if (!nfcFindFlag) {
            nfcFindFlag = true;
            getDataFromServer(nfcId);

        }


        //Toast.makeText(getApplicationContext(), " resolveIntent(intent);" + , Toast.LENGTH_SHORT).show();
    }

    public void getDataFromServer(String id) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String result = HttpUrl.SERVER_FALSE;
                try {
                    String nfcId = strings[0];
                    Log.d(LOGING_TAG, "nfcId = " + nfcId);

                    result = Utility.executePost(HttpUrl.productDetais, nfcId);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = HttpUrl.SERVER_FALSE;
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d(LOGING_TAG, "onPostExecute result = " + result);

                if (result != null) {
                    result = result.trim();
                    if (!"".equals(result)) {
                        if (!HttpUrl.SERVER_FALSE.equals(result)) {
                            Gson gson = new Gson();
                            ProductDetail productDetail = gson.fromJson(result, ProductDetail.class);
                            if (productDetail != null) {
                                String responseStatus = productDetail.getResult();
                                Log.d(LOGING_TAG, "responseStatus = " + responseStatus);
                                if (responseStatus != null) {
                                    if (!"".equals(responseStatus)) {
                                        if (responseStatus.equals(HttpUrl.SERVER_FALSE)) {
                                            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                                            Log.d(LOGING_TAG, "No Data");
                                        } else if (responseStatus.equals(HttpUrl.SERVER_OK)) {
                                            Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                                            intent.putExtra(HttpUrl.PRODUCT_DETAIS_KEY, result);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "response status " + responseStatus, Toast.LENGTH_SHORT).show();
                                            Log.d(LOGING_TAG, "responseStatus comes error ");
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "resposne status empty ", Toast.LENGTH_SHORT).show();
                                        Log.d(LOGING_TAG, "responseStatus comes error ");
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "empty resposne", Toast.LENGTH_SHORT).show();
                                    Log.d(LOGING_TAG, "responseStatus comes error ");
                                }


                            } else {
                                Log.d(LOGING_TAG, "jsone persering Error");

                            }
                        } else {
                            Log.d(LOGING_TAG, "execption occes");

                        }

                    } else {
                        Log.d(LOGING_TAG, "empty Data");
                    }
                } else {
                    Log.d(LOGING_TAG, "null data");

                }

                nfcFindFlag = false;
                super.onPostExecute(result);
            }
        }.execute(id);


    }
}

