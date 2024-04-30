import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Report {
	private String studentId;
	private int grade;
	private File file;
	private String content;
	
	public Report(String id, File file) {
		this.studentId = id;
		this.file = file;
		content = "Report: " + studentId + "\n";
	}
	
	public void addLine(String line) {
		content += line + "\n";
	}
	
	public String getId() {
		return this.studentId;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getGrade() {
		return this.grade;
	}
	
	public String getContent() {
		return content;
	}
	
	public void flushReport() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.write(content);
		writer.close();
	}
	
}