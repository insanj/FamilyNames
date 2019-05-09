<h3 align="center">
🏰
<br/>
FamilyNames
<br/>
 simple rpg nicknames (bukkit 1.13.2)
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

  <a href="https://github.com/insanj/familynames/releases">
    <img src="https://img.shields.io/badge/🚀-Download%20on%20Github-red.svg" />
  </a>
</p>

<br/>

<img src="https://pbs.twimg.com/profile_images/1205959797/bukkit_400x400.png" width="50" height="50"><a href="https://bukkit.org/threads/familynames.478902/">See the original request on Bukkit Forums.</a>

<br/>

<h2>Commands</h2>

| Command | Arguments | Description |
| --- | --- |
| `/family set <player_name> <family_name>` | `player_name` = name of a player currently logged into the server, `family_name` = string that represents the Family Name to use in chat (should already be composed of a first_name and surname before this point) | Sets the Family Name of a player |
| `/family removep <player_name>` | `player_name` = name of a player | Removes the Family Name for the player |
| `/family add <type> <name>` | `type` = `male_first_name`, `female_first_name`, or `surname`; `name` = name to add | Adds a Family Name to the config |
| `/family remove <type> <name>` | `type` = `male_first_name`, `female_first_name`, or `surname`; `name` = name to remove | Removes a Family Name from the config |


<br/>

<h2>Permissions</h2>

```
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

```

<br/>

<h2>Helpful Links</h2>

- [Bukkit Configuration API Reference](https://bukkit.gamepedia.com/Configuration_API_Reference)
- [Minecraft Formatting Color Codes](https://minecraft.gamepedia.com/Formatting_codes#Color_codes) & [Minecraft Colored Text Generator](https://codepen.io/Rundik/pen/ggVemP)

<br/>

<h2>Authors</h2>

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

