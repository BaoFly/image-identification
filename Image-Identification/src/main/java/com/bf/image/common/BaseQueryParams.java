package com.bf.image.common;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class BaseQueryParams {

    /**
     * 条件符号
     */
    private String condition;

    /**
     * 多表查询时，指定的表名
     */
    private String propertyType;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性值
     */
    private String val;

    /**
     * 第二属性值
     */
    private String secondVal;

    /**
     * 属性值集合
     */
    private List<Object> vals;

    public BaseQueryParams() {
    }

    private BaseQueryParams(String condition, String propertyName, String val) {
        this.condition = condition;
        this.propertyName = propertyName;
        this.val = val;
    }

    private BaseQueryParams(String condition, Long nullLong, String propertyType, String propertyName, String val) {
        this.condition = condition;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.val = val;
    }

    private BaseQueryParams(String condition, String propertyType, String propertyName, List<Object> vals) {
        this.condition = condition;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.vals = vals;
    }

    private BaseQueryParams(String condition, String propertyName, List<Object> vals) {
        this.condition = condition;
        this.propertyName = propertyName;
        this.vals = vals;
    }

    private BaseQueryParams(String condition, String propertyName, String val, String secondVal) {
        this.condition = condition;
        this.propertyName = propertyName;
        this.val = val;
        this.secondVal = secondVal;
    }

    private BaseQueryParams(String condition, String propertyType, String propertyName, String val, String secondVal) {
        this.condition = condition;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.val = val;
        this.secondVal = secondVal;
    }

    private BaseQueryParams(String condition, String propertyName) {
        this.condition = condition;
        this.propertyName = propertyName;
    }

    private BaseQueryParams(String condition, String propertyType, String propertyName, Long nullLong) {
        this.condition = condition;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
    }

    private BaseQueryParams(String propertyName, List<Object> vals) {
        this.propertyName = propertyName;
        this.vals = vals;
    }

    private BaseQueryParams(String condition, List<Object> object, Long nullLong) {
        this.condition = condition;
        this.vals = object;
    }

    public static List<BaseQueryParams> buildBaseQueryParams(BaseQueryParams... baseQueryParams) {
        ArrayList<BaseQueryParams> arrayList = new ArrayList<BaseQueryParams>(baseQueryParams.length);
        Collections.addAll(arrayList, baseQueryParams);
        return arrayList;
    }

    /**
     * 等于 =
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams eq(String propertyName, String val) {
        return new BaseQueryParams(Condition.EQ, propertyName, val);
    }

    /**
     * 等于 =
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams eq(String propertyName, Long val) {
        return new BaseQueryParams(Condition.EQ, propertyName, val.toString());
    }

    /**
     * 等于 =
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams eq(String propertyName, Integer val) {
        return new BaseQueryParams(Condition.EQ, propertyName, val.toString());
    }

    /**
     * 不等于 <>
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams ne(String propertyName, String val) {
        return new BaseQueryParams(Condition.NE, propertyName, val);
    }

    /**
     * 大于 >
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams gt(String propertyName, String val) {
        return new BaseQueryParams(Condition.GT, propertyName, val);
    }

    /**
     * 大于等于 >=
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams ge(String propertyName, String val) {
        return new BaseQueryParams(Condition.GE, propertyName, val);
    }

    /**
     * 小于 <
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams lt(String propertyName, String val) {
        return new BaseQueryParams(Condition.LT, propertyName, val);
    }

    /**
     * 小于等于 <=
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams le(String propertyName, String val) {
        return new BaseQueryParams(Condition.LE, propertyName, val);
    }

    /**
     * 大于 >
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams gt(String propertyName, Date val) {
        return new BaseQueryParams(Condition.GT, propertyName, DateUtil.formatDateTime(val));
    }

    /**
     * 大于等于 >=
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams ge(String propertyName, Date val) {
        return new BaseQueryParams(Condition.GE, propertyName, DateUtil.formatDateTime(val));
    }

    /**
     * 小于 <
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams lt(String propertyName, Date val) {
        return new BaseQueryParams(Condition.LT, propertyName, DateUtil.formatDateTime(val));
    }

    /**
     * 小于等于 <=
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams le(String propertyName, Date val) {
        return new BaseQueryParams(Condition.LE, propertyName, DateUtil.formatDateTime(val));
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param propertyName
     * @param val
     * @param secondVal
     * @return
     */
    public static BaseQueryParams between(String propertyName, String val, String secondVal) {
        return new BaseQueryParams(Condition.BETWEEN, propertyName, val, secondVal);
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param propertyName
     * @param val
     * @param secondVal
     * @return
     */
    public static BaseQueryParams between(String propertyType, String propertyName, Date val, Date secondVal) {
        return new BaseQueryParams(Condition.BETWEEN, propertyType, propertyName, DateUtil.formatDateTime(val), DateUtil.formatDateTime(secondVal));
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param propertyName
     * @param val
     * @param secondVal
     * @return
     */
    public static BaseQueryParams between(String propertyName, Date val, Date secondVal) {
        return new BaseQueryParams(Condition.BETWEEN, propertyName, DateUtil.formatDateTime(val), DateUtil.formatDateTime(secondVal));
    }

    /**
     * NOT BETWEEN 值1 AND 值2
     *
     * @param propertyName
     * @param val
     * @param secondVal
     * @return
     */
    public static BaseQueryParams notBetween(String propertyName, String val, String secondVal) {
        return new BaseQueryParams(Condition.NOTBETWEEN, propertyName, val, secondVal);
    }

    /**
     * LIKE '%值%'
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams like(String propertyName, String val) {
        return new BaseQueryParams(Condition.LIKE, propertyName, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams notLike(String propertyName, String val) {
        return new BaseQueryParams(Condition.NOTLIKE, propertyName, val);
    }

    /**
     * LIKE '%值'
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams likeLeft(String propertyName, String val) {
        return new BaseQueryParams(Condition.LIKELEFT, propertyName, val);
    }

    /**
     * LIKE '值%'
     *
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams likeRight(String propertyName, String val) {
        return new BaseQueryParams(Condition.LIKERIGHT, propertyName, val);
    }

    /**
     * 字段 IS NULL
     *
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams isNull(String propertyName) {
        return new BaseQueryParams(Condition.ISNULL, propertyName);
    }

    /**
     * 字段 IS NOT NULL
     *
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams isNotNull(String propertyName) {
        return new BaseQueryParams(Condition.ISNOTNULL, propertyName);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     *
     * @param propertyName 属性名称
     * @param vals         值的集合
     * @return
     */
    public static BaseQueryParams in(String propertyName, List<Object> vals) {
        return new BaseQueryParams(Condition.IN, propertyName, vals);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     *
     * @param propertyName 属性名称
     * @param vals         值的集合
     * @return
     */
    public static BaseQueryParams notIn(String propertyName, List<Object> vals) {
        return new BaseQueryParams(Condition.NOTIN, propertyName, vals);
    }

    /**
     * 分组：GROUP BY 字段
     *
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams groupBy(String propertyName) {
        return new BaseQueryParams(Condition.GROUPBY, propertyName);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     *
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams orderByAsc(String propertyName) {
        return new BaseQueryParams(Condition.ORDERBYASC, propertyName);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     *
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams orderByDesc(String propertyName) {
        return new BaseQueryParams(Condition.ORDERBYDESC, propertyName);
    }

    /**
     * 等于 =
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams eq(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.EQ, 0L, propertyType, propertyName, val);
    }

    /**
     * 等于 =
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams eq(String propertyType, String propertyName, Long val) {
        return new BaseQueryParams(Condition.EQ, 0L, propertyType, propertyName, val.toString());
    }

    /**
     * 不等于 <>
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams ne(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.NE, 0L, propertyType, propertyName, val);
    }

    /**
     * 大于 >
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams gt(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.GT, 0L, propertyType, propertyName, val);
    }

    /**
     * 大于等于 >=
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams ge(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.GE, 0L, propertyType, propertyName, val);
    }

    /**
     * 小于 <
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams lt(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.LT, 0L, propertyType, propertyName, val);
    }

    /**
     * 小于等于 <=
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams le(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.LE, 0L, propertyType, propertyName, val);
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param propertyName 指定表名
     * @param propertyName
     * @param val
     * @param secondVal
     * @return
     */
    public static BaseQueryParams between(String propertyType, String propertyName, String val, String secondVal) {
        return new BaseQueryParams(Condition.BETWEEN, propertyType, propertyName, val, secondVal);
    }

    /**
     * NOT BETWEEN 值1 AND 值2
     *
     * @param propertyName 指定表名
     * @param propertyName
     * @param val
     * @param secondVal
     * @return
     */
    public static BaseQueryParams notBetween(String propertyType, String propertyName, String val, String secondVal) {
        return new BaseQueryParams(Condition.NOTBETWEEN, propertyType, propertyName, val, secondVal);
    }

    /**
     * LIKE '%值%'
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams like(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.LIKE, 0L, propertyType, propertyName, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams notLike(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.NOTLIKE, 0L, propertyType, propertyName, val);
    }

    /**
     * LIKE '%值'
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams likeLeft(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.LIKELEFT, 0L, propertyType, propertyName, val);
    }

    /**
     * LIKE '值%'
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param val          值
     * @return
     */
    public static BaseQueryParams likeRight(String propertyType, String propertyName, String val) {
        return new BaseQueryParams(Condition.LIKERIGHT, 0L, propertyType, propertyName, val);
    }

    /**
     * 字段 IS NULL
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams isNull(String propertyType, String propertyName) {
        return new BaseQueryParams(Condition.ISNULL, propertyType, propertyName, 0L);
    }

    /**
     * 字段 IS NOT NULL
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams isNotNull(String propertyType, String propertyName) {
        return new BaseQueryParams(Condition.ISNOTNULL, propertyType, propertyName, 0L);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param vals         值的集合
     * @return
     */
    public static BaseQueryParams in(String propertyType, String propertyName, List<Object> vals) {
        return new BaseQueryParams(Condition.IN, propertyType, propertyName, vals);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @param vals         值的集合
     * @return
     */
    public static BaseQueryParams notIn(String propertyType, String propertyName, List<Object> vals) {
        return new BaseQueryParams(Condition.NOTIN, propertyType, propertyName, vals);
    }

    /**
     * 分组：GROUP BY 字段
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams groupBy(String propertyType, String propertyName) {
        return new BaseQueryParams(Condition.GROUPBY, propertyType, propertyName, 0L);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams orderByAsc(String propertyType, String propertyName) {
        return new BaseQueryParams(Condition.ORDERBYASC, propertyType, propertyName, 0L);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     *
     * @param propertyName 指定表名
     * @param propertyName 属性名称
     * @return
     */
    public static BaseQueryParams orderByDesc(String propertyType, String propertyName) {
        return new BaseQueryParams(Condition.ORDERBYDESC, propertyType, propertyName, 0L);
    }

    /**
     * 拼接 OR
     *
     * @return
     */
    public static BaseQueryParams or() {
        return new BaseQueryParams(Condition.OR, "");
    }

    /**
     * 拼接 and
     *
     * @return
     */
    public static BaseQueryParams and(BaseQueryParams... andBaseQueryParams) {
        List<Object> list = new ArrayList<>();
        list.add(andBaseQueryParams);
        return new BaseQueryParams(Condition.AND, list, null);
    }

    @Override
    public String toString() {
        return "BaseQueryParams{" +
                "condition='" + condition + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", val='" + val + '\'' +
                ", secondVal='" + secondVal + '\'' +
                ", vals=" + vals +
                '}';
    }

    public class Condition {
        public final static String EQ = "eq";
        public final static String NE = "ne";
        public final static String GT = "gt";
        public final static String GE = "ge";
        public final static String LT = "lt";
        public final static String LE = "le";
        public final static String BETWEEN = "between";
        public final static String NOTBETWEEN = "notBetween";
        public final static String LIKE = "like";
        public final static String NOTLIKE = "notLike";
        public final static String LIKELEFT = "likeLeft";
        public final static String LIKERIGHT = "likeRight";
        public final static String ISNULL = "isNull";
        public final static String ISNOTNULL = "isNotNull";
        public final static String IN = "in";
        public final static String NOTIN = "notIn";
        public final static String GROUPBY = "groupBy";
        public final static String ORDERBYASC = "orderByAsc";
        public final static String ORDERBYDESC = "orderByDesc";
        public final static String OR = "or";
        public final static String AND = "and";
    }

    public class SysMethod {
        public final static String SAVE = "save";
        public final static String SAVEBATCH = "saveBatch";
        public final static String SAVEORUPDATEBATCH = "saveOrUpdateBatch";
        public final static String REMOVEBYID = "removeById";
        public final static String REMOVE = "remove";
        public final static String REMOVEBYIDS = "removeByIds";
        public final static String UPDATEBYID = "updateById";
        public final static String UPDATE = "update";
        public final static String UPDATEBATCHBYID = "updateBatchById";
        public final static String SAVEORUPDATE = "saveOrUpdate";
        public final static String GETBYID = "getById";
        public final static String GETONE = "getOne";
        public final static String LISTBYIDS = "listByIds";
        public final static String COUNT = "count";
        public final static String LIST = "list";
        public final static String PAGE = "page";
    }
}