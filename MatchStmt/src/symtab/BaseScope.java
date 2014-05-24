package symtab;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseScope implements Scope  {
	protected Scope enclosing_scope = null;
	protected String scope_name = null;
	public BaseScope(Scope enclosing, String name) {
		enclosing_scope = enclosing;
		scope_name = name;
	}
	
	public String getScopeName() {
		return scope_name;
	}

	public Scope getEnclosingScope() {
		return enclosing_scope;
	}

	protected Map<String, Symbol> symbols = new HashMap<String, Symbol>();
	
	public void define(Symbol symbol) {
		symbols.put(symbol.getName(), symbol);
	}
	public Symbol resolve(String name) {
		if (symbols.get(name) != null) {
			return symbols.get(name);
		} else if (enclosing_scope != null){
			return enclosing_scope.resolve(name);
		}
		return null;
	}
	// only resolve symbols in this scope, do not ascend recursively
	public Symbol resolve_this_level(String name) {
		return symbols.get(name);
	}
	public void print_symbols() {
		for (String s : symbols.keySet()) {
			System.out.println(scope_name + ": " + s);
		}
		if (enclosing_scope != null) {
			enclosing_scope.print_symbols();
		}
	}
}
