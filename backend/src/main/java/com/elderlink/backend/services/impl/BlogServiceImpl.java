package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.BlogEntity;
import com.elderlink.backend.repositories.BlogRepository;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.BlogService;
import com.elderlink.backend.services.UserService;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    Logger logger = LoggerFactory.getLogger(BlogService.class);
    private static final Logger log = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private UserService userService;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IsUserAuthorized isUserAuthorized;
    @Autowired
    private UserRepository userRepository;
    /**
     * Check if a blog exists by its ID.
     *
     * @param id The ID of the blog
     * @return True if the blog exists, otherwise false
     */
    @Override
    public boolean doesBlogExistById(Long id) {
        try {
            return blogRepository.existsById(id);
        } catch (Exception e) {
            logger.error("An error occurred while checking if blog exists or not!. -> {}", e.getMessage());
            throw new RuntimeException("An error occurred while checking if blog exists or not.");
        }
    }
    /**
     * Check if a blog exists by its title.
     *
     * @param title The title of the blog
     * @return An optional containing the blog if it exists, otherwise empty
     */

    @Override
    public Optional<BlogEntity> doesBlogExistByTitle(String title) {
        try {
            return blogRepository.findByTitle(title);
        } catch (Exception e) {
            logger.error("An error occurred while checking if blog exists or not!. -> {}", e.getMessage());
            throw new RuntimeException("An error occurred while checking if blog exists or not.");
        }
    }
    /**
     * Update an existing blog.
     *
     * @param id          The ID of the blog to update
     * @param blogEntity  The updated blog entity
     * @return The updated blog entity
     */
    @Override
    public BlogEntity updateBlog(Long id, BlogEntity blogEntity) {
        try {
            if (!doesBlogExistById(id)) {
                throw new RuntimeException("Blog with this id, does not exists.");
            }

            BlogEntity existingBlog = blogRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Blog doesn't exists!"));

            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(blogEntity, existingBlog);
            modelMapper.getConfiguration().setSkipNullEnabled(false);

            return blogRepository.save(existingBlog);

        } catch (RuntimeException e) {
            logger.error("An error occurred while updating the user. -> {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * Create a new blog.
     *
     * @param blogEntity The blog entity to create
     * @return The created blog entity
     */

    @Override
    public BlogEntity createBlog(BlogEntity blogEntity) {
        try {

            if (!userService.isUserExisted(blogEntity.getUser().getId())) {
                throw new EntityNotFoundException("User with this id doesn't exist!");
            }

            String title = blogEntity.getTitle();
            String body = blogEntity.getBody();

            if (title.isEmpty() || body.isEmpty()) {
                throw new IllegalArgumentException("Blog must have a title and a body!");
            }

            BlogEntity blog = blogRepository.save(blogEntity);
            logger.info("Blog Created successfully.");
            return blog;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating a blog! " + e.getMessage());
        }
    }
    /**
     * Delete a blog by its ID.
     *
     * @param id The ID of the blog to delete
     */
    public void deleteBlog(Long id) {
        try {
            if (!doesBlogExistById(id)) {
                throw new RuntimeException("Blog with this id, does not exists.");
            }
            blogRepository.deleteById(id);

            log.info("Blog deleted successfully.");
        } catch (RuntimeException e) {
            logger.error("An error occurred while updating the blog. -> {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * Get all blogs.
     *
     * @return A list of all blogs
     */
    public List<BlogEntity> getBlogs() {
        try {
            return blogRepository.findAll ();
        }catch (Exception e){
            throw new RuntimeException (e.getMessage ());
        }
    }
}
