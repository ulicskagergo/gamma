package hu.bme.mit.gamma.verification.util

import hu.bme.mit.gamma.transformation.util.GammaFileNamer
import hu.bme.mit.gamma.util.FileUtil
import hu.bme.mit.gamma.util.GammaEcoreUtil
import hu.bme.mit.gamma.verification.util.AbstractVerifier.Result
import java.io.File
import java.util.logging.Logger

abstract class AbstractVerification {

	protected final FileUtil fileUtil = FileUtil.INSTANCE
	protected final GammaEcoreUtil ecoreUtil = GammaEcoreUtil.INSTANCE
	protected final extension GammaFileNamer fileNamer = GammaFileNamer.INSTANCE

	protected final Logger logger = Logger.getLogger("GammaLogger")
	
	abstract def Result execute(File modelFile, File queryFile)
	abstract def String[] getParameters()

}