package de.devland.masterpassword.ui;

import android.content.Intent;
import android.os.Bundle;

import java.util.regex.Matcher;

import de.devland.masterpassword.R;
import de.devland.masterpassword.base.ui.BaseActivity;
import de.devland.masterpassword.base.util.Constants;
import de.devland.masterpassword.base.util.SnackbarUtil;


public class EditActivity extends BaseActivity {

    EditFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        if (savedInstanceState == null) {
            fragment = new EditFragment();
            Bundle arguments = getIntent().getExtras();
            String hostname = handleShareIntent();
            if (hostname != null) {
                arguments.putString(EditFragment.ARG_HOSTNAME, hostname);
            }
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.container, fragment)
                                       .commit();
        } else {
            fragment = (EditFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragment");
        }
        getSupportActionBar().setTitle(R.string.title_activity_edit);
        if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private String handleShareIntent() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                Matcher matcher = Constants.pattern.matcher(sharedText);
                if (matcher.matches()) {
                    String hostname = matcher.group(2);
                    return hostname;
                } else {
                    SnackbarUtil.showShort(this, R.string.msg_noUrlFound);
                }
            }
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
    }

    @Override
    public void onBackPressed() {
        fragment.onBackPressed();
        super.onBackPressed();
    }
}
