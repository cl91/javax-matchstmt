package symtab;

public class ScopedSymbol extends Symbol implements Scope {
	private LocalScope scope = null;
	public ScopedSymbol(String name, Type type, Scope enclosing_scope) {
		super(name, type);
		scope = new LocalScope(enclosing_scope, name);
	}

	@Override
	public String getScopeName() {
		return scope.getScopeName();
	}

	@Override
	public Scope getEnclosingScope() {
		return scope.getEnclosingScope();
	}

	@Override
	public void define(Symbol symbol) {
		scope.define(symbol);
	}

	@Override
	public Symbol resolve(String name) {
		return scope.resolve(name);
	}

	@Override
	public void print_symbols() {
		scope.print_symbols();
	}

	@Override
	public Symbol resolve_this_level(String name) {
		return scope.resolve_this_level(name);
	}
}
