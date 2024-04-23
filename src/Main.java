import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws Exception{
		File homeworksFolder = new File("homeworks");
		File sourceFolder = new File("src");
		File[] homeworkFiles = homeworksFolder.listFiles();
		String[] filesNeed = { "Homework.java", "__MACOSX/._Homework.java" };
		
		for (File file : homeworkFiles) {
			if(!file.getName().endsWith(".zip")) {
				continue;
			}
			int result;
			String studentID = file.getName().replace(".zip", "");
			System.out.println(studentID);
			

			try {
				FileHelper.unzipFile(file.getAbsolutePath(), sourceFolder, filesNeed);
			} catch (IOException e) {
				result = 0;
				System.out.println("File is broken. std: " + studentID + " Result: " + result);
				continue;
			}
			
			Terminal.run(sourceFolder, "javac -d bin src/Homework.java");
			
			int[] grades = getTestResults();
			if(grades == null) {
				System.out.println("JUnit test file is broken.");
				result = 0;
				continue;
			}
			
			result = (int)calculatePoints(JUnitTests.MAX_POINTS, grades[0] + grades[1], grades[0]);
			
			System.out.println("StudentID: " + studentID + " Result: " + result);
			
			new File("src/Homework.java").delete();
			new File("bin/Homework.class").delete();
			
		}
	}
	
	// returns an integer array which includes successfull answers(0) and failed answers(1) count
	public static int[] getTestResults() {
		File location = new File(".");
		
		String compile_command = "";
		String run_command = "java -jar lib/junit-platform-console-standalone-1.10.2.jar execute -cp bin/ -c JUnitTests";
		
		// use ";" instead of ":" on windows
		if(Terminal.IS_WINDOWS) {
			compile_command = "javac -d bin -sourcepath src -cp .;lib/junit-platform-console-standalone-1.10.2.jar src/JUnitTests.java";
		}
		else {
			compile_command = "javac -d bin -sourcepath src -cp .:lib/junit-platform-console-standalone-1.10.2.jar src/JUnitTests.java";			
		}
		
		try {
			Terminal.run(location, compile_command);
			
			String output = Terminal.run(location, run_command);
			String[] outputs = output.split("\n");
			
			int successfull = Character.getNumericValue(outputs[outputs.length - 2].charAt(10));
			int failed = Character.getNumericValue(outputs[outputs.length - 1].charAt(10));
			
			return new int[] { successfull, failed };
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static double calculatePoints(int max_point, int num_of_questions, int correct_answers) {
		return (double)(correct_answers * max_point) / num_of_questions;
	}
}