package symtab;

public interface Scope {
	public String getScopeName();
	public Scope getEnclosingScope();
	public void define(Symbol symbol);
	public Symbol resolve(String name);
	public Symbol resolve_this_level(String name);
	public void print_symbols();
}
