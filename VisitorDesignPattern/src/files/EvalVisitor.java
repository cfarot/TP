package files;
public class EvalVisitor implements IEvalVisitor {

	
	@Override
	public double caseConstExp(ConstExp e) {
		return e.getValue();
	}

	@Override
	public double caseUnaryExp(UnaryExp e) {
		if (e.getOp().equals("-")) {
			return -e.getExp().acceptEval(this);
		} else {
			return e.getExp().acceptEval(this);
		}
	}

	@Override
	public double caseBinaryExp(BinaryExp e) {
		if (e.getOp().equals("-"))
			return e.getLeft().acceptEval(this) - e.getRight().acceptEval(this);

		if (e.getOp().equals("+"))
			return e.getLeft().acceptEval(this) + e.getRight().acceptEval(this);

		if (e.getOp().equals("*"))
			return e.getLeft().acceptEval(this) * e.getRight().acceptEval(this);

		if (e.getOp().equals("/"))
			return e.getLeft().acceptEval(this) / e.getRight().acceptEval(this);
		else return 0;
	}

}
