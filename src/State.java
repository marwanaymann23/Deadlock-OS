import java.util.Scanner;

public class State {
    int processes, resources;
    int[] available;
    int[][] maximum, allocation, need;

    public State(int processes, int resources, int[] available, int[][] maximum, int[][] allocation){
        this.processes = processes;
        this.resources = resources;
        this.maximum = maximum;
        this.allocation = allocation;
        this.need =  new int[processes][resources];
        this.available = available;

        //need  = max - allocated
        for (int i = 0;i<processes;i++){
            for (int j = 0;j<resources;j++){
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }




    }

    public void displayRow(int[] arr){
        for (int x: arr) {
            System.out.print( x + " ");
        }
        System.out.print("\t\t\t");
    }

    public void display(){
        System.out.println("Allocation\t\tNeed\t\tMaximum");
        for (int i = 0; i<allocation.length; i++){
            displayRow(allocation[i]);
            displayRow(need[i]);
            displayRow(maximum[i]);
            System.out.print("\n");
        }

        System.out.println("Available");
        displayRow(available);
        System.out.print("\n");
    }

    // utility function to compare arrays
    public boolean compareArr(int[] arr1, int[] arr2){
        for (int i = 0;i< arr1.length;i++){
            if(arr1[i] > arr2[i]) return false;
        }
        return true;
    }

    public boolean isSafe(){

        boolean[] finish = new boolean [processes];

        //int[] work = available;
        int[] work = new int[resources];
        for(int i = 0; i < resources; ++i)
            work[i] = available[i];;


        boolean found;

        do{
            found  = false;
            for (int i= 0; i<processes; i++){

                //if not finished and need<available
                if(!finish[i] && compareArr(need[i], work)){

                    //move allocated processes to work(available)
                    for (int j=0; j<resources; j++){
                        work[j] += allocation[i][j];
                    }

                    //print work
                    System.out.println("Process " + i + " done");
                    System.out.println("Available: ");
                    displayRow(work);
                    System.out.println("\n");
                    finish[i] = true;
                    found = true;
                    break;
                }
            }

        }while(found);

        for (int i = 0; i<processes; i++){
            if(!finish[i]){
                System.out.println("Not Safe");
                return false;
            }

        }

        System.out.println("Safe");
        return true;
    }

    public void recover(int ID, int[] request) throws Exception {
        int max = 0;
        int current = 0;
        int i = 0;
        int maxProcID = -1;
        for(i = 0; i<processes;i++){
            for(int j = 0; j<resources;j++){
                current+=allocation[i][j];
            }
            if(current>max) {
                max = current;
                maxProcID = i;
            }
            current = 0;
        }

        //free allocation
        for (int j = 0;j<resources;j++){
            available[j]+=allocation[maxProcID][j];
            allocation[maxProcID][j] = 0;
        }

        request(ID, request);


    }

    public void request(int ID, int[] request) throws Exception {
        //invalid id
        if(ID>=processes || ID<0) {
            System.out.println("Invalid Process ID");
            return;
        }

        //if need < request
        if(!compareArr(request,need[ID])) {
            System.out.println("Process exceeded its max claim");
            return;
        }
        // available < request
//        if(!compareArr(request,available)){
//
//        }



        //--------


        //available -= request
        for (int i = 0; i < resources; ++i)
            available[i] -= request[i];

        //allocation += request
        for (int i = 0; i < resources; ++i)
            allocation[ID][i] += request[i];

        //need -= request
        for (int i = 0; i < resources; ++i)
            need[ID][i] -= request[i];

        //check if safe
        if(isSafe()){
            System.out.println("Request Approved");
            display();
            return;
        }else{


            //available -= request
            for (int i = 0; i < resources; ++i)
                available[i] += request[i];

            //allocation += request
            for (int i = 0; i < resources; ++i)
                allocation[ID][i] -= request[i];

            //need -= request
            for (int i = 0; i < resources; ++i)
                need[ID][i] += request[i];

            System.out.println("Request Denied");
            display();

            System.out.println("Perform recovery procedure y/n?");
            Scanner scan = new Scanner(System.in);
            String rec = scan.nextLine();

            if(rec.equals("y")){
                recover(ID, request);
            }


            return ;
        }


    }

    public void release(int ID, int[] release) throws Exception{

        //invalid id
        if(ID>=processes || ID<0) {
            System.out.println("Invalid Process ID");
            return;
        }

        //release> allocation
        if(!compareArr(release,allocation[ID])) {
            System.out.println("Process can not release more than its allocation");
            return;
        }

        //available -= request
        for (int i = 0; i < resources; ++i)
            available[i] += release[i];

        //allocation += request
        for (int i = 0; i < resources; ++i)
            allocation[ID][i] -= release[i];

        //need -= request
        for (int i = 0; i < resources; ++i)
            need[ID][i] += release[i];

        System.out.println("Release Approved");
        display();
        return;
    }

}