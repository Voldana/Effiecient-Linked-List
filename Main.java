package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        System.out.println("Enter Your command: 1- Adding new data 2- Removing a certain data 3- Printing the list and 0 for exit" +
                "4- Showing the number of extended nodes" );
        Scanner input = new Scanner(System.in);
        int command = input.nextInt();
        while(command != 0)
            switch (command){
                case 1:
                    System.out.print("Enter new data for adding: ");
                    linkedList.add(input.nextInt());
                    command = input.nextInt();
                    break;
                case 2:
                    System.out.print("Enter the data to remove: ");
                    linkedList.remove(input.nextInt());
                    command = input.nextInt();
                    break;
                case 3:
                    //               System.out.println("Printing");
                    linkedList.printList();
                    command = input.nextInt();
                    break;
                case 4:
                    linkedList.showExtended();
                    command = input.nextInt();
                    break;
                default:
                    System.out.println("Unidentified command please enter your choice again");
                    command = input.nextInt();
                    break;
            }
    }
}


class LinkedList {
    private Node head;
    private ExpressNode expressHead;
    private int size,firstGroupSize,lastGroupSize;
    private int gap;
    LinkedList(){
        expressHead = new ExpressNode(null);
        this.head = null;
        firstGroupSize = 0;
        this.size = 0;
        gap = 0;
    }

    private void rearrangeExpressedLane(int data)throws NullPointerException{
        ExpressNode tempExNode = expressHead;
        Node tempNode = expressHead.getNext();

        while(tempExNode != null) {
            // for when the node ex-line refers to gets removed
            if (tempExNode.getNextEx()!= null && data == tempExNode.getNextEx().getNext().getData()) {
                while(tempNode.getNext() != tempExNode.getNextEx().getNext()){
                    tempNode = tempNode.getNext();
                }
//                tempNode.setNext(tempNode.getNext().getNext());
                tempExNode.getNextEx().setNext(tempNode.getNext().getNext());
                tempExNode = tempExNode.getNextEx().getNextEx();
//                System.out.println("ran");
            }   // for when a normal node is getting removed
            else if(tempExNode.getNext() != null && data > tempExNode.getNext().getData())
                tempExNode = tempExNode.getNextEx();
            else if(tempExNode.getNext() != null && data <= tempExNode.getNext().getData()){
                if(tempExNode.getNext().getNext() == null) {
                    tempExNode.setNext(null);
                    break;
                }
                else {
                    tempExNode.setNext(tempExNode.getNext().getNext());
                    tempExNode = tempExNode.getNextEx();
                }
            }
            /*else {
                if(data > tempExNode.getNext().getData()){
                    tempNode = tempExNode.getNext();
                    while(tempNode.getNext() != null && tempNode.getNext().getData() != data){
                        tempNode = tempNode.getNext();
                    }
                    if(tempNode.getNext() !=null)
                        tempNode.setNext(tempNode.getNext().getNext());
                    while (tempExNode != null){
                        tempExNode.setNext(tempExNode.getNext().getNext());
                        tempExNode = tempExNode.getNextEx();
                    }
                }else
                    tempExNode = tempExNode.getNextEx();*/
        }
        System.out.println("Removed");
        removeNullExNode();
    }   //removing junk expressed nodes

    private void gapDecreased(){    // alittle buggy
        expressHead.setNextEx(null);
        System.out.println("Decreasing gap");
        Node tempNode = expressHead.getNext();
        ExpressNode tempExNode = expressHead;
        tempExNode.setNext(tempNode);
        gap = (int)Math.floor(Math.sqrt(size));
        System.out.println("new gap is:" + gap);
        int counter = 0;
        while(tempNode != null){
            counter++;
            tempNode = tempNode.getNext();
            if(counter == gap ){
                ExpressNode newExNode = new ExpressNode(tempNode);
                tempExNode.setNextEx(newExNode);
                tempExNode = newExNode;
                counter = 0;
            }
        }
    }

    void add(int data){
        size++;
        ExpressNode tempExNode;// = expressHead;
//        System.out.println("Processing");
        Node tempNode = find(data);
        if(tempNode == null){
            System.out.print("|  Not found  |");
            tempNode = new Node(data,head);
            head = tempNode;
            expressHead.setNext(head);
            firstGroupSize++;
            if(size != 1){
                //               System.out.println((int)Math.floor(Math.sqrt(size)));
                if(gap < (int)Math.floor(Math.sqrt(size))){
                    gapIncreased(data);
                }
            }
        }else{
            System.out.print("|  Found  |");
            Node newNode = new Node(data,tempNode.getNext());
            tempNode.setNext(newNode);
//            System.out.println(gap +"--"+ (int)Math.floor(Math.sqrt(size)));
            if(gap < (int)Math.floor(Math.sqrt(size))){
                gapIncreased(data);
            }else {
                tempExNode = expressHead.getNextEx();
                while (tempExNode != null && tempExNode.getNext() != null && tempExNode.getNext().getData() < data ) {
//                    tempExNode.setNext(tempNode.getNext().getNext());
                    tempExNode.setNext(tempExNode.getNext().getNext());
                    tempExNode = tempExNode.getNextEx();
                }
                firstGroupSize ++;
            }
        }
        // Checking if the first grp is getting bigger than the rest
        if(firstGroupSize > gap){
            ExpressNode newExNode = new ExpressNode(head.getNext());
            newExNode.setNextEx(expressHead.getNextEx());
            expressHead.setNextEx(newExNode);
            firstGroupSize = 1;
            System.out.print("| New express added |");
        }
        System.out.print("|  Data Added  | |First group size: " + firstGroupSize + "\n");
    }

    private void gapIncreased(int data){
        boolean found = false;
        int counter = 0;
        gap = (int)Math.floor(Math.sqrt(size));
        System.out.println("Changing gap... new gap = " + gap);
        ExpressNode tempExNode = expressHead.getNextEx();       //testing sth
        Node tempNode;

        if(size == 2)
            firstGroupSize++;

        while(tempExNode != null){
            tempNode = tempExNode.getNext();
            counter++;
            if(tempExNode.getNext().getData() >= data && !found){
                found = true;
                counter--;
            }
            for(int i = 0; i < counter; i++){
                tempNode = tempNode.getNext();
                if(counter == 1 && !found)
                    firstGroupSize++;
            }
            tempExNode.setNext(tempNode);
            tempExNode = tempExNode.getNextEx();
        }

        removeNullExNode();
    }

    private Node find(int data){
        Node tempNode; //= null;
        ExpressNode tempExNode = expressHead;
        removeNullExNode();
        if(head != null && data < head.getData())
            return null;
        while (tempExNode.getNextEx() != null  && tempExNode.getNextEx().getNext().getData() < data){
            tempExNode = tempExNode.getNextEx();
        }
        tempNode = tempExNode.getNext();
        while(tempNode != null && tempNode.getNext() != null && tempNode.getNext().getData() < data){
            tempNode = tempNode.getNext();
        }
        //       System.out.println("this is done");
        return tempNode;
    }

    void remove(int data){
        //Remember to change this one
        ExpressNode tempExNode = expressHead;
        if(data != head.getData()) {
            Node beforeTarget = find(data);
            if(beforeTarget == null){
                System.out.println("Data was not found in the list");
            }else {
                //       beforeTarget.setNext(beforeTarget.getNext().getNext());
                size--;
                if(gap > (int)Math.floor(Math.sqrt(size))){
                    beforeTarget.setNext(beforeTarget.getNext().getNext());
                    gapDecreased();
                }else {
                    rearrangeExpressedLane(data);
                    beforeTarget.setNext(beforeTarget.getNext().getNext());
                    tempExNode = tempExNode.getNextEx();
                    //fixes an exception for some case idc, btw it's 4am rn -__-
                    if(beforeTarget.getNext() == null && firstGroupSize < gap){
                        while(tempExNode != null){
                            if(tempExNode.getNext().getNext() != null)
                                tempExNode.setNext(tempExNode.getNext().getNext());
                            else
                                tempExNode.setNext(null);
                            tempExNode = tempExNode.getNextEx();
                        }
                    }
                }
            }
        }else{
//            System.out.println("The head " + head.getData()+" is getting removed");
            size--;
            if(gap > (int)Math.floor(Math.sqrt(size))){
                expressHead.setNext(head.getNext());
                gapDecreased();
            }else {
                expressHead.setNext(head.getNext());
//            System.out.println("Next to head is: " + head.getNext().getData());
                head = head.getNext();
//            System.out.println("New head is: " + expressHead.getNext().getData());
                tempExNode = tempExNode.getNextEx();
                while (tempExNode != null) {
                    if (tempExNode.getNext().getNext() == null)
                        tempExNode.setNext(null);
                    else {
                        tempExNode.setNext(tempExNode.getNext().getNext());
                    }
                    tempExNode = tempExNode.getNextEx();
                }
            }
            //           rearrangeExpressedLane(head.getData() + 1);
        }
    }

    void printList(){
        System.out.println("Printing...");
        removeNullExNode();
        ExpressNode tempExNode = expressHead;
        Node tempNode = head;
        Node pointer;
        while(tempExNode != null && tempExNode.getNext() != null){
            System.out.print("E");
            pointer = tempExNode.getNext().getNext();
            while(tempExNode.getNextEx() != null && pointer != tempExNode.getNextEx().getNext()){
                System.out.print(" ");
                pointer = pointer.getNext();
            }
            tempExNode = tempExNode.getNextEx();
        }
        System.out.println();
        while(tempNode != null){
            System.out.print(tempNode.getData());
            tempNode = tempNode.getNext();
        }
    }

    void showExtended(){
        int sum = 0;
        ExpressNode temp = expressHead;
        while(temp != null){
            sum++;
            temp = temp.getNextEx();
        }
        System.out.println("Express nodes: " + sum);

    }

    private void removeNullExNode(){
        ExpressNode tempExNode = expressHead;
        while(tempExNode.getNextEx() != null){
            if(tempExNode.getNextEx().getNext() == null){
                tempExNode.setNextEx(null);
                break;
            }
            tempExNode = tempExNode.getNextEx();
        }
    }

}

class Node{
    private int data;
    private Node next;
    /*    public Node(int data){
            this.data = data;
        }*/
    Node(int data,Node next){
        this.data = data;
        this.next = next;
    }

    Node getNext() {
        return next;
    }

    void setNext(Node next) {
        this.next = next;
    }

    int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}

class ExpressNode{
    private Node next;
    private ExpressNode nextEx = null;
    ExpressNode(Node next){
        this.next = next;
    }


    Node getNext() {
        return next;
    }

    void setNext(Node next) {
        this.next = next;
    }

    ExpressNode getNextEx() {
        return nextEx;
    }

    void setNextEx(ExpressNode nextEx) {
        this.nextEx = nextEx;
    }
}
