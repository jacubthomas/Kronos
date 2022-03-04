


import java.util.Objects;

public class Word 
{
    private int id;
    private String word;
    private int l_count;
    private int r_count;

    public Word(){}

    public Word(int id, String word, int l_count, int r_count) {
        this.id = id;
        this.word = word;
        this.l_count = l_count;
        this.r_count = r_count;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Word id(int id) {
        setId(id);
        return this;
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

    // @Override
    // public boolean equals(Object o) {
    //     if (o == this)
    //         return true;
    //     if (!(o instanceof Word)) {
    //         return false;
    //     }
    //     Word word = (Word) o;
    //     return id == word.id && Objects.equals(word, word.word) && l_count == word.l_count && r_count == word.r_count;
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(id, word, l_count, r_count);
    // }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", word='" + getWord() + "'" +
            ", l_count='" + getL_count() + "'" +
            ", r_count='" + getR_count() + "'" +
            "}";
    }
    
}
