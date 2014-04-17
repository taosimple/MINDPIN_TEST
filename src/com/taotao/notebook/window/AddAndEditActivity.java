package com.taotao.notebook.window;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.taotao.notebook.R;
import com.taotao.notebook.db.MySQLiteHelp;
import com.taotao.notebook.enity.Note;
import com.taotao.notebook.util.LogUtil;
import com.taotao.notebook.util.Tools;
import com.taotao.notebook.view.RecordPopwindow;
import com.taotao.notebook.view.RecordPopwindow.OnFinish;

public class AddAndEditActivity extends BaseActivity {
	
	private Note mNote;
	private boolean isEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit);
		mNote = (Note)getIntent().getSerializableExtra("note");
		if(mNote != null) isEdit = true;
		initView();
		filterData();
	}
	
	private EditText editText;
	private Button btnPlay;
	private ImageView photo;
	
	private void initView(){
		editText = (EditText) findViewById(R.id.edit_content);
		btnPlay = (Button) findViewById(R.id.btn_play_voice);
		photo = (ImageView) findViewById(R.id.img_photo);
     }
	
	
	private void filterData(){
		if(!isEdit) return;
		if(mNote.content != null) editText.setText(mNote.content);
		if(mNote.img != null) {
        	img_path = mNote.img;
			photo.setVisibility(View.VISIBLE);
			photo.setImageBitmap(BitmapFactory.decodeFile(img_path));
		}
        
        if(mNote.audio != null){
        	View view = findViewById(R.id.linear_audio);
			view.setVisibility(View.VISIBLE);
			audioPath = mNote.audio;
        }

	}
	
	RecordPopwindow pop;
	MusicPlayThread musicTask;
	String audioPath, img_path;
	static final int REQUEST_CODE_IMG = 1001;
	static final int REQUEST_CODE_IMG_FILTER = 1002;
	
	public void doClick(View v){
		LogUtil.d(this, "--onClick--");
		switch (v.getId()) {
		case R.id.btn_add_audio://‘ˆº”…˘“Ù
			
			if(pop == null){
				pop = new RecordPopwindow(getApplicationContext(), audioPath, finishLis);
			}
			pop.showAtLocation(editText, Gravity.CENTER, 0, 0, audioPath);
			break;

		case R.id.btn_add_img://‘ˆº”Õº∆¨
			Tools.selectPicture(this, REQUEST_CODE_IMG);
			break;
			
		case R.id.btn_play_voice://≤•∑≈…˘“Ù
			if(musicTask == null || !musicTask.isPlaying()){
				//String path = (String) v.getTag();
				musicTask = new MusicPlayThread(audioPath);
				musicTask.start();
				btnPlay.setText("Õ£÷π");
			}else{
				musicTask.stopPlay();
				musicTask.interrupt();
				musicTask = null;
				btnPlay.setText("≤•∑≈");
			}
			
			break;
			
		case R.id.btn_delete:
			Button btn = (Button) v;
			if(btn.getText().equals("…æ≥˝")){//
				
			}else{//
				if(isEdit){
					mNote.audio = audioPath;
					mNote.content = editText.getText().toString();
					mNote.img = img_path;
					MySQLiteHelp.updateNote(mNote, getApplicationContext());
				}else{
					Note note = new Note();
					note.audio = audioPath;
					note.content = editText.getText().toString();
					note.img = img_path;
					MySQLiteHelp.addNote(note, getApplicationContext());
					
				}
				setResult(RESULT_OK);
			}
			
		case R.id.btn_back:
			onBackPressed();
			break;
			
		}
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			btnPlay.setText("≤•∑≈");
		};
	};
	
	class MusicPlayThread extends Thread{
		MediaPlayer player;
		
		public MusicPlayThread(String path){
			setDaemon(true);
			player = new MediaPlayer();
			try {
				player.setDataSource(path);
				player.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						mHandler.sendEmptyMessage(0);
					}
				});
			} catch (IllegalArgumentException | SecurityException
					| IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		@Override
		public void run() {
			super.run();
			try {
				player.prepare();
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			player.start();
		}
		
		public boolean isPlaying(){
			return player.isPlaying();
		}
		
		public void stopPlay(){
			player.stop();
			player.release();
		}
	}
	
	
	RecordPopwindow.OnFinish finishLis = new OnFinish() {
		@Override
		public void onFinish(String path) {
			File file = new File(path);
			if(file.exists()){
				View view = findViewById(R.id.linear_audio);
				view.setVisibility(View.VISIBLE);
				audioPath = path;
			}
		}
	};
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.d(this, "onActivityResult : requestCode = " + requestCode + " resultCode = " + resultCode);
		if(resultCode != RESULT_OK) return;
		if(requestCode == REQUEST_CODE_IMG){
			String path = Tools.getPictureUriFromIntent(data, getApplicationContext());
			if(path == null || path.trim().equals("")){
				Toast.makeText(getApplicationContext(), "Õº∆¨µÿ÷∑Œﬁ–ß", Toast.LENGTH_SHORT).show();
				return;
			}
			img_path = path;
			photo.setVisibility(View.VISIBLE);
			photo.setImageBitmap(BitmapFactory.decodeFile(path));
			ImageFilterMain.launcheForResult(this, path, REQUEST_CODE_IMG_FILTER);
		}
		
		if(requestCode == REQUEST_CODE_IMG_FILTER){
			String filter_path = data.getStringExtra("path");
			img_path = filter_path;
			photo.setVisibility(View.VISIBLE);
			photo.setImageBitmap(BitmapFactory.decodeFile(filter_path));
		}
		
	};
	
	
	public static void launcheForResult(Activity context){
		Intent intent = new Intent(context , AddAndEditActivity.class);
		context.startActivityForResult(intent,0);
	}
	
	public static void launcheForResult(Activity context, Note note){
		Intent intent = new Intent(context , AddAndEditActivity.class);
		intent.putExtra("note", note);
		context.startActivityForResult(intent,0);
	}
	
	@Override
	public void onBackPressed() {
		if(pop != null && pop.isShowing()){
			pop.dismiss();
			return;
		}
		super.onBackPressed();
	}
	
	
	
}
