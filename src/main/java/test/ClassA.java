package test;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Data
public class ClassA {
    private ClassB classB;

    private Optional<ClassB> optClassB;

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

//        ClassA classA1 = new ClassA();
//        ClassB classB = new ClassB();
//        classA1.setClassB(classB);
//
//        List<String> strings = Optional.of(classA1)
//                .flatMap(ClassA::getOptClassB)
//                .flatMap(ClassB::getOptList)
//                .orElse(new ArrayList<>());
//        Optional<List<String>> strings1 = Optional.of(classA1)
//                .flatMap(ClassA::getOptClassB)
//                .flatMap(ClassB::getOptList);
//        System.out.println(strings);
    }
}
