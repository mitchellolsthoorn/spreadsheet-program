package sheetproject.test.formulatests;

import static org.junit.Assert.*;

import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Test;

import sheetproject.exception.CharacterOutOfBoundsException;
import sheetproject.exception.IllegalFormulaException;
import sheetproject.exception.NullObjectException;
import sheetproject.exception.NumberOutOfBoundsException;
import sheetproject.formula.Product;
import sheetproject.spreadsheet.Cell;
import sheetproject.spreadsheet.Sheet;

public class ProductTest {
	Sheet data = null;
	
	@Before
	public void setUp() throws IndexOutOfBoundsException, NullObjectException{
		data = new Sheet();
		data.setCell(new Cell("5"), 1, 1);
		data.setCell(new Cell("4"), 1, 2);
		data.setCell(new Cell("3"), 1, 3);
		data.setCell(new Cell("7"), 2, 1);
		data.setCell(new Cell("8"), 2, 2);
		data.setCell(new Cell("2"), 2, 3);
		data.setCell(new Cell("zes"), 3, 1);
		data.setCell(new Cell("zeven"), 3, 2);
		data.setCell(new Cell("acht"), 3, 3);
	}
	
	@Test
	public void testConstructor() throws CharacterOutOfBoundsException, IllegalFormulaException, IndexOutOfBoundsException
	{			
		assertNotNull(new Product());
		
	}
	
	@Test
	public void testEvaluatePositive() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{		
		assertEquals(Product.evaluate("=PRODUCT(2, 4)", data), "8.0");		
	}
	
	@Test
	public void testEvaluatePositiveZero1() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{		
		assertEquals(Product.evaluate("=PRODUCT(2, 0)", data), "0.0");		
	}
	
	@Test
	public void testEvaluatePositiveZero2() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{		
		assertEquals(Product.evaluate("=PRODUCT(0, 4)", data), "0.0");		
	}
	
	@Test
	public void testEvaluatePositiveNegative1() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{		
		assertEquals(Product.evaluate("=PRODUCT(-2, 4)", data), "-8.0");		
	}
	
	@Test
	public void testEvaluatePositiveNegative2() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{		
		assertEquals(Product.evaluate("=PRODUCT(2, -4)", data), "-8.0");		
	}
	
	@Test
	public void testEvaluatePositiveCell1() throws CharacterOutOfBoundsException, IllegalFormulaException, IndexOutOfBoundsException, NullObjectException, NumberOutOfBoundsException 
	{			
		data.setCell(new Cell("4"), 1, 1);
		assertEquals(Product.evaluate("=PRODUCT(2, A1)", data), "8.0");		
	}
	
	@Test
	public void testEvaluatePositiveCell2() throws CharacterOutOfBoundsException, IllegalFormulaException, IndexOutOfBoundsException, NullObjectException, NumberOutOfBoundsException 
	{			
		data.setCell(new Cell("2"), 1, 1);
		assertEquals(Product.evaluate("=PRODUCT(A1, 4)", data), "8.0");		
	}
	
	@Test
	public void testEvaluatePositiveNested() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{			
		assertEquals(Product.evaluate("=PRODUCT(2, PRODUCT(2, 3))", data), "12.0");		
	}
	
	@Test
	public void testPositiveOneColumn() throws CharacterOutOfBoundsException, IllegalFormulaException, ScriptException, NumberOutOfBoundsException, IndexOutOfBoundsException, NullObjectException {	
		assertEquals(Product.evaluate("=PRODUCT(A1:A3)", data), "60.0");
	}
	
	@Test
	public void testPositiveTwoColumns() throws CharacterOutOfBoundsException, IllegalFormulaException, ScriptException, NumberOutOfBoundsException, IndexOutOfBoundsException, NullObjectException {	
		assertEquals(Product.evaluate("=PRODUCT(A1:B3))", data), "6720.0");
	}	
	
	@Test
	public void testPositiveText() throws CharacterOutOfBoundsException, IllegalFormulaException, ScriptException, NumberOutOfBoundsException, IndexOutOfBoundsException, NullObjectException {	
		assertEquals(Product.evaluate("=PRODUCT(A1:C3)", data), "6720.0");
	}
	
	@Test
	public void testEvaluateNegativeColumnMore() throws CharacterOutOfBoundsException, IllegalFormulaException, ScriptException, NumberOutOfBoundsException 
	{			
		assertEquals(Product.evaluate("=PRODUCT(B1:A1)", data), "");		
	}
	
	@Test
	public void testEvaluateNegativeRowMore() throws CharacterOutOfBoundsException, IllegalFormulaException, ScriptException, NumberOutOfBoundsException 
	{			
		assertEquals(Product.evaluate("=PRODUCT(A2:A1)", data), "");		
	}
	
	@Test
	public void testEvaluateNegativeBothMore() throws CharacterOutOfBoundsException, IllegalFormulaException, ScriptException, NumberOutOfBoundsException 
	{			
		assertEquals(Product.evaluate("=PRODUCT(B2:A1)", data), "");		
	}
	
	@Test
	public void testEvaluateNegativeCell1() throws CharacterOutOfBoundsException, IllegalFormulaException, IndexOutOfBoundsException, NullObjectException, NumberOutOfBoundsException 
	{			
		data.setCell(new Cell("vier"), 1, 1);
		assertEquals(Product.evaluate("=PRODUCT(2, A1)", data), "0.0");		
	}
	
	@Test
	public void testEvaluateNegativeCell2() throws CharacterOutOfBoundsException, IllegalFormulaException, IndexOutOfBoundsException, NullObjectException, NumberOutOfBoundsException 
	{			
		data.setCell(new Cell("twee"), 1, 1);
		assertEquals(Product.evaluate("=PRODUCT(A1, 4)", data), "0.0");		
	}
	
	@Test
	public void testEvaluateNegativeNoPatternMatchEmpty() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{
		assertEquals(Product.evaluate("=PRODUCT()", data), "");
	}
	
	@Test
	public void testEvaluateNegativeNoPatternMatchLessArguments() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{
		assertEquals(Product.evaluate("=PRODUCT(4)", data), "");
	}
	
	@Test
	public void testEvaluateNegativeNoPatternMatchMoreArguments() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{
		assertEquals(Product.evaluate("=PRODUCT(4,5,6)", data), "");
	}
	
	@Test
	public void testEvaluateNegativeNoDouble() throws CharacterOutOfBoundsException, IllegalFormulaException, NumberOutOfBoundsException 
	{
		assertEquals(Product.evaluate("=PRODUCT(vier)", data), "");
	}

}
