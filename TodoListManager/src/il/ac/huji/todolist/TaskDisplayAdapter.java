package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TaskDisplayAdapter extends ArrayAdapter<Task> {

	public TaskDisplayAdapter(TodoListManagerActivity activity, List<Task> tasks) {
		super(activity, android.R.layout.simple_list_item_1, tasks);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Task curTask = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.task_row, null);
		TextView taskTitle = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView taskDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		taskTitle.setText(curTask.getTask());
		taskDate.setText(curTask.getStrDate());
		if (curTask.getDueDate() != null){			
			if(curTask.getDueDate().before(new Date())){
				taskTitle.setTextColor(Color.RED);	
				taskDate.setTextColor(Color.RED);
			}
		}
		
		return view;
	}
	
	
}
