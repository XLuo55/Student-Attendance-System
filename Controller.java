/**
 * This is the Controller class which is responsible for performing actions
 * when an user interface element is clicked.
 * Architecture:MVC-Controller
 * @author Xiang Luo
 */
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JButton;


public class Controller implements ActionListener {

	private Main main;
	private Repository repository;

   /**
   * This is Controller's constructor which initializes member variables.
   */
	public Controller(Main main, Repository repository)
	{
		this.main = main;
		this.repository = repository;
	}

   /**
   * This is the addActionListener method which adds this controller as the
   * ActionListener to the given JMenuItem that performs action when activated.
   * @param item the JMenuItem.
   * @param action the action to perform when activated.
   */
	public void addActionListener(JMenuItem item, String action) {
		item.setActionCommand(action);
		item.addActionListener(this);
	}

   /**
   * This is the addJButtonActionListener method which adds this controller as
   * the ActionListener to the given JButton that performs action when 
   * activated.
   * @param button the JButton.
   * @param action the action to perform when activated.
   */
	public void addJButtonActionListener(JButton button, String action) {
		button.setActionCommand(action);
		button.addActionListener(this);
	}

   /**
   * This is the actionPerformed method which performs different actions 
   * depending on the source user interface element.
   * @param evt the ActionEvent.
   */
	public void actionPerformed(ActionEvent evt) {
		String action = evt.getActionCommand();
		if(action.equals("Roster")) {
			repository.loadRoster();
		} else if(action.equals("Add")) {
			repository.addAttendance();
		} else if(action.equals("Save")) {
			repository.save();
		} else if(action.equals("Plot")) {
			repository.plotData();
		} else if(action.equals("About")) {
			main.aboutDialog();
		}
	}

}
