import library.PostCodeDTO;
import utils.FileHandler;
import java.util.InputMismatchException;

public class Runner {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler("C:\\IdeaProjects\\DevComp\\DevComp7Source\\src");
        PostCodeDTO data = fileHandler.readDataSerial(args[0]);
        PostCodeExecutor executor = new PostCodeExecutor(data);
        try {
            executor.initSymbols();
            System.out.println("Результат: " + executor.getResult());
        } catch (InputMismatchException e) {
            System.out.println("Неверные аргументы");
        }
    }
}
