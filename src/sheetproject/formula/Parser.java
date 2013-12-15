package sheetproject.formula;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sheetproject.alfabet.Alfabet;
import sheetproject.exception.CharacterOutOfBoundsException;
import sheetproject.exception.IllegalFormulaException;
import sheetproject.exception.NullObjectException;
import sheetproject.spreadsheet.*;

public class Parser {
	
	static Pattern format = Pattern.compile("(([A-Z]{2,10})\\((.*)\\))");
	static Pattern format2 = Pattern.compile("\\s*([A-Z]{1,2})([0-9]{1,6})\\s*");
	
	private static String[] allowedFormulas = 
	{
		"Average","Count","Counta","Countif","If","Int","Islogical","Iseven",
		"Isnumber","Lower","Max","Median","Min","Mod","Not","Or","Power",
		"Product","Proper","Rounddown","Roundup","Sign","Sqrt","Sum","Sumif"
	};
	
	public static String parse(String formula, Sheet data) throws IllegalFormulaException, CharacterOutOfBoundsException
	{		

		formula = formula.trim();
		if (formula.startsWith("="))
		{
			 formula = evaluate(formula, data);
		}
		return formula;
	}
	
	static String evaluate(String formula, Sheet data) throws IllegalFormulaException, CharacterOutOfBoundsException
	{
		Matcher m = format.matcher(formula);
		Matcher m2 = format2.matcher(formula);
		String res = formula;
		
		if(m.find())
		{
			String classname = m.group(2);
			classname = classname.toLowerCase();
			classname = Character.toUpperCase(classname.charAt(0)) + classname.substring(1);
			
			if (containsFormula(classname))
			{
				try
				{
					classname = "sheetproject.formula." + classname;
					Class c = Class.forName(classname);
					Class[] methodParameters = new Class[2];
					methodParameters[0] = String.class;
					methodParameters[1] = Sheet.class;
					Object[] parameters = new Object[2];
					parameters[0] = formula;
					parameters[1] = data;
					Method method = c.getMethod("evaluate", methodParameters);
					return (String) method.invoke(null, parameters);
				}					
				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				throw new IllegalFormulaException();
			}
		}
		else if (m2.find())
		{
                int i = Alfabet.parseChar(m2.group(1));
                int j = Integer.parseInt(m2.group(2));
                String cellContent = data.getCell(i, j).getFormula();
                cellContent = Parser.evaluate(cellContent, data);
                return cellContent;
		}
		return res;
	}
	
	public static boolean containsFormula(String formula)
	{
		for (String formulaName : allowedFormulas)
		{
			if (formula.equals(formulaName))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws IllegalFormulaException, IndexOutOfBoundsException, NullObjectException, CharacterOutOfBoundsException
	{
		Sheet sheet = new Sheet();
		Cell a = new Cell("6");
		Cell b = new Cell("5");
		sheet.setCell(a, 1, 1);
		sheet.setCell(b, 1, 2);
		
		String z = Parser.parse("=SUM(A2,SUM(SUM(8,A1),A2))", sheet);
		System.out.println(z);
	}
	
	
}