# Space Invaders

## Overview

This project is an implementation of the classic Space Invaders arcade game, built in Java using Gradle as the build system. The game showcases the application of various design patterns to create a modular and maintainable code structure.

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.4 or higher

### Running the Game

To run the game, navigate to the directory containing the `build.gradle` file and execute:

```
gradle clean build run
```

## Architecture

The project implements several design patterns to create a flexible and maintainable architecture:

### Factory Method Pattern

Used to create different types of projectiles in the game. The Factory Method pattern provides an interface for creating objects but allows subclasses to alter the type of objects that will be created.

Key components:
- GameEngine
- Projectile
- ProjectileFactory
- SlowProjectileFactory
- FastProjectileFactory

### Strategy Pattern

Implemented to define different movement behaviors for projectiles. The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable.

Key components:
- Projectile
- ProjectileStrategy
- SlowStraight
- FastStraight
- SlowProjectileFactory
- FastProjectileFactory

### Builder Pattern

Used to construct complex objects like bunkers and aliens. The Builder pattern separates the construction of a complex object from its representation, allowing the same construction process to create different representations.

Key components:
- Bunker
- BunkerDirector
- BunkerBuilder
- DefaultBunkerBuilder
- AlienDirector
- AlienBuilder
- DefaultAlienBuilder

### State Pattern

Implemented to manage different states of the bunker based on damage levels. The State pattern allows an object to alter its behavior when its internal state changes.

Key components:
- Bunker
- BunkerState
- BunkerGreen
- BunkerYellow
- BunkerRed

## Game Features

- Classic Space Invaders gameplay
- Different projectile types with varying speeds
- Destructible bunkers with visual damage states
- Enemy aliens with different behaviors

## Controls

- Left/Right Arrow Keys: Move player ship
- Space: Fire projectile
