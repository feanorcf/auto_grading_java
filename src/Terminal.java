import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class Terminal {
	
	public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");
	
	public static String run(File location, String command) throws Exception {
		// process builder helps us to run a console command
		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(location);
		
		// send our command to builder
		// if windows open cmd if linux/macOS open shell(terminal)
		if(IS_WINDOWS) {
			builder.command("cmd.exe", "/c", command);
		}
		else {
			builder.command("sh", "-c", command);
		}
		
		// start method returns us process object includes our data
		Process process = builder.start();
			
		//  get process streams
		OutputStream output = process.getOutputStream();
		InputStream input = process.getInputStream();
		
		String responses = getStream(input);
		
		// if process running time > 30 seconds this will return false
		boolean isFinished = process.waitFor(30, TimeUnit.SECONDS);
		
		// closing stream
		output.flush();
		output.close();
		
		//  if not finished, finish process
		if(!isFinished) {
			process.destroyForcibly();
		}
		
		return responses;
	}
	
	
	private static String getStream(InputStream input) throws IOException {
		// read output data
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
			String outputs = "";
			
			String line;
			while((line = reader.readLine()) != null) {
				// System.out.println(line);
				outputs += line + "\n";
			}
			return outputs;
		}
	}
}
