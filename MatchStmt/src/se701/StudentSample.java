package se701;

public class StudentSample {

    Object[] objs = { new Window(), new Button(), new RadioBox(), new Menu(), "Fun" };

    public static class Window {

        public void paint() {
            System.out.println("Painting window");
        }
    }

    public static class Button {

        public void set_inactive() {
            System.out.println("Setting button inactive");
        }
    }

    public static class RadioBox {

        public void tune() {
            System.out.println("Tuning");
        }
    }

    public static class Menu {

        public void add_dish() {
            System.out.println("Adding to menu");
        }
    }

    public static void main(String[] arg) {
        Object[] objs = { new Window(), new Button(), new RadioBox(), new Menu(), "Fun" };
        for (int i = 0; i < objs.length; i++) do_stuff(objs[i]);
    }

    public static void do_stuff(Object b) {
        // GENERATED: Match statement
        if (false) {}
        else if (b instanceof Window){
            System.out.println("Do stuff with Window");
            ((Window) b).paint();
        }
        else if (b instanceof StudentSample.Button){
            System.out.println("Do stuff with Button");
            ((StudentSample.Button) b).set_inactive();
        }
        else if (b instanceof RadioBox){
            System.out.println("Do stuff with RadioBox");
            Object obj = new Menu();
            // GENERATED: Match statement
            if (false) {}
            else if (obj instanceof Menu){
                ((RadioBox) b).tune();
                ((Menu) obj).add_dish();
            }
            // END OF MATCH STATEMENT

        }
        else if (b instanceof Menu){
            System.out.println("Do stuff with Menu");
        }
        else {
            System.out.println("Do nothing");
        }
        // END OF MATCH STATEMENT

    }
}
