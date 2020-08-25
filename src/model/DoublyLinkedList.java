package model;

public class DoublyLinkedList {  
	  
    //Represent a node of the doubly linked list  
  
    public class Node{  
        int data;  
        Node previous;  
        Node next;  
        Integer visitingDegree;
  
        public Integer getVisitingDegree() {
			return visitingDegree;
		}

		public void setVisitingDegree(Integer visitingDegree) {
			this.visitingDegree = visitingDegree;
		}

		public Node(int data) {  
            this.data = data;  
        }  
        
        public int getData() {
        	return this.data;
        }

		public Node getPrevious() {
			return previous;
		}

		public Node getNext() {
			return next;
		}

		@Override
		public String toString() {
			return "Node [data=" + data + ", visitingDegree=" + visitingDegree + "]";
		}
        
        
    }  
  
    //Represent the head and tail of the doubly linked list  
    Node head, tail = null;  
  
    //addNode() will add a node to the list  
    public void addNode(int data, Integer visitingDegree) { 
        //Create a new node  
        Node newNode = addNode(data);  
        newNode.setVisitingDegree(visitingDegree);
    }  
    
    public Node addNode(int data) {  
    	 //Create a new node  
        Node newNode = new Node(data);  
        
        //If list is empty  
        if(head == null) {  
            head = tail = newNode;  
            head.previous = null;  
            tail.next = null;  
        }  
        else {  
            tail.next = newNode;  
            newNode.previous = tail;  
            tail = newNode;  
            tail.next = null;  
        }  
        return newNode;    	
    }
    
    public void removeLast() {
    	int delId;
    	if(tail == null) {
    		//System.out.println("No node to delete...");
    		return;
    	}else if(head == tail) {
    		delId = tail.data;
    		head = tail = null;
    	}else {
    		delId = tail.data;
    		Node tmp = tail;
    		tail = tail.previous;   
        	tail.next = null;
        	tmp = null;
    	}
		/*
		 * System.out.println(" current nodes "+this);
		 * System.out.println("removed Node "+delId);
		 */
    	
    }
  
    //display() will print out the nodes of the list  
    public void printNode() {  
        //Node current will point to head  
        Node current = head;  
        if(head == null) {  
            //System.out.println("List is empty");  
            return;  
        }  
        //System.out.println("Nodes of doubly linked list: ");  
        while(current != null) {  
            //Prints each node by incrementing the pointer.  
            current = current.next;  
        }  
    }
    public int size() {
    	int size = 0;
    	 Node current = head;  
         if(head == null) {  
             //System.out.println("List is empty");  
             return 0;  
         } 
         while(current != null) {
        	 size++;
             current = current.next;  
         }  
		return size;
    }
    
	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	@Override
	public String toString() {
		return "DoublyLinkedList [head=" + head + ", tail=" + tail + "]";
	}
	

	public void removeNode(Integer nodeData) {
		//Node current will point to head  
        Node current = head; 
        if(head == null) {  
            //System.out.println("List is empty");  
            return;  
        }  
        while(current != null) {  
        	if(current.getData() == nodeData) {
        		break;
        	}
            current = current.next;  
        }  
        if(current == head) {
        	head = head.next;
        	if(head != null) {
        		head.previous = null;
        	}
        	current = null;
        }else if(current == tail) {
        	tail = current.previous;
        	if(head != null) {
        		tail.next = null;
        	}
        	
        	current = null;        	
        }else {
        	current.previous.next = current.next;
        	current.next.previous = current.previous;
        	current = null;
        }
        
	}
	
	/**
     * this method walks backward through the linked list
     */
    public void iterateBackward(){
         
        //System.out.println("iterating backword..");
        Node tmp = tail;
        while(tmp != null){
           // System.out.println(tmp.getData());
            tmp = tmp.previous;
        }
    }
    
    
	
    
}
