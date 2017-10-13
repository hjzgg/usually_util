import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private static final int BUFFER_SIZE = 2048;

    /**
     * 复制文件或者目录,复制前后文件完全一样。
     * @param resFilePath   源文件路径
     * @param distFolder    目标文件夹
     * @throws IOException         当操作发生异常时抛出
     */
    public static void copyFile(String resFilePath, String distFolder)
            throws IOException {
        File resFile = new File(resFilePath);
        File distFile = new File(distFolder);
        if (resFile.isDirectory()) { // 目录时
            FileUtils.copyDirectoryToDirectory(resFile, distFile);
        } else if (resFile.isFile()) { // 文件时
            FileUtils.copyFileToDirectory(resFile, distFile);
        }
    }

    /**
     *
     * 删除一个文件或者目录
     * @param targetPath     文件或者目录路径
     * @throws IOException 当操作发生异常时抛出
     */
    public static boolean deleteFile(String targetPath) {
        try {
            File targetFile = new File(targetPath);
            if (targetFile.isDirectory()) {
                FileUtils.deleteDirectory(targetFile);
            } else if (targetFile.isFile()) {
                return targetFile.delete();
            }
        }catch (Exception e){
            LOGGER.error("download file error ",e);
        }
        return false;
    }

    public static void download(String url,String targetFiel)throws Exception{
        try {
            FileUtils.copyURLToFile(new URL(url), new File(targetFiel));
        }catch (Exception e){
            LOGGER.error("download file error ",e);
            throw e;
        }
    }


    public static String getFileContent(String filePath) {
        try {
            return FileUtils.readFileToString(new File(filePath), "UTF-8");
        }catch (Exception e){
            LOGGER.error("",e);
        }
        return "";
    }

    /**
     * 解压 targz文件,
     * @param file  源文件路径
     * @param destDir 解压后的文件路径
     * @return
     * @throws Exception
     */
    public static List<String> unTarGZ(String file,String destDir)throws Exception{
        try {
            File tarFile = new File(file);
            return unTarGZ(tarFile, destDir);
        }catch (Exception e){
            LOGGER.error("unTarGZ error",e);
            throw e;
        }
    }

    private static List<String> unTarGZ(File tarFile, String destDir) throws Exception{
        if(StringUtils.isBlank(destDir)) {
            destDir = tarFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        return unTar(new GzipCompressorInputStream(new FileInputStream(tarFile)), destDir);
    }

    private static List<String> unTar(InputStream inputStream, String destDir) throws Exception {
        List<String> fileNames = new ArrayList<>();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream, BUFFER_SIZE);
        TarArchiveEntry entry;
        try {
            while ((entry = tarIn.getNextTarEntry()) != null) {
                fileNames.add(entry.getName());
                if (entry.isDirectory()) {//是目录
                    createDirectory(destDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(destDir + File.separator + entry.getName());
                    createDirectory(tmpFile.getParent() + File.separator, null);//创建输出目录
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;
                        byte[] b = new byte[2048];
                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }
                    } finally {
                        IOUtils.closeQuietly(out);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("unTar error",e);
            throw e;
        } finally {
            IOUtils.closeQuietly(tarIn);
        }

        return fileNames;
    }


    public static void createDirectory(String outputDir,String subDir){
        File file = new File(outputDir);
        if(!(subDir == null || subDir.trim().equals(""))){//子目录不为空
            file = new File(outputDir + File.separator + subDir);
        }
        if(!file.exists()){
            file.mkdirs();
        }
    }
    /**
     * 加载properties文件
     * @param file 文件全路径
     * @return
     * @throws Exception
     */
    public static Map<String,String> lodeProperties(String file)throws Exception{
        Map<String,String> map;
        try ( InputStream in = new BufferedInputStream(new FileInputStream(file))){
            Properties properties = new Properties();
            properties.load(in);
            Iterator<String> it=properties.stringPropertyNames().iterator();
            map = new HashMap<>();
            while (it.hasNext()){
                String key = it.next();
                map.put(key,properties.getProperty(key));
            }
        }catch (Exception e){
            LOGGER.error("lodeProperties error",e);
            throw e;
        }
        return map;
    }

}
