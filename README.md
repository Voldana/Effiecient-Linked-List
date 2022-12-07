# Effiecient-Linked-List
___
### Description
This is the data structure course midterm project held in fall of 2019

The default linked-list is one of the most popular data structures but one of its flaws is that it traverses throught the whole list whenever we need to search, insert or delete a node so it's time complexity is O(n).
The purpose of this project is to design a linked-list that can do all of said activities in O(√n).
___
### How it works
In order to do that we need two seperate lists one that connects each node to the next one and one that connects each node to node that is √n nodes ahead of the first one. We can now use this express list to traverse easier through our main list.