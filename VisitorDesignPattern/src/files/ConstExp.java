package files;

public class ConstExp implements Expression {

	private double value;

	public ConstExp(double value) {
		this.value = value;
	}

	@Override
	public double acceptEval(IEvalVisitor v) {
		return v.caseConstExp(this);
	}

	public double getValue() {
		return value;
	}

}
