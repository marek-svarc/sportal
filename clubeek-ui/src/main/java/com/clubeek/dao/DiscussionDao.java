package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.DiscussionPost;

public interface DiscussionDao<T> {

    public List<DiscussionPost> getAllDiscussionPosts(int referencedObjectId);
    
    public void insertDiscussionPost(DiscussionPost discussionPost);
    
    public void updateDiscussionPost(DiscussionPost discussionPost);
    
    public void deleteDiscussionPost(int discussionPostId);
}
