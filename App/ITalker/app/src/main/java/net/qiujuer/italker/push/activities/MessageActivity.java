package net.qiujuer.italker.push.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import net.qiujuer.italker.factory.model.Author;
import net.qiujuer.italker.push.R;

public class MessageActivity extends Activity {

    public static void show(Context context, Author author){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

}
