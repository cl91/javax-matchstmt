package symtab;

public class LocalScope extends BaseScope {
	public LocalScope(Scope enclosing_scope, String scope_name) {
		super(enclosing_scope, scope_name);
	}
}
