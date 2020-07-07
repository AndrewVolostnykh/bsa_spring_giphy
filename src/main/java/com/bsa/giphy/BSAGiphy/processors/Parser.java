package com.bsa.giphy.BSAGiphy.processors;

import com.bsa.giphy.BSAGiphy.dto.CacheDto;
import com.bsa.giphy.BSAGiphy.dto.HistoryDto;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class Parser {
    public CacheDto[] parseFullCache(Map<String, File[]> queriedCache) {

        CacheDto[] cache = new CacheDto[queriedCache.size()];
        int counter = 0;
        int cacheCounter = 0;


        for(Map.Entry<String, File[]> gif : queriedCache.entrySet()) {
            String[] array = new String[gif.getValue().length];
            for(File file : queriedCache.get(gif.getKey())) {
                array[counter] = file.getAbsolutePath();
                counter++;
            }
            cache[cacheCounter] = new CacheDto();
            cache[cacheCounter].setQuery(gif.getKey());
            cache[cacheCounter].setGifs(array);
            counter = 0;
            cacheCounter++;
        }

        return cache;
    }

    public String[] parseOnlyFiles(Map<String, File[]> queriedCache) {
        var cacheDto = parseFullCache(queriedCache);
         return cacheDto[0].getGifs();
    }

    public List<HistoryDto> historyParser(File file) {
        var historyList = new ArrayList<HistoryDto>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while((line = reader.readLine()) != null) {
                var history = new HistoryDto();
                String[] result = line.split(",");
                history.setDate(LocalDate.parse(result[0]));
                history.setQuery(result[1]);
                history.setGif(result[2]);
                historyList.add(history);
            }
        } catch (IOException ex) {
            // logger here too
            System.out.println("Impossible to read file");
        }

        return historyList;
    }

    public HistoryDto[] parseHistory(File file) {
        var resultList = historyParser(file);
        var resultArray = new HistoryDto[resultList.size()];
        return resultList.toArray(resultArray);
    }
}
