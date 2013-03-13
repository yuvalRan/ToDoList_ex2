package il.ac.huji.todolist;

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
		TextView toDo = (TextView)view.findViewById(R.id.taskText);
		toDo.setText(curTask.getTask());
		if(position % 2 == 0){
			toDo.setTextColor(Color.RED);			
		}
		if(position % 2 == 1){
			toDo.setTextColor(Color.BLUE);			
		}
		return view;
	}
	
	
}
