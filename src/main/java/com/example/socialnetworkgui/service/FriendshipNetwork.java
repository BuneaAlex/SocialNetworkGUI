package com.example.socialnetworkgui.service;



import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.Pair;

import java.util.*;

/**
 * Manages communities of users.txt with mutual friendship
 */
public class FriendshipNetwork {

    private Vector<Vector<Integer>> graph;

    private Map<Integer,Boolean> viz;
    private Map<Integer,Integer> codeID;
    private Map<Integer,Integer> decodeID;
    private Vector<Vector<Integer>> connectedComponents;

    private Iterable<User> users;
    private Iterable<Friendship> friendships;

    private Integer numberOfCommunities = 0;

    /***
     * Constructor
     * @param users -the collection of user on which the communities are build from
     */

    public FriendshipNetwork(Iterable<User> users, Iterable<Friendship> friendships) {
        this.users = users;
        this.friendships = friendships;
        this.graph = new Vector<>();
        this.viz = new HashMap<>();
        this.connectedComponents = new Vector<>();
        this.codeID = new HashMap<>();
        this.decodeID = new HashMap<>();

        Integer index = 0;
        for(User user : this.users)
        {
            codeID.put(user.getId(),index);
            decodeID.put(index,user.getId());
            viz.put(index,false);
            graph.add(new Vector<>());//modif
            index++;
        }

        for(Friendship f : this.friendships)
        {
            Pair<Integer,Integer> p = f.getId();
            Integer id1_coded = codeID.get(p.first);
            Integer id2_coded = codeID.get(p.second);
            graph.elementAt(id1_coded).add(id2_coded);
            graph.elementAt(id2_coded).add(id1_coded);
        }

        buildCommunities();
    }

    /**
     * building the connected components with dfs
     */

    private void buildCommunities()
    {
        for (User u : users) {
            Integer vf = codeID.get(u.getId());
            if (!viz.get(vf)) {
                dfs(vf);
                numberOfCommunities++;
            }
        }
    }

    /**
     * dfs to find which nodes are in the same conneted component as node vf
     * @param vf -the node of the graph where we start the search
     */

    private void dfs(Integer vf) {

        viz.put(vf,true);
        for (Integer elem : graph.elementAt(vf))
        {
            if (!viz.get(elem)) {
                dfs(elem);
            }
        }

        try {
            connectedComponents.elementAt(numberOfCommunities).add(vf);
        } catch (ArrayIndexOutOfBoundsException e) {
            connectedComponents.add(new Vector<Integer>());
            connectedComponents.elementAt(numberOfCommunities).add(vf);
        }
    }

    /**
     * bfs to find one of the endpoints of the longest path
     * @param index_comp -which community (connected component) is covered
     * @return an endpoint of the longest path
     */

    private Integer bfs1(Integer index_comp)
    {
        Map<Integer,Integer> dist = new HashMap<>();

        for(Integer elem : connectedComponents.elementAt(index_comp))
        {
            dist.put(elem,-1);
        }

        Queue<Integer> q = new LinkedList<>();

        Integer vf = connectedComponents.elementAt(index_comp).get(0);
        dist.put(vf,0);
        q.add(vf);

        while(!q.isEmpty())
        {
            Integer x = q.remove();
            for (Integer elem : graph.elementAt(x))
            {
                if (dist.get(elem) == -1) {
                    q.add(elem);
                    dist.put(elem,dist.get(x)+1);
                }
            }
        }


        Integer max = 0;
        Integer vf_departat = 0;

        for(Map.Entry<Integer,Integer> entry : dist.entrySet())
        {
            if(entry.getValue() > max)
            {
                max=entry.getValue();
                vf_departat=entry.getKey();
            }
        }

        return vf_departat;

    }

    /**
     * calculating the maximum distance from node vf_start to the other nodes
     * @param vf_start -the node where the bfs starts
     * @param index_comp -which community (connected component) is covered
     * @return the maximum distance
     */

    private Integer bfs2(Integer vf_start,Integer index_comp)
    {
        Map<Integer,Integer> dist = new HashMap<>();

        for(Integer elem : connectedComponents.elementAt(index_comp))
        {
            dist.put(elem,-1);
        }

        Queue<Integer> q = new LinkedList<>();

        dist.put(vf_start,0);
        q.add(vf_start);

        while(!q.isEmpty())
        {
            Integer x = q.remove();
            for (Integer elem : graph.elementAt(x))
            {
                if(dist.get(elem) != null)
                {
                    if (dist.get(elem) == -1) {
                        q.add(elem);
                        dist.put(elem,dist.get(x)+1);
                    }
                }

            }
        }

        Integer max = 0;

        for(Integer val : dist.values())
        {
            if(val>max)
                max=val;
        }

        return max;
    }

    /**
     *
     * @return number of communities
     */
    public Integer getNumberOfCommunities()
    {
        return numberOfCommunities;
    }

    /**
     *
     * @return a pair of the community and its longest path's length
     */

    public Pair<Integer, Integer> mostSociableCommunity()
    {
        Integer max = 0;
        Integer community = -1;


        for(Integer i = 0; i< numberOfCommunities; i++)
        {
            Integer vf = bfs1(i);
            Integer length = bfs2(vf,i);
            if(max<length)
            {
                max=length;
                community=i;
            }

        }

        return new Pair<>(community,max);
    }

    public String MostSociableCommunityMembers()
    {
        Pair<Integer,Integer> p = mostSociableCommunity();
        Integer community = p.first;

        String members = "";

        for(Integer e : connectedComponents.elementAt(community))
        {
            Integer id = decodeID.get(e);
            members = members + id.toString() + " ";
        }

        return members;
    }
}
