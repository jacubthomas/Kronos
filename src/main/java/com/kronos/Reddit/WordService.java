package com.kronos.Reddit;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordService {
  private final WordRepository wordRepository;

  @Autowired
  public WordService(WordRepository wordRepository) {
    this.wordRepository = wordRepository;
  }

  public List<Word> getWord() {
    return wordRepository.findAll();
  }
}
