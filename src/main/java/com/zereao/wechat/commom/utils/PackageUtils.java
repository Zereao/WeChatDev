package com.zereao.wechat.commom.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Darion Mograine H
 * @version 2019/01/14  17:33
 */
@Slf4j
public class PackageUtils {
    private static final Pattern CLASS_FILE_PATTERN = Pattern.compile("^.*\\\\classes\\\\(.*)\\.class$");

    private static final Pattern JAR_FILE_PATTERN = Pattern.compile("^.*:/(.*\\.jar)$");

    private static final String KEY_CLASS_NAME = "classname";
    private static final String KEY_CLASSES = "classes";

    /**
     * 获取某个包下的所有的类的Class对象
     *
     * @param packageName          包名
     * @param containChildPackages 是否包含子包
     * @return class list
     */
    public static List<Class> getClass(String packageName, boolean containChildPackages) {
        log.debug("------>  getClass Start! packageName = {}", packageName);
        return getClassInfo(packageName, containChildPackages, KEY_CLASSES);
    }

    /**
     * 获取某个包下的所有的类的名称
     *
     * @param packageName          包名
     * @param containChildPackages 是否包含子包
     * @return className list
     */
    public static List<String> getClassName(String packageName, boolean containChildPackages) {
        log.debug("------>  getClassName Start! packageName = {}", packageName);
        return getClassInfo(packageName, containChildPackages, KEY_CLASS_NAME);
    }

    /**
     * 剥离出的一个公共方法
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> getClassInfo(String packageName, boolean containChildPackages, String key) {
        Map<String, List<?>> resultMap = getClassInfo(packageName, containChildPackages);
        return (resultMap == null || resultMap.size() <= 0 || !resultMap.containsKey(key)) ? new ArrayList<>() : (List<T>) resultMap.get(key);
    }

    /**
     * 获取某个包下的类信息
     *
     * @param packageName          包名
     * @param containChildPackages 是否包含子包
     * @return 类型信息Map
     */
    private static Map<String, List<?>> getClassInfo(String packageName, boolean containChildPackages) {
        String packagePath = packageName.replace(".", "/");
        Map<String, List<?>> resultMap = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String path = url.getPath();
            switch (url.getProtocol()) {
                case "file":
                    resultMap = getClassInfoByFile(path, containChildPackages);
                    break;
                case "jar":
                    resultMap = getClassInfoByJar(path, containChildPackages);
                    break;
                default:
                    break;
            }
        } else {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            resultMap = PackageUtils.getClassInfoByJars(urls, packagePath, containChildPackages);
        }
        return resultMap;
    }

    /**
     * 从项目文件获取某包下所有类的类信息
     *
     * @param path                 文件或包路径
     * @param containChildPackages 是否遍历子包
     * @return resultMap
     */
    @SuppressWarnings("unchecked")
    private static Map<String, List<?>> getClassInfoByFile(String path, boolean containChildPackages) {
        Map<String, List<?>> resultMap = new ConcurrentHashMap<>(16);
        List<String> classNameList = new CopyOnWriteArrayList<>();
        List<Class> classList = new CopyOnWriteArrayList<>();
        resultMap.put(KEY_CLASS_NAME, classNameList);
        resultMap.put(KEY_CLASSES, classList);
        File[] files = new File(path).listFiles();
        if (files == null || files.length <= 0) {
            return null;
        }
        Arrays.stream(files).parallel().forEach(file -> {
            if (file.isDirectory()) {
                if (containChildPackages) {
                    Map<String, List<?>> tempMap = getClassInfoByFile(file.getPath(), true);
                    if (tempMap != null && tempMap.size() > 0) {
                        classList.addAll((List<Class>) tempMap.get(KEY_CLASSES));
                        classNameList.addAll((List<String>) tempMap.get(KEY_CLASS_NAME));
                    }
                }
            } else {
                String filePath = file.getPath();
                if (filePath.endsWith(".class")) {
                    /*  例如：childFilePath =
                      E:\JavaCode\ChatRoom\ChatRoomServer\target\classes\com\zereao\chatroom\app\ServerApp.class
                      经过下面这一步处理过后，得到结果 com\zereao\chatroom\app\ServerApp                      */
                    Matcher matcher = CLASS_FILE_PATTERN.matcher(filePath);
                    if (matcher.find()) {
                        String className = matcher.group(1).replace("\\", ".");
                        classNameList.add(className);
                        try {
                            classList.add(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            log.error("class ---> {}  Not Found!", className);
                            throw new RuntimeException(String.format("Class %s Not Found!", className));
                        }
                    }
                }
            }
        });
        return resultMap;
    }

    /**
     * 从Jar 文件中读取类信息
     *
     * @param jarPath              Jar 文件路径
     * @param containChildPackages 是否遍历子包
     * @return resultMap
     */
    private static Map<String, List<?>> getClassInfoByJar(String jarPath, boolean containChildPackages) {
        /* 例如，走到这里时，jarPath =
              file:/E:/CodeTools/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar!/org/junit/jupiter/api         */
        String[] jarInfo = jarPath.split("!");
        /* 经过以下处理，最终得到jarFilePath =
            E:/CodeTools/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar         */
        Matcher matcher = JAR_FILE_PATTERN.matcher(jarInfo[0]);
        String jarFilePath = matcher.find() ? matcher.group(1) : "";
        /* 经过以下处理，最终得到packagePath =
            org/junit/jupiter/api         */
        String packagePath = jarInfo[1].substring(1);
        Map<String, List<?>> resultMap = new ConcurrentHashMap<>(16);
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            List<String> classNameList = new ArrayList<>();
            List<Class> classList = new ArrayList<>();

            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                /* String entryRealName = entry.getRealName();
                    如果JarEntry不表示多版本JarFile的版本化条目，或者未将JarFile配置为处理多版本jar文件，
                则此方法返回与getName()返回的名称相同的名称。

                经过下面的处理，得到的entryName =
                    META-INF/
                    META-INF/MANIFEST.MF
                    org/
                    org/junit/
                    org/junit/jupiter
                    org/junit/jupiter/api
                    org/junit/jupiter/api/AssertArrayEquals.class
                    org/junit/jupiter/api/AssertNotNull.class等。
                也就是说，得到的jarEntry，包括文件对象、文件夹对象。 */
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    if (containChildPackages) {
                        // 筛选出packagePath包路径下的所有类
                        if (entryName.startsWith(packagePath)) {
                            // 经过以下处理，得到className = org.junit.jupiter.api.BeforeAll
                            String className = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            classNameList.add(className);
                            classList.add(Class.forName(className));
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            String tempPackagePath = entryName.substring(0, index);
                            // 只要packagePath下的类，不要packagePath子包下的类
                            if (packagePath.equals(tempPackagePath)) {
                                String className = entryName.replace("/", ",").substring(0, entryName.lastIndexOf("."));
                                classNameList.add(className);
                                classList.add(Class.forName(className));
                            }
                        }
                    }
                }
            }
            resultMap.put(KEY_CLASS_NAME, classNameList);
            resultMap.put(KEY_CLASSES, classList);
        } catch (IOException | ClassNotFoundException e) {
            log.error("发生了错误！", e);
        }
        return resultMap;
    }

    /**
     * 从当前项目依赖的所有的Jar文件中搜索该包，并获取该包下所有的类
     *
     * @param urls                 当前URLClassLoader下所有的URL 数组
     * @param packagePath          包路径
     * @param containChildPackages 是否包含子包
     * @return 类的完整名称List
     */
    private static Map<String, List<?>> getClassInfoByJars(URL[] urls, String packagePath, boolean containChildPackages) {
        if (urls != null) {
            final Map<String, List<?>> resultMap = new HashMap<>();
            for (URL url : urls) {
                String jarPath = url.getPath() + "!/" + packagePath;
                Boolean result = Optional.ofNullable(getClassInfoByJar(jarPath, containChildPackages)).map(map -> {
                    map.forEach(resultMap::put);
                    return true;
                }).orElse(null);
            }
            return resultMap;
        }
        return null;
    }

}