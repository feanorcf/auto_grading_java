import java.io.File;

public class Main {
	static final int MAX_POINTS = 100;
	
	public static void main(String[] args) {
		int[] answers = getTestResults();
		
		int num_of_questions = answers[0] + answers[1];
		int correct_answers = answers[0];
		
		double result = calculatePoints(MAX_POINTS, num_of_questions, correct_answers);
		
		System.out.println(result + "/" + MAX_POINTS);
	}
	
	public static double calculatePoints(int max_point, int num_of_questions, int correct_answers) {
		double result = (double)(correct_answers * max_point) / num_of_questions;
		
		return result;
	}
	
	// returns an integer array which includes successfull answers(0) and failed answers(1) count
	public static int[] getTestResults() {
		File location = new File(".");
		String command = "java -jar lib/junit-platform-console-standalone-1.10.2.jar execute -cp bin/ -c CalculatorTest";
		
		try {
			String output = Terminal.run(location, command);
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
	
	
}
