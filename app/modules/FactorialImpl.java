package modules;

import play.inject.ApplicationLifecycle;
import javax.inject.Inject;

public class FactorialImpl implements  Factorial{

    @Inject
    public FactorialImpl(ApplicationLifecycle lifecycle) {
        //implement plugin lifecycle methods
    }
    public int fact(int number) {
        return calculateFactorial(number);
    }
    private int calculateFactorial(int num) {
        if(num > 25) {
            throw new RuntimeException("Our of range");
        }
        if(num == 1)
         return num;
        return num * calculateFactorial(num-1);
    }
}