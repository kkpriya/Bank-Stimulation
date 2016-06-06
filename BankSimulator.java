package PJ3;

import java.util.*;
import java.io.*;

// You may add new functions or data in this class 
// You may modify any functions or data members here
// You must use Customer, Teller and ServiceArea
// to implement Bank simulator

class BankSimulator {

  // input parameters
  private int numTellers, customerQLimit;
  private int simulationTime, dataSource;
  private int chancesOfArrival, maxTransactionTime;

  // statistical data
  private int numGoaway, numServed, totalWaitingTime;

  // internal data
  private int customerIDCounter;   // customer ID counter
  private ServiceArea servicearea; // service area object
  private Scanner dataFile;	   // get customer data from file
  private Random dataRandom;	   // get customer data using random function

  // most recent customer arrival info, see getCustomerData()
  private boolean anyNewArrival;  
  private int transactionTime;

  // initialize data fields
  private BankSimulator()
  {
	numGoaway = 0;
	numServed = 0;
	totalWaitingTime = 0;
  }

  private void setupParameters()
  {
	// read input parameters
	// setup dataFile or dataRandom
	// add statements
	  Scanner userParameter = new Scanner(System.in);
	  System.out.print("Enter simulation time (positive integer): ");
	     simulationTime = userParameter.nextInt();		     
	 		 
	     System.out.print("Enter maximum transaction time of customers: ");		     
	     maxTransactionTime = userParameter.nextInt();		     
	 		 
	     System.out.print("enter chances (0% < & <= 100%) of new customer: ");		     
	     chancesOfArrival = userParameter.nextInt();		     
	 		 
	     System.out.print("Enter the number of tellers: ");		     
	     numTellers = userParameter.nextInt();		     
	 		 
	     System.out.print("Enter customer queue limit: ");		     
	     customerQLimit = userParameter.nextInt();		     
	 		 
	     System.out.print("Enter 1/0 to get data from file/Random: ");		     
	     dataSource = userParameter.nextInt();		 
	     
	     String fileName = userParameter.nextLine();
	     switch(dataSource){
	     
	     case 0: 
	    	 System.out.println("Random Data File");
	    	 dataRandom = new Random();
	    	 boolean newArrival = ((dataRandom.nextInt(100)+1)<= chancesOfArrival);
	    	 int transactionTime = dataRandom.nextInt(maxTransactionTime)+1;
	    	 break;
	     case 1:
	    	 System.out.println("Enter File name: ");
	    	 fileName = userParameter.nextLine().trim();
	         
	    	 try{
	    		 File file = new File(fileName);
	    		 dataFile = new Scanner(file);
	    	 } catch(FileNotFoundException e){
	    		 e.printStackTrace();
	    	 }
	    	
	     }
	     userParameter.close(); 
  }

  // Refer to step 1 in doSimulation()
  private void getCustomerData()
  {
	// get next customer data : from file or random number generator
	// set anyNewArrival and transactionTime
	// add statements
	  if (dataSource == 1){
		  
			int data1 = dataFile.nextInt();
			int data2 = dataFile.nextInt();

			anyNewArrival = (((data1%100)+1)<= chancesOfArrival);
			transactionTime= (data2%maxTransactionTime)+1;
		}

		else {
			anyNewArrival = ((dataRandom.nextInt(100)+1) <= chancesOfArrival);
			transactionTime = dataRandom.nextInt(maxTransactionTime)+1;  

		}
  }
  

  private void doSimulation()
  {
	System.out.println("\t***  Start Simulation  ***");
	

	// Initialize ServiceArea
	  servicearea = new ServiceArea(numTellers, customerQLimit);

	  System.out.println("Customer #1 to #3 are ready...");
	  System.out.println("\n---------------------------------------------");
	// Time driver simulation loop
  	for (int currentTime = 0; currentTime < simulationTime; currentTime++) {
  		System.out.println("Time " + currentTime);
  		
    		// Step 1: any new customer enters the bank?
    		getCustomerData();
    		
    		if (anyNewArrival) {

      		    // Step 1.1: setup customer data
    			
    			customerIDCounter++;
    			Customer newCustomer = new Customer(customerIDCounter, transactionTime, currentTime);
    			
    			System.out.println("\tCustomer #"+customerIDCounter+" arrives with transaction time "+newCustomer.getTransactionTime()+" units");
      		    // Step 1.2: check customer waiting queue too long?
                    //           customer goes away or enters queue
    			if(servicearea.isCustomerQTooLong()){
					System.out.println("\tCustomer Q is too long: Customer #"+customerIDCounter+" leaves the queue");
					numGoaway++;
				}
				else{
					servicearea.insertCustomerQ(newCustomer);
					System.out.println("\tCustomer #"+customerIDCounter+" waits in the customer queue");
				}
 
    			
    		} else {
      		    System.out.println("\tNo new customer!");
    		}

    		// Step 2: free busy tellers, add to free tellerQ
    		for(int i = 0; i < servicearea.numBusyTellers(); i++){
				//Peek and check if it is still busy, if not remove and add it to free, otherwise do nothing
				Teller newTeller = servicearea.getFrontBusyTellerQ();
				if(newTeller.getEndBusyIntervalTime() <= currentTime) {
					Customer newCustomer;
					newTeller = servicearea.removeBusyTellerQ();
					newCustomer = newTeller.busyToFree();	                              
					System.out.println("\tCustomer #" + newCustomer.getCustomerID() + " is done.");
					servicearea.insertFreeTellerQ(newTeller);
					System.out.println("\tTeller #" + newTeller.getTellerID() + " is free.");
				}
 
			}
    		// Step 3: get free tellers to serve waiting customers 
    		for(int i = 0; i < servicearea.numFreeTellers(); i++){
    			 
				if(servicearea.numWaitingCustomers() != 0) {
					Customer newCustomer = servicearea.removeCustomerQ();
					Teller newTeller = servicearea.removeFreeTellerQ();
					newTeller.freeToBusy(newCustomer, currentTime); 
					servicearea.insertBusyTellerQ(newTeller);
					System.out.println("\tCustomer #" + newCustomer.getCustomerID() + " gets a teller");
					System.out.println("\tTeller #" + newTeller.getTellerID() + " starts serving customer #"+newCustomer.getCustomerID()+" for "+newCustomer.getTransactionTime()+" units.");
					
					numServed++;
					totalWaitingTime = totalWaitingTime + (currentTime - newCustomer.getArrivalTime());
				}
			}
    		
    		System.out.println("\n---------------------------------------------");
  	} // end simulation loop

  	// clean-up
  }

  private void printStatistics()
  {
	// add statements into this method!
	// print out simulation results
	// see the given example in README file 
        // you need to display all free and busy gas pumps
	  System.out.println("   End of simulation report");
		System.out.println("         # total arrival customers :" + customerIDCounter);
		System.out.println("         # customers gone-away     :" + numGoaway);
		System.out.println("         # customers served        :" + numServed);
		System.out.println("\n         *** Current Teller Info. ***");
		System.out.println("\n         # waiting customers       :" + servicearea.numWaitingCustomers());
		System.out.println("         # busy tellers            :" + servicearea.numBusyTellers());
		System.out.println("         # free tellers            :" + servicearea.numFreeTellers());
		System.out.println("\n         Total waiting line        :" + totalWaitingTime);
		System.out.printf("         Average waiting line      :" + (totalWaitingTime)/ (numServed + servicearea.numBusyTellers()));

		System.out.println("\n\n         Busy Tellers Info. :");
		while(servicearea.numBusyTellers()>0){
			Teller teller = servicearea.removeBusyTellerQ();
			teller.setEndIntervalTime ( simulationTime, 1);
			teller.printStatistics();
		}

		System.out.println("         Free Tellers Info. :");
		while(servicearea.numFreeTellers()>0){
			Teller teller = servicearea.removeFreeTellerQ();
			teller.setEndIntervalTime ( simulationTime, 0);
			teller.printStatistics();
		}
  }
  // *** main method to run simulation ****
  public static void main(String[] args) {
   	BankSimulator runBankSimulator=new BankSimulator();
   	runBankSimulator.setupParameters();
   	runBankSimulator.doSimulation();
   	runBankSimulator.printStatistics();
  }

}
