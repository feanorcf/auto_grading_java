import java.io.File;

public class Main {
	public static void main(String[] args) {
		int[] answers = getTestResults();
		
		int num_of_questions = answers[0] + answers[1];
		int correct_answers = answers[0];
		
		int result = (int)Math.round(calculatePoints(JUnitTests.MAX_POINTS, num_of_questions, correct_answers));
		
		System.out.println("Result: " + result + "/" + JUnitTests.MAX_POINTS);
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