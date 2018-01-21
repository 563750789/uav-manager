package com.tw.uav.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lmbda {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("c");

        list.stream().map(x -> x.toUpperCase()).filter(x->x.equals("A")).forEach(x->System.out.println(x));
    }
}
