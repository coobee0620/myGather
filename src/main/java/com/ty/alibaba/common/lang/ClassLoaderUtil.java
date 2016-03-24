package com.ty.alibaba.common.lang;

import com.ty.alibaba.common.lang.io.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/6/23
 */
public class ClassLoaderUtil {

    /* ============================================================================ */
    /*  取得context class loader的方法。                                            */
    /* ============================================================================ */

    /**
     * 取得当前线程的<code>ClassLoader</code>。这个功能需要JDK1.2或更高版本的JDK的支持。
     *
     * @return 当前线程的<code>ClassLoader</code>
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /* ============================================================================ */
    /*  装入类的方法。                                                              */
    /* ============================================================================ */

    /**
     * 从当前线程的<code>ClassLoader</code>装入类。对于JDK1.2以下，则相当于<code>Class.forName</code>。
     *
     * @param className 要装入的类名
     *
     * @return 已装入的类
     *
     * @throws ClassNotFoundException 如果类没找到
     */
    public static Class loadClass(String className) throws ClassNotFoundException {
        return loadClass(className, getContextClassLoader());
    }

    /**
     * 从指定的调用者的<code>ClassLoader</code>装入类。
     *
     * @param className 要装入的类名
     * @param referrer 调用者类，如果为<code>null</code>，则该方法相当于<code>Class.forName</code>
     *
     * @return 已装入的类
     *
     * @throws ClassNotFoundException 如果类没找到
     */
    public static Class loadClass(String className, Class referrer)
            throws ClassNotFoundException {
        ClassLoader classLoader = getReferrerClassLoader(referrer);

        // 如果classLoader为null，表示从ClassLoaderUtil所在的classloader中装载，
        // 这个classloader未必是System class loader。
        return loadClass(className, classLoader);
    }

    /**
     * 从指定的<code>ClassLoader</code>中装入类。如果未指定<code>ClassLoader</code>，
     * 则从装载<code>ClassLoaderUtil</code>的<code>ClassLoader</code>中装入。
     *
     * @param className 要装入的类名
     * @param classLoader
     *        从指定的<code>ClassLoader</code>中装入类，如果为<code>null</code>，表示从<code>ClassLoaderUtil</code>所在的class
     *        loader中装载
     *
     * @return 已装入的类
     *
     * @throws ClassNotFoundException 如果类没找到
     */
    public static Class loadClass(String className, ClassLoader classLoader)
            throws ClassNotFoundException {
        if (className == null) {
            return null;
        }

        if (classLoader == null) {
            return Class.forName(className);
        } else {
            return Class.forName(className, true, classLoader);
        }
    }

    /**
     * 按照标准的jar service规范搜索并装载指定名称的service类。
     *
     * <p>
     * 搜索方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param serviceId 服务名
     *
     * @return service class
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     */
    public static Class loadServiceClass(String serviceId) throws ClassNotFoundException {
        return loadServiceClass(serviceId, getContextClassLoader());
    }

    /**
     * 按照标准的jar service规范搜索并装载指定名称的service类。
     *
     * <p>
     * 搜索方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param serviceId 服务名
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return service class
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     */
    public static Class loadServiceClass(String serviceId, Class referrer)
            throws ClassNotFoundException {
        ClassLoader classLoader = getReferrerClassLoader(referrer);

        // 如果classLoader为null，表示从ClassLoaderUtil所在的classloader中查找，
        return loadServiceClass(serviceId, classLoader);
    }

    /**
     * 按照标准的jar service规范搜索并装载指定名称的service类。
     *
     * <p>
     * 搜索方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param serviceId 服务名
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return service class
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     */
    public static Class loadServiceClass(String serviceId, ClassLoader classLoader)
            throws ClassNotFoundException {
        if (serviceId == null) {
            return null;
        }

        serviceId = "META-INF/services/" + serviceId;

        InputStream istream = getResourceAsStream(serviceId, classLoader);

        if (istream == null) {
            throw new ServiceNotFoundException("Could not find " + serviceId);
        }

        // 读文件/META-INF/services/serviceId。
        String serviceClassName;

        try {
            serviceClassName = StringUtil.trimToEmpty(StreamUtil.readText(istream, "UTF-8"));
        } catch (IOException e) {
            throw new ServiceNotFoundException("Failed to load " + serviceId, e);
        }

        return ClassLoaderUtil.loadClass(serviceClassName, classLoader);
    }

    /**
     * 首先试图装入指定名称的类，如果找不到，再按照标准的jar service规范搜索并装载指定名称的service类。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param className 要装入的类名
     * @param serviceId 服务名
     *
     * @return service class
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     */
    public static Class loadServiceClass(String className, String serviceId)
            throws ClassNotFoundException {
        return loadServiceClass(className, serviceId, getContextClassLoader());
    }

    /**
     * 首先试图装入指定名称的类，如果找不到，再按照标准的jar service规范搜索并装载指定名称的service类。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param className 要装入的类名
     * @param serviceId 服务名
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return service class
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     */
    public static Class loadServiceClass(String className, String serviceId, Class referrer)
            throws ClassNotFoundException {
        ClassLoader classLoader = getReferrerClassLoader(referrer);

        // 如果classLoader为null，表示从ClassLoaderUtil所在的classloader中查找，
        return loadServiceClass(className, serviceId, classLoader);
    }

    /**
     * 首先试图装入指定名称的类，如果找不到，再按照标准的jar service规范搜索并装载指定名称的service类。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param className 要装入的类名
     * @param serviceId 服务名
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return service class
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     */
    public static Class loadServiceClass(String className, String serviceId, ClassLoader classLoader)
            throws ClassNotFoundException {
        try {
            // 先尝试装入class。
            if (className != null) {
                return loadClass(className, classLoader);
            }
        } catch (ClassNotFoundException e) {
        }

        // 找不到，尝试查找service。
        return loadServiceClass(serviceId, classLoader);
    }

    /**
     * 取得调用者的class loader。
     *
     * @param referrer 调用者类
     *
     * @return 调用者的class loader，如果referrer为<code>null</code>，则返回<code>null</code>
     */
    private static ClassLoader getReferrerClassLoader(Class referrer) {
        ClassLoader classLoader = null;

        if (referrer != null) {
            classLoader = referrer.getClassLoader();

            // classLoader为null，说明referrer类是由bootstrap classloader装载的，
            // 例如：java.lang.String
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }

        return classLoader;
    }

    /* ============================================================================ */
    /*  装入并实例化类的方法。                                                      */
    /* ============================================================================ */

    /**
     * 从当前线程的<code>ClassLoader</code>装入类并实例化之。
     *
     * @param className 要实例化的类名
     *
     * @return 指定类名的实例
     *
     * @throws ClassNotFoundException 如果类没找到
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadClass(className));
    }

    /**
     * 从指定的调用者的<code>ClassLoader</code>装入类并实例化之。
     *
     * @param className 要实例化的类名
     * @param referrer 调用者类，如果为<code>null</code>，则从<code>ClassLoaderUtil</code>所在的class loader装载
     *
     * @return 指定类名的实例
     *
     * @throws ClassNotFoundException 如果类没找到
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className, Class referrer)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadClass(className, referrer));
    }

    /**
     * 从指定的<code>ClassLoader</code>中装入类并实例化之。如果未指定<code>ClassLoader</code>，
     * 则从装载<code>ClassLoaderUtil</code>的<code>ClassLoader</code>中装入。
     *
     * @param className 要实例化的类名
     * @param classLoader
     *        从指定的<code>ClassLoader</code>中装入类，如果为<code>null</code>，表示从<code>ClassLoaderUtil</code>所在的class
     *        loader中装载
     *
     * @return 指定类名的实例
     *
     * @throws ClassNotFoundException 如果类没找到
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className, ClassLoader classLoader)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadClass(className, classLoader));
    }

    /**
     * 创建指定类的实例。
     *
     * @param clazz 要创建实例的类
     *
     * @return 指定类的实例
     *
     * @throws ClassInstantiationException 如果实例化失败
     */
    private static Object newInstance(Class clazz) throws ClassInstantiationException {
        if (clazz == null) {
            return null;
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ClassInstantiationException("Failed to instantiate class: " + clazz.getName(),
                    e);
        } catch (IllegalAccessException e) {
            throw new ClassInstantiationException("Failed to instantiate class: " + clazz.getName(),
                    e);
        } catch (Exception e) {
            throw new ClassInstantiationException("Failed to instantiate class: " + clazz.getName(),
                    e);
        }
    }

    /**
     * 按照标准的jar service规范搜索并装载指定名称的service类并实例化之。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param serviceId 服务名
     *
     * @return 实例类
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newServiceInstance(String serviceId)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadServiceClass(serviceId));
    }

    /**
     * 按照标准的jar service规范搜索并装载指定名称的service类并实例化之。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param serviceId 服务名
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return 实例类
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newServiceInstance(String serviceId, Class referrer)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadServiceClass(serviceId, referrer));
    }

    /**
     * 按照标准的jar service规范搜索并装载指定名称的service类并实例化之。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param serviceId 服务名
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return 实例类
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newServiceInstance(String serviceId, ClassLoader classLoader)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadServiceClass(serviceId, classLoader));
    }

    /**
     * 首先试图装入指定名称的类，如果找不到，再按照标准的jar service规范搜索并装载指定名称的service类。 找到以后实例化之。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param className 要装入的类名
     * @param serviceId 服务名
     *
     * @return 实例类
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newServiceInstance(String className, String serviceId)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadServiceClass(className, serviceId));
    }

    /**
     * 首先试图装入指定名称的类，如果找不到，再按照标准的jar service规范搜索并装载指定名称的service类。 找到以后实例化之。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param className 要装入的类名
     * @param serviceId 服务名
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return 实例类
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newServiceInstance(String className, String serviceId, Class referrer)
            throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadServiceClass(className, serviceId, referrer));
    }

    /**
     * 首先试图装入指定名称的类，如果找不到，再按照标准的jar service规范搜索并装载指定名称的service类。 找到以后实例化之。
     *
     * <p>
     * 搜索service的方法是在class loader中查找<code>META-INF/services/serviceId</code>文件，并将其内容作为service类名。
     * </p>
     *
     * @param className 要装入的类名
     * @param serviceId 服务名
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return 实例类
     *
     * @throws ClassNotFoundException 如果service class找不到，或装载service文件失败
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newServiceInstance(String className, String serviceId,
                                            ClassLoader classLoader) throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadServiceClass(className, serviceId, classLoader));
    }

    /* ============================================================================ */
    /*  装入和查找资源文件的方法。                                                  */
    /* ============================================================================ */

    /**
     * 从<code>ClassLoader</code>取得所有resource URL。按如下顺序查找:
     *
     * <ol>
     * <li>
     * 在当前线程的<code>ClassLoader</code>中查找。
     * </li>
     * <li>
     * 在装入自己的<code>ClassLoader</code>中查找。
     * </li>
     * <li>
     * 通过<code>ClassLoader.getSystemResource</code>方法查找。
     * </li>
     * </ol>
     *
     *
     * @param resourceName 要查找的资源名，就是以&quot;/&quot;分隔的标识符字符串
     *
     * @return resource的URL数组，如果没找到，则返回空数组。数组中保证不包含重复的URL。
     */
    public static URL[] getResources(String resourceName) {
        LinkedList urls  = new LinkedList();
        boolean    found = false;

        // 首先试着从当前线程的ClassLoader中查找。
        found = getResources(urls, resourceName, getContextClassLoader(), false);

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        if (!found) {
            getResources(urls, resourceName, ClassLoaderUtil.class.getClassLoader(), false);
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        if (!found) {
            getResources(urls, resourceName, null, true);
        }

        // 返回不重复的列表。
        return getDistinctURLs(urls);
    }

    /**
     * 从指定调用者所属的<code>ClassLoader</code>取得所有resource URL。
     *
     * @param resourceName 要查找的资源名，就是以&quot;/&quot;分隔的标识符字符串
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找
     *
     * @return resource的URL数组，如果没找到，则返回空数组。数组中保证不包含重复的URL。
     */
    public static URL[] getResources(String resourceName, Class referrer) {
        ClassLoader classLoader = getReferrerClassLoader(referrer);
        LinkedList  urls = new LinkedList();

        getResources(urls, resourceName, classLoader, classLoader == null);

        // 返回不重复的列表。
        return getDistinctURLs(urls);
    }

    /**
     * 从指定的<code>ClassLoader</code>中取得所有resource URL。如果未指定<code>ClassLoader</code>，
     * 则从装载<code>ClassLoaderUtil</code>的<code>ClassLoader</code>中取得所有resource URL。
     *
     * @param resourceName 要查找的资源名，就是以&quot;/&quot;分隔的标识符字符串
     * @param classLoader 从指定的<code>ClassLoader</code>中查找
     *
     * @return resource的URL数组，如果没找到，则返回空数组。数组中保证不包含重复的URL。
     */
    public static URL[] getResources(String resourceName, ClassLoader classLoader) {
        LinkedList urls = new LinkedList();

        getResources(urls, resourceName, classLoader, classLoader == null);

        // 返回不重复的列表。
        return getDistinctURLs(urls);
    }

    /**
     * 在指定class loader中查找指定名称的resource，把所有找到的resource的URL放入指定的集合中。
     *
     * @param urlSet 存放resource URL的集合
     * @param resourceName 资源名
     * @param classLoader 类装入器
     * @param sysClassLoader 是否用system class loader装载资源
     *
     * @return 如果找到，则返回<code>true</code>
     */
    private static boolean getResources(List urlSet, String resourceName, ClassLoader classLoader,
                                        boolean sysClassLoader) {
        if (resourceName == null) {
            return false;
        }

        Enumeration i = null;

        try {
            if (classLoader != null) {
                i = classLoader.getResources(resourceName);
            } else if (sysClassLoader) {
                i = ClassLoader.getSystemResources(resourceName);
            }
        } catch (IOException e) {
        }

        if ((i != null) && i.hasMoreElements()) {
            while (i.hasMoreElements()) {
                urlSet.add(i.nextElement());
            }

            return true;
        }

        return false;
    }

    /**
     * 去除URL列表中的重复项。
     *
     * @param urls URL列表
     *
     * @return 不重复的URL数组，如果urls为<code>null</code>，则返回空数组
     */
    private static URL[] getDistinctURLs(LinkedList urls) {
        if ((urls == null) || (urls.size() == 0)) {
            return new URL[0];
        }

        Set urlSet = new HashSet(urls.size());

        for (Iterator i = urls.iterator(); i.hasNext();) {
            URL url = (URL) i.next();

            if (urlSet.contains(url)) {
                i.remove();
            } else {
                urlSet.add(url);
            }
        }

        return (URL[]) urls.toArray(new URL[urls.size()]);
    }

    /**
     * <p>
     * 从<code>ClassLoader</code>取得resource URL。按如下顺序查找:
     * </p>
     *
     * <ol>
     * <li>
     * 在当前线程的<code>ClassLoader</code>中查找。
     * </li>
     * <li>
     * 在装入自己的<code>ClassLoader</code>中查找。
     * </li>
     * <li>
     * 通过<code>ClassLoader.getSystemResource</code>方法查找。
     * </li>
     * </ol>
     *
     *
     * @param resourceName 要查找的资源名，就是以&quot;/&quot;分隔的标识符字符串
     *
     * @return resource的URL
     */
    public static URL getResource(String resourceName) {
        if (resourceName == null) {
            return null;
        }

        ClassLoader classLoader = null;
        URL         url = null;

        // 首先试着从当前线程的ClassLoader中查找。
        classLoader = getContextClassLoader();

        if (classLoader != null) {
            url = classLoader.getResource(resourceName);

            if (url != null) {
                return url;
            }
        }

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        classLoader = ClassLoaderUtil.class.getClassLoader();

        if (classLoader != null) {
            url = classLoader.getResource(resourceName);

            if (url != null) {
                return url;
            }
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        return ClassLoader.getSystemResource(resourceName);
    }

    /**
     * 从指定调用者所属的<code>ClassLoader</code>取得resource URL。
     *
     * @param resourceName 要查找的资源名，就是以&quot;/&quot;分隔的标识符字符串
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return resource URL，如果没找到，则返回<code>null</code>
     */
    public static URL getResource(String resourceName, Class referrer) {
        if (resourceName == null) {
            return null;
        }

        ClassLoader classLoader = getReferrerClassLoader(referrer);

        return (classLoader == null)
                ? ClassLoaderUtil.class.getClassLoader().getResource(resourceName)
                : classLoader.getResource(resourceName);
    }

    /**
     * 从指定的<code>ClassLoader</code>取得resource URL。
     *
     * @param resourceName 要查找的资源名，就是以&quot;/&quot;分隔的标识符字符串
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return resource URL，如果没找到，则返回<code>null</code>
     */
    public static URL getResource(String resourceName, ClassLoader classLoader) {
        if (resourceName == null) {
            return null;
        }

        return (classLoader == null)
                ? ClassLoaderUtil.class.getClassLoader().getResource(resourceName)
                : classLoader.getResource(resourceName);
    }

    /**
     * 从<code>ClassLoader</code>取得resource的输入流。
     * 相当于<code>getResource(resourceName).openStream()</code>。
     *
     * @param resourceName 要查找的资源名，就是以"/"分隔的标识符字符串
     *
     * @return resource的输入流
     */
    public static InputStream getResourceAsStream(String resourceName) {
        URL url = getResource(resourceName);

        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
            // 打开URL失败。
        }

        return null;
    }

    /**
     * 从<code>ClassLoader</code>取得resource的输入流。 相当于<code>getResource(resourceName,
     * referrer).openStream()</code>。
     *
     * @param resourceName 要查找的资源名，就是以"/"分隔的标识符字符串
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return resource的输入流
     */
    public static InputStream getResourceAsStream(String resourceName, Class referrer) {
        URL url = getResource(resourceName, referrer);

        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
            // 打开URL失败。
        }

        return null;
    }

    /**
     * 从<code>ClassLoader</code>取得resource的输入流。 相当于<code>getResource(resourceName,
     * classLoader).openStream()</code>。
     *
     * @param resourceName 要查找的资源名，就是以"/"分隔的标识符字符串
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return resource的输入流
     */
    public static InputStream getResourceAsStream(String resourceName, ClassLoader classLoader) {
        URL url = getResource(resourceName, classLoader);

        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
            // 打开URL失败。
        }

        return null;
    }

    /* ============================================================================ */
    /*  查找class的位置。                                                           */
    /*                                                                              */
    /*  类似于UNIX的which方法。                                                     */
    /* ============================================================================ */

    /**
     * 从当前线程的<code>ClassLoader</code>中查找指定名称的类。
     *
     * @param className 要查找的类名
     *
     * @return URL数组，列举了系统中所有可找到的同名类，如果未找到，则返回一个空数组
     */
    public static URL[] whichClasses(String className) {
        return getResources(ClassUtil.getClassNameAsResource(className));
    }

    /**
     * 从当前线程的<code>ClassLoader</code>中查找指定名称的类。
     *
     * @param className 要查找的类名
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return URL数组，列举了系统中所有可找到的同名类，如果未找到，则返回一个空数组
     */
    public static URL[] whichClasses(String className, Class referrer) {
        return getResources(ClassUtil.getClassNameAsResource(className), referrer);
    }

    /**
     * 从当前线程的<code>ClassLoader</code>中查找指定名称的类。
     *
     * @param className 要查找的类名
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return URL数组，列举了系统中所有可找到的同名类，如果未找到，则返回一个空数组
     */
    public static URL[] whichClasses(String className, ClassLoader classLoader) {
        return getResources(ClassUtil.getClassNameAsResource(className), classLoader);
    }

    /**
     * 从当前线程的<code>ClassLoader</code>中查找指定名称的类。
     *
     * @param className 要查找的类名
     *
     * @return 类文件的URL，如果未找到，则返回<code>null</code>
     */
    public static URL whichClass(String className) {
        return getResource(ClassUtil.getClassNameAsResource(className));
    }

    /**
     * 从当前线程的<code>ClassLoader</code>中查找指定名称的类。
     *
     * @param className 要查找的类名
     * @param referrer 调用者类，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class loader中找。
     *
     * @return 类文件的URL，如果未找到，则返回<code>null</code>
     */
    public static URL whichClass(String className, Class referrer) {
        return getResource(ClassUtil.getClassNameAsResource(className), referrer);
    }

    /**
     * 从当前线程的<code>ClassLoader</code>中查找指定名称的类。
     *
     * @param className 要查找的类名
     * @param classLoader
     *        在指定classLoader中查找，如果为<code>null</code>，表示在<code>ClassLoaderUtil</code>的class
     *        loader中找。
     *
     * @return 类文件的URL，如果未找到，则返回<code>null</code>
     */
    public static URL whichClass(String className, ClassLoader classLoader) {
        return getResource(ClassUtil.getClassNameAsResource(className), classLoader);
    }
}

