package org.gauntlet.core.commons.util.bean;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

public class BeanPropertyCopyUtil {
    /**
     * copies properties from one object to another
     * 
     * @param src
     *            the source object
     * @param dest
     *            the destination object
     * @param properties
     *            a list of property names that are to be copied. Each value has
     *            the format "srcProperty destProperty". For example,
     *            "name fullName" indicates that you want to copy the src.name
     *            value to dest.fullName. If both the srcProperty and
     *            destProperty property have the same name, you can omit the
     *            destProperty. For example, "name" indicates that you want to
     *            copy src.name to dest.name.
     * 
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void copyProperties(Object src, Object dest,
                    String... properties) throws IllegalAccessException,
                    InvocationTargetException, NoSuchMethodException {
            for (String property : properties) {
                    String[] arr = property.split(" ");
                    String srcProperty;
                    String destProperty;
                    if (arr.length == 2) {
                            srcProperty = arr[0];
                            destProperty = arr[1];
                    } else {
                            srcProperty = property;
                            destProperty = property;
                    }
                    BeanUtils.setProperty(dest, destProperty, BeanUtils.getProperty(
                                    src, srcProperty));
            }
    }
    
    /**
     * copies all properties from src object to dest another
     * 
     * @param src
     *            the source object
     * @param dest
     *            the destination object
     * 
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void copyProperties(Object src, Object dest) throws IllegalAccessException,
                    InvocationTargetException, NoSuchMethodException {
    	BeanUtils.copyProperties(dest, src);
    }    
}
