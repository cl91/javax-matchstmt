package symtab;

public class NullType extends Symbol implements Type {
	public NullType() {
		super("null", null);	// A Type doesn't have type
	}
}
