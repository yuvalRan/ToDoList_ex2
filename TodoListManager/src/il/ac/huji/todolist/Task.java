package il.ac.huji.todolist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
	
	private String _theTask;
	private Date _dueDate;
	private String _strDate;
	
	public Task(String theTask, long date) {
		_theTask = theTask;
		_dueDate = new Date(date);
		_strDate = dateAsString(_dueDate);
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
	
	public String dateAsString(Date taskDate){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = df.format(taskDate);
		return dateStr;
	}
}
