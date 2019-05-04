


 <h3 align="center">
FamilyNames
<br/>
</h3>

<p align="center">
  <a href="https://github.com/insanj/familynames/releases">
    <img src="https://img.shields.io/github/release/insanj/familynames.svg" />
  </a>

  <a href="https://github.com/insanj/familynames/releases">
    <img src="https://img.shields.io/github/release-date/insanj/familynames.svg" />
  </a>

  <a href="https://github.com/insanj/familynames/">
    <img src="https://img.shields.io/github/languages/code-size/insanj/familynames.svg" />
  </a>

  <br/>

  <a href="https://github.com/insanj/familynames/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/insanj/familynames.svg" />
  </a>

  <a href="https://jdk.java.net/">
    <img src="https://img.shields.io/badge/java-8-yellow.svg" />
  </a>

  <a href="https://getbukkit.org/download/craftbukkit">
    <img src="https://img.shields.io/badge/bukkit-1.13.2-purple.svg" />
  </a>

</p>

<br/>

<h2 align="center"><a href="https://bukkit.org/threads/familynames.478902/">Original Request</a></h2>

 What I would like: This is for a Harry Potter themed server that I am making. When a player joins the server their name changes to a name that has been selected randomly from the config file, first name and a surname they are both randomly generated and when they are they get put together when they are joined they say if they are male or female and that depends their first name, when they join a message pops up and they click either male or female in the chat section and that sets their name (configurable), an example is Steve as a first name and surname Stephen then put them together making Steve_Stephen. This is applied as a nickname and if a player does do -> /nick example <- then It overrides the original name of Steve_Stephen and it removes the Steve and makes Stephen a suffix so: NICKNAME [Stephen] <- and if it hasn’t been nick named then it would be: FIRSTNAMEGENERATED_SURNAMEGENERATED

Example of config file

    #First Names
    #
    Harry
    Henry
    Albert
    Cederic
    Adam
    Oliver
    Dominic
    Elizabeth
    Lucy
    Hermione

    #Surnames and Suffix’s
    #
    Potter &7[&8Potter&7]&f
    Granger &7[&8Granger&7]&f
    Weasley &7[&8Weasley&7]&f
    Malfoy &7[&8Malfoy&7]&f
    Diggory &7[&8Diggory&7]&f
    Patil &7[&8Patil&7]&f
    Longbottom &7[&8Longbottom&7]&f





Ideas for commands:

    Removes a family from config: /Family remove <family name>
    Sets a players family name: /Family set <player> <family name>
    Add’s a family to config: /Family add <family name>
    Removes a player from a family so they don’t have one unless it is set this means they won’t have a suffix and it is removed so they only have their standard Minecraft username: /family removep <player>
    Sets first and last name /family fset <player> <Firstname> <Surname>





Ideas for permissions:

    Family.set
    Family.remove
    Family.add
    Family.removep
    Family.all
    Family.fset



Family.set =gives perm for /family set <player> <Family name>

Family.remove =gives perm for removing a family from config (used in game use)

Family.add =gives permission for adding a family to config (used in game use)

Family.removep =gives perm for removing a player from a family

Family.all =gives permission to all commands

Family.fset =gives permission to fset

<br/>
<h2 align="center">Authors</h2>

```
Julian Weiss
me@insanj.com
github.com/insanj
```


<br/>
<h2 align="center"><a href="https://github.com/insanj/familynames/blob/master/LICENSE">License</a></h2>


```
MIT License

Copyright (c) 2019 Julian Weiss

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

