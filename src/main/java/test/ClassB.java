package test;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class ClassB {
    private List<String> list;

    private Optional<List<String>> optList;
}
