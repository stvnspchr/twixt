twixt!
=====

# The Game of Twixt

Twixt is board game where players take turns placing pegs on a 24X24 grid. The object of the game is 
to "connect" pegs from opposite sides. Red travels horizontally and blue travels vertically. Only 
the matching color can play in the first or last row or column. If a player places a peg in any 
combination of 2 in one direction and 1 in a perpendicular direction (like the rook in chess) then 
a connector is placed on the board connecting the two pegs. You cannot place pegs on the same spot, 
and connectors are not allowed to overlap. You are allowed to move anywhere on the board at any time.

Created for a CS class project circa 2007.

## How The Program Works

I used a main class called Twixt which does basically all of the operations.  Another class called TwixtCell is a child class of JLabel.  It is used to create the 24x24 array of board “cells” or pegs.  The methods in this class are used to place a peg in Twixt’s computed location.

## Incomplete In-Game
	
This program proved very difficult and tedious. There were a few things that I did not get to work completely.  Overlapping the graphics interface and a GUI environment was very complicated to grasp and implement. All of the processing for placing connectors is complete except for a minor combination of peg placements, which shouldn’t occur too often. Despite the failed graphics implementation, the basic operation of the game works fine.

Another part that is not complete is declaring a winner. For a simple path, my implementation works; with more complicated paths, the algorithm begins to fail. I save the vertexes in a 576x576 array and if a vertex is connected to more than one other vertex, it only returns the lowest vertex and continues to check from there. If peg 4 is connected to 26 and 30, but then 30 continues on to form a complete path, there will be no winner because my implementation only checks that 26 is not connected to another.
	
Another part that is not complete is saving and opening an incomplete game. You are able to start a new game, but an incomplete game does not work. I was attempting to have it save the array of pegs and connecters while saving, and then recall these and place them on the board upon opening a saved game.This didn’t all get finished and/or work.



