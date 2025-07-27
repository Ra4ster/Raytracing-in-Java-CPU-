# Raytracing in Java CPU 📷

This project is a simple implementation of a ray tracing algorithm using Java, designed to render 3D scenes using the CPU.

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## Description

This project demonstrates the process of rendering 3D scenes using a ray tracing algorithm implemented in Java. The program traces rays from the camera to determine the color of pixels by simulating light interaction with objects in the scene.

The core of this project is to explore how ray tracing works on the CPU and to create a basic but functional example of 3D rendering.

## Features

- **Simple Ray Tracing Algorithm**: Renders basic 3D shapes such as spheres and planes.
- **Camera and Viewport**: Supports basic camera positioning and viewing angles.
- **Lighting**: Supports basic light sources that interact with objects.
- **Shading**: Implements simple diffuse and reflective shading models.
- **Java-based**: Written entirely in Java, no external libraries are required.

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Ra4ster/Raytracing-in-Java-CPU-.git
   ```

2. **Navigate to the project folder**:
   ```bash
   cd Raytracing-in-Java-CPU-
   ```

3. **Compile the code**:
   If you are using the terminal, compile the project using:
   ```bash
   javac -d bin src/*.java
   ```

4. **Run the application**:
   Once compiled, run the program with:
   ```bash
   java -cp bin Main
   ```

## Usage

1. After running the program, a rendered 3D image should appear.
2. You can modify the scene by editing the **Scene.java** file, where objects, lights, and camera settings are defined.
3. The project can be expanded by adding more objects, different materials, or more advanced lighting/shading techniques.

## Examples

Here’s an example of how the rendering looks:

![Sample Render](images/sample-render.png)

You can add more examples or use images generated from the project as visual representations here.

## Contributing

We welcome contributions to this project! If you find a bug or want to add a feature, feel free to fork the repository, create a branch, and submit a pull request. Please ensure your code is well-commented and follows the project style guide.

Steps to contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -am 'Add feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Create a new pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
