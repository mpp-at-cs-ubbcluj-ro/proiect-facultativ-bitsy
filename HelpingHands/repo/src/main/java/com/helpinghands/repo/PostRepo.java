package com.helpinghands.repo;

import com.helpinghands.domain.Post;

public class PostRepo extends AbstractRepo<Post> implements IPostRepo{
    public PostRepo(Class<Post> entityType) {
        super(entityType);
    }
}
