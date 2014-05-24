package japa.parser.ast.stmt;

import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.MatchEntryStmt;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

import java.util.List;

public final class MatchStmt extends Statement {

    private final String id;

    private final List<MatchEntryStmt> entries;

    public MatchStmt(int line, int column, String id, List<MatchEntryStmt> entries) {
        super(line, column);
        this.id = id;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public List<MatchEntryStmt> getEntries() {
        return entries;
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