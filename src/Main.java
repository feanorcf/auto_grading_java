import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws Exception {
		File sourceFolder = new File("src");
		String[] filesNeed = { "Homework.java" };

		String output = Terminal.run(new File("."), "python3 lib/zip_helper.py 0");
		
		if(output.endsWith("BROKEN_FILE_EXCEPTION\n")) {
			throw new FileNotFoundException("Zip file cannot found or file is broken.");
		}

		File[] outputs = new File("homeworks/outputs").listFiles();

		for (File file : outputs) {
			// for remove unwanted folders
			if (file.getName().endsWith("__MACOSX") || file.getName().endsWith(".DS_Store")) {
				deleteDirectory(file);
				continue;
			}
			File[] studentFiles = file.listFiles();
			
			System.out.println(file.getName());
			
			// for initialize student report
			String studentId = file.getName();
			
			File reportFile = new File("./junit_test_folder/student_report.txt");
			reportFile.createNewFile();
			
			Report report = new Report(studentId, reportFile);
			
			// try that student has correct files.
			try {
				int fileCount = 0;
				
				main:
				for (String fileNeed : filesNeed) {
					for(File studentFile : studentFiles) {
						if (studentFile.getName().equals(fileNeed)) {
							studentFile.renameTo(new File("junit_test_folder/" + fileNeed));
							fileCount += 1;
							continue main;
						}
					}
				}
				if(fileCount != filesNeed.length) {
					throw new Exception();
				}
			}
			// if there are wrong files write this to the report
			catch(Exception e) {
				report.setGrade(0);
				report.addLine("Homework files can not found. So grade is " + report.getGrade());
				report.flushReport();
				
				// delete student files and move report file to student folder
				for(File studentFile : studentFiles) {
					studentFile.delete();
				}
				reportFile.renameTo(new File(file.getPath() + "/student_report.txt"));
				continue;
			}
			
			// compile all the files we need
			for(String fileName : filesNeed) {
				Terminal.run(sourceFolder, "javac junit_test_folder/" + fileName);				
			}
			
			// run tests and get results
			int[] grades = getTestResults();
			if(grades == null) {
				new File("junit_test_folder/Homework.java").delete();
				new File("junit_test_folder/Homework.class").delete();
				reportFile.delete();
				System.out.println("JUnit test file is broken. Try with different JUnit file.");
				break;
			}
			
			// for calculation and add results to student report
			int result = (int)calculatePoints(100, grades[0] + grades[1], grades[0]);
			report.setGrade(result);
			report.addLine("result is " + result);
			report.addLine("report is finished.");
			report.flushReport();
			
			// delete student files and move report file to student folder
			for(File studentFile : studentFiles) {
				studentFile.delete();
			}
			reportFile.renameTo(new File(file.getPath() + "/student_report.txt"));
			
			// delete student files on main directory
			for(String fileName : filesNeed) {
				new File("junit_test_folder/" + fileName).delete();
				fileName = fileName.replace(".java", ".class");
				new File("junit_test_folder/" + fileName).delete();							
			}
			
		}
		
		// compress output folder
		String zip_output = Terminal.run(new File("."), "python3 lib/zip_helper.py 1");
		
		if(zip_output.endsWith("BROKEN_FILE_EXCEPTION\n")) {
			deleteDirectory(new File("homeworks/outputs/"));
			throw new FileNotFoundException("Zip file already exist. Delete results.zip and try again");
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

	// returns an integer array which includes successfull answers(0) and failed answers(1) count
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