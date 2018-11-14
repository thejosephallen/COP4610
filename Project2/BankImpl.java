/**
 * Instructions:
 * Write a program to implement the Banker’s Algorithm as described on pages 
 * 344-345 of your text book. Test the program as specified. Include your program 
 * along with output showing it run correctly for one case where the bank grants 
 * a request by satisfying the safety algorithm (section 7.5.3.1) and for one 
 * case where the bank denies a request since the request does not leave the 
 * system in a safe state.
 * 
 * Date: 11/10/18
 * @author - Joseph Allen
 */
class BankImpl implements Bank{

    private final int maxCustomers = 500;

    int numberOfCustomers;
    int numberOfResources;
    int[] available;
    int[][] maximum;
    int[][] allocation;
    int[][] need;

    public BankImpl(int[] initialResources){
        this.numberOfResources = initialResources.length;
        this.available = initialResources;
        this.maximum = new int[maxCustomers][initialResources.length];
        this.allocation = new int[maxCustomers][initialResources.length];
        this.need = new int[maxCustomers][initialResources.length];
    }

    public void addCustomer(int customerNum, int[] maximumDemand){
        if (this.numberOfCustomers == this.maxCustomers){
            System.out.println("Error: Max number of customers reached.");
            return;
        } else{
            this.maximum[customerNum] = maximumDemand.clone();
            this.need[customerNum] = maximumDemand.clone();
            this.numberOfCustomers++;
        }
    }

    public void getState(){
        System.out.println("Available:");
        for (int i = 0; i < available.length; i++){
            System.out.printf("%d ", this.available[i]);
        }
        System.out.println();

        System.out.println("Maximum:");
        for (int i = 0; i < this.numberOfCustomers; i++){
            for (int j = 0; j < this.numberOfResources; j++){
                System.out.printf("%d ",this.maximum[i][j]);
            }
            System.out.println();
        }

        System.out.println("Allocated:");
        for (int i = 0; i < this.numberOfCustomers; i++){
            for (int j = 0; j < allocation[i].length; j++){
                System.out.printf("%d ",allocation[i][j]);
            }
            System.out.println();
        }

        System.out.println("Need:");
        for (int i = 0; i < this.numberOfCustomers; i++){
            for (int j = 0; j < need[i].length; j++){
                System.out.printf("%d ",need[i][j]);
            }
            System.out.println();
        }
    }

    public boolean requestResources(int customerNum, int[] request){
        
        // Check for valid customer num
        if (customerNum > this.numberOfCustomers){
            System.out.println("Error: That customer does not exist.");
            return false;
        }

        // Check that request is less than need
        for (int i = 0; i < this.numberOfResources; i++) {
            if (request[i] > this.need[customerNum][i]) {
                System.out.println("Error: Request exceeeds maximum need");
                return false;
            }
        }
        // Check that request is satisfiable
        for (int i = 0; i < this.numberOfResources; i++) {
            if (request[i] > this.available[i]) {
                System.out.println("Error: Request exceeds what is available.");
                return false;
            }
        }

        // Simulate granting the request
        int[] safe_available = this.available.clone();
        int[] safe_allocation = this.allocation[customerNum].clone();
        int[] safe_need = this.need[customerNum].clone();
        for (int i = 0; i < this.numberOfResources; i++) {
            this.available[i] -= request[i];
            this.allocation[customerNum][i] += request[i];
            this.need[customerNum][i] -= request[i];
        }

        // Check if resulting state is safe
        boolean[] finished = new boolean[numberOfCustomers];
        int[] work = this.available.clone();
        boolean done = false;
        while (!done){
            done = true;
            for (int i = 0; i < finished.length; i++) {
                if (!finished[i]){
                    boolean needMet = true;
                    for (int j = 0; j < this.numberOfResources; j++) {
                        if (this.need[i][j] > work[j]) needMet = false;
                    }
                    if (needMet){
                        for (int j = 0; j < this.numberOfResources; j++) 
                            work[j] += this.allocation[i][j];
                        finished[i] = true;
                        done = false; 
                    }
                }
            }        
        }

        // Determine if result of simulation leaves bank in safe state
        boolean safe = true;
        for (int i = 0; i < finished.length; i++) {
            if (!finished[i]) safe = false;
        }

        // Update need if safe, else reset state to safety
        if (safe){
            for (int i = 0; i < this.numberOfResources; i++)
                this.need[customerNum][i] = this.maximum[customerNum][i] - this.allocation[customerNum][i];
            return true;
        } else {
            this.available = safe_available;
            this.allocation[customerNum] = safe_allocation;
            this.need[customerNum] = safe_need;
            return false;
        }
    }

    public void releaseResources(int customerNum, int[] release){
        // CTRL+C to release resources
    }
}