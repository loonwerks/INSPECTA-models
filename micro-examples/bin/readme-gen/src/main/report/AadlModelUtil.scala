// #Sireum
package report

import org.sireum._
import org.sireum.cli.HAMR
import org.sireum.hamr.codegen.common.properties.PropertyUtil
import org.sireum.hamr.codegen.common.symbols.{AadlThread, GclAnnexClauseInfo, SymbolResolver, SymbolTable, SymbolUtil}
import org.sireum.hamr.codegen.common.transformers.Transformers
import org.sireum.hamr.codegen.common.types.TypeResolver
import org.sireum.hamr.ir
import org.sireum.hamr.ir.{JSON, MsgPack}
import org.sireum.message.{FlatPos, Position, Reporter}

object AadlModelUtil {
  def getGumboSubclausePos(g: GclAnnexClauseInfo, aadlDir: Os.Path): Position = {
    val p: Position = // find 'nearest' posOpt to the start of the subclause
      if (g.getAnnex.state.nonEmpty)
        g.getAnnex.state(0).posOpt.get
      else if (g.getAnnex.methods.nonEmpty)
        g.getAnnex.methods(0).getMethod.posOpt.get
      else if (g.getAnnex.invariants.nonEmpty)
        g.getAnnex.invariants(0).getPosOpt.get
      else if (g.getAnnex.getInitializes.nonEmpty)
        if (g.getAnnex.getInitializes.get.modifies.nonEmpty)
          g.getAnnex.getInitializes.get.modifies(0).posOpt.get
        else if (g.getAnnex.getInitializes.get.guarantees.nonEmpty)
          g.getAnnex.getInitializes.get.guarantees(0).posOpt.get
        else g.getAnnex.getInitializes.get.flows(0).posOpt.get
      else if (g.getAnnex.integration.nonEmpty)
        g.getAnnex.integration.get.getSpecs(0).posOpt.get
      else //
        if (g.getAnnex.compute.get.modifies.nonEmpty)
          g.getAnnex.compute.get.modifies(0).posOpt.get
        else if (g.getAnnex.getCompute.get.specs.nonEmpty)
          g.getAnnex.compute.get.specs(0).posOpt.get
        else if (g.getAnnex.getCompute.get.cases.nonEmpty)
          g.getAnnex.getCompute.get.cases(0).posOpt.get
        else if (g.getAnnex.getCompute.get.handlers.nonEmpty)
          g.getAnnex.getCompute.get.handlers(0).port.posOpt.get
        else
          g.getAnnex.getCompute.get.flows(0).posOpt.get

    val f = getAadlFile(p.uriOpt.get, aadlDir)//Os.path(p.uriOpt.get)
    val lines = Util.readLines(f.read, '\n')
    for (i <- p.beginLine to 0 by - 1 if ops.StringOps(lines(i)).contains("annex GUMBO {**")) {
      return p.asInstanceOf[FlatPos](beginLine32 = conversions.Z.toU32(i + 1))
    }
    return p
  }

  def getGumboSubclause(thread: AadlThread, st: SymbolTable): Option[GclAnnexClauseInfo] = {
    st.annexClauseInfos.get(thread.path) match {
      case Some(annexes) =>
        for (a <- annexes if a.isInstanceOf[GclAnnexClauseInfo]) {
          return Some(a.asInstanceOf[GclAnnexClauseInfo])
        }
        return None()
      case _ => return None()
    }
  }

  def getComponentTypeName(thread: AadlThread): String = {
    val split = ops.StringOps(thread.component.classifier.get.name).split(c => c == '.')
    return split(0)
  }

  def isImplementation(thread: AadlThread): B = {
    return ops.StringOps(thread.component.classifier.get.name).contains(".")
  }

  def getAadlArchDiagram(aadlRoot: Os.Path): Option[Os.Path] = {
    if ((aadlRoot / "diagrams" / "aadl-arch.svg").exists) {
      return Some(aadlRoot / "diagrams" / "aadl-arch.svg")
    } else if ((aadlRoot / "diagrams" / "aadl-arch.png").exists) {
      return Some(aadlRoot / "diagrams" / "aadl-arch.png")
    } else {
      return None()
    }
  }


  @memoize def getModel(inputFile: Os.Path, isMsgpack: B): ir.Aadl = {
    val input: String = if (inputFile.exists && inputFile.isFile) {
      inputFile.read
    } else {
      halt(s"AIR input file ${inputFile} not found")
    }

    val model: ir.Aadl = if (isMsgpack) {
      org.sireum.conversions.String.fromBase64(input) match {
        case Either.Left(u) =>
          MsgPack.toAadl(u) match {
            case Either.Left(m) => m
            case Either.Right(m) =>
              halt(s"MsgPack deserialization error at offset ${m.offset}: ${m.message}")
          }
        case Either.Right(m) =>
          halt(m)
      }
    }
    else {
      JSON.toAadl(input) match {
        case Either.Left(m) => m
        case Either.Right(m) =>
          halt(s"Json deserialization error at (${m.line}, ${m.column}): ${m.message}")
      }
    }
    return model
  }

  @memoize def getSymbolTable(model: ir.Aadl, basePackageName: String, o: Cli.SireumHamrCodegenOption): SymbolTable = {
    val reporter = Reporter.create

    var _model = model

    val result = ir.Transformer(Transformers.MissingTypeRewriter()).transformAadl(Transformers.CTX(F, ISZ()), _model)
    _model = if (result.resultOpt.nonEmpty) result.resultOpt.get else model

    val rawConnections: B = PropertyUtil.getUseRawConnection(_model.components(0).properties)
    val aadlTypes = TypeResolver.processDataTypes(_model, rawConnections, o.maxStringSize, o.bitWidth, basePackageName, reporter)

    val aadlMaps = SymbolUtil.buildAadlMaps(_model, reporter)

    val s = SymbolResolver.resolve(_model, aadlTypes, aadlMaps, HAMR.toCodeGenOptions(o), reporter)
    if (reporter.hasError) {
      println("**********************************************")
      println("***  Messages from AadlModelUtil")
      println("**********************************************")
      reporter.printMessages()
      println("**********************************************")
      println("***  END OF Messages from AadlModelUtil")
      println("**********************************************")

    }
    return s.get
  }

  def getAadlFile(uri: String, aadlDir: Os.Path): Os.Path = {
    val sops = ops.StringOps(uri)
    assert(sops.startsWith("/"))
    val pos = sops.indexOfFrom('/', 1)
    val stripped = sops.substring(pos, sops.size)
    return aadlDir / stripped
  }

  def createLinkFromPos(text: String, pos: Position,
                        aadlDir: Os.Path, rootDir: Os.Path): ST = {
    val ret: ST = pos match {
      case org.sireum.message.FlatPos(Some(uriOpt), beginLine, _, _, _, _, _) =>

        val uri = getAadlFile(uriOpt, aadlDir)

        val line = conversions.U32.toZ(beginLine)
        st"[${text}](${rootDir.relativize(uri).value}#L${line})"

      case _ => halt(s"Infeasible: not a FlatPos - ${pos}")
    }
    return ret
  }

}
