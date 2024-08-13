## Maze generation algorithms

This project provides Java implementations of more than 35 algorithms for generating so called "perfect mazes" (which are just spanning trees of undirected graphs).

![Maze Demo Application](https://github.com/armin-reichert/mazes/wiki/images/mazedemoapp.png)

The shown demo application can be downloaded [here](https://github.com/armin-reichert/mazes/releases). (Java runtime needed.)

### How to build the library

```
cd <your_root_dir>
git clone https://github.com/armin-reichert/graph.git
cd graph
mvn clean install
cd <your_root_dir>
git clone https://github.com/armin-reichert/mazes.git
cd mazes
mvn clean install
```

### How to build the Swing sample application 

```
cd <your_root_dir>
git clone https://github.com/armin-reichert/mazes-demos.git
cd mazes-demos/SwingMazeDemo
mvn clean install
```

Then you find an executable jar file named `SwingMazeDemo-1.0-jar-with-dependencies.jar` inside the folder `target`.


## Mazes are fun

On the web, many maze generation implementations in all possible programming languages can be found. The popularity of these algorithms probably comes from the fact that mazes and their creation processes are visually appealing and not really difficult to implement. The most popular algorithm seems to be "recursive backtracking" which is random depth-first traversal of a graph. 

On the other hand, there are only a few websites where the whole spectrum of maze creation algorithms is investigated. One prominent example is [this blog](http://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap) where Jamis Buck presents the most popular maze algorithms together with Ruby/Javascript implementations. Reading his blog led myself to investigate this topic too.

<details>
  <summary>And then this guy...</summary>
Some "moderator" at StackOverflow deleted all my answers to user questions about maze generation and set my reputation to zero because I added pointers to this repository. This moron claimed I would do "self promoting". Self promoting what? Free code with an MIT license written by a retired software developer? @StackOverflow moderator: You are a complete idiot!
</details>

[Read more...](https://github.com/armin-reichert/mazes/wiki)
