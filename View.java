/**
 * This is the View class. It is responsible for creating the JTable and the
 * JScrollPane. It also responds to notifications from the Repository class.
 * Architecture:MVC-View
 * @author Xiang Luo
 */
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


public class View extends JPanel implements Observer {

	private Repository repository;
	private Main main;

	private JScrollPane scrollPane;
	private JTable table;

        /**
	* This is View's constructor. It creates a new JTable and JScrollPane.
	* @param main An instance of the Main class.
	* @param repository An instance of the Repository class.
	*/
	public View(Main main, Repository repository)
	{
		this.main = main;
		this.repository = repository;

		DefaultTableModel model = generateModel();
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		initTable();
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(560, 500));
		setLayout(new GridLayout(1, 1));
		add(scrollPane);
	}

        /**
	* This is the generateModel method. It generates a new DefaultTableModel
	* that is initialized with the headers of the roster file.
	* @return a DefaultTableModel that can be set as the model in a JTable.
	*/
	public DefaultTableModel generateModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Program");
		model.addColumn("Level");
		model.addColumn("ASURITE");
		return model;
	}

        /**
	* This is the initTable method. It sets the widths of the columns in the
	* JTable.
	*/
	public void initTable() {
		TableColumnModel colModel = table.getColumnModel();
		colModel.getColumn(0).setPreferredWidth(100);
		colModel.getColumn(1).setPreferredWidth(100);
		colModel.getColumn(2).setPreferredWidth(100);
		colModel.getColumn(3).setPreferredWidth(150);
		colModel.getColumn(4).setPreferredWidth(150);
		colModel.getColumn(5).setPreferredWidth(100);
	}

        /**
	* This is the getTableModel method. It gets the model of the JTable.
	* @return the model of the JTable.
	*/
	public DefaultTableModel getTableModel() {
		return (DefaultTableModel)table.getModel();
	}

        /**
	* This is the update method. It updates the JTable when the View class
	* receives a notification from the Repository class.
	* @param o an Observable object.
	* @param repository an instance of the Repository class.
	*/
	@Override
	public void update(Observable o, Object repository) {
		this.repository = (Repository)repository;
		DefaultTableModel model = generateModel();
		ArrayList<Student> students = this.repository.getStudents();
		ArrayList<ArrayList<String>> attendeesLists = this.repository.getAttendeesLists();
		ArrayList<ArrayList<Integer>> attendeesTimesLists = this.repository.getAttendeesTimesLists();
		ArrayList<String> dates = this.repository.getDates();
		if(!this.repository.tableIsAdded() && this.repository.rosterIsLoaded()) {
			main.showTable();
			this.repository.setTableAdded();
		}
		if(attendeesLists.isEmpty()) {
			for(int i = 0; i < students.size(); i++) {
				Student student = students.get(i);
				model.addRow(new Object[]{student.getID(), student.getFirstName(), student.getLastName(), student.getProgram(), student.getLevel(), student.getASURITE()});
			}
			table.setModel(model);
			initTable();
		} else {
			for(int i = 0; i < dates.size(); i++) {
				model.addColumn(dates.get(i));
			}
			for(int i = 0; i < students.size(); i++) {
				Student student = students.get(i);
				Object[] row = new Object[6 + dates.size()];
				row[0] = student.getID();
				row[1] = student.getFirstName();
				row[2] = student.getLastName();
				row[3] = student.getProgram();
				row[4] = student.getLevel();
				row[5] = student.getASURITE();
				for(int j = 0; j < dates.size(); j++) {
					row[6 + j] = "" + student.getAttendanceForDate(dates.get(j));
				}
				model.addRow(row);
			}
			table.setModel(model);
			initTable();
			main.attendanceDialog();
		}
	}

}
