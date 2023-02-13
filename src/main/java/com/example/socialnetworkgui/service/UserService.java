package com.example.socialnetworkgui.service;


import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.validators.*;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.db.FriendRequestDBRepo;
import com.example.socialnetworkgui.repository.db.FriendshipDBRepo;
import com.example.socialnetworkgui.repository.db.MessageDBRepo;
import com.example.socialnetworkgui.repository.db.UserDBRepo;
import com.example.socialnetworkgui.utils.Pair;
import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.UserChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service for users
 */

public class UserService extends Service<Integer, User> implements Observable<UserChangeEvent> {

    static UserService service = new UserService(new UserDBRepo(new UserValidator()),new FriendshipDBRepo(new FriendshipValidator()),new FriendRequestDBRepo(new FriendRequestValidator()), new MessageDBRepo(new MessageValidator()));
    private Integer id = 0;

    private final Repository<Pair<Integer,Integer>, Friendship> friendshipRepository;

    private final Repository<Integer, FriendRequest> friendRequestRepository;

    private final MessageDBRepo messageRepository;

    private List<Observer<UserChangeEvent>> observers=new ArrayList<>();

    /**
     * Constructor
     *
     * @param repository              -a generic repository
     * @param friendshipRepository    -repository for friendships between users
     * @param friendRequestRepository -repository for friend requests between users
     * @param messageRepository -repository for messages between users
     */

    private UserService(Repository<Integer, User> repository, Repository<Pair<Integer, Integer>, Friendship> friendshipRepository, Repository<Integer, FriendRequest> friendRequestRepository, MessageDBRepo messageRepository) {
        super(repository);
        this.friendshipRepository = friendshipRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.messageRepository = messageRepository;
        setIdMax();
    }


    public static UserService getInstance()
    {
        return service;
    }

    /**
     * saving an entity in the repository
     * @param entity -a serializable entity which must not be null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     */
    @Override
    public User save(User entity) {
        entity.setId(id);
        id++;
        User user = super.save(entity);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.ADD,entity);
        notifyObservers(event);
        return user;
    }

    public User findUser(Integer id)
    {
        return repository.findOne(id);
    }

    /**
     * Setting the id to the right value
     */
    private void setIdMax() {
        Integer max = 0;
        for (User user : repository.findAll())
        {
            if (user.getId() > max)
                max = user.getId();
        }
        if(max>0)
            id=max+1;
    }

    /**
     * Delete user from repository and from the users friends lists
     * @param id -must not be null
     * @return the removed entity or null if there is no entity with the given id
     */

    @Override
    public User delete(Integer id) {

        List<Friendship> list = new ArrayList<>();

        for(Friendship friendship : friendshipRepository.findAll())
            list.add(friendship);

        for(Friendship friendship : list)
        {
            Integer id1 = friendship.getId().first;
            Integer id2 = friendship.getId().second;
            if(id1 == id || id2 == id)
            {
                Pair<Integer,Integer> pair = new Pair<>(id1,id2);
                friendshipRepository.delete(pair);
            }
        }

        User user = findUser(id);
        super.delete(id);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.DELETE,user);
        notifyObservers(event);
        return user;
    }

    @Override
    public User update(User entity) {
        User user = super.update(entity);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.UPDATE,entity);
        notifyObservers(event);
        return user;
    }

    private Pair<Integer,Integer> getUsersIds(User user1, User user2)
    {
        Integer id1 = user1.getId();
        Integer id2 = user2.getId();
        if(id1 > id2)
        {
            Integer aux=id1;
            id1=id2;
            id2=aux;
        }

        return new Pair<>(id1,id2);
    }

    private Pair<Integer, Integer> getExistentUserIds(Integer ID_user1, Integer ID_user2) {
        User user1 = repository.findOne(ID_user1);
        User user2 = repository.findOne(ID_user2);

        if(user1 == null || user2 == null)
            throw new FriendshipException("Cannot find users with these IDs");

        return getUsersIds(user1,user2);
    }

    /**
     * Adding a friend to both user's list of friends (mutual friendship)
     * @param ID_user1 -id of a user
     * @param ID_user2 -id of another user
     */
    public void addFriend(Integer ID_user1, Integer ID_user2) {
        Pair<Integer, Integer> ids = getExistentUserIds(ID_user1, ID_user2);
        friendshipRepository.findAll().forEach(fr ->
        {if(Objects.equals(fr.getId().first, ids.first) && Objects.equals(fr.getId().second, ids.second))
            throw new FriendshipException("Already friends!");});


        friendshipRepository.save(new Friendship(ids.first,ids.second, LocalDateTime.now()));
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.ADD,null);
        notifyObservers(event);

        }


    /**
     * Removing a friend to both user's list of friends (mutual friendship)
     * @param ID_user1 -id of a user
     * @param ID_user2 -id of another user
     */

    public void removeFriend(Integer ID_user1, Integer ID_user2) {
        Pair<Integer, Integer> ids = getExistentUserIds(ID_user1, ID_user2);
        Pair<Integer,Integer> friendshipId = new Pair<>(ids.first,ids.second);
        friendshipRepository.delete(friendshipId);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.DELETE,null);
        notifyObservers(event);
    }

    /**
     * Update the friendship between the users with ids:ID_user1 & ID_user2
     * @param ID_user1 -id of a user
     * @param ID_user2 -id of another user
     */

    public void updateFriendship(Integer ID_user1, Integer ID_user2)
    {
        Pair<Integer, Integer> ids = getExistentUserIds(ID_user1, ID_user2);

        Friendship friendship = new Friendship(ids.first,ids.second,LocalDateTime.now());
        friendshipRepository.update(friendship);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.UPDATE,null);
        notifyObservers(event);
    }


    public List<User> userFriends(Integer id_user)
    {
        List<User> friends = new ArrayList<>();

        friendshipRepository.findAll().forEach(friendship -> {
            Integer id1 = friendship.getId().first;
            Integer id2 = friendship.getId().second;
            if(Objects.equals(id1, id_user))
            {
                User friend = repository.findOne(id2);
                friends.add(friend);
            }
            else
            {
                if(Objects.equals(id2, id_user))
                {
                    User friend = repository.findOne(id1);
                    friends.add(friend);
                }
            }
        });

        return friends;
    }

    /**
     *
     * @return number of communities
     */

    public int numberOfCommunities()
    {
        FriendshipNetwork friendshipNetwork = new FriendshipNetwork(repository.findAll(),friendshipRepository.findAll());
        return friendshipNetwork.getNumberOfCommunities();
    }

    /**
     *
     * @return most sociable community
     */

    public String mostSociableCommunity()
    {
        FriendshipNetwork friendshipNetwork = new FriendshipNetwork(repository.findAll(),friendshipRepository.findAll());
        return friendshipNetwork.MostSociableCommunityMembers();
    }

    /**
     * Friendships getter
     * @return all friendships
     */

    public Iterable<Friendship> getFriendships()
    {
        return friendshipRepository.findAll();
    }


    /**
     * Sending a friend request
     * @param sentFrom the user from which the request is sent
     * @param sentTo the user to which the request is sent
     */

    public void sendFriendRequest(Integer sentFrom,Integer sentTo)
    {
        FriendRequest friendRequest = new FriendRequest(sentFrom,sentTo, RequestStatus.PENDING,LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.ADD,null);
        notifyObservers(event);
    }

    /**
     * Accepting a friend request
     * @param id the request's id
     */

    public void acceptFriendRequest(Integer id)
    {
        FriendRequest friendRequest = friendRequestRepository.findOne(id);
        friendRequest.setStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.update(friendRequest);
        addFriend(friendRequest.getSentFrom(),friendRequest.getSentTo());
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.ADD,null);
        notifyObservers(event);
    }

    /**
     * Rejecting a friend request
     * @param id the request's id
     */

    public void rejectFriendRequest(Integer id)
    {
        FriendRequest friendRequest = friendRequestRepository.findOne(id);
        friendRequest.setStatus(RequestStatus.REJECTED);
        friendRequestRepository.update(friendRequest);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.UPDATE,null);
        notifyObservers(event);
    }

    /**
     * Friend requests received by user with id = id_user
     * @param id_user user's id
     * @param status request status
     * @return the list of requests received by the user
     */


    public List<FriendRequest> friendRequestsReceived(Integer id_user, RequestStatus status)
    {
        List<FriendRequest> requests = new ArrayList<>();

        friendRequestRepository.findAll().forEach(request ->
        {
            if(Objects.equals(request.getSentTo(), id_user) && request.getStatus()==status)
                requests.add(request);
        });

        return requests;
    }

    public List<UserFriendRequestDTO> getUserFriendRequestDTOS(Integer id_user,RequestStatus status) {
        List<FriendRequest> requestsReceived = friendRequestsReceived(id_user, status);
        List<UserFriendRequestDTO> requestDTOS = new ArrayList<>();
        requestsReceived.forEach(request -> {
            User user = findUser(request.getSentFrom());
            UserFriendRequestDTO userRequest = new UserFriendRequestDTO(user.getFirstName(),user.getLastName(),request.getDateSent());
            userRequest.setId(request.getId());//the same id as the request
            requestDTOS.add(userRequest);
        });
        return requestDTOS;
    }

    public boolean alreadySentFriendRequest(Integer sender,Integer receiver)
    {
        List<FriendRequest> friendRequests = friendRequestsReceived(receiver,RequestStatus.PENDING);
        for(FriendRequest request : friendRequests)
            if(Objects.equals(request.getSentFrom(), sender))
                return true;

        List<FriendRequest> friendRequests2 = friendRequestsReceived(sender,RequestStatus.PENDING);
        for(FriendRequest request : friendRequests2)
            if(Objects.equals(request.getSentFrom(), receiver))
                return true;
        return false;
    }

    /**
     * Getting the id of a friend request between 2 users
     * @param sender-the user who sent the friend request
     * @param receiver-the user who received the friend request
     * @return null if there is no such entry or the id of the entry
     */

    public Integer getFriendRequestPending(Integer sender,Integer receiver)
    {
        for(FriendRequest friendRequest : friendRequestRepository.findAll())
        {
            if(Objects.equals(friendRequest.getSentFrom(), sender) && Objects.equals(friendRequest.getSentTo(), receiver) &&
                    friendRequest.getStatus()==RequestStatus.PENDING)
                return friendRequest.getId();
        }
        return null;
    }

    /**
     *
     * @param sender-the user who sent the friend request
     * @param receiver-the user who received the friend request
     * @return true if the request has been canceled, false if there is no request between users
     */

    public boolean cancelFriendRequest(Integer sender,Integer receiver)
    {
        Integer id = getFriendRequestPending(sender,receiver);
        if(id!=null)
        {
            friendRequestRepository.delete(id);
            UserChangeEvent event = new UserChangeEvent(ChangeEventType.DELETE,null);
            notifyObservers(event);
            return true;
        }

        return false;
    }

    /**
     * Find users with the name: name, except for the user who does the search
     * @param name the name that we are looking for
     * @param user_id the user that cannot appear in the result
     * @return the list of users with the name: name
     */
    public List<User> findUserByName(String name,Integer user_id)
    {
        List<User> users = new ArrayList<>();

        repository.findAll().forEach(user -> {
            if(user.getId() != user_id)
            {
                if(Objects.equals(user.getFirstName(), name) || Objects.equals(user.getLastName(), name))
                    users.add(user);
            }

        });

        return users;
    }

    public List<Message> getMessagesBetweenUsers(Integer sender,Integer receiver)
    {
        return messageRepository.findAllForUser(sender,receiver);
    }

    public void sendMessage(Integer sender,Integer receiver,String message)
    {
        Message messageEntity = new Message(message,sender,receiver,LocalDateTime.now());
        messageRepository.save(messageEntity);
        UserChangeEvent event = new UserChangeEvent(ChangeEventType.ADD,null);
        notifyObservers(event);
    }

    @Override
    public void addObserver(Observer<UserChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserChangeEvent t) {
        observers.forEach(obs -> obs.update(t));
    }
}
