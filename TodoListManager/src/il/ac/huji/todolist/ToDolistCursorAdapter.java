package il.ac.huji.todolist;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ToDolistCursorAdapter extends SimpleCursorAdapter{

	private Context _myContext;
	private Cursor _myCursor;
	
	public ToDolistCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		_myContext = context;
		_myCursor = c;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		Task curTask;
		
		try{
			curTask = new Task("", new Date(_myCursor.getLong(2)));
		}catch (Exception e) {
			curTask = new Task("", null);
		}
		
		TextView taskTitle = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView taskDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		
		taskDate.setText(curTask.getStrDate());
		if (curTask.getDueDate() != null){			
			if(curTask.getDueDate().before(new Date())){
				taskTitle.setTextColor(Color.RED);	
				taskDate.setTextColor(Color.RED);
			}
			else{
				taskTitle.setTextColor(Color.BLACK);	
				taskDate.setTextColor(Color.BLACK);
			}
		}
		return view;
	}
	
	public Task getItem(){
		return new Task(_myCursor.getString(1), new Date());
	}
}
