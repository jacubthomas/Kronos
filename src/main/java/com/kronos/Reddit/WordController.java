package com.kronos.Reddit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    // expedites retrieval of word sequences
    HashMap<String, HashMap<String, Double>> hm = new HashMap<>();

    // move list to more efficient container
    for (Word w : words) {
      hm.put(w.getWord(), new HashMap<String, Double>());
      hm.get(w.getWord()).put("Liberal", w.getL_score());
      hm.get(w.getWord()).put("Republican", w.getR_score());
    }

    // return sums
    double Liberal = 0.0;
    double Republican = 0.0;
    double i = 0;

    // tokenizes input quote & tallies words
    Scanner sc = new Scanner(quote);
    ArrayList<String> tokenized_input = new ArrayList();
    while (sc.hasNext()) {
      i += 1;
      String temp = normalizeString(sc.next());
      tokenized_input.add(temp);
    }

    // tries to form largest word sequence at each point in sentence and return
    // larger sequences attain larger sums
    int size = tokenized_input.size();
    for (int j = 0; j < size; j++) {
      String trie = "";
      if (j + 2 < size) {
        trie =
            tokenized_input.get(j)
                + " "
                + tokenized_input.get(j + 1)
                + " "
                + tokenized_input.get(j + 2);
        if (hm.get(trie) != null) {
          Liberal += hm.get(trie).get("Liberal") * 5;
          Republican += hm.get(trie).get("Republican") * 5;
          j += 2;
          System.out.println("3trie : " + trie);
          continue;
        }
      }
      if (j + 1 < size) {
        trie = tokenized_input.get(j) + " " + tokenized_input.get(j + 1);
        if (hm.get(trie) != null) {

          Liberal += hm.get(trie).get("Liberal") * 2.5;
          Republican += hm.get(trie).get("Republican") * 2.5;
          j += 1;
          System.out.println("2trie : " + trie);
          continue;
        }
      }
      trie = tokenized_input.get(j);
      if (hm.get(trie) != null) {
        Liberal += hm.get(trie).get("Liberal");
        Republican += hm.get(trie).get("Republican");
        System.out.println("1trie : " + trie);
      }
    }

    sc.close();
    HashMap<String, Double> result = new HashMap<>();
    result.put("Liberal", Liberal);
    result.put("Republican", Republican);
    result.put("Word_Count", i);
    return result;
  }

  public String normalizeString(String raw) {
    String result = raw.replaceAll("\\p{Punct}|[^\\x00-\\x7F]", "");
    return result.toLowerCase();
  }
}
