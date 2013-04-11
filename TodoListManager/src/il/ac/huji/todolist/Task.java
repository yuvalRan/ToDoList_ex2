package il.ac.huji.todolist;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements ITodoItem{
	
	private static final String NO_DUE_DATE = "No due date";
	private String _theTask;
	private Date _dueDate;
	private String _strDate;
	
	public Task(String theTask, Date dueDate) {
		_theTask = theTask;
		if(dueDate != null){
			_dueDate = new Date(dueDate.getTime());
			_strDate = dateAsString(_dueDate);
		}
		else{
			_dueDate = null;
			_strDate = NO_DUE_DATE;
		}
	}

	public String getTask() {
		return _theTask;
	}

	public Date getDueDate() {
		return _dueDate;
	}

	public String getStrDate() {
		return _strDate;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String dateAsString(Date taskDate){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = df.format(taskDate);
		return dateStr;
	}

	public String getTitle() {
		return _theTask;
	}
}
