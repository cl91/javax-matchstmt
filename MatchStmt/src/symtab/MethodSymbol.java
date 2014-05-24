package symtab;

import java.util.List;
import java.util.ArrayList;

public class MethodSymbol extends Symbol {
	private List<List<Type>> overload_list = new ArrayList<List<Type>>();
	private List<LocalScope> scope_list = new ArrayList<LocalScope>();
	private Scope enclosing_scope;
	
	public MethodSymbol(String name, Type type, Scope enclosing_scope) {
		super(name, type);
		this.enclosing_scope = enclosing_scope;
	}
	
	public LocalScope add_overloading(List<Type> signature) {
		overload_list.add(signature);
		String scope_name = get_mangled_name(signature);
		LocalScope scope = new LocalScope(enclosing_scope, scope_name);
		scope_list.add(scope);
		return scope;
	}
	
	private static boolean signature_equals(List<Type> s1,
							List<Type> s2) {
		if (s1.size() != s2.size())
			return false;
		for (int i = 0; i < s1.size(); i++) {
			Type t1 = s1.get(i);
			Type t2 = s2.get(i);
			if (!t1.getName().equals(t2.getName()))
					return false;
		}
		return true;
	}
	
	// returns true if the signature matches one in overload_list
	public boolean is_defined(List<Type> signature) {
		for (List<Type> l : overload_list) {
			if (signature_equals(l, signature)) {
				return true;
			}
		}
		return false;
	}
	
	public String get_mangled_name(List<Type> signature) {
		String mangled = new String(name);
		if (signature.size() != 0) {
			mangled += "@"+signature.get(0).getName();
			for (int i = 1; i < signature.size(); i++)
				mangled += ":"+signature.get(i).getName();
		}
		return mangled;
	}
	
	public String get_readable_name(List<Type> signature) {
		String name = new String(this.name);
		name += "(";
		if (signature.size() != 0) {
			name += signature.get(0).getName();
			for (int i = 1; i < signature.size(); i++)
				name += ", "+signature.get(i).getName();
		}
		name += ")";
		return name;

	}

	public Scope get_scope(List<Type> signature) {
		for (int i = 0; i < overload_list.size(); i++) {
			if (signature_equals(overload_list.get(i), signature)) {
				return scope_list.get(i);
			}
		}
		return null;
	}
}