package test;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ClassA {
    private ClassB classB;

    public static void main(String[] args) {
        ClassA classA = new ClassA();
        List<String> list = Optional.ofNullable(classA)
                .map(ClassA::getClassB)
                .map(ClassB::getList)
                .orElse(new ArrayList<>());
        List<String> list1 = Optional.ofNullable(classA)
                .map(ClassA::getClassB)
                .map(ClassB::getList)
                .orElseGet(ArrayList::new);
        Optional.ofNullable(classA)
                .map(ClassA::getClassB)
                .map(ClassB::getList);

        System.out.println(list);
    }
}
