package com.bsa.giphy.BSAGiphy;

import com.bsa.giphy.BSAGiphy.entities.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Cache cache = Cache.getInstance();

        cache.updateCache("Andrew", "moon", "132asd");
        cache.updateCache("Solya", "ligt", "wffwefd");
        cache.updateCache("Andrew", "flame", "weefw3dxz");
        cache.updateCache("Andrew", "moon", "vfgrbgtr");
        cache.updateCache("Andrew", "flame", "sefsdfsfdANDREtr");
        cache.updateCache("Andrew", "moon", "00x1245err");
        cache.updateCache("Solya", "moon", "vfgrbgtr");


        for(Map.Entry<String, Map<String, ArrayList<String>>> item : cache.getCacheMap().entrySet()) {
            System.out.print("USER: " + item.getKey() + " , \n\tQUERY: ");
            for(Map.Entry<String, ArrayList<String>> elements : item.getValue().entrySet()) {
                System.out.print(elements.getKey());
                for(String element : elements.getValue()) {
                    System.out.println("\n\t\tGifID: " + element);
                }
            }
        }

        System.out.println("DOES GETTER WORK?");

        System.out.println(cache.getGif("Andrew", "moon"));
    }
}
