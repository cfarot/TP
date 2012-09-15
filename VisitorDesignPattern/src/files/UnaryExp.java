package files;

public class UnaryExp implements Expression{

	private String op;
	private Expression exp;
	
	
	public UnaryExp(String op, Expression exp) {
		this.op = op;
		this.exp = exp;
	}
	
	@Override
	public double acceptEval(IEvalVisitor v) {
		return v.caseUnaryExp(this);
	}

	public String getOp() {
		return op;
	}

	public Expression getExp() {
		return exp;
	}
	
}
