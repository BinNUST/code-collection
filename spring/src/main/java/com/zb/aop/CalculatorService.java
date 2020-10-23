package com.zb.aop;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService implements ICalculatorService {
    public int mul(int a, int b) {
        return a * b;
    }

    public int div(int a, int b) {
        return a / b;
    }
}
