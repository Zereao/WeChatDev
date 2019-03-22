package com.zereao.wechat.common.utils;

import com.zereao.wechat.common.tuple.TwoTuple;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
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
    private static final Pattern CLASS_FILE_PATTERN = Pattern.compile("^.*[\\\\/]classes[\\\\/](.*)\\.class$");

    private static final Pattern JAR_FILE_PATTERN = Pattern.compile("^.*:(/.*\\.jar)$");

    /**
     * 获取某个包下的所有的类的Class对象
     *
     * @param packageName          包名
     * @param containChildPackages 是否包含子包
     * @return class list
     */
    public static List<Class<?>> getClass(String packageName, boolean containChildPackages) {
        log.debug("------>  getClass Start! packageName = {}", packageName);
        return getClassInfo(packageName, containChildPackages).classObj;
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
        return getClassInfo(packageName, containChildPackages).name;
    }


    /**
     * 获取某个包下的类信息
     *
     * @param packageName          包名
     * @param containChildPackages 是否包含子包
     * @return 类型信息Map
     */
    private static TwoTuple<List<String>, List<Class<?>>> getClassInfo(String packageName, boolean containChildPackages) {
        String packagePath = packageName.replace(".", "/");
        TwoTuple<List<String>, List<Class<?>>> nameClassTuple = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String path = url.getPath();
            switch (url.getProtocol()) {
                case "file":
                    nameClassTuple = getClassInfoByFile(path, containChildPackages);
                    break;
                case "jar":
                    nameClassTuple = getClassInfoByJar(path, containChildPackages);
                    break;
                default:
                    break;
            }
        } else {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            nameClassTuple = PackageUtils.getClassInfoByJars(urls, packagePath, containChildPackages);
        }
        return nameClassTuple;
    }

    /**
     * 从项目文件获取某包下所有类的类信息
     *
     * @param path                 文件或包路径
     * @param containChildPackages 是否遍历子包
     * @return resultMap
     */
    private static TwoTuple<List<String>, List<Class<?>>> getClassInfoByFile(String path, boolean containChildPackages) {
        List<String> classNameList = new ArrayList<>();
        List<Class<?>> classList = new ArrayList<>();
        TwoTuple<List<String>, List<Class<?>>> nameClassTuple = new TwoTuple<>(classNameList, classList);
        File[] files = new File(path).listFiles();
        if (files == null || files.length <= 0) {
            return null;
        }
        Arrays.stream(files).forEach(file -> {
            if (file.isDirectory()) {
                if (containChildPackages) {
                    TwoTuple<List<String>, List<Class<?>>> tempTuple = getClassInfoByFile(file.getPath(), true);
                    if (tempTuple != null) {
                        classNameList.addAll(tempTuple.name);
                        classList.addAll(tempTuple.classObj);
                    }
                }
            } else {
                String filePath = file.getPath();
                if (filePath.endsWith(".class")) {
                    /*  例如：filePath =
                      /Users/jupiter/Code/JavaCode/WeChatDev/target/classes/com/zereao/wechat/dao/ArticlesDAO.class
                      经过下面这一步处理过后，得到结果 className = com.zereao.wechat.dao.ArticlesDAO        */
                    Matcher matcher = CLASS_FILE_PATTERN.matcher(filePath);
                    if (matcher.find()) {
                        String className = matcher.group(1).replace(File.separator, ".");
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
        return nameClassTuple;
    }

    /**
     * 从Jar 文件中读取类信息
     *
     * @param jarPath              Jar 文件路径
     * @param containChildPackages 是否遍历子包
     * @return resultMap
     */
    private static TwoTuple<List<String>, List<Class<?>>> getClassInfoByJar(String jarPath, boolean containChildPackages) {
        /* 例如，走到这里时，jarPath =
            file:/home/tom/apps/wechat-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/com/zereao/wechat      */
        String[] jarInfo = jarPath.split("!");
        Matcher matcher = JAR_FILE_PATTERN.matcher(jarInfo[0]);
        String jarFilePath = matcher.find() ? matcher.group(1) : "";
        String packagePath = jarPath.substring(jarPath.lastIndexOf("!") + 2);

        List<String> classNameList = new ArrayList<>();
        List<Class<?>> classList = new ArrayList<>();
        TwoTuple<List<String>, List<Class<?>>> nameClassTuple = new TwoTuple<>(classNameList, classList);
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                String entryName = entry.getName().replace("BOOT-INF/classes/", "");
                if (entryName.endsWith(".class")) {
                    if (containChildPackages) {
                        // 筛选出packagePath包路径下的所有类
                        if (entryName.startsWith(packagePath)) {
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
        } catch (Throwable e) {
            log.error("发生了错误！", e);
        }
        return nameClassTuple;
    }

    /**
     * 从当前项目依赖的所有的Jar文件中搜索该包，并获取该包下所有的类
     *
     * @param urls                 当前URLClassLoader下所有的URL 数组
     * @param packagePath          包路径
     * @param containChildPackages 是否包含子包
     * @return 类的完整名称List
     */
    private static TwoTuple<List<String>, List<Class<?>>> getClassInfoByJars(URL[] urls, String packagePath, boolean containChildPackages) {
        if (urls != null) {
            List<String> classNameList = new ArrayList<>();
            List<Class<?>> classList = new ArrayList<>();
            TwoTuple<List<String>, List<Class<?>>> nameClassTuple = new TwoTuple<>(classNameList, classList);
            for (URL url : urls) {
                String jarPath = url.getPath() + "!/" + packagePath;
                Boolean result = Optional.ofNullable(getClassInfoByJar(jarPath, containChildPackages)).map(tuple -> {
                    classList.addAll(tuple.classObj);
                    classNameList.addAll(tuple.name);
                    return true;
                }).orElse(null);
            }
            return nameClassTuple;
        }
        return null;
    }

}