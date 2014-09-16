package sample.stickylistheaders.emilsjolander.se.myapplication;

import static android.provider.BaseColumns._ID;
import static sample.stickylistheaders.emilsjolander.se.myapplication.DbConstants.*;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyActivity extends Activity implements OnClickListener {

    private DBHelper dbhelper = null;

    private TextView result = null;
    private ListView listData = null;
    private EditText editName = null;
    private EditText editTel = null;
    private EditText editEmail = null;
    private EditText editId = null;
    private Button btnAdd = null;
    private Button btnDel = null;
    private Button btnUpdate = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();

        openDatabase();
        show();
        showInList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDatabase();
    }

    private void openDatabase(){
        dbhelper = new DBHelper(this);
    }

    private void closeDatabase(){
        dbhelper.close();
    }

    private void initView(){
        result = (TextView) findViewById(R.id.txtResult);
        listData = (ListView) findViewById(R.id.listData);
        editName = (EditText) findViewById(R.id.editName);
        editTel = (EditText) findViewById(R.id.editTel);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editId = (EditText) findViewById(R.id.editId);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                add();
                break;

            case R.id.btnDel:
                del();
                break;

            case R.id.btnUpdate:
                update();
                break;

            default:
                break;
        }

        show();
        showInList();
    }

    private void add(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, editName.getText().toString());
        values.put(TEL, editTel.getText().toString());
        values.put(EMAIL, editEmail.getText().toString());
        db.insert(TABLE_NAME, null, values);

        cleanEditText();
    }

    private Cursor getCursor(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String[] columns = {_ID, NAME, TEL, EMAIL};

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        startManagingCursor(cursor);

        return cursor;
    }

    private void show(){

        Cursor cursor = getCursor();

        StringBuilder resultData = new StringBuilder("RESULT: \n");

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String tel = cursor.getString(2);
            String email = cursor.getString(3);

            resultData.append(id).append(": ");
            resultData.append(name).append(": ");
            resultData.append(tel).append(": ");
            resultData.append(email).append(": ");
            resultData.append("\n");
        }


        result.setText(resultData);
    }


    private void showInList(){

        Cursor cursor = getCursor();

        String[] from = {_ID, NAME, TEL, EMAIL};
        int[] to = {R.id.txtId, R.id.txtName, R.id.txtTel, R.id.txtEmail};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_item, cursor, from, to);
        listData.setAdapter(adapter);
    }

    private void del(){
        String id = editId.getText().toString();

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(TABLE_NAME, _ID + "=" + id, null);

        cleanEditText();
    }

    private void update(){
        String id = editId.getText().toString();

        ContentValues values = new ContentValues();
        values.put(NAME, editName.getText().toString());
        values.put(TEL, editTel.getText().toString());
        values.put(EMAIL, editEmail.getText().toString());

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.update(TABLE_NAME, values, _ID + "=" + id, null);

        cleanEditText();
    }

    private void cleanEditText(){
        editName.setText("");
        editTel.setText("");
        editEmail.setText("");
        editId.setText("");
    }

}