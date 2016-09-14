package samples

import javafx.scene.paint.ImagePattern

import simplefx.util.Predef._
import simplefx.core._
import simplefx.all._
import simplefx.experimental._

/* === PlanetSimulator ========================== START ===================================== */
object PlanetSimulator2D extends App
@SimpleFXApp class PlanetSimulator2D {

  /* Declarations of major constants and variables ------------------------------------------ */
  val G            = 30.0                     // Gravity constant.
  val minRadius    = 2                        // Smallest possible radius of a planet.
  val maxRadius    = 10                       // Largest possible radius of a planet.
  val distanceMin  = 100.0                    // Distance of the first Planet to the Sun.
  val distanceIncr = 10.0                     // Distance increment for the planets.
  val planetCount  = 120                     // Number of planets.
  val universe     = new Group                // Where all the Objects are pinned.
  val planets      = new Group {              // Where all the planets are pinned.
    this <++ (new Rectangle{
      layoutXY = (-10000,-10000)
      wh <-- (100000,100000)
      Δ(layoutXY) <-- Δ(universe.dragDistance) * -0.9
      fill = new ImagePattern(new Image("/PlanetSimulator2D/starbackground.png"),
    -10000,-10000,500,500,false)
    })
    Δ(layoutXY) <-- Δ(universe.dragDistance)  // this allows to move the Scene with Dragging
  }
  /* ........................................................................................ */



  /* Pin all Objects onto the Scene-Graph --------------------------------------------------- */
  universe <++ (new Rectangle{ wh <-- scene.wh; fill = Color.TRANSPARENT})
  universe <++ planets
  universe <++ (new Text{text <-- "FPS: " + framerate; fill = Color.WHITE; layoutXY := (50,50)})
  scene = new Scene (universe, 1000, 1000)    // Black background.
  /* ........................................................................................ */

  /* Declare the sun and put it into the group of planets ----------------------------------- */
  val sun = new Planet((500,500),(0,0), 50, new ImagePattern(new Image("/PlanetSimulator2D/sun2.png")))
  /* ........................................................................................ */


  /* Declare all the planets and put them into the group ------------------------------------ */
  (1 to planetCount) foreach { i => // Add all Planets
    val theRadius    = minRadius + random[Double] * (maxRadius - minRadius)
    val direction    = angleToVector(2 * π * random[Double]) // Get a random angle!
    val distance     = i * distanceIncr + distanceMin           // Distance to the sun.
    val acceleration = G * sun.mass / (distance ** 2)            // some Math to keep the planet
    val theSpeed     = orthogonal(direction) * √(acceleration * abs(distance)) // in the orbit

    new Planet (sun.center + direction * distance, theSpeed, theRadius, new ImagePattern(new Image("/PlanetSimulator2D/planets/planet_" + (i % 13) + ".png")))
  }
  /* ........................................................................................ */


  /* Declare the planet-class --------------------------------------------------------------- */
  class Planet(pos: Double2, startSpeed: Double2, rad: Double, image: ImagePattern) extends Circle {
    center = pos
    radius = rad
    fill = image
    @Bind var mass = Bindable(radius ** 3)
    // The constant mass of the planet.
    @Bind var speed = Bindable(startSpeed)
    // The speed is defined through it's Δ.
    @Bind var force = Bindable(0.0, 0.0) // The force is defined through the Σ.

    /* The last distance between planets is the max of the vector-length and their radiuses - */
    def distance(other: Planet) = (center - other.center).length max (this.radius + other.radius)

    @Bind val otherPlanets = <--(planets.children.of[Planet] - this) // All, but me.

    planets <++ this // Add me.


      Δ(center) <-- (speed * (Δ(time) / second)) // delta-distance = speed * delta-time
      Δ(speed) <-- (force * (Δ(time) / second) / mass) // delta-speed    = force * delta-time/mass
      force <-- prev(Σ(otherPlanets, (other: Planet) => {
        // The sum of all results of all elements
        val amount = G * mass * other.mass / (distance(other) ** 2) // of otherPlanets
        val direction = (other.center - center).normalize
        direction * amount
      }))
  }

  /* ........................................................................................ */
}
/* === PlanetSimulator ========================== END ======================================= */
