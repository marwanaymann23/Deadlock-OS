import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert number of resources.");
        int resources = scanner.nextInt();
        System.out.println("Insert number of processes.");
        int processes = scanner.nextInt();
        int[] available = new int[resources];

        System.out.println("Insert number of initially available instances of each resource.");
        for (int i = 0; i < resources; i++){
            available[i] = scanner.nextInt();
        }

        int[][] maximum = new int[processes][resources];
        System.out.println("Insert maximum need of each process.");
        for(int i = 0; i < processes; i++){
            for (int j = 0; j < resources; j++){
                maximum[i][j] = scanner.nextInt();
            }
        }

        int[][] allocation = new int[processes][resources];
        System.out.println("Insert allocated resources to each process.");
        for(int i = 0; i < processes; i++){
            for (int j = 0; j < resources; j++){
                allocation[i][j] = scanner.nextInt();
            }
        }

        scanner.nextLine();



        State state = new State(processes, resources, available, maximum, allocation);
        state.isSafe();
        state.display();

        String userInput = scanner.nextLine();
        String[] arguments = userInput.split(" ");
        int pid = Integer.parseInt(arguments[1]);
        int[] request = new int[resources];
        while(!userInput.equalsIgnoreCase("quit")){

            if(arguments.length == resources + 2){
                for (int i = 2; i < arguments.length; i++){
                    request[i - 2] = Integer.parseInt(arguments[i]);
                }
            }
            if(arguments[0].equalsIgnoreCase("RQ")){
                state.request(pid, request);
            }
            else if(arguments[0].equalsIgnoreCase("RL")) {
                state.release(pid, request);
            }
            else{
                System.out.println("Please provide a valid input!");
            }
            userInput = scanner.nextLine();
            arguments = userInput.split(" ");

        }
    }
}