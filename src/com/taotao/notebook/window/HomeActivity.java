package com.taotao.notebook.window;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.taotao.notebook.R;
import com.taotao.notebook.db.MySQLiteHelp;
import com.taotao.notebook.enity.Note;
import com.taotao.notebook.util.Tools;

public class HomeActivity extends BaseActivity{
	
	private TextView textNumber;
	private Button btnAdd;
	private ListView mListView;
    private List<Note> data;
    ArrayAdapter<String> adapter;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
		filterListView();
	};
	
	private void filterListView(){
		data = MySQLiteHelp.findAllNotes(getApplicationContext());
		if(data != null){
			String [] names = new String[data.size()];
			for(int i = 0 ; i < data.size(); i++){
				String content = data.get(i).content;
				if(content != null && content.length() > 10){
					content = content.substring(0, 10);
				}
				names[i] = content;
			}
			adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
			mListView.setAdapter(adapter);
			textNumber.setText("共有笔记" + data.size() + "条");
		}else{
			
		}
	}
	
	
	private void initView(){
		textNumber = (TextView) findViewById(R.id.text_title);
		btnAdd = (Button) findViewById(R.id.btn_add_note);
		mListView = (ListView) findViewById(R.id.list_note);
		mListView.setOnItemClickListener(itemClickLis);
		mListView.setOnItemLongClickListener(longClick);
	}
	
	OnItemClickListener itemClickLis = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AddAndEditActivity.launcheForResult(HomeActivity.this, data.get(position));
		}
	};
	
	OnItemLongClickListener longClick = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
			builder.setTitle("删除");
			builder.setMessage("删除这条笔记")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Note note = data.get(position);
					data.remove(position);
					MySQLiteHelp.deleteNote(note._id, getApplicationContext());
					filterListView();
					
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
			return true;
		}
	};
	
	
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.btn_add_note:
			AddAndEditActivity.launcheForResult(this);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode == RESULT_OK){
			filterListView();
		}
	}
	
	
}
