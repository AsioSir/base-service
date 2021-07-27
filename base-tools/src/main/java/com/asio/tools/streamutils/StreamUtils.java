package com.asio.tools.streamutils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.asio.tools.streamutils.model.Person;
import com.asio.tools.streamutils.model.SexEnum;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: asio
 * @time: 2021-07-27 19:35
 * 描述:  数据流处理工具
 */
public class StreamUtils {

    public static final String ADD_OPERATION = "add";
    public static final String SUB_OPERATION = "sub";
    public static final String MUL_OPERATION = "mul";
    public static final String DIV_OPERATION = "div";


    /**
     * Description: 将集合中对象的字段提取出来转为list集合，默认去重
     * @author asio
     * @time 2021-07-27 19:51
     * @param sourceList 原list集合对象
     * @param classifier 提取字段lambda表单式
     */
    public static<T, K>List<K> extractFieldToListDistinct(List<T> sourceList, Function<? super T, ? extends K> classifier) {
        return extractFieldToList(sourceList, classifier, null, true);
    }

    /**
     * Description: 将集合中对象的字段提取出来转为list集合，默认不去重
     * @author asio
     * @time 2021-07-27 19:51
     * @param sourceList 原list集合对象
     * @param classifier 提取字段lambda表单式
     */
    public static<T, K>List<K> extractFieldToListNotDistinct(List<T> sourceList, Function<? super T, ? extends K> classifier) {
        return extractFieldToList(sourceList, classifier, null, false);
    }

    /**
     * Description: 将集合中对象的字段提取出来转为list集合
     * @author asio
     * @time 2021-07-27 19:43 
     * @param sourceList 原list集合对象
     * @param classifier 提取字段lambda表单式
     * @param predicate 过滤lambda表达式
     * @param isDistinct 是否去重
     */
    public static<T, K>List<K> extractFieldToList(List<T> sourceList, Function<? super T, ? extends K> classifier,
                                                  Predicate<? super T> predicate, boolean isDistinct) {
        Stream<T> stream = sourceList.stream();
        //过滤
        if (predicate != null) {
            stream = stream.filter(predicate);
        }
        Stream<? extends K> targetStream = stream.map(classifier);
        //是否去重
        if (isDistinct) {
            return targetStream.distinct().collect(Collectors.toList());
        } else {
            return targetStream.collect(Collectors.toList());
        }
    }

    /**
     * Description: 将list集合对象转为map,value为list对象值，key重复时默认新增覆盖旧值
     * @author asio
     * @time 2021-07-27 20:56
     * @param sourceList 原list集合对象
     * @param keyMapper key值lambda表达式
     */
    public static <T, K> Map<K,T> extractFieldToMap(List<T> sourceList, Function<? super T, ? extends K> keyMapper) {
        return sourceList.stream().collect(Collectors.toMap(keyMapper, Function.identity(), (v1, v2) -> v2));
    }

    /**
     * Description: 将list对象集合转为map集合，key重复是否覆盖
     * @author asio
     * @time 2021-07-27 20:41
     * @param sourceList 原list集合对象
     * @param keyMapper key值lambda表达式
     * @param valueMapper value值lambda表达式
     * @param isCover key值相同时是否使用新增覆盖旧值
     */
    public static <T, K, U> Map<K,U> extractFieldToMap(List<T> sourceList, Function<? super T, ? extends K> keyMapper,
                                                       Function<? super T, ? extends U> valueMapper, boolean isCover) {
        //key值相同时是否使用新值覆盖旧值
        if (isCover) {
            return sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper, (key1, key2) -> key2));
        } else {
            return sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper, (key1, key2) -> key1));
        }
    }

    /**
     * Description:  将list对象集合转为map集合，key重复自定义操作
     * @author asio
     * @time 2021-07-27 20:54
     * @param sourceList 原list集合对象
     * @param keyMapper key值lambda表达式
     * @param valueMapper value值lambda表达式
     * @param mergeFunction key重复自定义操作表达式
     */
    public static <T, K, U> Map<K,U> extractFieldToMap(List<T> sourceList, Function<? super T, ? extends K> keyMapper,
                                                       Function<? super T, ? extends U> valueMapper,
                                                       BinaryOperator<U> mergeFunction) {
        //key值相同时是否使用新值覆盖旧值
        return sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }


    /**
     * Description: 将list集合按指定字段分组转为map
     * @author asio
     * @time 2021-07-27 21:07
     * @param sourceList 原list集合对象
     * @param classifier 提取字段lambda表单式
     */
    public static  <T, K> Map<K, List<T>> groupListToMap(List<T> sourceList, Function<? super T, ? extends K> classifier) {
        return sourceList.stream().collect(Collectors.groupingBy(classifier));
    }

    /**
     * Description: 将list集合过滤后按指定字段分组转为map
     * @author asio
     * @time 2021-07-27 21:18
     * @param sourceList 原list集合对象
     * @param classifier 提取字段lambda表单式
     * @param predicate 过滤lambda表达式
     */
    public static  <T, K> Map<K, List<T>> groupListToMap(List<T> sourceList, Function<? super T, ? extends K> classifier,
                                                         Predicate<? super T> predicate) {
        return sourceList.stream().filter(predicate).collect(Collectors.groupingBy(classifier));
    }

    /**
     * Description: 合并流并进行计算
     * @author asio
     * @time 2021-07-27 22:10
     * @param sourceList 原list集合对象
     * @param classifier 提取字段lambda表单式
     * @param operationType 计算方式
     */
    public static <T> Number operationNumberByList(List<T> sourceList,  Function<? super T, Number> classifier, String operationType) {
        Optional<Number> reduceOptional = null;
        if (StreamUtils.ADD_OPERATION.equals(operationType)) {
            reduceOptional = sourceList.stream().map(classifier).reduce(NumberUtil::add);
        } else if (StreamUtils.SUB_OPERATION.equals(operationType)) {
            reduceOptional = sourceList.stream().map(classifier).reduce(NumberUtil::sub);
        } else if (StreamUtils.MUL_OPERATION.equals(operationType)) {
            reduceOptional = sourceList.stream().map(classifier).reduce(NumberUtil::mul);
        } else if (StreamUtils.DIV_OPERATION.equals(operationType)) {
            reduceOptional = sourceList.stream().map(classifier).reduce(NumberUtil::div);
        }
        return reduceOptional.orElse(null);
    }

    /**
     * Description: 按对象指定字段顺序排序
     * @author asio
     * @time 2021-07-27 22:15
     * @param sourceList 原list集合对象
     * @param keyMapper 排序字段lambda表达式
     */
    public static <T, K extends Comparable<? super K>> List<T> sortListAsc(List<T> sourceList, Function<? super T, ? extends K> keyMapper) {
        Comparator<? super T> comparing = Comparator.comparing(keyMapper);
        return sourceList.stream().sorted(comparing).collect(Collectors.toList());
    }

    /**
     * Description: 按对象指定字段倒序排序
     * @author asio
     * @time 2021-07-27 22:16
     * @param sourceList 原list集合对象
     * @param keyMapper 排序字段lambda表达式
     */
    public static <T, K extends Comparable<? super K>> List<T> sortListDesc(List<T> sourceList, Function<? super T, ? extends K> keyMapper) {
        Comparator<? super T> comparing = Comparator.comparing(keyMapper).reversed();
        return sourceList.stream().sorted(comparing).collect(Collectors.toList());
    }

    /**
     * Description: 按对象指定字段倒序排序
     * @author asio
     * @time 2021-07-27 22:16
     * @param sourceList 原list集合对象
     * @param keyMapper 排序字段lambda表达式
     * @param keyComparator 比较器
     */
    public static <T, K extends Comparable<? super K>> List<T> sortList(List<T> sourceList,
                                                                        Function<? super T, ? extends K> keyMapper,
                                                                        Comparator<? super K> keyComparator) {
        Comparator<? super T> comparing = Comparator.comparing(keyMapper, keyComparator).reversed();
        return sourceList.stream().sorted(comparing).collect(Collectors.toList());
    }



    /*
        stream流工具类测试
     */
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("张三", 21, 63.5, SexEnum.BOY.getValue()));
        list.add(new Person("李四", 36, 67.2, SexEnum.BOY.getValue()));
        list.add(new Person("王五", 21, 58.8, SexEnum.BOY.getValue()));
        list.add(new Person("李艳", 19, 46.2, SexEnum.GIRL.getValue()));
        list.add(new Person("李艳", 21, 47.3, SexEnum.GIRL.getValue()));

        //不去重
        /*List<Person> resultList1 = StreamUtils.extractFieldToListNotDistinct(list, v -> v);
        System.out.println("不去重转list集合：" + JSONUtil.toJsonPrettyStr(resultList1));*/
        //去重
        /*List<String> resultList2 = StreamUtils.extractFieldToListDistinct(list, Person::getName);
        System.out.println("去重转list集合：" + resultList2);*/
        //过滤不去重
        /*List<Person> resultList3 = StreamUtils.extractFieldToList(list, v -> v, (person) -> person.getName().toString().startsWith("李"), false);
        System.out.println("转list转集合：" + JSONUtil.toJsonPrettyStr(resultList3));*/

        //list转map集合,key值重复选着是否覆盖
        /*Map<String, Integer> personMap1 = StreamUtils.extractFieldToMap(list, Person::getName, Person::getAge, true);
        System.out.println("转map集合1：" + JSONUtil.toJsonPrettyStr(personMap1));*/

        //list转map集合,key值重复自定义操作
       /* Map<String, Person> personMap2 = StreamUtils.extractFieldToMap(list, Person::getName, Function.identity(),  (data1, data2) -> {
            //key值重复时，支持操作
            data1.setAge(data1.getAge() + data2.getAge());
            return data1;
        });
        Map<String, Person> personMap3 = StreamUtils.extractFieldToMap(list, Person::getName, Function.identity(), (data1, data2) -> addAge(data1, data2.getAge()));
        System.out.println("转map集合2：" + JSONUtil.toJsonPrettyStr(personMap2));
        System.out.println("转map集合3：" + JSONUtil.toJsonPrettyStr(personMap3));*/

        //将list集合对象转为map,value为list对象值，key重复时默认新增覆盖旧值
        /*Map<String, Person> personMap4 = StreamUtils.extractFieldToMap(list, Person::getName);
        System.out.println("转map集合4：" + JSONUtil.toJsonPrettyStr(personMap4));*/

        //将list集合按指定字段分组转为map
        /*Map<String, List<Person>> groupListToMap1 = StreamUtils.groupListToMap(list, Person::getSexDesc);
        System.out.println(JSONUtil.toJsonPrettyStr(groupListToMap1));*/

        //将list集合过滤后按指定字段分组转为map
        /*Map<String, List<Person>> groupListToMap2 = StreamUtils.groupListToMap(list, Person::getSexDesc, data -> data.getName().startsWith("李"));
        System.out.println(JSONUtil.toJsonPrettyStr(groupListToMap2));*/

        //list中对象数字计算
        /*BigDecimal sumWeight = NumberUtil.toBigDecimal(StreamUtils.operationNumberByList(list, Person::getWeight, StreamUtils.ADD_OPERATION));
        System.out.println(sumWeight);*/

        System.out.println("--------------------按年龄倒序排序-------------------------");
        List<Person> peopleDesByAgeList = StreamUtils.sortListDesc(list, Person::getAge);
        System.out.println(JSONUtil.toJsonPrettyStr(peopleDesByAgeList));
        System.out.println("---------------------按年龄倒序排序------------------------");
        List<Person> peopleAscByAgeList = StreamUtils.sortListAsc(list, Person::getAge);
        System.out.println(JSONUtil.toJsonPrettyStr(peopleAscByAgeList));
        System.out.println("----------------------比较器按体重倒序排序-----------------------");
        List<Person> peopleSortByWeightDescList = StreamUtils.sortList(list, Person::getWeight, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(JSONUtil.toJsonPrettyStr(peopleSortByWeightDescList));
    }


    public static Person addAge(Person person,  Integer age2) {
        person.setAge(person.getAge() - age2);
        return person;
    }

}
