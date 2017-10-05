
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
 
 

public class MissCanibal 
{
  
  
 public static void main(String[] args) 
 {
  //used to maintain the explored nodes. 
  //This variable ensures that we do not see a node more than once 
  HashMap<String, Boolean> exploredState = new HashMap<String,Boolean>();
   
  //Queue data structure to implement breadth first search
  Queue<Node> breadthFirstQueue = new LinkedList<Node>();
   
  Node otherSide = new Node(0, 0, 0, null, null, null);
   
  Node initialState = new Node(3, 3, 1, null, "substract", otherSide);
  
  //adding initial state to the explored list.
  breadthFirstQueue.add(initialState);
   
  //if removeAction, boat is going from source to destination
  //if !removeAction, boat is coming from destination to source
  boolean removeAction = true;
   
  //list of actions that can be performed
  ArrayList<String> actions = new ArrayList<String>();
   
  actions.add("101");
  actions.add("201");
  actions.add("111");
  actions.add("011");
  actions.add("021");
   
  MissCanibal m = new MissCanibal();
  m.reachAndPrintGoal(initialState, exploredState, breadthFirstQueue, removeAction, actions);
 }
  
 public void reachAndPrintGoal(Node initialNode, HashMap<String, Boolean> exploredState, Queue<Node> breadthFirstQueue, boolean removeAction, ArrayList<String> actions)
 {
  boolean reachedFinalGoal = false;
   
  Node finalNode = null;
   
  while (!breadthFirstQueue.isEmpty() && !reachedFinalGoal)
  {
   Node node = breadthFirstQueue.poll();
    
   for (String currentAction: actions)
   {
    if (node.getNextArithmetic().equalsIgnoreCase("substract"))
    {
     int missionaries = node.getMissionaries() - Integer.parseInt(currentAction.charAt(0) + ""); 
     int cannibals = node.getCannibals() - Integer.parseInt(currentAction.charAt(1) + "");
     int boat = node.getBoat()- Integer.parseInt(currentAction.charAt(2) + "");
      
     int otherMissionaries = node.getOtherSide().getMissionaries() + Integer.parseInt(currentAction.charAt(0) + ""); 
     int otherCannibals = node.getOtherSide().getCannibals() + Integer.parseInt(currentAction.charAt(1) + "");
      
     if (missionaries == 0 && cannibals == 0)
     {
      Node otherSideNode = new Node(3, 3, 0, null, null, null);
      Node newNode = new Node(0, 0, 0, currentAction, "add", otherSideNode);
      newNode.setParent(node);
      breadthFirstQueue.add(newNode);
       
      finalNode = newNode;
      reachedFinalGoal = true;
      break;
     }
      
     if (missionaries >= 0 && cannibals >= 0 && (missionaries > 0 && (missionaries - cannibals >= 0)) || (missionaries == 0 && cannibals >= 0) && otherMissionaries <= 3 && otherCannibals <= 3)
     {
      if ((otherMissionaries > 0 && (otherMissionaries - otherCannibals >= 0)) || (otherMissionaries == 0 && otherCannibals >= 0))
      {
       String newState = "" + missionaries + cannibals;
       if (!exploredState.containsKey(newState))
       {
        Node otherSideNode = new Node(otherMissionaries, otherCannibals, 1, null, null, null);
         
        Node newNode = new Node(missionaries, cannibals, boat, currentAction, "add", otherSideNode);
        newNode.setParent(node);
        breadthFirstQueue.add(newNode);
       }
      }
       
     }
    }
    else if (node.getNextArithmetic().equalsIgnoreCase("add"))
    {
     int missionaries = node.getMissionaries() + Integer.parseInt(currentAction.charAt(0) + ""); 
     int cannibals = node.getCannibals() + Integer.parseInt(currentAction.charAt(1) + "");
     int boat = node.getBoat() + Integer.parseInt(currentAction.charAt(2) + "");
      
     int otherMissionaries = node.getOtherSide().getMissionaries() - Integer.parseInt(currentAction.charAt(0) + ""); 
     int otherCannibals = node.getOtherSide().getCannibals() - Integer.parseInt(currentAction.charAt(1) + "");
     if (missionaries == 0 && cannibals == 0)
     {
      Node otherSideNode = new Node(3, 3, 0, null, null, null);
      Node newNode = new Node(0, 0, 0, currentAction, "add", otherSideNode);
      newNode.setParent(node);
      breadthFirstQueue.add(newNode);
       
      finalNode = newNode;
      reachedFinalGoal = true;
      break;
     }
      
     if (missionaries <= 3 && cannibals <= 3 && (missionaries > 0 && (missionaries - cannibals >= 0)) || (missionaries == 0 && cannibals >= 0) && otherMissionaries >= 0 && otherCannibals >= 0)
     {
      if ((otherMissionaries > 0 && (otherMissionaries - otherCannibals >= 0)) || (otherMissionaries == 0 && otherCannibals >= 0))
      {
       String newState = "" + missionaries + cannibals;
       if (!exploredState.containsKey(newState))
       {
        Node otherSideNode = new Node(otherMissionaries, otherCannibals, 1, null, null, null);
        Node newNode = new Node(missionaries, cannibals, boat, currentAction, "substract", otherSideNode);
        newNode.setParent(node);
        breadthFirstQueue.add(newNode);
       }
      }
     }
    }
   }
  }
   
  if (finalNode == null)
  {
   System.out.println("cannot find the solution");
  }
  else
  {
	int point=0;
 	String pointerRight="  --->  ";
 	String pointerLeft="  <---  ";
 	String pointer;
 	System.out.println("Starting From Initial State");
   System.out.println("L	     R");
   String finalAnswer="";
   while (finalNode.getParent() != null)
   {
	   		
	   		if(point==0){
	   			pointer=pointerRight;
	   			point=1;
	   		}
	   		else{
	   			pointer=pointerLeft;
	   			point=0;
	   		}
		    String answer="" + finalNode.getMissionaries()+"M/" + finalNode.getCannibals()+"C" + pointer + finalNode.getOtherSide().getMissionaries()+"M/" + finalNode.getOtherSide().getCannibals()+"C \n";
		    finalAnswer=answer+finalAnswer;
		    finalNode = finalNode.getParent();
   }
   
   String answer="" + finalNode.getMissionaries()+"M/" + finalNode.getCannibals()+"C" + "        0M/0C \n";
   finalAnswer=answer+finalAnswer;
   System.out.println(finalAnswer);
   System.out.println("Reached final goal state");
  }
 }
};