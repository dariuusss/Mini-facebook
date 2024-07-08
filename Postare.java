package TemaTest;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Postare {

    String text;

    private String id;

    String getId() {
        return id;
    }

    static Postare []postari;
    static int nr_total_postari = 0;

    private Comentariu[] comentarii_postare_curenta;

    Comentariu[] getComentarii_postare_curenta() {
        return comentarii_postare_curenta;
    }

    private int nr_comentarii_postare_curenta = 0;

    int getNr_comentarii_postare_curenta() {
        return nr_comentarii_postare_curenta;
    }

    static int max_id = 0;

    private int number_of_likes = 0;

    int getNumber_of_likes() {
        return this.number_of_likes;
    }

    void setNumber_of_likes(int x) {
        this.number_of_likes = x;
    }

    Utilizator owner;

    Postare(String text_postare,Utilizator proprietar) {
        this.text = text_postare;
        this.id = "'" + String.valueOf(++max_id) + "'";
        nr_total_postari++;
        this.owner = proprietar;
        Postare []copie = new Postare[nr_total_postari];
        for(int i = 0 ; i < nr_total_postari - 1 ; i++)
            copie[i] = postari[i];
        postari = new Postare[nr_total_postari];
        for(int i = 0; i < nr_total_postari - 1;i++)
            postari[i] = copie[i];
        postari[nr_total_postari - 1] = this;

    }

    static int gaseste_postare(String id) {
        for(int i = 0; i < nr_total_postari;i++)
            if(postari[i].id.equals(id))
                return 1;
        return 0;
    }

    void getPostDetails() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String currentDateAsString = dateFormat.format(date);
        System.out.print("{'status' : 'ok', 'message' : [{'post_text' : ");
        System.out.print(this.text + ", 'post_date' :'" + currentDateAsString);
        System.out.print("', 'username' : " + this.owner.nume_utilizator + ", 'number_of_likes' : ");
        System.out.print("'" + this.number_of_likes + "', 'comments' : [" );
        for(int i = 0; i < this.nr_comentarii_postare_curenta;i++) {
            System.out.print("{'comment_id' : " + this.comentarii_postare_curenta[i].getId());
            System.out.print(" , 'comment_text' : " + this.comentarii_postare_curenta[i].text);
            System.out.print(", 'comment_date' : '" + currentDateAsString + "', " + "'username' : ");
            System.out.print(this.comentarii_postare_curenta[i].owner_name + ", 'number_of_likes' : ");
            System.out.print("' " + this.comentarii_postare_curenta[i].getNr_aprecieri_comm_curent() + "'}]");
            if(i == this.nr_comentarii_postare_curenta - 1)
                System.out.print(" }");
            else
                System.out.print(",");

        }

        System.out.print("] }");

    }

    void adauga_comentariu(String owner,String text_comentariu,Postare postare) {
        this.nr_comentarii_postare_curenta++;
        Comentariu comment = new Comentariu(owner,text_comentariu,this);
        Comentariu[] copie = new Comentariu[nr_comentarii_postare_curenta];
        for(int i = 0; i < this.nr_comentarii_postare_curenta - 1;i++)
            copie[i] = comentarii_postare_curenta[i];
        comentarii_postare_curenta = new Comentariu[nr_comentarii_postare_curenta];
        for(int i = 0; i < this.nr_comentarii_postare_curenta - 1;i++)
            comentarii_postare_curenta[i] = copie[i];
        comentarii_postare_curenta[nr_comentarii_postare_curenta - 1] = comment;
    }

    void sterge_comentariu(String comment_id) {
        int i,j;
        for(i = 0; i < nr_comentarii_postare_curenta;i++)
            if(comentarii_postare_curenta[i].getId().equals(comment_id)) {
                for(j = i;  j < nr_comentarii_postare_curenta - 1;j++)
                    comentarii_postare_curenta[j] = comentarii_postare_curenta[j + 1];
                this.nr_comentarii_postare_curenta--;
                for(j = 0;j < Comentariu.nr_total_comentarii;j++)
                    if(Comentariu.comentarii[i].getId().equals(comment_id))
                        break;
                for(i = j;i < Comentariu.nr_total_comentarii - 1;i++)
                    Comentariu.comentarii[i] = Comentariu.comentarii[i + 1];
                Comentariu.nr_total_comentarii--;

            }

    }

    static void getMostLikedPosts() {
        Postare[] copie = new Postare[nr_total_postari];
        Postare aux = null;
        int i,j;
        for(i = 0; i < nr_total_postari;i++)
            copie[i] = postari[i];
        for(i = 0;i < nr_total_postari - 1;i++)
            for(j = i + 1; j < nr_total_postari;j++)
                if(postari[i].number_of_likes >= postari[j].number_of_likes) {
                    aux = postari[i];
                    postari[i] = postari[j];
                    postari[j] = aux;
                }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String currentDateAsString = dateFormat.format(date);

        System.out.print("{ 'status' : 'ok', 'message' : [");
        for(i = 0;i < nr_total_postari;i++) {
            System.out.print("{'post_id' : " + copie[i].getId()  + ",'post_text' : ");
            System.out.print(copie[i].text + ", 'post_date' : '" + currentDateAsString + "', 'username' : ");
            System.out.print(copie[i].owner.nume_utilizator + ", 'number_of_likes' : '");
            System.out.print(copie[i].number_of_likes + "' }");
            if(i == nr_total_postari - 1)
                System.out.print(" ]}");
            else
                System.out.print(",");
        }

    }

    static void getMostCommentedPosts() {
        Postare[] copie = new Postare[nr_total_postari];
        Postare aux = null;
        int i,j;
        for(i = 0; i < nr_total_postari;i++)
            copie[i] = postari[i];
        for(i = 0;i < nr_total_postari - 1;i++)
            for(j = i + 1; j < nr_total_postari;j++)
                if(postari[i].nr_comentarii_postare_curenta >= postari[j].nr_comentarii_postare_curenta) {
                    aux = postari[i];
                    postari[i] = postari[j];
                    postari[j] = aux;
                }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String currentDateAsString = dateFormat.format(date);

        System.out.print("{ 'status' : 'ok', 'message' : [");
        for(i = 0;i < nr_total_postari;i++) {
            System.out.print("{'post_id' : " + copie[i].getId()  + ",'post_text' : ");
            System.out.print(copie[i].text + ", 'post_date' : '" + currentDateAsString + "', 'username' : ");
            System.out.print(copie[i].owner.nume_utilizator + ", 'number_of_comments' : '");
            System.out.print(copie[i].nr_comentarii_postare_curenta + "' }");
            if(i == nr_total_postari - 1)
                System.out.print("]}");
            else
                System.out.print(",");
        }

    }

}