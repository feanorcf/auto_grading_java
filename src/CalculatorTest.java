import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class CalculatorTest {
	
	// to run in console
	// !!! junit-platform-console-standalone-1.10.2.jar file must be in your "project_name"/lib folder !!!
	// !!! your terminal must be on "project_name" folder !!!
	
	// $ java -jar lib/junit-platform-console-standalone-1.10.2.jar execute -cp bin/ -c CalculatorTest
	
	// compile java code(not required in this case)
	// $ javac -d bin -sourcepath src -cp .:lib/junit-platform-console-standalone-1.10.2.jar src/CalculatorTest.java
	
	
	// add tests(4)
	@Test
	void adding_test_0_0() {		
		// define expected value after using method
		int expected = 0;
		
		// define actual value
		int actual = Calculator.add(0, 0);
		
		// if both values are equal, test will be correct
		assertEquals(expected, actual);
	}
	
	@Test
	void adding_test_5_3() {
		int expected = 8;
		int actual = Calculator.add(5, 3);
		assertEquals(expected, actual);
	}
	
	@Test
	void adding_test_89_12() {
		int expected = 101;
		int actual = Calculator.add(89, 12);
		assertEquals(expected, actual);
	}
	
	@Test
	void adding_test_false_100_650() {
		int not_expected = 10;
		int actual = Calculator.add(100, 650);
		
		// if both values are not equal, test will be correct
		assertNotEquals(not_expected, actual);
	}
	
	@Test
	void adding_test_false_15_15() {
		int not_expected = 35;
		int actual = Calculator.add(15, 15);
		assertNotEquals(not_expected, actual);
	}
	
	// divide tests(3)
	@Test
	void divide_test_120_2() {
		double expected = 60;
		double actual = Calculator.divide(120, 2);
		assertEquals(expected, actual);
	}
	
	@Test
	void divide_test_25_5() {
		double expected = 5;
		double actual = Calculator.divide(25, 5);
		assertEquals(expected, actual);
	}
	
	@Test
	void divide_test_false_50_5() {
		double not_expected = 25;
		double actual = Calculator.divide(50, 5);
		assertNotEquals(not_expected, actual);
	}
	
	// divide by 0
	@Test
	void divide_test_division_by_zero() {		
		// define expected exceptions class
		Class<ArithmeticException> expected_exception = ArithmeticException.class;
		
		// define Executable function(lambda)
		Executable function = () -> { Calculator.divide(5, 0); } ;
		
		// if actual function trows expected excepition, test will be correct 
		assertThrows(expected_exception, function);
		
	}
	
}

