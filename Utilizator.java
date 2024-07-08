package TemaTest;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilizator {

    public String nume_utilizator;
    private String parola;
    public static Utilizator[] utilizatori;

    public static int numar_utilizatori = 0;

    private Postare[] postari_user_curent;

    Postare[] get_postari_user_curent() {
        return postari_user_curent;
    }

    private int nr_postari_user_curent = 0;

    int get_Nr_postari_user_curent() {
        return nr_postari_user_curent;
    }

    private Postare[] postari_apreciate;

    Postare[] getPostari_apreciate() {
        return postari_apreciate;
    }

    private int nr_postari_apreciate = 0;

    int getNr_postari_apreciate() {
        return nr_postari_apreciate;
    }

    void setNr_postari_apreciate(int x) {
        this.nr_postari_apreciate = x;
    }

    private  Utilizator[] follow_list; //pe cine urmareste

    Utilizator[] getFollow_list() {
        return follow_list;
    }
    int follow_count = 0; //cati urmareste

    private Utilizator[] followed_list; //de catre cine e urmarit

    Utilizator[] getFollowed_list() {
        return followed_list;
    }

    int followed_count = 0; //de cati e urmarit

    int getFollow_count() {
        return follow_count;
    }

    private int total_number_of_likes = 0;

    int user_id;

    static int max_user_id = 0;

    void setTotal_number_of_likes() {
        int suma = 0;
        int i,j;
        for(j = 0;j < Postare.nr_total_postari;j++)
            if(Postare.postari[j].owner.nume_utilizator.equals(this.nume_utilizator))
                suma = suma + Postare.postari[j].getNumber_of_likes();

        for(j = 0; j < Comentariu.nr_total_comentarii ; j++)
            if(Comentariu.comentarii[j].owner_name.equals(this.nume_utilizator))
                suma = suma + Comentariu.comentarii[j].getNr_aprecieri_comm_curent();


        this.total_number_of_likes = suma;
    }


    int follow(String username2) {
        if(this.nume_utilizator.equals(username2)) //nu isi da follow singur
            return 0;
        int i;
        for(i = 0; i < follow_count;i++) {
            if(follow_list[i].nume_utilizator.equals(username2)) //il are deja la follow
                return 0;
        }
        follow_count++;
        Utilizator[] copie = new Utilizator[follow_count];
        for(i = 0; i < follow_count - 1;i++)
            copie[i] = follow_list[i];
        follow_list = new Utilizator[follow_count];
        for(i = 0; i < follow_count - 1;i++)
            follow_list[i] = copie[i];
        for(i = 0; i < numar_utilizatori;i++)
            if(utilizatori[i].nume_utilizator.equals(username2)) {
                follow_list[follow_count - 1] = utilizatori[i];
                utilizatori[i].followed_count++;
                int k;
                copie = new Utilizator[utilizatori[i].followed_count];
                for(k = 0; k < utilizatori[i].followed_count - 1;k++)
                    copie[k] = utilizatori[i].followed_list[k];
                utilizatori[i].followed_list = new Utilizator[utilizatori[i].followed_count];
                for(k = 0; k < utilizatori[i].followed_count - 1;k++)
                    utilizatori[i].followed_list[k] = copie[k];
                utilizatori[i].followed_list[k] = this;
                break;
            }

        return 1;
    }

    int unfollow(String username2) {
        if(this.nume_utilizator.equals(username2))
            return 0;
        int i,j,k,m;
        Utilizator user2 = null;
        for(i = 0; i < this.follow_count;i++) {
            if(follow_list[i].nume_utilizator.equals(username2)) {
                user2 = follow_list[i];
                for(k = 0; k < user2.followed_count;k++)
                    if(user2.followed_list[k].nume_utilizator.equals(this.nume_utilizator)) {
                        for(m = k; m < user2.followed_count - 1;m++)
                            user2.followed_list[m] = user2.followed_list[m + 1];
                        user2.followed_count--;
                    }
                for(j = i; j < this.follow_count - 1;j++)
                    follow_list[j] = follow_list[j + 1];
                this.follow_count--;
                return 1;
            }
        }
        return 0;
    }

    void adauga_postare(String text_postare) {
        this.nr_postari_user_curent++;
        Postare post = new Postare(text_postare,this);
        Postare[] copie = new Postare[nr_postari_user_curent];
        for(int i = 0; i < this.nr_postari_user_curent - 1;i++)
            copie[i] = postari_user_curent[i];
        postari_user_curent = new Postare[nr_postari_user_curent];
        for(int i = 0; i < this.nr_postari_user_curent - 1;i++)
            postari_user_curent[i] = copie[i];
        postari_user_curent[nr_postari_user_curent - 1] = post;
    }

    int sterge_postare(String id) { //returneaza 1 pt stergere cu succes
        int i,j;
        for(i = 0; i < this.nr_postari_user_curent;i++)
            if(this.postari_user_curent[i].getId().equals(id)) { //am gasit postarea
                for(j = i;  j < nr_postari_user_curent - 1;j++)
                    postari_user_curent[j] = postari_user_curent[j + 1];
                this.nr_postari_user_curent--;
                for(j = 0;j < Postare.nr_total_postari;j++)
                    if(Postare.postari[j].getId().equals(id))
                        break;
                for(i = j;i < Postare.nr_total_postari - 1;i++)
                    Postare.postari[i] = Postare.postari[i + 1];
                Postare.nr_total_postari--;
                return 1;
            }
        return 0;
    }

    int apreciaza_postare(String post_id) {
        Postare post = null;
        for(int i = 0; i < this.nr_postari_user_curent;i++)
            if(postari_user_curent[i].getId().equals(post_id)) //nu isi poate da like singur
                return 0;
        for(int i = 0; i < this.nr_postari_apreciate;i++)
            if(postari_apreciate[i].getId().equals(post_id)) //i-am dat deja like
                return 0;

        for(int i = 0; i < Postare.nr_total_postari;i++)
            if(Postare.postari[i].getId().equals(post_id)) {
                post = Postare.postari[i];
                break;
            }

        Postare []copie = new Postare[++this.nr_postari_apreciate];
        for(int i = 0; i < this.nr_postari_apreciate - 1;i++)
            copie[i] = this.postari_apreciate[i];
        this.postari_apreciate = new Postare[nr_postari_apreciate];
        for(int i = 0; i < this.nr_postari_apreciate - 1;i++)
            this.postari_apreciate[i] = copie[i];
        postari_apreciate[nr_postari_apreciate - 1] = post;
        postari_apreciate[nr_postari_apreciate - 1].setNumber_of_likes(postari_apreciate[nr_postari_apreciate - 1].getNumber_of_likes() + 1);
        return 1;
    }

    static int da_unlike_la_postare(String username,String post_id) {
        int i,j;
        Utilizator user = null;
        for(i = 0; i < Utilizator.numar_utilizatori;i++)
            if(Utilizator.utilizatori[i].nume_utilizator.equals(username)) {
                user = Utilizator.utilizatori[i];
                break;
            }



        for(i = 0; i < user.getNr_postari_apreciate();i++)
            if(user.getPostari_apreciate()[i].getId().equals(post_id)) {
                user.getPostari_apreciate()[i].setNumber_of_likes(user.getPostari_apreciate()[i].getNumber_of_likes() - 1);
                for(j = i; j < user.getNr_postari_apreciate() - 1;j++)
                    user.getPostari_apreciate()[j] = user.getPostari_apreciate()[j + 1];
                user.setNr_postari_apreciate(user.getNr_postari_apreciate() - 1);
                return 1;
            }
        return 0;
    }

    Utilizator(String username,String password) {
        this.nume_utilizator = username;
        this.setParola(password);
        numar_utilizatori++;
        this.user_id = ++max_user_id;
    }

    void setParola(String password) {
        this.parola = password;
    }

    String getParola() {
        return this.parola;
    }

    static int verifica_utilizator_unic(String username) {

        for(int i = 0;i < numar_utilizatori; i++) {
            if(utilizatori[i].nume_utilizator.equals(username))
                return 0;
        }
        return 1;
    }

    static int check_password(String username,String password) {
        for(int i = 0; i < numar_utilizatori;i++) {
            if(utilizatori[i].nume_utilizator.equals(username)) {
                if(utilizatori[i].getParola().equals(password))
                    return 1;
                return 0;
            }
        }
        return 0;
    }

    public static void adauga_utilizator(Utilizator user) {
        Utilizator[] copie = new Utilizator[numar_utilizatori];
        int i;
        for(i = 0; i < numar_utilizatori - 1;i++)
            copie[i] = utilizatori[i];
        utilizatori = new Utilizator[numar_utilizatori];
        for(i = 0; i < numar_utilizatori - 1;i++)
            utilizatori[i] = copie[i];
        utilizatori[numar_utilizatori - 1] = user;
    }

    void getFollowings() {
        int i,j;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String currentDateAsString = dateFormat.format(date);
        System.out.print("{'status':'ok','message': [");
        for(i = follow_count - 1; i >= 0; i--) { //cei de la follow
            for(j = this.follow_list[i].nr_postari_user_curent - 1; j >= 0; j--) {
                System.out.print("{" + "'post_id':" + this.follow_list[i].postari_user_curent[j].getId());
                System.out.print(",'post_text':");
                System.out.print(this.follow_list[i].postari_user_curent[j].text);
                System.out.print(",'post_date':" + "'" + currentDateAsString + "'");
                System.out.print(",'username':" + this.follow_list[i].nume_utilizator + "}");
                if(!(i == 0 && j == 0)) //nu sunt la ultima postarea afisata
                    System.out.print(",");
            }
        }
        System.out.print("]}");
    }

    int getUserPosts(String username2) {
        int i,j;
        Utilizator user2 = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String currentDateAsString = dateFormat.format(date);
        for(i = 0;i < this.follow_count;i++)
            if(this.follow_list[i].nume_utilizator.equals(username2)) {
                user2 = this.follow_list[i];
                System.out.print("{'status':'ok','message': [");
                for(j = user2.nr_postari_user_curent - 1; j >= 0; j--) {
                    System.out.print("{" + "'post_id':" + user2.postari_user_curent[j].getId());
                    System.out.print(",'post_text':");
                    System.out.print(user2.postari_user_curent[j].text);
                    System.out.print(",'post_date':" + "'" + currentDateAsString + "'}");
                    if(j != 0) //nu sunt la ultima postarea afisata
                        System.out.print(",");
                }
                System.out.print("]}");
                return 1;
            }
        return 0;
    }

    void getFollowing() {
        System.out.print("{ 'status' : 'ok', 'message' : [ ");
        for(int i = 0; i < this.follow_count;i++) {
            System.out.print(this.follow_list[i].nume_utilizator);
            if(i == this.follow_count - 1)
                System.out.print(" ");
            else
                System.out.print(", ");
        }
        System.out.print("]}");
    }

    void getFollowers() {
        System.out.print("{'status':'ok','message': [");
        for(int i = 0; i < this.followed_count;i++) {
            System.out.print(this.followed_list[i].nume_utilizator);
            if(i == this.followed_count - 1)
                System.out.print("]}");
            else
                System.out.print(",");
        }
    }

    static void getMostFollowedUsers() {
        Utilizator[] copie = new Utilizator[numar_utilizatori];
        Utilizator aux = null;
        int i,j;
        for(i = 0; i < numar_utilizatori;i++)
            copie[i] = utilizatori[i];
        for(i = 0;i < numar_utilizatori - 1;i++)
            for(j = i + 1; j < numar_utilizatori;j++)
                if(utilizatori[i].followed_count < utilizatori[j].followed_count) {
                    aux = utilizatori[i];
                    utilizatori[i] = utilizatori[j];
                    utilizatori[j] = aux;
                }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String currentDateAsString = dateFormat.format(date);
        System.out.print("{ 'status' : 'ok', 'message' : [");
        j = 0;
        while(utilizatori[j].followed_count > 0)
            j++;
        for(i = 0; i < j;i++) {
            System.out.print("{'username' : " + utilizatori[i].nume_utilizator);
            System.out.print(",'number_of_followers' : ' " + utilizatori[i].followed_count + "' }");
            if(i != j - 1)
                System.out.print(",");
            else
                System.out.print(" ]}");
        }
    }

    static void getMostLikedUsers() {
        int i,j;
        for(i = 0;i < numar_utilizatori;i++)
            utilizatori[i].setTotal_number_of_likes();


        Utilizator[] copie = new Utilizator[numar_utilizatori];
        Utilizator aux = null;
        for(i = 0; i < numar_utilizatori;i++)
            copie[i] = utilizatori[i];
        for(i = 0;i < numar_utilizatori - 1;i++)
            for(j = i + 1; j < numar_utilizatori;j++)
                if(copie[i].total_number_of_likes < copie[j].total_number_of_likes) {
                    aux = copie[i];
                    copie[i] = copie[j];
                    copie[j] = aux;
                }


        for(i = 0;i < numar_utilizatori - 1;i++)
            for(j = i + 1; j < numar_utilizatori;j++)
                if(copie[i].total_number_of_likes == copie[j].total_number_of_likes && copie[i].user_id > copie[j].user_id) {
                    aux = copie[i];
                    copie[i] = copie[j];
                    copie[j] = aux;
                }

        j = 0;
        while(copie[j].total_number_of_likes > 0)
            j++;

        System.out.print("{ 'status' : 'ok', 'message' : [");
        for(i = 0; i < j; i++) {
            System.out.print("{'username' : " + copie[i].nume_utilizator + ",'number_of_likes' : '");
            System.out.print(copie[i].total_number_of_likes + "'}");
            if(i != j - 1)
                System.out.print(",");
            else
                System.out.print("]}");
        }

    }


    static void cleanup_all() {
        numar_utilizatori = 0;
        Postare.nr_total_postari = 0;
        Postare.max_id = 0;
        Comentariu.nr_total_comentarii = 0;
        Comentariu.max_id = 0;
    }


}