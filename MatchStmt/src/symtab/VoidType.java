package symtab;

public class VoidType extends Symbol implements Type {
	public VoidType() {
		super("void", null);	// A Type doesn't have type
	}
}
