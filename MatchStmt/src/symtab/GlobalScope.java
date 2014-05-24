package symtab;

import java.util.ArrayList;
import java.util.List;

public class GlobalScope extends BaseScope {
	public GlobalScope() {
		super(null, "GlobalScope");
		define(new BuiltInTypeSymbol("int"));
		define(new BuiltInTypeSymbol("boolean"));
        define(new BuiltInTypeSymbol("byte"));
        define(new BuiltInTypeSymbol("char"));
        define(new BuiltInTypeSymbol("double"));
        define(new BuiltInTypeSymbol("float"));
        define(new BuiltInTypeSymbol("long"));
        define(new BuiltInTypeSymbol("short"));
        VoidType void_sym = new VoidType();
        define(void_sym);
        define(new NullType());
		ClassSymbol str = new ClassSymbol("String", this, false);
		define(str);
		ClassSymbol obj = new ClassSymbol("Object", this, false);
		define(obj);
		ClassSymbol sys = new ClassSymbol("System", this, false);
		define(sys);
		str.add_superclass(obj);
		sys.add_superclass(obj);
		ClassSymbol out = new ClassSymbol("out", sys, false);
		sys.define(out);
		MethodSymbol pln = new MethodSymbol("println", void_sym, out);
		List<Type> pln_sig = new ArrayList<Type>();
		pln_sig.add(str);
		pln.add_overloading(pln_sig);
		sys.define(pln);
	}
}
