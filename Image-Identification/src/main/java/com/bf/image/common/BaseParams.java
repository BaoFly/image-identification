package com.bf.image.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.common.util.SpringUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@Slf4j
public class BaseParams {

    /**
     * 主键
     */
    private Long id;

    /**
     * 主键集合
     */
    private List<Long> ids;

    /**
     * 操作实体
     */
    private Object obj;

    /**
     * 操作实体集合
     */
    private List<Object> objs;

    /**
     * 页数
     */
    private long size;

    /**
     * 第几页
     */
    private long current;

    /**
     * 查询条件
     */
    private List<BaseQueryParams> baseQueryParams;

    /**
     * 上传文件集合
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MultipartFile[] multipartFiles;

    /**
     * 业务键
     */
    private String businessKey;

    /**
     * 字段名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] sf;

    public BaseParams() {
    }

    private BaseParams(Long id) {
        this.id = id;
    }

    private BaseParams(String nullStr, List<Long> ids) {
        this.ids = ids;
    }

    private BaseParams(Object obj) {
        this.obj = obj;
    }

    private BaseParams(Object obj, List<BaseQueryParams> baseQueryParams) {
        this.obj = obj;
        this.baseQueryParams = baseQueryParams;
    }

    private BaseParams(List<Object> objs, String nullStr) {
        this.objs = objs;
    }

    private BaseParams(List<BaseQueryParams> baseQueryParams) {
        this.baseQueryParams = baseQueryParams;
    }

    private BaseParams(String[] sf, List<BaseQueryParams> baseQueryParams) {
        this.sf = sf;
        this.baseQueryParams = baseQueryParams;
    }

    private BaseParams(long size, long current, List<BaseQueryParams> baseQueryParams) {
        this.size = size;
        this.current = current;
        this.baseQueryParams = baseQueryParams;
    }

    private BaseParams(long size, long current, String[] sf, List<BaseQueryParams> baseQueryParams) {
        this.size = size;
        this.current = current;
        this.baseQueryParams = baseQueryParams;
        this.sf = sf;
    }


    /**
     * 设置主键ID
     *
     * @param id 主键ID
     * @return
     */
    public static BaseParams bulidById(Long id) {
        return new BaseParams(id);
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     * @return
     */
    public static BaseParams bulidById(String id) {
        return new BaseParams(Long.valueOf(id));
    }

    /**
     * 设置主键ID集合
     *
     * @param ids 主键ID集合
     * @return
     */
    public static BaseParams bulidByIds(List<Long> ids) {
        return new BaseParams("", ids);
    }

    /**
     * 设置操作实体
     *
     * @param obj 实体
     * @return
     */
    public static BaseParams bulidByObject(Object obj) {
        return new BaseParams(obj);
    }

    /**
     * 设置操作实体集合
     *
     * @param objs 实体集合
     * @return
     */
    public static BaseParams bulidByObjects(List<Object> objs) {
        return new BaseParams(objs, "");
    }

    /**
     * 设置查询条件集合
     *
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams bulidByQuery(List<BaseQueryParams> baseQueryParams) {
        return new BaseParams(baseQueryParams);
    }

    /**
     * 设置查询条件集合
     *
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams bulidByQuery(BaseQueryParams... baseQueryParams) {
        ArrayList<BaseQueryParams> arrayList = new ArrayList<BaseQueryParams>(baseQueryParams.length);
        Collections.addAll(arrayList, baseQueryParams);
        return bulidByQuery(arrayList);
    }

    /**
     * 设置查询条件
     *
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams bulidByQuery(BaseQueryParams baseQueryParams) {
        List<BaseQueryParams> list = new ArrayList<>();
        list.add(baseQueryParams);
        return new BaseParams(list);
    }

    /**
     * 设置查询条件
     *
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams addQuery(BaseParams baseParams, BaseQueryParams baseQueryParams) {
        baseParams.baseQueryParams.add(baseQueryParams);
        return baseParams;
    }

    /**
     * 设置更新实体及更新条件
     *
     * @param obj             待更新实体
     * @param baseQueryParams 更新条件集合
     * @return
     */
    public static BaseParams bulidByUpdateAndQuery(Object obj, List<BaseQueryParams> baseQueryParams) {
        return new BaseParams(obj, baseQueryParams);
    }

    /**
     * 设置更新实体及更新条件
     *
     * @param obj             待更新实体
     * @param baseQueryParams 更新条件集合
     * @return
     */
    public static BaseParams bulidByUpdateAndQuery(Object obj, BaseQueryParams... baseQueryParams) {
        ArrayList<BaseQueryParams> arrayList = new ArrayList<BaseQueryParams>(baseQueryParams.length);
        Collections.addAll(arrayList, baseQueryParams);
        return new BaseParams(obj, arrayList);
    }

    /**
     * 设置更新实体及更新条件
     *
     * @param obj             待更新实体
     * @param baseQueryParams 更新条件
     * @return
     */
    public static BaseParams bulidByUpdateAndQuery(Object obj, BaseQueryParams baseQueryParams) {
        ArrayList<BaseQueryParams> arrayList = new ArrayList<BaseQueryParams>();
        arrayList.add(baseQueryParams);
        return new BaseParams(obj, arrayList);
    }

    /**
     * 设置条件返回分页数据
     *
     * @param size            条数
     * @param current         当前页数
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams buildByPage(long size, long current, List<BaseQueryParams> baseQueryParams) {
        return new BaseParams(size, current, baseQueryParams);
    }

    /**
     * 设置条件返回分页数据
     *
     * @param size            条数
     * @param current         当前页数
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams buildByPage(long size, long current, BaseQueryParams... baseQueryParams) {
        ArrayList<BaseQueryParams> arrayList = new ArrayList<BaseQueryParams>(baseQueryParams.length);
        Collections.addAll(arrayList, baseQueryParams);
        return new BaseParams(size, current, arrayList);
    }

    private static final ObjectMapper jsonParser;

    static {
//        long start = System.nanoTime();
        jsonParser = new ObjectMapper();
        SimpleFilterProvider provider = new SimpleFilterProvider();
        provider.setFailOnUnknownId(false);
        jsonParser.setFilterProvider(provider);
//        long end = System.nanoTime();
//        System.out.println("getBaseResult创建时间:" + (end - start));
    }
    /**
     * 设置查询条件集合
     *
     * @param searchField     查询字段
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams bulidByQuery(String[] searchField, List<BaseQueryParams> baseQueryParams) {
        return new BaseParams(searchField, baseQueryParams);
    }

    /**
     * 设置查询条件集合
     *
     * @param searchField     查询字段
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams bulidByQuery(String[] searchField, BaseQueryParams... baseQueryParams) {
        ArrayList<BaseQueryParams> arrayList = new ArrayList<BaseQueryParams>(baseQueryParams.length);
        Collections.addAll(arrayList, baseQueryParams);
        return bulidByQuery(searchField, arrayList);
    }

    /**
     * 设置查询条件
     *
     * @param searchField     查询字段
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams bulidByQuery(String[] searchField, BaseQueryParams baseQueryParams) {
        List<BaseQueryParams> list = new ArrayList<>();
        list.add(baseQueryParams);
        return new BaseParams(searchField, list);
    }

    /**
     * 设置条件返回分页数据
     *
     * @param size            条数
     * @param current         当前页数
     * @param searchField     查询字段
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams buildByPage(long size, long current, String[] searchField, List<BaseQueryParams> baseQueryParams) {
        return new BaseParams(size, current, searchField, baseQueryParams);
    }

    /**
     * 设置条件返回分页数据
     *
     * @param size            条数
     * @param current         当前页数
     * @param searchField     查询字段
     * @param baseQueryParams 查询条件
     * @return
     */
    public static BaseParams buildByPage(long size, long current, String[] searchField, BaseQueryParams... baseQueryParams) {
        ArrayList<BaseQueryParams> arrayList = new ArrayList<BaseQueryParams>(baseQueryParams.length);
        Collections.addAll(arrayList, baseQueryParams);
        return new BaseParams(size, current, searchField, arrayList);
    }

    public static BaseResult getBaseResult(String beanName, String methodName, BaseParams params) {
        ServiceImpl serviceImpl = null;
        Object result = null;
        //根据beanname获取容器中servicebean
        serviceImpl = (ServiceImpl) SpringUtil.getBean(lowerFirstCapse(beanName) + "ServiceImpl");
        //获取对于beanname的class类型
        try {
            Method get = serviceImpl.getClass().getDeclaredMethod("getClazz");
            result = get.invoke(serviceImpl);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(getStackTraceInfo(e));
        }
        Class<?> clazz = (Class<?>) result;
        //因feign远程调用对象会为linkhashmap,需转换成对于bean类型
        if (params.getObj() != null) {
            try {
                params.setObj(jsonParser.convertValue(params.getObj(), clazz));
                //判断更新还是新增
                checkSaveOrUpdate(methodName, clazz, params.getObj());
            } catch (Exception e) {

                log.warn("框架内部转换失败，不影响程序运行,原因是BaseParams#getObj()获取的对象中有{}不具备的字段导致,错误信息：{}," +
                        "为避免这种情况,建议传输对象时，在服务中新增一个方法，采用DTO的方式传输。",
                    clazz,
                    e.getMessage());
            }
        }
        if (params.getObjs() != null) {
            CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            params.setObjs(jsonParser.convertValue(params.getObjs(), listType));
            //判断更新还是新增
            checkSaveOrUpdate(methodName, clazz, params.getObjs().toArray());
            // TODO  此处应该也会存在类似上面的转型错误, 需要进行处理
        }


        try {
            Object objs = BaseParams.invoke(serviceImpl, methodName, beanName, params);
            if (objs instanceof Boolean) {
                if (!(Boolean) objs) {
                    return BaseResult.failGenerator(BaseReturnEnum.FAIL);
                }
            }
            if (objs == null) {
                return BaseResult.failGenerator(BaseReturnEnum.FAIL);
            }
            return BaseResult.successGenerator(objs);
        } catch (InvocationTargetException e) {
            log.error(getStackTraceInfo(e));
            if (e.getTargetException() instanceof BaseException) {
                BaseException ex = (BaseException) e.getTargetException();
                if (StringUtils.isNotBlank(ex.getMsg())) {
                    return BaseResult.failGenerator(ex.getCode(), ex.getMsg());
                }
                return BaseResult.failGenerator(ex.getBaseReturnEnum());
            }
        } catch (Exception e) {
            log.error(getStackTraceInfo(e));
        }
        return BaseResult.failGenerator(BaseReturnEnum.UNKNOWN_ERROR);
    }

    private static String lowerFirstCapse(String str) {

        char[] chars = str.toCharArray();

        chars[0] += 32;

        return String.valueOf(chars);

    }

    public static Object invoke(ServiceImpl serviceImpl, String methodName, String beanName, BaseParams params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        switch (methodName) {
            case BaseQueryParams.SysMethod.SAVE:
                if (serviceImpl.save(params.getObj())) {
                    return params.getObj();
                }
                return null;
            case BaseQueryParams.SysMethod.SAVEBATCH:
                if (serviceImpl.saveBatch(params.getObjs(), params.getObjs().size())) {
                    return params.getObjs();
                }
                return null;
            case BaseQueryParams.SysMethod.SAVEORUPDATEBATCH:
                if (serviceImpl.saveOrUpdateBatch(params.getObjs(), params.getObjs().size())) {
                    return params.getObjs();
                }
                return null;
            case BaseQueryParams.SysMethod.REMOVEBYID:
                return serviceImpl.removeById(params.getId());
            case BaseQueryParams.SysMethod.REMOVE:
                //更新没有条件时 直接不允许remove
                if(params.getBaseQueryParams() == null || params.getBaseQueryParams().size()==0){
                    throw new BaseException(BaseReturnEnum.BASE_QUERY_PARAMETER_ERROR);
                }
                return serviceImpl.remove(getBaseUpdateWrapper(methodName, params));
            case BaseQueryParams.SysMethod.REMOVEBYIDS:
                return serviceImpl.removeByIds(params.getIds());
            case BaseQueryParams.SysMethod.UPDATEBYID:
                return serviceImpl.updateById(params.getObj());
            case BaseQueryParams.SysMethod.UPDATE:
                //更新没有条件时 直接不允许update
                if(params.getBaseQueryParams() == null || params.getBaseQueryParams().size()==0){
                    throw new BaseException(BaseReturnEnum.BASE_QUERY_PARAMETER_ERROR);
                }
                return serviceImpl.update(params.getObj(), getBaseUpdateWrapper(methodName, params));
            case BaseQueryParams.SysMethod.UPDATEBATCHBYID:
                return serviceImpl.updateBatchById(params.getObjs(), params.getObjs().size());
            case BaseQueryParams.SysMethod.SAVEORUPDATE:
                if (serviceImpl.saveOrUpdate(params.getObj())) {
                    return params.getObj();
                }
                return null;
            case BaseQueryParams.SysMethod.GETBYID:
                return serviceImpl.getById(params.getId());
            case BaseQueryParams.SysMethod.GETONE:
                return serviceImpl.getOne(getBaseQueryWrapper(methodName, params));
            case BaseQueryParams.SysMethod.LISTBYIDS:
                return serviceImpl.listByIds(params.getIds());
            case BaseQueryParams.SysMethod.COUNT:
                return serviceImpl.count(getBaseQueryWrapper(methodName, params));
            case BaseQueryParams.SysMethod.LIST:
                return serviceImpl.list(getBaseQueryWrapper(methodName, params));
            case BaseQueryParams.SysMethod.PAGE:
                return serviceImpl.page(new Page(params.getCurrent(), params.getSize()), getBaseQueryWrapper(methodName, params));
            default:
                // 自定义方法
                Method method = null;
                //获取相关方法，填充queryWrapper
                final String lowerCase = methodName.toLowerCase();
                final Class<? extends ServiceImpl> serviceImplClass = serviceImpl.getClass();
                if (lowerCase.contains("get".toLowerCase()) || lowerCase.contains("select".toLowerCase())) {
                    Method get = serviceImplClass.getDeclaredMethod(methodName, BaseParams.class, BaseQueryWrapper.class);
                    return get.invoke(serviceImpl, params, getBaseQueryWrapper(methodName, params));
                    //新增、修改、删除，填充updateWrapper
                } else if (lowerCase.contains(BaseQueryParams.SysMethod.SAVE.toLowerCase())
                    || lowerCase.contains(BaseQueryParams.SysMethod.UPDATE.toLowerCase())
                    || lowerCase.contains(BaseQueryParams.SysMethod.REMOVE.toLowerCase())) {
                    Method get = serviceImplClass.getDeclaredMethod(methodName, BaseParams.class, BaseUpdateWrapper.class);
                    return get.invoke(serviceImpl, params, getBaseUpdateWrapper(methodName, params));
                } else {
                    //其他则不填充
                    Method get = serviceImplClass.getDeclaredMethod(methodName, BaseParams.class);
                    return get.invoke(serviceImpl, params);
                }

        }
    }

    static BaseQueryWrapper getBaseQueryWrapper(String methodName, BaseParams params) {
        BaseQueryWrapper<?> baseQueryWrapper = new BaseQueryWrapper();
        boolean voFlag = false;
        if (methodName.toLowerCase().contains("vo".toLowerCase())) {
            voFlag = true;
        }
        final String[] sf = params.getSf();
        List<BaseQueryParams> baseQueryParams = params.getBaseQueryParams();
        baseQueryParams =  baseQueryParams == null ? new ArrayList<>() : baseQueryParams;

        // 2021/7/28 xjx fix sf单独有值是不提前返回
        if (params == null || (baseQueryParams == null && sf == null)) {
            return baseQueryWrapper;
        }

        if (sf != null && sf.length > 0) {
            for (int i = 0; i < params.sf.length; i++) {
                params.sf[i] = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(params.sf[i]);
            }
            baseQueryWrapper.select((params.sf));
        }
        //查询条件逻辑
        for (BaseQueryParams baseQueryParam : baseQueryParams) {
            String propertyName = baseQueryParam.getPropertyName();
            if (propertyName != null) {
                baseQueryParam.setPropertyName(voFlag && baseQueryParam.getPropertyType() != null ? (baseQueryParam.getPropertyType() + "." + propertyName) : propertyName);
            }
            propertyName = baseQueryParam.getPropertyName();
            final String val = baseQueryParam.getVal();
            final String secondVal = baseQueryParam.getSecondVal();
            final List<Object> vals = baseQueryParam.getVals();
            switch (baseQueryParam.getCondition()) {
                case BaseQueryParams.Condition.EQ:
                    baseQueryWrapper.eq(propertyName, val);
                    break;
                case BaseQueryParams.Condition.NE:
                    baseQueryWrapper.ne(propertyName, val);
                    break;
                case BaseQueryParams.Condition.GT:
                    baseQueryWrapper.gt(propertyName, val);
                    break;
                case BaseQueryParams.Condition.GE:
                    baseQueryWrapper.ge(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LT:
                    baseQueryWrapper.lt(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LE:
                    baseQueryWrapper.le(propertyName, val);
                    break;
                case BaseQueryParams.Condition.BETWEEN:
                    baseQueryWrapper.between(propertyName, val, secondVal);
                    break;
                case BaseQueryParams.Condition.NOTBETWEEN:
                    baseQueryWrapper.notBetween(propertyName, val, secondVal);
                    break;
                case BaseQueryParams.Condition.LIKE:
                    baseQueryWrapper.like(propertyName, val);
                    break;
                case BaseQueryParams.Condition.NOTLIKE:
                    baseQueryWrapper.notLike(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LIKELEFT:
                    baseQueryWrapper.likeLeft(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LIKERIGHT:
                    baseQueryWrapper.likeRight(propertyName, val);
                    break;
                case BaseQueryParams.Condition.ISNULL:
                    baseQueryWrapper.isNull(propertyName);
                    break;
                case BaseQueryParams.Condition.ISNOTNULL:
                    baseQueryWrapper.isNotNull(propertyName);
                    break;
                case BaseQueryParams.Condition.IN:
                    baseQueryWrapper.in(propertyName, vals);
                    break;
                case BaseQueryParams.Condition.NOTIN:
                    baseQueryWrapper.notIn(propertyName, vals);
                    break;
                case BaseQueryParams.Condition.GROUPBY:
                    baseQueryWrapper.groupBy(propertyName);
                    break;
                case BaseQueryParams.Condition.ORDERBYASC:
                    baseQueryWrapper.orderByAsc(propertyName);
                    break;
                case BaseQueryParams.Condition.ORDERBYDESC:
                    baseQueryWrapper.orderByDesc(propertyName);
                    break;
                case BaseQueryParams.Condition.OR:
                    baseQueryWrapper.or();
                    break;
                case BaseQueryParams.Condition.AND:
                    baseQueryWrapper.and(i -> {
                        for (BaseQueryParams baseQueryParams1 : JSONArray.parseArray(JSONObject.toJSONString(vals.get(0)), BaseQueryParams.class)) {
                            final String column = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(baseQueryParams1.getPropertyName());
                            final String val1 = baseQueryParams1.getVal();
                            final List<Object> vals1 = baseQueryParams1.getVals();
                            final String secondVal1 = baseQueryParams1.getSecondVal();
                            switch (baseQueryParams1.getCondition()) {
                                case BaseQueryParams.Condition.EQ:
                                    i.eq(column, val1);
                                    break;
                                case BaseQueryParams.Condition.NE:
                                    i.ne(column, val1);
                                    break;
                                case BaseQueryParams.Condition.GT:
                                    i.gt(column, val1);
                                    break;
                                case BaseQueryParams.Condition.GE:
                                    i.ge(column, val1);
                                    break;
                                case BaseQueryParams.Condition.LT:
                                    i.lt(column, val1);
                                    break;
                                case BaseQueryParams.Condition.LE:
                                    i.le(column, val1);
                                    break;
                                case BaseQueryParams.Condition.BETWEEN:
                                    i.between(column, val1, secondVal1);
                                    break;
                                case BaseQueryParams.Condition.NOTBETWEEN:
                                    i.notBetween(column, val1, secondVal1);
                                    break;
                                case BaseQueryParams.Condition.LIKE:
                                    i.like(column, val1);
                                    break;
                                case BaseQueryParams.Condition.NOTLIKE:
                                    i.notLike(column, val1);
                                    break;
                                case BaseQueryParams.Condition.LIKELEFT:
                                    i.likeLeft(column, val1);
                                    break;
                                case BaseQueryParams.Condition.LIKERIGHT:
                                    i.likeRight(column, val1);
                                    break;
                                case BaseQueryParams.Condition.ISNULL:
                                    i.isNull(column);
                                    break;
                                case BaseQueryParams.Condition.ISNOTNULL:
                                    i.isNotNull(column);
                                    break;
                                case BaseQueryParams.Condition.IN:
                                    i.in(column, vals1);
                                    break;
                                case BaseQueryParams.Condition.NOTIN:
                                    i.notIn(column, vals1);
                                    break;
                                case BaseQueryParams.Condition.GROUPBY:
                                    i.groupBy(column);
                                    break;
                                case BaseQueryParams.Condition.ORDERBYASC:
                                    i.orderByAsc(column);
                                    break;
                                case BaseQueryParams.Condition.ORDERBYDESC:
                                    i.orderByDesc(column);
                                    break;
                                case BaseQueryParams.Condition.OR:
                                    i.or();
                                    break;
                                default:
                            }
                        }
                    });
                    break;
                default:
            }
        }
        return baseQueryWrapper;
    }

    static BaseUpdateWrapper getBaseUpdateWrapper(String methodName, BaseParams params) {
        BaseUpdateWrapper baseUpdateWrapper = new BaseUpdateWrapper();
        boolean voFlag = false;
        if (methodName.toLowerCase().contains("vo".toLowerCase())) {
            voFlag = true;
        }
        if (params == null || params.getBaseQueryParams() == null) {
            return baseUpdateWrapper;
        }
        //查询条件逻辑
        for (BaseQueryParams baseQueryParam : params.getBaseQueryParams()) {
            String propertyName = baseQueryParam.getPropertyName();
            if (propertyName != null) {
                baseQueryParam.setPropertyName(voFlag ? (baseQueryParam.getPropertyType() + "." + propertyName) : propertyName);
            }
            propertyName = baseQueryParam.getPropertyName();
            final String val = baseQueryParam.getVal();
            final String secondVal = baseQueryParam.getSecondVal();
            final List<Object> vals = baseQueryParam.getVals();
            switch (baseQueryParam.getCondition()) {
                case BaseQueryParams.Condition.EQ:
                    baseUpdateWrapper.eq(propertyName, val);
                    break;
                case BaseQueryParams.Condition.NE:
                    baseUpdateWrapper.ne(propertyName, val);
                    break;
                case BaseQueryParams.Condition.GT:
                    baseUpdateWrapper.gt(propertyName, val);
                    break;
                case BaseQueryParams.Condition.GE:
                    baseUpdateWrapper.ge(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LT:
                    baseUpdateWrapper.lt(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LE:
                    baseUpdateWrapper.le(propertyName, val);
                    break;
                case BaseQueryParams.Condition.BETWEEN:
                    baseUpdateWrapper.between(propertyName, val, secondVal);
                    break;
                case BaseQueryParams.Condition.NOTBETWEEN:
                    baseUpdateWrapper.notBetween(propertyName, val, secondVal);
                    break;
                case BaseQueryParams.Condition.LIKE:
                    baseUpdateWrapper.like(propertyName, val);
                    break;
                case BaseQueryParams.Condition.NOTLIKE:
                    baseUpdateWrapper.notLike(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LIKELEFT:
                    baseUpdateWrapper.likeLeft(propertyName, val);
                    break;
                case BaseQueryParams.Condition.LIKERIGHT:
                    baseUpdateWrapper.likeRight(propertyName, val);
                    break;
                case BaseQueryParams.Condition.ISNULL:
                    baseUpdateWrapper.isNull(propertyName);
                    break;
                case BaseQueryParams.Condition.ISNOTNULL:
                    baseUpdateWrapper.isNotNull(propertyName);
                    break;
                case BaseQueryParams.Condition.IN:
                    baseUpdateWrapper.in(propertyName, vals);
                    break;
                case BaseQueryParams.Condition.NOTIN:
                    baseUpdateWrapper.notIn(propertyName, vals);
                    break;
                case BaseQueryParams.Condition.GROUPBY:
                    baseUpdateWrapper.groupBy(propertyName);
                    break;
                case BaseQueryParams.Condition.ORDERBYASC:
                    baseUpdateWrapper.orderByAsc(propertyName);
                    break;
                case BaseQueryParams.Condition.ORDERBYDESC:
                    baseUpdateWrapper.orderByDesc(propertyName);
                    break;
                case BaseQueryParams.Condition.OR:
                    baseUpdateWrapper.or();
                    break;
                case BaseQueryParams.Condition.AND:
                    BaseUpdateWrapper baseUpdateWrapper1 = getBaseUpdateWrapper("", BaseParams.bulidByQuery((ArrayList<BaseQueryParams>) vals.get(0)));
                    baseUpdateWrapper.and(i -> baseUpdateWrapper1.lambda());
                    break;
                default:
            }
        }
        return baseUpdateWrapper;
    }

    static void checkSaveOrUpdate(String methodName, Class clazz, Object... objs) {
        switch (methodName) {
            case BaseQueryParams.SysMethod.SAVEORUPDATE:
            case BaseQueryParams.SysMethod.SAVEORUPDATEBATCH:
                for (Object obj : objs) {
                    List<Field> list = Arrays.asList(clazz.getDeclaredFields());
                    boolean flag = false;
                    for (int i = 0; i < list.size(); i++) {
                        Field field = list.get(i);
                        if (field.getAnnotation(TableId.class) != null) {
                            field.setAccessible(true);
                            try {
                                Object val = field.get(obj);
                                if (val != null) {
                                    flag = true;
                                    break;
                                }
                            } catch (IllegalAccessException e) {
                                log.error(getStackTraceInfo(e));
                            }
                        }
                    }
                    //主键有值 移除创建人和创建属性
                    if (flag) {
                        try {
                            Field field = clazz.getDeclaredField("creator");
                            field.setAccessible(true);
                            field.set(obj, null);
                            Field field1 = clazz.getDeclaredField("createTime");
                            field1.setAccessible(true);
                            field1.set(obj, null);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            log.error(getStackTraceInfo(e));
                        }
                    }
                }
                break;
            case BaseQueryParams.SysMethod.UPDATE:
            case BaseQueryParams.SysMethod.UPDATEBATCHBYID:
            case BaseQueryParams.SysMethod.UPDATEBYID:
                System.out.println("update创建人、创建时间转换");
                for (Object obj : objs) {
                    //更新移除创建人和创建时间
                    try {
                        Field field = clazz.getDeclaredField("creator");
                        field.setAccessible(true);
                        field.set(obj, null);
                        Field field1 = clazz.getDeclaredField("createTime");
                        field1.setAccessible(true);
                        field1.set(obj, null);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.error(getStackTraceInfo(e));
                    }
                }
            default:
        }
    }

    /**
     * 获取e.printStackTrace() 的具体信息，赋值给String 变量，并返回
     *
     * @param e Exception
     * @return e.printStackTrace() 中 的信息
     */
    public static String getStackTraceInfo(Exception e) {

        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);//将出错的栈信息输出到printWriter中
            pw.flush();
            sw.flush();

            return sw.toString();
        } catch (Exception ex) {
            return "发生错误";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

    }

    @Override
    public String toString() {
        return "BaseParams{" +
            "id=" + id +
            ", ids=" + ids +
            ", obj=" + obj +
            ", objs=" + objs +
            ", size=" + size +
            ", current=" + current +
            ", baseQueryParams=" + baseQueryParams +
            '}';
    }
}

