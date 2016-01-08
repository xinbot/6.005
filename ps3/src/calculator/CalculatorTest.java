package calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class CalculatorTest {

	// TODO write tests for MultiUnitCalculator.evaluate
	@Test
	public void MultiUnitCalculatorTest(){
		MultiUnitCalculator cal = new MultiUnitCalculator();
		
		//44.5 + 12.3 = 56.8
		String expected = "56.8";
		String result = cal.evaluate("44.5 + 12.3");
		
		assertEquals(expected, result);
		
		//44.5 - 11.9 = 32.6
		expected = "32.6";
		result = cal.evaluate("44.5 - 11.9");
		
		assertEquals(expected, result);
		
		//12.4 * 45.6 = 565.44
		expected = "565.44";
		result = cal.evaluate("12.4 * 45.6");
		
		assertEquals(expected, result);
		
		//34 / 2 = 17.0
		expected = "17.0";
		result = cal.evaluate("34 / 2");
		
		assertEquals(expected, result);
		
	}

	boolean approxEquals(String expr1, String expr2, boolean compareUnits) {
		return new Value(expr1).approxEquals(new Value(expr2), compareUnits);
	}

	static class Value {
		static float delta = 0.001f;
 
		enum Unit {
			POINT, INCH, SCALAR
		}

		Unit unit;
		// in points if a length
		float value;

		Value(String value) {
			value = value.trim();
			if (value.endsWith("pt")) {
				unit = Unit.POINT;
				this.value = Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else if (value.endsWith("in")) {
				unit = Unit.INCH;
				this.value = 72 * Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else {
				unit = Unit.SCALAR;
				this.value = Float.parseFloat(value);
			}
		}

		boolean approxEquals(Value that, boolean compareUnits) {
			return (this.unit == that.unit || !compareUnits)
					&& Math.abs(this.value - that.value) < delta;
		}
	}

}
