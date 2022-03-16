package com.kronos.Reddit;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Dictionary")
public class Word {

  @Id
  @Column(name = "word", length = 50)
  private String word;

  @Column(name = "l_count")
  private int l_count;

  @Column(name = "r_count")
  private int r_count;

  @Column(name = "l_score")
  private double l_score;

  @Column(name = "r_score")
  private double r_score;

  public Word() {}

  public Word(String word, int l_count, int r_count, double l_score, double r_score) {
    this.word = word;
    this.l_count = l_count;
    this.r_count = r_count;
    this.l_score = l_score;
    this.r_score = r_score;
  }

  public String getWord() {
    return this.word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getL_count() {
    return this.l_count;
  }

  public void setL_count(int l_count) {
    this.l_count = l_count;
  }

  public int getR_count() {
    return this.r_count;
  }

  public void setR_count(int r_count) {
    this.r_count = r_count;
  }

  public double getL_score() {
    return this.l_score;
  }

  public void setL_score(double l_score) {
    this.l_score = l_score;
  }

  public double getR_score() {
    return this.r_score;
  }

  public void setR_score(double r_score) {
    this.r_score = r_score;
  }

  public Word word(String word) {
    setWord(word);
    return this;
  }

  public Word l_count(int l_count) {
    setL_count(l_count);
    return this;
  }

  public Word r_count(int r_count) {
    setR_count(r_count);
    return this;
  }

  public Word l_score(double l_score) {
    setL_score(l_score);
    return this;
  }

  public Word r_score(double r_score) {
    setR_score(r_score);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Word)) {
      return false;
    }
    Word word = (Word) o;
    return Objects.equals(word, word.word)
        && l_count == word.l_count
        && r_count == word.r_count
        && l_score == word.l_score
        && r_score == word.r_score;
  }

  @Override
  public int hashCode() {
    return Objects.hash(word, l_count, r_count, l_score, r_score);
  }

  @Override
  public String toString() {
    return "{"
        + " word='"
        + getWord()
        + "'"
        + ", l_count='"
        + getL_count()
        + "'"
        + ", r_count='"
        + getR_count()
        + "'"
        + ", l_score='"
        + getL_score()
        + "'"
        + ", r_score='"
        + getR_score()
        + "'"
        + "}";
  }
}
