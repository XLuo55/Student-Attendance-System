/**
 * This is the Repository class which is responsible for storing all the data
 * that is used in the program and performing the four main functionalities
 * (load roster, add attendance, save to CSV file, and plot the data).
 * Architecture:MVC-Model
 * @author Xiang Luo
 */
import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.filechooser.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import org.jdatepicker.impl.*;
import org.jfree.data.xy.*;

/**
* This is the DateLabelFormatter class which is responsible for formatting
* the date that the user picks using JDatePicker.
*/
class DateLabelFormatter extends AbstractFormatter {
	private String datePattern = "MMM dd";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

   /**
   * This is the stringToValue method which creates a Calendar object from a
   * String representation of the object.
   * @param text the string representation of the Calendar object.
   * @return a Calendar object that is parsed from the given text.
   */
	@Override
	public Object stringToValue(String text) throws ParseException {
		return dateFormatter.parseObject(text);
	}

   /**
   * This is the valueToString method which creates a formatted String from
   * a Calendar object.
   * @param value a Calendar object to convert to String.
   * @return a formatted String that represents the Calendar object.
   */
	@Override
	public String valueToString(Object value) throws ParseException {
		if (value != null) {
			Calendar cal = (Calendar) value;
			return dateFormatter.format(cal.getTime());
		}
		return "";
	}
}


public class Repository extends Observable {

	private Main main;
	private View view;

	private ArrayList<String> dates;
	private ArrayList<Student> students;
	private ArrayList<ArrayList<String>> attendeesLists;
	private ArrayList<ArrayList<Integer>> attendeesTimesLists;
	private boolean rosterLoaded;
	private boolean attendanceAdded;
	private boolean tableAdded;
	private int studentsCount;
	private int additionalCount;
	private String loadedMessage;
	private String additionalMessage;
	private String additionalDetail;

   /**
   * This is Repository's constructor which allocates memory for the ArrayLists
   * and initializes member variables.
   * @param main an instance of the Main class.
   */
	public Repository(Main main)
	{
		this.main = main;
		dates = new ArrayList<String>();
		students = new ArrayList<Student>();
		attendeesLists = new ArrayList<ArrayList<String>>();
		attendeesTimesLists = new ArrayList<ArrayList<Integer>>();
		rosterLoaded = false;
		attendanceAdded = false;
		tableAdded = false;
	}

   /**
   * This is the setView method which assigns the given object to the view
   * field.
   * @param view an instance of the View class.
   */
	public void setView(View view) {
		this.view = view;
	}

   /**
   * This is the getDates getter which gets the dates ArrayList.
   * @return the dates ArrayList.
   */
	public ArrayList<String> getDates() {
		return dates;
	}

   /**
   * This is the getStudents getter which gets the students ArrayList.
   * @return the students ArrayList.
   */
	public ArrayList<Student> getStudents() {
		return students;
	}

   /**
   * This is the getAttendeesLists getter which gets the attendeesLists
   * ArrayList.
   * @return the attendeesLists ArrayList.
   */
	public ArrayList<ArrayList<String>> getAttendeesLists() {
		return attendeesLists;
	}

   /**
   * This is the getAttendeesTimesLists getter which gets the
   * attendeesTimesLists ArrayList.
   * @return the attendeesTimesLists ArrayList.
   */
	public ArrayList<ArrayList<Integer>> getAttendeesTimesLists() {
		return attendeesTimesLists;
	}

   /**
   * This is the getAttendanceLoadedMessage method which creates and returns
   * the message that is shown to the user after attendance file is loaded.
   * @return the message that is shown to the user after attendance file is
   *		 loaded.
   */
	public String getAttendanceLoadedMessage() {
		String message = "<html>" + loadedMessage + "<br><br>" + additionalMessage + "<br><br>" + additionalDetail + "</html>";
		return message;
	}

   /**
   * This is the rosterIsLoaded getter which gets the boolean rosterLoaded.
   * @return a boolean which indicates whether a roster file is loaded.
   */
	public boolean rosterIsLoaded() {
		return rosterLoaded;
	}

   /**
   * This is the tableIsAdded getter which gets the boolean tableAdded.
   * @return a boolean which indicates whether the JTable is added to the main
   *		 JFrame.
   */
	public boolean tableIsAdded() {
		return tableAdded;
	}

   /**
   * This is the setTableAdded method which sets the boolean tableAdded to true.
   */
	public void setTableAdded() {
		tableAdded = true;
	}

   /**
   * This is the loadRoster method which asks the user to select a file to open
   * as the roster file and opens the file.
   */
	public void loadRoster() {
		JFileChooser fc = main.openFile();
		if(fc != null) {
			File file = fc.getSelectedFile();
			openRosterFile(file);
		}
	}

   /**
   * This is the addAttendance method which asks the user to select a file to
   * open as an attendance file and opens the file. If the user has not loaded
   * a roster file first, it shows an error message in a JOptionPane instead.
   */
	public void addAttendance() {
		if(rosterIsLoaded()) {
			JFileChooser fc = main.openFile();
			if(fc != null) {
				File file = fc.getSelectedFile();
				openAttendanceFile(file);
			}
		} else {
			main.showErrorMessage("Roster file must to be loaded before adding attendance");
		}
	}

   /**
   * This is the save method which asks the user to enter a file name to save
   * the contents of the JTable to and saves the file with that file name. If
   * the user has not loaded at least one attendance file first, it shows an
   * error message in a JOptionPane instead.
   */
	public void save() {
		if(attendanceAdded) {
			JFileChooser fc = main.saveFile();
			if(fc != null) {
				File file = fc.getSelectedFile();
				saveToCSV(file);
			}
		} else {
			main.showErrorMessage("Both roster file and attendance file(s) must be loaded before saving");
		}
	}

   /**
   * This is the plotData method which calls the plotDialog method of main to
   * display a JDialog with a scatter plot in it. If the user has not loaded
   * at least one attendance file first, it shows an error message in a
   * JOptionPane instead.
   */
	public void plotData() {
		if(attendanceAdded) {
			main.plotDialog(createDataset());		
		} else {
			main.showErrorMessage("Both roster file and attendance file(s) must be loaded before plotting");
		}
	}

   /**
   * This is the createDataset method which creates a XYSeriesCollection from
   * the attendance data.
   * @return the generated dataset.
   */
	public XYSeriesCollection createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		for(int i = 0; i < dates.size(); i++) {
			XYSeries series = new XYSeries(dates.get(i));
			int[][] data = getSeries(dates.get(i));
			for(int j = 0; j < 11; j++) {
				series.add(data[j][0], data[j][1]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

   /**
   * This is the openRosterFile method which opens the given file as the roster
   * file, parses the file, and stores the data into the students ArrayList. It
   * also notifies the View class that new data was loaded.
   * @param file the file to open as the roster file.
   */
	private void openRosterFile(File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String[] info;
			boolean valid = true;
			students.clear();
			loadedMessage = "";
			additionalMessage = "";
			additionalDetail = "";
			attendeesLists.clear();
			attendeesTimesLists.clear();
			while((line = br.readLine()) != null) {
				info = line.split(",");
				if(info.length != 6) {
					valid = false;
					break;
				}
				boolean exist = false;
				for(int i = 0; !exist && i < students.size(); i++) {
					if(students.get(i).getASURITE().equals(info[5])) {
						exist = true;
					}
				}
				if(!exist) {
					Student student = new Student(info[0], info[1], info[2], info[3], info[4], info[5]);
					students.add(student);
				}
			}
			if(!valid) {
				main.showErrorMessage("Not a valid roster file, please try again");
				return;
			}
			rosterLoaded = true;
			br.close();

			setChanged();
			notifyObservers(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

   /**
   * This is the openAttendanceFile method which asks the user for the date of
   * the attendance file, opens the given file as the attendance file, parses
   * the file, stores the data into the attendees and the attendeesTimes
   * ArrayLists, and generate a report about the loaded attendance file. It
   * also notifies the View class that new data was loaded.
   * @param file the file to open as the attendance file.
   */
	private void openAttendanceFile(File file) {
		try {
			String selectedDate = "";
			boolean validDate = false;
			while(!validDate) {
				UtilDateModel model = new UtilDateModel();
				Properties p = new Properties();
				p.put("text.today", "Today");
				p.put("text.month", "Month");
				p.put("text.year", "Year");
				JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
				JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
				int result = JOptionPane.showConfirmDialog(null, datePicker, "Choose a date", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION) {
					selectedDate = (String)datePicker.getJFormattedTextField().getText();
					if(selectedDate.length() == 0) {
						main.showErrorMessage("You have not entered a date, please try again");
						continue;
					}
					else if(dates.contains(selectedDate)) {
						main.showErrorMessage("You have already selected this date for another attendance file, please try again");
						continue;
					}
					validDate = true;
					dates.add(selectedDate);
				}
				else {
					return;
				}
			}

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String[] info;
			ArrayList<String> attendees = new ArrayList<String>();
			ArrayList<Integer> attendeesTimes = new ArrayList<Integer>();
			boolean valid = true;
			studentsCount = 0;
			additionalCount = 0;
			loadedMessage = "";
			additionalMessage = "";
			additionalDetail = "";
			while((line = br.readLine()) != null) {
				info = line.split(",");
				if(info.length != 2) {
					valid = false;
					break;
				}
				String ASURITE = info[0];
				Integer time = Integer.parseInt(info[1]);
				if(attendees.contains(ASURITE)) {
					int index = attendees.indexOf(ASURITE);
					Integer newTime = attendeesTimes.get(index) + time;
					attendeesTimes.set(index, newTime);
					boolean found = false;
					for(int i = 0; !found && i < students.size(); i++) {
						Student student = students.get(i);
						String rosterASURITE = student.getASURITE();
						if(ASURITE.equals(rosterASURITE)) {
							student.updateAttendance(selectedDate, newTime);
							found = true;
						}
					}
				} else {
					attendees.add(ASURITE);
					attendeesTimes.add(time);
					boolean found = false;
					for(int i = 0; !found && i < students.size(); i++) {
						Student student = students.get(i);
						String rosterASURITE = student.getASURITE();
						if(ASURITE.equals(rosterASURITE)) {
							student.addAttendance(selectedDate, time);
							studentsCount++;
							found = true;
						}
					}
					if(!found) {
						additionalCount++;
						String s = "";
						if(time > 1) {
							s = "s";
						}
						additionalDetail += ASURITE + ", connected for " + time + " minute" + s + "<br>";
					}
				}
			}
			if(!valid) {
				main.showErrorMessage("Not a valid attendance file, please try again");
				return;
			}
			attendanceAdded = true;
			attendeesLists.add(attendees);
			attendeesTimesLists.add(attendeesTimes);
			String s = " ";
			String was = "was";
			if(additionalCount > 1) {
				s = "s ";
				was = "were";
			}
			additionalMessage = "" + additionalCount + " additional attendee" + s + was + " found:";
			s = "";
			if(studentsCount > 1) {
				s = "s";
			}
			loadedMessage = "Data loaded for " + studentsCount + " user" + s + " in the roster.";
			br.close();

			setChanged();
			notifyObservers(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

   /**
   * This is the saveToCSV method which saves the contents of the JTable in a
   * CSV file with a file name set by the user.
   * @param file the file name to save the data in.
   */
	private void saveToCSV(File file) {
		try {
			String filename = file.getPath();
			if(!filename.toLowerCase().endsWith(".csv")) {
				filename += ".csv";
			}
			FileWriter fw = new FileWriter(filename);
			DefaultTableModel model = view.getTableModel();
			fw.write("ID,First Name,Last Name,Program,Level,ASURITE");
			for(int i = 6; i < model.getColumnCount(); i++) {
				fw.write("," + model.getColumnName(i));
			}
			fw.write("\n");
			for(int i = 0; i < model.getRowCount(); i++) {
				for(int j = 0; j < model.getColumnCount(); j++) {
					fw.write(model.getValueAt(i, j).toString());
					if(j < model.getColumnCount() - 1) {
						fw.write(",");
					}
				}
				if(i < model.getRowCount() - 1) {
					fw.write("\n");
				}
			}
			fw.flush();
			fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

   /**
   * This is the getSeries method which generate a series of x and y value pairs
   * that is used to plot the attendance data. It calculates percentages of
   * attendance and counts how many students are in each percentage of
   * attendance.
   * @param date the date of attendance.
   */
	private int[][] getSeries(String date) {
		int[][] series = new int[11][2];
		for(int i = 0; i < 11; i++) {
			int x = i * 10;
			series[i][0] = x;
			series[i][1] = 0;
		}
		for(int i = 0; i < students.size(); i++) {
			Integer time = students.get(i).getAttendanceForDate(date);
			int percentage = (int)(time.doubleValue() / 75.0 * 100.0);
			if(percentage > 100) {
				percentage = 100;
			}
			percentage -= percentage % 10;
			series[percentage / 10][1]++;
		}
		return series;
	}

}
