package samples

import java.util.Random

import com.interactivemesh.jfx.importer.ImportException
import com.interactivemesh.jfx.importer.obj.ObjModelImporter
import simplefx.core._
import simplefx.all._
import simplefx.experimental._
import simplefx.util.Predef._

/* === PlanetSimulator ========================== START ============================================================= */
object PlanetSimulator3D extends App
@SimpleFXApp class PlanetSimulator3D {

  /* Declarations of major constants and variables ------------------------------------------------------------------ */
  val G               = 30.0                                     // Gravity constant.
  val minRadius       = 3                                        // Smallest possible radius of a planet.
  val maxRadius       = 12                                       // Largest  possible radius of a planet.
  val distanceMin     = 100.0                                    // Distance of the first Planet to the Sun.
  val distanceIncr    = 10.0                                     // Distance increment for the planets.
  val planetCount     = 60                                       // Number of planets.
  val distinctPlanets = 11                                       // Number of distinct planet models
  val rand            = new Random()
  val surface2D       = new Group                                // The 2D Surface which is used for Dragging.
  val planets         = new Group                                // Where all the planets, except the sun, are pinned.
  val universe        = new SubScene(planets, 1000, 1000, true, SceneAntialiasing.DISABLED) {
    expansion    <-- scene.wh                                 // The 3D Scene has the same size as the surface has.
  }                                                           // It is bound and therefore automatically updated.
  /* ................................................................................................................ */


  /* Pin all Objects onto the Scene-Graph --------------------------------------------------------------------------- */
  surface2D <++ new Rectangle{ wh <-- scene.wh}               // Detect all mouse events.
  universe  <++ planets                                       // Puts all the planets into the universe.
  surface2D <++ new Text{text <-- "FPS: " + framerate; fill = Color.WHITE; layoutXY := (50,50)}
  surface2D <++ universe                                      // Puts the universe onto the 2D Surface.
  scene = new Scene (surface2D, 1000, 1000, Color.BLACK)      // Creates the overall Scene, with a black background.
  /* ................................................................................................................ */


  /* Define the dragging functionality ------------------------------------------------------------------------------ */
  @Bind var rotateMatrix = Transform.IDENTITY                 // Declares the rotation, and
  rotateMatrix <-- (prev(rotateMatrix)                        // binds it to the drag-distance.
            * Rotate(Δ(surface2D.scrollDistance)/(5,-5)))       // The constant (5, -5) to adjust usability.

  universe.camera = new PerspectiveCamera(true) {
    farClip     = Double.MaxValue                             // Look at the end of universe.
    transform <-- rotateMatrix * Translate(0, 0, zTrans)      // Transform to react to the scrolling-events.
  }

  def zTrans = surface2D.dragDistance.y * 5 - 3000          // Using the scroll-wheel for the z-transform.
                                                              // The constants "5" and "-2000" to adjust usability.
  /* ................................................................................................................ */


  /* Declare the sun and put it into the group of planets ----------------------------------------------------------- */
  val sun = new Planet("sun",(0,0,0),(0,0,0), 50)
  /* ................................................................................................................ */


  /* Declare all the planets and put them into the group ------------------------------------------------------------ */
  for(i <- 1 to planetCount) {                                // Add all Planets.
    val randomSize   = random[Double] * (maxRadius-minRadius) // The variable/random part of the radius-size.
    val theRadius    = minRadius + randomSize                 // The radius used equals the minimum plus the variable.
    val direction2D  = angleToVector(2 * π * random[Double])  // Get a random angle!
    val direction    = (direction2D.x, 0.0, direction2D.y)    // Convert the angle to 3D.
    val distance     = i * distanceIncr + distanceMin         // Distance to the sun.
    val acceleration = G * sun.mass / (distance ** 2)         // Some Math to keep the planet in the orbit.
    val theSpeed2D   = orthogonal(direction2D) * √(acceleration * abs(distance))  // Standard physics - √ = square-root.
    val theSpeed     = (theSpeed2D.x, 0.0, theSpeed2D.y)      // Convert the speed to 3D.
    val randZOffset  = (0.0, (random[Double]*2-1) * 50, 0.0)  // Random offset between -50 and 50.
    val id           = rand.nextInt(distinctPlanets-1)+1
    new Planet ("planet_"+id,direction * distance + randZOffset, theSpeed, theRadius)
  }
  /* ................................................................................................................ */


  /* Declare the planet-class --------------------------------------------------------------------------------------- */
  class Planet(name: String, pos: Double3, startSpeed: Double3, scale: Double) extends Group {

    /* The last distance between planets is the max of the vector-length and their radiuses */
    def distance (other: Planet) = (center - other.center).length max (this.radius + other.radius)
    def amount   (other: Planet) = G * mass * other.mass / (distance(other) ** 2)
    def direction(other: Planet) = (other.center - center).normalize

    children <-- importObj(name)

    scaleXYZ = scale.to3D

    translateXYZ  = pos

    @Bind var radius       =      scale
    @Bind var center       = <--  (translateXYZ)
    @Bind var mass         =      radius ** 3                         // The constant mass of the planet.
    @Bind var speed        =      startSpeed                          // The speed is defined below through it's Δ.
    @Bind var force        =     (0.0 , 0.0 , 0.0)                    // The force is defined below through the Σ.
    @Bind val otherPlanets = <-- (planets.children.of[Planet] - this) // All, but me.

    Δ(translateXYZ) <-- (speed * (Δ(time) / second))                  // delta-distance = speed * delta-time
    Δ(speed )       <-- (force * (Δ(time) / second) / mass)           // delta-speed    = force * delta-time/mass
    force           <-- prev(Σ(otherPlanets,(other:Planet) => {       // The sum of all results of all elements
      direction(other) * amount(other)                                // of otherPlanets.
    }))

    ++> (planets)                                                     // Add me.
  }

  def importObj(filename:String):List[MeshView] = {
    val importer = new ObjModelImporter

    try {
      val modelUrl = this.getClass.getResource("/samples/planets3d/" + filename+".obj");
      importer.read(modelUrl);
    }
    catch {
      case ie: ImportException => ie.printStackTrace()
    }

    importer.getImport.toList.map(x => {
      x.setMaterial( new PhongMaterial{
        diffuseMap = Image.cached("/samples/planets3d/"+filename+".png")
      })
      x
    })
  }
  /* ................................................................................................................ */
}
/* === PlanetSimulator ========================== END =============================================================== */