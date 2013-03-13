package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
	
	private ArrayAdapter<Task> taskAdapter;
	List<Task> allTasks;
	ListView tasksList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		allTasks = new ArrayList<Task>();
		tasksList = (ListView)findViewById(R.id.lstTodoItems);
		taskAdapter = new TaskDisplayAdapter(this, allTasks);
		tasksList.setAdapter(taskAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menuItemAdd:
	        	EditText newTask = (EditText)findViewById(R.id.edtNewItem);	
	        	taskAdapter.add(new Task(newTask.getText().toString()));
	        	break;
	        case R.id.menuItemDelete:
	        	allTasks.remove(tasksList.getSelectedItemPosition());
	        	taskAdapter.notifyDataSetChanged();
	        	break;
	    }
	    return true;
	}

}

