package samples

import simplefx.all._
import simplefx.core._
import simplefx.experimental._

object UntangleStarter extends App {
  Untangle.main(null)
}

object Untangle extends App {}
@SimpleFXApp class Untangle {
  val _font = new Font(48)

  def SCENE_WH   = ( 800.0 , 800.0)
  def BACKGROUND = scene.wh * (1.0, 0.87)
  def RADIUS     = 30.0
  def RANRAD     = RADIUS + 0.5
  def SCALEY     = scene.height / 800

  scene         = new Scene(pin,SCENE_WH.x,SCENE_WH.y,new ImagePattern(new Image("/untangle/background.png")))
  lazy val pin  = new Group()
  def randomPos = (RANRAD, RANRAD) + random[Double2] * (BACKGROUND - (RANRAD,RANRAD)*2)

  @Bind var level       = 1
  @Bind var nextLevelIn = 0 s
  @Bind val finished    = <--{edges.forall{x => !x.hasCollision}}
  @Bind var corners     = List.empty[Corner]
  @Bind var edges       = List.empty[Edge  ]

  class Corner extends Circle {
    @Bind var edges = List.empty[Edge]
    @Bind val hasCollision = <--(!edges.forall(!_.hasCollision))
    center        = randomPos
    radius        = RADIUS
    strokeWidth   = 0
    stroke        = Color.BLACK
    fill        <-- (if (hasCollision) new ImagePattern(Image.cached("/untangle/red_button.png"  ))
                                  else new ImagePattern(Image.cached("/untangle/green_button.png")) )
    //Δ(center)   <-- Δ(dragDistance)
    def newPos = prev(center) + Δ(dragDistance)
    center      <-- minmax(RADIUS.to2D, newPos, BACKGROUND - RADIUS.to2D)
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

  class Star (propXY:Double2, mod:Int) extends ImageView ("/untangle/button_star.png") {
    laXY    <-- (scene.wh * propXY)
    fitWH   <-- (scene.wh * (0.045, 0.045))
    visible <-- ((level % 10 >= mod || level % 10 == 0))
  }

  class Pillar (propX:Double, div:Int) extends Rectangle {
    laXY    <-- (scene.wh * (propX , 0.88875 ))
    this.wh <-- (scene.wh * (0.021 , 0.089   ))
    fill      = Color.BLUE
    visible <-- (((level-1) / 10 >= div))
  }

  private var pillarIndex = 0
  @Bind var levelTenIndicators = List ( 0.598, 0.643, 0.688, 0.731 )
  levelTenIndicators.foreach ( xPos => { pillarIndex += 1; pin <++ new Pillar ( xPos, pillarIndex) } )

  private var starIndex = 0
  @Bind var levelOneXpositions = List (0.7595, 0.8070, 0.853 , 0.901 , 0.9485)
  levelOneXpositions.foreach ( xPos => { starIndex += 1; pin <++ new Star ( (xPos, 0.8875), starIndex ) } )
  levelOneXpositions.foreach ( xPos => { starIndex += 1; pin <++ new Star ( (xPos, 0.936 ), starIndex ) } )

  pin <++ new Label { layoutXY <-- scene.wh * (0.3,0.896); font = new Font(48); text <-- level.toString; transform <-- Scale(SCALEY.to2D)}

  pin <++ new Group { children <-- edges  }
  pin <++ new Group { children <-- corners}
  pin <++ new Label {
    transform      <-- Scale(SCALEY.to2D)
    translateXY    <-- scene.wh * (0.49,0.883)
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

  nextFrame --> {
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
}
