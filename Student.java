/**
 * This is the Student class which is responsible for storing data about each
 * student on the roster.
 * Architecture:MVC-Model
 * @author Xiang Luo
 */
import java.util.LinkedHashMap;


public class Student {
	
	private String ID;
	private String fName;
	private String lName;
	private String program;
	private String level;
	private String ASURITE;
	private LinkedHashMap<String, Integer> times;

   /**
   * This is one of Student's constructors which allocates memory for storing
   * the attendance data of the student.
   */
	public Student()
	{
		this.times = new LinkedHashMap<String, Integer>();
	}

   /**
   * This is one of Student's constructors which initializes member variables
   * and allocates memory for storing the attendance data of the student.
   * @param ID the ID of the student.
   * @param fName the first name of the student.
   * @param lName the last name of the student.
   * @param program the program that the student is in.
   * @param level whether the student is undergraduate or graduate.
   * @param ASURITE the ASURITE of the student.
   */
	public Student(String ID, String fName, String lName, String program, String level, String ASURITE)
	{
		this.ID = ID;
		this.fName = fName;
		this.lName = lName;
		this.program = program;
		this.level = level;
		this.ASURITE = ASURITE;
		this.times = new LinkedHashMap<String, Integer>();
	}

   /**
   * This is the setID setter which sets the student's ID.
   * @param ID the ID of the student.
   */
	public void setID(String ID) {
		this.ID = ID;
	}

   /**
   * This is the setFirstName setter which sets the student's first name.
   * @param fName the first name of the student.
   */
	public void setFirstName(String fName) {
		this.fName = fName;
	}

   /**
   * This is the setLastName setter which sets the student's last name.
   * @param fName the last name of the student.
   */
	public void setLastName(String lName) {
		this.lName = lName;
	}

   /**
   * This is the setProgram setter which sets the student's program.
   * @param program the program the student is in.
   */
	public void setProgram(String program) {
		this.program = program;
	}

   /**
   * This is the setLevel setter which sets the student's level.
   * @param level the level the student is in.
   */
	public void setLevel(String level) {
		this.level = level;
	}

   /**
   * This is the setASURITE setter which sets the student's ASURITE.
   * @param ASURITE the ASURITE of the student.
   */
	public void setASURITE(String ASURITE) {
		this.ASURITE = ASURITE;
	}

   /**
   * This is the addAttendance method which adds attendance data.
   * @param date the date of attendance.
   * @param time the total number of minutes the student is connected.
   */
	public void addAttendance(String date, Integer time) {
		this.times.put(date, time);
	}

   /**
   * This is the updateAttendance method which updates attendance data.
   * @param date the date of attendance.
   * @param time the total number of minutes the student is connected.
   */
	public void updateAttendance(String date, Integer time) {
		this.times.put(date, time);
	}

   /**
   * This is the getID getter which gets the student's ID.
   * @return student's ID
   */
	public String getID() {
		return ID;
	}

   /**
   * This is the getFirstName getter which gets the student's first name.
   * @return student's first name.
   */
	public String getFirstName() {
		return fName;
	}

   /**
   * This is the getLastName getter which gets the student's last name.
   * @return student's last name.
   */
	public String getLastName() {
		return lName;
	}

   /**
   * This is the getProgram getter which gets the student's program.
   * @return student's program.
   */
	public String getProgram() {
		return program;
	}

   /**
   * This is the getLevel getter which gets the student's level.
   * @return student's level.
   */
	public String getLevel() {
		return level;
	}

   /**
   * This is the getASURITE getter which gets the student's ASURITE.
   * @return student's ASURITE.
   */
	public String getASURITE() {
		return ASURITE;
	}

   /**
   * This is the getAttendanceForDate method which gets the total time of 
   * attendance for the given date.
   * @return total time of attendance for the given date.
   */
	public Integer getAttendanceForDate(String date) {
		Integer time = times.get(date);
		if(time == null) {
			time = 0;
		}
		return time;
	}

}
