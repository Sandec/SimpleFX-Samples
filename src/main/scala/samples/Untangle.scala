package samples

import simplefx.all._
import simplefx.core._
import simplefx.experimental._

object Untangle extends App
@SimpleFXApp class Untangle {

  scene         = new Scene(pin,800,800,new ImagePattern(new Image("/untangle/background.png")))
  lazy val pin  = new Group()
  def randomPos = (35.0, 35.0) + random[Double2] * ((800.0,650.0) - (35.0,35.0)*2)

  @Bind var level       = 1
  @Bind var nextLevelIn = 0 s
  @Bind val finished    = <--{edges.forall{x => !x.hasCollision}}
  @Bind var corners     = List.empty[Corner]
  @Bind var edges       = List.empty[Edge  ]

  class Corner extends Circle {
    @Bind var edges = List.empty[Edge]
    @Bind val hasCollision = <--(!edges.forall(!_.hasCollision))
    center        = randomPos
    radius        = 30
    strokeWidth   = 0
    stroke        = Color.BLACK
    fill        <-- (if (hasCollision) new ImagePattern(Image.cached("/untangle/red_button.png"))
                     else new ImagePattern(Image.cached("/untangle/green_button.png")))
    Δ(center)   <-- Δ(dragDistance)
    corners     ::= this
  }

  class Edge(startCorner: Corner, endCorner: Corner) extends Line {
    @Bind val hasCollision = <--(!edges.forall { e2 => !lineIntersection(startEnd,e2.startEnd) })
    start       <-- startCorner.center
    end         <--   endCorner.center
    stroke      <-- (if (hasCollision) Color.RED else Color.SPRINGGREEN)
    strokeWidth   = 10
    edges       ::= this
    startCorner.edges ::= this
    endCorner  .edges ::= this
  }

  pin <++ new Label { layoutXY <-- scene.wh * (0.15,0.894) ; font = new Font(40); text <-- ("           " + level)}
  pin <++ new Group { children <-- edges  }
  pin <++ new Group { children <-- corners}
  pin <++ new Label {
    translateXY      <-- scene.wh * (0.485,0.874)
    font             = new Font(65)
    text           <-- (if(time < nextLevelIn && finished) "" + ((nextLevelIn - time) / second).toInt else "")
    mouseTransparent = true
    textFill         = Color.RED
  }

  def createLevel(level: Int) {
    /* reset everything */
    corners = Nil
    edges   = Nil

    /* add corners */
    for (i <- 1 until level + 4) { new Corner{ center = randomPos} }

    /* add edges  */
    val potentialEdges = for {
      c1 <- corners;
      c2 <- corners if c1 ne c2
    } yield (c1,c2)

    for (line <- potentialEdges) { // add all edges, so none of the added edges collide
      if(edges.forall(edge =>
        !lineIntersection(edge.startEnd,(line._1.center,line._2.center)) &&
          edge.startEnd.swap != (line._1.center,line._2.center)
      )) {
        new Edge(line._1,line._2)
      }
    }
    // Move everything to a random position, so the player has to resort everything
    // (This is the intention of the game)
    corners.foreach { corner => corner.center = randomPos }
  }

  def lineIntersection(l1: (Double2,Double2), l2: (Double2,Double2)): Boolean = {
    val (p1,p2) = l1 ; val (p3,p4) = l2
    val (x1,y1) = p1 ; val (x2,y2) = p2
    val (x3,y3) = p3 ; val (x4,y4) = p4

    val divx = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
    val divy = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)

    if(divx == 0 || divy == 0) return false

    lazy val x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / divx
    lazy val y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / divy

    if(!((x1 < x) && (x < x2) || (x2 < x) && (x < x1))) return false
    if(!((y1 < y) && (y < y2) || (y2 < y) && (y < y1))) return false
    if(!((x3 < x) && (x < x4) || (x4 < x) && (x < x3))) return false
    if(!((y3 < y) && (y < y4) || (y4 < y) && (y < y3))) return false
    if(!(p1 != p3 && p1 != p4 && p2 != p3 && p2 != p4)) return false

    true
  }

  createLevel(level)
  when(finished) ==> {
    nextLevelIn = time + (5 s)
    in(5 s) --> {
      if(finished) {
        level += 1
        createLevel(level)
      }
    }
  }
  when(!finished) ==> { nextLevelIn = time }
}
