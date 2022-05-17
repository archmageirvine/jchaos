Java Chaos
==========

A pure Java implementation of Chaos.

Most of this is very old code, 8-bit graphics technology.

To play the game, do something like:

```
ant release
java -cp /home/sean/jchaos/build.tmp/chaos.jar chaos.Chaos
```

By default it will attempt to run in full-screen exclusive mode.
Press ESC to get out of the game.

There are a few different command line options, discover those with

```
java chaos.Chaos -h
```

There are many things that could be improved, but I do not have the time.
The game play itself is reasonably solid.

Coding Standards
----------------

The principle style reference is the Sun Java style.  The following
extra conditions and modifications apply.

* Indentation is 2 spaces.

* When splitting across lines the operator is carried to the next
  line.

* Member variables match the pattern `m[A-Z][A-Za-z0-9]*`

* Class variables match the pattern `[A-Z][A-Z0-9_]*`

* Abstract classes have names starting with `Abstract`.

