/**
 * This is the Main class. It is responsible for creating the user interface of
 * the program and initializing the controller and the model.
 * Architecture:MVC-View
 * @author Xiang Luo
 * @author Zuoan He
 */
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.*;


public class Main extends JFrame {

	private Repository repository;
	private View view;
	private Controller controller;

	private JMenuBar menuBar;

	private JMenu file;
	private JButton about;

	private JMenuItem roster;
	private JMenuItem add;
	private JMenuItem save;
	private JMenuItem plot;
	
   /**
   * This is Main's constructor which initializes the model, the controller,
   * and the view, initializes user interface elements and calls the
   * controller's addActionListener method to add action listeners to
   * the file JMenuItems and the about JButton.
   */
	public Main()
	{
		repository = new Repository(this);
		view = new View(this, repository);
		controller = new Controller(this, repository);

		repository.setView(view);
		repository.addObserver(view);
		
		menuBar = new JMenuBar();

		setJMenuBar(menuBar);
		
		file = new JMenu("File");
		about = new JButton("About");

		about.setOpaque(true);
		about.setContentAreaFilled(false);
		about.setBorderPainted(false);
		about.setFocusable(false);
		
		menuBar.add(file);
		menuBar.add(about);
		
		roster = new JMenuItem("Load a Roster");
		add = new JMenuItem("Add Attendance");
		save = new JMenuItem("Save");
		plot = new JMenuItem("Plot Data");
		
		JMenuItem[] items = {roster, add, save, plot};
		
		for(int i = 0; i < items.length; i++)
		{
			file.add(items[i]);
			if(i < items.length - 1)
			{
				file.addSeparator();
			}
		}
		
		controller.addActionListener(roster, "Roster");
		controller.addActionListener(add, "Add");
		controller.addActionListener(save, "Save");
		controller.addActionListener(plot, "Plot");
		controller.addJButtonActionListener(about, "About");

	}

   /**
   * This is the showTable method which adds the View JPanel that contains a
   * table to the JFrame.
   */
	public void showTable() {
		add(view);
		revalidate();
	}

   /**
   * This is the fileChooser method which creates a new JFileChooser, sets its
   * working directory to the current directory, and sets its file filter to
   * allow only CSV files.
   * @return A JFileChooser that is ready to open or save files.
   */
	public JFileChooser fileChooser() {
		JFileChooser fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
		fc.setCurrentDirectory(workingDirectory);
		fc.setFileFilter(filter);
		return fc;
	}

   /**
   * This is the openFile method which opens a JFileChooser open dialog. It
   * returns the JFileChooser if the user clicks the "OK" button on the dialog,
   * and returns null otherwise.
   * @return A JFileChooser with a file that the user selected.
   */
	public JFileChooser openFile() {
		JFileChooser fc = fileChooser();
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			return fc;
		}
		return null;
	}

   /**
   * This is the saveFile method which opens a JFileChooser save dialog. It
   * returns the JFileChooser if the user clicks the "OK" button on the dialog,
   * and returns null otherwise.
   * @return A JFileChooser with a file name that the user selected or entered.
   */
	public JFileChooser saveFile() {
		JFileChooser fc = fileChooser();
		if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			return fc;
		}
		return null;
	}

   /**
   * This is the showErrorMessage method which creates a JOptionPane dialog
   * that displays an error messsage.
   * @param message The error message to show in the JOptionPane.
   */
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

   /**
   * This is the attendanceDialog method which creates a new JDialog that
   * displays information about the loaded attendance file (number of people
   * found in roster, number of additional attendee(s), and how long additional
   * attendees connected).
   */
	public void attendanceDialog() {
		JDialog dialog = new JDialog(this, "Attendance added", true);
		JLabel text = new JLabel(repository.getAttendanceLoadedMessage());
		text.setHorizontalAlignment(SwingConstants.CENTER);
		dialog.add(text);
		dialog.setSize(350, 175);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

   /**
   * This is the plotDialog method which creates a new JDialog that displays
   * a scatter plot of the attendance data. It uses the JFreeChart library
   * to generate a scatter plot.
   * @param seriesCollection The x and y values of the scatter plot.
   */
	public void plotDialog(XYSeriesCollection seriesCollection) {
		JDialog dialog = new JDialog(this, "Plot", true);
		XYDataset dataset = seriesCollection;
		JFreeChart chart = ChartFactory.createScatterPlot("", "Percentage of Attendance", "Count", dataset);
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 228, 196));
		ChartPanel panel = new ChartPanel(chart);
		dialog.add(panel);
		dialog.setSize(600, 300);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

   /**
   * This is the aboutDialog method which creates a new JDialog that displays
   * information about our team.
   */
	public void aboutDialog() {
		JDialog dialog = new JDialog(this, "About", true);
		JLabel text = new JLabel("<html><p style='font-size: 1.25em;text-align: center;'>Team Developers:<br><br>Zuoan He,<br>Xiang Luo</p></html>");
		text.setHorizontalAlignment(SwingConstants.CENTER);
		dialog.add(text);
		dialog.setSize(350, 175);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
   /**
   * This is the main method which instantiates a new instance of Main and sets
   * up the JFrame.
   * @param args Unused.
   */
	public static void main(String[] args) {
		Main frame = new Main();
		frame.setTitle("CSE360 Final Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 700);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
