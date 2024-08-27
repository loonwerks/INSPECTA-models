// #Sireum

package report

import org.sireum._
import org.sireum.hamr.codegen.common.symbols.SymbolTable
import org.sireum.hamr.codegen.common.util.CodeGenConfig
import org.sireum.message.Reporter
import report.Report

object ReadmeGen extends App {

  val runCodegen: B = F
  val replaceReadmes: B = F
  val emitMarkdown: B = T

  val repoRootDir: Os.Path = Os.path(".").up.up.up

  @sig trait Target {
    def name: String

    def producerBehaviorCode: Os.Path
    def consumerBehaviorCode: Os.Path
  }

  @datatype class Microkit(val system: Os.Path,

                           val producerBehaviorCode: Os.Path,
                           val consumerBehaviorCode: Os.Path,

                           val periodicDispatcher: Option[Os.Path],
                           val end_of_schedule_ping: Option[Os.Path]
                          ) extends Target {
    def name: String = "Microkit"
  }

  @sig trait CAmkES extends Target {
    def producerCamkesComponent: Os.Path
    def producerCamkesInfrastructure: Os.Path

    def consumerCamkesComponent: Os.Path

    def camkesRoot: Os.Path

    def jimCounter: Option[Os.Path]
    def jimH: Option[Os.Path]
    def jimC: Option[Os.Path]
  }

  @sig trait Sel4Config extends CAmkES {
    def consumerCamkesInfrastructure: Os.Path
  }

  @datatype class sel4 (val producerBehaviorCode: Os.Path,
                        val consumerBehaviorCode: Os.Path,

                        val producerCamkesComponent: Os.Path,
                        val producerCamkesInfrastructure: Os.Path,

                        val consumerCamkesComponent: Os.Path,
                        val consumerCamkesInfrastructure: Os.Path,

                        val camkesRoot: Os.Path,

                        val jimCounter: Option[Os.Path],
                        val jimH: Option[Os.Path],
                        val jimC: Option[Os.Path]
                       ) extends Sel4Config {
    def name: String = "seL4"
  }

  @datatype class sel4Only(val producerBehaviorCode: Os.Path,
                           val consumerBehaviorCode: Os.Path,

                           val producerCamkesComponent: Os.Path,
                           val producerCamkesInfrastructure: Os.Path,

                           val consumerCamkesComponent: Os.Path,
                           val consumerCamkesInfrastructure: Os.Path,

                           val camkesRoot: Os.Path,

                           val jimCounter: Option[Os.Path],
                           val jimH: Option[Os.Path],
                           val jimC: Option[Os.Path]
                          ) extends Sel4Config {
    def name: String = "seL4_Only"
  }

  @datatype class vm(val producerBehaviorCode: Os.Path,
                     val consumerBehaviorCode: Os.Path,

                     val producerCamkesComponent: Os.Path,
                     val producerCamkesInfrastructure: Os.Path,

                     val consumerCamkesComponent: Os.Path,

                     val camkesRoot: Os.Path,

                     val jimCounter: Option[Os.Path],
                     val jimH: Option[Os.Path],
                     val jimC: Option[Os.Path]
                    ) extends CAmkES {
    def name: String = "vm"
  }

  @datatype class Project(val title: String,
                          val description: Option[String],
                          val projectRootDir: Os.Path,
                          val aadlRootDir: Os.Path,
                          val hamrDir: Os.Path,
                          val air: Os.Path,
                          val targets: ISZ[Target],
                          //val packageName: String,

                          // NOTE: ignore tipe warning that it can't find Cli.SireumHamrCodegenOption
                          //       as the source for that is in the jitpack jar file and thus not
                          //       accessible to tipe
                          val configs: ISZ[Cli.SireumHamrCodegenOption]
                         )


  val dataPort: Project = {
    val projRootDir = repoRootDir / "basic" / "test_data_port_periodic_domains"
    val defaultDirs = Util.getDefaultDirectories(projRootDir)

    Project(
      title = "Data Port Micro-Example",
      description = None(),
      projectRootDir = projRootDir,
      aadlRootDir = defaultDirs.aadlDir,
      hamrDir = defaultDirs.slangDir.up,
      air = defaultDirs.json,
      targets = ISZ(
        populateSeL4Only(
          target = "seL4_Only",
          portType = "data",
          root = projRootDir,
          producerPrefix = "producer_thread_i", producerSuffix = "_producer_producer",
          consumerPrefix = "consumer_thread_i", consumerSuffix="_consumer_consumer",
        ),
        populateMicokit(
          portType = "data",
          root = projRootDir,
          system = "dataport.system"
        ),
        populateSeL4(
          target = "seL4",
          portType = "data",
          root = projRootDir,
          producerPrefix = "producer_thread_i", producerSuffix = "_producer_producer",
          consumerPrefix = "consumer_thread_i", consumerSuffix="_consumer_consumer",
        )
      ),
      configs = ISZ(Util.baseOptions(
        packageName = Some("base"),
        args = ISZ(defaultDirs.json.value),
        outputDir = Some(defaultDirs.slangDir.value),
        aadlRootDir = Some(defaultDirs.aadlDir.value)
      ))
    )
  }

  val eventPort: Project = {
    val projRootDir = repoRootDir / "basic" / "test_event_port_periodic_domains"
    val defaultDirs = Util.getDefaultDirectories(projRootDir)

    Project(
      title = "Event Port Micro-Example",
      description = None(),
      projectRootDir = projRootDir,
      aadlRootDir = defaultDirs.aadlDir,
      hamrDir = defaultDirs.slangDir.up,
      air = defaultDirs.json,
      targets = ISZ(
        populateSeL4Only(
          target = "seL4_Only",
          portType = "event",
          root = projRootDir,
          producerPrefix = "producer_t_i", producerSuffix = "_producer_producer",
          consumerPrefix = "consumer_t_i", consumerSuffix="_consumer_consumer"),
        populateSeL4(
          target = "seL4",
          portType = "event",
          root = projRootDir,
          producerPrefix = "producer_t_i", producerSuffix = "_producer_producer",
          consumerPrefix = "consumer_t_i", consumerSuffix="_consumer_consumer")
      ),
      configs = ISZ(Util.baseOptions(
        packageName = Some("base"),
        args = ISZ(defaultDirs.json.value),
        outputDir = Some(defaultDirs.slangDir.value),
        aadlRootDir = Some(defaultDirs.aadlDir.value)
      ))
    )
  }

  val eventDataPort: Project = {
    val projRootDir = repoRootDir / "basic" / "test_event_data_port_periodic_domains"
    val defaultDirs = Util.getDefaultDirectories(projRootDir)

    Project(
      title = "Event Data Port Micro-Example",
      description = None(),
      projectRootDir = projRootDir,
      aadlRootDir = defaultDirs.aadlDir,
      hamrDir = defaultDirs.slangDir.up,
      air = defaultDirs.json,
      targets = ISZ(
        populateSeL4Only(
          target = "seL4_Only",
          portType = "event",
          root = projRootDir,
          producerPrefix = "producer_t_i", producerSuffix = "_producer_producer",
          consumerPrefix = "consumer_t_i", consumerSuffix="_consumer_consumer"),
        populateSeL4(
          target = "seL4",
          portType = "eventdata",
          root = projRootDir,
          producerPrefix = "producer_t_i", producerSuffix = "_producer_producer",
          consumerPrefix = "consumer_t_i", consumerSuffix="_consumer_consumer")
      ),
      configs = ISZ(Util.baseOptions(
        packageName = Some("base"),
        args = ISZ(defaultDirs.json.value),
        outputDir = Some(defaultDirs.slangDir.value),
        aadlRootDir = Some(defaultDirs.aadlDir.value)
      ))
    )
  }

  val vmProj: Project = {
    val projRootDir = repoRootDir / "vm" / "test_event_data_port_periodic_domains_VM" / "receiver_vm"

    val defaultDirs = Util.getDefaultDirectories(projRootDir)

    Project(
      title = "VM Micro-Example",
      description = None(),
      projectRootDir = projRootDir,
      aadlRootDir = defaultDirs.aadlDir,
      hamrDir = defaultDirs.slangDir.up,
      air = defaultDirs.json,
      targets = ISZ(
        populateVM(
          root = projRootDir,
          producerPrefix = "producer_t_i", producerSuffix = "_producer_producer"
        )
      ),
      configs = ISZ(Util.baseOptions(
        packageName = Some("base"),
        args = ISZ(defaultDirs.json.value),
        outputDir = Some(defaultDirs.slangDir.value),
        aadlRootDir = Some(defaultDirs.aadlDir.value)
      ))
    )
  }


  def h(s: String): Unit = {
    halt(s)
  }

  val projects: ISZ[Project] = ISZ(dataPort, eventPort, eventDataPort, vmProj)

  def main(args: ISZ[String]): Z = {
    run()
    return 0
  }

  def run(): Unit = {
    val reporter = Reporter.create

    var reports: ISZ[Report] = ISZ()

    for(project <- projects) {

      val aadlRootDir = Os.path(project.configs(0).aadlRootDir.get)
      val packageName = project.configs(0).packageName.get

      assert(aadlRootDir.exists)

      for(config <- project.configs) {
        assert (config.aadlRootDir.get == aadlRootDir.value)
        assert (config.packageName.get == packageName)

        println("***************************************")
        println(s"${project.projectRootDir} -- ${config.platform})")
        println("***************************************")

        if (runCodegen) {
          org.sireum.cli.HAMR.codeGen(config, reporter)
        }

        reporter.printMessages()
      }

      if (!reporter.hasError) {
        val readmeLoc = project.projectRootDir / "readme.md"
        val readmeContent = Report.genReport(project, packageName, aadlRootDir, repoRootDir, reporter, emitMarkdown)
        if (!reporter.hasError) {
          if (!readmeLoc.exists || replaceReadmes) {
            Report.overwrite(readmeLoc, readmeContent)
          } else {
            Report.weave(readmeLoc, readmeContent)
          }
        }
      }
    }
  }



  private def populateSeL4(target: String, portType: String, root: Os.Path,
                           producerPrefix: String, producerSuffix: String,
                           consumerPrefix: String, consumerSuffix: String): sel4 = {
    val cDir = root / "hamr" / "c"
    val sel4Dir = root / "hamr" / "camkes-seL4"
    val producer = s"${producerPrefix}${producerSuffix}"
    val consumer = s"${consumerPrefix}${consumerSuffix}"

    val (jimCounter, jimH, jimC): (Option[Os.Path], Option[Os.Path], Option[Os.Path]) = portType match {
      case string"data" =>
        (None(), Some(Util.hackyFind(sel4Dir, "sp_union_art_DataContent.h").get), Some(Util.hackyFind(sel4Dir, "sp_union_art_DataContent.c").get))
      case string"event" => (Some(Util.hackyFind(sel4Dir, "sb_event_counter.h").get), None(), None())
      case string"eventdata" =>
        (Some(Util.hackyFind(sel4Dir, "sb_event_counter.h").get), Some(Util.hackyFind(sel4Dir, "sb_queue_union_art_DataContent_1.h").get), Some(Util.hackyFind(sel4Dir, "sb_queue_union_art_DataContent_1.c").get))
      case _ =>
        h("nope")
        (None(), None(), None())
    }
    sel4(
      producerBehaviorCode = Util.hackyFind(cDir, s"${producer}.c").get,
      consumerBehaviorCode = Util.hackyFind(cDir, s"${consumer}.c").get,

      producerCamkesComponent = Util.hackyFind(sel4Dir, s"${producer}.camkes").get,
      producerCamkesInfrastructure = Util.hackyFind(sel4Dir, s"sb_${producerPrefix}.c").get,

      consumerCamkesComponent = Util.hackyFind(sel4Dir, s"${consumer}.camkes").get,
      consumerCamkesInfrastructure = Util.hackyFind(sel4Dir, s"sb_${consumerPrefix}.c").get,

      camkesRoot = Util.hackyFind(sel4Dir, "top_impl_Instance.camkes").get,

      jimCounter = jimCounter,
      jimH = jimH,
      jimC = jimC)
  }

  private def populateSeL4Only(target: String, portType: String, root: Os.Path,
                               producerPrefix: String, producerSuffix: String,
                               consumerPrefix: String, consumerSuffix: String): sel4Only = {
    val sel4Dir = root / "hamr" / "camkes-seL4_Only"
    val producer = s"${producerPrefix}${producerSuffix}"
    val consumer = s"${consumerPrefix}${consumerSuffix}"

    val (jimCounter, jimH, jimC): (Option[Os.Path], Option[Os.Path], Option[Os.Path]) = portType match {
      case string"data" =>
        (None(), Some(Util.hackyFind(sel4Dir, "sp_int8_t.h").get), Some(Util.hackyFind(sel4Dir, "sp_int8_t.c").get))
      case string"event" => (Some(Util.hackyFind(sel4Dir, "sb_event_counter.h").get), None(), None())
      case string"eventdata" =>
        (Some(Util.hackyFind(sel4Dir, "sb_event_counter.h").get), Some(Util.hackyFind(sel4Dir, "sb_queue_union_art_DataContent_1.h").get), Some(Util.hackyFind(sel4Dir, "sb_queue_union_art_DataContent_1.c").get))
      case _ =>
        h("nope")
        (None(), None(), None())
    }
    sel4Only(
      producerBehaviorCode = Util.hackyFind(sel4Dir, s"producer.c").get,
      consumerBehaviorCode = Util.hackyFind(sel4Dir, s"consumer.c").get,

      producerCamkesComponent = Util.hackyFind(sel4Dir, s"${producer}.camkes").get,
      producerCamkesInfrastructure = Util.hackyFind(sel4Dir, s"sb_${producerPrefix}.c").get,

      consumerCamkesComponent = Util.hackyFind(sel4Dir, s"${consumer}.camkes").get,
      consumerCamkesInfrastructure = Util.hackyFind(sel4Dir, s"sb_${consumerPrefix}.c").get,

      camkesRoot = Util.hackyFind(sel4Dir, "top_impl_Instance.camkes").get,

      jimCounter = jimCounter,
      jimH = jimH,
      jimC = jimC)
  }

  private def populateVM(root: Os.Path,
                         producerPrefix: String, producerSuffix: String): vm = {
    val cDir = root / "hamr" / "c"
    val sel4Dir = root / "hamr" / "camkes-seL4"
    val producer = s"${producerPrefix}${producerSuffix}"
    //val consumer = s"${consumerPrefix}${consumerSuffix}"

    val (jimCounter, jimH, jimC): (Option[Os.Path], Option[Os.Path], Option[Os.Path]) =
      (None(), Some(Util.hackyFind(sel4Dir, "sb_queue_union_art_DataContent_1.h").get), Some(Util.hackyFind(sel4Dir, "sb_queue_union_art_DataContent_1.c").get))

    vm(
      producerBehaviorCode = Util.hackyFind(cDir, s"${producer}.c").get,
      consumerBehaviorCode = Util.hackyFind(sel4Dir, "vmconsumer.c").get,

      producerCamkesComponent = Util.hackyFind(sel4Dir, s"${producer}.camkes").get,
      producerCamkesInfrastructure = Util.hackyFind(sel4Dir, s"sb_${producerPrefix}.c").get,

      consumerCamkesComponent = Util.hackyFind(sel4Dir, "VM_consumer_p_i_consumer.camkes").get,

      camkesRoot = Util.hackyFind(sel4Dir, "top_impl_Instance.camkes").get,

      jimCounter = jimCounter,
      jimH = jimH,
      jimC = jimC)
  }

  def populateMicokit(portType: String, root: Os.Path, system: String): Microkit = {
    val mkDir = root / "microkit"
    return Microkit(
      system = mkDir / system,
      producerBehaviorCode = mkDir / "producer.c",
      consumerBehaviorCode = mkDir / "consumer.c",
      periodicDispatcher = Some(mkDir / "periodic_dispatcher.c"),
      end_of_schedule_ping = Some(mkDir / "end_of_schedule_ping.c")
    )
  }
}
