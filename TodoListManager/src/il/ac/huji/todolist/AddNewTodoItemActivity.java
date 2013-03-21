package il.ac.huji.todolist;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {
	public static final String FIRST_CONTENT = "title";
	public static final String SECOND_CONTENT = "dueDate";
	
	DatePicker dueDate;	
	Calendar calendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		
		final Button cancelButton = (Button)findViewById(R.id.btnCancel);
		final Button okButton = (Button)findViewById(R.id.btnOK);
		
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dueDate = (DatePicker)findViewById(R.id.datePicker);
				EditText taskTitle = (EditText)findViewById(R.id.edtNewItem); 
				calendar = Calendar.getInstance();
				calendar.set(dueDate.getYear(), dueDate.getMonth(), dueDate.getDayOfMonth());
				Intent taskIntent = new Intent();
				taskIntent.putExtra(FIRST_CONTENT, taskTitle.getText().toString());
				taskIntent.putExtra(SECOND_CONTENT, calendar.getTime());
				setResult(RESULT_OK, taskIntent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
		return true;
	}

}
