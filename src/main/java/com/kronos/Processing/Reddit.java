// import java.util.Objects;

// public class Reddit {

//     private String id;
//     private String post;
//     private String party;

//     // @CsvBindByName
//     // private int score;

//     public Reddit() {
//     }

//     public Reddit(String id, String post, String party) {
//         this.id = id;
//         this.post = post;
//         this.party = party;
//     }

//     public Reddit(String post, String party) {
//         this.post = post;
//         this.party = party;
//     }

//     public String getId() {
//         return this.id;
//     }

//     public void setId(String id) {
//         this.id = id;
//     }

//     public String getPost() {
//         return this.post;
//     }

//     public void setPost(String post) {
//         this.post = post;
//     }

//     public String getParty() {
//         return this.party;
//     }

//     public void setParty(String party) {
//         this.party = party;
//     }

//     public Reddit id(String id) {
//         setId(id);
//         return this;
//     }

//     public Reddit post(String post) {
//         setPost(post);
//         return this;
//     }

//     public Reddit party(String party) {
//         setParty(party);
//         return this;
//     }

//     @Override
//     public boolean equals(Object o) {
//         if (o == this)
//             return true;
//         if (!(o instanceof Reddit)) {
//             return false;
//         }
//         Reddit reddit = (Reddit) o;
//         return Objects.equals(id, reddit.id) && Objects.equals(post, reddit.post)
//                 && Objects.equals(party, reddit.party);
//     }

//     @Override
//     public int hashCode() {
//         return Objects.hash(id, post, party);
//     }

//     @Override
//     public String toString() {
//         return "{" +
//                 " id='" + getId() + "'" +
//                 ", post='" + getPost() + "'" +
//                 ", party='" + getParty() + "'" +
//                 "}";
//     }

// }