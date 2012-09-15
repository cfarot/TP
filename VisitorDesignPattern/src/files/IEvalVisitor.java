package files;

public interface IEvalVisitor {
	public double caseConstExp(ConstExp e);
	public double caseUnaryExp(UnaryExp e);
	public double caseBinaryExp(BinaryExp e);
}
