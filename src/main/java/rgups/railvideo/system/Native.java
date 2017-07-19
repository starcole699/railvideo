package rgups.railvideo.system;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by Dmitry on 22.11.2015.
 */
public class Native {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static final String PATH_32_SUFFIX = "32";
    public static final String PATH_64_SUFFIX = "64";
    public static final String PATH_WINDOWS   = "win";
    public static final String PATH_LINUX     = "linux";
    public static final String PATH_MAC       = "mac";


    public static void addNativesFromRoot(String rootPath) throws Exception {
        File pathDir = new File(rootPath, getNativeLibsPathSuffix());
        addLibraryPath(pathDir.getAbsolutePath());
    }

    public static String getNativeLibsPathSuffix() {
        if (SystemUtils.IS_OS_MAC) {
            return PATH_MAC;
        }

        String retPath;
        if (SystemUtils.IS_OS_WINDOWS) {
            retPath = PATH_WINDOWS;
        } else if (SystemUtils.IS_OS_LINUX) {
            retPath = PATH_LINUX;
        } else {
            throw new RuntimeException("Unsupported OS: " + OS);
        }

        String platform = System.getProperty("sun.arch.data.model");

        if ("32".equals(platform)) {
            retPath += PATH_32_SUFFIX;
        } else if ("64".equals(platform)) {
            retPath += PATH_64_SUFFIX;
        } else {
            throw new RuntimeException("Can't determine platform: " + platform);
        }

        return retPath;
    }

    /**
     * Adds the specified path to the java library path
     * From here: http://fahdshariff.blogspot.be/2011/08/changing-java-library-path-at-runtime.html
     *
     * @param pathToAdd the path to add
     * @throws Exception
     */
    public static void addLibraryPath(String pathToAdd) throws Exception{
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        //get array of paths
        final String[] paths = (String[])usrPathsField.get(null);

        //check if the path to add is already present
        for(String path : paths) {
            if(path.equals(pathToAdd)) {
                return;
            }
        }

        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length-1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }
}