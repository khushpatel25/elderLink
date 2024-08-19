package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.BlogEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.BlogRepository;
import com.elderlink.backend.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private Configuration configuration;

    @Mock private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private BlogServiceImpl blogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configuration = mock (Configuration.class);
    }

    @Test
    void doesBlogExistById_ExistingId_ReturnsTrue() {
        // Arrange
        Long id = 1L;
        when(blogRepository.existsById(id)).thenReturn(true);

        // Act
        boolean result = blogService.doesBlogExistById(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void doesBlogExistById_NonExistingId_ReturnsFalse() {
        // Arrange
        Long id = 1L;
        when(blogRepository.existsById(id)).thenReturn(false);

        // Act
        boolean result = blogService.doesBlogExistById(id);

        // Assert
        assertFalse(result);
    }

    @Test
    void doesBlogExistByTitle_ExistingTitle_ReturnsBlogEntity() {
        // Arrange
        String title = "Test Title";
        BlogEntity blogEntity = new BlogEntity();
        when(blogRepository.findByTitle(title)).thenReturn(Optional.of(blogEntity));

        // Act
        Optional<BlogEntity> result = blogService.doesBlogExistByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(blogEntity, result.get());
    }

    @Test
    void doesBlogExistByTitle_NonExistingTitle_ReturnsEmptyOptional() {
        // Arrange
        String title = "Non Existing Title";
        when(blogRepository.findByTitle(title)).thenReturn(Optional.empty());

        // Act
        Optional<BlogEntity> result = blogService.doesBlogExistByTitle(title);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void createBlog_ValidBlog_ReturnsCreatedBlog() {
        // Arrange
        BlogEntity blogEntity = new BlogEntity();
        UserEntity user = new UserEntity();
        user.setId(1L);
        blogEntity.setUser(user);
        blogEntity.setTitle("Test Title");
        blogEntity.setBody("Test Body");

        when(userService.isUserExisted(1L)).thenReturn(true);
        when(blogRepository.save(blogEntity)).thenReturn(blogEntity);

        // Act
        BlogEntity createdBlog = blogService.createBlog(blogEntity);

        // Assert
        assertNotNull(createdBlog);
        assertEquals("Test Title", createdBlog.getTitle());
        assertEquals("Test Body", createdBlog.getBody());
        assertEquals(user, createdBlog.getUser());
    }

    @Test
    void createBlog_InvalidTitle_ThrowsIllegalArgumentException() {
        // Arrange
        BlogEntity blogEntity = new BlogEntity();
        UserEntity user = new UserEntity();
        user.setId(1L);
        blogEntity.setUser(user);
        blogEntity.setTitle("");
        blogEntity.setBody("Test Body");

        when(userService.isUserExisted(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> blogService.createBlog(blogEntity));
    }

    @Test
    void updateBlogSuccess() {
        Long blogId = 1L;
        BlogEntity existingBlog = new BlogEntity(); // Assume a constructor or builder pattern
        BlogEntity updatedBlogDetails = new BlogEntity(); // Assume updated details

        when(blogRepository.existsById(blogId)).thenReturn(true);
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(existingBlog));

        when(modelMapper.getConfiguration()).thenReturn(configuration); // Return the mock Configuration when getConfiguration is called

        doNothing().when(modelMapper).map(any(BlogEntity.class), any(BlogEntity.class));

        blogService.updateBlog(blogId, updatedBlogDetails);

        verify(blogRepository).save(existingBlog); // Verify save was called
    }

    @Test
    void updateBlogNotExist() {
        Long blogId = 1L;
        BlogEntity updatedBlogDetails = new BlogEntity(); // Assume updated details

        when(blogRepository.existsById(blogId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> blogService.updateBlog(blogId, updatedBlogDetails));
    }

    @Test
    void createBlog_UserNotExist_ThrowsEntityNotFoundException() {
        // Arrange
        BlogEntity blogEntity = new BlogEntity();
        UserEntity user = new UserEntity();
        user.setId(1L);
        blogEntity.setUser(user);
        blogEntity.setTitle("Test Title");
        blogEntity.setBody("Test Body");

        when(userService.isUserExisted(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> blogService.createBlog(blogEntity));
    }
    @Test
    void deleteBlogSuccess() {
        Long blogId = 1L;

        when(blogRepository.existsById(blogId)).thenReturn(true);
        doNothing().when(blogRepository).deleteById(blogId);

        blogService.deleteBlog(blogId);

        verify(blogRepository).deleteById(blogId); // Verify delete was called
    }

    // Delete Blog - Blog Does Not Exist
    @Test
    void deleteBlogNotExist() {
        Long blogId = 1L;

        when(blogRepository.existsById(blogId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> blogService.deleteBlog(blogId));
    }
    @Test
    void getBlogsSuccess() {
        List<BlogEntity> blogEntities = Arrays.asList(new BlogEntity(), new BlogEntity()); // Assume a list of blogs

        when(blogRepository.findAll()).thenReturn(blogEntities);

        List<BlogEntity> blogs = blogService.getBlogs();

        assertNotNull(blogs);
        assertEquals(2, blogs.size()); // Assuming the list contains 2 blogs
        verify(blogRepository).findAll(); // Verify findAll was called
    }

    // Get All Blogs - Exception Scenario
    @Test
    void getBlogsException() {
        when(blogRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> blogService.getBlogs());
    }
}
