public interface Bank{
    /**
     * Add a customer
     * @param customerNum - the number of the customer
     * @param maximumDemand - the maximum demand for this customer
     */
    public void addCustomer(int customerNum, int[] maximumDemand);

    /**
     * Output the value of available, maximum, allocation, and need.
     */
    public void getState();

    /**
     * Request resources
     * @param customerNum - the customer requesting resources
     * @param request - the resources being requested
     * @return - true if granting request results in safe state else false
     */
    public boolean requestResources(int customerNum, int[] request);

    /**
     * Release resources
     */
    public void releaseResources(int customerNum, int[] release);
}