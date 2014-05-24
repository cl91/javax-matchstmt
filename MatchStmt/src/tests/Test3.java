public class Test3 {

    int a;

    public void foo(boolean x) {
        x = true;
        bar();
        x = true;
    }

    boolean x;

    public void bar() {
        int b;
        {
            i.w = 45;
            b = 1;
        }
        int c = 123;
        {
        }
    }

    public void bar(int abc) {
    }

    private void test() {
        i.hello();
        hello();
        k.hello();
    }

    private Inner i;

    private InnerSub k;

    class Inner {

        public int w;

        public int c;

        public void hello() {
        }
    }

    class InnerSub extends Inner {

        public void hi() {
            w = 123;
        }
    }

    class Another {

        public void bye() {
            i.w = 123;
        }
    }
}
