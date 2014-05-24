package symtab;

import java.util.List;
import java.util.ArrayList;
import se701.A2SemanticsException;

public class ClassSymbol extends ScopedSymbol implements Type {
	private boolean is_iface;
	private List<ClassSymbol> superclasses =
			new ArrayList<ClassSymbol>();
	private List<ClassSymbol> implemented_ifaces =
			new ArrayList<ClassSymbol>();
	public ClassSymbol(String name, Scope enclosing_scope,
					boolean is_iface) {
		super(name, null, enclosing_scope);		// class symbols don't have types
		this.is_iface = is_iface;
	}
	public void add_superclass(ClassSymbol cls) {
		if (cls.is_interface()) {
			throw new A2SemanticsException("ClassSymbol.add_superclass: class expected, interface found instead");
		}
		superclasses.add(cls);
	}
	public List<ClassSymbol> get_superclasses() {
		return superclasses;
	}
	public boolean is_subclass(String name) {
		for (ClassSymbol cls : superclasses) {
			if (name.equals(cls.getName())) {
				return true;
			}
		}
		for (ClassSymbol cls : superclasses) {
			if (cls.is_subclass(name)) {
				return true;
			}
		}
		return false;
	}
	public List<ClassSymbol> get_implemented_ifaces() {
		return implemented_ifaces;
	}
	public boolean is_implemented(String name) {
		for (ClassSymbol cls : implemented_ifaces) {
			if (name.equals(cls.getName()))
					return true;
		}
		return false;
	}
	public void add_implemented_iface(ClassSymbol cls) {
		if (!cls.is_interface())
			throw new A2SemanticsException("ClassSymbol.add_implemented_iface: interface expected, class found instead");
		implemented_ifaces.add(cls);
	}
	public boolean is_interface() {
		return is_iface;
	}
	// resolve member access, will include superclasses and interfaces
	public Symbol resolve_member(String name) {
		Symbol ret = resolve_this_level(name);
		if (ret != null) {
			return ret;
		}
		for (ClassSymbol sup : superclasses) {
			ret = sup.resolve_member(name);
			if (ret != null) {
				return ret;
			}
		}
		for (ClassSymbol ifce : implemented_ifaces) {
			ret = ifce.resolve_member(name);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}
}
