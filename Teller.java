// DO NOT ADD NEW METHODS OR DATA FIELDS!

package PJ3;

class Teller {

   // teller id and current customer 
   private int tellerID;
   private Customer currentCustomer;

   // start time and end time of current interval
   private int startTime;
   private int endTime;

   // for keeping statistical data
   private int totalFreeTime;
   private int totalBusyTime;
   private int totalCustomers;

   // Constructor
   Teller()
   {
	    tellerID = 0;
	    startTime=0;
	    endTime = 0;
        totalFreeTime = 0;
        totalBusyTime = 0; 
        totalCustomers = 0;
   }


   // Constructor with teller id
   Teller(int tellerId)
   {
        this.tellerID = tellerId;
        startTime=0;
	    endTime = 0;
        totalFreeTime = 0;
        totalBusyTime = 0;
        totalCustomers  = 0;
   }

   // accessor methods

   int getTellerID () 
   {
	   
	return tellerID;
   }

   Customer getCustomer() 
   {
	return currentCustomer;
   }

   // Transition from free interval to busy interval
   void freeToBusy (Customer currentCustomer, int currentTime)
   {
        // goal  : switch from free interval to busy interval
        //         i.e. end free interval, start busy interval
        //              to serve a new customer
        //
  	// steps : update totalFreeTime
  	// 	   set startTime, endTime, currentCustomer,
  	// 	   update totalCustomers
	       
	   endTime = currentTime;
	   totalFreeTime += (endTime - currentTime);
	   startTime = currentTime;
	   endTime = startTime + currentCustomer.getTransactionTime();
	   this.currentCustomer = currentCustomer;
       totalCustomers ++;
   }

   // Transition from busy interval to free interval
   Customer busyToFree ()
   {
        // goal  : switch from busy interval to free interval
        //         i.e. end busy interval to return served customer,
        //              start free interval
   	// 
  	// steps : update totalBusyTime 
  	// 	   set startTime 
  	//         return currentCustomer
	   totalBusyTime += (endTime - startTime);
       startTime = endTime;
       Customer tempCustomer = currentCustomer;
		this.currentCustomer=null;
		return tempCustomer;
   }

   // Return end busy time, use by priority queue
   int getEndBusyIntervalTime() 
   {
        // add statements
	return endTime; 
   }

   // Use this method at the end of simulation to update teller time
   void setEndIntervalTime (int endsimulationtime, int intervalType)
   {
  	// for end of simulation
  	// set endTime, 
  	// for free interval (0), update totalFreeTime
  	// for busy interval (1), update totalBusyTime
	   endTime = endsimulationtime;
		if (intervalType == 0) {
			totalFreeTime += endTime - startTime;
		}else{
			totalBusyTime += endTime - startTime;
			this.currentCustomer = currentCustomer;
		}
	}
        

   // functions for printing teller's statistics :
   void printStatistics () 
   {
  	// print teller statistics, see project statement
  	System.out.println("\t\tTeller ID                : "+tellerID);
  	System.out.println("\t\tTotal free time          : "+totalFreeTime);
  	System.out.println("\t\tTotal busy time          : "+totalBusyTime);
  	System.out.println("\t\tTotal # of customers     : "+totalCustomers);
  	if (totalCustomers > 0)
  	   System.out.format("\t\tAverage transaction time : %.2f%n\n",(totalBusyTime*1.0)/totalCustomers);
   }

   public String toString()
   {
        return "tellerID="+tellerID+":startTime="+startTime+
               ":endTime="+endTime+">>currentCustomer:"+currentCustomer;
   }

   public static void main(String[] args) {
        // quick check
        Customer mycustomer = new Customer(1,11,8);
  	System.out.println("======================================================");
        System.out.println(mycustomer);
	Teller myteller = new Teller(5);
        myteller.freeToBusy (mycustomer, 13);
        System.out.println(myteller);
        mycustomer=myteller.busyToFree();
  	System.out.println("======================================================");
        System.out.println(myteller);
        System.out.println(mycustomer);
  	System.out.println("======================================================");

   }

};

