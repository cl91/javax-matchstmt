package tests;

public class Test1 {

    boolean a;

    Test2 t;

    String[] b = new String[] { "1", "2" };

    public void foo(boolean x) {
        int[][] a = new int[][] {};
        if (true) break;
    }
}

public class Test2 extends String {

    String c = new Test2();

    public class Test3 extends Test2 {
    }

    Test2 d = new Test3();
}
