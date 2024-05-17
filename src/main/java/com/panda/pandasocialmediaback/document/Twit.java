package com.panda.pandasocialmediaback.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Twit")
@Data
public class Twit {
    @Id
    private String id;
    @DBRef //UNO A UNO
    private User user;

    private String content;
    @DBRef //UNO A MUCHOS
    private List<Like> likes = new ArrayList<>();

    @DBRef //UNO A MUCHOS
    private List<Twit> replyTwits = new ArrayList<>();

    @DBRef //MUCHOS A MUCHOS
    private List<Twit> retwitUser = new ArrayList<>();

    @DBRef //MUCHOS A UNO
    private Twit replyFor;

    private  boolean isReply;

    private boolean isTwit;
}
