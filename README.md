# Colony
An Educational Evolution Simulator
 * Mac installer - [download .pkg](https://www.mediafire.com/file/tyhgrktxygj2cen/ColonyInstaller-1.0.pkg/file)

Colony lets you easily observe the processes of mutation and natural selection!
The user can vary the temperature, type of food, and abundance of food, and see the effects on the traits of a simulated population.
This is not meant as an accurate scientific demonstration but instead as an accessible way to display the process of evolution!
Feel free to use as an educational tool or just for fun.

## Motivation
Colony was created as my final project submission for Big Bang to Humankind, a class at my university covering elements of astronomy, physics,
chemistry, biology, and anthropology. I was tasked with creating something that demonstrated my synthesis of topics covered in the course and 
decided to demonstrate my knowledge of evolution and genetic mutations.

## Getting Started
### Prerequisites
* Java 14
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
