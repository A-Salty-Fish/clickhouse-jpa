package asalty.fish.clickhousejpa.util;

import asalty.fish.clickhousejpa.annotation.ClickHouseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 别人那里抄的，没有改过
 */
@Slf4j
public class ClassScanUtil {
    public static List<Class<?>> getClickHouseEntities(String springBootPackage, String resourcePattern) {
        List<Class<?>> clickHouseEntities = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+
                    ClassUtils.convertClassNameToResourcePath(springBootPackage) + resourcePattern;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerfactory =new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource: resources) {
                MetadataReader reader = readerfactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                //判断是否有指定主解
                ClickHouseEntity anno = clazz.getAnnotation(ClickHouseEntity.class);
                if(anno != null) {
                    clickHouseEntities.add(clazz);
                    log.info("scan clickhouse entity：{}", classname);
                }
            }
        }catch(IOException | ClassNotFoundException e) {
            log.error("读取 Handler失败",e);
        }
        return clickHouseEntities;
    }
}