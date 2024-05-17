package com.panda.pandasocialmediaback.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "like")
@Data
public class Like {
    @Id
    private String id;
    @DBRef //many to one
    private User user;

    @DBRef //many to one
    private Twit twit;
}
