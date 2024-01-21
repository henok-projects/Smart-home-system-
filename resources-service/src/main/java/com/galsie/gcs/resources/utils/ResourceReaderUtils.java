package com.galsie.gcs.resources.utils;

import com.galsie.lib.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * All read from resources directory
 */
public class ResourceReaderUtils {

    private static final Logger logger = LogManager.getLogger();

    public static byte[] readAllBytesFromFile(String path) throws IOException{
        var fis = readInputStreamFromFile(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return data;
    }

    public static InputStream readInputStreamFromFile(String path) throws IOException{
        Path filePath = Paths.get(path);
        return Files.newInputStream(filePath);
    }

    /**
     * Lists files in a path. FILE NAME CANT HAVE SPACES
     * we a resource patter resolvers to get the files because it can get files from jars as well
     * the pattern after path means all files in all subdirectories
     * @param path
     * @return the list of files in the path
     */
    public static List<File> listFiles(String path) {
        List<File> fileList = new ArrayList<>();
        Path filePath = Paths.get(path);
        listFilesRecursively(filePath.toFile(), fileList);
        return fileList;
    }

    private static void listFilesRecursively(File directory, List<File> fileList) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    listFilesRecursively(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }

    /**
     * Scans the path and its subdirectories for files that match the extension
     *
     * @param scanPath
     * @param extensions
     * @param ignoreHidden
     * @return List of files paths found that match the extension. The paths returned are all relative to the scanPath parameter
     */
    public static List<String> scanFiles(String scanPath, String[] extensions, boolean ignoreHidden) throws Exception {
        List<String> files = new ArrayList<>();
        scanFilesAux(scanPath, "", extensions, ignoreHidden, files);
        return files;
    }

    /**
     * Auxiliary function..
     * Scans for files a certain type in a directory.
     * If a directory was found, scans that directory as well.
     * Results are in a list of paths.
     *
     * @param mainScanPath:  the main path. Results are returned relative to this path. The main path is kept the same throughout the recursion, the subpath is changed.
     * @param startScanPath: The scan starts in this path relative to the mainScanPath. If empty, starts in the main path
     * @param extensions:    the file extensions to find
     * @param ignoreHidden:  whether to ignore hidden files
     */
    private static void scanFilesAux(String mainScanPath, String startScanPath, String[] extensions, boolean ignoreHidden, List<String> files) throws Exception {
        for (int i = 0; i < extensions.length; i++) {
            String extension = extensions[i];
            extensions[i] = (extension.startsWith(".") || extension.isEmpty()) ? extension : "." + extension;
        }
        String path = StringUtils.joinPaths(mainScanPath, startScanPath);
        // System.out.println("CURR PATH: " + path);
        for (File file : ResourceReaderUtils.listFiles(path)) {
            if ((ignoreHidden && file.getName().startsWith(".")) || file.getName().isEmpty()) { // ingore hidden files
                continue;
            }
            var resourcePath = file.getPath().replaceAll("\\\\", "/");
            var index = resourcePath.indexOf(mainScanPath) + 1;
            String relativeFilePath = resourcePath.substring(index + mainScanPath.length());
            if (resourcePath.endsWith("/")) {
                continue;
            }
            String fileName = file.getName();
            var matchedExtOpt = Arrays.stream(extensions).filter(fileName::endsWith).findFirst();
            if (matchedExtOpt.isEmpty()) {
                continue;
            }
            files.add(relativeFilePath);
        }
    }

    public static  String urlPathCleanUp(String path){
        path = path.replace("%20", " ");//to resolve space
        return path;
    }

    public static long getFileSize(String path) {
        Path filePath = Paths.get(path);
        if(!filePath.toFile().exists()){
            logger.error("Failed to get file size for " + filePath + " because it doesn't exist");
            return 0L;
        }
        try{
            return Files.size(filePath);
        }catch (Exception e){
            logger.error("Failed to get file size for " + filePath, e);
            return 0L;
        }
    }
}
