**Overview**

Tank Commander is a project that I made so I can learning Java and practice more object-oriented code. I wanted to learn to use variables, expressions, conditionals, loops, and methods in a game.

Tank Commander is a game that is run in a console where the player drives a tank through five encounters (enemy tanks). The player can fire the cannon, repair damage, scan an enemy, or retreat. Ammo and repair kits are limited, attacks have a chance to cause critical damage, and surviving enemies fire back. There are scout, medium, and heavy tanks with different challenges. The game tracks enemies destroyed and shows the mission results.

I used inheritance in this project through a `Tank` class and two subclasses, `PlayerTank` and `EnemyTank`. Both of the subclasses use their own `attack()` behavior. An `ArrayList<EnemyTank>` stores the enemies, while loops and conditionals control the encounters. I was able to get practice with sharing behavior between classes, working on the object state, and checking user input.

I added comments in the files to explain the reasons behind the design choices I made.

Youtube Link: https://youtu.be/bZtfJEMN414

**Development Environment**

I compiled and tested on Windows 11 using PowerShell and the Java Development Kit (JDK) 21. The `javac` compiler builds the program, and the `java` command runs it. I also made a `Play.bat` launcher for Windows just so its easy to play for those who want to give it a shot.

The software is written in Java and uses only its standard library:

- java.util.Scanner reads the commander's name and menu selections.
- java.util.ArrayList stores the enemy tanks for the campaign.
- java.util.Random determines whether a cannon shot is a critical hit.

To compile and run the game, you can open a terminal in the folder containing the four Java files and enter:

```
javac -encoding UTF-8 Tank.java PlayerTank.java EnemyTank.java Game.java
java Game
```

On Windows, you can also double-click `Play.bat` with a JDK installed.

**Useful Websites**

- [Oracle Java Tutorial: Abstract Methods and Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html) — explains abstract classes and how subclasses implement abstract methods.
- [Java ArrayList Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html) — describes the collection used to store the enemy tanks.
- [Java Scanner Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html) — explains reading and processing console input.
- [Java Random Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html) — documents the random number generation used for critical hits.

**Future Work**

- Allow the player to control multiple allied tanks, each with its own health, ammunition, and abilities.
- Add save and load functionz and store any high scores between sessions.
- add more enemy behaviors, difficulty settings, and encounter types, then add more to the combat balance with more gameplay testing.
