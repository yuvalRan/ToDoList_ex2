package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoDAL {
	private SQLiteDatabase todo_db;
	private Cursor allDataCursor;
	ParseObject taskParse; 
	
	public TodoDAL(Context context) {
		ActivityDatabase dbHelper = new ActivityDatabase(context);
		todo_db = dbHelper.getWritableDatabase();
		allDataCursor = todo_db.query(ActivityDatabase.TABLE_NAME, 
				new String[] {"_id", ActivityDatabase.COLUMN1, ActivityDatabase.COLUMN2 },
				null, null, null, null, null);
		allDataCursor.moveToFirst();
		Parse.initialize(context, context.getResources().getString(R.string.parseApplication), 
				context.getResources().getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();

	}
	public boolean insert(ITodoItem todoItem) {
		//PARSE
		taskParse = new ParseObject(ActivityDatabase.TABLE_NAME);
		taskParse.put(ActivityDatabase.COLUMN1, todoItem.getTitle());
		Date date = todoItem.getDueDate();
		if (date != null){
			taskParse.put(ActivityDatabase.COLUMN2, todoItem.getDueDate().getTime());			
		}
		else{
			taskParse.put(ActivityDatabase.COLUMN2, JSONObject.NULL);
		}
		taskParse.saveInBackground(); 
		
		//local DB
		ContentValues task = new ContentValues();
		task.put(ActivityDatabase.COLUMN1, todoItem.getTitle());
		if (date != null){
			task.put(ActivityDatabase.COLUMN2, todoItem.getDueDate().getTime());			
		}
		else{
			task.putNull(ActivityDatabase.COLUMN2);
		}
		long returnVal = todo_db.insert(ActivityDatabase.TABLE_NAME, null, task);
		allDataCursor.requery();
		if (returnVal == -1) {
			return false;
		}
		return true;
	}
	
	public boolean update(ITodoItem todoItem) {
		//PARSE part
		final Date date = todoItem.getDueDate();
		final ITodoItem finalItem = todoItem;
		ParseQuery query = new ParseQuery(ActivityDatabase.TABLE_NAME);
		query.whereEqualTo(ActivityDatabase.COLUMN1, todoItem.getTitle());
		query.findInBackground(new FindCallback() 
			{
				public void done(List<ParseObject> matches, ParseException e) {
					if(e == null){
						if(!matches.isEmpty()){
							if(date != null){
								matches.get(0).put(ActivityDatabase.COLUMN2, date.getTime());															
							}
							else{
								matches.get(0).put(ActivityDatabase.COLUMN2, JSONObject.NULL);
							}
							matches.get(0).saveInBackground();
						}
					}
					else
						return;	
				}
			}); 
		
		//local DB
		ContentValues task = new ContentValues();
		if (date != null){
			task.put(ActivityDatabase.COLUMN2, date.getTime());			
		}
		else{
			task.putNull(ActivityDatabase.COLUMN2);
		}
		int returnVal = todo_db.update(ActivityDatabase.TABLE_NAME, task, 
				ActivityDatabase.COLUMN1 + " = ?", new String[]{todoItem.getTitle()}); 
		allDataCursor.requery();
		if (returnVal != 1) {
			return false;
		}
		return true;
		
	}
	public boolean delete(ITodoItem todoItem) {
		//DELETE from PARSE
		ParseQuery query = new ParseQuery(ActivityDatabase.TABLE_NAME);
		query.whereEqualTo(ActivityDatabase.COLUMN1, todoItem.getTitle());
		query.findInBackground(new FindCallback() 
			{
				public void done(List<ParseObject> matches, ParseException e) {
					if(e != null){
						return;
					}
					else{
						if(!matches.isEmpty()){
							matches.get(0).deleteInBackground();						
						}
					}
				}
			}); 
		
		//DELETE from DB
		int returnVal = todo_db.delete(ActivityDatabase.TABLE_NAME, 
				ActivityDatabase.COLUMN1 + " = ?", new String[] {todoItem.getTitle() });
		allDataCursor.requery();
		if (returnVal != 1) {
			return false;
		}
		return true;
	}
	
	public List<ITodoItem> all() {
		List<ITodoItem> activitiesList = new ArrayList<ITodoItem>();
		Cursor allCursor;
		allCursor = todo_db.query(ActivityDatabase.TABLE_NAME, 
				new String[] {ActivityDatabase.COLUMN1, ActivityDatabase.COLUMN2},
		null, null, null, null, null);
		long date;
		String title;
		if (allCursor.moveToFirst()) {
			do {
				title = allCursor.getString(0);
				Task task = new Task(title, null);
				try{
					date = allCursor.getLong(1);
					task = new Task(title, new Date(date));
				}catch (Exception e) {
					
				}
				activitiesList.add(task);
			} while (allCursor.moveToNext());
		} 
		return activitiesList;	
	}
	
	public Cursor getCursor(){
		return allDataCursor;
	}
}
