package com.taotao.notebook.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.taotao.notebook.enity.Note;

public class MySQLiteHelp extends SQLiteOpenHelper {
	
	private MySQLiteHelp(Context context){
		super(context, "note.db", null, 1);
	}
	
	private static MySQLiteHelp instance;
	public static MySQLiteHelp getSQLieHelp(Context context){
		if(instance == null)
			instance = new MySQLiteHelp(context);
		return instance;
	}
	
	

	public MySQLiteHelp(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
    
	
	public static final String TABLE_NOTE_LIST = "note_list";
	public static final String ROW_NOTE_CONTENT = "content";
	public static final String ROW_NOTE_AUDIO = "audio";
	public static final String ROW_NOTE_IMG = "img";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTE_LIST + " (" + "_id" + " INTEGER PRIMARY KEY," + 
	            ROW_NOTE_CONTENT + " VARCHAR," + 
				ROW_NOTE_AUDIO + " VARCHAR,"  + 
	            ROW_NOTE_IMG + " VARCHAR);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE_LIST);
	}
	
	
	public static void addNote(Note note, Context context){
		SQLiteDatabase db = getSQLieHelp(context).getWritableDatabase();
		ContentValues values = new ContentValues(1);
		values.put(ROW_NOTE_AUDIO, note.audio);
		values.put(ROW_NOTE_CONTENT, note.content);
		values.put(ROW_NOTE_IMG, note.img);
		db.insert(TABLE_NOTE_LIST, null, values);
		db.close();
	}
	
	public static List<Note> findAllNotes(Context context){
		SQLiteDatabase db = getSQLieHelp(context).getReadableDatabase();
		Cursor c = db.query(TABLE_NOTE_LIST, null, null, null, null, null, null);
		if(c.getColumnCount() > 0){
			List<Note> list = new ArrayList<Note>(c.getColumnCount());
			while(c.moveToNext()){
				Note note = new Note();
				note.audio = c.getString(c.getColumnIndex(ROW_NOTE_AUDIO));
				note.content = c.getString(c.getColumnIndex(ROW_NOTE_CONTENT));
				note.img = c.getString(c.getColumnIndex(ROW_NOTE_IMG));
				note._id = c.getInt(0);
				list.add(note);
			}
			c.close();
			db.close();
			return list;
		}else{
			db.close();
			return new ArrayList<Note>();
		}
		
	}
	
	
	public static void deleteNote(int _id, Context context){
		SQLiteDatabase db = getSQLieHelp(context).getWritableDatabase();
		db.delete(TABLE_NOTE_LIST, "_id=" + _id, null);
		db.close();
	}
	
	public static void updateNote(Note note, Context context){
		SQLiteDatabase db = getSQLieHelp(context).getWritableDatabase();
		ContentValues values = new ContentValues(1);
		values.put(ROW_NOTE_AUDIO, note.audio);
		values.put(ROW_NOTE_CONTENT, note.content);
		values.put(ROW_NOTE_IMG, note.img);
		db.update(TABLE_NOTE_LIST, values, "_id=" + note._id, null);
		db.close();		
	}
	
	
	
	
	
	
	
	
	

}
