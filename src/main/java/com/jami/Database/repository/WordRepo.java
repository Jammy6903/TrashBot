package com.jami.Database.repository;

import java.util.List;

import com.jami.Database.Fun.WordRecord;

public interface WordRepo {
  WordRecord getById(String id);

  WordRecord getByWord(String word);

  List<WordRecord> getWords(int amount, int page, String order, boolean reverseOrder);

  void incrementWord(String word);

  void save(WordRecord record);
}
