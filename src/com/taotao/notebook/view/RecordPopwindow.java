package com.taotao.notebook.view;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.taotao.notebook.R;
import com.taotao.notebook.util.LogUtil;
import com.taotao.notebook.util.Tools;

public class RecordPopwindow extends PopupWindow implements OnClickListener{
	
	
	String srcPath, newPath;
	Button mButton;
	
	
	public RecordPopwindow(Context context, String path ,OnFinish listener){
		super(context);
		View contentView = LayoutInflater.from(context).inflate(R.layout.layout_record, null);
		LogUtil.i(this, "contentView = " + contentView);
		mButton = (Button) contentView.findViewById(R.id.record_btn);
		mButton.setOnClickListener(this);
		Drawable draw = context.getResources().getDrawable(R.drawable.translate_bar_middle_no_voice);
		setBackgroundDrawable(draw);
		setContentView(contentView);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setOnDismissListener(missLis);
		setTouchable(true);
		this.mOnFinish = listener;
		srcPath = path;
	}
	
	
	public void showAtLocation(View parent, int gravity, int x, int y, String path) {
		showAtLocation(parent, gravity, x, y);
		srcPath = path;
		
	}

	MediaRecorder recorder;
	boolean isFinish, isStart;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.record_btn:
			Button btn = (Button) v;
			String text = btn.getText().toString();
			if(text.equals("Â¼Òô")){
				isStart = true;
				btn.setText("Íê³É");
				
				recorder = new MediaRecorder();
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				
				newPath = Tools.getRecordDir() + System.currentTimeMillis()+"";
				File file = new File(newPath);
				if(file.exists()){
					file.delete();
				}else{
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				recorder.setOutputFile(newPath);
				try {
					recorder.prepare();
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				recorder.start();
			}else{
				isFinish = true;
				recorder.stop();
				recorder.release();
				mOnFinish.onFinish(newPath);
				
				this.dismiss();
			}
			break;

		default:
			break;
		}
	}
	
	OnFinish mOnFinish;
	
	public interface OnFinish{
		void onFinish(String path);
	}
	
	OnDismissListener missLis = new OnDismissListener() {
		
		@Override
		public void onDismiss() {
			if(isStart){
				if(isFinish){
					if(srcPath != null && !srcPath.equals("")){
						File file = new File(srcPath);
						file.delete();
					}
					
				}else{
					File file = new File(newPath);
					file.delete();
					recorder.stop();
					recorder.release();
				}
			}
			
			isStart = false;
			isFinish = false;
			mButton.setText("Â¼Òô");
		}
	};
	
	
	
}
