package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
	
	private static final int ADD_ITEM = 1;
	private static final String CALL_TASK_PREFIX = "Call ";
	private ArrayAdapter<Task> taskAdapter;
	private List<Task> allTasks;
	private ListView tasksList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		allTasks = new ArrayList<Task>();
		tasksList = (ListView)findViewById(R.id.lstTodoItems);
		taskAdapter = new TaskDisplayAdapter(this, allTasks);
		tasksList.setAdapter(taskAdapter);
		registerForContextMenu(tasksList);
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
	        	Intent addIntent = new Intent(this, AddNewTodoItemActivity.class);
	        	startActivityForResult(addIntent, ADD_ITEM);
	        	break;
	    }
	    return true;
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, tasksList, null);
		getMenuInflater().inflate(R.menu.task_context, menu);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
		String curTask = taskAdapter.getItem(info.position).getTask();
		menu.setHeaderTitle(curTask);
		if (curTask.startsWith(CALL_TASK_PREFIX)) {
			menu.findItem(R.id.menuItemCall).setTitle(curTask);
		}
		else{
			menu.removeItem(R.id.menuItemCall);
		}
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		Task selectedTask = taskAdapter.getItem(info.position);
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				taskAdapter.remove(selectedTask);
				return true;
			case R.id.menuItemCall:
				String phoneNumber = selectedTask.getTask().substring(CALL_TASK_PREFIX.length());
				Intent dial = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
				startActivity(dial);
			return true;
			}
		return false;
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case ADD_ITEM:
			if(resultCode == RESULT_OK){
				Task toAdd = new Task((String)data.getSerializableExtra(AddNewTodoItemActivity.FIRST_CONTENT), 
						((Date)data.getSerializableExtra(AddNewTodoItemActivity.SECOND_CONTENT)).getTime());
				taskAdapter.add(toAdd);
			}
			break;
		}
	}
}
	

