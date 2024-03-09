package TemaTest;

import org.checkerframework.checker.units.qual.C;

public class Comentariu {

    String text;

    private String id;

    String getId() {
        return id;
    }

    static Comentariu[] comentarii;
    static int nr_total_comentarii = 0;

    static int max_id = 0;

    private String[] aprecieri_comm_curent; //tin minte numele celor care au apreciat comentariul curent

    private int nr_aprecieri_comm_curent = 0; //cati au apreciat

    int getNr_aprecieri_comm_curent() {
        return this.nr_aprecieri_comm_curent;
    }

    Postare postare;

    String owner_name;

    Comentariu(String owner,String text_comentariu,Postare postare) {
        this.owner_name = owner;
        this.text = text_comentariu;
        this.id = "'" + String.valueOf(++max_id) + "'";
        nr_total_comentarii++;
        this.postare = postare;
        Comentariu []copie = new Comentariu[nr_total_comentarii];
        for(int i = 0; i < nr_total_comentarii - 1;i++)
            copie[i] = comentarii[i];
        comentarii = new Comentariu[nr_total_comentarii];
        for(int i = 0; i < nr_total_comentarii - 1;i++)
            comentarii[i] = copie[i];
        comentarii[nr_total_comentarii - 1] = this;

    }

    static int gaseste_comentariu(String id) {
        for(int i = 0; i < nr_total_comentarii;i++)
            if(comentarii[i].id.equals(id))
                return 1;
        return 0;
    }

    static int apreciaza_comentariu(String username,String comment_id) {
        int i,j;
        Comentariu comm = null;
        for(i = 0;i < nr_total_comentarii;i++)
            if(comentarii[i].getId().equals(comment_id)) {
                comm = comentarii[i];
                break;
            }

        for(i = 0; i < comm.nr_aprecieri_comm_curent;i++)
            if(comm.aprecieri_comm_curent[i].equals(username)) //a dat like deja
                return 0;

        comm.nr_aprecieri_comm_curent++;
        String[] copie = new String[comm.nr_aprecieri_comm_curent];
        for(i = 0; i < comm.nr_aprecieri_comm_curent - 1;i++)
            copie[i] = comm.aprecieri_comm_curent[i];
        comm.aprecieri_comm_curent = new String[comm.nr_aprecieri_comm_curent];
        for(i = 0; i < comm.nr_aprecieri_comm_curent - 1;i++)
            comm.aprecieri_comm_curent[i] = copie[i];
        comm.aprecieri_comm_curent[comm.nr_aprecieri_comm_curent - 1] = username;
        return 1;
    }


    static int da_unlike_la_comentariu(String username,String comment_id) {
        int i,j;
        Comentariu comm = null;
        for(i = 0;i < nr_total_comentarii;i++)
            if(comentarii[i].getId().equals(comment_id)) {
                comm = comentarii[i];
                break;
            }

        for(i = 0; i < comm.nr_aprecieri_comm_curent;i++)
            if(comm.aprecieri_comm_curent[i].equals(username)) {
                for(j = i;j < comm.nr_aprecieri_comm_curent - 1;j++)
                    comm.aprecieri_comm_curent[j] = comm.aprecieri_comm_curent[j + 1];
                comm.nr_aprecieri_comm_curent--;
                return 1;
            }

        return 0;
    }


}