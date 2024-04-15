public class SampleProgram {
	
	public static int add(int a, int b) {
		return a + b;
	}
	
	public static double divide(int a, int b) {
		if(b == 0) {
			throw new ArithmeticException("can not divide by 0");
		}
		return a / b;
	}
}