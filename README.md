# Colony
An Graphical Evolution Simulator
 * Mac installer (version 1.1) - [download .pkg](https://drive.google.com/file/d/1Em37nU_HboT2yq3neVeLzhNHqcl17klQ/view?usp=sharing)
 * Windows installer (version 1.1) - [download .msi](https://drive.google.com/file/d/1dnnU4HA7V7d6oYw4iigYacjqjgLiqy9r/view?usp=sharing)

Colony lets you easily observe the processes of mutation and natural selection!
The user can vary the temperature, type of food, and abundance of food, and see the effects on the traits of a simulated population.
This is not meant as an accurate scientific demonstration but instead as an accessible way to display the process of evolution!
Feel free to use as an educational tool or just for fun.

## WARNING
### This program has a graphical component that may cause eye strain or seizures in certain viewers. Even if you are not normally susceptible I would recommend switiching off 'Draw Entities' for longer viewing periods.

## Motivation
Colony was created as my final project submission for Big Bang to Humankind, a class at my university covering elements of astronomy, physics,
chemistry, biology, and anthropology. I was tasked with creating something that demonstrated my synthesis of topics covered in the course and 
decided to demonstrate my knowledge of evolution and genetic mutations.

## Getting Started
### Prerequisites
* Java 17
### Installing
Step 1) Navigate to the root directory and run:
```
gradlew jpackage
```

Step 2) This should generate a installer specific to your OS in directory build/your-os-installer.
Run the installer.

## Built With
* [Java](https://www.java.com/en/) - Language the application is built in
* [JavaFX](https://openjfx.io) - Framework for GUI and 2D graphics
* [Intellij](https://www.jetbrains.com/idea/) - IDE used for development
* [Gradle](https://gradle.org/features/) - Build automation tool
* [Badass Runtime Plugin](https://badass-runtime-plugin.beryx.org/releases/latest/) - For creating installers and runtime images
* [Gluon Scene Builder](https://gluonhq.com/products/scene-builder/) - For creating GUI layout
### Dependencies/Libraries
* [JFoenix](https://github.com/sshahine/JFoenix) - For material design UI elements

## Contributors
* [Jasper Geer](https://github.com/jaspergeer)
