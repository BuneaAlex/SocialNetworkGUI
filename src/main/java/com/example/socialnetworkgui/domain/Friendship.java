package com.example.socialnetworkgui.domain;



import com.example.socialnetworkgui.utils.Constants;
import com.example.socialnetworkgui.utils.Pair;

import java.time.LocalDateTime;

/**
 * Friendship between two Users
 */

public class Friendship extends Entity<Pair<Integer,Integer>>{

    private LocalDateTime friendsFrom;

    /**
     * constructor
     * @param id1-id of the first user
     * @param id2-id of the second user
     * @param friendsFrom-the date in which the friendship started
     */
    public Friendship(Integer id1,Integer id2,LocalDateTime friendsFrom) {
        this.setId(new Pair<>(id1,id2));
        this.friendsFrom = friendsFrom;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public String getFriendsFromFormatted() {
        return friendsFrom.format(Constants.DATE_FORMAT);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + getId().first.toString() + "," + getId().second.toString() + " " +
                "friendsFrom=" + getFriendsFromFormatted() +
                '}';
    }
}
