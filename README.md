# Colony
An Graphical Evolution Simulator
### Installer Download Links
 * Mac installer (version 1.1.5) - [download .pkg](https://drive.google.com/file/d/1orO6uK1L2k6rGvJMvNMd9crx1e8yGFaQ/view?usp=sharing)
 * Windows installer (version 1.1.5) - [download .msi](https://drive.google.com/file/d/1rIBqBADBgQJ5OCz4ciql4FQKJtiSAmKw/view?usp=sharing)

Once you have downloaded and run the installer, make sure to visit the [user guide](guide/GUIDE.md) for more information.

Colony lets you easily observe the processes of mutation and natural selection!
The user can vary the temperature, type of food, and abundance of food, and see the effects on the traits of a simulated population.
If you are feeling especially malevolent, unleash a virus upon your colony and see how they adapt and evolve.

## Getting Started
If you have downloaded an installer using one of the above links, simply run the installer and then run the application.
See the [user guide](guide/GUIDE.md) for more information.
### Prerequisites
* Java 17
### Installing
Step 1) Navigate to the root directory and run:
```
gradlew jpackage
```

Step 2) This should generate a installer specific to your OS in directory build/your-os-installer.
Run the installer.

Step 3) The Colony application should now be installed on your machine. Run the application and enjoy!
See the [user guide](guide/GUIDE.md) for more information.

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
