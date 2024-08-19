package com.elderlink.backend.services;

import com.elderlink.backend.domains.entities.BlogEntity;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    public boolean doesBlogExistById(Long id);
    public void deleteBlog(Long id);

    public BlogEntity updateBlog(Long id, BlogEntity blogEntity);

    public Optional<BlogEntity> doesBlogExistByTitle(String title);

    public BlogEntity createBlog(BlogEntity blogEntity);

    public List<BlogEntity> getBlogs();
}