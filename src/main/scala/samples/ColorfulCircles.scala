package samples

import simplefx.core._
import simplefx.all._
import simplefx.all.Color._
import simplefx.all.BlendMode._
import simplefx.experimental._

/* === ColorfulCircles ========================== START ============================================================= */
object ColorfulCirclesStarter extends App {
  ColorfulCircles.main(null)
}

object ColorfulCircles extends App
@SimpleFXApp class ColorfulCircles { title = "SimpleFX Sample - ColorfulCircles"

  val THIS = this
  val pin  = new Group		                                        // The Main Group to contain the Scene-Graph.
  val LGR  = d2uGrad("f8bd55","c0fe56","5dfbc1","64c2f8",         // A diagonal gradient, from left Down
                     "be4af7","ed5fc2","ef504c","f2660f")         // to right Up, for the blendmode.

  def frame     = (scene.width, scene.height)                     // The frame of the scene.
  def randomPos = frame * random[Double2]                         // The random XY-position of each circle.

  /* Pin all Objects onto the Scene-Graph --------------------------------------------------------------------------- */
  scene = new Scene (pin, 800, 600, BLACK)

  pin <++ new Group {
    for (i <- 1 to 30) {                                          // Pin 30 circles to the Scene-Graph.
      <++ (new Circle {                                           // Pin this circle.
        definition = (150.0, WHITE^0.05)                          // The radius and the Circle's Color with Opacity.
        strokedef  = (  4.0, WHITE^0.16)                          // Width, Color and Opacity of the Circle's frame.
        layoutXY  := randomPos in (40 s) startAt randomPos        // "Progressive Assignment" of the position,
      })                                                          // position moves from one random-value to
    }                                                             // another random-value during 40 seconds.
    effect = new BoxBlur(10, 10, 3)                               // BoxBlur effect with (width, height, iterations).
  }

  pin <++ new Rectangle{wh<--frame; fill=LGR; blendMode=OVERLAY}  // Create a rectangle with a blendmode-effect
                                                                  // and use this to // overlay all the circles with.
  /* ................................................................................................................ */
}
/* === ColorfulCircles ========================== END =============================================================== */
