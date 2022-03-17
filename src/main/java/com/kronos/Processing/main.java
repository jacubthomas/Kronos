import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import com.mysql.cj.xdevapi.Statement;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class main {
    static ArrayList<Reddit> reddits = new ArrayList<Reddit>();
    static HashMap<String, HashMap<String, Integer>> hm = new HashMap<String, HashMap<String, Integer>>();
    static HashMap<String, HashMap<String, Double>> p_o_p_b_w = new HashMap<String, HashMap<String, Double>>();
    static Connection conn = null;
    static int word_count = 0;

    // American Nuclear Society urges forces to refrain from military actions near
    // nuclear facilities"

    public static void main(String[] args) throws Exception {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Reddit?user=root&password=root");
        // DBtoHM();
        prob_of_party_by_word();
        String input = " ";
        System.out.println("Enter a phrase : ");
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        while (!input.equalsIgnoreCase("Quit")) {
            evaluatePhrase(input);
            System.out.println("Enter a phrase : ");
            input = sc.nextLine();
        }
    }

    static void evaluatePhrase(String input) {
        input = normalizeString(input);
        String s_arr[] = input.split(" ");
        double l_val = 0.0;
        double r_val = 0.0;

        for (String s : s_arr) {
            s = s.trim();
            Statement st = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String query = "SELECT * FROM Dictionary WHERE word = ?;";
                ps = conn.prepareStatement(query);
                ps.setString(1, s);
                rs = ps.executeQuery();
                if (rs.next()) {
                    l_val += rs.getDouble("l_score");
                    r_val += rs.getDouble("r_score");
                }
            } catch (SQLException sqle) {
                System.out.println("SQLException: " + sqle.getMessage());
            }

            // HashMap<String, Double> p_map = p_o_p_b_w.get(s);
            // if (p_map != null) {
            // if (p_map.get("Liberal") != null)
            // l_val += p_map.get("Liberal");
            // if (p_map.get("Conservative") != null)
            // r_val += p_map.get("Conservative");
            // }
        }

        if (l_val > r_val)
            System.out.println("Liberal");
        else if (l_val < r_val)
            System.out.println("Conservative");
        else
            System.out.println("Tie");
        System.out.println("l: " + l_val + ", r: " + r_val);
    }

    static void prob_of_party_by_word() {
        for (Map.Entry<String, HashMap<String, Integer>> wordEntry : hm.entrySet()) {
            String word = wordEntry.getKey();
            p_o_p_b_w.put(word, new HashMap<String, Double>());

            double lib = 0;
            if (wordEntry.getValue().get("Liberal") != null)
                lib = wordEntry.getValue().get("Liberal");
            double cons = 0;
            if (wordEntry.getValue().get("Conservative") != null)
                cons = wordEntry.getValue().get("Conservative");
            double sum = lib + cons;
            double left = (double) ((lib/sum) * 1000) / (word_count);
            double right = (double) ((cons/sum) * 1000) / (word_count);

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
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Reddit?user=root&password=root");
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

    // static void ingestXSLX()
    // {
    // OPCPackage pkg = OPCPackage.open(new File("data/Reddit.xlsx"));
    // XSSFWorkbook wb = new XSSFWorkbook(pkg);
    // XSSFSheet sheet = wb.getSheetAt(0);
    // Iterator<Row> itr = sheet.iterator();
    // while (itr.hasNext()) {
    // try {
    // Reddit r = new Reddit();
    // Row row = itr.next();
    // row = itr.next();
    // Iterator<Cell> cellitr = row.cellIterator();
    // Cell cell = cellitr.next();
    // String raw = cell.getStringCellValue();
    // String processed = normalizeString(raw);
    // if (processed.length() <= 2)
    // continue;
    // r.setPost(processed);
    // cell = cellitr.next();
    // r.setParty(cell.getStringCellValue());
    // cell = cellitr.next();
    // r.setId(cell.getStringCellValue());
    // reddits.add(r);
    // } catch (Exception ex) {
    // continue;
    // }
    // }
    // try {
    // Class.forName("com.mysql.cj.jdbc.Driver");
    // } catch (ClassNotFoundException e) {
    // e.printStackTrace();
    // }
    // Statement st = null;
    // PreparedStatement ps = null;
    // ResultSet rs = null;
    // try {
    // conn =
    // DriverManager.getConnection("jdbc:mysql://localhost:3306/Reddit?user=root&password=root");
    // for (Reddit r : reddits) {
    // String query = "INSERT into Reddit (id, post, party)" +
    // " values (?, ?, ?)";
    // ps = conn.prepareStatement(query);
    // ps.setString(1, r.getId());
    // ps.setString(2, r.getPost());
    // ps.setString(3, r.getParty());
    // ps.execute();
    // }
    // } catch (Exception e) {
    // }
    // int i = 0;
    // int left = 0;
    // int right = 0;
    // for (Reddit r : reddits) {
    // String s_arr[] = r.getPost().split(" ");
    // for (String s : s_arr) {
    // s = s.trim();
    // if (s.length() <= 2)
    // continue;
    // HashMap<String, Integer> m = hm.get(s);
    // if (m == null) {
    // hm.put(s, new HashMap<String, Integer>());
    // hm.get(s).put(r.getParty(), 1);
    // } else {
    // try {
    // int m_count = 1;
    // if (m.get(r.getParty()) != null)
    // m_count += m.get(r.getParty());

    // m.put(r.getParty(), m_count);
    // if (r.getParty().equalsIgnoreCase("Liberal"))
    // left++;
    // else
    // right++;
    // } catch (NullPointerException npe) {
    // System.err.println(r.getId() + " : " + r.getParty() + " : " + r.getPost());
    // } catch (Exception exc) {
    // System.err.println(exc.getMessage());
    // }
    // }
    // i++;
    // }
    // }
    // System.err.println("processed " + i + " words!");
    // System.err.println("processed " + left + " left words!");
    // System.err.println("processed " + right + " right words!");
    // processWords();
    // }

    static void processWords() {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        for (Map.Entry<String, HashMap<String, Integer>> wordEntry : hm.entrySet()) {
            String word = wordEntry.getKey();
            int count_l = -1;
            int count_r = -1;
            HashMap<String, Integer> m = wordEntry.getValue();
            if (m.get("Liberal") != null)
                count_l = m.get("Liberal");
            else
                count_l = 0;
            if (m.get("Conservative") != null)
                count_r = m.get("Conservative");
            else
                count_r = 0;
            try {
                String query = "INSERT into Dictionary (word, l_count, r_count)" +
                        " values (?, ?, ?)";
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