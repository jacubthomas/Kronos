package com.kronos.Reddit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/word")
public class WordController {
  private final WordService wordService;

  @Autowired
  public WordController(WordService wordService) {
    this.wordService = wordService;
  }

  // Returns predicted party after ingesting quote
  @PostMapping("/party/{quote}")
  public HashMap<String, Double> getParty(@PathVariable String quote) {

    // retrieves all word objects from DB
    List<Word> words = wordService.getWord();

    double Liberal = 0.0;
    double Republican = 0.0;
    double i = 0;

    // tokenizes input quote
    Scanner sc = new Scanner(quote);

    // preprocesses and weights words in input quote
    HashMap<String, Integer> hm = new HashMap<>();
    while (sc.hasNext()) {
      i += 1;
      String temp = sc.next().toLowerCase();

      if (hm.containsKey(temp)) {
        hm.put(temp, hm.get(temp) + 1);
      } else {
        hm.put(temp, 1);
      }
    }

    // sums ( word * weight * party_bias)
    for (Map.Entry<String, Integer> entry : hm.entrySet()) {
      String key = entry.getKey();
      int value = entry.getValue();
      for (Word w : words) {
        if (w.getWord().equals(key)) {
          Liberal += w.getL_score() * value;
          Republican += w.getR_score() * value;
        }
      }
    }
    sc.close();
    HashMap<String, Double> result = new HashMap<>();
    result.put("Liberal", Liberal);
    result.put("Republican", Republican);
    result.put("Word_Count", i);
    // String json = "{ Liberal:" + Liberal + ", Republican : " + Republican + " };";
    return result;
  }
}
