package il.ac.huji.todolist;


import java.util.Date;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
	
	private static final int ADD_ITEM = 1;
	private static final String CALL_TASK_PREFIX = "Call ";
	private ToDolistCursorAdapter todoListCursorAdapter;
	private ListView tasksList;
	private TodoDAL todoDALhelper; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		//allTasks = new ArrayList<Task>();
		todoDALhelper = new TodoDAL(this);
		tasksList = (ListView)findViewById(R.id.lstTodoItems);
		
		String[] from = {"title", "due"};
		int[] to = {R.id.txtTodoTitle, R.id.txtTodoDueDate};
		//Adapter between DB and ListView
		todoListCursorAdapter = new ToDolistCursorAdapter(this, R.layout.task_row, todoDALhelper.getCursor(), from, to);
		//bind DB and ListView
		tasksList.setAdapter(todoListCursorAdapter);
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
		String curTask = todoDALhelper.getCursor().getString(1);
		//set context menu header as the task title
		menu.setHeaderTitle(curTask);
		if (curTask.startsWith(CALL_TASK_PREFIX)) {
			menu.findItem(R.id.menuItemCall).setTitle(curTask);
		}
		else{
			//If not "Call" task remove that option.
			menu.removeItem(R.id.menuItemCall);
		}
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		String selectedTask = todoDALhelper.getCursor().getString(1);
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				//delete from DB and from Parse
				todoDALhelper.delete(new Task(selectedTask, new Date()));
				return true;
			case R.id.menuItemCall:
				//Open dialer
				String phoneNumber = selectedTask.substring(CALL_TASK_PREFIX.length());
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
						((Date)data.getSerializableExtra(AddNewTodoItemActivity.SECOND_CONTENT)));
				//Insert Task to DB and Parse
				todoDALhelper.insert(toAdd);
			}
			break;
		}
	}
}
	

