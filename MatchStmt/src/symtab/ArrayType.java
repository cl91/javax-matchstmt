package symtab;

public class ArrayType extends ClassSymbol implements Type {
	private Type type;		// 'int' as in 'int[]'
	private int array_count;
	public ArrayType(Type type, int array_count,
					Scope enclosing_scope) {
		super(null, enclosing_scope, false);
		this.type = type;
		this.array_count = array_count;
		this.name = getName();
		VariableSymbol len_sym = new VariableSymbol("length",
				(Type) this.resolve("int"), this, 0, 0);
		this.define(len_sym);
	}

	@Override
	public String getName() {
		String name = new String(type.getName());
		for (int i = 0; i < get_array_count(); i++) {
			name += "[]";
		}
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	public int get_array_count() {
		return array_count;
	}
}