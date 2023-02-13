package com.example.socialnetworkgui.repository.file;



import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.utils.Constants;
import com.example.socialnetworkgui.utils.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFile extends AbstractFileRepo<Pair<Integer,Integer>, Friendship>{

    public FriendshipFile(Validator<Friendship> validator, String filename) {
        super(validator, filename);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Integer id1 = Integer.valueOf(attributes.get(0));
        Integer id2 = Integer.valueOf(attributes.get(1));
        String date = attributes.get(2);
        LocalDateTime friendsFrom = LocalDateTime. parse(date, Constants.DATE_FORMAT);
        return new Friendship(id1,id2,friendsFrom);
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        String id1 = entity.getId().first.toString();
        String id2 = entity.getId().second.toString();
        String friendsFrom = entity.getFriendsFromFormatted();
        return id1 + "," + id2 + "," + friendsFrom;
    }


}
