import java.io.File;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No filepath indicated");
            return;
        }

        String filePath = args[0];
        File file = new File(filePath);
        Parser parser = new Parser();
        parser.parse(file);
        parser.printStats();

    }

}
