/*
 * Copyright (C) 2007 J�lio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05/10/2006
 */
package japa.parser.ast.visitor;

import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.SuperMemberAccessExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.MatchEntryStmt;
import japa.parser.ast.stmt.MatchStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import se701.A2SemanticsException;
import symtab.*;

public final class ResolvingVisitor implements GenericVisitor<symtab.Type, Object> {
	private Scope current_scope = null;
	public ResolvingVisitor(GlobalScope global) {
		current_scope = global;
	}

    public symtab.Type visit(Node n, Object arg) {
        throw new IllegalStateException(n.getClass().getName());
    }

    public symtab.Type visit(CompilationUnit n, Object arg) {
        if (n.getPakage() != null) {
            n.getPakage().accept(this, arg);
        }
        if (n.getImports() != null) {
            for (ImportDeclaration i : n.getImports()) {
                i.accept(this, arg);
            }
        }
        if (n.getTypes() != null) {
            for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i.hasNext();) {
                i.next().accept(this, arg);
            }
        }
		return null;
    }

    public symtab.Type visit(PackageDeclaration n, Object arg) {
        //n.getName().accept(this, arg);
        return null;
    }

    private boolean not_defined_yet(VariableSymbol v, NameExpr n) {
    	return !v.is_defined_before(n.getBeginLine(), n.getBeginColumn());
    }
    
    private boolean is_local_variable(VariableSymbol v) {
    	return !(v.get_defining_scope() instanceof ClassSymbol);
    }
    
    public symtab.Type visit(NameExpr n, Object arg) {
    	String name = n.getName();
    	Symbol sym = resolve_locally(current_scope, name);
    	if (sym == null) {
    		Scope scope = current_scope;
    		do {
    			scope = scope.getEnclosingScope();
    		} while (!(scope instanceof ClassSymbol));
    		sym = ((ClassSymbol) scope).resolve_member(name);
    	}
    	if (sym == null) {
    		sym = current_scope.resolve(name);
    	}
    	if (sym == null) {
    		throw new A2SemanticsException("undefined identifier `"+name
    				+"' on line "+n.getBeginLine()+", column "+n.getBeginColumn());
    	}

    	if (sym instanceof ClassSymbol) {
    		return (symtab.Type) sym;
    	}
    	if (sym instanceof VariableSymbol) {
    		VariableSymbol var = (VariableSymbol) sym;
    		if (is_local_variable(var) && not_defined_yet(var, n)) {
    			throw new A2SemanticsException("variable symbol `"+name
    					+"' is not defined yet, line "+n.getBeginLine()
    					+", column "+n.getBeginColumn());
    		}
    		return var.get_type();
    	}
    	return null;
    }

    // QualifiedNameExpr is for import and package only
    // so we ignore it for now
    public symtab.Type visit(QualifiedNameExpr n, Object arg) {
        n.getQualifier().accept(this, arg);
        return null;
    }

    public symtab.Type visit(ImportDeclaration n, Object arg) {
        //n.getName().accept(this, arg);
        return null;
    }

    public symtab.Type visit(ClassOrInterfaceDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        
        Symbol cls = current_scope.resolve(n.getName());
        current_scope = (ClassSymbol) cls;
        if (n.getExtends() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }

        if (n.getImplements() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }
        
        if (n.getMembers() != null) {
        	for (BodyDeclaration m : n.getMembers()) {
        		m.accept(this, arg);
        	}
        }
        current_scope = ((ClassSymbol)cls).getEnclosingScope();
        return null;
    }

    public symtab.Type visit(EmptyTypeDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(JavadocComment n, Object arg) {
    	return null;
    }

    public symtab.Type visit(ClassOrInterfaceType n, Object arg) {
    	Scope scope = current_scope;
        if (n.getScope() != null) {
            ClassSymbol supercls = (ClassSymbol) n.getScope().accept(this, arg);
            scope = supercls;
        }
        String type = n.getName();
        Symbol type_symbol = scope.resolve(type);
        if ((type_symbol == null) || !(type_symbol instanceof ClassSymbol)) {
        	throw new A2SemanticsException("`"+type
            			+"' is not a class or interface (line "+n.getBeginLine()
            			+", column "+n.getBeginColumn()+").");
        }
        return (ClassSymbol) type_symbol;
    }

    public symtab.Type visit(TypeParameter n, Object arg) {
        if (n.getTypeBound() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }
        return null;
    }

    public symtab.Type visit(PrimitiveType n, Object arg) {
    	String name = null;
    	switch (n.getType()) {
    	case Boolean:
    		name = "boolean";
    		break;
    	case Char:
    		name = "char";
    		break;
    	case Byte:
    		name = "byte";
    		break;
    	case Short:
    		name = "short";
    		break;
    	case Int:
    		name = "int";
    		break;
    	case Long:
    		name = "long";
    		break;
    	case Float:
    		name = "float";
    		break;
    	case Double:
    		name = "double";
    		break;
    	}
		return (symtab.Type) current_scope.resolve(name);
    }

    public symtab.Type visit(ReferenceType n, Object arg) {
    	if (n.getArrayCount() == 0) {
    		return n.getType().accept(this, arg);
    	} else {
    		return new ArrayType(n.getType().accept(this, arg), n.getArrayCount(),
    				current_scope);
    	}
    }

    public symtab.Type visit(WildcardType n, Object arg) {
        if (n.getExtends() != null) {
            n.getExtends().accept(this, arg);
        }
        if (n.getSuper() != null) {
            n.getSuper().accept(this, arg);
        }
		return null;
    }
    
    public symtab.Type visit(FieldDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        // this checks if type is defined (in the ClassOrInterfaceType visitor
        symtab.Type type = n.getType().accept(this, arg);
//        System.out.println(type.getName());
        
        for (VariableDeclarator v : n.getVariables()) {
            v.accept(this, type);
        }
        return null;
    }

    private boolean is_convertible(symtab.Type from, symtab.Type to) {
		if (from instanceof BuiltInTypeSymbol) {
			return from == to;
		} else if (from instanceof NullType) {
			if (to instanceof ClassSymbol) {
				return true;
			} else {
				return false;
			}
		} else if (from instanceof ArrayType) {
			return (to instanceof ArrayType) &&
					is_convertible(((ArrayType) from).getType(),
							((ArrayType) to).getType()) &&
					(((ArrayType) from).get_array_count() ==
						((ArrayType) to).get_array_count());
		} else if (from instanceof ClassSymbol) {
			if (from == to) {
				return true;
			} else {
				return ((ClassSymbol)from).is_subclass(to.getName());
			}
		}
		return false;	// void type falls here
	}
    
    private void check_convertibility(symtab.Type ltype, symtab.Type rtype,
    							int line, int col) {
    	if (!is_convertible(rtype, ltype)) {
    		throw new A2SemanticsException("illegal type conversion from `"+
                    rtype.getName()+"' to `"+ltype.getName()+"' on line "
                    +line+", column "+col);
    	}
    }

	public symtab.Type visit(VariableDeclarator n, Object arg) {
        n.getId().accept(this, null);
        symtab.Type ltype = (symtab.Type) arg;
        if (n.getInit() != null) {
            symtab.Type rtype = n.getInit().accept(this, ltype);
            check_convertibility(ltype, rtype, n.getBeginLine(),
            					n.getBeginColumn());
        }
        return null;
    }

    public symtab.Type visit(VariableDeclaratorId n, Object arg) {
    	return null;
    }

    public symtab.Type visit(ArrayInitializerExpr n, Object arg) {
        if ((n.getValues() != null) && (arg != null)) {
        	ArrayType arr = (ArrayType) arg;
        	symtab.Type lt = arr.getType();
        	for (Expression expr : n.getValues()) {
                symtab.Type rt = expr.accept(this, null);
                check_convertibility(lt, rt, n.getBeginLine(),
                				n.getBeginColumn());
            }
        }
    	return (symtab.Type) arg;
    }

    public symtab.Type visit(VoidType n, Object arg) {
    	return (symtab.Type) current_scope.resolve("void");
    }

    public symtab.Type visit(ArrayAccessExpr n, Object arg) {
        symtab.Type arr_type = n.getName().accept(this, arg);
        if (!(arr_type instanceof ArrayType)) {
        	throw new A2SemanticsException("`"+arr_type.getName()+
        			"' is not an array type, line "+n.getBeginLine()
        			+", column "+n.getBeginColumn());
        }
        // we will add index checking later
        n.getIndex().accept(this, arg);
        return ((ArrayType) arr_type).getType();
    }

    public symtab.Type visit(ArrayCreationExpr n, Object arg) {
        symtab.Type prim_type = n.getType().accept(this, arg);
        ArrayType arr_type = new ArrayType(prim_type, n.getArrayCount(), current_scope);
        
        if (n.getDimensions() != null) {
            for (Expression dim : n.getDimensions()) {
                dim.accept(this, arg);
            }
        } else {
            symtab.Type rtype = n.getInitializer().accept(this, arg);
            check_convertibility(arr_type, rtype, n.getBeginLine(),
            						n.getBeginColumn());
        }
        return arr_type;
    }

    public symtab.Type visit(AssignExpr n, Object arg) {
        symtab.Type ltype = n.getTarget().accept(this, arg);
        symtab.Type rtype = n.getValue().accept(this, arg);
        check_convertibility(ltype, rtype, n.getBeginLine(), n.getBeginColumn());
        return null;
    }

    public symtab.Type visit(BinaryExpr n, Object arg) {
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
        return null;
    }

    public symtab.Type visit(CastExpr n, Object arg) {
        n.getType().accept(this, arg);
        n.getExpr().accept(this, arg);
        return null;
    }

    public symtab.Type visit(ClassExpr n, Object arg) {
        n.getType().accept(this, arg);
        return null;
    }

    public symtab.Type visit(ConditionalExpr n, Object arg) {
        n.getCondition().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getElseExpr().accept(this, arg);
        return null;
    }

    public symtab.Type visit(EnclosedExpr n, Object arg) {
        n.getInner().accept(this, arg);
        return null;
    }

    public symtab.Type visit(FieldAccessExpr n, Object arg) {
        symtab.Type t = n.getScope().accept(this, arg);
        if (!(t instanceof ClassSymbol)) {
        	throw new A2SemanticsException("invalid field access ("+
        			n.getScope().toString()+" is not a class or interface): line "+n.getBeginLine()+
        			", column "+n.getBeginColumn());
        }
        ClassSymbol c = (ClassSymbol) t;
        Symbol s = c.resolve_member(n.getField());
        if (s == null) {
        	throw new A2SemanticsException("cannot resolve symbol `"+n.getField()
        			+"' within class "+c.getName()+", line "
        			+n.getBeginLine()+", column "+n.getBeginColumn());
        }
        if (s instanceof MethodSymbol) {
        	throw new A2SemanticsException("`"+n.getField()+"' is a"
        			+" method member of class "+c.getName()+", line "+
        			n.getBeginLine()+", column "+n.getBeginColumn());
        }
        if (s instanceof ClassSymbol) {
        	return (ClassSymbol) s;
        } else {
        	return s.get_type();
        }
    }

    public symtab.Type visit(InstanceOfExpr n, Object arg) {
        n.getExpr().accept(this, arg);
        n.getType().accept(this, arg);
        return null;
    }

    public symtab.Type visit(CharLiteralExpr n, Object arg) {
        return null;
    }

    public symtab.Type visit(DoubleLiteralExpr n, Object arg) {
        return null;
    }

    public symtab.Type visit(IntegerLiteralExpr n, Object arg) {
        return (symtab.Type) current_scope.resolve("int");
    }

    public symtab.Type visit(LongLiteralExpr n, Object arg) {
        return (symtab.Type) current_scope.resolve("long");
    }

    public symtab.Type visit(IntegerLiteralMinValueExpr n, Object arg) {
        return (symtab.Type) current_scope.resolve("int");
    }

    public symtab.Type visit(LongLiteralMinValueExpr n, Object arg) {
        return (symtab.Type) current_scope.resolve("long");
    }

    public symtab.Type visit(StringLiteralExpr n, Object arg) {
        return (symtab.Type) current_scope.resolve("String");
    }

    public symtab.Type visit(BooleanLiteralExpr n, Object arg) {
        return (symtab.Type) current_scope.resolve("boolean");
    }

    public symtab.Type visit(NullLiteralExpr n, Object arg) {
        return (symtab.Type) current_scope.resolve("null");
    }

    public symtab.Type visit(ThisExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(SuperExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(MethodCallExpr n, Object arg) {
    	MethodSymbol method = null;
    	String class_name = null;
    	if (n.getScope() != null) {
    		symtab.Type t = n.getScope().accept(this, arg);
    		if (!(t instanceof ClassSymbol)) {
    			throw new A2SemanticsException("invalid method member access ("+
        			n.getScope().toString()+" is not a class or interface): line "+n.getBeginLine()+
        			", column "+n.getBeginColumn());
    		}
    		ClassSymbol c = (ClassSymbol) t;
    		Symbol s = c.resolve_member(n.getName());
    		if (s == null) {
    			s = c.resolve(n.getName());
    		}
    		if (s == null) {
    			throw new A2SemanticsException("cannot resolve symbol `"+n.getName()
        			+"' within class "+c.getName()+", line "
        			+n.getBeginLine()+", column "+n.getBeginColumn());
    		}
    		if (!(s instanceof MethodSymbol)) {
    			throw new A2SemanticsException("`"+n.getName()+"' is a"
    				+" method member of class "+c.getName()+", line "+
        			n.getBeginLine()+", column "+n.getBeginColumn());
    		}
    		class_name = n.getName();
    		method = (MethodSymbol) s;
    	} else {
    		Symbol s = current_scope.resolve(n.getName());
    		if ((s == null) || !(s instanceof MethodSymbol)) {
    			throw new A2SemanticsException("`"+n.getName()+"' cannot "
    					+"be resolved as a method symbol, line "+n.getBeginLine()
    					+", column "+n.getBeginColumn());
    		}
    		method = (MethodSymbol) s;
    	}
        
        // check method signature
    	List<symtab.Type> signature = new ArrayList<symtab.Type>();
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                signature.add(e.accept(this, arg));
            }
        }
        if (!method.is_defined(signature)) {
        	throw new A2SemanticsException("method `"+method.get_readable_name(signature)+
        			"'"+((n.getScope() == null) ? "" : " with in class "+class_name)+
        			"is not defined, line "+n.getBeginLine()+", column"+n.getBeginColumn());
        }
        return null;
    }

    public symtab.Type visit(ObjectCreationExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        // this checks if the type is defined (in the ClassOrIfceType visitor)
        symtab.Type t = n.getType().accept(this, arg);
        // we ignore ctors for now
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        return t;
    }

    public symtab.Type visit(SuperMemberAccessExpr n, Object arg) {
        return null;
    }

    public symtab.Type visit(UnaryExpr n, Object arg) {
        return n.getExpr().accept(this, arg);
    }

    // we ignore ctor for now
    public symtab.Type visit(ConstructorDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getParameters() != null) {
            for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, arg);
            }
        }
        if (n.getThrows() != null) {
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, arg);
            }
        }
        n.getBlock().accept(this, arg);
        return null;
    }

    public symtab.Type visit(MethodDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        n.getType().accept(this, arg);
        String name = n.getName();
        
        List<symtab.Type> signature = new ArrayList<symtab.Type>();
        
        if (n.getParameters() != null) {
            for (Parameter p : n.getParameters()) {
            	symtab.Type tsym = p.getType().accept(this, arg);
            	signature.add(tsym);
            }
        }
        
    	MethodSymbol method = (MethodSymbol) current_scope.resolve(name);
    	current_scope = (Scope) n.getData();
        
        // we ignore throws for now
        if (n.getThrows() != null) {
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr nameexpr = i.next();
                nameexpr.accept(this, arg);
            }
        }
        if (n.getBody() != null) {
            n.getBody().accept(this, arg);
        }
        current_scope = current_scope.getEnclosingScope();
        return null;
    }

    public symtab.Type visit(Parameter n, Object arg) {
        symtab.Type type = n.getType().accept(this, arg);
        n.getId().accept(this, arg);
        return type;
    }

    public symtab.Type visit(ExplicitConstructorInvocationStmt n, Object arg) {
        if (!n.isThis()) {
            if (n.getExpr() != null) {
                n.getExpr().accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        return null;
    }
    
    // try resolving symbol until ClassScope (represented by a ClassSymbol) is met 
    public Symbol resolve_locally(Scope scope, String name) {
    	Symbol ret = scope.resolve_this_level(name);
    	if (ret != null)
    		return ret;
    	for (Scope parent = scope.getEnclosingScope();
    			(parent != null) && !(parent instanceof ClassSymbol);
    			parent = parent.getEnclosingScope()) {
    		ret = parent.resolve_this_level(name);
    		if (ret != null)
    			return ret;
    	}
    	return null;
    }

    public symtab.Type visit(VariableDeclarationExpr n, Object arg) {
        symtab.Type type = n.getType().accept(this, arg);

        for (VariableDeclarator v : n.getVars()) {
            v.accept(this, type);
        }
        return null;
    }

    public symtab.Type visit(TypeDeclarationStmt n, Object arg) {
        n.getTypeDeclaration().accept(this, arg);
        return null;
    }

    public symtab.Type visit(AssertStmt n, Object arg) {
        n.getCheck().accept(this, arg);
        if (n.getMessage() != null) {
            n.getMessage().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(BlockStmt n, Object arg) {
    	current_scope = (Scope) n.getData();
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
        current_scope = current_scope.getEnclosingScope();
        return null;
    }

    public symtab.Type visit(LabeledStmt n, Object arg) {
        n.getStmt().accept(this, arg);
        return null;
    }

    public symtab.Type visit(EmptyStmt n, Object arg) {
        return null;
    }

    public symtab.Type visit(ExpressionStmt n, Object arg) {
        n.getExpression().accept(this, arg);
        return null;
    }

    public symtab.Type visit(SwitchStmt n, Object arg) {
    	current_scope = (Scope) n.getData();
        n.getSelector().accept(this, arg);
        if (n.getEntries() != null) {
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
        current_scope = current_scope.getEnclosingScope();
        return null;
    }

    public symtab.Type visit(SwitchEntryStmt n, Object arg) {
        if (n.getLabel() != null) {
            n.getLabel().accept(this, arg);
        } else {
        }
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
        return null;
    }

    public symtab.Type visit(BreakStmt n, Object arg) {
        return null;
    }

    public symtab.Type visit(ReturnStmt n, Object arg) {
        if (n.getExpr() != null) {
            n.getExpr().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(EnumDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getImplements() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }

        if (n.getEntries() != null) {
            for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
                EnumConstantDeclaration e = i.next();
                e.accept(this, arg);
            }
        }
        return null;
    }

    public symtab.Type visit(EnumConstantDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        return null;
    }

    public symtab.Type visit(EmptyMemberDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(InitializerDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        n.getBlock().accept(this, arg);
        return null;
    }

    public symtab.Type visit(IfStmt n, Object arg) {
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt() != null) {
            n.getElseStmt().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(WhileStmt n, Object arg) {
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
        return null;
    }

    public symtab.Type visit(ContinueStmt n, Object arg) {
        return null;
    }

    public symtab.Type visit(DoStmt n, Object arg) {
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
        return null;
    }

    public symtab.Type visit(ForeachStmt n, Object arg) {
    	current_scope = (Scope) n.getData();
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
        current_scope = current_scope.getEnclosingScope();
        return null;
    }

    public symtab.Type visit(ForStmt n, Object arg) {
    	current_scope = (Scope) n.getData();
        if (n.getInit() != null) {
            for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        if (n.getCompare() != null) {
            n.getCompare().accept(this, arg);
        }
        if (n.getUpdate() != null) {
            for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        n.getBody().accept(this, arg);
        current_scope = current_scope.getEnclosingScope();
        return null;
    }

    public symtab.Type visit(ThrowStmt n, Object arg) {
        n.getExpr().accept(this, arg);
        return null;
    }

    public symtab.Type visit(SynchronizedStmt n, Object arg) {
        n.getExpr().accept(this, arg);
        n.getBlock().accept(this, arg);
        return null;
    }

    public symtab.Type visit(TryStmt n, Object arg) {
        n.getTryBlock().accept(this, arg);
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, arg);
            }
        }
        if (n.getFinallyBlock() != null) {
            n.getFinallyBlock().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(CatchClause n, Object arg) {
        n.getExcept().accept(this, arg);
        n.getCatchBlock().accept(this, arg);
        return null;
    }

    public symtab.Type visit(AnnotationDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(AnnotationMemberDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }

        n.getType().accept(this, arg);
        if (n.getDefaultValue() != null) {
            n.getDefaultValue().accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(MarkerAnnotationExpr n, Object arg) {
        n.getName().accept(this, arg);
        return null;
    }

    public symtab.Type visit(SingleMemberAnnotationExpr n, Object arg) {
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
        return null;
    }

    public symtab.Type visit(NormalAnnotationExpr n, Object arg) {
        n.getName().accept(this, arg);
        for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i.hasNext();) {
            MemberValuePair m = i.next();
            m.accept(this, arg);
        }
        return null;
    }

    public symtab.Type visit(MemberValuePair n, Object arg) {
        n.getValue().accept(this, arg);
        return null;
    }

    public symtab.Type visit(LineComment n, Object arg) {
        return null;
    }

    public symtab.Type visit(BlockComment n, Object arg) {
        return null;
    }

	@Override
	public symtab.Type visit(MatchStmt n, Object arg) {
		// TODO Auto-generated method stub
        return null;		
	}

	@Override
	public symtab.Type visit(MatchEntryStmt n, Object arg) {
		// TODO Auto-generated method stub
        return null;
	}
}
