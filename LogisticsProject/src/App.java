import CustomDataStructures.CustomStack;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        CustomStack<String> stackname = new CustomStack<>();
        stackname.clear();
        stackname.isEmpty();
        stackname.peek();
        stackname.pop();
        stackname.push(null);
        stackname.size();
        
    }
}
