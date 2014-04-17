package com.taotao.notebook.window;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.taotao.notebook.R;

public class FilterActivity extends BaseActivity {
	
	String path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		path = getIntent().getStringExtra("path");
		initView();
		bmp = BitmapFactory.decodeFile(path);
	    img.setImageBitmap(bmp);
	}
	
	Bitmap bmp;
	ImageView img;
	private void initView(){
		img = (ImageView) findViewById(R.id.filter_img);
	}
	
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.filter_BoxBlurFilter:
			break;

		default:
			break;
		}
	}
	
	
	public static void launcheForResult(Context context, String path){
		Intent intent = new Intent(context , FilterActivity.class);
		intent.putExtra("path", path);
		context.startActivity(intent);
	}
	
}
