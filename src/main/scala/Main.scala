import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.{Scene, Group}
import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color
import scala.util.Random
import scalafx.animation.AnimationTimer

object Main extends JFXApp3 {

  val random = new Random()

  class Particle {
    var x: Double = random.nextDouble() * 1000
    var y: Double = random.nextDouble() * 1000
    var dx: Double = (random.nextDouble() - 0.5)*2
    var dy: Double = (random.nextDouble() - 0.5)*2
    val radius: Double = 3
    val color: Color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))

    def move(): Unit = {
      x += dx
      y += dy
    }
  }

  val particles = for (_ <- 1 to 2000) yield new Particle

  val particleGroup = new Group {
    children = particles.map { particle =>
      new Circle {
        centerX = particle.x
        centerY = particle.y
        radius = particle.radius
        fill = particle.color
      }
    }
  }

  val animationTimer: AnimationTimer = AnimationTimer(t => {
    for (particle <- particles) {
      particle.move()

      if (particle.x < 0) particle.x = 1000
      if (particle.x > 1000) particle.x = 0
      if (particle.y < 0) particle.y = 1000
      if (particle.y > 1000) particle.y = 0

      for (other <- particles if other != particle) {
        val dx = particle.x - other.x
        val dy = particle.y - other.y
        val distance = Math.sqrt(dx*dx + dy*dy)
        if (distance < particle.radius + other.radius) {
          particle.dx = (random.nextDouble() - 0.5) * 2
          particle.dy = (random.nextDouble() - 0.5) * 2

          val overlap = (particle.radius + other.radius) - distance
          val shiftX = overlap * dx / (2 * distance)
          val shiftY = overlap * dy / (2 * distance)
          particle.x += shiftX
          particle.y += shiftY
          other.x -= shiftX
          other.y -= shiftY
        }
      }
    }
    particleGroup.children = particles.map { particle =>
      new Circle {
        centerX = particle.x
        centerY = particle.y
        radius = particle.radius
        fill = particle.color
      }
    }
  })

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Particle Simulator"
      scene = new Scene(2000, 2000) {
        content = particleGroup
      }
    }

    animationTimer.start()
  }
}
