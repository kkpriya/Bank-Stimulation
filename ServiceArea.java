package PJ3;

import java.util.*;

//--------------------------------------------------------------------------
//
// Define simulation queues in a service area. Queues hold references to Customer 
// and Teller objects
//
// Customer (FIFO) queue is used to hold waiting customers. If the queue is too long
// (i.e. >  customerQLimnit), customer goes away without entering customer queue
//
// There are several tellers in a service area. Use PriorityQueue to 
// hold BUSY tellers and FIFO queue to hold FREE tellers, 
// i.e. a teller that is FREE for the longest time should start be used first.
//
// To handle teller in PriorityQueue, we need to define comparator 
// for comparing 2 teller objects. Here is a constructor from Java API:
//
// 	PriorityQueue(int initialCapacity, Comparator<? super E> comparator) 
//
// For priority queue, the default compare function is "natural ordering"
// i.e. for numbers, minimum value is returned first
//
// User can define own comparator class for PriorityQueue.
// For teller objects, we like to have smallest end busy interval time first.
// i.e. use Teller's getEndBusyIntervalTime()
//
// The following class define compare() for two tellers :

class CompareTellers implements Comparator<Teller>{
	// overide compare() method
 	public int compare(Teller o1, Teller o2) {
		return o1.getEndBusyIntervalTime() - o2.getEndBusyIntervalTime(); 
	}
}

// DO NOT ADD NEW METHODS OR DATA FIELDS
class ServiceArea {

  
  // Private data fields:
  
  // define one priority queue 
  private PriorityQueue <Teller> busyTellerQ;

  // define two FIFO queues
  private Queue<Customer> customerQ;
  private Queue<Teller> freeTellerQ;

  // define customer queue limit
  private int customerQLimit;


  // Constructor 
  public ServiceArea() 
  {
        busyTellerQ = new PriorityQueue <Teller>();
 
  }

  // Constructor 
  public ServiceArea(int numTellers, int customerQlimit)
  {
	// use ArrayDeque to construct FIFO queue objects
	  this.customerQLimit = customerQlimit;
	  freeTellerQ = new ArrayDeque<Teller>(numTellers);
	  customerQ = new ArrayDeque<Customer>(customerQlimit);
	  
	// construct PriorityQueue object
 	// overide compare() in Comparator to compare Teller objects
	busyTellerQ= new PriorityQueue<Teller>( numTellers, 
						new CompareTellers()); 

	// initialize customerQlimit
        // Construct Teller objects and insert into FreeTellerQ
        // assign teller ID from 1, 2,..., numTellers
		int tellerID = 1;
        int i;
        for(i=0; i < numTellers; i++){
        	freeTellerQ.add(new Teller( tellerID + i ));
        }
  }

  // -----------------
  // remove methods
  // -----------------

  public Teller removeFreeTellerQ()
  {
	// remove and return a free teller
	// Add statetments
	return ((ArrayDeque<Teller>) freeTellerQ).pollFirst();
  }

  public Teller removeBusyTellerQ() 
  {
	// remove and return a busy teller
	// Add statetments
	return busyTellerQ.poll();
  }

  public Customer removeCustomerQ()
  {
	// remove and return a customer 
	// Add statetments
	return ((ArrayDeque<Customer>) customerQ).pollFirst();
  }

  // -----------------
  // insert methods
  // -----------------

  public void insertFreeTellerQ(Teller teller)
  {
	  // insert a free teller
	  // Add statetments
	  ((ArrayDeque<Teller>) freeTellerQ).addLast(teller);
  }

  public void insertBusyTellerQ(Teller teller)
  {
	// insert a busy teller
	// Add statetments
	  busyTellerQ.add(teller);
  }

  public void insertCustomerQ(Customer customer)
  {
	// insert a customer 
	customerQ.add(customer);
  }

  // -----------------
  // empty methods
  // -----------------

  public boolean emptyFreeTellerQ()
  {
	// is freeTellerQ empty?
	// Add statetments
	return freeTellerQ.isEmpty();
  }

  public boolean emptyBusyTellerQ()
  {
	// is busyTellerQ empty?
	// Add statetments
	return busyTellerQ.isEmpty();
  }

  public boolean emptyCustomerQ()
  {
	// is customerQ empty?
	// Add statetments
	return customerQ.isEmpty();
  }


  // -----------------
  // size methods
  // -----------------

  public int numFreeTellers()
  {
	// get number of free tellers
	// Add statetments
	return freeTellerQ.size();
  }

  public int numBusyTellers()
  {
	// get number of busy tellers
	// Add statetments
	return busyTellerQ.size();
  }

  public int numWaitingCustomers()
  {
	// get number of customers 
	// Add statetments
	return customerQ.size();
  }

  // -----------------
  // other methods
  // -----------------

  public Teller getFrontBusyTellerQ() 
  {
	// get front of busy tellers
	// "retrieve" but not "remove"
	// Add statetments
	return busyTellerQ.peek();
  }

  public boolean isCustomerQTooLong()
  {
	// is customerQ too long?
	// Add statetments
	return (customerQ.size() == customerQLimit);
  }

  public void printStatistics()
  {
  	System.out.println("\t# waiting customers : "+numWaitingCustomers());
  	System.out.println("\t# busy tellers      : "+numBusyTellers());
  	System.out.println("\t# free tellers      : "+numFreeTellers());
  }

  public static void main(String[] args) {

        // quick check

        // create a ServiceArea and 4 customers
        ServiceArea sc = new ServiceArea(4, 5);
        Customer c1 = new Customer(1,18,10);
        Customer c2 = new Customer(2,33,11);
        Customer c3 = new Customer(3,21,12);
        Customer c4 = new Customer(4,37,13);

        // insert customers into customerQ
  	sc.insertCustomerQ(c1);
  	sc.insertCustomerQ(c2);
  	sc.insertCustomerQ(c3);
  	sc.insertCustomerQ(c4);
	System.out.println(""+sc.customerQ);
        System.out.println("===============================================");
	System.out.println("Remove customer:"+sc.removeCustomerQ());
	System.out.println("Remove customer:"+sc.removeCustomerQ());
	System.out.println("Remove customer:"+sc.removeCustomerQ());
	System.out.println("Remove customer:"+sc.removeCustomerQ());
        System.out.println("===============================================");

        // remove tellers from freeTellerQ
	System.out.println("freeTellerQ:"+sc.freeTellerQ);
        System.out.println("===============================================");
	Teller p1=sc.removeFreeTellerQ();
	Teller p2=sc.removeFreeTellerQ();
	Teller p3=sc.removeFreeTellerQ();
	Teller p4=sc.removeFreeTellerQ();
	System.out.println("Remove free teller:"+p1);
	System.out.println("Remove free teller:"+p2);
	System.out.println("Remove free teller:"+p3);
	System.out.println("Remove free teller:"+p4);
        System.out.println("===============================================");
	System.out.println("freeTellerQ:"+sc.freeTellerQ);
	System.out.println("busyTellerQ:"+sc.busyTellerQ);
        System.out.println("===============================================");


        // insert customers to tellers
        p1.freeToBusy (c1, 13);
        p2.freeToBusy (c2, 13);
        p3.freeToBusy (c3, 13);
        p4.freeToBusy (c4, 13);
	System.out.println("Assign customers to free tellers");

        // insert tellers to busyTellerQ
        System.out.println("===============================================");
	System.out.println("Insert tellers to busyTellerQ");
	sc.insertBusyTellerQ(p1);
	sc.insertBusyTellerQ(p2);
	sc.insertBusyTellerQ(p3);
	sc.insertBusyTellerQ(p4);
	System.out.println("busyTellerQ:"+sc.busyTellerQ);
        System.out.println("===============================================");

        // remove tellers from busyTellerQ
	p1=sc.removeBusyTellerQ();
	p2=sc.removeBusyTellerQ();
	p3=sc.removeBusyTellerQ();
	p4=sc.removeBusyTellerQ();
	System.out.println("Remove busy teller:"+p1);
	System.out.println("Remove busy teller:"+p2);
	System.out.println("Remove busy teller:"+p3);
	System.out.println("Remove busy teller:"+p4);

   }


};

