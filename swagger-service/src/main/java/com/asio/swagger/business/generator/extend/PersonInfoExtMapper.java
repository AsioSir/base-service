package com.asio.swagger.business.generator.extend;

import com.asio.swagger.business.generator.mapper.PersonInfoMapper;
import com.asio.swagger.business.generator.model.PersonInfo;
import com.asio.tools.streamutils.StreamUtils;
import com.asio.tools.streamutils.model.SexEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: asio
 * @time: 2021-07-29 21:34
 * 描述: mapper扩展，自定义sql,实际为接口
 */
@Component
public class PersonInfoExtMapper implements PersonInfoMapper {
    //模拟请求，真正方法在extMapper.xml
    public PersonInfo selectPersonInfoByName(String name, Integer sex) {
        List<PersonInfo> list = new ArrayList<>();
        list.add(new PersonInfo("张三", 21, 63.5, SexEnum.BOY.getValue()));
        list.add(new PersonInfo("李四", 36, 67.2, SexEnum.BOY.getValue()));
        list.add(new PersonInfo("王五", 21, 58.8, SexEnum.BOY.getValue()));
        list.add(new PersonInfo("李艳", 19, 46.2, SexEnum.GIRL.getValue()));
        list.add(new PersonInfo("李艳", 21, 47.3, SexEnum.GIRL.getValue()));
        Map<String, List<PersonInfo>> personMap = StreamUtils.groupListToMap(list, PersonInfo::getName);
        List<PersonInfo> groupList = personMap.get(name);
        for (PersonInfo personInfo : groupList) {
            if (personInfo.getSex().equals(sex)){
                return personInfo;
            }
        }
        return null;
    }

    @Override
    public int insertByExample(PersonInfo example) {
        return 0;
    }

    @Override
    public int updateByExample(PersonInfo example) {
        return 0;
    }

    @Override
    public int delete(Long keyId) {
        return 0;
    }
}
