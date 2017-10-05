public class Node 
{
 
 private int missionaries;
 private int cannibals;
 private int boat;
  
 private Node parent;
 private Node otherSide;
  
 private String action;
  
 private String nextArithmetic;
 
 public Node(int missionaries, int cannibals, int boat, String action, String nextArithmetic, Node otherSide)
 {
  this.missionaries = missionaries;
  this.cannibals = cannibals;
  this.boat = boat;
  parent = null;
  this.action = action;
  this.nextArithmetic = nextArithmetic;
  this.otherSide = otherSide;
 }
 
 //no implementation in this constructor
 public Node()
 {
   
 }
  
 public int getMissionaries() 
 {
  return missionaries;
 }
 
 public void setMissionaries(int missionaries) 
 {
  this.missionaries = missionaries;
 }
 
 public int getCannibals() 
 {
  return cannibals;
 }
 
 public void setCannibals(int cannibals) 
 {
  this.cannibals = cannibals;
 }
 
 public int getBoat() 
 {
  return boat;
 }
 
 public void setBoat(int boat) 
 {
  this.boat = boat;
 }
 
 public Node getParent() 
 {
  return parent;
 }
 
 public void setParent(Node parent) 
 {
  this.parent = parent;
 }
 
 public String getAction() 
 {
  return action;
 }
 
 public void setAction(String action) 
 {
  this.action = action;
 }
 
 public String getNextArithmetic() 
 {
  return nextArithmetic;
 }
 
 public void setNextArithmetic(String nextArithmetic) 
 {
  this.nextArithmetic = nextArithmetic;
 }
  
 public Node getOtherSide() 
 {
  return otherSide;
 }
 
 public void setOtherSide(Node otherSide) 
 {
  this.otherSide = otherSide;
 }
}
