package com.asio.tools.excelutils.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

import cn.hutool.core.util.StrUtil;

import com.asio.constant.DefaultValueConstant;
import com.asio.exception.BusinessException;
import com.asio.tools.excelutils.PropertyFeatureMappingUtil;
import com.asio.tools.excelutils.annotions.FeatureMapping;
import com.asio.tools.excelutils.bo.ExpressTemplateExcelBO;
import com.asio.tools.excelutils.entity.ConditionItemVO;
import com.asio.tools.excelutils.entity.ExcelTemplateXmlEntity;
import com.asio.tools.excelutils.enums.FeatureMappingTypeEnum;
import com.asio.tools.excelutils.model.PropertyFeatureMapping;
import com.asio.tools.loggerutils.LoggerUtils;
import com.asio.tools.streamutils.StreamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: leijun
 * @creat: 2024-10-25 19:28
 * 描述:属性字段映射业务service实现类
 */
@Service
public class PropertyFeatureMappingBizService {


    /**
     * Description: 获取对象映射字段
     * @param classPath Bo对象全路径
     */
    public List<ConditionItemVO> getBoFeatureProperty(String classPath) throws Exception {

        List<ConditionItemVO> conditionItemVOList = new ArrayList<>();
        Class<?> objClazz = Class.forName(classPath);
        Field[] declaredFields = objClazz.getDeclaredFields();
        for (Field field : declaredFields) {
            FeatureMapping annotation = field.getAnnotation(FeatureMapping.class);
            if (null == annotation) {
                continue;
            }
            String desc = annotation.desc();
            conditionItemVOList.add(new ConditionItemVO(field.getName(), StrUtil.format("{}({})", desc, field.getName())));
        }

        return conditionItemVOList;
    }



    /**
     * Description: 转换快递模板Excel对象属性
     * @param expressTemplateExcelBOList 快递导入模版数据
     * @param propertyFeatureMappingList 对象属性映射信息
     */
    public void convertExpressTemplateExcelBOList(List<ExpressTemplateExcelBO> expressTemplateExcelBOList,
                                                  List<PropertyFeatureMapping> propertyFeatureMappingList) throws Exception {
        try {
            if (CollUtil.isEmpty(expressTemplateExcelBOList) || CollUtil.isEmpty(propertyFeatureMappingList)) {
                return;
            }

            propertyFeatureMappingList = StreamUtils.sortListAsc(propertyFeatureMappingList, PropertyFeatureMapping::getSort);
            Map<Long, Map<String, String>> featureMappingDictionaryMap = getFeatureMappingDictionaryMap(propertyFeatureMappingList);

            // 根据映射关系转换对象字段值
            for (ExpressTemplateExcelBO expressTemplateExcelBO : expressTemplateExcelBOList) {
                for (PropertyFeatureMapping propertyFeatureMapping : propertyFeatureMappingList) {
                    Map<String, String> dictionaryMap = featureMappingDictionaryMap.get(propertyFeatureMapping.getPropertyFeatureMappingKeyId());
                    PropertyFeatureMappingUtil.convertBasePropertyValue(propertyFeatureMapping, expressTemplateExcelBO, dictionaryMap);
                }
            }
        } catch (Exception e) {
            LoggerUtils.error("映射对象属性字段异常：", e);
            throw new BusinessException("映射对象属性字段异常：" + e.getMessage());
        }

    }


    /**
     * Description: 获取按字典项值映射关系的信息
     * @author leijun
     * @create 2024-10-28 19:54
     * @param propertyFeatureMappingList 映射信息表集合
     * @return [映射关系主键，[映射值key,映射值value]]
     */
    private Map<Long, Map<String, String>> getFeatureMappingDictionaryMap( List<PropertyFeatureMapping> propertyFeatureMappingList) throws Exception {
        List<PropertyFeatureMapping> dictionaryFeatureMappingList = StreamUtils.filter(propertyFeatureMappingList, v -> FeatureMappingTypeEnum.DICTIONARY.getValue().equals(v.getFeatureType()));
        Map<Long, Map<String, String>> featureMappingDictionaryMap = new HashMap<>();

        /*List<String> dictionaryIdList = StreamUtils.extractFieldToListDistinct(dictionaryFeatureMappingList, v -> {
            String rules = PropertyFeatureMappingUtil.substringByBrackets(v.getFeatureRule());
            String[] splits = rules.split(DefaultValueConstant.E_COMMA);
            return splits[1];
        });

        if (CollUtil.isEmpty(dictionaryIdList)) {
            return new HashMap<>();
        }

        // 先取缓存数据
        Map<String, List<PubDictionaryItemVo>> cacheGroupDictionaryItemVoMap = new HashMap<>();
        List<String> cacheExistDictionaryIdList = new ArrayList<>();
        for (String dictionaryId : dictionaryIdList) {
            String dictionaryIdRedisKey = RedisKeyConstant.PAYABLE_ERP_PUB_DICTIONARY_LIST_REDIS_KEY + dictionaryId;
            List<PubDictionaryItemVo> cachePubDictionaryItemVoList = cacheService.getValue(dictionaryIdRedisKey);
            if (CollUtil.isNotEmpty(cachePubDictionaryItemVoList)) {
                cacheGroupDictionaryItemVoMap.put(dictionaryId, cachePubDictionaryItemVoList);
                cacheExistDictionaryIdList.add(dictionaryId);
            }
        }

        dictionaryIdList.removeAll(cacheExistDictionaryIdList);

        // 查询字典项
        DictionaryItemCondition dictionaryItemCondition = new DictionaryItemCondition();
        dictionaryItemCondition.setDictionaryIdList(dictionaryIdList);
        DictionaryItemResponse dictionaryItemResponse = dictionaryItemApi.queryItemByCondition(dictionaryItemCondition);
        List<PubDictionaryItemVo> pubDictionaryItemVoList = dictionaryItemResponse.getPubDictionaryItemVoList();
        Map<String, List<PubDictionaryItemVo>> groupDictionaryItemVoMap = StreamUtils.groupToList(pubDictionaryItemVoList, PubDictionaryItemVo::getDictionaryId);

        groupDictionaryItemVoMap.forEach((dictionaryId, dictionaryItemVoList) -> {
            cacheService.setKey(RedisKeyConstant.PAYABLE_ERP_PUB_DICTIONARY_LIST_REDIS_KEY + dictionaryId, dictionaryItemVoList, 10, TimeUnit.MINUTES);
        });

        groupDictionaryItemVoMap.putAll(cacheGroupDictionaryItemVoMap);

        // 获取字典项值，并按照映射记录转换为map集合
        for (PropertyFeatureMapping dictionaryPropertyFeatureMapping : dictionaryFeatureMappingList) {
            String targetKeyField = dictionaryPropertyFeatureMapping.getTargetKeyField();
            String targetValueField = dictionaryPropertyFeatureMapping.getTargetValueField();
            String rules = PropertyFeatureMappingUtil.substringByBrackets(dictionaryPropertyFeatureMapping.getFeatureRule());
            String[] splits = rules.split(DefaultValueConstant.E_COMMA);
            String dictionaryId = splits[1];
            List<PubDictionaryItemVo> dictionaryItemVoList = groupDictionaryItemVoMap.get(dictionaryId);
            if (CollUtil.isEmpty(dictionaryItemVoList)) {
                continue;
            }

            Map<String, String> fieldValueMap = new HashMap<>();
            for (PubDictionaryItemVo pubDictionaryItemVo : dictionaryItemVoList) {
                ImmutablePair<Object, Type> keyPair = PropertyFeatureMappingUtil.getObjFieldValue(pubDictionaryItemVo, targetKeyField);
                String mapKey = Objects.isNull(keyPair.left) ? null : keyPair.left.toString();

                ImmutablePair<Object, Type> valuePair = PropertyFeatureMappingUtil.getObjFieldValue(pubDictionaryItemVo, targetValueField);
                String mapValue = Objects.isNull(valuePair.left) ? null : valuePair.left.toString();
                fieldValueMap.put(mapKey, mapValue);
            }

            featureMappingDictionaryMap.put(dictionaryPropertyFeatureMapping.getPropertyFeatureMappingKeyId(), fieldValueMap);
        }*/
        return featureMappingDictionaryMap;
    }


}