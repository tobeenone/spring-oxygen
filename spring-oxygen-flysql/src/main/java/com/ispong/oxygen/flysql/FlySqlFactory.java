package com.ispong.oxygen.flysql;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.ispong.oxygen.flysql.AbstractSqlBuilder.BEAN_COLUMNS_MAP;

/**
 * sql工厂类,生成sqlBuilder对象
 *
 * @author ispong
 * @version v0.1.0
 */
public class FlySqlFactory {

    static <A> SqlBuilder<A> generateSql(Class<A> genericType) {
        SqlBuilder<A> sqlBuilder = new SqlBuilder<>();
        sqlBuilder.setGenericType(genericType);
        if (!BEAN_COLUMNS_MAP.containsKey(genericType.getName())) {
            BEAN_COLUMNS_MAP.put(genericType.getName(), parseColumns(genericType));
        }
        return sqlBuilder;
    }

    /**
     * sqlBuilder工厂 生成新的sqlBuilder
     *
     * @param genericType 泛型转移/暂存
     * @since 2019-12-23
     */
    public static <A> SqlBuilder<A> selectSql(Class<A> genericType) {

        SqlBuilder<A> generateSql = generateSql(genericType);
        String tableName = "";
        if (genericType.isAnnotationPresent(Table.class)) {
            tableName = genericType.getAnnotation(Table.class).name();
        }
        generateSql.setSqlStr(new StringBuilder("select * from " + tableName));
        return generateSql;
    }

    /**
     * 更新sql拼接
     *
     * @since 2019-12-23
     */
    static <A> SqlBuilder<A> updateSql(Class<A> genericType) {

        SqlBuilder<A> generateSql = generateSql(genericType);
        if (!genericType.isAnnotationPresent(Table.class)) {
            throw new FlysqlException("注解数据库名");
        }
        generateSql.setSqlStr(new StringBuilder("update " + genericType.getAnnotation(Table.class).name()));
        return generateSql;
    }

    /**
     * 删除sql拼接
     *
     * @since 2019-12-24
     */
    static <A> SqlBuilder<A> deleteSql(Class<A> genericType) {

        SqlBuilder<A> generateSql = generateSql(genericType);
        if (!genericType.isAnnotationPresent(Table.class)) {
            throw new FlysqlException("注解数据库名");
        }
        generateSql.setSqlStr(new StringBuilder("delete " + genericType.getAnnotation(Table.class).name()));
        return generateSql;
    }

    /**
     * 插入sql拼接
     *
     * @since 2019-12-24
     */
    static <A> SqlBuilder<A> insertSql(Class<A> genericType) {

        SqlBuilder<A> generateSql = generateSql(genericType);
        if (!genericType.isAnnotationPresent(Table.class)) {
            throw new FlysqlException("注解数据库名");
        }
        generateSql.setSqlStr(new StringBuilder("insert into " + genericType.getAnnotation(Table.class).name()));
        return generateSql;
    }

    /**
     * 解析bean对象,将注解中的数据库字段映射到对象
     *
     * @param
     * @return
     * @since 2019-12-31
     */
    static Map<String, String> parseColumns(Class<?> beanClazz) {

        Map<String, String> columnMap = new HashMap<>();

        for (Field metaField : beanClazz.getDeclaredFields()) {

            if (metaField.isAnnotationPresent(ColumnName.class)) {
                columnMap.put(metaField.getName(), metaField.getAnnotation(ColumnName.class).value());
            } else {
                columnMap.put(metaField.getName(), metaField.getName());
            }
        }

        for (Field metaField : beanClazz.getSuperclass().getDeclaredFields()) {

            if (metaField.isAnnotationPresent(ColumnName.class)) {
                columnMap.put(metaField.getName(), metaField.getAnnotation(ColumnName.class).value());
            } else {
                columnMap.put(metaField.getName(), metaField.getName());
            }
        }

        return columnMap;
    }

}
