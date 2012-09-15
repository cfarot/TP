package test;
import org.junit.Test;

import files.BinaryExp;
import files.ConstExp;
import files.EvalVisitor;
import files.Expression;
import files.UnaryExp;


public class start {
	@Test
	public void startTest() {
		EvalVisitor evalVisit = new EvalVisitor();
		
		Expression e1 = new ConstExp(1);
		Expression e2 = new ConstExp(2);
		Expression e3 = new ConstExp(3);
		
		Expression e1_plus_e2 = new BinaryExp(e1, "+", e2);
		Expression e1_plus_e2_fois3 = new BinaryExp(e1_plus_e2, "*", e3);
		
		Expression e6 = new UnaryExp("-", e1_plus_e2_fois3);

	   System.out.println(e6.acceptEval(evalVisit)); 
	}
}
