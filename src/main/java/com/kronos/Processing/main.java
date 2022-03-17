package com.example;

import com.mysql.cj.xdevapi.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class main {
  static ArrayList<Reddit> reddits = new ArrayList<Reddit>();
  static HashMap<String, HashMap<String, Integer>> hm =
      new HashMap<String, HashMap<String, Integer>>();
  static HashMap<String, HashMap<String, Double>> p_o_p_b_w =
      new HashMap<String, HashMap<String, Double>>();
  static Set<String> top_words;
  static Connection conn = null;
  static int word_count = 0;

  public static void main(String[] args) throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    conn =
        DriverManager.getConnection("jdbc:mysql://localhost:3306/Reddit?user=root&password=root");

    ingestXSLX();
    DBtoHM();
    prob_of_party_by_word();

    top_words = new HashSet<String>();
    combTopWords("R");
    reevaluatePostsForPhrases("R");

    top_words = new HashSet<String>();
    combTopWords("L");
    reevaluatePostsForPhrases("L");

    DBtoHM();
    prob_of_party_by_word();
    System.out.println("All Done!");
  }

  // Gather top 100 words from each party, place in global set
  public static void combTopWords(String politicalparty) {
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Gather at Republican words
    if (politicalparty.equals("R")) {
      try {
        String query =
            "SELECT * FROM Dictionary HAVING r_count > l_count ORDER BY r_count DESC LIMIT 100;";
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
          String word = rs.getString("word");
          top_words.add(word);
        }
      } catch (SQLException sqle) {
        System.out.println("SQLException: " + sqle.getMessage());
      }
    }
    // Gather at Democrat words
    else {
      try {
        String query =
            "SELECT * FROM Dictionary HAVING l_count > r_count ORDER BY l_count DESC LIMIT 100;";
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()) {
          String word = rs.getString("word");
          top_words.add(word);
        }
      } catch (SQLException sqle) {
        System.out.println("SQLException: " + sqle.getMessage());
      }
    }
    System.out.println(politicalparty + ": Top words size = " + top_words.size());
  }

  // sift through each reddit post for top words, and add 2&3 word phrases involving such to
  // dictionary
  public static void reevaluatePostsForPhrases(String politicalparty) {
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    HashMap<String, Integer> phrases = new HashMap();
    try {
      // conn =
      //
      // DriverManager.getConnection("jdbc:mysql://localhost:3306/Reddit?user=root&password=root");

      // Select all posts from reddit
      String query = "SELECT * FROM Reddit";
      ps = conn.prepareStatement(query);
      rs = ps.executeQuery();
      int posts = 0;
      while (rs.next()) {
        if (posts % 25 == 0) {
          System.err.println("Posts processed = " + posts);
        }
        posts++;
        // split post into normalized words
        String post = rs.getString("post");
        Scanner sc = new Scanner(post);
        ArrayList<String> words = new ArrayList();
        while (sc.hasNext()) {
          words.add(normalizeString(sc.next()));
        }

        // look for top words and compile phrases
        for (int i = 0; i < words.size(); i++) {
          String two_back = "";
          String one_back = "";
          String current = "";
          String one_ahead = "";
          String two_ahead = "";
          String key = "";

          current = words.get(i);

          if (top_words.contains(current)) {
            if (i - 1 >= 0) {
              one_back = words.get(i - 1);
              if (i - 2 >= 0) two_back = words.get(i - 2);
            }
            if (i + 1 < words.size()) {
              one_ahead = words.get(i + 1);
              if (i + 2 < words.size()) two_ahead = words.get(i + 2);
            }
            if (two_back.length() > 1) {
              key = two_back + " " + one_back + " " + current;
              if (phrases.containsKey(key)) phrases.put(key, phrases.get(key) + 1);
              else phrases.put(key, 1);
            }
            if (one_back.length() > 1) {
              key = one_back + " " + current;
              if (phrases.containsKey(key)) phrases.put(key, phrases.get(key) + 1);
              else phrases.put(key, 1);
              if (one_ahead.length() > 1) {
                key += " " + one_ahead;
                if (phrases.containsKey(key)) phrases.put(key, phrases.get(key) + 1);
                else phrases.put(key, 1);
                key = current + " " + one_ahead;
                if (phrases.containsKey(key)) phrases.put(key, phrases.get(key) + 1);
                else phrases.put(key, 1);
              }
              if (two_ahead.length() > 1) {
                key += " " + two_ahead;
                if (phrases.containsKey(key)) phrases.put(key, phrases.get(key) + 1);
                else phrases.put(key, 1);
              }
            }
          }
        }
      }
    } catch (SQLException sqle) {
      System.out.println("SQLException: " + sqle.getMessage());
    }

    for (Map.Entry<String, Integer> entry : phrases.entrySet()) {
      try {
        st = null;
        ps = null;
        rs = null;
        String query = "SELECT * FROM Dictionary WHERE word = ?;";
        ps = conn.prepareStatement(query);
        ps.setString(1, entry.getKey());
        rs = ps.executeQuery();
        // already in DB
        if (rs.next()) {
          int l_score = rs.getInt("l_score");
          int r_score = rs.getInt("r_score");
          if (politicalparty.equals("R")) r_score += entry.getValue();
          else l_score += entry.getValue();
          query = "UPDATE Dictionary SET l_score = ?, r_score = ? WHERE word = ?";
          ps = conn.prepareStatement(query);
          ps.setDouble(1, l_score);
          ps.setDouble(2, r_score);
          ps.setString(3, entry.getKey());
          ps.executeUpdate();
        } else { // First Time Upload
          query = "INSERT into Dictionary (word, l_count, r_count) values (?, ?, ?)";
          ps = conn.prepareStatement(query);
          int l_score = 0;
          int r_score = 0;
          if (politicalparty.equals("R")) r_score += entry.getValue();
          else l_score += entry.getValue();
          ps.setString(1, entry.getKey());
          ps.setInt(2, l_score);
          ps.setInt(3, r_score);
          ps.execute();
        }
      } catch (SQLException sqle) {
        continue;
      }
    }
    System.err.println("Phrases collected = " + phrases.size());
  }

  static void prob_of_party_by_word() {
    for (Map.Entry<String, HashMap<String, Integer>> wordEntry : hm.entrySet()) {
      String word = wordEntry.getKey();
      p_o_p_b_w.put(word, new HashMap<String, Double>());

      double lib = 0;
      if (wordEntry.getValue().get("Liberal") != null) lib = wordEntry.getValue().get("Liberal");
      double cons = 0;
      if (wordEntry.getValue().get("Conservative") != null)
        cons = wordEntry.getValue().get("Conservative");

      double sum = lib + cons;
      double left = (double) ((lib / sum) * 1000000) / (word_count);
      double right = (double) ((cons / sum) * 1000000) / (word_count);

      p_o_p_b_w.get(word).put("Liberal", left);
      p_o_p_b_w.get(word).put("Conservative", right);

      Statement st = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        String query = "UPDATE Dictionary SET l_score = ?, r_score = ? WHERE word = ?";
        ps = conn.prepareStatement(query);
        ps.setDouble(1, left);
        ps.setDouble(2, right);
        ps.setString(3, word);
        ps.executeUpdate();
      } catch (SQLException sqle) {
        System.out.println("SQLException: " + sqle.getMessage());
      }

      // System.out.println(word + " : L : " + left + " ||| : R : " + right);
    }
  }

  static void DBtoHM() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conn =
          DriverManager.getConnection("jdbc:mysql://localhost:3306/Reddit?user=root&password=root");
      String query = "SELECT * FROM Dictionary;";
      ps = conn.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        word_count++;
        String word = rs.getString("word");
        hm.put(word, new HashMap<String, Integer>());
        hm.get(word).put("Liberal", rs.getInt("l_count"));
        hm.get(word).put("Conservative", rs.getInt("r_count"));
      }
    } catch (SQLException sqle) {
      System.out.println("SQLException: " + sqle.getMessage());
    }
    System.out.println("hashmap contains " + word_count + " words.");
  }

  public static void ingestXSLX() {
    OPCPackage pkg = OPCPackage.open(new File("data/Reddit.xlsx"));
    XSSFWorkbook wb = new XSSFWorkbook(pkg);
    XSSFSheet sheet = wb.getSheetAt(0);
    Iterator<Row> itr = sheet.iterator();
    while (itr.hasNext()) {
      try {
        Reddit r = new Reddit();
        Row row = itr.next();
        row = itr.next();
        Iterator<Cell> cellitr = row.cellIterator();
        Cell cell = cellitr.next();
        String raw = cell.getStringCellValue();
        String processed = normalizeString(raw);
        if (processed.length() <= 2) continue;
        r.setPost(processed);
        cell = cellitr.next();
        r.setParty(cell.getStringCellValue());
        cell = cellitr.next();
        r.setId(cell.getStringCellValue());
        reddits.add(r);
      } catch (Exception ex) {
        continue;
      }
    }
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      conn =
          DriverManager.getConnection("jdbc:mysql://localhost:3306/Reddit?user=root&password=root");
      for (Reddit r : reddits) {
        String query = "INSERT into Reddit (id, post, party)" + " values (?, ?, ?)";
        ps = conn.prepareStatement(query);
        ps.setString(1, r.getId());
        ps.setString(2, r.getPost());
        ps.setString(3, r.getParty());
        ps.execute();
      }
    } catch (Exception e) {
    }
    int i = 0;
    int left = 0;
    int right = 0;
    for (Reddit r : reddits) {
      String s_arr[] = r.getPost().split(" ");
      for (String s : s_arr) {
        s = s.trim();
        if (s.length() <= 2) continue;
        HashMap<String, Integer> m = hm.get(s);
        if (m == null) {
          hm.put(s, new HashMap<String, Integer>());
          hm.get(s).put(r.getParty(), 1);
        } else {
          try {
            int m_count = 1;
            if (m.get(r.getParty()) != null) m_count += m.get(r.getParty());

            m.put(r.getParty(), m_count);
            if (r.getParty().equalsIgnoreCase("Liberal")) left++;
            else right++;
          } catch (NullPointerException npe) {
            System.err.println(r.getId() + " : " + r.getParty() + " : " + r.getPost());
          } catch (Exception exc) {
            System.err.println(exc.getMessage());
          }
        }
        i++;
      }
    }
    System.err.println("processed " + i + " words!");
    System.err.println("processed " + left + " left words!");
    System.err.println("processed " + right + " right words!");
    processWords();
  }

  static void processWords() {
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    for (Map.Entry<String, HashMap<String, Integer>> wordEntry : hm.entrySet()) {
      String word = wordEntry.getKey();
      int count_l = -1;
      int count_r = -1;
      HashMap<String, Integer> m = wordEntry.getValue();
      if (m.get("Liberal") != null) count_l = m.get("Liberal");
      else count_l = 0;
      if (m.get("Conservative") != null) count_r = m.get("Conservative");
      else count_r = 0;
      try {
        String query = "INSERT into Dictionary (word, l_count, r_count)" + " values (?, ?, ?)";
        ps = conn.prepareStatement(query);
        ps.setString(1, word);
        ps.setInt(2, count_l);
        ps.setInt(3, count_r);
        ps.execute();
      } catch (SQLException sqle) {
        System.out.println("SQLException: " + sqle.getMessage());
      }
    }
  }

  static String normalizeString(String raw) {
    String result = raw.replaceAll("\\p{Punct}|[^\\x00-\\x7F]", "");
    return result.toLowerCase();
  }
}
// static void evaluatePhrase(String input) {
    //     input = normalizeString(input);
    //     String s_arr[] = input.split(" ");
    //     double l_val = 0.0;
    //     double r_val = 0.0;

    //     for (String s : s_arr) {
    //         s = s.trim();
    //         Statement st = null;
    //         PreparedStatement ps = null;
    //         ResultSet rs = null;
    //         try {
    //             String query = "SELECT * FROM Dictionary WHERE word = ?;";
    //             ps = conn.prepareStatement(query);
    //             ps.setString(1, s);
    //             rs = ps.executeQuery();
    //             if (rs.next()) {
    //                 l_val += rs.getDouble("l_score");
    //                 r_val += rs.getDouble("r_score");
    //             }
    //         } catch (SQLException sqle) {
    //             System.out.println("SQLException: " + sqle.getMessage());
    //         }

    //         // HashMap<String, Double> p_map = p_o_p_b_w.get(s);
    //         // if (p_map != null) {
    //         // if (p_map.get("Liberal") != null)
    //         // l_val += p_map.get("Liberal");
    //         // if (p_map.get("Conservative") != null)
    //         // r_val += p_map.get("Conservative");
    //         // }
    //     }

    //     if (l_val > r_val)
    //         System.out.println("Liberal");
    //     else if (l_val < r_val)
    //         System.out.println("Conservative");
    //     else
    //         System.out.println("Tie");
    //     System.out.println("l: " + l_val + ", r: " + r_val);
    // }
