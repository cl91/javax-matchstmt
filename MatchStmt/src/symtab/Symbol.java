package symtab;

public class Symbol {
	public Symbol(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	protected String name;
	protected Type type;
	
	public String getName() {
		return name;
	}
	
	public Type get_type() {
		return type;
	}
}
