package files;

public class BinaryExp implements Expression {

	private Expression left;
	private String op;
	private Expression right;
	
	
	public BinaryExp(Expression left, String op, Expression right) {
		
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	@Override
	public double acceptEval(IEvalVisitor v) {
		return v.caseBinaryExp(this);
	}

	public Expression getLeft() {
		return left;
	}

	public String getOp() {
		return op;
	}

	public Expression getRight() {
		return right;
	}	
	
}
