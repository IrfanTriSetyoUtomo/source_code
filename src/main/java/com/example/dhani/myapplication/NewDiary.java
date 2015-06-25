package com.example.dhani.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class NewDiary extends ActionBarActivity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DiaryDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary);
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.activity_new_diary);
        setTitle("New Diary");

        mTitleText = (EditText) findViewById(R.id.diaryText);
        mBodyText = (EditText) findViewById(R.id.isiDiary);

        Button saveButton = (Button) findViewById(R.id.save);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(DiaryDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(DiaryDbAdapter.KEY_ROWID)
                    : null;
        }
        populateFields();

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor diary = mDbHelper.fetchDiary(mRowId);
            startManagingCursor(diary);
            mTitleText.setText(diary.getString(
                    diary.getColumnIndexOrThrow(DiaryDbAdapter.KEY_TITLE)));
            mBodyText.setText(diary.getString(
                    diary.getColumnIndexOrThrow(DiaryDbAdapter.KEY_BODY)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DiaryDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createDiary(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateDiary(mRowId, title, body);
        }
    }

    public void CancelNewDiary(View view) {

        finish();
    }

}
