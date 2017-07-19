package rgups.railvideo.system;

import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by Dmitry on 29.06.2017.
 */
public class NativeLibLoader {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${native_lib_path_root:lib/native}")
    String nativeLibPathRoot;

//    String[] libsToLoad = {Core.NATIVE_LIBRARY_NAME, "opencv_ffmpeg320_64"};
    String[] libsToLoad = {Core.NATIVE_LIBRARY_NAME};

    @PostConstruct
    public void initNative() throws Exception{
        String suffix = Native.getNativeLibsPathSuffix();
        String rootPath = new File(nativeLibPathRoot).getAbsolutePath();
        LOG.info("Loading native lib from '" + rootPath + "' for system: " + suffix);

        Native.addNativesFromRoot(nativeLibPathRoot);

        for (String lib : libsToLoad){
            LOG.info("Loading library: " + lib);
            System.loadLibrary(lib);
        }
    }
}
