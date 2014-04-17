package com.taotao.notebook.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicPlayService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	MediaPlayer player;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		player = new MediaPlayer();
	//	player.setDataSource(path);
		
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	
    	
    }
	
	

}
