package com.cc.validator;

import com.cc.bean.PostData;
import com.cc.bean.Ship;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by cc on 2017/2/22.
 */
public class ShipValidator implements Validator {



    public boolean supports(Class clazz) {
        return PostData.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        System.out.println("validate--------");
        ValidationUtils.rejectIfEmpty(e, "token", "token.empty");
        ValidationUtils.rejectIfEmpty(e, "data", "data.empty");
//        Ship p = (Ship) obj;
//        if (p.getIndex() == null) {
//            e.rejectValue("index", "negativevalue");
//        }
    }
}
