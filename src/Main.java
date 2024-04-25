import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws Exception {
		File sourceFolder = new File("src");
		String[] filesNeed = { "Homework.java" };

		String output = Terminal.run(new File("."), "python3 lib/unzipper.py");
		
		if(output.endsWith("BROKEN_FILE_EXCEPTION\n")) {
			throw new FileNotFoundException("Zip file cannot found or file is broken.");
		}

		File[] outputs = new File("homeworks/outputs").listFiles();

		for (File file : outputs) {
			if (file.getName().endsWith("__MACOSX") || file.isFile()) {
				continue;
			}

			String studentID = "";
			
			File[] studentFiles = file.listFiles();

			for (File studentFile : studentFiles) {
				if (studentFile.getName().equals(filesNeed[0])) {
					studentFile.renameTo(new File("junit_test_folder/Homework.java"));
					System.out.println(file.getName());
					studentID = file.getName();
				}
			}
			
			Terminal.run(sourceFolder, "javac junit_test_folder/Homework.java");
			
			int[] grades = getTestResults();
			if(grades == null) {
				System.out.println("JUnit test file is broken.");
				continue;
			}
			
			int result = (int)calculatePoints(100, grades[0] + grades[1], grades[0]);
			
			System.out.println("StudentID: " + studentID + " Result: " + result);
			
			new File("junit_test_folder/Homework.java").delete();
			new File("junit_test_folder/Homework.class").delete();
			
		}
		deleteDirectory(new File("homeworks/outputs/"));
	}
	
	private static boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}

	// returns an integer array which includes successfull answers(0) and failed
	// answers(1) count
	private static int[] getTestResults() {
		File location = new File(".");

		String compile_command = "";
		String run_command = "java -jar lib/junit-platform-console-standalone-1.10.2.jar execute -cp junit_test_folder/ -c JUnitTests";

		// use ";" instead of ":" on windows
		if (Terminal.IS_WINDOWS) {
			compile_command = "javac -d junit_test_folder -sourcepath junit_test_folder -cp .;lib/junit-platform-console-standalone-1.10.2.jar junit_test_folder/JUnitTests.java";
		} else {
			compile_command = "javac -d junit_test_folder -sourcepath junit_test_folder -cp .:lib/junit-platform-console-standalone-1.10.2.jar junit_test_folder/JUnitTests.java";
		}

		try {
			Terminal.run(location, compile_command);

			String output = Terminal.run(location, run_command);
			String[] outputs = output.split("\n");

			int successfull = Character.getNumericValue(outputs[outputs.length - 2].charAt(10));
			int failed = Character.getNumericValue(outputs[outputs.length - 1].charAt(10));

			return new int[] { successfull, failed };
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static double calculatePoints(int max_point, int num_of_questions, int correct_answers) {
		return (double) (correct_answers * max_point) / num_of_questions;
	}
}