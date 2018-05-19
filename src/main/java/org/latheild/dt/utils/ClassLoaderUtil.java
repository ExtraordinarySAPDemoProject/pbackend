package org.latheild.dt.utils;

import java.lang.reflect.Method;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaderUtil {

    private volatile static ClassLoaderUtil instance;

    public static ClassLoaderUtil getInstance() {
        if (instance == null) {
            synchronized(ClassLoaderUtil.class) {
                if (instance == null) {
                    instance = new ClassLoaderUtil();
                }
            }
        }
        return instance;
    }

    private ClassLoaderUtil() {

    }

    private static Logger logger = LoggerFactory.getLogger(ClassLoaderUtil.class);

    private static JavaCompiler javaCompiler;

    static {
        javaCompiler = ToolProvider.getSystemJavaCompiler();
    }

    private static void javaCompile(List<String> compileOptions, String... sourceFiles) {
        StandardJavaFileManager manager = null;
        try {
            manager = javaCompiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> it = manager.getJavaFileObjects(sourceFiles);
            JavaCompiler.CompilationTask compilationTask = javaCompiler.getTask(null, manager, null, compileOptions, null, it);
            compilationTask.call();
            if (logger.isDebugEnabled()) {
                for (String sourceFile : sourceFiles) {
                    logger.debug("Compile Java File: " + sourceFile);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static Class<?> load(String className) {
        Class<?> cls = null;
        ClassLoader classLoader = null;
        try {
            classLoader = ClassLoaderUtil.class.getClassLoader();
            cls = classLoader.loadClass(className);
            if (logger.isDebugEnabled()) {
                logger.debug("Load Class[" + className + "] by " + classLoader);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return cls;
    }

    public Class<?> loadClass(List<String> compileOptions, String sourceFile, String className) {
        try {
            javaCompile(compileOptions, sourceFile);
            return load(className);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public Object invoke(Class<?> cls, String methodName, Class<?>[] paramterClass, Object[] params) throws Exception {
        Object result = null;
        try {
            Method method = cls.getDeclaredMethod(methodName, paramterClass);
            Object obj = cls.newInstance();
            result = method.invoke(obj, params);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e.getCause().getMessage());
        }
        return result;
    }
}