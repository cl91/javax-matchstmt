package symtab;

public class VariableSymbol extends Symbol {
	private Scope defining_scope = null;
	private int line = 0;
	private int column = 0;

	public VariableSymbol(String name, Type type, Scope defined_in,
			int line, int column) {
		super(name, type);
		defining_scope = defined_in;
		this.line = line;
		this.column = column;
	}
	
	// get the scope in which this symbol is first defined
	public Scope get_defining_scope() {
		return defining_scope;
	}
	
	public boolean is_defined_before(int line, int column) {
		if (this.line < line) {
			return true;
		} else if (this.line == line) {
			return this.column < column;
		} else {
			return false;
		}
	}
}
