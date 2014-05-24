package japa.parser.ast.stmt;

import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

public final class MatchEntryStmt extends Statement {

    private final ClassOrInterfaceType id;

    private final BlockStmt block;

    public MatchEntryStmt(int line, int column, ClassOrInterfaceType id, BlockStmt block) {
        super(line, column);
        this.id = id;
        this.block = block;
    }

    public ClassOrInterfaceType getId() {
        return id;
    }

    public BlockStmt getBlockStmt() {
        return block;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}