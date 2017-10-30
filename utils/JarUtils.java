import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarUtils {

    private static final List<File> LOAD_FILE = new ArrayList<>();
    private static final String NON_SENSE_WORD = "&@#";

    private static final Log log = LogFactory.getLog(JarUtils.class);

    private JarUtils() {
    }

    /**
     * 扫描jar包中的类
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> scanClassInJar(File file) throws IOException {
        List<String> list = new ArrayList<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration enu = jarFile.entries();
            while (enu.hasMoreElements()) {
                JarEntry entry = (JarEntry) enu.nextElement();
                String name = entry.getName();
                name = StringUtils.replace(name, "/", ".");
                if (!name.endsWith(".class")) {
                    continue;
                }
                list.add(name.substring(0, name.lastIndexOf(".class")));
            }
        }
        return list;
    }

    /**
     * 遍历目录下的jar包
     *
     * @param file
     */
    private static void scanJarFile(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    scanJarFile(files[i]);
                } else if (files[i].getName().endsWith(".jar")) {
                    LOAD_FILE.add(files[i]);
                }
            }
        }
    }

    public static List<File> getJarFile(File file) {
        scanJarFile(file);
        List<File> list = new ArrayList<>(LOAD_FILE);
        LOAD_FILE.clear();
        return list;
    }

    /**
     * 通过JarFile获取接口／方法签名
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static Map<String, List<String>> getInterfaceDeclarationsAndMethods(File file) throws IOException {
        Map<String, List<String>> interfaceSignatures = new HashMap<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    ClassNode classNode = new ClassNode();
                    InputStream classFileInputStream = jarFile.getInputStream(entry);
                    try {
                        ClassReader classReader = new ClassReader(classFileInputStream);
                        classReader.accept(classNode, 0);
                    } finally {
                        classFileInputStream.close();
                    }
                    if (isAnInterface(classNode)) {
                        interfaceSignatures.put(Type.getObjectType(classNode.name).getClassName(), classNode.methods.stream().map(JarUtils::getMethodSignature).collect(Collectors.toList()));
                    }
                }
            }
        }
        return interfaceSignatures;
    }

    public static List<String> getInterfacesDeclarations(File file) throws IOException {
        List<String> interfaces = new ArrayList<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    ClassNode classNode = new ClassNode();
                    InputStream classFileInputStream = jarFile.getInputStream(entry);
                    try {
                        ClassReader classReader = new ClassReader(classFileInputStream);
                        classReader.accept(classNode, 0);
                    } finally {
                        classFileInputStream.close();
                    }
                    if (isAnInterface(classNode)) {
                        interfaces.add(Type.getObjectType(classNode.name).getClassName());
                    }
                }
            }
        }
        return interfaces;
    }

    private static Boolean isAnInterface(ClassNode classNode) {
        return (classNode.access & Opcodes.ACC_PUBLIC) != 0 && (classNode.access & Opcodes.ACC_INTERFACE) != 0;
    }

    private static String getMethodSignature(MethodNode methodNode) {
        return methodNode.name;
    }
}
