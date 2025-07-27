package com.twitter_backend_spring_boot.twitter.api.test;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class NickNameService {

    public NickNameService() {
        System.out.println(getNickNameByExcludingInvalidStrings(null,"TES","2434",List.of("NA","Null")));
    }

    public String getNickNameByExcludingInvalidStrings(String nickName, String consumerName, String rechargeNumber, List<String> invalidStrings){
        String nick_name = null;
        try{
            if(Objects.nonNull(nickName)
                    &&!StringUtils.isEmpty(nickName.trim())){
                nick_name = nickName;
            }else{
                nick_name = (Objects.nonNull(consumerName)
                        &&!StringUtils.isEmpty(consumerName.trim()) && !isLabelInInvalidStrings(invalidStrings,consumerName)) ? consumerName : rechargeNumber;
            }
        }catch(RuntimeException e) {
        }
        return nick_name;
    }

    public static boolean isLabelInInvalidStrings(List<String> invalidStrings, String label) {
        if(Objects.isNull(label)){
            return true;
        }
        if (!Objects.isNull(invalidStrings)) {
            for (String invalidString : invalidStrings) {
                if (invalidString.equalsIgnoreCase(label.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}
