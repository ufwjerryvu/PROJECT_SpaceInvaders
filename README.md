# Space Invaders
## University of Sydney SOFT2201 Semester 2 2023 (SID: 510156033)
Hello tutor, welcome to my space invaders game.

Use the `gradle clean build run` command in the directory with the `build.gradle` file and `src` folder to run the game. This game was coded in Java v.17 and built using gradle 7.4 so it should work nicely. 

## Classes associated with each design pattern
### Factory method
- **GameEngine** (dir: `src\main\java\invaders\engine\GameEngine.java`)
- **Projectile** (dir: `src\main\java\invaders\entities\Projectile.java`)
- **ProjectileFactory** (dir: `src\main\java\invaders\entities\factories\ProjectileFactory.java`)
- **SlowProjectileFactory** (dir: `src\main\java\invaders\entities\factories\SlowProjectileFactory.java`)
- **FastProjectileFactory** (dir: `src\main\java\invaders\entities\factories\FastProjectileFactory.java`)
  
### Strategy pattern
- **Projectile** (dir: `src\main\java\invaders\entities\Projectile.java`)
- **ProjectileStrategy** (dir: `src\main\java\invaders\entities\strategies\ProjectileStrategy.java`)
- **SlowStraight** (dir: `src\main\java\invaders\entities\strategies\SlowStraight.java`)
- **FastStraight** (dir: `src\main\java\invaders\entities\strategies\FastStraight.java`)
- **SlowProjectileFactory** (dir: `src\main\java\invaders\entities\factories\SlowProjectileFactory.java`)
- **FastProjectileFactory** (dir: `src\main\java\invaders\entities\factories\FastProjectileFactory.java`)
  
### Builder pattern
- **Bunker** (dir: `src\main\java\invaders\entities\Bunker.java`)
- **BunkerDirector** (dir: `src\main\java\invaders\entities\builders\BunkerDirector.java`)
- **BunkerBuilder** (dir: `src\main\java\invaders\entities\builders\BunkerBuilder.java`)
- **DefaultBunkerBuilder** (dir: `src\main\java\invaders\entities\builders\DefaultBunkerBuiler.java`)
- **AlienDirector** (dir: `src\main\java\invaders\entities\builders\AlienDirector.java`)
- **AlienBuilder** (dir: `src\main\java\invaders\entities\builders\AlienBuilder.java`)
- **DefaultAlienBuilder** (dir: `src\main\java\invaders\entities\builders\DefaultAlienBuilder.java`)

### State pattern
- **Bunker** (dir: `src\main\java\invaders\entities\Bunker.java`)
- **BunkerState** (dir: `src\main\java\invaders\entities\states\BunkerState.java`)
- **BunkerGreen** (dir: `src\main\java\invaders\entities\states\BunkerGreen.java`)
- **BunkerYellow** (dir: `src\main\java\invaders\entities\states\BunkerYellow.java`)
- **BunkerRed** (dir: `src\main\java\invaders\entities\states\BunkerRed.java`)
