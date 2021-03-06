package com.FinalP.finalchat.models.application;

import com.FinalP.finalchat.models.domain.UserD;

import java.io.Serializable;
import java.util.Date;

public class User extends UserD implements Serializable {
    public final String id;
    public final Date creationDate;

    public User(UserD userD) {
        this.name = userD.name;
        this.email = userD.email;
        this.id = email.replaceAll(";", "").replaceAll("\\.", "").replaceAll("@", "");
        this.creationDate = new Date(userD.createDate);

    }
}
