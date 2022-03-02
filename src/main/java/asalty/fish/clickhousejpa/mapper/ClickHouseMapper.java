package asalty.fish.clickhousejpa.mapper;

import asalty.fish.clickhousejpa.annotation.ClickHouseColumn;
import asalty.fish.clickhousejpa.exception.TypeNotSupportException;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 13090
 * @version 1.0
 * @description: ClickHouse jdbc的映射器
 * @date 2022/2/28 14:50
 */
@Configuration
public class ClickHouseMapper {
    ConcurrentHashMap<String, HashMap<String, Integer>> columnNameMap = new ConcurrentHashMap<>(16);

    public void addColumnName(String tableName, String columnName, Integer columnIndex) {
        HashMap<String, Integer> columnNameMap = this.columnNameMap.computeIfAbsent(tableName, k -> new HashMap<>(16));
        columnNameMap.put(columnName, columnIndex);
    }

    public Integer getColumnIndex(String tableName, String columnName) {
        HashMap<String, Integer> columnNameMap = this.columnNameMap.get(tableName);
        if (columnNameMap == null) {
            return null;
        }
        return columnNameMap.get(columnName);
    }

    public void addColumnName(String tableName, ResultSet resultSet) throws Exception {
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            addColumnName(tableName, columnName, i);
        }
    }

    public <T> T convertResultSetToClass(ResultSet rs, Class<T> clazz) {
        String tableName = clazz.getSimpleName();
        if (columnNameMap.get(tableName) == null) {
            try {
                addColumnName(tableName, rs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            T t = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                ClickHouseColumn clickHouseColumn = field.getAnnotation(ClickHouseColumn.class);
                String fieldName = field.getName();
                String columnName = field.getName();
                // 尝试拿出用户自定义的表列名
                if (clickHouseColumn != null && clickHouseColumn.name() != null && !"".equals(clickHouseColumn.name())) {
                    columnName = clickHouseColumn.name();
                }
                Integer columnIndex = getColumnIndex(tableName, columnName);
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class<?> type = field.getType();
                Method method = clazz.getMethod(methodName, type);
                convertAndSetStringToOtherType(t, type, method, rs.getString(columnIndex));
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 clichouse 返回的 string 值转换为对应的类型
     */
    public void convertAndSetStringToOtherType(Object t, Class<?> type, Method method, String value) {
        try {
            if (type == String.class) {
                method.invoke(t, value);
            } else if (type == Long.class) {
                method.invoke(t, Long.parseLong(value));
            } else if (type == Boolean.class) {
                method.invoke(t, Boolean.parseBoolean(value));
            } else if (type == LocalDateTime.class) {
                method.invoke(t, LocalDateTime.parse(value));
            } else if (type == LocalDate.class) {
                method.invoke(t, LocalDate.parse(value));
            } else {
                throw new TypeNotSupportException("type not support: " + type.getSimpleName());
            }
            // todo 支持其他类型
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将java类型转换为clickhouse支持的字符串
     * @param target
     * @param field
     * @return
     * @throws IllegalAccessException
     * @throws TypeNotSupportException
     */
    public String convertTypeToString(Object target, Field field) throws IllegalAccessException, TypeNotSupportException {
        String value;
        if (field.getType().equals(String.class)) {
            value = "'" + field.get(target).toString() + "'";
        } else if (field.getType().equals(Boolean.class)) {
            value = "" + ((Boolean) field.get(target) ? "1" : "0");
        } else if (field.getType().equals(LocalDateTime.class)) {
            value = "'" + ((LocalDateTime) field.get(target)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'";
        } else if (field.getType().equals(LocalDate.class)) {
            value = "'" + ((LocalDate) field.get(target)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "'";
        } else if (field.getType().equals(Long.class)) {
            value = "" + field.get(target);
        } else if (field.getType().equals(Double.class)) {
            value = "" + field.get(target);
        } else if (field.getType().equals(Integer.class)) {
            value = "" + field.get(target);
        } else {
            throw new TypeNotSupportException("type not support: " + field.getType().getSimpleName());
        }
        return value;
    }

    public <T> List<T> convertResultSetToList(ResultSet rs, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(convertResultSetToClass(rs, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
