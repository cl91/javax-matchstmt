javax-matchstmt
===============

A source-to-source Java compiler which implments standard Java with added feature --- typed pattern matching.

The compiler itself is also written in Java. It uses javacc to generate the tokenizer and the parser, and installs customized hooks to the generated parser tree to perform semantics analysis.


Documentation
=============

For a complete design report see ./docs/report.pdf.


How to Run the Compiler
=======

To run the project, import the directory ./MatchStmt to eclipse and run A2MainRunner.java. The compiler will try to compile ./MatchStmt/src/se701/StudentSample.javax. The compiled code will be written to ./MatchStmt/src/se701/StudentSample.java.
