package Amps

import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("classpath:features/all_tests.feature"),
  glue = Array("Amps.steps"),
  plugin = Array("pretty")
)
class AllTestsRunner