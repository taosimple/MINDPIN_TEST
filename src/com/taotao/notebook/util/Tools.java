package com.taotao.notebook.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class Tools {
	
	
	public static String getRecordDir(){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Notebook" + File.separator + "recorder" + File.separator;
		Log.i("getRecordDir", "getRecordDir--path = " + path);
		File dir = new File(path);
		if(!dir.exists()) {
			LogUtil.e("getRecordDir", ">>>" + dir.mkdirs());
		}
		return path;
	}
	
	public static String getImageFilterDir(){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Notebook" + File.separator + "img_filter" + File.separator;
		Log.i("getRecordDir", "getRecordDir--path = " + path);
		File dir = new File(path);
		if(!dir.exists()) {
			LogUtil.e("getRecordDir", ">>>" + dir.mkdirs());
		}
		return path;
	}
	
	/**
	 * 选择图片不可裁剪
	 * 
	 * @param acty
	 */
	public static void selectPicture(Activity acty, int requestCode) {
		LogUtil.d(acty, "select photo");
		Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
		intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		acty.startActivityForResult(intent, requestCode);
	}
     
	/**
	 * @method: selectPicture
	 * @param data
	 * @param ctx
	 * @return
	 */
	public static String getPictureUriFromIntent(Intent data, Context ctx) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = ctx.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String mPicturePath = cursor.getString(columnIndex);
		LogUtil.d("picture_path", "picture--path = " + mPicturePath);
		cursor.close();
		return mPicturePath;
	}
	
	
}
