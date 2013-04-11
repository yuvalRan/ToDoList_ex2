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
		//Creates DBHelper Object
		ActivityDatabase dbHelper = new ActivityDatabase(context);
		todo_db = dbHelper.getWritableDatabase();
		//get Cursor for the "todo" table with all data
		allDataCursor = todo_db.query(ActivityDatabase.TABLE_NAME, 
				new String[] {"_id", ActivityDatabase.COLUMN1, ActivityDatabase.COLUMN2 },
				null, null, null, null, null);
		allDataCursor.moveToFirst();
		//Parse initializing with keys
		Parse.initialize(context, context.getResources().getString(R.string.parseApplication), 
				context.getResources().getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();

	}
	@SuppressWarnings("deprecation")
	public boolean insert(ITodoItem todoItem) {
		//insert to PARSE:
		//Create Parse object and fill with the appropriate values 
		taskParse = new ParseObject(ActivityDatabase.TABLE_NAME);
		taskParse.put(ActivityDatabase.COLUMN1, todoItem.getTitle());
		Date date = todoItem.getDueDate();
		if (date != null){
			taskParse.put(ActivityDatabase.COLUMN2, todoItem.getDueDate().getTime());			
		}
		else{
			taskParse.put(ActivityDatabase.COLUMN2, JSONObject.NULL);
		}
		//commit insertion
		taskParse.saveInBackground(); 
		
		//local DB:
		//creates ContentValue object and fill with the appropriate values
		ContentValues task = new ContentValues();
		task.put(ActivityDatabase.COLUMN1, todoItem.getTitle());
		if (date != null){
			task.put(ActivityDatabase.COLUMN2, todoItem.getDueDate().getTime());			
		}
		else{
			task.putNull(ActivityDatabase.COLUMN2);
		}
		//insert to DB ("todo" table)
		long returnVal = todo_db.insert(ActivityDatabase.TABLE_NAME, null, task);
		//update cursor --> ListView
		allDataCursor.requery();
		if (returnVal == -1) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean update(ITodoItem todoItem) {
		//new Date to update
		final Date date = todoItem.getDueDate();
		//PARSE part
		//ask Parse DB for this ITodoItem in order to update it's due.
		//create ParseQuery object and ask for tuples that their "title" field is like
		//the title of the updated ITodoItem because only due is valid to update.
		ParseQuery query = new ParseQuery(ActivityDatabase.TABLE_NAME);
		query.whereEqualTo(ActivityDatabase.COLUMN1, todoItem.getTitle());
		//run query
		query.findInBackground(new FindCallback() 
			{
				//callback function when query returns
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
		//update DB (the tuple his "title" column equal to the given ITodoItem title.
		int returnVal = todo_db.update(ActivityDatabase.TABLE_NAME, task, 
				ActivityDatabase.COLUMN1 + " = ?", new String[]{todoItem.getTitle()}); 
		//update cursor --> ListView
		allDataCursor.requery();
		if (returnVal != 1) {
			return false;
		}
		return true;
		
	}
	@SuppressWarnings("deprecation")
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
		//update cursor --> ListView
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
		Task task;
		if (allCursor.moveToFirst()) {
			do {
				if(allCursor.isNull(1)){
					task = new Task(allCursor.getString(0), null);
				}
				else{
					task = new Task(allCursor.getString(0), new Date(allCursor.getLong(1)));					
				}
				activitiesList.add(task);
			} while (allCursor.moveToNext());
		} 
		return activitiesList;	
	}
	
	public Cursor getCursor(){
		return allDataCursor;
	}
	
	public void testAll(){
		List<ITodoItem> test = all();
		for(int i=0;i<test.size();i++){
			System.out.print("Item" + i + ": ");
			System.out.print(test.get(i).getTitle());
			if(test.get(i).getDueDate() == null){
				System.out.print(" null");
			}
			else{
				System.out.print(test.get(i).getDueDate().toString());				
			}
			System.out.println();
		}
	}
}
