Stealth
========

Introduction
~~~~~~~~~~~~
Stealth like the name suggests is about a soldier that sneaks behind enemy lines. 
The player plays the soldier.

I used Gitlab for my development. I created issues which the different branches closed.
I have also successfully created the first version (v1.0) which implements all the low bar items.


Shortcuts
~~~~~~~~~
To access the different levels use the ``Z`` key for level two and the ``X`` key for level three.


What has been done
~~~~~~~~~~~~~~~~~~

- **Pathfinding:** The algorithm implemented is Dijkstras. The guards use pathfinding for their patrol routes. They also use path finding when the alarm is sounded and they have to head to where the soldier is.
- **Soldier & Guards:** The soldier and guards with animated movements.
- **Invisibility powerup:** Acquired by walking to the treasure chest(50/ 50 chance)
- **Collision detection:** Bullets and guards, soldier and walls, soldier and guards
- **Weapon:** The soldier has a gun with limited bullets that can be fired using the spacebar key
- **High Score:** On the menu screen there is an option to list the high scores. This tracks the top 10 high scores.
- **Alarm:** An alarm is sounded when you get within four blocks of the guard. The alarm alerts the other guards and goes on for 15secs. There is a 5sec cool down period where the guards don't sound the alarm. This is to allow the player to get away.
- **World:** I have 3 different worlds that constitute the three different levels
- **Lives:** The player starts off with 3 lives. These are lost if the alarm is sounded more than once in a level or the soldier collides with the guards.
- **Score:** The player gains points for completing a level, taking the powerup and killing a guard. The player loses points if they are spotted or if they die
- **Sounds:** There are 3 different sounds implemented. A gunshot sound triggered when the gun is shot. If the gun doesn't have any more bullets a different sound is played. The alarm. This is sounded when the player is spotted and the powerup sound which is played when the powerup is activated


Gameplay
~~~~~~~~
To move the soldier you can use the arrow keys/ ``WASD`` keys. 
The soldier has limited bullets. To fire the bullets use the ``spacebar`` key.

Walking to the treasure chest will allow you to get the invisibility powerup.(50/50 chance.)
To use the powerup press the ``left shift`` key. There should be a sound that plays. 
For the duration of the sound (5 secs) the guards cannot spot you when you get close.
