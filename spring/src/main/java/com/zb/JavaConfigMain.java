package com.zb;

import com.zb.aop.CalculatorAspect;
import com.zb.aop.ICalculatorService;
import com.zb.xmlconfig.XmlBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


public class JavaConfigMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        /*ICalculatorService calculatorService = context.getBean(ICalculatorService.class);
        System.out.println(calculatorService.getClass());
        int result = calculatorService.mul(1, 1);
        System.out.println("结果为："+result);*/
        XmlBean bean = context.getBean(XmlBean.class);
        bean.show();
    }
}
